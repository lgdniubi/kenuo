/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.training.common.config.Global;
import com.training.common.persistence.DataEntity;
import com.training.common.supcan.annotation.treelist.cols.SupCol;
import com.training.common.utils.Collections3;
import com.training.common.utils.excel.annotation.ExcelField;
import com.training.modules.train.entity.FzxRole;
//import com.training.common.utils.excel.fieldtype.RoleListType;

/**
 * 用户Entity
 * 
 * @version 2013-12-05
 */
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	private Office company;	// 归属商家
	private Office office;	// 归属机构
	private String code;	//店铺编码
	private String loginName;// 登录名
	private String password;// 密码
	private String no;		// 工号
	private String idCard;	//身份证号码
	private Date inductionTime;	//入职时间
	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像
	private String qrCode;	//二维码
	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码
	private String delRemarks;	//删除备注
	private int gold;			//积分
	private double usermoney;	//账户余额
	private String delindex;	//删除标记   用于验证身份证		
	private int parentDel;		//查询是否被删除
	private int mtmyUserId;		//每天美耶用户id
	private int userLevel;
	
	
	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	
	private Role role;	// 根据角色查询用户条件
	
	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
	
	private Userinfo userinfo;		//美容师信息完善
	private Userinfocontent userinfocontent;	//美容师图片路径
	private Speciality	speciality;			//用户特长
	private Franchisee franchisee;			//加盟商
	private UserLog userLog;				//用户操作日志
	
	private Skill skill;                  //美容师的技能标签
	private String sex;					//用户性别
	private String parendNames;			//所有父类name
	
	private List<Map<String, Object>> arealist;		//区域list
	
	private String result;           //添加用户时判断用户是否存在在每天美耶或者妃子校的标识
	private String layer;            //用户等级
	
	// new add kele 2016-11-3
	// 数据范围（1：所在部门及以下数据；2：按明细设置）
	private	int dataScope;//数据范围(1:所在部门及以下数据;2:按明细设置)
	private List<Office> officeList = Lists.newArrayList(); // 按明细设置数据范围
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "1";
	public static final String DATA_SCOPE_CUSTOM = "2";
	
	private FzxRole fzxRole; 	//妃子校菜单
	
	private String isRecommend;  //是否推荐（0：未推荐；1：推荐）
	
	private String appellationId;  //称谓标签id
	private String appellationName; //称谓名称

	private String companyIds; // 数据范围按商家区分
	private String companyNames; // 数据范围按商家区分
	
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public List<Map<String, Object>> getArealist() {
		return arealist;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public double getUsermoney() {
		return usermoney;
	}
	public void setUsermoney(double usermoney) {
		this.usermoney = usermoney;
	}
	public void setArealist(List<Map<String, Object>> arealist) {
		this.arealist = arealist;
	}
	public Userinfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(Userinfo userinfo) {
		this.userinfo = userinfo;
	}
	public Userinfocontent getUserinfocontent() {
		return userinfocontent;
	}
	public void setUserinfocontent(Userinfocontent userinfocontent) {
		this.userinfocontent = userinfocontent;
	}
	public Speciality getSpeciality() {
		return speciality;
	}
	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}
	public Franchisee getFranchisee() {
		return franchisee;
	}
	public void setFranchisee(Franchisee franchisee) {
		this.franchisee = franchisee;
	}
	public User() {
		super();
		this.loginFlag = Global.YES;
	}
	public User(String id){
		super(id);
	}
	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}
	public User(Role role){
		super();
		this.role = role;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	@SupCol(isUnique="true", isHide="true")
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}

	@JsonIgnore
	@NotNull(message="归属商家不能为空")
	@ExcelField(title="归属商家", align=2, sort=20)
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}
	
	@JsonIgnore
	@ExcelField(title="归属机构", align=2, sort=25)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@JsonIgnore
	@NotNull(message="机构编码不能为空")
	@ExcelField(title="机构编码", align=2, sort=26)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@JsonIgnore
	@NotNull(message="身份证号码不能为空")
	@ExcelField(title="身份证号", align=2, sort=45)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	
	@JsonIgnore
	@NotNull(message="入职日期不能为空")
	@ExcelField(title="入职日期", align=2, sort=75)
	public Date getInductionTime() {
		return inductionTime;
	}

	public void setInductionTime(Date inductionTime) {
		this.inductionTime = inductionTime;
	}
	
	@NotNull(message="工号不能为空")
	@Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
	@ExcelField(title="工号", align=2, sort=45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}



	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
//	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
//	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getDelRemarks() {
		return delRemarks;
	}

	public void setDelRemarks(String delRemarks) {
		this.delRemarks = delRemarks;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
//	导入用户默认为美容师  
//	@JsonIgnore
//	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称id, 多个角色名称用','分隔.
	 * @return
	 */
	public String getRoleIds() {
		return Collections3.extractToString(roleList, "id", ",");
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return Collections3.extractToString(roleList, "name", ",");
	}
	
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@Override
	public String toString() {
		return id;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getQrCode() {
		return qrCode;
	}

	public String getDelindex() {
		return delindex;
	}

	public void setDelindex(String delindex) {
		this.delindex = delindex;
	}

	public int getParentDel() {
		return parentDel;
	}

	public void setParentDel(int parentDel) {
		this.parentDel = parentDel;
	}

	public int getMtmyUserId() {
		return mtmyUserId;
	}

	public void setMtmyUserId(int mtmyUserId) {
		this.mtmyUserId = mtmyUserId;
	}

	public UserLog getUserLog() {
		return userLog;
	}

	public void setUserLog(UserLog userLog) {
		this.userLog = userLog;
	}
	public List<Office> getOfficeList() {
		return officeList;
	}
	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}
	public List<String> getOfficeIdList() {
		List<String> officeIdList = Lists.newArrayList();
		for (Office office : officeList) {
			officeIdList.add(office.getId());
		}
		return officeIdList;
	}

	public void setOfficeIdList(List<String> officeIdList) {
		officeList = Lists.newArrayList();
		for (String officeId : officeIdList) {
			Office office = new Office();
			office.setId(officeId);
			officeList.add(office);
		}
	}

	public String getOfficeIds() {
		return StringUtils.join(getOfficeIdList(), ",");
	}
	
	public void setOfficeIds(String officeIds) {
		officeList = Lists.newArrayList();
		if (officeIds != null){
			String[] ids = StringUtils.split(officeIds, ",");
			setOfficeIdList(Lists.newArrayList(ids));
		}
	}
	public int getDataScope() {
		return dataScope;
	}
	public void setDataScope(int dataScope) {
		this.dataScope = dataScope;
	}
	public Skill getSkill() {
		return skill;
	}
	public void setSkill(Skill skill) {
		this.skill = skill;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getParendNames() {
		return parendNames;
	}
	public void setParendNames(String parendNames) {
		this.parendNames = parendNames;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public FzxRole getFzxRole() {
		return fzxRole;
	}
	public void setFzxRole(FzxRole fzxRole) {
		this.fzxRole = fzxRole;
	}
	public String getIsRecommend() {
		return isRecommend;
	}
	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
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
	public String getCompanyIds() {
		return companyIds;
	}
	public void setCompanyIds(String companyIds) {
		this.companyIds = companyIds;
	}
	public String getCompanyNames() {
		return companyNames;
	}
	public void setCompanyNames(String companyNames) {
		this.companyNames = companyNames;
	}
	
}