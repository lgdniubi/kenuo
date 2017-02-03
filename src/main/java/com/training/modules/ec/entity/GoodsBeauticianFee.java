package com.training.modules.ec.entity;


import java.util.Date;

import com.training.common.persistence.TreeEntity;
/**
 *商品服务费基础配置表实体
 * @author zhangyang
 *
 */
public class GoodsBeauticianFee extends TreeEntity<GoodsBeauticianFee> {

	private static final long serialVersionUID = 1L;
	private int goodsId;						//商品id
	private String goodsNo;						//商品编码
	private double basisFee;					//消耗业绩
	private int type;							//职位类型
	private double postFee;					//职位费用
//	private String createBy;					//创建者
	private Date createDate;					//创建日期
//	private String updateBy;					//更新人
	private Date updateDate;					//更新时间
	private String remarks;						//备注
	private String goodsName;					//商品名称
	private int isReal;						//商品类型
	
	
	//等级字段
	private double primary;					//初级业绩
	private double middle;						//中级业绩
	private double high;						//高级业绩
	private double internship;					//实习业绩
	private double store;						//储备业绩
	private double prther;						//项目老师
	
	
	
	
	
	
	public double getPrimary() {
		return primary;
	}
	public void setPrimary(double primary) {
		this.primary = primary;
	}
	public double getMiddle() {
		return middle;
	}
	public void setMiddle(double middle) {
		this.middle = middle;
	}
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	public double getInternship() {
		return internship;
	}
	public void setInternship(double internship) {
		this.internship = internship;
	}
	public double getStore() {
		return store;
	}
	public void setStore(double store) {
		this.store = store;
	}
	public double getPrther() {
		return prther;
	}
	public void setPrther(double prther) {
		this.prther = prther;
	}
	public int getIsReal() {
		return isReal;
	}
	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public double getBasisFee() {
		return basisFee;
	}
	public void setBasisFee(double basisFee) {
		this.basisFee = basisFee;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getPostFee() {
		return postFee;
	}
	public void setPostFee(double postFee) {
		this.postFee = postFee;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	
	
	
	@Override
	public GoodsBeauticianFee getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(GoodsBeauticianFee parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
