package com.quadrant;

public interface VideoViewer {
	public String getId();
	public void notify(FDCameraData d);
	public FDCameraData poll();
}
