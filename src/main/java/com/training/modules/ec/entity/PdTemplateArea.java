package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 模板表
 * @author dalong
 *
 */
public class PdTemplateArea extends TreeEntity<PdTemplateArea> {
	
	
	private static final long serialVersionUID = 1L;
	private Integer templateAreaId;
	private int priceId;
	private String areaId;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTemplateAreaId() {
		return templateAreaId;
	}
	public void setTemplateAreaId(Integer templateAreaId) {
		this.templateAreaId = templateAreaId;
	}
	public String getAreaId() {
		return areaId;
	}
	public int getPriceId() {
		return priceId;
	}
	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Override
	public PdTemplateArea getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(PdTemplateArea parent) {
		// TODO Auto-generated method stub
		
	}
}
