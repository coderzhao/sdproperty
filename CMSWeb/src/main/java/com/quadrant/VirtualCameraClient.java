package com.quadrant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;

public class VirtualCameraClient {
	public static void main(String[] args) throws IOException, InterruptedException {
//		final int DEFAULT_PORT = 8100;
//		final String IP = "192.168.0.4";
//		SocketChannel socketChannel = SocketChannel.open();
//		if (socketChannel.isOpen()) {
//			socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
//			socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
//			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
//			socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);
//
//			socketChannel.connect(new InetSocketAddress(IP, DEFAULT_PORT));
//			ByteBuffer buffer = ByteBuffer.allocateDirect(176);
//			buffer.put("QCFD".getBytes("ASCII"));
//			ByteBuffer totalLen = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(176);
//			buffer.put(totalLen);
//
//
//
//		}
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					System.out.println("I am thread 1");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		int i=0;
		while(true){
			System.out.println("thread 1 status is active:"+thread.isAlive());
			Thread.sleep(1000);
			for(;i<5;i++){
//				thread.
			}
		}
	}
}
