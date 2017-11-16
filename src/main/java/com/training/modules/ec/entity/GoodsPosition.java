package com.training.modules.ec.entity;

import com.training.common.persistence.TreeEntity;

/**
 * 每天美耶-项目部位表
 * @author 土豆
 * @version 2017-10-9
 */
public class GoodsPosition extends TreeEntity<GoodsPosition>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	name;				//部位名称
	private int 	level;				//等级
	
	private int num;					//查看是否有子类
	
	@Override
	public GoodsPosition getParent() {
		return parent;
	}
	
	@Override
	public void setParent(GoodsPosition parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
