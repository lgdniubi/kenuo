package com.training.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext acxt ;
	@Override
	public void setApplicationContext(ApplicationContext cxt) throws BeansException {
		acxt = cxt;
	}
	
	public static Object getBean(String beanName){
		if(acxt == null){
			return null;
		}
		return acxt.getBean(beanName);
	}
}
