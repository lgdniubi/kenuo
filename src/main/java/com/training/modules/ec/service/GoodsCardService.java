package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.GoodsCardDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCard;

/**
 * 卡项-Service层
 * 
 * @author 土豆
 * @version 2017-7-26
 */
@Service
@Transactional(readOnly = false)
public class GoodsCardService extends CrudService<GoodsCardDao, GoodsCard> {

	
	@Autowired
	private GoodsCardDao goodsCardDao;

	/**
	 * 分页展示所有信息
	 * 
	 * @param page
	 * @param trainLessonss
	 * @return
	 */
	public Page<GoodsCard> findGoodsCard(Page<GoodsCard> page, GoodsCard goodsCard) {
		goodsCard.setPage(page);
		page.setList(dao.findList(goodsCard));
		return page;
	}
	/**
	 * 根据套卡ID查询对于的商品
	 * @param goods
	 * @return
	 */
	/*public List<Goods> findGoodsList(Goods goods) {
		return goodsCardDao.findGoodsList(goods);
	}*/

}
