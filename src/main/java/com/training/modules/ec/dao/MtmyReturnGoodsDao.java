package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyReturnGoods;

/**
 * 商品退货期设置
 * @author coffee
 *
 */
@MyBatisDao
public interface MtmyReturnGoodsDao extends CrudDao<MtmyReturnGoods>{
	
}


