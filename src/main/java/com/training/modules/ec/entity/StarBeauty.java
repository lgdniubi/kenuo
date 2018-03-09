package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 
 * 明星技师自由配置表
 * @author 土豆   2018-3-7
 *
 */
public class StarBeauty extends DataEntity<StarBeauty>{

	private static final long serialVersionUID = 1L;
	private String name;		//组名
	private int isShow;			//是否展示（0：否，1：是）
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	
	
}
