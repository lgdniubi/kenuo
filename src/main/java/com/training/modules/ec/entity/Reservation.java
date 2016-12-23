package com.training.modules.ec.entity;


import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
/**
 * 预约实体类
 * @author coffee
 *
 */
public class Reservation extends DataEntity<Reservation>{

	private static final long serialVersionUID = 1L;
	
	private int reservationId;			//预约id
	private String beauticianId;		//美容师id
	private int userId;					//消费者id
	private String userPhoneNum;		//用户电话
	private String userName;			//用户姓名
	private Date reservationDayTime;	//预约日期
	private Date beginTime;				//预约开始时间
	private Date endTime;				//预约结束时间
	private String shopId;				//预约店铺id
	private String reservationStatus;	//预约状态	0 等待服务 1取消 2已完成 3已评价 4客户爽约
	private Date createdTime;			//添加时间
	
	private Date beginDate;				//开始时间
	private Date endDate;				//结束时间
	private String keyword;				//关键字
	
	private User user;					//美容师表
	private Orders order;				//订单实体类
	private Office office;				//店铺机构实体类
	private Goods goods;				//商品实体类
	
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public String getBeauticianId() {
		return beauticianId;
	}
	public void setBeauticianId(String beauticianId) {
		this.beauticianId = beauticianId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserPhoneNum() {
		return userPhoneNum;
	}
	public void setUserPhoneNum(String userPhoneNum) {
		this.userPhoneNum = userPhoneNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getReservationDayTime() {
		return reservationDayTime;
	}
	public void setReservationDayTime(Date reservationDayTime) {
		this.reservationDayTime = reservationDayTime;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	@Length(min=1, max=1)
	public String getReservationStatus() {
		return reservationStatus;
	}
	public void setReservationStatus(String reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Orders getOrder() {
		return order;
	}
	public void setOrder(Orders order) {
		this.order = order;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
}
