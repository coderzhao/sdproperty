package com.quadrant.ws;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quadrant.FDCameraDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class CameraWebSocketHandler extends TextWebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(CameraWebSocketHandler.class);
	@Autowired
	FDCameraDataHandler FDCameraDataHandler;
	
//	VideoViewerManager manager = VideoViewerManager.getInstance();
//	Map<String , CameraLiveViewer> viewers = new ConcurrentHashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("Session {} connected",session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		System.out.println(message.getPayload());
//		String prjFolder = "/Users/bean/Documents/seedonk/china-quadrant/sdPropertyCMS/CMSWeb";
		try {
			
			
			String commandStr = message.getPayload();
			if (Strings.isNullOrEmpty(commandStr))
				session.sendMessage(new TextMessage("Invalid command"));
			LiveViewCommand cmd = getGson().fromJson(commandStr, LiveViewCommand.class);
			if ("StartLiveStream".equals(cmd.getCmd())) {
				CameraLiveViewer viewer = new CameraLiveViewer(FDCameraDataHandler ,session , cmd.getMac());
				VideoViewerManager.getInstance().put(session.getId(), viewer);
				viewer.startViewing();
			} else if ("StopLiveStream".equals(cmd.getCmd())){
				CameraLiveViewer viewer = VideoViewerManager.getInstance().remove(session.getId());
				if(viewer != null) viewer.stopViewing();
			}else {
				logger.info("Invalid web socket command {}" + commandStr);
				session.sendMessage(new TextMessage("Invalid command"));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			session.sendMessage(new TextMessage(e.getMessage()));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		CameraLiveViewer viewer = VideoViewerManager.getInstance().remove(session.getId());
		if(viewer != null) viewer.stopViewing();
	}
	 private Gson getGson() {
 		GsonBuilder builder = new GsonBuilder();;
 		return builder.create();
 }
}
