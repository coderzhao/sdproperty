package com.quadrant.ws;

public class LiveViewCommand {
	private String mac;
	private String cmd;//StartLiveStream , StopLiveStram
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	
}
