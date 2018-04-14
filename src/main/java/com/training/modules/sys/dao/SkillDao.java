package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSkill;
import com.training.modules.sys.entity.Skill;
import com.training.modules.sys.entity.UserSkill;

/**
 * 技能标签dao
 * @author 小叶  2016.12.27
 *
 */

@MyBatisDao
public interface SkillDao extends CrudDao<Skill>{
	/**
	 * 查询出所有的技能标签(只查找显示的)
	 */
	public List<Skill> findAllList();
	
	/**
	 * 查询出所有的技能标签(无论显示还是隐藏都查出来)
	 */
	public List<Skill> newFindAllList();
	
	/**
	 * 新增技能标签
	 * @param skill
	 * @return
	 */
	public int insertSkill(Skill skill);
	
	/**
	 * 编辑技能标签
	 * @param skill
	 * @return
	 */
	public int updateSkill(Skill skill);
	
	/**
	 * 逻辑删除技能标签
	 * @param skillId
	 * @return
	 */
	public int deleteSkill(Skill skill);
	
	/**
	 * 插入美容师用户技能标签中间表
	 * @param list
	 * @return
	 */
	public int insertUserSkill(List<UserSkill> list);
	
	/**
	 * 删除技能标签表中间冗余部分数据并且从新更新
	 * @param userid
	 * @return
	 */
	
	public int deletormSpec(String userid);
	
	/**
	 * 根据用户id 查找技能标签
	 * @param userId
	 * @return
	 */
	public List<Skill> findSkillListByuserid(String userId);
	
	/**
	 * 插入商品技能标签中间表
	 * @param list
	 * @return
	 */
	public int insertGoodsSkill(List<GoodsSkill> list);
	
	/**
	 * 删除商品技能标签表中间冗余部分数据并且从新更新
	 * @param goodsId
	 * @return
	 */
	public int deleteGoodsSkill(int goodsId);
	
	/**
	 * 根据商品id 查找技能标签
	 * @param goodsId
	 * @return
	 */
	public List<Skill> findSkillListByGoodsId(int goodsId);
	
	/**
	 * 修改技能标签是否显示
	 */
	public void updateIsShow(Skill skill);
	
	/**
	 * 验证技能标签对应的商品是否仍有上架的
	 * @param skillId
	 * @return
	 */
	public int selectGoodsisOnSale(int skillId);
	
	/**
	 * 验证技能标签名称
	 * @param name
	 * @return
	 */
	public int getByName(String name);

	public int validDel(Skill skill);
}
