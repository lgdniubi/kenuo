package com.training.common.track.thread;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.common.track.config.TrackConfig;

/**
 * 类名称：	TrackThread
 * 类描述：	埋点线程主方法
 * 创建人： 	bigdata
 * 创建时间：    	2018年4月25日16:53:13
 */
public class TrackThread implements Runnable{
	
	private static Log log = LogFactory.getLog(TrackThread.class);

	private Map<String, Object> paramMap;
	
	public TrackThread(Map<String, Object> paramMap) {
		super();
		this.paramMap = paramMap;
	}
	
	@Override
	public void run() {
		String methodName = (String) paramMap.get("METHOD_NAME");
		log.info("[埋点线程主方法]："+methodName);
		try {
			// 睡眠 1秒
			Thread.sleep(1000);
			// 执行方法
			TrackConfig.execute(methodName, paramMap);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
