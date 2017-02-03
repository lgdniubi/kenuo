package com.training.modules.train.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.common.persistence.DataEntity;

public class LessonAsks extends DataEntity<LessonAsks>{
	
	private static final long serialVersionUID = 1L;
	private String askId;  //问题的ID
	private String title;	//问题标题
	private Date createtime;	//创建时间
	private int ststus;		//问题状态
	private String userId;  //用户ID
	private String content;  //问题内容  
	private int askType;     //问题类型       2  文本+图片     3  文本+视频    
	
	private int num; 			//回答问题的总数
	private String name;		//提问者名字
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	public String getAskId() {
		return askId;
	}
	public void setAskId(String askId) {
		this.askId = askId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public int getStstus() {
		return ststus;
	}
	public void setStstus(int ststus) {
		this.ststus = ststus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getAskType() {
		return askType;
	}
	public void setAskType(int askType) {
		this.askType = askType;
	}
	
}
