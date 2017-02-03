package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Office;

/**
 * 仓库表
 * @author dalong
 *
 */
public class PdWareHouse extends TreeEntity<PdWareHouse> {
	  
	
	private static final long serialVersionUID = 1L;
	
	private Integer wareHouseId;
	private String franchiseeId; //商家id 公司
	private String name; //仓库名称
	private String fName; //仓库名称
	private String areaId; //区域ID
	private String areaName; //区域名称
	private String address; //仓库地址
	private String postalcode; //邮政编码
	private String governor; //管理员
	private String phone; //仓库联系方式电话
	private String remarks; //remark
	private Integer houseDelFlag; //删除标识 0 未删除 1 已删除
	private String province; // 省ID
	private String city;     // 市
	private String district; //县、区、县级市
	private Area area;       //区域
	private Office company;	// 归属商家
	private Date timeStart;
	private Date timeEnd;
	@Override
	public PdWareHouse getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(PdWareHouse parent) {
		// TODO Auto-generated method stub
		
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public Office getCompany() {
		return company;
	}
	public void setCompany(Office company) {
		this.company = company;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public Integer getWareHouseId() {
		return wareHouseId;
	}
	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}
	public String getName() {
		return name;
	}
	public void setHouseName(String name) {
		this.name = name;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGovernor() {
		return governor;
	}
	public void setGovernor(String governor) {
		this.governor = governor;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getHouseDelFlag() {
		return houseDelFlag;
	}
	public void setHouseDelFlag(Integer houseDelFlag) {
		this.houseDelFlag = houseDelFlag;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Date getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}
	public Date getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	
}
