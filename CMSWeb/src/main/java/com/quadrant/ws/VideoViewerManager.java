package com.quadrant.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VideoViewerManager {
	private static Map<String , CameraLiveViewer> viewers = new ConcurrentHashMap<>();
	private static VideoViewerManager singleton = new VideoViewerManager();
	private VideoViewerManager () {}
	public static VideoViewerManager getInstance() {
		return singleton;
	}
	public void put(String sessionId, CameraLiveViewer viewer) {
		viewers.put(sessionId, viewer);
	}
	
	public CameraLiveViewer remove(String sessionId) {
		return viewers.remove(sessionId);
	}
	
	public boolean stopViewingByMac(String mac) {
		boolean result = false;
		for(CameraLiveViewer viewer : viewers.values()) {
			if(viewer.getMac().equals(mac)) {
				viewer.stopViewing();
				result = true;
			}
		}
		return result;
	}
}
