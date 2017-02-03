package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 仓库模板中间表
 * @author dalong
 *
 */
public class PdWhTemplates extends TreeEntity<PdWhTemplates> {
	
	
	private static final long serialVersionUID = 1L;
	private Integer warehouseId;
	private Integer templateId;
	private int isDefault;
	private Integer isDelFlag;
	
	public Integer getIsDelFlag() {
		return isDelFlag;
	}

	public void setIsDelFlag(Integer isDelFlag) {
		this.isDelFlag = isDelFlag;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public PdWhTemplates getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(PdWhTemplates parent) {
		// TODO Auto-generated method stub
		
	}
}
