package com.training.modules.ec.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;
import com.training.modules.sys.entity.Office;
/**
 * 订单实体
 * @author zhangyang
 *
 */
public class Orders extends TreeEntity<Orders> {
	
	private static final long serialVersionUID = 1L;
	
	private String orderid;			//订单id
//	private int addressid;			//地址id
	private int userid;			//用户id
	private int orderstatus;		//订单状态
	private String status;			//到处订单状态
	private int shippingstatus;	//发货状态
//	private int paystatus;			//支付状态
	private String consignee;		//收货人
//	private String country;			//国家
//	private String province;		//省份
	private	 String city;			//城市
	private String cityname;		//城市名称
//	private	 String district;		//县区
//	private String twonl;			//城镇
	private String address;			//地址
//	private String zipcode;			//邮政编码
	private String phone;			//收货者固定电话
//	private String email;			//邮箱
	private int shippingtype;     //0 快递发货 1到店自取 2 无需发货
	private String shippingcode;	//物流code
	private Date shippingtime;		//发货时间
	private String shippingname;	//物流名称
	private int payid;				//支付方式id
	private String paycode;			//支付code
	private String payname;			//支付方式名称
	private String invoicetitle;	//发票抬头
	private double goodsprice;		//商品总价
	private double shippingprice;	//邮费
	private double usermoney;		//使用的余额
	private double couponprice;	//优惠价格
	private int integral;			//积分
	private double integralmoney;	//积分抵多少钱
	private double orderamount;	//应付金额
	private double totalamount;	//订单的总价
	private Date addtime;			//下单时间
	private Date confirmtime;		//收货时间
	private Date  paytime;			//支付时间
	private int actionid;				//活动id
	private	 double orderactionamount;	//活动优惠金额
	private double discount;			//价格调整
	private String usernote;			//用户备注
	private String adminnote;			//管理员备注
	private String parentid;			//父单号id
	private String	username;			//购买者姓名
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
	private int createlogo;			//订单标识 1 客户端创建 2 后台创建
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
			this.status="未付款";
		}else if(status.equals("1")){
			this.status="已支付";
		}else if(status.equals("2")){
			this.status="已发货";
		}else if(status.equals("3")){
			this.status="已退货";
		}else if(status.equals("4")){
			this.status="已完成";
		}else if(status.equals("5")){
			this.status="申请退款";
		}

	}

	@JsonIgnore
	@ExcelField(title="订单类型", align=2, sort=2)
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
	@ExcelField(title="物流编码",align=2, sort=38)
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
	@ExcelField(title="发货时间",align=2,sort=37)
	public Date getShippingtime() {
		return shippingtime;
	}

	public void setShippingtime(Date shippingtime) {
		this.shippingtime = shippingtime;
	}

	@JsonIgnore
	@ExcelField(title="物流公司",align=2,sort=36)
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
	@ExcelField(title="支付金额", align=2, sort=70)
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
	
	
	

}
