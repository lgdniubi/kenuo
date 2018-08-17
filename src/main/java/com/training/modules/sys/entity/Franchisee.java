package com.training.modules.sys.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.TreeEntity;
import com.training.modules.train.entity.BankAccount;

/**
 * 加盟商管理Entity
 * @author kele
 * @version 2016-6-4 16:17:17
 */
public class Franchisee extends TreeEntity<Franchisee>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Area 	area;			//区域
	private	String 	name;			//加盟商名称
	private String	type;			//类型（1：直营；2：加盟；）
	private String	code;			//编码
	private String 	province;		//省份
	private String	city;			//地市
	private String	district;		//区县
	private String 	address;		//地址
	private String 	zipcode;		//邮政编码
	private String 	legalName;		//加盟法人
	private String 	legalCard;		//加盟法人身份证号码
	private Date 	setDate;		//加盟法人成立日期
	private String 	shortName;		//加盟法人成立日期
	private String 	contacts;		//加盟商联系人
	private String 	mobile;			//联系电话
	private String 	tel;			//联系固话
	private String 	charterUrl;		//营业执照（照片）
	private String 	taxationUrl;	//税务登记（照片）
	private String 	iconUrl;		//机构icon
	private String 	bankBeneficiary;//开户行
	private String 	bankCode;		//银行卡号
	private String 	bankName;		//持卡人姓名
	private String 	status = "0";	//状态(未用)
	private List<BankAccount> bankAccount;			//银行账户
	
	private String isRealFranchisee;  //是否真实的商家（0：否；1：是）
	private String publicServiceFlag; //公共商品服务标识(0: 做 1: 不做)  土豆添加   2018-2-27
	
	private String description;      //商品详情
	private int isTest;      //测试账号标识（0：正常；1：测试）
	
	//-------------------------2018-08-10
	private String superUserName;			//该商家的超管姓名
	private String superUserPhone;			//该商家的超管手机号
	private String superUserId;				//该商家的超管id
	private String oldSuperUserId;			//旧该商家的超管id
	
	@Override
	public Franchisee getParent() {
		return parent;
	}
	@Override
	public void setParent(Franchisee parent) {
		this.parent = parent;
	}
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCharterUrl() {
		return charterUrl;
	}
	public void setCharterUrl(String charterUrl) {
		this.charterUrl = charterUrl;
	}
	public String getTaxationUrl() {
		return taxationUrl;
	}
	public void setTaxationUrl(String taxationUrl) {
		this.taxationUrl = taxationUrl;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getBankBeneficiary() {
		return bankBeneficiary;
	}
	public void setBankBeneficiary(String bankBeneficiary) {
		this.bankBeneficiary = bankBeneficiary;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<BankAccount> getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(List<BankAccount> bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getIsRealFranchisee() {
		return isRealFranchisee;
	}
	public void setIsRealFranchisee(String isRealFranchisee) {
		this.isRealFranchisee = isRealFranchisee;
	}
	public String getPublicServiceFlag() {
		return publicServiceFlag;
	}
	public void setPublicServiceFlag(String publicServiceFlag) {
		this.publicServiceFlag = publicServiceFlag;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLegalCard() {
		return legalCard;
	}
	public void setLegalCard(String legalCard) {
		this.legalCard = legalCard;
	}
	public Date getSetDate() {
		return setDate;
	}
	public void setSetDate(Date setDate) {
		this.setDate = setDate;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public int getIsTest() {
		return isTest;
	}
	public void setIsTest(int isTest) {
		this.isTest = isTest;
	}
	public String getSuperUserName() {
		return superUserName;
	}
	public void setSuperUserName(String superUserName) {
		this.superUserName = superUserName;
	}
	public String getSuperUserPhone() {
		return superUserPhone;
	}
	public void setSuperUserPhone(String superUserPhone) {
		this.superUserPhone = superUserPhone;
	}
	public String getSuperUserId() {
		return superUserId;
	}
	public void setSuperUserId(String superUserId) {
		this.superUserId = superUserId;
	}
	public String getOldSuperUserId() {
		return oldSuperUserId;
	}
	public void setOldSuperUserId(String oldSuperUserId) {
		this.oldSuperUserId = oldSuperUserId;
	}
	
}
