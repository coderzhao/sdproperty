package com.quadrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class CameraServerBroadcaster {
	private static final Logger logger = LoggerFactory.getLogger(CameraServerBroadcaster.class);
	
	private int port =13517;
	private String address = "255.255.255.255";
//	private String ethName = "enp4s0";
	private static final String broadcast_passwd = "SeedonkCameraLocalNetworkInformation";
	
//	public String getEthName() {
//		return ethName;
//	}
//
//	public void setEthName(String ethName) {
//		this.ethName = ethName;
//	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void broadcast() {
		logger.info("DatagramChannel eth={}, port={}",new String[]{address , String.valueOf(port)});
		try (DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)) {
			if (datagramChannel.isOpen()) {
				// get the network interface used for 
//				NetworkInterface networkInterface = NetworkInterface.getByName(getEthName());
				// set some options
				datagramChannel.setOption(StandardSocketOptions.SO_BROADCAST,true);
				datagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				// bind the channel to the local address
				datagramChannel.bind(new InetSocketAddress(getPort()));

				ByteBuffer token = ByteBuffer.wrap(broadcast_passwd.getBytes());
				datagramChannel.send(token, new InetSocketAddress(InetAddress.getByName(getAddress()), getPort()));
				token.flip();
//				logger.debug("CameraInstanceDetector message sent!");
			} else {
//				logger.debug("CameraInstanceDetector channel cannot be opened! port ={} , address ={} " , port , address);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println(ex);
		}
	}
	
	public static void main(String[] args) {
		CameraServerBroadcaster detector = new CameraServerBroadcaster();
		detector.broadcast();
	}
}
