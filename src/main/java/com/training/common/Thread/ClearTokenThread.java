package com.training.common.Thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.service.RedisClientTemplate;
/**
 * 版本删除菜单时删除用户的所有token
 * @author: jf
 * @date 2018年6月25日下午2:18:03
 */
public class ClearTokenThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(ClearTokenThread.class);
	private List<String> uids;
	private RedisClientTemplate redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
	public ClearTokenThread(List<String> uids) {
		this.uids = uids;
	}
	@Override
	public void run() {
		try {
			if(uids != null && uids.size() > 0){
				for(String uid : uids){
					this.redisClientTemplate.del("UTOKEN_"+uid);
				}
			}
			logger.info("版本删除菜单时清除token，所有的用户id"+uids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
