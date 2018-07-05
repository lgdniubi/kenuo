package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;
/**
 * 
 * @author: jingfeng
 * @date 2018年5月2日下午3:18:53
 * @Description:供应链--协议模板
 */
public class ProtocolType extends DataEntity<ProtocolType>{
	private static final long serialVersionUID = 1L;
	private String name;			//模板类型名称
	private int status;				//模板类型:1启用,2停用
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
