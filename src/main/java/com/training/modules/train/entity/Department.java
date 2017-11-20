package com.training.modules.train.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.training.common.persistence.DataEntity;
import com.training.modules.sys.entity.Office;

/**
 * 
 * @className Department
 * @description TODO 部门管理Entity
 * @author chenbing
 * @date 2017年11月14日 兵子
 *
 *
 */
public class Department extends DataEntity<Department>{

	private static final long serialVersionUID = 1L;
	
	private Integer dId;		//部门id
	private String name;		//部门名称
	private Integer sort;		//排序
	private Office office;		//商家id
	
	private List<Position> list = Lists.newArrayList();		//职位
	
	public Integer getdId() {
		return dId;
	}
	public void setdId(Integer dId) {
		this.dId = dId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Office getOffice() {
		return office;
	}
	public void setOffice(Office office) {
		this.office = office;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<Position> getList() {
		return list;
	}
	public void setList(List<Position> list) {
		this.list = list;
	}
	

}
