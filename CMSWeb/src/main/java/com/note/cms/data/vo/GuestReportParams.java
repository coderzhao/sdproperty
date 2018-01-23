package com.note.cms.data.vo;

import java.sql.Timestamp;

public class GuestReportParams {
	private Integer guestRoleId = 1;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer ipcId = null;
	
	public Integer getGuestRoleId() {
		return guestRoleId;
	}
	public void setGuestRoleId(Integer guestRoleId) {
		this.guestRoleId = guestRoleId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Integer getIpcId() {
		return ipcId;
	}
	public void setIpcId(Integer ipcId) {
		this.ipcId = ipcId;
	}
	
	
	
}
