package com.training.modules.quartz.tasks.utils;

import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.service.TaskService;

/**
 * 定时任务-公共Service
 * @author kele
 * @version 2016年9月29日
 */
public class CommonService {

	protected static RedisClientTemplate redisClientTemplate;
	protected static TaskService taskService ;
	
	static{
		redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
		taskService = (TaskService) BeanUtil.getBean("taskService");
	}
}
