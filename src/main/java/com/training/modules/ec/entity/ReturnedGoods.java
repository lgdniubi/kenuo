package com.training.modules.ec.entity;


import java.util.Date;
import java.util.List;

import com.training.common.persistence.TreeEntity;
/**
 *退货商品实体
 * @author zhangyang
 *
 */
public class ReturnedGoods extends TreeEntity<ReturnedGoods> {

	private static final long serialVersionUID = 1L;	//退货id
	private String returnedId;					//退货订单(主要用于接收退货订单中退货单号)
	private String orderId	;					//原订单id
	private String goodsMappingId	;			//退货商品 订单-商品ID
	private int userId;						//用户id
	private int isReal;					//是否为实物 0 实物 1 虚拟
	private int applyType;						//申请类型（0：退货并退款；1：仅换货;2：仅退款）
	private Date applyDate	;					//申请日期audit_by
	private String returnReason;				//退货原因
	private int returnNum;						//退货数量
	private double totalAmount;				//商品金额
	private double orderAmount;				//实付金额
	private double orderArrearage;				//欠款金额
	private double returnAmount;				//退款金额
	private String isStorage;					//货品状态（0：未入库；1：已入库）
	private String returnStatus;				//售后状态（-10：拒绝退货；11：申请退货；12：同意退货；13：退货中；14：退货完成；15：退款中；16：已退款；
													   //-20：拒绝换货；21：申请换货；22：同意换货；23：换货退货中；24：换货退货完成；25：换货中；26：换货完成）
	private String refusalCause;				//拒绝原因
	private String problemDesc;					//问题描述
	private String receiptBy;					//入库人
	private String financialBy;					//财务人员
	private Date updateDate;					//修改时间
	private String officeId;					//权限id（默认商家id）
	private String remarks;						//备注
	private Date receiptDate;					//入库时间
	private Date financialDate;					//退款时间
	private String auditBy;						//审核人
	private Date auditDate;						//审核时间
	private int warehouseId;					//仓库Id
	
	private String userName;					//用户名称
	private String mobile;						//用户手机号
	private Date begtime;						//查询 开始时间
	private Date endtime;						//查询 结束时间
	private int goodsNum;						//购买商品数量
	private String goodsName;					//商品名称
	private String specName;					//规格名称
	private String goodsPrice;					//商品价格
	private int serviceTimes;					//购买服务次数
	
	private String consignerName;				//发货人（消费者）
	private String consignerAddress;			//发货地址（消费者）
	private String consignerShippingName;		//发货快递公司（消费者）
	private String consignerShippingCode;		//发货快递单号（消费者）
	private Date consignerShippingTime;			//发货时间（消费者）
	private String consignerShippingImg;		//发货物流单图片（消费者）
	private String shippingName;				//物流公司名称（换货）
	private String shippingCode;				//物流单号（换货）
	private Date shippingTime;				//发货时间
	private String shipperBy;					//换货人
	private Date shipperDate;					//换货时间			

	List<ReturnedGoodsImages> imgList;			//退货图片list
	
	private int isConfirm;						//是否同意退货
	private String hoseName;					//仓库名称
	private String governor;					//管理员
	private String address;						//仓库地址	
    
	private String keyword;                     //搜索用关键字
	//---------------------------套卡使用字段-----------------------------------
	private List<Integer> recIds;			//卡项实物商品 id集合(mapping_id)
	private List<Integer> returnNums;		//卡项实物商品 售后数量集合
	private List<Integer> oldReturnNums;	//记录审核之前卡项实物商品 售后数量集合
	
	private String applyBy;                 //申请人
	private int returnType;					//退款方式（0：现金；1：微信；2：支付宝；3:银行账号；4:充值到每天美耶账户；5：原路退回）
	private String receiveName;				//收款人姓名
	private String receiveAccount;			//收款人账号
	private int oldReturnNum;				//退货数量
		
	public List<Integer> getRecIds() {
		return recIds;
	}
	public void setRecIds(List<Integer> recIds) {
		this.recIds = recIds;
	}
	public List<Integer> getReturnNums() {
		return returnNums;
	}
	public void setReturnNums(List<Integer> returnNums) {
		this.returnNums = returnNums;
	}
	public String getReturnedId() {
		return returnedId;
	}
	public void setReturnedId(String returnedId) {
		this.returnedId = returnedId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getShipperBy() {
		return shipperBy;
	}
	public void setShipperBy(String shipperBy) {
		this.shipperBy = shipperBy;
	}
	public Date getShipperDate() {
		return shipperDate;
	}
	public void setShipperDate(Date shipperDate) {
		this.shipperDate = shipperDate;
	}
	public String getHoseName() {
		return hoseName;
	}
	public void setHoseName(String hoseName) {
		this.hoseName = hoseName;
	}
	public String getGovernor() {
		return governor;
	}
	public void setGovernor(String governor) {
		this.governor = governor;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(int warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getAuditBy() {
		return auditBy;
	}
	public void setAuditBy(String auditBy) {
		this.auditBy = auditBy;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getRefusalCause() {
		return refusalCause;
	}
	public void setRefusalCause(String refusalCause) {
		this.refusalCause = refusalCause;
	}
	public String getProblemDesc() {
		return problemDesc;
	}
	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}
	public int getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(int isConfirm) {
		this.isConfirm = isConfirm;
	}
	public String getConsignerName() {
		return consignerName;
	}
	public void setConsignerName(String consignerName) {
		this.consignerName = consignerName;
	}
	public String getConsignerAddress() {
		return consignerAddress;
	}
	public void setConsignerAddress(String consignerAddress) {
		this.consignerAddress = consignerAddress;
	}
	public String getConsignerShippingName() {
		return consignerShippingName;
	}
	public void setConsignerShippingName(String consignerShippingName) {
		this.consignerShippingName = consignerShippingName;
	}
	public String getConsignerShippingCode() {
		return consignerShippingCode;
	}
	public void setConsignerShippingCode(String consignerShippingCode) {
		this.consignerShippingCode = consignerShippingCode;
	}
	public Date getConsignerShippingTime() {
		return consignerShippingTime;
	}
	public void setConsignerShippingTime(Date consignerShippingTime) {
		this.consignerShippingTime = consignerShippingTime;
	}
	public String getConsignerShippingImg() {
		return consignerShippingImg;
	}
	public void setConsignerShippingImg(String consignerShippingImg) {
		this.consignerShippingImg = consignerShippingImg;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public Date getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}
	public List<ReturnedGoodsImages> getImgList() {
		return imgList;
	}
	public void setImgList(List<ReturnedGoodsImages> imgList) {
		this.imgList = imgList;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getServiceTimes() {
		return serviceTimes;
	}
	public void setServiceTimes(int serviceTimes) {
		this.serviceTimes = serviceTimes;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Date getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}
	public Date getFinancialDate() {
		return financialDate;
	}
	public void setFinancialDate(Date financialDate) {
		this.financialDate = financialDate;
	}
	public int getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getBegtime() {
		return begtime;
	}
	public void setBegtime(Date begtime) {
		this.begtime = begtime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodsMappingId() {
		return goodsMappingId;
	}
	public void setGoodsMappingId(String goodsMappingId) {
		this.goodsMappingId = goodsMappingId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getIsReal() {
		return isReal;
	}
	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}
	public int getApplyType() {
		return applyType;
	}
	public void setApplyType(int applyType) {
		this.applyType = applyType;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public double getOrderArrearage() {
		return orderArrearage;
	}
	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}
	public double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public String getIsStorage() {
		return isStorage;
	}
	public void setIsStorage(String isStorage) {
		this.isStorage = isStorage;
	}
	public String getReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}
	public String getReceiptBy() {
		return receiptBy;
	}
	public void setReceiptBy(String receiptBy) {
		this.receiptBy = receiptBy;
	}
	public String getFinancialBy() {
		return financialBy;
	}
	public void setFinancialBy(String financialBy) {
		this.financialBy = financialBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/*public String getBelongUserId() {
		return belongUserId;
	}
	public void setBelongUserId(String belongUserId) {
		this.belongUserId = belongUserId;
	}
	public String getBelongOfficeId() {
		return belongOfficeId;
	}
	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}
	
	public String getBelongUserName() {
		return belongUserName;
	}
	public void setBelongUserName(String belongUserName) {
		this.belongUserName = belongUserName;
	}
	public String getBelongOfficeName() {
		return belongOfficeName;
	}
	public void setBelongOfficeName(String belongOfficeName) {
		this.belongOfficeName = belongOfficeName;
	}*/
	@Override
	public ReturnedGoods getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(ReturnedGoods parent) {
		// TODO Auto-generated method stub
		
	}
	public String getApplyBy() {
		return applyBy;
	}
	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}
	public int getReturnType() {
		return returnType;
	}
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveAccount() {
		return receiveAccount;
	}
	public void setReceiveAccount(String receiveAccount) {
		this.receiveAccount = receiveAccount;
	}
	public int getOldReturnNum() {
		return oldReturnNum;
	}
	public void setOldReturnNum(int oldReturnNum) {
		this.oldReturnNum = oldReturnNum;
	}
	public List<Integer> getOldReturnNums() {
		return oldReturnNums;
	}
	public void setOldReturnNums(List<Integer> oldReturnNums) {
		this.oldReturnNums = oldReturnNums;
	}
	
}
