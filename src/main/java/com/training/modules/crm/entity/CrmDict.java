package com.training.modules.crm.entity;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.validator.constraints.Length;

import com.training.common.persistence.DataEntity;

/**    
* kenuo      
* @description：字典实体类   
* @author：sharp   
* @date：2017年3月7日            
*/
public class CrmDict extends DataEntity<CrmDict> {

	private static final long serialVersionUID = 1L;
	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
	private String parentId;//父Id
	private Object multiValue;//skinFile的多选值
	private String groupType;//父类型
	private String actionType;//操作类型

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Object getMultiValue() {
		return multiValue;
	}

	public void setMultiValue(Object object) {
		this.multiValue = object;
	}

	private List<CrmDict> crmDictList; //每个type返回一个list
	

	public List<CrmDict> getCrmDictList() {
		return crmDictList;
	}

	public void setCrmDictList(List<CrmDict> crmDictList) {
		this.crmDictList = crmDictList;
	}

	public CrmDict() {
		super();
	}
	
	public CrmDict(String id){
		super(id);
	}
	
	public CrmDict(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=1, max=100)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public String toString() {
		return label;
	}
}