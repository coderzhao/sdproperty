package com.quadrant;

public class FaceBox {
	private int x;
	private int y;
	private int w;
	private int h;
	private String meta;
	private String name;
	
	public FaceBox(int x, int y, int w, int h, String meta) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.meta = meta;
		if(meta.startsWith("QT")) {
//			String code = meta.substring(0,6);
			name = meta.substring(6);
		}
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public String getMeta() {
		return meta;
	}
	public void setMeta(String meta) {
		this.meta = meta;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FaceBox [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", meta=" + meta + ", name=" + name + "]";
	}
	
	
}
