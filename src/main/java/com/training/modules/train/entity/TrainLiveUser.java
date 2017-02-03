package com.training.modules.train.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 直播会员entity
 * @author yangyang
 *
 */
public class TrainLiveUser extends DataEntity<TrainLiveUser> {
	
	private static final long serialVersionUID = 1L;
	private int auditId;			//直播间id
	private String userId;			//用户id
	private String name;			//用户姓名
	private String mobile;			//手机号
	private double money;			//购买金额
	private String payment;			//支付方式
	private String remak;			//备注说明
	private Date validityDate;		//有效期
	//private String createBy;
	private String title;			//直播标题
	private Date createDate;		//创建日期

	private Date bengTime; 			//直播日期
	private String position;		//职位
	
	

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Date getBengTime() {
		return bengTime;
	}

	public void setBengTime(Date bengTime) {
		this.bengTime = bengTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	@NotNull(message="直播ID不能为空")
	@ExcelField(title="直播ID", align=2, sort=10)
	public int getAuditId() {
		return auditId;
	}

	public void setAuditId(int auditId) {
		this.auditId = auditId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JsonIgnore
	@ExcelField(title="会员姓名", align=2, sort=15)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	@NotNull(message="手机号不能为空")
	@ExcelField(title="手机号", align=2, sort=20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@JsonIgnore
	@ExcelField(title="购买金额", align=2, sort=25)
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	@JsonIgnore
	@ExcelField(title="支付方式", align=2, sort=30)
	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	@JsonIgnore
	@ExcelField(title="备注", align=2, sort=35)
	public String getRemak() {
		return remak;
	}

	public void setRemak(String remak) {
		this.remak = remak;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@ExcelField(title="有效期", align=2, sort=40)
	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}



}
