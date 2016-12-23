package com.training.modules.ec.entity;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.utils.excel.annotation.ExcelField;

public class Shipping {
	
	private String orderid;			//订单id
	private String shippingcode;	//物流code
	private String shippingtime;		//发货时间
	private String shippingname;	//物流名称
	
	
	@JsonIgnore
	@NotNull(message="订单号不能为空")
	@ExcelField(title="订单号", align=2, sort=10)
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	@JsonIgnore
	@NotNull(message="物流编码不能为空")
	@ExcelField(title="物流编码",align=2, sort=20)
	public String getShippingcode() {
		return shippingcode;
	}
	public void setShippingcode(String shippingcode) {
		this.shippingcode = shippingcode;
	}
	
	@JsonIgnore
	@ExcelField(title="发货时间",align=2, sort=30)
	public String getShippingtime() {
		return shippingtime;
	}

	public void setShippingtime(String shippingtime) {
		this.shippingtime = shippingtime;
	}

	@JsonIgnore
	@NotNull(message="物流公司不能为空")
	@ExcelField(title="物流公司",align=2, sort=40)
	public String getShippingname() {
		return shippingname;
	}

	public void setShippingname(String shippingname) {
		this.shippingname = shippingname;
	}

}
