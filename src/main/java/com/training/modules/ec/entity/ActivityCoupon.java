package com.training.modules.ec.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷表
 * @author water
 *
 */
public class ActivityCoupon extends TreeEntity<ActivityCoupon> {
	
	
	private static final long serialVersionUID = 1L;
	private String  couponName;     //优惠券名称
	private String  couponType;    	 //红包类型：1：商品详情页；2：活动页 ,3 新注册 4.通用红包
	private int usedNum;			 //使用数量
	private int actionId;			 //活动id	
	private double baseAmount;		 //满减金额
	private double couponMoney;	 //金额
	private int totalNumber;		 //发放数量
	private int couponNumber;		 //剩余数量
//	private Date   expirationDate;    //截止时间
//	private Date   getBegintime;      //领取开始时间
//	private Date   getEndtime;        //领取截止时间
	private int   usedType;      //使用类型（1:全部分类 2：商品分类；3：指定商品)
//	private String   goodsValue;      //商品值（商品分类：分类id；指定商品：优惠券商品mapping表id)
	private int  status;             //优惠券状态（0：开始；1：已关闭)
//	private String createBy;         	//创建人
	private Date   createDate;        ///创建时间
//	private String   updateBy;         //修改人
	private Date   updateDate;         //修改时间
	private String categoryId;			//分类id
	private String categoryName;		//分类name
	private String goodsId;				//商品id
	private String goodsName;			//商品name
	private String actionType;			//活动类型
	private String oneCategoryId;		//一级分类
	private String oneCategoryName;		//一级分类名称
	private String cateId;				//分类id集合
	private String cateName;			//分类名称集合
	private int franchiseeId;			//所属商家
	private String franchiseeName;		//商家名称
	
	private int ceiling;				//领取上限
	
	private List<ActivityCouponGoods> list;			//优惠卷商品关联表
	private List<ActivityCouponCategory> catelist;	//优惠卷分类关联表
	private int sendType;				//推送方式
	
	private String userId;				//用户id 集合

	

	//private int status;
	//private String create_by;


	public String getFranchiseeName() {
		return franchiseeName;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public double getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(double couponMoney) {
		this.couponMoney = couponMoney;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getCouponNumber() {
		return couponNumber;
	}

	public void setCouponNumber(int couponNumber) {
		this.couponNumber = couponNumber;
	}

	public void setFranchiseeName(String franchiseeName) {
		this.franchiseeName = franchiseeName;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	
	public int getFranchiseeId() {
		return franchiseeId;
	}

	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

	
	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getOneCategoryId() {
		return oneCategoryId;
	}

	public void setOneCategoryId(String oneCategoryId) {
		this.oneCategoryId = oneCategoryId;
	}

	public String getOneCategoryName() {
		return oneCategoryName;
	}

	public void setOneCategoryName(String oneCategoryName) {
		this.oneCategoryName = oneCategoryName;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public int getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
	

	public List<ActivityCouponGoods> getList() {
		return list;
	}

	public void setList(List<ActivityCouponGoods> list) {
		this.list = list;
	}

	public List<ActivityCouponCategory> getCatelist() {
		return catelist;
	}

	public void setCatelist(List<ActivityCouponCategory> catelist) {
		this.catelist = catelist;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public int getUsedType() {
		return usedType;
	}

	public void setUsedType(int usedType) {
		this.usedType = usedType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public int getCeiling() {
		return ceiling;
	}

	public void setCeiling(int ceiling) {
		this.ceiling = ceiling;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public ActivityCoupon getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(ActivityCoupon parent) {
		// TODO Auto-generated method stub
		
	}
	

}
