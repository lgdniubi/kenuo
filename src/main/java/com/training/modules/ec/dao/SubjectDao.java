package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.Subject;

/**
 * 主题图Dao
 * @author 小叶  2017年2月17日
 *
 */

@MyBatisDao
public interface SubjectDao extends CrudDao<Subject>{

	/**
	 * 插入主题图
	 */
	public void insertSubject(Subject subject);
	
	/**
	 * 更新主题图
	 */
	public void updateSubject(Subject subject);
	
	/**
	 * 根据subId获取相应的主题图
	 * @param subject
	 * @return
	 */
	public Subject getSubject(int subId);
	
	/**
	 * 逻辑删除主题图
	 * @param subId
	 */
	public void delSubject(int subId);
	

	/**
	 * 查询出主题图对应的商品
	 */
	public List<Goods> findGoodsList(Goods goods);
	
	/**
	 * 保存主题图对应的商品 
	 * @param subId
	 * @param goodsId
	 */
	public void insertGoods(@Param("subId")int subId,@Param(value="goodsId")int goodsId);
	
	/**
	 * 删除主题图对应的商品 
	 * @param subId
	 * @param goodsId
	 */
	public void delGoods(@Param("subId")int subId,@Param(value="goodsId")int goodsId);
}
