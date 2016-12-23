package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.ec.dao.GoodsSpecDao;
import com.training.modules.ec.dao.GoodsSpecItemDao;
import com.training.modules.ec.entity.GoodsSpec;
import com.training.modules.ec.entity.GoodsSpecItem;

/**
 * 商品规格-Service层
 * @author kele
 * @version 2016-6-20
 */
@Service
@Transactional(readOnly = false)
public class GoodsSpecService extends CrudService<GoodsSpecDao,GoodsSpec>{

	@Autowired
	private GoodsSpecDao goodsSpecDao;
	
	@Autowired
	private GoodsSpecItemDao goodsSpecItemDao;
	
	/**
	 * 查询所有信息
	 * @param goodsSpec
	 * @return
	 */
	public List<GoodsSpec> findAllList(GoodsSpec goodsSpec){
		return dao.findAllList(goodsSpec);
	}
	
	/**
	 * 分页展示所有信息
	 * @param page
	 * @param trainLessons
	 * @return
	 */
	public Page<GoodsSpec> find(Page<GoodsSpec> page, GoodsSpec goodsSpec) {
		goodsSpec.setPage(page);
		
		List<GoodsSpec> goodsSpecList = dao.findList(goodsSpec);
		
		List<GoodsSpec> gsList = Lists.newArrayList();
		for (int i = 0; i < goodsSpecList.size(); i++) {
			GoodsSpec gs = goodsSpecList.get(i);
			GoodsSpecItem goodsSpecItem = new GoodsSpecItem();
			goodsSpecItem.setSpecId(Integer.parseInt(gs.getId()));
			List<GoodsSpecItem> goodsSpecItemList = goodsSpecItemDao.findSpecItemList(goodsSpecItem);
			gs.setSpecItemList(goodsSpecItemList);
			gsList.add(gs);
		}
		page.setList(gsList);
		return page;
	}
	
	/**
	 * 查询商品规格以及该规格的规格项信息
	 * @param goodsSpec
	 * @return
	 */
	public List<GoodsSpec> findList(GoodsSpec goodsSpec){
		List<GoodsSpec> goodsSpecList = dao.findList(goodsSpec);
		List<GoodsSpec> gsList = Lists.newArrayList();
		for (int i = 0; i < goodsSpecList.size(); i++) {
			GoodsSpec gs = goodsSpecList.get(i);
			GoodsSpecItem goodsSpecItem = new GoodsSpecItem();
			goodsSpecItem.setSpecId(Integer.parseInt(gs.getId()));
			List<GoodsSpecItem> goodsSpecItemList = goodsSpecItemDao.findSpecItemList(goodsSpecItem);
			gs.setSpecItemList(goodsSpecItemList);
			gsList.add(gs);
		}
		return gsList;
	}
	
	/**
	 * 根据id查询该对象
	 */
	public GoodsSpec get(GoodsSpec goodsSpec){
		GoodsSpec gs = new GoodsSpec();
		gs = goodsSpecDao.get(goodsSpec);
		
		//获取规格项所有数据
		GoodsSpecItem gsi = new GoodsSpecItem();
		gsi.setSpecId(Integer.parseInt(gs.getId()));
		List<GoodsSpecItem> goodsSpecItemList = goodsSpecItemDao.findSpecItemList(gsi);
		gs.setSpecItemList(goodsSpecItemList);
		
		return dao.get(goodsSpec);
	}
	
	/**
	 * 保存/修改 商品规格表
	 * @param goodsSpec
	 */
	public void saveGoodsSpec(GoodsSpec goodsSpec){
		//判断商品规格ID，无则添加，有则修改
		if (StringUtils.isBlank(goodsSpec.getId())){
			goodsSpecDao.insertGoodsSpec(goodsSpec);
			
			//循环获取商品规格项值
			String[] specItems = goodsSpec.getSpecItem().split("\r\n");
			List<GoodsSpecItem> gsiList = Lists.newArrayList();
			if (specItems.length >= 1) {
				for (int i = 0; i < specItems.length; i++) {
					GoodsSpecItem goodsSpecItem = new GoodsSpecItem();
					goodsSpecItem.setItem(specItems[i]);
					goodsSpecItem.setSpecId(Integer.parseInt(goodsSpec.getId()));
					gsiList.add(goodsSpecItem);
				}
			}
			goodsSpec.setSpecItemList(gsiList);
			
			if(goodsSpec.getSpecItemList().size() > 0){
				goodsSpecDao.insertSpecItems(goodsSpec);//添加商品规格项值
			}
			
		}else{
			//修改商品规格
			//商品规格项由异步方式添加、删除
			goodsSpecDao.update(goodsSpec);
		}
	}
	
	/**
	 * 删除商品规格表
	 * 删除商品规格项表
	 */
	public void delete(GoodsSpec goodsSpec) {
		dao.deleteByLogic(goodsSpec);
		goodsSpecDao.deleteSpecItems(goodsSpec);//删除原商品规格表
	} 
	
}
