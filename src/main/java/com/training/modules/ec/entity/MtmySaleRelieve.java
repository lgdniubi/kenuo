package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;

/**
 * 分销  用户关系及解除  实体类
 * @author coffee
 *
 */
public class MtmySaleRelieve extends DataEntity<MtmySaleRelieve>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Users users;			//mtmy用户
	private Users newusers;			//mtmy用户
	private User user;				//妃子笑用户
	private Office office;			//用户所属机构
	
	private int userId;				//被邀请人id（每天美耶用户）
	private String name;			//被邀请人name（每天美耶用户）
	private String mobile;			//被邀请人mobile（每天美耶用户）
	private Date date;				//邀请成功时间
	private int parentId;			//邀请人（妃子校用户、每天美耶用户）
	private String depth;			//级别关系类型（AB、BC、AC、CD）
	private int inviteType;			//邀请类型（1：未注册；2：已注册）
	private String invitationCode;	//邀请码
	private String layer;			//用户等级  用于查看用户BCD级用户标示
	
	private int status;				//解除申请状态（0：申请中；1：同意；2：拒绝）   用户关系状态（0：正常；1:解除申请）
	
	private String applyExplain;	//申请说明
	private String remark;			//审核备注
	
	private String tag;				//关键字（姓名、手机号）
	private Date startTime;			//开始时间
	private Date endTime;			//结束时间
	
	private int ABnum;				//A邀请B的用户上限
	private int ACnum;				//A邀请C的用户上限
	private int BCnum;				//B邀请C的用户上限
	private int CDnum;				//C邀请D的用户上限
	
	private String newParentId;		//新父类id
	
	private MtmySaleRelieveLog mtmySaleRelieveLog;	//用户订单关系
	
	private int lowerNum;			//下级用户数量
	
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public Users getNewusers() {
		return newusers;
	}
	public void setNewusers(Users newusers) {
		this.newusers = newusers;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public int getInviteType() {
		return inviteType;
	}
	public void setInviteType(int inviteType) {
		this.inviteType = inviteType;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public String getLayer() {
		return layer;
	}
	public void setLayer(String layer) {
		this.layer = layer;
	}
	public String getApplyExplain() {
		return applyExplain;
	}
	public void setApplyExplain(String applyExplain) {
		this.applyExplain = applyExplain;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public MtmySaleRelieveLog getMtmySaleRelieveLog() {
		return mtmySaleRelieveLog;
	}
	public void setMtmySaleRelieveLog(MtmySaleRelieveLog mtmySaleRelieveLog) {
		this.mtmySaleRelieveLog = mtmySaleRelieveLog;
	}
	public int getABnum() {
		return ABnum;
	}
	public void setABnum(int aBnum) {
		ABnum = aBnum;
	}
	public int getACnum() {
		return ACnum;
	}
	public void setACnum(int aCnum) {
		ACnum = aCnum;
	}
	public int getBCnum() {
		return BCnum;
	}
	public void setBCnum(int bCnum) {
		BCnum = bCnum;
	}
	public int getCDnum() {
		return CDnum;
	}
	public void setCDnum(int cDnum) {
		CDnum = cDnum;
	}
	public String getNewParentId() {
		return newParentId;
	}
	public void setNewParentId(String newParentId) {
		this.newParentId = newParentId;
	}
	public int getLowerNum() {
		return lowerNum;
	}
	public void setLowerNum(int lowerNum) {
		this.lowerNum = lowerNum;
	}
}
    