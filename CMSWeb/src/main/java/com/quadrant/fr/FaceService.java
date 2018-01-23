package com.quadrant.fr;

import com.quadrant.FDCameraData;

public interface FaceService {
	public CreatedFace addFace(String meta, byte[] image);
	public void updateFace();
	public void deleteFace();
	public NTechIdentifyResponse analyze(FDCameraData fd);
	public void detect();
}
