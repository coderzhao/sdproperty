package com.quadrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author bean
 *@deprecated
 */
@Component
public class IPCServer extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(IPCServer.class);
	protected ServerSocket ss =null;
	private int port = 8100;
	
	@Autowired
	private FDCameraDataHandler FDCameraDataHandler;

	public FDCameraListener getListener() {
		return FDCameraDataHandler;
	}

	public void setListener(FDCameraDataHandler listener) {
		this.FDCameraDataHandler = listener;
	}

	public IPCServer() {}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		createServerSocket();
		startListen();
	}

	
	public void startListen() {
		if (ss != null) {
			try {
				while (true) {
					Socket s = ss.accept();
					logger.info("Camera {} connected." , s.getRemoteSocketAddress().toString());
					new CameraClientThread(s, (FDCameraListener)FDCameraDataHandler).start();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				logger.info("IPCServer stop!");
			}
		}else {
			logger.info("IPCServer startup failed.");
		}
	}

	public void createServerSocket() {
		
		try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			logger.error(e1.getMessage() , e1);
			e1.printStackTrace();
		}
		try {
			InetAddress ia = InetAddress.getByName(null);
			logger.info("IPCServer@" + ia + " start listen on port = {}",port);
		} catch (UnknownHostException e1) {
			logger.error(e1.getMessage() , e1);
			e1.printStackTrace();
		}
		
	}
}
