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
public class Question extends DataEntity<Question>{
	
	private static final long serialVersionUID = 1L;
	private String name;			//问题名称
	private String content;			//内容
	private String typeId;			//手册类型id
	private String typeName;			//手册类型名称
	private String type;			//手册类型
	private String status = "1";	//问题状态：1启用,2停用,
	
	public Question() {
		super();
	}
	public Question(String type) {
		super(type);
		this.type = type;
	}
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
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	
}
