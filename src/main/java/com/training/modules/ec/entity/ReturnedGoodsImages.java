package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 退货商品图片
 * @author water
 *
 */
public class ReturnedGoodsImages extends TreeEntity<ReturnedGoodsImages>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnId;						//退货id
	private String returnImg;						//图片路径
	private Date createDate;						//添加时间

	
	
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public String getReturnImg() {
		return returnImg;
	}
	public void setReturnImg(String returnImg) {
		this.returnImg = returnImg;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public ReturnedGoodsImages getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(ReturnedGoodsImages parent) {
		// TODO Auto-generated method stub
		
	}

	

}
