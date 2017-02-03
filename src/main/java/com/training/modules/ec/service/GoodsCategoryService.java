package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.GoodsCategoryDao;
import com.training.modules.ec.entity.GoodsCategory;

/**
 * 商品分类-Service层
 * @author kele
 * @version 2016-6-15 
 */
@Service
@Transactional(readOnly = false)
public class GoodsCategoryService extends TreeService<GoodsCategoryDao,GoodsCategory>{

	@Autowired
	private GoodsCategoryDao goodsCategoryDao;
	
	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<GoodsCategory> findListbyPID(String pid){
		return goodsCategoryDao.findListbyPID(pid);
	}
	
	/**
	 * 查询所有信息
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsCategory> findAllList(GoodsCategory goodsCategory){
		return goodsCategoryDao.findAllList(goodsCategory);
	}
	
	/**
	 * 查询父类商品分类 
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsCategory> queryParentCategory(GoodsCategory goodsCategory){
		return goodsCategoryDao.queryParentCategory(goodsCategory);
	}
	
	/**
	 * 根据父类ID查询子商品分类
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsCategory> queryCategory(GoodsCategory goodsCategory){
		return goodsCategoryDao.queryCategory(goodsCategory);
	}
	
	/**
	 * 保存商品分类
	 * @param goodsCategory
	 */
	public void insertGoodsCategory(GoodsCategory goodsCategory){
		goodsCategoryDao.insert(goodsCategory);
	}
	
	/**
	 * 修改商品分类
	 * @param goodsCategory
	 * @return
	 */
	public int updateGoodsCategory(GoodsCategory goodsCategory){
		int result = goodsCategoryDao.update(goodsCategory);
		if(1 == result){
			return 1;
		}else{
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	/**
	 * 商品分类是否显示
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsShow(GoodsCategory goodsCategory){
		int result = goodsCategoryDao.updateIsShow(goodsCategory);
		if(1 == result){
			return 1;
		}else{
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	/**
	 * 商品分类是否推荐
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsHot(GoodsCategory goodsCategory){
		int result = goodsCategoryDao.updateIsHot(goodsCategory);
		if(1 == result){
			return 1;
		}else{
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	/**
	 *查询一级分类
	 * @return
	 */
	public List<GoodsCategory> catelist(){
		return goodsCategoryDao.catelist();
	}
	/**
	 * 根据一级查询二级
	 * @param id
	 * @return
	 */
	public List<GoodsCategory> catetwolist(String id){
		return goodsCategoryDao.catetwolist(id);
	}
	
}
