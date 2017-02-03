package com.training.modules.train.entity;

import java.util.Date;

import com.training.common.persistence.DataEntity;

public class LessonAskContent extends DataEntity<LessonAskContent>{
	private static final long serialVersionUID = 1L;

	private String title;	//问题标题
	private String textcontent; //问题文本内容   
	private int askType;	//问题类型
	private String content;		//问题图片或视频内容  1 文本	2  文本+多张图片     3  文本+视频  
	private String askId;		//问题ID	

	private Date createtime;	//评论时间
	//获取用户表的数据
	private String name;	// 姓名
	private String photo;	// 头像
	
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
	public int getAskType() {
		return askType;
	}
	public void setAskType(int askType) {
		this.askType = askType;
	}
	public String getAskId() {
		return askId;
	}
	public void setAskId(String askId) {
		this.askId = askId;
	}
	public String getTextcontent() {
		return textcontent;
	}
	public void setTextcontent(String textcontent) {
		this.textcontent = textcontent;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
