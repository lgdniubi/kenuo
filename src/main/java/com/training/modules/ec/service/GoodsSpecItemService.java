package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsSpecItemDao;
import com.training.modules.ec.entity.GoodsSpecItem;

/**
 * 商品规格项-Service层
 * @author kele
 * @version 2016-6-20
 */
@Service
@Transactional(readOnly = false)
public class GoodsSpecItemService extends CrudService<GoodsSpecItemDao,GoodsSpecItem>{

	@Autowired
	private GoodsSpecItemDao goodsSpecItemDao;
	
	/**
	 * 根据id查询该对象
	 */
	public GoodsSpecItem get(GoodsSpecItem goodsSpecItem){
		return dao.get(goodsSpecItem);
	}
	
	/**
	 * 根据拼接好的itemid，查询
	 * @param items
	 * @return
	 */
	public List<GoodsSpecItem> findSpecItems(GoodsSpecItem goodsSpecItem){
		return dao.findSpecItems(goodsSpecItem);
	}
	
	/**
	 * 保存商品规格项
	 * @return
	 */
	public int addItems(GoodsSpecItem goodsSpecItem){
		goodsSpecItemDao.saveItems(goodsSpecItem);
		int result = goodsSpecItem.getSpecItemId();
		if(0 == result){
			try {
				throw new Exception("保存失败");
			} catch (Exception e) {
				
			}
		}
		return result; 
	}
	
	/**
	 * 删除商品规格项
	 * @return
	 */
	public int deleteItems(GoodsSpecItem goodsSpecItem){
		return goodsSpecItemDao.delteteItems(goodsSpecItem);
	}
}
