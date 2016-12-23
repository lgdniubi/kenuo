package com.training.modules.quartz.utils;

import java.util.Random;
/**
 * redis key锁类
 * @author QJL
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.modules.quartz.service.RedisClientTemplate;

public class RedisLock {
	private static Log log = LogFactory.getLog(RedisLock.class);
	//加锁值
	private static final String LOCKED = "TRUE";
	private static final long ONE_MILLI_NANOS = 1000000L;
	//默认超时时间（毫秒）
	private static final long DEFAULT_TIME_OUT = 500;
	
	private static final Random r = new Random();
	//锁的超时时间（秒），过期删除
	private static final int EXPIRE = 1 * 60;
	
	private RedisClientTemplate redisClientTemplate;
	private String key;
	private boolean locked = false; //锁的状态标识
	
	public RedisLock(RedisClientTemplate redisClientTemplate, String key ){
		this.redisClientTemplate = redisClientTemplate;
		this.key = key;
	}
	
	public boolean lock(long timeout){
		
		long nano = System.nanoTime();
		timeout *= ONE_MILLI_NANOS;
		
		try {
			while((System.nanoTime()-nano) < timeout){
				if(this.redisClientTemplate.setnx(key, LOCKED) == 1){
					this.redisClientTemplate.expire(key, EXPIRE);
					locked = true;
					return locked;
				}
				//短暂休眠，nano避免出现活锁
				Thread.sleep(3,r.nextInt(500));
			}
		} catch (Exception e) {
			log.info("RedisLock.lock 发生了异常");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean lock(){
		return lock(DEFAULT_TIME_OUT);
	}
	//无论是否加锁成功。必须调用
	public void unlock(){
		log.info("删除锁");
		if(locked)this.redisClientTemplate.del(key);
	}
}
