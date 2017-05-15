package com.training.modules.forms.entity;

import java.util.Date;

import com.training.common.persistence.TreeEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 妃子校用户汇总表
 * @author stone
 * @date 2017年4月24日
 */

public class UserCollectReport extends TreeEntity<UserCollectReport>{

	private static final long serialVersionUID = 1L;
	
	private int allUserCount;              //总用户数
	private int addUserCount;              //新增用户数
	private int leaveUserCount;            //离职用户数
	private Date begtime;				   //查询开始时间
	private Date endtime;				   //查询结束时间
	private String times;				   //时间

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
	
	@ExcelField(title = "时间" ,sort = 100 )
	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}
	
	@ExcelField(title = "总用户数" , align = 2 , type = 1 , sort = 200)
	public int getAllUserCount() {
		return allUserCount;
	}

	public void setAllUserCount(int allUserCount) {
		this.allUserCount = allUserCount;
	}

	@ExcelField(title = "新增用户数" , align = 2 , type = 1 , sort = 300)
	public int getAddUserCount() {
		return addUserCount;
	}

	public void setAddUserCount(int addUserCount) {
		this.addUserCount = addUserCount;
	}

	@ExcelField(title = "离职用户数" , align = 2 , type = 1 , sort = 400)
	public int getLeaveUserCount() {
		return leaveUserCount;
	}

	public void setLeaveUserCount(int leaveUserCount) {
		this.leaveUserCount = leaveUserCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public UserCollectReport getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(UserCollectReport parent) {
		// TODO Auto-generated method stub
		
	}

}
