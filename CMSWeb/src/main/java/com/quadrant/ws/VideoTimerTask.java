package com.quadrant.ws;

import com.quadrant.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.TimerTask;

public class VideoTimerTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(VideoTimerTask.class);
	private WebSocketSession wsClient;
	private String mac = null;
	private FDCameraDataHandler handler;
	private VideoViewer buffer = null;
	private String remoteClientIp = null;
	private int count = 0;
	private long start = System.currentTimeMillis();
	public VideoTimerTask(FDCameraDataHandler handler, WebSocketSession wsClient , String mac) {
		this.handler = handler;
		this.wsClient = wsClient;
		this.mac = mac;
		this.buffer = new VideoViewerBuffer(wsClient.getId());
		CameraSession cs = handler.getCameraSession(mac);
		if(cs!= null) cs.registerVideoViewer(buffer);
		remoteClientIp = wsClient.getRemoteAddress().getAddress().getHostAddress();
	}
	
	public void registerVideoViewer(CameraSession cs) {
		cs.registerVideoViewer(buffer); // register to new CameraSession after camera reconnected
	}
	
	private void calSendingRate() {
		if((count % 100) == 0) {
			long ttlTime = (System.currentTimeMillis() - start)/1000;
			logger.info("camera [{}] sending image to browser  rate = {} f/s , ttl={}" ,new String[] {mac , String.valueOf(count/ttlTime) , String.valueOf(count)});
		}
	}
	
	@Override
	public void run() {
		try {
				CameraSession cs = handler.getCameraSession(mac);
		
				if(cs != null && cs.isOnline()) {
					registerVideoViewer(cs);
//					FDCameraData fd = cs.pollFirst();
					FDCameraData fd = buffer.poll();
//					logger.info(remoteClientIp +" Poll FDCameraData from {} , fd is null {} ",mac , fd == null);
		
					try {
						if(fd == null) return;
//						logger.info("Sending base64 image from {} to {} ",mac , remoteClientIp);
						wsClient.sendMessage(new TextMessage(encodeBase64(fd.mJpgData)));
						count++;
						calSendingRate();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}catch(Throwable e){
			//ignore any error to avoid scheduler service stop
			logger.error(e.getMessage());
		}
	}
	
	private String encodeBase64(byte[] img) throws UnsupportedEncodingException{
		return new String(Base64.encodeBase64(img),"UTF-8");
	}

	public void stop() {
		CameraSession cs = handler.getCameraSession(mac);
		if(cs != null)	cs.unregisterVideoViewer(buffer);
//		if(cs != null) cs.setBindViwer(false);
	}
	
	
	
}
