package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsTypeDao;
import com.training.modules.ec.entity.GoodsType;

/**
 * 商品类型-Service层
 * @author kele
 * @version 2016-6-18
 */
@Service
@Transactional(readOnly = false)
public class GoodsTypeService extends CrudService<GoodsTypeDao,GoodsType>{

	/**
	 * 分页展示所有信息
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<GoodsType> find(Page<GoodsType> page, GoodsType goodsType) {
		goodsType.setPage(page);
		page.setList(dao.findList(goodsType));
		return page;
	} 
	
	/**
	 * 查询所有商品属性
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsType> findAllList(GoodsType goodsType){
		return dao.findAllList(goodsType);
	} 

}
