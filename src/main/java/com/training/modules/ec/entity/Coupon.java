package com.training.modules.ec.entity;

import java.util.Date;
import java.util.List;

import com.training.common.persistence.TreeEntity;

/**
 * 优惠卷表
 * @author water
 *
 */
public class Coupon extends TreeEntity<Coupon> {
	
	
	private static final long serialVersionUID = 1L;
	private int   couponId;        //优惠券ID、主键、自动增长
	private String  couponName;     //优惠券名称
	private String  couponType;     //优惠券类型（1：商品详情页；2：活动页）后续依次增加
	private int usedNum;			//使用数量
	private Date   expirationDate;    //截止时间
	private Date   getBegintime;      //领取开始时间
	private Date   getEndtime;        //领取截止时间
	private int   goodsUseType;      //使用类型（1:全部分类 2：商品分类；3：指定商品)
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
	private String amountId;			//金额集合
	private String amountName;			//金额名称集合
	
	private List<CouponGoodsMapping> list;			//优惠卷商品关联表
	private List<CouponCategoryMapping> catelist;	//优惠卷分类关联表
	private int sendType;				//推送方式
	
	private String userId;				//用户id 集合


	
	
	
	public String getFranchiseeName() {
		return franchiseeName;
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

	public String getAmountId() {
		return amountId;
	}

	public void setAmountId(String amountId) {
		this.amountId = amountId;
	}

	public String getAmountName() {
		return amountName;
	}

	public void setAmountName(String amountName) {
		this.amountName = amountName;
	}

	public int getFranchiseeId() {
		return franchiseeId;
	}

	public void setFranchiseeId(int franchiseeId) {
		this.franchiseeId = franchiseeId;
	}

	public List<CouponCategoryMapping> getCatelist() {
		return catelist;
	}

	public void setCatelist(List<CouponCategoryMapping> catelist) {
		this.catelist = catelist;
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
	public List<CouponGoodsMapping> getList() {
		return list;
	}

	public void setList(List<CouponGoodsMapping> list) {
		this.list = list;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
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

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getGetBegintime() {
		return getBegintime;
	}

	public void setGetBegintime(Date getBegintime) {
		this.getBegintime = getBegintime;
	}

	public Date getGetEndtime() {
		return getEndtime;
	}

	public void setGetEndtime(Date getEndtime) {
		this.getEndtime = getEndtime;
	}

	public int getGoodsUseType() {
		return goodsUseType;
	}

	public void setGoodsUseType(int goodsUseType) {
		this.goodsUseType = goodsUseType;
	}

//	public String getGoodsValue() {
//		return goodsValue;
//	}
//
//	public void setGoodsValue(String goodsValue) {
//		this.goodsValue = goodsValue;
//	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	public Coupon getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Coupon parent) {
		// TODO Auto-generated method stub
		
	}
	

}
