package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 团购项目推送
 * @author coffee
 * @date 2018年3月30日
 */
public class MtmyGroupActivityRemind extends DataEntity<MtmyGroupActivityRemind> {
	
	
	private static final long serialVersionUID = 1L;
	
	private int rId;		// 主键id
	private int userId;		// 用户id
	private String goodsId;	// 商品id
	private String goodsName;	// 商品名称
	private String mobile;	// 用户手机号
	private String clientId;// 客户端id
	public int getrId() {
		return rId;
	}
	public void setrId(int rId) {
		this.rId = rId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
