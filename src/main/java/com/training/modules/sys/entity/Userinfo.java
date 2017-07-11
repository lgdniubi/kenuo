package com.training.modules.sys.entity;



import java.util.Date;
import java.util.List;

import com.training.common.persistence.DataEntity;

public class Userinfo extends DataEntity<Userinfo> {
	
	private static final long serialVersionUID = 1L;
	
	private String userid;		//用户id
	private int sex;			//性别 1男 2女
	private Date birthday;		//生日
	private String nativearea;	//籍贯
	private String workarea;	//工作地址
	private String position;	//职位
	private int userlevel;		//等级
	private String franchiseeid;	//加盟商id
	private String officeid;		//实体店id

	private	 String selfintro;	//自我评价
	private Date createtime;	//创建时间
	private Date updatetime;	//更新时间
	
	private Franchisee franchisee;		//加盟商
	private Area areaP;			//区域
	private Area areaC;
	private List<Userinfocontent> infocontlist;		//用户图片集合
	
	private String teachersComment;       //导师对美容师的评价
	private int teachersStarLevel;      //美容师星级(一共五颗星，1：一颗星，2：两颗星，以此类推)
	
	private Date workYear;            //从业年限
	private String serviceManifesto;   //服务宣言
	
	private String teachersName;       //培训师姓名
	
	public List<Userinfocontent> getInfocontlist() {
		return infocontlist;
	}
	public void setInfocontlist(List<Userinfocontent> infocontlist) {
		this.infocontlist = infocontlist;
	}
	public Franchisee getFranchisee() {
		return franchisee;
	}
	public void setFranchisee(Franchisee franchisee) {
		this.franchisee = franchisee;
	}
	
	public Area getAreaP() {
		return areaP;
	}
	public void setAreaP(Area areaP) {
		this.areaP = areaP;
	}
	public Area getAreaC() {
		return areaC;
	}
	public void setAreaC(Area areaC) {
		this.areaC = areaC;
	}
	public String getFranchiseeid() {
		return franchiseeid;
	}
	public void setFranchiseeid(String franchiseeid) {
		this.franchiseeid = franchiseeid;
	}
	public String getOfficeid() {
		return officeid;
	}
	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	public String getUserid() {
		return userid;
	}
	public String getWorkarea() {
		return workarea;
	}
	public void setWorkarea(String workarea) {
		this.workarea = workarea;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getNativearea() {
		return nativearea;
	}
	public void setNativearea(String nativearea) {
		this.nativearea = nativearea;
	}

	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}
	public String getSelfintro() {
		return selfintro;
	}
	public void setSelfintro(String selfintro) {
		this.selfintro = selfintro;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getTeachersComment() {
		return teachersComment;
	}
	public void setTeachersComment(String teachersComment) {
		this.teachersComment = teachersComment;
	}
	public int getTeachersStarLevel() {
		return teachersStarLevel;
	}
	public void setTeachersStarLevel(int teachersStarLevel) {
		this.teachersStarLevel = teachersStarLevel;
	}
	public Date getWorkYear() {
		return workYear;
	}
	public void setWorkYear(Date workYear) {
		this.workYear = workYear;
	}
	public String getServiceManifesto() {
		return serviceManifesto;
	}
	public void setServiceManifesto(String serviceManifesto) {
		this.serviceManifesto = serviceManifesto;
	}
	public String getTeachersName() {
		return teachersName;
	}
	public void setTeachersName(String teachersName) {
		this.teachersName = teachersName;
	}
	
	
}
