package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 规则参数实体类
 * @author kele
 * @version 2016-10-19
 */
public class RuleParam extends DataEntity<RuleParam>{
 
	private static final long serialVersionUID = 1L;
	
	private String paramKey;		//参数Key
	private String paramValue;		//参数Value
	
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
}
