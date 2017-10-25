package com.training.modules.ec.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
/**
 * 订单实体
 * @author zhangyang
 *
 */
public class Orders extends TreeEntity<Orders> {
	
	private static final long serialVersionUID = 1L;
	
	private String orderid;				//订单id
//	private int addressid;				//地址id
	private String status;				//到处订单状态
	private int shippingstatus;			//发货状态
//	private int paystatus;				//支付状态
	private String consignee;			//收货人
//	private String country;				//国家
//	private String province;			//省份
	private	String city;				//城市
	private String cityname;			//城市名称
//	private	 String district;			//县区s
//	private String twonl;				//城镇
	private String address;				//地址
//	private String zipcode;				//邮政编码
	private String phone;				//收货者固定电话
//	private String email;				//邮箱
	private String shopId;              //店铺id
	private String shopName;            //店铺名称
	private int shippingtype;    	 	//0 快递发货 1到店自取 2 无需发货
	private String shippingcode;		//物流code
	private Date shippingtime;			//发货时间
	private String shippingname;		//物流名称
	private Date returnTime;			//退货期时间
	private int payid;					//支付方式id
	private String paycode;				//支付code
	private String payname;				//支付方式名称
	private String invoicetitle;		//发票抬头
	private double shippingprice;		//邮费
	private double usermoney;			//使用的余额
	private double couponprice;			//优惠价格
	private int integral;				//积分
	private double integralmoney;		//积分抵多少钱
	
	private double goodsprice;			//商品总价
	private double orderamount;			//应付款金额
	private double totalamount;			//实付款金额
	private double orderBalance;		//订单余款
	private double orderArrearage;		//订单欠款
	private Integer orderArrearageType; //欠款类型 是否有欠款
	
	private Date addtime;				//下单时间
	private Date confirmtime;			//收货时间
	private Date  paytime;				//支付时间
	private int actionid;				//活动id
	private	double orderactionamount;	//活动优惠金额
	private double discount;			//价格调整
	private String usernote;			//用户备注
	private String adminnote;			//管理员备注
	private String parentid;			//父单号id
	private String username;			//购买者姓名
	private String mobile;				//收货者手机号码
	private String mobile1;				//收货者手机号码
	private String goodsType;			//商品类型
	private int goodsnum;				//购买数量
	private Date begtime;				//查询条件开始时间
	private Date endtime;				//查询条件结束时间
	private String keyword;				//关键字
	private int marketprice;			//市场标签
	private int goodsid;				//商品id
	private String goodsSn;				//货号
	private String goodsname;			//商品name
	private int createlogo;				//订单标识 1 客户端创建 2 后台创建
	private String speckey;				//规格key
	private String speckeyname;			//规格名称
	private String cetaid;				//商品分类id
	private String catename;			//商品分类name
	private String goodsids;			//查询条件
	private String postalcode;			//邮政编码
	private int remaintimes;			//剩余服务次数
	private Office office;				//实体店对象
	private Users users;				//订单用户
	private OrderGoods orderGoods;		//商品
	private AcountLog acountLog;		//操作日志
	private int oldstatus;				//修改前订单状态
	private String tempOrderId;			//交易号
	private String shipType;			//0 快递发货 1到店自取 2 无需发货 作为excel 导出
	private String pid;					//订单父id
	private int isShow;					//是否展示
	private int isReal;					//是否实物
	private String searchIsReal;		//搜索条件是否实物
	private String orderIsReal;			//是否实物(导出字段)
	private String channelFlag;			//渠道标识（WAP：wap端；IOS：苹果手机；Android：安卓手机；BM：后台管理）
	private double memberGoodsPrice;	//会员折扣优惠了多少
	
	private String goodselectId;		//商品集合
	private String number;				//商品购买数量集合
	private int isDiscount;				//是否打折
	private String goodsTotalPrice;		//商品总价格
	private String residue;				//剩余服务次数集合
	private String officeId;			//机构id
	
	//导出字段
	private String usersId;               //用户id
	private String usersMobile;       //用户手机号（每天美耶的手机号）
	private String strGoodsNum;			//商品数量
	private String strGoodsPrice;		//商品价格
	private String strOrderAmount;		//订单价格
	private String strTotalAmount;		//商品实付金额
	private String orderTotalAmount;    //订单实付金额
	private String goodsNo;				//货号
	private String barCode;				//商品条形码
	private String newShippingPrice;       //邮费
	
	private double singleNormPrice;		//单次标价
	private double singleRealityPrice;	//实际服务单次价
	private double afterPayment;		//实际付款（后）
	private double actualPayment;		//实际付款（前）
	private int  actualNum;				//实际次数
	private double debtMoney;			//欠款
	private	double spareMoney;			//余款
	private Date invoiceOvertime;		//过期时间（YYYYMMDD）
	private String payTime;             //支付时间
	//-------------------------------保存需要传入后台的数据---------------------------------------------
	private List<Integer> goodselectIds;	//商品订单 商品id
	private List<Double> orderAmounts;		//商品订单 成交价
	private List<String> speckeys;			//商品订单 规格key
	private List<Double> actualPayments;	//商品订单 实际付款（前）
	private List<Integer> kindgoodsnum;		//实物商品购买数量集合
	private int userid;						//用户id
	private int distinction;				//订单性质（0：电商；1：售前卖；2：售中卖；3：老带新）
	private int isNeworder;					//新老订单
	private int orderstatus;				//订单状态
	private int invoiceType;				//发票类型
	private String personheadContent;		//个人发票抬头
	private String companyheadContent;		//公司发票抬头
	private String taxNum;					//纳税人识别号
	private String bankNo;					//银行账号
	private String bankName;				//开户行
	private	String invoicePhone;			//发票电话
	private	String invoiceAddress;			//发票地址
	private	String invoiceContent;			//发票内容
	private	double invoiceAmount;			//发票金额
	private int Ichecks;					//是否索要发票
	private String headContent;				//抬头内容
	private String recipientsName;			//发票收件人
	private String recipientsPhone;			//发票收件人电话
	private String recipientsAddress;		//发票收件人地址
	private int invoiceId;					//发票自增id
	//提成人员信息
	/*private List<Integer> mtmyUserId;		//提成人员ID*/	 //小叶删除   提成人员由每天美耶改成妃子校
	private List<String> sysUserId;		//提成人员ID
	private List<Double> pushMoney;			//提成金额
	private List<OrderRemarksLog> orderRemarksLog;		//订单备注返回对象
	private List<String> orderRemarks;		//订单备注信息存储数据
	private String orderRemark;				//单条订单备注信息存储数据
	private List<Integer> remaintimeNums;		//实际次数  --新加属性
	private OrderInvoice orderInvoice;		//订单发票对象
	private User user;                       //妃子校用户
	//--------------------用户账户需要字段------------------------------------------
	private double accountBalance;		//账户余额
	private double accountArrearage;	//账户欠款
	//------------------修改页面需要显示的字段----------------------------------------
	private List<OrderGoods> orderGoodList;
	List<OrderPushmoneyRecord> orderPushmoneyRecords;
	List<OrderGoodsCoupon> orderGoodsCoupons;	//订单红包
	//------------------CRM根据用户查询需要显示的字段----------------------------------
	private String isComment;
	
	private int flag;						//分销是否处理 分销结算标示（0：未结算；1：已结算）
	private String strReal;				//导出 字段 订单类型
	private String strChannel;		//订单创建类型
	private String strAddTime;		//导出字段 下单时间
	
	private String newProvince;     //导出字段  省
	private String newCity;         //导出字段  市
	private String newDistrict;     //导出字段  县
	
	private String oldAddress;      //修改前的地址
	private String newFlag;         //标识   判断当前订单的收货地址是否修改，0 未修改 ，1修改
	
	private String userDelFlag;		//订单类型（0：正常 1：用户删除）
	private String cancelType;		//订单取消类型
	private int isInvoice;		//是否可开发票
	 
	private int num;         //订单的发票个数
	
	private String newIsNeworder;   //查询所用的新老订单字段
	
	private Date payBegTime;    //支付开始时间，用于查询
	private Date payEndTime;    //支付结束时间，用于查询
	
	private List<Date> realityAddTimeList;       //实际下单时间
	
	private int userIntegral;                   //充值或者处理预约金全部的欠款后送的云币
	
	private String belongOfficeId;           //归属机构id
	private String belongOfficeName;         //归属机构名称
	private String belongUserId;            //归属人id
	private String belongUserName;          //归属人名称
	
	public String getSearchIsReal() {
		return searchIsReal;
	}
	public void setSearchIsReal(String searchIsReal) {
		this.searchIsReal = searchIsReal;
	}
	@JsonIgnore
	@ExcelField(title="创建类型", align=2, sort=5)
	public String getStrChannel() {
		return strChannel;
	}
	public void setStrChannel(String strChannel) {
		if(strChannel.equals("bm")){
			this.strChannel = "后台订单";
		}else if(strChannel.equals("wap")){
			this.strChannel = "wap端订单";
		}else if(strChannel.equals("ios")){
			this.strChannel = "苹果端订单";
		}else if(strChannel.equals("android")){
			this.strChannel = "安卓端订单";
		}
		
	}
	
	public String getStrReal() {
		return strReal;
	}
	public void setStrReal(String strReal) {
		if(strReal.equals("0")){
			this.strReal="实物";
		}else if(strReal.equals("1")){
			this.strReal="虚拟";
		}
		
	}

	private String userNote;            //买家留言
	private String adminNote;           //备注
	
	@JsonIgnore
	@ExcelField(title="商品货号", align=2, sort=74)
	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	@JsonIgnore
	@ExcelField(title="商品条形码", align=2, sort=73)
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	@JsonIgnore
	@ExcelField(title="商品实付金额", align=2, sort=70)
	public String getStrTotalAmount() {
		return strTotalAmount;
	}

	public void setStrTotalAmount(String strTotalAmount) {
		this.strTotalAmount = strTotalAmount;
	}

	public List<Integer> getKindgoodsnum() {
		return kindgoodsnum;
	}

	public void setKindgoodsnum(List<Integer> kindgoodsnum) {
		this.kindgoodsnum = kindgoodsnum;
	}

	public double getMemberGoodsPrice() {
		return memberGoodsPrice;
	}

	public void setMemberGoodsPrice(double memberGoodsPrice) {
		this.memberGoodsPrice = memberGoodsPrice;
	}

	public List<OrderGoodsCoupon> getOrderGoodsCoupons() {
		return orderGoodsCoupons;
	}

	public void setOrderGoodsCoupons(List<OrderGoodsCoupon> orderGoodsCoupons) {
		this.orderGoodsCoupons = orderGoodsCoupons;
	}

	public Integer getOrderArrearageType() {
		return orderArrearageType;
	}

	public void setOrderArrearageType(Integer orderArrearageType) {
		this.orderArrearageType = orderArrearageType;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public List<OrderRemarksLog> getOrderRemarksLog() {
		return orderRemarksLog;
	}

	public void setOrderRemarksLog(List<OrderRemarksLog> orderRemarksLog) {
		this.orderRemarksLog = orderRemarksLog;
	}


	public List<String> getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(List<String> orderRemarks) {
		this.orderRemarks = orderRemarks;
	}

	public OrderInvoice getOrderInvoice() {
		return orderInvoice;
	}

	public void setOrderInvoice(OrderInvoice orderInvoice) {
		this.orderInvoice = orderInvoice;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getRecipientsName() {
		return recipientsName;
	}

	public void setRecipientsName(String recipientsName) {
		this.recipientsName = recipientsName;
	}

	public String getRecipientsPhone() {
		return recipientsPhone;
	}

	public void setRecipientsPhone(String recipientsPhone) {
		this.recipientsPhone = recipientsPhone;
	}

	public String getRecipientsAddress() {
		return recipientsAddress;
	}

	public void setRecipientsAddress(String recipientsAddress) {
		this.recipientsAddress = recipientsAddress;
	}

	public String getHeadContent() {
		return headContent;
	}

	public void setHeadContent(String headContent) {
		this.headContent = headContent;
	}

	public int getIchecks() {
		return Ichecks;
	}

	public void setIchecks(int ichecks) {
		Ichecks = ichecks;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getPersonheadContent() {
		return personheadContent;
	}

	public void setPersonheadContent(String personheadContent) {
		this.personheadContent = personheadContent;
	}

	public String getCompanyheadContent() {
		return companyheadContent;
	}

	public void setCompanyheadContent(String companyheadContent) {
		this.companyheadContent = companyheadContent;
	}

	public String getTaxNum() {
		return taxNum;
	}

	public void setTaxNum(String taxNum) {
		this.taxNum = taxNum;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getInvoicePhone() {
		return invoicePhone;
	}

	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Date getInvoiceOvertime() {
		return invoiceOvertime;
	}

	public void setInvoiceOvertime(Date invoiceOvertime) {
		this.invoiceOvertime = invoiceOvertime;
	}

	
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public List<OrderPushmoneyRecord> getOrderPushmoneyRecords() {
		return orderPushmoneyRecords;
	}

	public void setOrderPushmoneyRecords(List<OrderPushmoneyRecord> orderPushmoneyRecords) {
		this.orderPushmoneyRecords = orderPushmoneyRecords;
	}

	public List<Double> getPushMoney() {
		return pushMoney;
	}

	public void setPushMoney(List<Double> pushMoney) {
		this.pushMoney = pushMoney;
	}

	public String getChannelFlag() {
		return channelFlag;
	}

	public void setChannelFlag(String channelFlag) {
		this.channelFlag = channelFlag;
	}

	public List<OrderGoods> getOrderGoodList() {
		return orderGoodList;
	}

	public void setOrderGoodList(List<OrderGoods> orderGoodList) {
		this.orderGoodList = orderGoodList;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getAccountArrearage() {
		return accountArrearage;
	}

	public void setAccountArrearage(double accountArrearage) {
		this.accountArrearage = accountArrearage;
	}

	public double getOrderBalance() {
		return orderBalance;
	}

	public void setOrderBalance(double orderBalance) {
		this.orderBalance = orderBalance;
	}

	public double getOrderArrearage() {
		return orderArrearage;
	}

	public void setOrderArrearage(double orderArrearage) {
		this.orderArrearage = orderArrearage;
	}

	public int getIsReal() {
		return isReal;
	}

	public void setIsReal(int isReal) {
		this.isReal = isReal;
	}

	@JsonIgnore
	@ExcelField(title="订单区分", align=2, sort=4)
	public String getOrderIsReal() {
		return orderIsReal;
	}

	public void setOrderIsReal(String orderIsReal) {
		if(orderIsReal.equals("0")){
			this.orderIsReal="实物商品";
		}else if(orderIsReal.equals("1")){
			this.orderIsReal="虚拟商品";
		}
	}

	public double getActualPayment() {
		return actualPayment;
	}

	public void setActualPayment(double actualPayment) {
		this.actualPayment = actualPayment;
	}

	public double getSingleNormPrice() {
		return singleNormPrice;
	}

	public void setSingleNormPrice(double singleNormPrice) {
		this.singleNormPrice = singleNormPrice;
	}

	public double getSingleRealityPrice() {
		return singleRealityPrice;
	}

	public void setSingleRealityPrice(double singleRealityPrice) {
		this.singleRealityPrice = singleRealityPrice;
	}

	public List<Integer> getGoodselectIds() {
		return goodselectIds;
	}

	public void setGoodselectIds(List<Integer> goodselectIds) {
		this.goodselectIds = goodselectIds;
	}

	public List<Double> getOrderAmounts() {
		return orderAmounts;
	}

	public void setOrderAmounts(List<Double> orderAmounts) {
		this.orderAmounts = orderAmounts;
	}

	public List<String> getSpeckeys() {
		return speckeys;
	}

	public void setSpeckeys(List<String> speckeys) {
		this.speckeys = speckeys;
	}

	public List<Double> getActualPayments() {
		return actualPayments;
	}

	public void setActualPayments(List<Double> actualPayments) {
		this.actualPayments = actualPayments;
	}

	public int getDistinction() {
		return distinction;
	}

	public void setDistinction(int distinction) {
		this.distinction = distinction;
	}

	public int getIsNeworder() {
		return isNeworder;
	}

	public void setIsNeworder(int isNeworder) {
		this.isNeworder = isNeworder;
	}

	public double getAfterPayment() {
		return afterPayment;
	}

	public void setAfterPayment(double afterPayment) {
		this.afterPayment = afterPayment;
	}

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public double getDebtMoney() {
		return debtMoney;
	}

	public void setDebtMoney(double debtMoney) {
		this.debtMoney = debtMoney;
	}

	public double getSpareMoney() {
		return spareMoney;
	}

	public void setSpareMoney(double spareMoney) {
		this.spareMoney = spareMoney;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public String getResidue() {
		return residue;
	}

	public void setResidue(String residue) {
		this.residue = residue;
	}

	public String getGoodsTotalPrice() {
		return goodsTotalPrice;
	}

	public void setGoodsTotalPrice(String goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}

	public int getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(int isDiscount) {
		this.isDiscount = isDiscount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGoodselectId() {
		return goodselectId;
	}

	public void setGoodselectId(String goodselectId) {
		this.goodselectId = goodselectId;
	}

	public int getOldstatus() {
		return oldstatus;
	}

	public void setOldstatus(int oldstatus) {
		this.oldstatus = oldstatus;
	}

	public int getPayid() {
		return payid;
	}

	public void setPayid(int payid) {
		this.payid = payid;
	}

	public AcountLog getAcountLog() {
		return acountLog;
	}

	public void setAcountLog(AcountLog acountLog) {
		this.acountLog = acountLog;
	}

	
	public OrderGoods getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(OrderGoods orderGoods) {
		this.orderGoods = orderGoods;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@JsonIgnore
	@ExcelField(title="订单号", align=2, sort=3)
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(int orderstatus) {
		this.orderstatus = orderstatus;
	}

	@JsonIgnore
	@ExcelField(title="订单状态", align=2, sort=1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if(status.equals("-2")){
			this.status="取消订单";
		}else if(status.equals("-1")){
			this.status="待付款";
		}else if(status.equals("1")){
			this.status="待发货";
		}else if(status.equals("2")){
			this.status="待收货";
		}else if(status.equals("3")){
			this.status="已退款";
		}else if(status.equals("4")){
			this.status="已完成";
		}
	}

	@JsonIgnore
	@ExcelField(title="发货方式", align=2, sort=2)
	public String getShipType() {
		return shipType;
	}
	
	public void setShipType(String shipType) {
		if(shipType.equals("0")){
			this.shipType="快递发货";
		}else if(shipType.equals("1")){
			this.shipType="到店自取";
		}else if(shipType.equals("2")){
			this.shipType="无需发货";
		}
		
	}

	public int getShippingstatus() {
		return shippingstatus;
	}

	public void setShippingstatus(int shippingstatus) {
		this.shippingstatus = shippingstatus;
	}

	
//	public int getPaystatus() {
//		return paystatus;
//	}
//
//	public void setPaystatus(int paystatus) {
//		this.paystatus = paystatus;
//	}

	public int getCreatelogo() {
		return createlogo;
	}

	public void setCreatelogo(int createlogo) {
		this.createlogo = createlogo;
	}

	@JsonIgnore
	@ExcelField(title="收货人", align=2, sort=25)
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	@JsonIgnore
	@ExcelField(title="商品名称", align=2, sort=50)
	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}
//
//	public String getProvince() {
//		return province;
//	}
//
//	public void setProvince(String province) {
//		this.province = province;
//	}
//
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	//
//	public String getDistrict() {
//		return district;
//	}
//
//	public void setDistrict(String district) {
//		this.district = district;
//	}
//
//	public String getTwonl() {
//		return twonl;
//	}
//
//	public void setTwonl(String twonl) {
//		this.twonl = twonl;
//	}
//
	@JsonIgnore
	@ExcelField(title="收货地址", align=2, sort=20)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public String getZipcode() {
//		return zipcode;
//	}
//
//	public void setZipcode(String zipcode) {
//		this.zipcode = zipcode;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
	
	
	@JsonIgnore
	@ExcelField(title="物流编码",align=2, sort=39)
	public String getShippingcode() {
		return shippingcode;
	}

	public void setShippingcode(String shippingcode) {
		this.shippingcode = shippingcode;
	}
	
	
	public int getShippingtype() {
		return shippingtype;
	}

	public void setShippingtype(int shippingtype) {
		this.shippingtype = shippingtype;
	}


	@JsonIgnore
	@ExcelField(title="发货时间",align=2,sort=38)
	public Date getShippingtime() {
		return shippingtime;
	}

	public void setShippingtime(Date shippingtime) {
		this.shippingtime = shippingtime;
	}

	@JsonIgnore
	@ExcelField(title="物流公司",align=2,sort=37)
	public String getShippingname() {
		return shippingname;
	}

	public void setShippingname(String shippingname) {
		this.shippingname = shippingname;
	}
	
	@JsonIgnore
	@ExcelField(title="支付编码", align=2, sort=34)
	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	@JsonIgnore
	@ExcelField(title="支付方式", align=2, sort=35)
	public String getPayname() {
		return payname;
	}

	public void setPayname(String payname) {
		this.payname = payname;
	}

	public String getInvoicetitle() {
		return invoicetitle;
	}

	public void setInvoicetitle(String invoicetitle) {
		this.invoicetitle = invoicetitle;
	}

	@JsonIgnore
	@ExcelField(title="单价", align=2, sort=65)
	public String getStrGoodsPrice() {
		return strGoodsPrice;
	}

	public void setStrGoodsPrice(String strGoodsPrice) {
		this.strGoodsPrice = strGoodsPrice;
	}

	
	public double getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(double goodsprice) {
		this.goodsprice = goodsprice;
	}

	
	public double getShippingprice() {
		return shippingprice;
	}

	public void setShippingprice(double shippingprice) {
		this.shippingprice = shippingprice;
	}

	public double getUsermoney() {
		return usermoney;
	}

	public void setUsermoney(double usermoney) {
		this.usermoney = usermoney;
	}

	public double getCouponprice() {
		return couponprice;
	}

	public void setCouponprice(double couponprice) {
		this.couponprice = couponprice;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public double getIntegralmoney() {
		return integralmoney;
	}

	public void setIntegralmoney(double integralmoney) {
		this.integralmoney = integralmoney;
	}


	@JsonIgnore
	@ExcelField(title="应付款金额", align=2, sort=69)
	public String getStrOrderAmount() {
		return strOrderAmount;
	}

	public void setStrOrderAmount(String strOrderAmount) {
		this.strOrderAmount = strOrderAmount;
	}

	public double getOrderamount() {
		return orderamount;
	}
	public void setOrderamount(double orderamount) {
		this.orderamount = orderamount;
	}

	public double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(Date confirmtime) {
		this.confirmtime = confirmtime;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public int getActionid() {
		return actionid;
	}

	public void setActionid(int actionid) {
		this.actionid = actionid;
	}

	public double getOrderactionamount() {
		return orderactionamount;
	}

	public void setOrderactionamount(double orderactionamount) {
		this.orderactionamount = orderactionamount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getUsernote() {
		return usernote;
	}

	public void setUsernote(String usernote) {
		this.usernote = usernote;
	}

	public String getAdminnote() {
		return adminnote;
	}

	public void setAdminnote(String adminnote) {
		this.adminnote = adminnote;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	
	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	
	
	public String getSpeckey() {
		return speckey;
	}

	public void setSpeckey(String speckey) {
		this.speckey = speckey;
	}

	@JsonIgnore
	@ExcelField(title="规格", align=2, sort=55)
	public String getSpeckeyname() {
		return speckeyname;
	}

	public void setSpeckeyname(String speckeyname) {
		this.speckeyname = speckeyname;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	@JsonIgnore
	@ExcelField(title="手机号", align=2, sort=30)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	

//	public int getAddressid() {
//		return addressid;
//	}
//
//	public void setAddressid(int addressid) {
//		this.addressid = addressid;
//	}

	public String getMobile1() {
		return mobile1;
	}

	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	@JsonIgnore
	@ExcelField(title="昵称", align=2, sort=10)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	@ExcelField(title="数量", align=2, sort=60)
	public String getStrGoodsNum() {
		return strGoodsNum;
	}

	public void setStrGoodsNum(String strGoodsNum) {
		this.strGoodsNum = strGoodsNum;
	}

	
	public int getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}

	
	public int getMarketprice() {
		return marketprice;
	}

	public void setMarketprice(int marketprice) {
		this.marketprice = marketprice;
	}

	public String getCetaid() {
		return cetaid;
	}

	public void setCetaid(String cetaid) {
		this.cetaid = cetaid;
	}

	@JsonIgnore
	@ExcelField(title="系列名称", align=2, sort=55)
	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	public String getGoodsids() {
		return goodsids;
	}

	public void setGoodsids(String goodsids) {
		this.goodsids = goodsids;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	
	@JsonIgnore
	@ExcelField(title="货号", align=2, sort=45)
	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public int getRemaintimes() {
		return remaintimes;
	}

	public void setRemaintimes(int remaintimes) {
		this.remaintimes = remaintimes;
	}
	
	@JsonIgnore
	@ExcelField(title="支付订单号", align=2, sort=4)
	public String getTempOrderId() {
		return tempOrderId;
	}

	public void setTempOrderId(String tempOrderId) {
		this.tempOrderId = tempOrderId;
	}

	@Override
	public Orders getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Orders parent) {
		// TODO Auto-generated method stub
		
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	
	@JsonIgnore
	@ExcelField(title="买家留言", type=1, align=2, sort=240)
	public String getUserNote() {
		return userNote;
	}

	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}
	@JsonIgnore
	@ExcelField(title="备注", type=1, align=2, sort=245)
	public String getAdminNote() {
		return adminNote;
	}

	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
	}
	@JsonIgnore
	@ExcelField(title="创建时间", type=1, align=2, sort=7)
	public String getStrAddTime() {
		return strAddTime;
	}
	public void setStrAddTime(String strAddTime) {
		this.strAddTime = strAddTime;
	}
	@JsonIgnore
	@ExcelField(title="订单实付金额", type=1, align=2, sort=71)
	public String getOrderTotalAmount() {
		return orderTotalAmount;
	}
	public void setOrderTotalAmount(String orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}
	@JsonIgnore
	@ExcelField(title="邮费", align=2, sort=72)
	public String getNewShippingPrice() {
		return newShippingPrice;
	}
	public void setNewShippingPrice(String newShippingPrice) {
		this.newShippingPrice = newShippingPrice;
	}
	@JsonIgnore
	@ExcelField(title="省", align=2, sort=17)
	public String getNewProvince() {
		return newProvince;
	}
	public void setNewProvince(String newProvince) {
		this.newProvince = newProvince;
	}
	
	@JsonIgnore
	@ExcelField(title="市", align=2, sort=18)
	public String getNewCity() {
		return newCity;
	}
	public void setNewCity(String newCity) {
		this.newCity = newCity;
	}
	
	@JsonIgnore
	@ExcelField(title="县", align=2, sort=19)
	public String getNewDistrict() {
		return newDistrict;
	}
	public void setNewDistrict(String newDistrict) {
		this.newDistrict = newDistrict;
	}
	public String getOldAddress() {
		return oldAddress;
	}
	public void setOldAddress(String oldAddress) {
		this.oldAddress = oldAddress;
	}
	public String getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	
	public String getUserDelFlag() {
		return userDelFlag;
	}
	public void setUserDelFlag(String userDelFlag) {
		this.userDelFlag = userDelFlag;
	}
	@JsonIgnore
	@ExcelField(title="取消类型", align=2, sort=6)
	public String getCancelType() {
		return cancelType;
	}
	public void setCancelType(String cancelType) {
		if(cancelType.equals("0")){
			this.cancelType="用户取消";
		}else if(cancelType.equals("1")){
			this.cancelType="后台取消";
		}else if(cancelType.equals("2")){
			this.cancelType="自动取消";
		}
	}
	public int getIsInvoice() {
		return isInvoice;
	}
	public void setIsInvoice(int isInvoice) {
		this.isInvoice = isInvoice;
	}
	
	@JsonIgnore
	@ExcelField(title="用户id", align=2, sort=8)
	public String getUsersId() {
		return usersId;
	}
	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}
	
	@JsonIgnore
	@ExcelField(title="注册手机号", align=2, sort=11)
	public String getUsersMobile() {
		return usersMobile;
	}
	public void setUsersMobile(String usersMobile) {
		this.usersMobile = usersMobile;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<Integer> getRemaintimeNums() {
		return remaintimeNums;
	}
	public void setRemaintimeNums(List<Integer> remaintimeNums) {
		this.remaintimeNums = remaintimeNums;
	}
	
	public String getIsComment() {
		return isComment;
	}
	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}
	public String getNewIsNeworder() {
		return newIsNeworder;
	}
	public void setNewIsNeworder(String newIsNeworder) {
		this.newIsNeworder = newIsNeworder;
	}
	@JsonIgnore
	@ExcelField(title="支付时间", align=2, sort=36)
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public Date getPayBegTime() {
		return payBegTime;
	}
	public void setPayBegTime(Date payBegTime) {
		this.payBegTime = payBegTime;
	}
	public Date getPayEndTime() {
		return payEndTime;
	}
	public void setPayEndTime(Date payEndTime) {
		this.payEndTime = payEndTime;
	}
	public List<Date> getRealityAddTimeList() {
		return realityAddTimeList;
	}
	public void setRealityAddTimeList(List<Date> realityAddTimeList) {
		this.realityAddTimeList = realityAddTimeList;
	}
	public int getUserIntegral() {
		return userIntegral;
	}
	public void setUserIntegral(int userIntegral) {
		this.userIntegral = userIntegral;
	}
	public List<String> getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(List<String> sysUserId) {
		this.sysUserId = sysUserId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBelongOfficeId() {
		return belongOfficeId;
	}
	public void setBelongOfficeId(String belongOfficeId) {
		this.belongOfficeId = belongOfficeId;
	}
	public String getBelongUserId() {
		return belongUserId;
	}
	public void setBelongUserId(String belongUserId) {
		this.belongUserId = belongUserId;
	}
	public String getBelongOfficeName() {
		return belongOfficeName;
	}
	public void setBelongOfficeName(String belongOfficeName) {
		this.belongOfficeName = belongOfficeName;
	}
	public String getBelongUserName() {
		return belongUserName;
	}
	public void setBelongUserName(String belongUserName) {
		this.belongUserName = belongUserName;
	}
	
}
