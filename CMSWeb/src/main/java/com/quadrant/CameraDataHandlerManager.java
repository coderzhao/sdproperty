package com.quadrant;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Not used
 * @author bean
 *@deprecated
 */
public class CameraDataHandlerManager implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	public FDCameraDataHandler getHandler() {
		return applicationContext.getBean("FDCameraDataHandler",FDCameraDataHandler.class);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
