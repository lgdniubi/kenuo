/**
 * 
 */
package com.training.modules.train.entity;

import java.math.BigDecimal;

import com.sun.org.apache.bcel.internal.generic.RETURN;

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
	private double expense;			//花销
	private double income;				//收入
	private String billmonth;			//账单月份
	
	public void setExpense(double expense) {
		this.expense = expense;
	}
	public void setIncome(double income) {
		this.income = income;
	}
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
	
	public void setAdds(int adds) {
		BigDecimal b1 = new BigDecimal(this.expense);
        BigDecimal b2 = new BigDecimal((this.income * -1));
        this.used_limit = b1.add(b2).doubleValue();
	}
	
}
