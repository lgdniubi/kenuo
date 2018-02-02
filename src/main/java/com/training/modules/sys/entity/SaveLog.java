/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 保存日志Entity
 * 
 * @version 2018年1月22
 */
public class SaveLog extends DataEntity<SaveLog> {

	private static final long serialVersionUID = 1L;
	private int lId; 			// 日志id
	private String title; 		// 日志标题
	private String content;		// 修改内容
	private Date startDate;		// 开始日期
	private Date endDate;		// 结束日期
	private long runTime;		// 执行时间（秒）	
	private String exceptionMsg; 	// 报错日志
	
	public int getlId() {
		return lId;
	}
	public void setlId(int lId) {
		this.lId = lId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	
}