package com.training.modules.forms.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 用户信息表 
 * @author stone
 * @date 2017年4月24日
 */
public class UserInfoReport extends TreeEntity<UserInfoReport>{

	private static final long serialVersionUID = 1L;
	
	private String userId;					//用户ID                     
	private String userNmae;				//用户昵称
	private String score;					//学分(总)
	private int bespeak;					//预约次数(总)
	private int valid;						//预约有效次数
	private int userBalance;				//可用佣金
	private String gold;					//云币额(总)
	private String oneClass;				//第一级(商家)
	private String twoClass;				//第二级(区域)
	private String threeClass;				//第三级(集团军)
	private String foreClass;				//第四级(市场)
	private String fiveClass;				//第五级(门店)
	private String role;					//角色
	private String job;						//职位
	private String createTime;				//创建时间
	private String mobile;					//注册手机号
	private Date begtime;					//开始时间
	private Date endtime;					//结束时间
	
	@ExcelField(title = "用户ID" ,sort = 100 )
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ExcelField(title = "用户昵称" ,sort = 200 )
	public String getUserNmae() {
		return userNmae;
	}

	public void setUserNmae(String userNmae) {
		this.userNmae = userNmae;
	}

	@ExcelField(title = "学分(总)" ,sort = 300 )
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@ExcelField(title = "预约次数" ,sort = 400 )
	public int getBespeak() {
		return bespeak;
	}

	public void setBespeak(int bespeak) {
		this.bespeak = bespeak;
	}
	@ExcelField(title = "预约有效次数" ,sort = 450 )
	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	@ExcelField(title = "云币额" ,sort = 500 )
	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	@ExcelField(title = "可用佣金" ,sort = 550 )
	public int getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(int userBalance) {
		this.userBalance = userBalance;
	}
	
	@ExcelField(title = "商家" ,sort = 600 )
	public String getOneClass() {
		return oneClass;
	}

	public void setOneClass(String oneClass) {
		this.oneClass = oneClass;
	}

	@ExcelField(title = "区域" ,sort = 700 )
	public String getTwoClass() {
		return twoClass;
	}

	public void setTwoClass(String twoClass) {
		this.twoClass = twoClass;
	}

	@ExcelField(title = "集团军" ,sort = 800 )
	public String getThreeClass() {
		return threeClass;
	}

	public void setThreeClass(String threeClass) {
		this.threeClass = threeClass;
	}

	@ExcelField(title = "市场" ,sort = 900 )
	public String getForeClass() {
		return foreClass;
	}

	public void setForeClass(String foreClass) {
		this.foreClass = foreClass;
	}

	@ExcelField(title = "门店" ,sort = 1000 )
	public String getFiveClass() {
		return fiveClass;
	}

	public void setFiveClass(String fiveClass) {
		this.fiveClass = fiveClass;
	}

	@ExcelField(title = "角色" ,sort = 1200 )
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@ExcelField(title = "职位" ,sort = 1100 )
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@ExcelField(title = "创建时间" ,sort = 1300 )
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ExcelField(title = "注册手机号" ,sort = 1400 )
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public UserInfoReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(UserInfoReport parent) {
		// TODO Auto-generated method stub
		
	}

	public Date getBegtime() {
		return begtime;
	}

	public void setBegtime(Date time) {
		this.begtime = time;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
}
