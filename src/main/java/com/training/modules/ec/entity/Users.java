package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;

/**
 * 电商用户实体类
 * @author coffee
 *
 */
public class Users extends TreeEntity<Users> {
	
	private static final long serialVersionUID = 1L;
	
	private Integer userid;			//用户id
	private String name;			//真实姓名
	private String email;			//邮箱
	private	 String password;		//密码
	private int sex;				//性别
	private Date birthday;			//生日
	private double usermoney;		//用户金额
	private double frozenmoney;		//冻结金额
	private int paypoints;			//金币
	private int addressid;			//收货地址
	private Date regtime;			//注册时间
	private Date lastlogin;			//最后登录时间
	private String lastip;			//最后登录ip地址
	private String qq;				//qq号
	private String mobile;			//手机号
	private String phone;			//座机
	private int mobilevalidated;	//手机是否验证
	private String oauth;			//第三方来源 wx weibo alipay
	private String openid;			//第三方唯一标识
	private String headpic;			//头像
	private String	province;		//省份
	private String city;			//市区
	private String district;		//县
	private int emailvalidated; 	//邮箱是否验证
	private String nickname;		//昵称
	private int level;				//会员等级
	private double discount;		//会员折扣 默认1  不享受折扣
	private double totalamount;		//消费累计额度
	private int islock;				//是否被冻结
	private int levelValue;			//消费积分
	
	private Date startRegtime;		//注册  开始时间
	private Date endRegtime;		//注册  结束时间
	
	private Date startLastlogin;	//最后登录  开始时间
	private Date endLastlogin;		//最后登录  开始时间
	
	private int num;				//查询个人所有订单数
	
	private String areaName;				//用户地址
	
	private String layer;			//用户等级  用于分销
	private int source;				//用户来源  用户分销
	

	private String officeId ;		//所属门店
	private String beautyId;	    //所属美容师
	
	private String appellationId;  //称谓标签id
	private String appellationName; //称谓名称
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getBeautyId() {
		return beautyId;
	}
	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}
	private String noLogin;			//未登陆用户
	public int getLevelValue() {
		return levelValue;
	}
	public void setLevelValue(int levelValue) {
		this.levelValue = levelValue;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public double getUsermoney() {
		return usermoney;
	}
	public void setUsermoney(double usermoney) {
		this.usermoney = usermoney;
	}
	public double getFrozenmoney() {
		return frozenmoney;
	}
	public void setFrozenmoney(double frozenmoney) {
		this.frozenmoney = frozenmoney;
	}
	public int getPaypoints() {
		return paypoints;
	}
	public void setPaypoints(int paypoints) {
		this.paypoints = paypoints;
	}
	public int getAddressid() {
		return addressid;
	}
	public void setAddressid(int addressid) {
		this.addressid = addressid;
	}
	public Date getRegtime() {
		return regtime;
	}
	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getLastip() {
		return lastip;
	}
	public void setLastip(String lastip) {
		this.lastip = lastip;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getMobilevalidated() {
		return mobilevalidated;
	}
	public void setMobilevalidated(int mobilevalidated) {
		this.mobilevalidated = mobilevalidated;
	}
	public String getOauth() {
		return oauth;
	}
	public void setOauth(String oauth) {
		this.oauth = oauth;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getHeadpic() {
		return headpic;
	}
	public void setHeadpic(String headpic) {
		this.headpic = headpic;
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
	public int getEmailvalidated() {
		return emailvalidated;
	}
	public void setEmailvalidated(int emailvalidated) {
		this.emailvalidated = emailvalidated;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	public int getIslock() {
		return islock;
	}
	public void setIslock(int islock) {
		this.islock = islock;
	}
	public Date getStartRegtime() {
		return startRegtime;
	}
	public void setStartRegtime(Date startRegtime) {
		this.startRegtime = startRegtime;
	}
	public Date getEndRegtime() {
		return endRegtime;
	}
	public void setEndRegtime(Date endRegtime) {
		this.endRegtime = endRegtime;
	}
	public Date getStartLastlogin() {
		return startLastlogin;
	}
	public void setStartLastlogin(Date startLastlogin) {
		this.startLastlogin = startLastlogin;
	}
	public Date getEndLastlogin() {
		return endLastlogin;
	}
	public void setEndLastlogin(Date endLastlogin) {
		this.endLastlogin = endLastlogin;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		if(layer.equals("C")){
			this.layer = "AB";
		}else if(layer.equals("D")){
			this.layer = "C";
		}else{
			this.layer = layer;
		}
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getNoLogin() {
		return noLogin;
	}
	public void setNoLogin(String noLogin) {
		this.noLogin = noLogin;
	}
	@Override
	public Users getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(Users parent) {
		// TODO Auto-generated method stub
		
	}
	public String getAppellationId() {
		return appellationId;
	}
	public void setAppellationId(String appellationId) {
		this.appellationId = appellationId;
	}
	public String getAppellationName() {
		return appellationName;
	}
	public void setAppellationName(String appellationName) {
		this.appellationName = appellationName;
	}
	
}
