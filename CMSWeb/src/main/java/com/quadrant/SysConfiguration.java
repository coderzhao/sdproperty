package com.quadrant;

import com.note.cms.common.Constant;
import org.springframework.stereotype.Component;

@Component
public class SysConfiguration {
	private String snapshotFolder;
	private boolean detectFace = false;
	private boolean saveSnapshot = true;
	private boolean drawFRBox = false;
	private boolean saveFR2DB = true;
	private int cameraDataThreshold = Constant.FR_CAMERA_DATA_THRESHOLD;
	public String getSnapshotFolder() {
		if(snapshotFolder == null)
			snapshotFolder = Constant.IMAGE_PATH;
		return snapshotFolder;
	}

	public void setSnapshotFolder(String snapshotFolder) {
		this.snapshotFolder = snapshotFolder;
	}

	public boolean isDetectFace() {
		return detectFace;
	}

	public void setDetectFace(boolean detectFace) {
		this.detectFace = detectFace;
	}

	public boolean isSaveSnapshot() {
		return saveSnapshot;
	}

	public void setSaveSnapshot(boolean saveSnapshot) {
		this.saveSnapshot = saveSnapshot;
	}

	public boolean isDrawFRBox() {
		return drawFRBox;
	}

	public void setDrawFRBox(boolean drawFRBox) {
		this.drawFRBox = drawFRBox;
	}

	public boolean isSaveFR2DB() {
		return saveFR2DB;
	}

	public void setSaveFR2DB(boolean saveFR2DB) {
		this.saveFR2DB = saveFR2DB;
	}

	public int getCameraDataThreshold() {
		return cameraDataThreshold;
	}

	public void setCameraDataThreshold(int cameraDataThreshold) {
		this.cameraDataThreshold = cameraDataThreshold;
	}

	
	
}
