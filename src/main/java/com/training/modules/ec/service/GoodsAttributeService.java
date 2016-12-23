package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsAttributeDao;
import com.training.modules.ec.entity.GoodsAttribute;

/**
 * 商品属性-Service层
 * @author kele
 * @version 2016-6-20
 */
@Service
@Transactional(readOnly = false)
public class GoodsAttributeService extends CrudService<GoodsAttributeDao,GoodsAttribute>{

	@Autowired
	private GoodsAttributeDao goodsAttributeDao;
	
	/**
	 * 查询所有信息
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsAttribute> findAllList(GoodsAttribute goodsAttribute){
		return dao.findAllList(goodsAttribute);
	}
	
	/**
	 * 分页展示所有信息
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<GoodsAttribute> find(Page<GoodsAttribute> page, GoodsAttribute goodsAttribute) {
		goodsAttribute.setPage(page);
		page.setList(dao.findList(goodsAttribute));
		return page;
	} 
	
	
	/**
	 * 商品属性是否检索
	 * @param goodsCategory
	 * @return
	 */
	public int updateAttrIndex(GoodsAttribute goodsAttribute){
		int result = goodsAttributeDao.updateAttrIndex(goodsAttribute);
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
