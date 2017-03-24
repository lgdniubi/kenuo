package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 直播送的礼物实体类
 * @author xiaoye 2017年3月16日
 *
 */
public class TrainLiveGift extends DataEntity<TrainLiveGift>{

	private static final long serialVersionUID = 1L;
	
	private int trainLiveGiftId;    //礼物id
	private String name;            //礼物名称
	private String imgUrl;         	//礼物照片路径
	private int integrals;          //云币
	private String imgName;         //图片名字，也就是页面上的那个英文名
	private String isBatter;       //是否连击 （0：是；1：否））
	private String isShow;         //是否显示（0：是；1：否）
	private int sort;              //排序
	
	public int getTrainLiveGiftId() {
		return trainLiveGiftId;
	}
	public void setTrainLiveGiftId(int trainLiveGiftId) {
		this.trainLiveGiftId = trainLiveGiftId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getIntegrals() {
		return integrals;
	}
	public void setIntegrals(int integrals) {
		this.integrals = integrals;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getIsBatter() {
		return isBatter;
	}
	public void setIsBatter(String isBatter) {
		this.isBatter = isBatter;
	}
	
	
}
