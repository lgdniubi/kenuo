package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.GoodsPositionDao;
import com.training.modules.ec.entity.GoodsPosition;

/**
 * 项目部位-Service层
 * @author 土豆
 * @version 2017-10-9 
 */
@Service
@Transactional(readOnly = false)
public class GoodsPositionService extends TreeService<GoodsPositionDao,GoodsPosition>{

	@Autowired
	private GoodsPositionDao goodsPositionDao;
	
	/**
	 * 通过父类ID查询  子类
	 * @param pid	父类ID
	 * @return
	 */
	public List<GoodsPosition> findListbyPID(String pid){
		return goodsPositionDao.findListbyPID(pid);
	}
	
	/**
	 * 查询所有信息
	 * @param goodsCategory
	 * @return
	 */
	public List<GoodsPosition> findAllList(GoodsPosition goodsPosition){
		return goodsPositionDao.findAllList(goodsPosition);
	}
	
	/**
	 * 保存项目部位
	 * @param goodsCategory
	 */
	public void insertGoodsPosition(GoodsPosition goodsPosition){
		goodsPositionDao.insert(goodsPosition);
	}
	
	/**
	 * 修改项目部位
	 * @param goodsCategory
	 */
	public void updateGoodsPosition(GoodsPosition goodsPosition){
		goodsPositionDao.update(goodsPosition);
	}
	
}
