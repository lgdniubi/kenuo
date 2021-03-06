package com.training.modules.ec.entity;

import java.util.List;

import com.training.common.persistence.TreeEntity;
/**
 * 订单提成记录表
 * @author dalong
 *
 */
public class OrderPushmoneyRecord extends TreeEntity<OrderPushmoneyRecord> {
	
	private static final long serialVersionUID = 1L;
	private int pushmoneyRecordId;	//主键自动增长
	private String orderId;			//订单ID
	private String pushmoneyUserId;	//提成人员ID
	private String pushmoneyUserName;	//提成人员name
	private String pushmoneyUserMobile;	//提成人员手机
	private double pushMoney;		//提成金额
	private String operatorName;	//操作人
	private String flag;            //标识
	
	private String departmentName;    //提成人员部门
	private int departmentId;       //部门id
	private int turnOverDetailsId;    //店营业额详情表id
	private double pushMoneySum;     //该用户在某一订单的总提成金额
	private int type;                  //类型(1:下单，2:充值/还欠款，3:退款，4:账户充值)
	
	private String officeId;		//操作用户当前机构ID   2017-10-12 土豆添加
	private String userOfficeId;	//业务员机构ID  	 2017-10-25 土豆添加
	private String userOfficeIds;	//业务员机构IDS  	 2017-10-25 土豆添加
	
	//------------------售后添加字段-------------
	private String returnedId;		//退货订单号		 2017-11-16 土豆添加
	private String pushMoneys;		//获取编辑营业额的增减值字符串	2017-11-16 土豆添加
	private String positionKey;		//职位标签名		 2017-11-16 土豆添加
	private String positionValue;	//职位标签键值		 2017-11-16 土豆添加
	private String belongOfficeId;	//归属店铺id
	private String belongOfficeIds;	//归属店铺ids
	private String belongOfficeName;//归属店铺名称
	
	private List<OrderPushmoneyRecord> children;	// 父级菜单(售后服务营业额)
	
	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPushmoneyUserName() {
		return pushmoneyUserName;
	}

	public void setPushmoneyUserName(String pushmoneyUserName) {
		this.pushmoneyUserName = pushmoneyUserName;
	}

	public String getPushmoneyUserMobile() {
		return pushmoneyUserMobile;
	}

	public void setPushmoneyUserMobile(String pushmoneyUserMobile) {
		this.pushmoneyUserMobile = pushmoneyUserMobile;
	}

	public String getPushmoneyUserId() {
		return pushmoneyUserId;
	}

	public void setPushmoneyUserId(String pushmoneyUserId) {
		this.pushmoneyUserId = pushmoneyUserId;
	}

	public int getPushmoneyRecordId() {
		return pushmoneyRecordId;
	}

	public void setPushmoneyRecordId(int pushmoneyRecordId) {
		this.pushmoneyRecordId = pushmoneyRecordId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getPushMoney() {
		return pushMoney;
	}

	public void setPushMoney(double pushMoney) {
		this.pushMoney = pushMoney;
	}

	@Override
	public OrderPushmoneyRecord getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(OrderPushmoneyRecord parent) {
		// TODO Auto-generated method stub
		
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getUserOfficeId() {
		return userOfficeId;
	}

	public void setUserOfficeId(String userOfficeId) {
		this.userOfficeId = userOfficeId;
	}

	public String getUserOfficeIds() {
		return userOfficeIds;
	}

	public void setUserOfficeIds(String userOfficeIds) {
		this.userOfficeIds = userOfficeIds;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getTurnOverDetailsId() {
		return turnOverDetailsId;
	}

	public void setTurnOverDetailsId(int turnOverDetailsId) {
		this.turnOverDetailsId = turnOverDetailsId;
	}

	public double getPushMoneySum() {
		return pushMoneySum;
	}

	public void setPushMoneySum(double pushMoneySum) {
		this.pushMoneySum = pushMoneySum;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getReturnedId() {
		return returnedId;
	}

	public void setReturnedId(String returnedId) {
		this.returnedId = returnedId;
	}

	public String getPushMoneys() {
		return pushMoneys;
	}

	public void setPushMoneys(String pushMoneys) {
		this.pushMoneys = pushMoneys;
	}

	public String getPositionKey() {
		return positionKey;
	}

	public void setPositionKey(String positionKey) {
		this.positionKey = positionKey;
	}

	public String getPositionValue() {
		return positionValue;
	}

	public void setPositionValue(String positionValue) {
		this.positionValue = positionValue;
	}

	public List<OrderPushmoneyRecord> getChildren() {
		return children;
	}

	public void setChildren(List<OrderPushmoneyRecord> children) {
		this.children = children;
	}

	public String getBelongOfficeId() {
		return belongOfficeId;
	}

	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}

	public String getBelongOfficeName() {
		return belongOfficeName;
	}

	public void setBelongOfficeName(String belongOfficeName) {
		this.belongOfficeName = belongOfficeName;
	}

	public String getBelongOfficeIds() {
		return belongOfficeIds;
	}

	public void setBelongOfficeIds(String belongOfficeIds) {
		this.belongOfficeIds = belongOfficeIds;
	}
	
}
