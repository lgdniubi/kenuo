package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsBrandDao;
import com.training.modules.ec.entity.GoodsBrand;

/**
 * 商品品牌-Service层
 * @author kele
 * @version 2016-6-17
 */
@Service
@Transactional(readOnly = false)
public class GoodsBrandService extends CrudService<GoodsBrandDao,GoodsBrand>{

	@Autowired
	private GoodsBrandDao goodsBrandDao;
	
	/**
	 * 分页展示所有信息
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<GoodsBrand> find(Page<GoodsBrand> page, GoodsBrand goodsBrand) {
		goodsBrand.setPage(page);
		page.setList(dao.findList(goodsBrand));
		return page;
	} 
	
	/**
	 * 查询所有信息
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsBrand> findAllList(GoodsBrand goodsBrand){
		return goodsBrandDao.findAllList(goodsBrand);
	}
	
	/**
	 * 保存商品品牌
	 * @param goodsCategory
	 */
	public void insertGoodsBrand(GoodsBrand goodsBrand){
		goodsBrandDao.insert(goodsBrand);
	}
	
	/**
	 * 修改商品品牌分类
	 * @param goodsCategory
	 * @return
	 */
	public int updateGoodsBrand(GoodsBrand goodsBrand){
		int result = goodsBrandDao.update(goodsBrand);
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
	 * 商品品牌是否推荐
	 * @param goodsCategory
	 * @return
	 */
	public int updateIsHot(GoodsBrand goodsBrand){
		int result = goodsBrandDao.updateIsHot(goodsBrand);
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
}
