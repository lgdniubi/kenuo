package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 设备标签管理
 * @author 小叶    2017-1-10 
 *
 */
public class EquipmentLabel extends DataEntity<EquipmentLabel>{

	private static final long serialVersionUID = 1L;
	private int equipmentLabelId;     //设备标签id
 	private String name;             //设备名称
	private String no;               //设备编号
	private int type;                //设备标签类型
	private String description;       //设备描述
	private int delflag;              //删除标识
	
	private String flag;             //用来验证添加还是修改用的标识
	private String newFlag;         //用来验证查看通用还是特殊标签的标识 
	
	public int getEquipmentLabelId() {
		return equipmentLabelId;
	}
	public void setEquipmentLabelId(int equipmentLabelId) {
		this.equipmentLabelId = equipmentLabelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	
}
