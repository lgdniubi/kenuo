package com.training.modules.personnelfile.entity;


import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Role;

/**
 * 机构Entity
 * 
 * @version 2013-05-15
 */
public class PersonnelFile extends DataEntity<PersonnelFile> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String soName;
	private String srName;
	private int parentDel;
	private Integer filing;
	private Integer sex;
	private Date birthday;
	private String nation;
	private String userNative;
	private Date create_date;
	private Date probationStartDate;
	private String oName;
	private String officeId;
	private String mobile;
	private Date inductionTime;
	/**
	 * 系统用户
	 */
	private String loginName;
	private String sName;
	private String idcard;
	private String photo;
	private String no;

	/**
	 * 户籍组合地址
	 */
	private String registerSite; //户籍所在地（下来选内容）
	private String registerSite1; //户籍所在地（单行文本内容）
	/**
	 * 现居住地
	 */
	private String dwelling; //现居住地（下来选内容）
	private String dwelling1; //现居住地（单行文本内容）
	
	/**
	 * 用户-基本信息
	 */
	private UserBaseInfo userBaseInfo;
	
	/**
	 * 用户-入职情况及联系方式
	 */
	private UserEmployed userEmployed;
	/**
	 * 用户-图片
	 */
	private List<UserImages> imgList;
	/**
	 * 用户-教育背景
	 */
	private UserEducation userEducation;
	/**
	 * 工作经历
	 */
	private List<UserWorkExperience> userWorkExperienceList;
	
	/**
	 * 用户-家庭情况
	 */
	private List<UserFamily> userFamilies;
	
	/**
	 * 用户-主要家庭成员信息
	 */
	private List<UserFamilymember> userFamilymembers;
	
	/**
	 * 用户-个人评价
	 */
	private UserSelfevaluation userSelfevaluation;
	
	/**
	 * 用户-离职情况
	 */
	private UserDepartures userDepartures;
	private Area oneArea;
	private Area twoArea;
	
	private List<Role> rolelist;

	/**----------------------------修改页面显示需要的对象------------------------------------------**/
	
	public UserFamily father;
	public UserFamily mother;
	
	public UserFamilymember oneUser;
	public UserFamilymember twoUser;
	public UserFamilymember threeUser;
	
	public UserImages userImage1;
	public UserImages userImage2;
	public UserImages userImage3;
	public UserImages userImage4;
	public UserImages userImage5;
	
	
	private UserWorkExperience oneUserWorkExperience;
	private UserWorkExperience twoUserWorkExperience;
	

	public Date getInductionTime() {
		return inductionTime;
	}

	public void setInductionTime(Date inductionTime) {
		this.inductionTime = inductionTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public UserWorkExperience getOneUserWorkExperience() {
		return oneUserWorkExperience;
	}

	public void setOneUserWorkExperience(UserWorkExperience oneUserWorkExperience) {
		this.oneUserWorkExperience = oneUserWorkExperience;
	}

	public UserWorkExperience getTwoUserWorkExperience() {
		return twoUserWorkExperience;
	}

	public void setTwoUserWorkExperience(UserWorkExperience twoUserWorkExperience) {
		this.twoUserWorkExperience = twoUserWorkExperience;
	}

	public Area getOneArea() {
		return oneArea;
	}

	public void setOneArea(Area oneArea) {
		this.oneArea = oneArea;
	}

	public Area getTwoArea() {
		return twoArea;
	}

	public void setTwoArea(Area twoArea) {
		this.twoArea = twoArea;
	}

	public String getoName() {
		return oName;
	}

	public void setoName(String oName) {
		this.oName = oName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public Integer getFiling() {
		return filing;
	}

	public void setFiling(Integer filing) {
		this.filing = filing;
	}

	public UserImages getUserImage1() {
		return userImage1;
	}

	public void setUserImage1(UserImages userImage1) {
		this.userImage1 = userImage1;
	}

	public UserImages getUserImage2() {
		return userImage2;
	}

	public void setUserImage2(UserImages userImage2) {
		this.userImage2 = userImage2;
	}

	public UserImages getUserImage3() {
		return userImage3;
	}

	public void setUserImage3(UserImages userImage3) {
		this.userImage3 = userImage3;
	}

	public UserImages getUserImage4() {
		return userImage4;
	}

	public void setUserImage4(UserImages userImage4) {
		this.userImage4 = userImage4;
	}

	public UserImages getUserImage5() {
		return userImage5;
	}

	public void setUserImage5(UserImages userImage5) {
		this.userImage5 = userImage5;
	}

	public UserFamilymember getOneUser() {
		return oneUser;
	}

	public void setOneUser(UserFamilymember oneUser) {
		this.oneUser = oneUser;
	}

	public UserFamilymember getTwoUser() {
		return twoUser;
	}

	public void setTwoUser(UserFamilymember twoUser) {
		this.twoUser = twoUser;
	}

	public UserFamilymember getThreeUser() {
		return threeUser;
	}

	public void setThreeUser(UserFamilymember threeUser) {
		this.threeUser = threeUser;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public UserBaseInfo getUserBaseInfo() {
		return userBaseInfo;
	}

	public void setUserBaseInfo(UserBaseInfo userBaseInfo) {
		this.userBaseInfo = userBaseInfo;
	}
	
	public PersonnelFile() {
		super();
	}

	public PersonnelFile(String id) {
		super(id);
	}
	
	public UserDepartures getUserDepartures() {
		return userDepartures;
	}

	public void setUserDepartures(UserDepartures userDepartures) {
		this.userDepartures = userDepartures;
	}

	public UserSelfevaluation getUserSelfevaluation() {
		return userSelfevaluation;
	}

	public void setUserSelfevaluation(UserSelfevaluation userSelfevaluation) {
		this.userSelfevaluation = userSelfevaluation;
	}

	public List<UserFamilymember> getUserFamilymembers() {
		return userFamilymembers;
	}

	public void setUserFamilymembers(List<UserFamilymember> userFamilymembers) {
		this.userFamilymembers = userFamilymembers;
	}

	public List<UserWorkExperience> getUserWorkExperienceList() {
		return userWorkExperienceList;
	}

	public void setUserWorkExperienceList(List<UserWorkExperience> userWorkExperienceList) {
		this.userWorkExperienceList = userWorkExperienceList;
	}

	public List<UserFamily> getUserFamilies() {
		return userFamilies;
	}

	public void setUserFamilies(List<UserFamily> userFamilies) {
		this.userFamilies = userFamilies;
	}

	public UserEducation getUserEducation() {
		return userEducation;
	}

	public void setUserEducation(UserEducation userEducation) {
		this.userEducation = userEducation;
	}

	public List<UserImages> getImgList() {
		return imgList;
	}

	public void setImgList(List<UserImages> imgList) {
		this.imgList = imgList;
	}

	public int getParentDel() {
		return parentDel;
	}

	public void setParentDel(int parentDel) {
		this.parentDel = parentDel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getSoName() {
		return soName;
	}

	public void setSoName(String soName) {
		this.soName = soName;
	}

	public String getSrName() {
		return srName;
	}

	public void setSrName(String srName) {
		this.srName = srName;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getRegisterSite() {
		return registerSite;
	}

	public void setRegisterSite(String registerSite) {
		this.registerSite = registerSite;
	}

	public String getRegisterSite1() {
		return registerSite1;
	}

	public void setRegisterSite1(String registerSite1) {
		this.registerSite1 = registerSite1;
	}

	public String getDwelling() {
		return dwelling;
	}

	public void setDwelling(String dwelling) {
		this.dwelling = dwelling;
	}

	public String getDwelling1() {
		return dwelling1;
	}

	public void setDwelling1(String dwelling1) {
		this.dwelling1 = dwelling1;
	}

	public UserEmployed getUserEmployed() {
		return userEmployed;
	}

	public void setUserEmployed(UserEmployed userEmployed) {
		this.userEmployed = userEmployed;
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

	public String getUserNative() {
		return userNative;
	}

	public void setUserNative(String userNative) {
		this.userNative = userNative;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getProbationStartDate() {
		return probationStartDate;
	}

	public void setProbationStartDate(Date probationStartDate) {
		this.probationStartDate = probationStartDate;
	}

	public List<Role> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<Role> rolelist) {
		this.rolelist = rolelist;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserFamily getFather() {
		return father;
	}

	public void setFather(UserFamily father) {
		this.father = father;
	}

	public UserFamily getMother() {
		return mother;
	}

	public void setMother(UserFamily mother) {
		this.mother = mother;
	}
	
}