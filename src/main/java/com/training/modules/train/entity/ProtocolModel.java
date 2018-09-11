package com.training.modules.train.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.training.common.persistence.DataEntity;
import com.training.common.utils.IdGen;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 
 * @author: jingfeng
 * @date 2018年5月2日下午3:18:53
 * @Description:供应链--协议模板
 */
public class ProtocolModel extends DataEntity<ProtocolModel>{
	
	  
	private static final long serialVersionUID = 1L;
	private String pid = "0";				//重新签订协议模板id
	private Integer isPid;				//是否重新签订
	private String name;			//模板名称
	private String content;			//内容
	private String type;			//协议类型：1妃子校注册,2用户认证手艺人,3用户认证企业,4用户登录商家PC
	private String status = "3";			//协议模板状态：1启用,3停用,2变更后历史
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPid() {
		if(getAssign()&&!getIsNewRecord())this.pid = id;
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public boolean getAssign() {
		if(isPid != null && isPid == 1)return true;
		return false;
	}
	public Integer getIsPid() {
		return isPid;
	}
	public void setIsPid(Integer isPid) {
		this.isPid = isPid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public void preInsert(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
}
