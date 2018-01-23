package com.quadrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FDCameraDataProcessor implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(FDCameraDataProcessor.class);
	private CameraSession session ;
	private FDCameraData data;
	
	public FDCameraDataProcessor(CameraSession session , FDCameraData data) {
		this.session = session;
		this.data = data;
	}
	
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		session.analyse(data , false ,false);
//		logger.info("FR cost = {} millis" , System.currentTimeMillis() - start );
//		session.addLast(data);
	}

}
