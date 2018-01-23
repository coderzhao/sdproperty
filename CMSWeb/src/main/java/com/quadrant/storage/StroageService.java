package com.quadrant.storage;

public interface StroageService {
	public void save(String key, byte[] object);
	public byte[] get(String key);
	public void delete(String key);
}
