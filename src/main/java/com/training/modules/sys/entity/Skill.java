package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;

public class Skill extends DataEntity<Skill>{

	private static final long serialVersionUID = 1L;
	
	private int skillId;    //技能标签id
    private String name;    //技能标签名字
    private String description;  //技能描述
    private int delflag;    //删除标识
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
