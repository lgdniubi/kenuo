package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.ec.entity.TurnOverDetails;


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
	 * 查询订单是否正在售后 或者 已经售后 的个数
	 * @param returnedGoods
	 * @return
	 */
	public int getReturnGoodsNum(ReturnedGoods returnedGoods);
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
	 * 查询卡项子项实物的售后数量
	 * @param returnedGoods
	 * @return
	 */
	public List<ReturnedGoods> getRealnum(ReturnedGoods returnedGoods);
	/**
	 * 查询订单是否售后或者正在
	 * @param returnedGoods
	 * @return
	 */
	public int getReturnedGoodsNum(ReturnedGoods returnedGoods);
	/**
	 * 获取订单商品中的已退款金额
	 * @param returnedGoods
	 * @return
	 */
	public double getSurplusReturnAmount(ReturnedGoods returnedGoods);
	/**
	 * 获取营业额信息
	 * @param returnedGoods
	 * @return
	 */
	public TurnOverDetails getTurnover(ReturnedGoods returnedGoods);
	/**
	 * 获取业务员营业额
	 * @param returnedGoods
	 * @return
	 */
	public OrderPushmoneyRecord getOrderPushmoneyRecord(ReturnedGoods returnedGoods);
	/**
	 * 获取店营业额
	 * @param returnedGoods
	 * @return
	 */
	public TurnOverDetails getMtmyTurnoverDetails(ReturnedGoods returnedGoods);
	/**
	 * 获取店铺营业额的操作日志
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> findMtmyTurnoverDetailsList(TurnOverDetails turnOverDetails);
	/**
	 * 获取店营业额明细列表
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getMtmyTurnoverDetailsList(TurnOverDetails turnOverDetails);
	/**
	 * 保存店营业额增减值
	 * @param turnOverDetails
	 */
	public void saveMtmyTurnoverDetails(TurnOverDetails turnOverDetails);
	/**
	 * 查询每个业务员的售后审核扣减的营业额
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getReturnedAmountList(TurnOverDetails turnOverDetails);
	/**
	 * 查询营业额是否为第二次编辑(根据售后id是否存在)
	 * @param turnOverDetails
	 * @return
	 */
	List<OrderPushmoneyRecord> findOrderPushmoneyRecordList(TurnOverDetails turnOverDetails);
	/**
	 * 查询每个业务员的售后审核扣减的营业额
	 * @param turnOverDetails
	 * @return
	 */
	List<OrderPushmoneyRecord> getReturnedPushmoneyList(TurnOverDetails turnOverDetails);
	/**
	 * 获取各个部门的营业额合计
	 * @param orderId
	 * @return
	 */
	public List<OrderPushmoneyRecord> getSumBeauticianTurnover(String orderId);
	/**
	 * 根据id获取修改业务员营业额的所有数据
	 * @param orderId
	 * @return
	 */
	public List<OrderPushmoneyRecord> getBeauticianTurnoverList(String orderId);
	/**
	 * 添加业务员营业额
	 * @param orderPushmoneyRecord
	 */
	public void saveBeauticianTurnover(OrderPushmoneyRecord orderPushmoneyRecord);
	/**
	 * 获取业务员退款营业额(type=3)
	 * @param turnOverDetails
	 * @return
	 */
	public List<OrderPushmoneyRecord> getOrderPushmoneyRecordListView(TurnOverDetails turnOverDetails);
	/**
	 * 获取店铺退款营业额(type=3)
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getMtmyTurnoverDetailsListView(TurnOverDetails turnOverDetails);
	/**
	 * 获取业务员退货营业额的操作记录
	 * @param orderPushmoneyRecord
	 * @return
	 */
	public List<OrderPushmoneyRecord> getReturnedBeauticianLog(OrderPushmoneyRecord orderPushmoneyRecord);
	/**
	 * 查询商品是否有过未取消的预约(通过mappingid查询预约表)
	 * @param goodsMappingId
	 * @return
	 */
	public int getCountApptOrder(String goodsMappingId);
	/**
	 * 获取不包含自己本身的总的售后金额和售后数量
	 * @param returnedGoods
	 * @return
	 */
	public ReturnedGoods getAmountAndNum(ReturnedGoods returnedGoods);
	/**
	 * 获取该订单中单个实物商品总售后数量
	 * @param orderGoods
	 * @return
	 */
	public int getOrderGoodsReturnNum(OrderGoods orderGoods);
	/**
	 * 查询订单中商品正在售后 或者 已经售后 的个数(但不包含自身.平预约金和平欠款用到)
	 * @param returnedGoods
	 * @return
	 */
	public int getReturnSaleNum(ReturnedGoods returnedGoods);
	/**
	 * 根据商品id查询不是当前售后订单id的实物售后数量
	 * @param returnedGoods
	 * @return
	 */
	public int getRealReturnNum(ReturnedGoods returnedGoods);
}
