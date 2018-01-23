package com.note.cms.tools.impl;

import com.note.cms.tools.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service("CacheService")
public class CacheServiceImpl implements CacheService {
	private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	private static String redisCode = "utf-8";
	private final static int DEFAULT_DELAY = (12*3600);//默认过期时间为12个小时

	@Autowired
	RedisTemplate mRedisTemplate;


	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@Override
	public void set(String key, Object value, long liveTime) {
		try {
			mRedisTemplate.opsForValue().set(key, value);
			mRedisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
		}catch (Exception e){

		}
	}

	/**
	 * @param key
	 * @param value
	 */
	@Override
	public void set(String key, Object value) {
		try {
			this.set(key, value, DEFAULT_DELAY);
		}catch (Exception e){
			return ;
		}
	}

	@Override
	public Object get(String key) {

		try{
			return mRedisTemplate.opsForValue().get(key);
		}catch (Exception e){
			return null;
		}
	}


	/**
	 * @return
	 */
	@Override
	public void flushDB() {
		try {
			mRedisTemplate.execute(new RedisCallback() {
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.flushDb();
					return "ok";
				}
			});
		}catch (Exception e) {
		}
	}


	/**
	 * @return
	 */
	@Override
	public void ping() {
		try {
			mRedisTemplate.execute(new RedisCallback() {
				public String doInRedis(RedisConnection connection) throws DataAccessException {

					return connection.ping();
				}
			});
		}catch (Exception e){

		}
	}

	/**
	 * 具体模块
	 */
//	@Override
//	public void setDeviceReportAndControl(String uid, DeviceReportAndControlCache obj){
//		set(TAG_DEVICE_REPORT_CONTROL_ID+uid, obj);
//	}
//
//	public DeviceReportAndControlCache getDeviceReportAndControl(String uid){
//		return (DeviceReportAndControlCache)get(TAG_DEVICE_REPORT_CONTROL_ID+uid);
//	}

}
