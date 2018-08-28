package com.training.modules.personnelfile.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
/**
 * 用户-基本信息
 * @author ouwei
 *
 */
public class UserBaseInfo extends DataEntity<UserBaseInfo> {

	private static final long serialVersionUID = 9094167965591975153L;
	
	private String userId; //用户ID
	private Integer age; //年龄
	private Date birthday;//出生日期
	private String nation;//民族
	private Integer stature;//身高
	private String bloodType;//血型
	private String maritalStatus;//婚姻状况
	private String party;//政治面貌
	private Date joinParty;//入党时间
	private String registerType;//户籍类别
	private String registerSite;//户籍所在地
	private String registerSite1;//户籍所在地1
	private String dwelling1;//现居住地1
	private String dwelling;//现居住地
	private int sex;
	private String isQuit;
	private String position;//职位
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getStature() {
		return stature;
	}
	public void setStature(Integer stature) {
		this.stature = stature;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBloodType() {
		return bloodType;
	}
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public Date getJoinParty() {
		return joinParty;
	}
	public void setJoinParty(Date joinParty) {
		this.joinParty = joinParty;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getRegisterSite() {
		return registerSite;
	}
	public void setRegisterSite(String registerSite) {
		this.registerSite = registerSite;
	}
	public String getDwelling() {
		return dwelling;
	}
	public void setDwelling(String dwelling) {
		this.dwelling = dwelling;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getRegisterSite1() {
		return registerSite1;
	}
	public void setRegisterSite1(String registerSite1) {
		this.registerSite1 = registerSite1;
	}
	public String getDwelling1() {
		return dwelling1;
	}
	public void setDwelling1(String dwelling1) {
		this.dwelling1 = dwelling1;
	}
	public String getIsQuit() {
		return isQuit;
	}
	public void setIsQuit(String isQuit) {
		this.isQuit = isQuit;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
