package com.training.modules.ec.entity;

import java.util.List;

import com.training.common.persistence.TreeEntity;

/**
 * 物流模板区域计费表
 * @author dalong
 *
 */
public class PdTemplatePrice extends TreeEntity<PdTemplatePrice> {
	
	
	private static final long serialVersionUID = 1L;
	private Integer priceId;//自增
	private Integer templateId;		//物流模板id
	private Integer firstWeight;	//首重
	private Double firstPrice;		//首重价格
	private Integer addWeight;		//续重
	private Double addPrice;		//续重价格
	private int isDefault;			//是否默认（0：否；1：是） 默认位0
	private String areaIds;
	
	private List<PdTemplateArea> templateAreas;
	
	public String getAreaIds() {
		return areaIds;
	}
	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	public List<PdTemplateArea> getTemplateAreas() {
		return templateAreas;
	}
	public void setTemplateAreas(List<PdTemplateArea> templateAreas) {
		this.templateAreas = templateAreas;
	}
	
	public Integer getPriceId() {
		return priceId;
	}
	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public Integer getFirstWeight() {
		return firstWeight;
	}
	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}
	public Double getFirstPrice() {
		return firstPrice;
	}
	public void setFirstPrice(Double firstPrice) {
		this.firstPrice = firstPrice;
	}
	public Integer getAddWeight() {
		return addWeight;
	}
	public void setAddWeight(Integer addWeight) {
		this.addWeight = addWeight;
	}
	public Double getAddPrice() {
		return addPrice;
	}
	public void setAddPrice(Double addPrice) {
		this.addPrice = addPrice;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	@Override
	public PdTemplatePrice getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(PdTemplatePrice parent) {
		// TODO Auto-generated method stub
		
	}
}
