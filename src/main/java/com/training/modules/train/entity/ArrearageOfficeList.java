/**
 * 
 */
package com.training.modules.train.entity;

/**  
* <p>Title: ArrearageOfficeList.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/

public class ArrearageOfficeList {
	
	private String order_id;			//订单id
	private String office_id;			//机构id
	private double used_limit;			//已使用额度
	private String billmonth;			//账单月份
	
	public String getBillmonth() {
		return billmonth;
	}
	public void setBillmonth(String billmonth) {
		this.billmonth = billmonth;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOffice_id() {
		return office_id;
	}
	public void setOffice_id(String office_id) {
		this.office_id = office_id;
	}
	public double getUsed_limit() {
		return used_limit;
	}
	public void setUsed_limit(double used_limit) {
		this.used_limit = used_limit;
	}
	
	
	
}
