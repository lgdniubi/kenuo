package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyReturnGoodsDao;
import com.training.modules.ec.entity.MtmyReturnGoods;



/**
 * 商品退货期Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyReturnGoodsService extends CrudService<MtmyReturnGoodsDao,MtmyReturnGoods> {
	
	/**
	 * 分页查询商品退货期·
	 * @param page
	 * @param mtmyReturnGoods
	 * @return
	 */
	public Page<MtmyReturnGoods> findList(Page<MtmyReturnGoods> page, MtmyReturnGoods mtmyReturnGoods) {
		mtmyReturnGoods.setPage(page);
		page.setList(dao.findList(mtmyReturnGoods));
		return page;
	} 
	
}
