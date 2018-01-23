package com.note.cms.data.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class GuestClickInOutVo {
	private Integer id;
	private String code;
	private String name;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
	private Date clickIn;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Shanghai")
	private Date clickOut;
	
	public GuestClickInOutVo(){}
	

	public GuestClickInOutVo(Integer id, String code, String name, Date clickIn, Date clickOut) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.clickIn = clickIn;
		this.clickOut = clickOut;
		
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getClickIn() {
		return clickIn;
	}
	public void setClickIn(Date clickIn) {
		this.clickIn = clickIn;
	}
	public Date getClickOut() {
		return clickOut;
	}
	public void setClickOut(Date clickOut) {
		this.clickOut = clickOut;
	}


	@Override
	public String toString() {
		return "GuestClickInOutVo [code=" + code + ", name=" + name + ", clickIn=" + clickIn + ", clickOut=" + clickOut
				+ "]";
	}
	
	
	
}
