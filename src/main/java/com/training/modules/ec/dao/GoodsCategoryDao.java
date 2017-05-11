package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsCategory;

/**
 * 商品分类-DAO层
 * @author kele
 * @version 2016-6-4 16:17:23
 */
@MyBatisDao
public interface GoodsCategoryDao extends TreeDao<GoodsCategory>{

	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<GoodsCategory> findListbyPID(String pid);
	
	/**
	 * 商品分类是否显示
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsShow(GoodsCategory goodsCategory);
	
	/**
	 * 商品分类是否推荐
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsHot(GoodsCategory goodsCategory);
	
	/**
	 * 查询父类商品分类 
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsCategory> queryParentCategory(GoodsCategory goodsCategory);
	
	/**
	 * 根据父类ID查询子商品分类
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsCategory> queryCategory(GoodsCategory goodsCategory);
	/**
	 * 查询一级分类
	 * @return
	 */
	public List<GoodsCategory> catelist();
	/**
	 * 查询二级分类
	 * @return
	 */
	public List<GoodsCategory> catetwolist(String id);

	
}
