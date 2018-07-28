package com.training.modules.sys.entity;

import java.util.Date;
import com.training.common.persistence.TreeEntity;

/**
 * 机构日志记录表
 * 
 * @version 2017-09-25  土豆
 */
public class OfficeLog extends TreeEntity<OfficeLog> {

	private static final long serialVersionUID = 1L;
	
	private String officeId; 	// 修改机构ID
	private int type;           // 日志类型(0:创建，1:删除，2：开店，3：关店)
	private String content;		// 修改内容
	private String remark;      // 备注
	
	private Date realtime;      //实际时间
	private Date finalTime;     //最终时间
	
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public OfficeLog getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParent(OfficeLog parent) {
		// TODO Auto-generated method stub
		
	}
	public Date getRealtime() {
		return realtime;
	}
	public void setRealtime(Date realtime) {
		this.realtime = realtime;
	}
	public Date getFinalTime() {
		return finalTime;
	}
	public void setFinalTime(Date finalTime) {
		this.finalTime = finalTime;
	}
	
	
}