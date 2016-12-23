package com.training.modules.quartz.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

/**
 * 计划任务定时器
 * @author kele
 * @version 2016年7月28日15:21:55
 */
public class Task extends DataEntity<Task> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String STATUS_RUNNING = "0"; 		// 任务状态 0-开启
	public static final String STATUS_NOT_RUNNING = "1"; 	// 任务状态 1-关闭
	public static final String CONCURRENT_IS = "0"; 		// 是否并发 0-是并发
	public static final String CONCURRENT_NOT = "1"; 		// 是否并发 1-否-等待执行完成后在执行

	private String jobId;			// 任务id
	private String jobName;			// 任务名称
	private String jobGroup;		// 任务分组
	private String jobStatus = "1";		// 任务状态 是否启动任务
	private String cronExpression;	// cron表达式
	private String description;		// 描述
	private String beanClass;		// 任务执行时调用哪个类的方法 包名+类名
	private String isConcurrent = "1";	// 任务是否并发
	private String springId;		// spring bean
	private String methodName;		// 任务调用的方法名
	private Date createTime; 		// 创建时间
	private Date updateTime; 		// 修改时间

	
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}

	public String getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String getSpringId() {
		return springId;
	}

	public void setSpringId(String springId) {
		this.springId = springId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
}
