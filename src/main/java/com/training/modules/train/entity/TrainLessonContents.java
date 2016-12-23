package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 课程内容
 * @author kele
 * @version 2016年3月16日
 */
public class TrainLessonContents extends DataEntity<TrainLessonContents>{

	private static final long serialVersionUID = 1L;
	private String contentId;				//内容ID
	private TrainLessons trainLessons;		//课程ID
	private String content;					//内容地址
	private int status;						//创建时间
	private String coverPic;				//状态(-1 无效; 0有效)
	private String name;					//内容名称
	private String contentlength;			//内容长度
	private int contentType;				//内容类型(1  视频;2  文档; 3 其他)
	private String downUrl;					//下载路径
	private String addPic;					//添加图片按键
	
	
	
	public TrainLessonContents() {
		super();
	}

	public TrainLessonContents(String contentId) {
		super();
		this.contentId = contentId;
	}

	public TrainLessonContents(String contentId, TrainLessons trainLessons, String content, int status, String coverPic,
			String name, String contentlength, int contentType, String downUrl) {
		super();
		this.contentId = contentId;
		this.trainLessons = trainLessons;
		this.content = content;
		this.status = status;
		this.coverPic = coverPic;
		this.name = name;
		this.contentlength = contentlength;
		this.contentType = contentType;
		this.downUrl = downUrl;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public TrainLessons getTrainLessons() {
		return trainLessons;
	}

	public void setTrainLessons(TrainLessons trainLessons) {
		this.trainLessons = trainLessons;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCoverPic() {
		return coverPic;
	}

	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentlength() {
		return contentlength;
	}

	public void setContentlength(String contentlength) {
		this.contentlength = contentlength;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getAddPic() {
		return addPic;
	}

	public void setAddPic(String addPic) {
		this.addPic = addPic;
	}
	
	
	
}
