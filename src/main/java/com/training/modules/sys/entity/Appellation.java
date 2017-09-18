package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

/**
 * 称谓标签表
 * @author xiaoye  2017年9月15日10:28:42
 *
 */
public class Appellation extends DataEntity<Appellation>{

	private static final long serialVersionUID = 1L;

	private int appellationId;         //称谓id
	private String name;               //称谓名称
	private int delflag;               //删除标识（0：正常；1：删除）
	
	public int getAppellationId() {
		return appellationId;
	}
	public void setAppellationId(int appellationId) {
		this.appellationId = appellationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	
	
}
