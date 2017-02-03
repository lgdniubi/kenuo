package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 商品技能标签中间表实体类
 * @author 小叶    2017年1月9日
 *
 */
public class GoodsSkill extends DataEntity<GoodsSkill>{

	private static final long serialVersionUID = 1L;
	
	private int goodsId;  //商品id
	private int skillId;  //技能标签id
	public int getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
}
