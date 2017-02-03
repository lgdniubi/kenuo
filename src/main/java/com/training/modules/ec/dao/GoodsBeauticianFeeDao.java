package com.training.modules.ec.dao;


import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsBeauticianFee;

/**
 * 商品服务费基础配置dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface GoodsBeauticianFeeDao extends TreeDao<GoodsBeauticianFee> {

	/**
	 * 保存数据
	 * @param goodsBeauticianFee
	 * @return
	 */
	public int insertBeaut(GoodsBeauticianFee goodsBeauticianFee);
	/**
	 * 根据商品id查询
	 * @param goodsBeauticianFee
	 * @return
	 */
	public List<GoodsBeauticianFee> selectBygoodsId(int goodsId);
	/**
	 * 修改数据
	 * @param goodsBeauticianFee
	 * @return
	 */
	public int updateBeautician(GoodsBeauticianFee goodsBeauticianFee);
	/**
	 * 根据商品id 查询记录数
	 * @param goodsId
	 * @return
	 */
	public int selectByGoodsIdNum(int goodsId);
	
}
