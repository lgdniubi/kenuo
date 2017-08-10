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
	 * 确定商品是否售后    (是:正在售后    或者   已经售后)
	 * @param returnedGoods
	 * @return
	 */
	public int getReturnedGoods(ReturnedGoods returnedGoods);
	/**
	 * 卡项售后子项添加到mtmy_returned_goods_card
	 * @param rg
	 */
	public void insertReturnGoodsCard(ReturnedGoods rg);
	/**
	 * 查询套卡子项虚拟商品的剩余次数(购买次数-预约次数)
	 * @param returnedGoods
	 * @return
	 */
	public List<ReturnedGoods> getReturnNum(ReturnedGoods returnedGoods);
	/**
	 * 修改套卡子项虚拟商品的剩余次数
	 * @param rgn
	 */
	public void updateCardNum(ReturnedGoods rgn);
	/**
	 * 查询出通用卡的售后次数(mapping表中的service_times - appt_order表中的预约次数)
	 * @param returnedGoods
	 * @return
	 */
	public ReturnedGoods getCommonNum(ReturnedGoods returnedGoods);
	/**
	 * 修改通用卡的售后次数
	 * @param commonNum
	 */
	public void updateCommonNum(ReturnedGoods commonNum);
	/**
	 * 查询卡项子项实物的售后数量
	 * @param returnedGoods
	 * @return
	 */
	public List<ReturnedGoods> getRealnum(ReturnedGoods returnedGoods);
	
}
