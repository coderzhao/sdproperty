package com.quadrant;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.note.cms.common.Constant;
import com.note.cms.handler.WsMessStore;
import com.note.cms.service.GuestService;
import com.note.cms.service.IpcService;
import com.note.cms.service.SnapshotFaceService;
import com.note.cms.service.SnapshotService;
import com.quadrant.fr.NTechFRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Scope("singleton")
public class FDCameraDataHandler implements FDCameraListener {
	private static final Logger logger = LoggerFactory.getLogger(FDCameraDataHandler.class);
	private Map<String,CameraSession> cameras = null;//key:mac 
	private Map<String,String> ipMac = null;//key: ip , value: mac
	private ExecutorService faceReconPool = null;

	public static Set<String> mac=new HashSet<>();
	
	private static final int IDLE_TIMEOUT_MILLIS = 100000;//100 seconds
	@Autowired
	NTechFRService nTechFRService;
	
	@Autowired
    IpcService ipcService;
	
	@Autowired
    SnapshotService snapshotService;
	
	@Autowired
    SnapshotFaceService snapshotFaceService;
	
	@Autowired
    GuestService guestService;
	
	@Autowired
	SysConfiguration sysConfiguration;
	
	ThreadLocal<Integer> counts = new ThreadLocal<>();
	
	public FDCameraDataHandler() {
		cameras = new ConcurrentHashMap<>();
		ipMac = new ConcurrentHashMap<>();
		faceReconPool = Executors.newFixedThreadPool(Constant.THREADS, new ThreadFactoryBuilder().setNameFormat("FDCameraDataFaceRecognition-thread-%d").setPriority(5).setDaemon(true).build());
	}
	
	
	
	@Override
	public void OnCameraData(String clientAddress,FDCameraData data) {

		Integer count = counts.get();
		if(count == null) {
			count = 1;
		}
		else {
			count++;
			
		}
		counts.set(count);
//		logger.info(data.toString());
		CameraSession session = cameras.get(data.mStrMac);
		if(session == null) {
			session = new CameraSession(data ,nTechFRService,ipcService,snapshotService,snapshotFaceService,guestService,sysConfiguration);
			cameras.put(data.mStrMac, session);
			logger.info("new CameraSession for {}",data.mStrMac);
		}
		session.setOnline(true);
		ipMac.put(clientAddress, data.mStrMac);
		if((count % sysConfiguration.getCameraDataThreshold()) == 0 ) {//量減1/5
			logger.debug("Put to pool -" + data.toString());
			counts.set(0);
			if(sysConfiguration.isDrawFRBox()) {
				session.analyse(data, true, true);
			}else {
				faceReconPool.execute(new FDCameraDataProcessor(session , data));
				session.notifyViewers(data);
			}
		}

		
	}


	@Override
	public void onSocketSessionClose(String clientIp) {
		String mac = ipMac.get(clientIp);
		logger.info("FDCameraDataHandler offline socket session, mac={} , ip={}" , mac, clientIp);
		try{
			CameraSession camera = cameras.get(mac);	
			if(camera != null) camera.setOnline(false);
		}catch(Exception e) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			CameraSession camera = cameras.get(mac);	
			if(camera != null) camera.setOnline(false);
		}
		
	}
	
	public void setCameraSessionOffline(String clientIp) {
		String mac = ipMac.get(clientIp);
		logger.info("FDCameraDataHandler check offline channel, mac={} , ip={}", mac, clientIp);
		try {
			setOffline(mac);
		} catch (Exception e) {
			try {
				Thread.sleep(500);
				setOffline(mac);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
	
	private void setOffline(String mac) throws Exception {
		CameraSession camera = cameras.get(mac);	
		if(camera != null) {
			if( (System.currentTimeMillis() - camera.getLatestOnline()) > IDLE_TIMEOUT_MILLIS ) {//idle timeout 120
				camera.setOnline(false);
				logger.info("FDCameraDataHandler {} offline.", mac);
			}
		}
	}
	
	public CameraSession getCameraSession(String mac) {
		return cameras.get(mac);
	}
	
	public Set<String> getOnlineCameras(){
		Set<String> result = new HashSet<>();
		for(CameraSession camera : cameras.values()) {
			if(camera.isOnline()) result.add(camera.getMac());
		}
		return result;
	}
	
	public boolean isCameraOnline(String mac) {
		return cameras.get(mac) != null;
	}
	
	public String getKeys() {
		return cameras.keySet().toString();
	}
	
}
