package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

public class MtmySaleRelieveExport extends DataEntity<MtmySaleRelieveExport>{

	private static final long serialVersionUID = 1L;
	
	private String mtmyUserId;             //每天美耶用户id
	private String grade;				//级别
	private String areaName;            //所在大区名称
	private String groupName;           //所在集团军名称
	private String bazaarName;          //所在市场名称
	private String shopName;            //所在店铺名称
	private String userName;			//妃子校用户名
	private String mtmyName;			//每天美耶用户名
	private String userMobile;          //用户手机号码
	private String bNum;                   //B级用户数
	private String surplusNum;             //剩余B级用户数
	private String totalSurplus;         //用户总余额
	private String totalCloudMoney;         //用户总云币
	private String saleUserId;              //被邀请人id
	private String bName;               //对应B级用户用户名
	private String bMobile;             //对应B级用户手机号
	private String bRegTime;              //对应B级用户注册时间
	private String bCreateTime;           //对应B级用户邀请成功时间
	private String bBalanceAmount;         //对应B级用户贡献余额
	private String bIntegralAmount;        //对应B级用户贡献云币
	private String bOrderNum;               //对应B级用户的订单总额
	private String bOrderAmount;           //对应B级用户的订单数量
	private String firstOrderTime;        //首单时间
	
	@ExcelField(title="每天美耶用户id", align=2, sort=5)
	public String getMtmyUserId() {
		return mtmyUserId;
	}
	public void setMtmyUserId(String mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}
	@ExcelField(title="级别", align=2, sort=10)
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@ExcelField(title="所在大区", align=2, sort=15)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@ExcelField(title="所在集团军", align=2, sort=20)
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@ExcelField(title="所在市场", align=2, sort=25)
	public String getBazaarName() {
		return bazaarName;
	}
	public void setBazaarName(String bazaarName) {
		this.bazaarName = bazaarName;
	}
	@ExcelField(title="所在店铺", align=2, sort=30)
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	@ExcelField(title="妃子校用户名", align=2, sort=35)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@ExcelField(title="每天美耶用户名", align=2, sort=40)
	public String getMtmyName() {
		return mtmyName;
	}
	public void setMtmyName(String mtmyName) {
		this.mtmyName = mtmyName;
	}
	@ExcelField(title="手机号码", align=2, sort=45)
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	@ExcelField(title="B级用户数", align=2, sort=50)
	public String getbNum() {
		return bNum;
	}
	public void setbNum(String bNum) {
		this.bNum = bNum;
	}
	@ExcelField(title="剩余B级用户数", align=2, sort=55)
	public String getSurplusNum() {
		return surplusNum;
	}
	public void setSurplusNum(String surplusNum) {
		this.surplusNum = surplusNum;
	}
	@ExcelField(title="用户总余额", align=2, sort=60)
	public String getTotalSurplus() {
		return totalSurplus;
	}
	public void setTotalSurplus(String totalSurplus) {
		this.totalSurplus = totalSurplus;
	}
	@ExcelField(title="用户总云币", align=2, sort=65)
	public String getTotalCloudMoney() {
		return totalCloudMoney;
	}
	public void setTotalCloudMoney(String totalCloudMoney) {
		this.totalCloudMoney = totalCloudMoney;
	}
	@ExcelField(title="被邀请人id", align=2, sort=70)
	public String getSaleUserId() {
		return saleUserId;
	}
	public void setSaleUserId(String saleUserId) {
		this.saleUserId = saleUserId;
	}
	@ExcelField(title="对应B级用户用户名", align=2, sort=75)
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}
	@ExcelField(title="对应B级用户手机号", align=2, sort=80)
	public String getbMobile() {
		return bMobile;
	}
	public void setbMobile(String bMobile) {
		this.bMobile = bMobile;
	}
	@ExcelField(title="对应B级用户注册时间", align=2, sort=85)
	public String getbRegTime() {
		return bRegTime;
	}
	public void setbRegTime(String bRegTime) {
		this.bRegTime = bRegTime;
	}
	@ExcelField(title="对应B级用户邀请成功时间", align=2, sort=90)
	public String getbCreateTime() {
		return bCreateTime;
	}
	public void setbCreateTime(String bCreateTime) {
		this.bCreateTime = bCreateTime;
	}
	@ExcelField(title="对应B级用户贡献余额", align=2, sort=95)
	public String getbBalanceAmount() {
		return bBalanceAmount;
	}
	public void setbBalanceAmount(String bBalanceAmount) {
		this.bBalanceAmount = bBalanceAmount;
	}
	@ExcelField(title="对应B级用户贡献云币", align=2, sort=100)
	public String getbIntegralAmount() {
		return bIntegralAmount;
	}
	public void setbIntegralAmount(String bIntegralAmount) {
		this.bIntegralAmount = bIntegralAmount;
	}
	@ExcelField(title="对应B级用户的订单总额", align=2, sort=105)
	public String getbOrderNum() {
		return bOrderNum;
	}
	public void setbOrderNum(String bOrderNum) {
		this.bOrderNum = bOrderNum;
	}
	@ExcelField(title="对应B级用户的订单数量", align=2, sort=110)
	public String getbOrderAmount() {
		return bOrderAmount;
	}
	public void setbOrderAmount(String bOrderAmount) {
		this.bOrderAmount = bOrderAmount;
	}
	@ExcelField(title="首单时间", align=2, sort=115)
	public String getFirstOrderTime() {
		return firstOrderTime;
	}
	public void setFirstOrderTime(String firstOrderTime) {
		this.firstOrderTime = firstOrderTime;
	}
	
}
