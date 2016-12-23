/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.training.common.persistence.TreeEntity;

/**
 * 区域Entity
 * 
 * @version 2013-05-15
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	private String code; 		// 区域编码
	private String type; 		// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	private String typeName;	// 区域对应名称（1：国家；2：省份、直辖市；3：地市；4：区县）
	private int childrenNum;	// 子类数量
	
	private String Pid;			//省份id
	private String Pname;		//省名称
	private String Cid;			//市区id
	private String Cname;		//市区名称
	private String Xid;			//县区id
	private String Xname;		//县区名称
	
	
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPid() {
		return Pid;
	}

	public void setPid(String pid) {
		Pid = pid;
	}

	public String getPname() {
		return Pname;
	}

	public void setPname(String pname) {
		Pname = pname;
	}

	public String getCid() {
		return Cid;
	}

	public void setCid(String cid) {
		Cid = cid;
	}

	public String getCname() {
		return Cname;
	}

	public void setCname(String cname) {
		Cname = cname;
	}

	public String getXid() {
		return Xid;
	}

	public void setXid(String xid) {
		Xid = xid;
	}

	public String getXname() {
		return Xname;
	}

	public void setXname(String xname) {
		Xname = xname;
	}

	public Area(){
		super();
		this.sort = 30;
	}

	public Area(String id){
		super(id);
	}
	
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(int childrenNum) {
		this.childrenNum = childrenNum;
	}

	@Override
	public String toString() {
		return name;
	}
}