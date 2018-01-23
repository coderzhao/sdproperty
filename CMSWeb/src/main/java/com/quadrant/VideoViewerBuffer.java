package com.quadrant;

import java.util.ArrayDeque;

public class VideoViewerBuffer implements VideoViewer {

	private ArrayDeque<FDCameraData> buffer = null;
	private int maxSize = 10;
	private String id;
	public VideoViewerBuffer(String id) {
		this.id = id;
		buffer = new ArrayDeque<>(maxSize);
	}
	
	
	public VideoViewerBuffer(String id ,int maxSize) {
		this.id = id;
		this.maxSize = maxSize;
		buffer = new ArrayDeque<>(maxSize);
	}


	@Override
	public void notify(FDCameraData d) {
		if(buffer.size() >= maxSize) buffer.poll();
		buffer.add(d);	

	}

	@Override
	public FDCameraData poll() {
		return buffer.poll();
	}


	@Override
	public String getId() {
		return id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VideoViewerBuffer other = (VideoViewerBuffer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
