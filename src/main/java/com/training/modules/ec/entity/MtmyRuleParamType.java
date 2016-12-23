package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 培训规则参数类型
 * @author kele
 * @version 2016-8-12
 */
public class MtmyRuleParamType extends DataEntity<MtmyRuleParamType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String typeName;//类型名称
	private String aliaName;//别名

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAliaName() {
		return aliaName;
	}

	public void setAliaName(String aliaName) {
		this.aliaName = aliaName;
	}
	
}
