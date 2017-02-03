package com.training.modules.ec.entity;

import java.util.List;

import com.training.common.persistence.DataEntity;

/**
 * 培训规则参数类
 * @author kele
 * @version 2016-8-12
 */
public class MtmyRuleParam extends DataEntity<MtmyRuleParam>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String paramKey;//规则参数Key
	private String paramValue;//规则参数Value
	private String paramExplain;//规则参数说明
	private String paramType;//规则参数类型
	private int sort;//排序
	private int paramUplimit;	//上限
	
	//private Date beginDate;//开始时间
	//private Date endDate;//结束时间
	
	private MtmyRuleParamType mtmyRuleParamType;//规则参数类型
	private List<MtmyRuleParamType> ruleparamtypelist;//所有参数类型列表
	
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getParamExplain() {
		return paramExplain;
	}
	public void setParamExplain(String paramExplain) {
		this.paramExplain = paramExplain;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public List<MtmyRuleParamType> getRuleparamtypelist() {
		return ruleparamtypelist;
	}
	public void setRuleparamtypelist(List<MtmyRuleParamType> ruleparamtypelist) {
		this.ruleparamtypelist = ruleparamtypelist;
	}
	public MtmyRuleParamType getMtmyRuleParamType() {
		return mtmyRuleParamType;
	}
	public void setMtmyRuleParamType(MtmyRuleParamType mtmyRuleParamType) {
		this.mtmyRuleParamType = mtmyRuleParamType;
	}
	public int getParamUplimit() {
		return paramUplimit;
	}
	public void setParamUplimit(int paramUplimit) {
		this.paramUplimit = paramUplimit;
	}
}
