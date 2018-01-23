package com.quadrant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 
 * @author bean
 *@deprecated
 */
@Component
public final class IPCServerStartup {
	
	private int port = 8100;
	@Autowired
	private IPCServer server = null;
	
	@Autowired
	public FDCameraDataHandler FDCameraDataHandler = null;
	
	public IPCServerStartup() {

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	public FDCameraDataHandler getListener() {
		return FDCameraDataHandler;
	}

	public void setListener(FDCameraDataHandler listener) {
		this.FDCameraDataHandler = listener;
	}

	public void startup() {

//		server = new IPCServer();
		server.setPort(port);
		server.setListener(FDCameraDataHandler);
		server.start();
		
	}

	public static void main(String[] args) {
		IPCServerStartup ipc = new IPCServerStartup();
		ipc.setPort(8100);
		
	}
}
