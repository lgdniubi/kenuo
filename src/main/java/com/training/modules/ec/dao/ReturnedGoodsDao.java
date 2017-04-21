package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;


/**
 * 退货订单处理Dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface ReturnedGoodsDao extends CrudDao<ReturnedGoods> {
	
	/**
	 * 查询退货商品图片
	 * @param id
	 * @return
	 */
	public List<ReturnedGoodsImages> selectImgById(String id);
	/**
	 * 审核
	 * @param returnedGoods
	 * @return
	 */
	public int saveEdite(ReturnedGoods returnedGoods);
	
	/**
	 * 确认入库
	 * @param returnedGoods
	 * @return
	 */
	public int confirmTake(ReturnedGoods returnedGoods);
	
	/**
	 * 重新发货
	 * @param returnedGoods
	 * @return
	 */
	public int UpdateShipping(ReturnedGoods returnedGoods);
	/**
	 * 确认退款
	 * @param returnedGoods
	 * @return
	 */
	public int updateReturnMomeny(ReturnedGoods returnedGoods);
	
	/**
	 * 申请退款
	 * @param returnedGoods
	 * @return
	 */
	public int confirmApply(ReturnedGoods returnedGoods);
	/**
	 * 重新发货
	 * @return
	 */
	public int againSendApply(ReturnedGoods returnedGoods);
	
	/**
	 * 插入语句
	 * @param returnedGoods
	 * @return
	 */
	public int insertReturn(ReturnedGoods returnedGoods);
	
	/**
	 * 强制取消
	 * @param returnedGoods
	 * @return
	 */
	public int insertForcedCancel(ReturnedGoods returnedGoods);
	
	/**
	 * 根据用户Id查找售后记录
	 * @param 
	 * @return int
	 */
	public List<ReturnedGoods> findListByUser(ReturnedGoods returnedGoods);
	
	/**
	 * 先查询实物中是否有正在退货的商品
	 * @return
	 */
	public int selectreturnedGoodsNum(String orderid);
	/**
	 * 根据orderID查询退货商品的return_status为11-15的数量
	 * @param orderid
	 * @return
	 */
	public int findreturnedGoodsNum(String orderid);

}
