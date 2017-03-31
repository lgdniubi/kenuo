package com.training.modules.ec.entity;


import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.entity.Userinfo;
/**
 * 预约实体类
 * @author coffee
 *
 */
public class Reservation extends DataEntity<Reservation>{

	private static final long serialVersionUID = 1L;
	
	private int reservationId;			//预约id
	private String goodsMappingId;		//匹配商品id
	private int goodsId;				//商品id
	private String beauticianId;		//美容师id
	private int userId;					//消费者id
	private String userPhone;			//用户电话
	private String userName;			//用户姓名
	private Date apptDate;				//预约日期
	private String apptStartTime;		//预约开始时间
	private String apptEndTime;			//预约结束时间
	private String shopId;				//预约店铺id
	private double performance;			//业绩
	private String apptStatus;			//预约状态	0：等待服务；1：已完成；2：已评价；3：已取消；4：客户爽约
	private String channelFlag;			//渠道标识（wap：wap端；ios：苹果手机；android：安卓手机；bm：后台管理）
	
	private Date beginDate;				//开始时间
	private Date endDate;				//结束时间
	private String keyword;				//关键字
	
	private User user;					//美容师表
	private Orders order;				//订单实体类
	private Office office;				//店铺机构实体类
	private Goods goods;				//商品实体类
	
	
	private Integer teachersStarLevel;  //sharp 3月27所加，美容师评级
	public Integer getTeachersStarLevel() {
		return teachersStarLevel;
	}
	public void setTeachersStarLevel(Integer teachersStarLevel) {
		this.teachersStarLevel = teachersStarLevel;
	}
	
	
	public int getReservationId() {
		return reservationId;
	}
	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}
	public String getGoodsMappingId() {
		return goodsMappingId;
	}
	public void setGoodsMappingId(String goodsMappingId) {
		this.goodsMappingId = goodsMappingId;
	}
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
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
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getApptDate() {
		return apptDate;
	}
	public void setApptDate(Date apptDate) {
		this.apptDate = apptDate;
	}
	public String getApptStartTime() {
		return apptStartTime;
	}
	public void setApptStartTime(String apptStartTime) {
		this.apptStartTime = apptStartTime;
	}
	public String getApptEndTime() {
		return apptEndTime;
	}
	public void setApptEndTime(String apptEndTime) {
		this.apptEndTime = apptEndTime;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public double getPerformance() {
		return performance;
	}
	public void setPerformance(double performance) {
		this.performance = performance;
	}
	public String getApptStatus() {
		return apptStatus;
	}
	public void setApptStatus(String apptStatus) {
		this.apptStatus = apptStatus;
	}
	public String getChannelFlag() {
		return channelFlag;
	}
	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
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
