package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 *	发票导出实体
 *
 */
public class OrderInvoiceExport extends TreeEntity<OrderInvoiceExport>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String receiptsNo;			//单据号
	private String shopLine;			//商品行数
	private String buyName; 			//购方名称
	private String buyTaxNo;			//购方税号
	private String buyPhone;			//购方地址电话
	private String buyBankNo;			//购方银行帐号
	private String remark;				//备注
	private String auditUser;			//复核人
	private String proceedsUser;		//收款人
	private String lineShopName;		//清单行商品名称
	private String receiptsDate;  		//单据日期
	private String sellerBankNo;		//销方银行帐号
	private String sellerPhone;			//销方地址电话
	private int    eId;					//序号
	private String shopName;			//货物名称
	private String unit;				//计量单位
	private String specKeyName;			//规格
	private int    goodsNum;			//数量
	private String totalAmount;			//金额
	private String TaxRate;				//税率
	private String shopTaxNum;			//商品税目
	private String discountPrice;		//折扣金额
	private String discountTax;			//税额
	private String discountRate;		//折扣率
	private String goodsPrice;			//单价
	private String PriceWay;			//价格方式
	private String goodsNoVersions;		//商品编码库版本号
	private String goodsNo;				//商品编码
	private String firmGoodsNo;			//企业商品编码
	private String foreigntradeFlag;	//使用优惠政策标识
	private String zeroTaxRateFlag;		//零税率标识
	private String foreigntradeRemark;	//优惠政策说明
	
	@ExcelField(title="单据号", align=2, sort=10,type=1)
	public String getReceiptsNo() {
		return receiptsNo;
	}
	public void setReceiptsNo(String receiptsNo) {
		this.receiptsNo = receiptsNo;
	}
	@ExcelField(title="商品行数", align=2, sort=20,type=1)
	public String getShopLine() {
		return shopLine;
	}
	public void setShopLine(String shopLine) {
		this.shopLine = shopLine;
	}
	@ExcelField(title="购方名称", align=2, sort=30,type=1)
	public String getBuyName() {
		return buyName;
	}
	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}
	@ExcelField(title="购方税号", align=2, sort=40,type=1)
	public String getBuyTaxNo() {
		return buyTaxNo;
	}
	public void setBuyTaxNo(String buyTaxNo) {
		this.buyTaxNo = buyTaxNo;
	}
	@ExcelField(title="购方地址电话", align=2, sort=50,type=1)
	public String getBuyPhone() {
		return buyPhone;
	}
	public void setBuyPhone(String buyPhone) {
		this.buyPhone = buyPhone;
	}
	@ExcelField(title="购方银行帐号", align=2, sort=60,type=1)
	public String getBuyBankNo() {
		return buyBankNo;
	}
	public void setBuyBankNo(String buyBankNo) {
		this.buyBankNo = buyBankNo;
	}
	@ExcelField(title="备注", align=2, sort=70,type=1)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ExcelField(title="复核人", align=2, sort=80,type=1)
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	@ExcelField(title="收款人", align=2, sort=90,type=1)
	public String getProceedsUser() {
		return proceedsUser;
	}
	public void setProceedsUser(String proceedsUser) {
		this.proceedsUser = proceedsUser;
	}
	@ExcelField(title="清单行商品名称", align=2, sort=100,type=1)
	public String getLineShopName() {
		return lineShopName;
	}
	public void setLineShopName(String lineShopName) {
		this.lineShopName = lineShopName;
	}
	@ExcelField(title="单据日期", align=2, sort=110,type=1)
	public String getReceiptsDate() {
		return receiptsDate;
	}
	public void setReceiptsDate(String receiptsDate) {
		this.receiptsDate = receiptsDate;
	}
	@ExcelField(title="销方银行帐号", align=2, sort=120,type=1)
	public String getSellerBankNo() {
		return sellerBankNo;
	}
	public void setSellerBankNo(String sellerBankNo) {
		this.sellerBankNo = sellerBankNo;
	}
	@ExcelField(title="销方地址电话", align=2, sort=130,type=1)
	public String getSellerPhone() {
		return sellerPhone;
	}
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}
	@ExcelField(title="序号", align=2, sort=140,type=1)
	public int getEId() {
		return eId;
	}
	public void seteId(int eId) {
		this.eId = eId;
	}
	@ExcelField(title="货物名称", align=2, sort=150,type=1)
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	@ExcelField(title="计量单位", align=2, sort=60,type=1)
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@ExcelField(title="规格", align=2, sort=170,type=1)
	public String getSpecKeyName() {
		return specKeyName;
	}
	public void setSpecKeyName(String specKeyName) {
		this.specKeyName = specKeyName;
	}
	@ExcelField(title="数量", align=2, sort=180,type=1)
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	@ExcelField(title="金额", align=2, sort=190,type=1)
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	@ExcelField(title="税率", align=2, sort=200,type=1)
	public String getTaxRate() {
		return TaxRate;
	}
	public void setTaxRate(String taxRate) {
		TaxRate = taxRate;
	}
	@ExcelField(title="商品税目", align=2, sort=210,type=1)
	public String getShopTaxNum() {
		return shopTaxNum;
	}
	public void setShopTaxNum(String shopTaxNum) {
		this.shopTaxNum = shopTaxNum;
	}
	@ExcelField(title="折扣金额", align=2, sort=220,type=1)
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
	@ExcelField(title="税额", align=2, sort=230,type=1)
	public String getDiscountTax() {
		return discountTax;
	}
	public void setDiscountTax(String discountTax) {
		this.discountTax = discountTax;
	}
	@ExcelField(title="折扣率", align=2, sort=240,type=1)
	public String getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}
	@ExcelField(title="单价", align=2, sort=250,type=1)
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	@ExcelField(title="价格方式", align=2, sort=260,type=1)
	public String getPriceWay() {
		return PriceWay;
	}
	public void setPriceWay(String priceWay) {
		PriceWay = priceWay;
	}
	@ExcelField(title="商品编码库版本号", align=2, sort=270,type=1)
	public String getGoodsNoVersions() {
		return goodsNoVersions;
	}
	public void setGoodsNoVersions(String goodsNoVersions) {
		this.goodsNoVersions = goodsNoVersions;
	}
	@ExcelField(title="商品编码", align=2, sort=280,type=1)
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	@ExcelField(title="企业商品编码", align=2, sort=290,type=1)
	public String getFirmGoodsNo() {
		return firmGoodsNo;
	}
	public void setFirmGoodsNo(String firmGoodsNo) {
		this.firmGoodsNo = firmGoodsNo;
	}
	@ExcelField(title="使用优惠政策标识", align=2, sort=300,type=1)
	public String getForeigntradeFlag() {
		return foreigntradeFlag;
	}
	public void setForeigntradeFlag(String foreigntradeFlag) {
		this.foreigntradeFlag = foreigntradeFlag;
	}
	@ExcelField(title="零税率标识", align=2, sort=301,type=1)
	public String getZeroTaxRateFlag() {
		return zeroTaxRateFlag;
	}
	public void setZeroTaxRateFlag(String zeroTaxRateFlag) {
		this.zeroTaxRateFlag = zeroTaxRateFlag;
	}
	@ExcelField(title="优惠政策说明", align=2, sort=320,type=1)
	public String getForeigntradeRemark() {
		return foreigntradeRemark;
	}
	public void setForeigntradeRemark(String foreigntradeRemark) {
		this.foreigntradeRemark = foreigntradeRemark;
	}
	@Override
	public OrderInvoiceExport getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OrderInvoiceExport parent) {
		// TODO Auto-generated method stub
		
	}
}
