package com.training.modules.sys.entity;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 自营店实体类
 * @author coffee
 *
 */
public class OfficeInfo extends DataEntity<OfficeInfo>{

	private static final long serialVersionUID = 1L;
	private String franchiseeId;  //所属加盟商id
	private String franchiseeCode;
	private String franchiseeName; //所属加盟商名字
	private String postalCode;	//邮政编码
	private String contacts;	//联系人
	private String telephone;	//联系电话
	private String storePhone;	//店铺电话
	private String detailedAddress;		//详细地址
	private String img;			//商品首图
	private String beginTime;	//开始时间
	private String endTime;		//结束时间
	private int	   status;		//状态
	
	private Area areaInfo;		//归属区域
	private String shortName;	//店铺短名称
	private String OfficeName;	//店铺名称
	private String bedNum;		//床位
	
	private int num;			//查询区域下有无子类
	//后期需优化
	private String upOfficeId;	//上级机构ID
	private String upOfficeName;	//上级机构名称
	private String upOfficeCode;	//上级机构编码
	private String areaId;	//归属区域
	private String areaName;	//区域名称
	private String areaCode;	//区域名称编码
	private String grade;		//是否为店铺 1 是 2 否
	private String code;		//机构code
	
	@JsonIgnore
	@NotNull(message="上级机构编码不能为空")
	@ExcelField(title="上级机构编码", type=0, align=2, sort=40)
	public String getUpOfficeCode() {
		return upOfficeCode;
	}
	@JsonIgnore
	@ExcelField(title="上级机构名称", type=0, align=2, sort=45)
	public String getUpOfficeName() {
		return upOfficeName;
	}
	public void setUpOfficeName(String upOfficeName) {
		this.upOfficeName = upOfficeName;
	}
	@JsonIgnore
	@NotNull(message="归属区域编码不能为空")
	@ExcelField(title="归属区域编码", type=0, align=2, sort=50)
	public String getAreaCode() {
		return areaCode;
	}
	@JsonIgnore
	@ExcelField(title="归属区域名称", type=0, align=2, sort=55)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	//仅导出
	@JsonIgnore
	@ExcelField(title="机构编码", type=1, align=2, sort=58)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@JsonIgnore
	@NotNull(message="机构名称不能为空")
	@ExcelField(title="机构名称", type=0, align=2, sort=60)
	public String getOfficeName() {
		return OfficeName;
	}
	public void setOfficeName(String officeName) {
		OfficeName = officeName;
	}
	@JsonIgnore
	@NotNull(message="店铺短名称不能为空")
	@ExcelField(title="店铺短名称", type=0, align=2, sort=64)
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public void setUpOfficeCode(String upOfficeCode) {
		this.upOfficeCode = upOfficeCode;
	}
	public String getFranchiseeId() {
		return franchiseeId;
	}
	public void setFranchiseeId(String franchiseeId) {
		this.franchiseeId = franchiseeId;
	}
	public String getFranchiseeCode() {
		return franchiseeCode;
	}
	public void setFranchiseeCode(String franchiseeCode) {
		this.franchiseeCode = franchiseeCode;
	}
	public String getFranchiseeName() {
		return franchiseeName;
	}
	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}
	//仅导入
	@JsonIgnore
	@ExcelField(title="是否为店铺", type=2, align=2, sort=63)
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	@JsonIgnore
	@ExcelField(title="邮政编码", type=0, align=2, sort=65)
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	@JsonIgnore
	@NotNull(message="店长不能为空")
	@ExcelField(title="店长", type=0, align=2, sort=70)
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	@JsonIgnore
	@NotNull(message="店长电话不能为空")
	@ExcelField(title="店长电话", type=0, align=1, sort=75)
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	@JsonIgnore
	@NotNull(message="店铺电话不能为空")
	@ExcelField(title="店铺电话", type=0, align=1, sort=80)
	public String getStorePhone() {
		return storePhone;
	}
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}
	@JsonIgnore
	@NotNull(message="详细地址不能为空")
	@ExcelField(title="详细地址", type=0, align=1, sort=95)
	public String getDetailedAddress() {
		return detailedAddress;
	}
	public void setDetailedAddress(String detailedAddress) {
		this.detailedAddress = detailedAddress;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@JsonIgnore
	@NotNull(message="上班时间不能为空")
	@ExcelField(title="上班时间", type=0, align=2, sort=85)
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	@JsonIgnore
	@NotNull(message="下班时间不能为空")
	@ExcelField(title="下班时间", type=0, align=2, sort=90)
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public Area getAreaInfo() {
		return areaInfo;
	}
	public void setAreaInfo(Area areaInfo) {
		this.areaInfo = areaInfo;
	}

	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getUpOfficeId() {
		return upOfficeId;
	}
	public void setUpOfficeId(String upOfficeId) {
		this.upOfficeId = upOfficeId;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@JsonIgnore
	@ExcelField(title="床位", type=0, align=2, sort=94)
	public String getBedNum() {
		return bedNum;
	}
	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}
}
