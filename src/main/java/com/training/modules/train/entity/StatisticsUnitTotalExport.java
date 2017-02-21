package com.training.modules.train.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.common.persistence.DataEntity;
import com.training.common.utils.excel.annotation.ExcelField;

/**
 * 统计单元测试
 * @author  yangyang
 * @version 
 */
public class StatisticsUnitTotalExport extends DataEntity<StatisticsUnitTotalExport>{

	/**
	 * 课程统计总揽
	 */
	private static final long serialVersionUID = 1L;
	

	private String categorysName;		//课程分类名称
	private String adoptNum;				//通过人数
	
	
	@JsonIgnore
	@ExcelField(title="分类名称", align=2, sort=1)
	public String getCategorysName() {
		return categorysName;
	}
	public void setCategorysName(String categorysName) {
		this.categorysName = categorysName;
	}
	@JsonIgnore
	@ExcelField(title="通过人数", align=2, sort=2)
	public String getAdoptNum() {
		return adoptNum;
	}
	public void setAdoptNum(String adoptNum) {
		this.adoptNum = adoptNum;
	}			
}
