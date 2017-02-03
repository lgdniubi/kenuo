package com.training.modules.ec.entity;

import java.util.List;

import com.training.common.persistence.TreeEntity;
import com.training.modules.sys.entity.Area;

/**
 * 模板表
 * @author dalong
 *
 */
public class PdTemplate extends TreeEntity<PdTemplate> {
	
	
	private static final long serialVersionUID = 1L;
	private Integer templateId;
	private String templateName;
	private String templateAreaId;
	private String templateAreaName;
	private String templateAddress;
	private Integer deliveryTime;
	private Integer isFree;
	private Integer valuationType;
	private Integer houseId;
	
	
	private List<PdTemplatePrice> templatePrices;
	//----------------------------------------------------
	private String areaCode; 	// 区域编码
	private String areaName;	//区域名字
	private String areaId;		//区域id
	private String areaPid;		//区域父id
	private String type;		//类型
	private Integer sortype;	//运送目的地，集合区分
	private List<PdTemplate> cityList;//城市集合
	//-------------------------------------------------
	private Integer firstWeightsDefault;
	private Double firstPricesDefault;
	private Integer addWeightsDefault;
	private Double addPricesDefault;
	private int isDefault;
	//--------------------------------------------
	private List<Integer> firstWeights;//首重（单位：公斤）
	private List<Double> firstPrices;//首重单价
	private List<Integer> addWeights;//续重
	private List<Double> addPrices;//续重价格
	private List<String> areaIds;
	//---------------------模板列表页面显示需要数据---------------------------
	private Area area;
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<Integer> getFirstWeights() {
		return firstWeights;
	}
	public void setFirstWeights(List<Integer> firstWeights) {
		this.firstWeights = firstWeights;
	}
	public List<Double> getFirstPrices() {
		return firstPrices;
	}
	public void setFirstPrices(List<Double> firstPrices) {
		this.firstPrices = firstPrices;
	}
	public List<Integer> getAddWeights() {
		return addWeights;
	}
	public void setAddWeights(List<Integer> addWeights) {
		this.addWeights = addWeights;
	}
	public List<Double> getAddPrices() {
		return addPrices;
	}
	public void setAddPrices(List<Double> addPrices) {
		this.addPrices = addPrices;
	}
	public String getTemplateAreaName() {
		return templateAreaName;
	}
	public void setTemplateAreaName(String templateAreaName) {
		this.templateAreaName = templateAreaName;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
	public Integer getFirstWeightsDefault() {
		return firstWeightsDefault;
	}
	public void setFirstWeightsDefault(Integer firstWeightsDefault) {
		this.firstWeightsDefault = firstWeightsDefault;
	}
	public Double getFirstPricesDefault() {
		return firstPricesDefault;
	}
	public void setFirstPricesDefault(Double firstPricesDefault) {
		this.firstPricesDefault = firstPricesDefault;
	}
	public Integer getAddWeightsDefault() {
		return addWeightsDefault;
	}
	public void setAddWeightsDefault(Integer addWeightsDefault) {
		this.addWeightsDefault = addWeightsDefault;
	}
	public Double getAddPricesDefault() {
		return addPricesDefault;
	}
	public void setAddPricesDefault(Double addPricesDefault) {
		this.addPricesDefault = addPricesDefault;
	}
	public List<PdTemplatePrice> getTemplatePrices() {
		return templatePrices;
	}
	public void setTemplatePrices(List<PdTemplatePrice> templatePrices) {
		this.templatePrices = templatePrices;
	}
	public Integer getHouseId() {
		return houseId;
	}
	public void setHouseId(Integer houseId) {
		this.houseId = houseId;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Integer getSortype() {
		return sortype;
	}
	public void setSortype(Integer sortype) {
		this.sortype = sortype;
	}
	public List<String> getAreaIds() {
		return areaIds;
	}
	public void setAreaIds(List<String> areaIds) {
		this.areaIds = areaIds;
	}
	public List<PdTemplate> getCityList() {
		return cityList;
	}
	public void setCityList(List<PdTemplate> cityList) {
		this.cityList = cityList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaPid() {
		return areaPid;
	}
	public void setAreaPid(String areaPid) {
		this.areaPid = areaPid;
	}
	
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getTemplateAreaId() {
		return templateAreaId;
	}
	public void setTemplateAreaId(String templateAreaId) {
		this.templateAreaId = templateAreaId;
	}
	public String getTemplateAddress() {
		return templateAddress;
	}
	public void setTemplateAddress(String templateAddress) {
		this.templateAddress = templateAddress;
	}
	public Integer getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public Integer getIsFree() {
		return isFree;
	}
	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}
	public Integer getValuationType() {
		return valuationType;
	}
	public void setValuationType(Integer valuationType) {
		this.valuationType = valuationType;
	}
	@Override
	public PdTemplate getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(PdTemplate parent) {
		// TODO Auto-generated method stub
		
	}
}
