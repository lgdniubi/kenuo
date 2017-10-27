package com.training.modules.ec.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;

/**
 * 评论实体类
 * @author coffee
 *
 */
public class Comment extends DataEntity<Comment>{

	private static final long serialVersionUID = 1L;
	
	private int commentId;		//评论id
	private String goodsId;		//商品id
	private String email;		//email邮箱
	private String contents;	//评论内容
	private int deliverRank;	//物流评价等级
	private Date addTime;		//添加时间
	private String ipAddress;	//ip地址
	private int isShow;			//是否显示
	private int parentId;		//父id
	private int userId;			//评论用户
	private String img;			//晒单图片
	private String orderId;		//订单id
	private int goodsRank;		//商品评价等级  或   美容师服务等级
	private int serviceRank;	//商家服务态度评价等级
	private String replyId;		//回复评论id
	
	private Goods goods;		//商品实体类
	private Users users;		//消费者实体类
	
	private String beautyId;	//美容师id
	private User user;			//美容师实体类
	
	private String newTime;		//String类型的时间
	
	private Date beginDate;		//开始时间
	private Date endDate;		//结束时间
	
	private String userName;	//用户姓名
	private String usersName;	//美容师姓名
	
	private String shopId;		//店铺id
	private String apptOrderId;	//预约订单ID
	private float shopRank;		//店铺评价等级
	private Office office;		//店铺信息
	private String shopName;	//店铺名称
	
	private String whetherImg;   //是否有图
	
	/**
	 * sharp 所加，用于查询每个预约下的评论 
	 */
	private String reservationId;	//预约Id
	
	public String getReservationId() {
		return reservationId;
	}
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getDeliverRank() {
		return deliverRank;
	}
	public void setDeliverRank(int deliverRank) {
		this.deliverRank = deliverRank;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getGoodsRank() {
		return goodsRank;
	}
	public void setGoodsRank(int goodsRank) {
		this.goodsRank = goodsRank;
	}
	public int getServiceRank() {
		return serviceRank;
	}
	public void setServiceRank(int serviceRank) {
		this.serviceRank = serviceRank;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public String getBeautyId() {
		return beautyId;
	}
	public void setBeautyId(String beautyId) {
		this.beautyId = beautyId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getNewTime() {
		return newTime;
	}
	public void setNewTime(String newTime) {
		this.newTime = newTime;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getApptOrderId() {
		return apptOrderId;
	}
	public void setApptOrderId(String apptOrderId) {
		this.apptOrderId = apptOrderId;
	}
	public float getShopRank() {
		return shopRank;
	}
	public void setShopRank(float shopRank) {
		this.shopRank = shopRank;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getWhetherImg() {
		return whetherImg;
	}
	public void setWhetherImg(String whetherImg) {
		this.whetherImg = whetherImg;
	}
	
	
}
