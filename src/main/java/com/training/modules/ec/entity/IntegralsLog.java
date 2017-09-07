package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 充值，处理预约金送云币实体类
 * @author xiaoye   2017年7月31日
 *
 */
public class IntegralsLog extends DataEntity<IntegralsLog>{

	private static final long serialVersionUID = 1L;
	
	private int integralsLogId;   //id
	private int userId;          //用户id
	private int integralType;     //云币属性类型（0:收入；1：支出）
	private int integralSource;    //积分来源（0：每天美耶；1：妃子校）
	private int actionType;       //动作类型（1:评论；2：意见反馈；3：取消预约 4：发布内容；5：收藏 6: 邀请奖励 7： 分销  8 ：购买奖励 9： 购买支出 10： 取消返还  11：订单过期 12：退货扣除  13： 退货返还 14 ：邀请注册 15 ：邀请首单返利 16： 按时奖励 17：分享奖励 18 ：爽约扣除 ，19：取消收藏，20：评价美容师  ,21:商品赠送）
	private int integral;        //云币（正：收入；负：支出）(四舍五入)
	private String orderId;      //订单id(购买商品)
	private String remark;        //备注
	private String createTime;   //创建时间
	
	public int getIntegralsLogId() {
		return integralsLogId;
	}
	public void setIntegralsLogId(int integralsLogId) {
		this.integralsLogId = integralsLogId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getIntegralType() {
		return integralType;
	}
	public void setIntegralType(int integralType) {
		this.integralType = integralType;
	}
	public int getIntegralSource() {
		return integralSource;
	}
	public void setIntegralSource(int integralSource) {
		this.integralSource = integralSource;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
