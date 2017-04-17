package com.training.modules.crm.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * kenuo @description： 用户详细信息表对应实体类
 * @author：sharp @date：2017年3月2日
 */
public class UserDetail extends DataEntity<UserDetail> {

	private static final long serialVersionUID = 1L;
	
	// 用户ID
	private String userId;
	private String nickname; // 昵称
	private String name; // 真实姓名
	private String mobile; // 手机号码
	private String idCard; // 身份证号码
	private Date birthday; // 生日
	private String lunarBirthday; // 阴历生日
	private String character; // 性格
	private String constellation; // 星座
	private String isMarrige; // 是否结婚
	private String weddingDay; // 结婚纪念日
	private String isEstate; // 房产情况
	private String isMember; // 是否会员,客户类型
	private String carBrand; // 汽车品牌
	private String children; // 孩子情况
	private String occupation; // 职业类型
	private String income; // 月收入
	private Date menstrualDate; // 例假日期
	private Double menstrualPeroid; // 例假周期
	private Double weight; // 体重
	private Double height; // 身高
	private String criticalDiseases; // 严重疾病
	private String beautyId;// 美容师id
	private String source; // 客户来源
	private String promotionAgent; // 推广人员
	private String usingBrand; // 正在使用的品牌
	private String intrest; // 客户兴趣
	private String taboo; // 客户禁忌
	private String hate; // 客户厌恶
	private String remark; // 标记
	private Date createDate; // 创建时间
	private Date updateDate;//更新时间
	private Integer age; // 客户年龄
	private String keyword; // 查询所用关键字
	private String bazaarId; // 对应的市场id
	private String officeId; // 对应的店铺id
	private String bazaarName; // 对应的市场名称
	private String officeName; // 对应的店铺名称
  	private String beautyName;//美容师名字
  	private String userLevel;//用户等级
  	private String shopId;
  	private String shopName;
	private String sex;

	//首页展示要用到的字段
	private String levelvalue;//颜值
	private Date regTime;//注册时间
	private String accountArrearage;//账户欠款
	private Date firstDate;//首次消费
	private Date lastDate;//最近消费
  	private String level;//颜值等级
  	
  	
  	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getLevelvalue() {
		return levelvalue;
	}

	public void setLevelvalue(String levelvalue) {
		this.levelvalue = levelvalue;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getAccountArrearage() {
		return accountArrearage;
	}

	public void setAccountArrearage(String accountArrearage) {
		this.accountArrearage = accountArrearage;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLunarBirthday() {
		return lunarBirthday;
	}

	public void setLunarBirthday(String lunarBirthday) {
		this.lunarBirthday = lunarBirthday;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public Date getMenstrualDate() {
		return menstrualDate;
	}

	public void setMenstralDate(Date menstrualDate) {
		this.menstrualDate = menstrualDate;
	}

	public String getBeautyId() {
		return beautyId;
	}

	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}

	public String getBeautyName() {
		return beautyName;
	}

	public void setBeautyName(String beautyName) {
		this.beautyName = beautyName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getBazaarId() {
		return bazaarId;
	}

	public void setBazaarId(String bazaarId) {
		this.bazaarId = bazaarId;
	}

	public String getBazaarName() {
		return bazaarName;
	}

	public void setBazaarName(String bazaarName) {
		this.bazaarName = bazaarName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getIsMarrige() {
		return isMarrige;
	}

	public void setIsMarrige(String isMarrige) {
		this.isMarrige = isMarrige;
	}

	public String getWeddingDay() {
		return weddingDay;
	}

	public void setWeddingDay(String weddingDay) {
		this.weddingDay = weddingDay;
	}

	public String getIsEstate() {
		return isEstate;
	}

	public void setIsEstate(String isEstate) {
		this.isEstate = isEstate;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setMenstrualDate(Date menstrualDate) {
		this.menstrualDate = menstrualDate;
	}

	public Double getMenstrualPeroid() {
		return menstrualPeroid;
	}

	public void setMenstrualPeroid(Double menstrualPeroid) {
		this.menstrualPeroid = menstrualPeroid;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getCriticalDiseases() {
		return criticalDiseases;
	}

	public void setCriticalDiseases(String criticalDiseases) {
		this.criticalDiseases = criticalDiseases;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPromotionAgent() {
		return promotionAgent;
	}

	public void setPromotionAgent(String promotionAgent) {
		this.promotionAgent = promotionAgent;
	}

	public String getUsingBrand() {
		return usingBrand;
	}

	public void setUsingBrand(String usingBrand) {
		this.usingBrand = usingBrand;
	}

	public String getIntrest() {
		return intrest;
	}

	public void setIntrest(String intrest) {
		this.intrest = intrest;
	}

	public String getTaboo() {
		return taboo;
	}

	public void setTaboo(String taboo) {
		this.taboo = taboo;
	}

	public String getHate() {
		return hate;
	}

	public void setHate(String hate) {
		this.hate = hate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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



	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}



}
