package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsSpecPrice;

/**
 * 商品规格价格表
 * @author yangyang
 *
 */
@MyBatisDao
public interface GoodsSpecPriceDao extends CrudDao<GoodsSpecPrice>{
	
	/**
	 * 根据商品查询规格
	 * @return
	 */
	public List<GoodsSpecPrice> speclistBygoods(String id);
	
	/**
	 * 根据商品规格key查询信息
	 * @return
	 */
	public GoodsSpecPrice getSpecPrce(GoodsSpecPrice goodsSpecPrice);
	
	public int modifySpecStoreCount(Map<String, Object> map);
	
	public List<GoodsSpecPrice> querySpecsPrices();
	
	/**
	 * 验证商品的规格是否存在
	 */
	public int checkGoodsSpecKey(@Param(value="goodsId")int goodsId,@Param(value="specKey")String specKey);
	
	/**
	 * 验证商品规格的次数
	 */
	public int checkServiceTimes(@Param(value="goodsId")int goodsId,@Param(value="specKey")String specKey);

	/**
	 * 卡项套卡添加默认规格
	 * @param gsp
	 */
	public void savespec(GoodsSpecPrice gsp);
}
