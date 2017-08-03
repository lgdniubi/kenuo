package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsCard;
/**
 * 商品-DAO层
 * @author 土豆
 * @version 2017-7-26
 */
@MyBatisDao
public interface GoodsCardDao extends CrudDao<GoodsCard>{

	List<GoodsCard> findGoodsList(GoodsCard goodsCard);

	GoodsCard findByGoodsCard(GoodsCard goodsCard);

	
}
