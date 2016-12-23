package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsType;

/**
 * 商品类型-DAO层
 * @author kele
 * @version 2016-6-18
 */
@MyBatisDao
public interface GoodsTypeDao extends CrudDao<GoodsType>{

}
