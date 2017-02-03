package com.training.modules.sys.entity;

import com.training.common.persistence.DataEntity;


/**
 * 美容师用户技能标签中间表实体来
 * @author 小叶 2016-12-30 
 *
 */
public class UserSkill extends DataEntity<UserSkill> {

	private static final long serialVersionUID = 1L;

	private String userId;  //用户id
	private int skillId; //特长id

	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	
	
}
