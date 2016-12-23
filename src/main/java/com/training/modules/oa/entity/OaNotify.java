/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.oa.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.Collections3;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.entity.User;

/**
 * 通知通告Entity
 * 
 * @version 2014-05-16
 */
public class OaNotify extends DataEntity<OaNotify> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String title;		// 标题
	private String content;		// 类型
	private String files;		// 附件
	private String status;		// 状态
	private Integer pushType;
	private String result;
	private Integer msgTotal;
	private Integer msgProcess;
	private Integer clickNum;
	private Date startTime;
	private Date endTime;
	private String contentId;
	private Integer pushNum;
	
	private String start;
	private String end;
	private String readNum;		// 已读
	private String unReadNum;	// 未读
	
	private boolean isSelf;		// 是否只查询自己的通知
	
	private String readFlag;	// 本人阅读状态
	
	//添加通告关联课程
	private String categoryId;		//类别ID
	private String name;			//类别名称
	private String lessonId;		//课程ID
	private String lessonName;		//课程名称
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	private List<OaNotifyRecord> oaNotifyRecordList = Lists.newArrayList();
	
	public OaNotify() {
		super();
	}

	public OaNotify(String id){
		super(id);
	}

	@Length(min=0, max=200, message="标题长度必须介于 0 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=2000, message="附件长度必须介于 0 和 2000 之间")
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(String unReadNum) {
		this.unReadNum = unReadNum;
	}
	
	public List<OaNotifyRecord> getOaNotifyRecordList() {
		return oaNotifyRecordList;
	}

	public void setOaNotifyRecordList(List<OaNotifyRecord> oaNotifyRecordList) {
		this.oaNotifyRecordList = oaNotifyRecordList;
	}
	
	/**
	 * 获取通知发送记录用户ID
	 * @return
	 */
	public String getOaNotifyRecordIds() {
		return Collections3.extractToString(oaNotifyRecordList, "user.id", ",") ;
	}
	
	/**
	 * 设置通知发送记录用户ID
	 * @return
	 */
	public void setOaNotifyRecordIds(String oaNotifyRecord) {
		this.oaNotifyRecordList = Lists.newArrayList();
		for (String id : StringUtils.split(oaNotifyRecord, ",")){
			OaNotifyRecord entity = new OaNotifyRecord();
			entity.setId(IdGen.uuid());
			entity.setOaNotify(this);
			entity.setUser(new User(id));
			entity.setReadFlag("0");
			this.oaNotifyRecordList.add(entity);
		}
	}

	/**
	 * 获取通知发送记录用户Name
	 * @return
	 */
	public String getOaNotifyRecordNames() {
		return Collections3.extractToString(oaNotifyRecordList, "user.name", ",") ;
	}
	
	/**
	 * 设置通知发送记录用户Name
	 * @return
	 */
	public void setOaNotifyRecordNames(String oaNotifyRecord) {
		// 什么也不做
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getMsgTotal() {
		return msgTotal;
	}

	public void setMsgTotal(Integer msgTotal) {
		this.msgTotal = msgTotal;
	}

	public Integer getMsgProcess() {
		return msgProcess;
	}

	public void setMsgProcess(Integer msgProcess) {
		this.msgProcess = msgProcess;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		if(startTime != null){
			this.start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
		}
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		if(endTime != null)
			this.end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime);
		this.endTime=endTime;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public Integer getPushNum() {
		return pushNum;
	}

	public void setPushNum(Integer pushNum) {
		this.pushNum = pushNum;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}