package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ReturnGoods;


/**
 * 退货订单处理Dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface ReturnGoodsDao extends CrudDao<ReturnGoods> {
	
	
	/**
	 * 根据订单orderid 查询 退货订单表 有没有生成退货记录
	 * @param orderid
	 * @return
	 */
	public List<ReturnGoods> retlist(String orderid);
	/**
	 * 更新退货订单数据
	 * @param returnGoods
	 * @return
	 */
	public int updateReturn(ReturnGoods returnGoods);
	/**
	 * 插入退货订单
	 * @param returnGoods
	 * @return
	 */
	public int insertReturn(ReturnGoods returnGoods);
	/**
	 * 确认退款
	 * @return
	 */
	public int updateReturnMomeny(ReturnGoods returnGoods);
}
