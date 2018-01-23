package com.quadrant.ws;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.quadrant.FDCameraDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CameraLiveViewer {
	private static final Logger logger = LoggerFactory.getLogger(CameraLiveViewer.class);
	private WebSocketSession websocket;
	private String mac;
	private FDCameraDataHandler FDCameraDataHandler;
	private ScheduledExecutorService service;
	private VideoTimerTask task = null;
	public CameraLiveViewer(FDCameraDataHandler fdHandler , WebSocketSession websocket, String mac) {
		super();
		this.websocket = websocket;
		this.mac = mac;
		this.FDCameraDataHandler = fdHandler;
	}

	public WebSocketSession getWebsocket() {
		return websocket;
	}

	public void setWebsocket(WebSocketSession websocket) {
		this.websocket = websocket;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	

	public void startViewing() {
		service = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setNameFormat("WebViewer-thread-%d").setDaemon(true).setPriority(Thread.NORM_PRIORITY + 2).build());
		task = new VideoTimerTask(FDCameraDataHandler , websocket , mac);
		service.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);
		
	}
	
	
	public void stopViewing() {
		logger.info("stopstop##################################");
		task.stop();
		service.shutdown();
	}
}
