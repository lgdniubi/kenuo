package com.training.modules.quartz.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 定时任务日志表
 * @author kele
 * @version 2016年9月29日
 */
public class TaskLog extends DataEntity<TaskLog>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jobName;			//定时器名称
	private String jobDescription;	//定时器描述
	private Date startDate;			//开始时间
	private Date endDate;			//结束时间
	private Long runTime;			//运行时间
	private Date createDate;		//创建时间
	private int status;				//定时任务状态【0-正常；1-异常；】
	private String exceptionMsg;	//异常信息
	private String remarks;			//备注
	
	
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public Long getRunTime() {
		return runTime;
	}
	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}
	
}
