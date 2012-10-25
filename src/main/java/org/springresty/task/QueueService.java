package org.springresty.task;

import java.util.concurrent.TimeUnit;

/**
 * 队列服务接口
 * 
 * @author yongboy
 * @time 2012-3-20
 * @version 1.0
 */
public interface QueueService {

	/**
	 * 设置数据
	 * 
	 * @param conn
	 */
	
	Long push(String key, byte[] value);

	/**
	 * 获取队列元素
	 * 
	 * @param conn
	 */
	byte[] pop(String key);
	
	byte[] pop(String key, long timeout, TimeUnit unit);
}