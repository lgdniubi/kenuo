/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.Collections3;
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 每天每夜通知通告Entity
 * 
 * @version 2014-05-16
 */
public class MtmyOaNotify extends DataEntity<MtmyOaNotify> {
	
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
	
	private List<MtmyOaNotifyRecord> mtmyOaNotifyRecordList = Lists.newArrayList();
	
	private Users users;
	
	private String phones;      //组推人员的手机号
	private String data;           //组推的时候接受的数据（手机号或用户id）
	private String groupImportType;     //组推时导入类型（0：手机号，1：用户id）
	
	public MtmyOaNotify() {
		super();
	}

	public MtmyOaNotify(String id){
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
	
	public List<MtmyOaNotifyRecord> getMtmyOaNotifyRecordList() {
		return mtmyOaNotifyRecordList;
	}

	public void setMtmyOaNotifyRecordList(List<MtmyOaNotifyRecord> mtmyOaNotifyRecordList) {
		this.mtmyOaNotifyRecordList = mtmyOaNotifyRecordList;
	}

	/**
	 * 获取通知发送记录用户ID
	 * @return
	 */
	public String getMtmyOaNotifyRecordIds() {
		return Collections3.extractToString(mtmyOaNotifyRecordList, "users.id", ",") ;
	}
	
	/**
	 * 设置通知发送记录用户ID
	 * @return
	 */
	public void setMtmyOaNotifyRecordIds(String mtmyOaNotifyRecord) {
		this.mtmyOaNotifyRecordList = Lists.newArrayList();
		for (String userid : StringUtils.split(mtmyOaNotifyRecord, ",")){
			MtmyOaNotifyRecord entity = new MtmyOaNotifyRecord();
			entity.setId(IdGen.uuid());
			entity.setMtmyOaNotify(this);
			Users users = new Users();
			users.setId(userid);
			entity.setUsers(users);
			entity.setReadFlag("0");
			this.mtmyOaNotifyRecordList.add(entity);
		}
	}

	/**
	 * 获取通知发送记录用户Name
	 * @return
	 */
	public String getMtmyOaNotifyRecordNames() {
		return Collections3.extractToString(mtmyOaNotifyRecordList, "users.nickname", ",") ;
	}
	
	/**
	 * 设置通知发送记录用户Name
	 * @return
	 */
	public void setMtmyOaNotifyRecordNames(String mtmyOaNotifyRecord) {
		// 什么也不做
	}

	/**
	 * 获取通知发送记录用户mobile
	 * @return
	 */
	public String getMtmyOaNotifyRecordMobile() {
		return Collections3.extractToString(mtmyOaNotifyRecordList, "mtmyOaNotify.phones", ",") ;
	}
	/**
	 * 获取通知发送记录用户IDS
	 * @return
	 */
	public String getMtmyOaNotifyRecordUserIds() {
		return Collections3.extractToString(mtmyOaNotifyRecordList, "mtmyOaNotify.users.userid", ",") ;
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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	@JsonIgnore
	@ExcelField(title="手机号码", align=2, sort=1,type=2)
	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}
	
	@JsonIgnore
	@ExcelField(title="数据", align=2, sort=1,type=2)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getGroupImportType() {
		return groupImportType;
	}

	public void setGroupImportType(String groupImportType) {
		this.groupImportType = groupImportType;
	}
	
	
}