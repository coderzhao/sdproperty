package com.note.cms.tools;

import com.note.cms.service.BaseService;


/**
 * @des 缓存api接口，目前用redis做缓存服务器
 * @Date 2016-02-11
 *
 */


public interface CacheService extends BaseService{
	final public static String LIST_PARENT_AGENT = "LIST_PARENT_AGENT";//存放所有可以当父代理商的代理商列表,也就是isEnd ＝ 0的

	public final static String TAG_USER = "userid_";
	public final static String TAG_USER_ID = "user_id_";
	public final static String TAG_SESSION = "session_";//cache相关的key的前缀， 用于session缓存
	public final static String TAG_SENSOR_TYPE = "sensortype_";//sensortype表缓存
	public final static String TAG_DEVICE_ID = "device_id_";
	public final static String TAG_SENSOR_ID = "sensor_id_";

	public  final static String TAG_DEVICE_REPORT_CONTROL_ID = "device_rc_id_";


	public final static long LIVETIME = 3600;//redis缓存存活时间，单位s
	public final static long LIVETIME_CATEGORY = 3600;
	/**
	 * 添加key value 并且设置存活时间
	 *
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	public abstract void set(String key, Object value, long liveTime);

	/**
	 * 添加key value
	 *
	 * @param key
	 */
	public abstract void set(String key, Object K);


	/**
	 * 根据key获取缓存的对象
	 *
	 * @param key
	 */
	public abstract Object get(String key);


	/**
	 * 清空redis 所有数据
	 *
	 * @return
	 */
	public abstract void flushDB();

	/**
	 * 检查是否连接成功
	 *
	 * @return
	 */
	public abstract void ping();

	/**
	 * 具体模块的缓存
	 */
//	public void setDeviceReportAndControl(String uid, DeviceReportAndControlCache obj);
//	public DeviceReportAndControlCache getDeviceReportAndControl(String uid);
}
