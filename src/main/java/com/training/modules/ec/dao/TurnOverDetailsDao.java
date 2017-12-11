package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.TurnOverDetails;

/**
 * 营业额明细表Dao
 * @author xiaoye  2017年11月16日
 *
 */
@MyBatisDao
public interface TurnOverDetailsDao extends CrudDao<TurnOverDetails>{
	
	/**
	 * 同步details表的数据到营业额明细表中
	 * @param turnOverDetails
	 */
	public void saveTurnOverDetails(TurnOverDetails turnOverDetails); 
	
	/**
	 * 根据订单id查询该订单的店营业额
	 * @return
	 */
	public List<TurnOverDetails> selectDetailsByOrderId(String orderId);
	
	/**
	 * 保存店营业额中处理预约金的那条记录的归属店铺
	 * @param belongOfficeId
	 * @param turnOverDetailsId
	 */
	public void updateBelongOffice(@Param(value="belongOfficeId")String belongOfficeId,@Param(value="turnOverDetailsId")int turnOverDetailsId,@Param(value="settleBy")String settleBy);

	/**
	 * 根据店营业额详情表查询业务员营业额详情
	 * @return
	 */
	public List<TurnOverDetails> selectPushDetails(String orderId);
	
	/**
	 * 查询单个店营业额对应的业务员营业额详情
	 * @return
	 */
	public TurnOverDetails selectOneDetails(int turnOverDetailsId);
	
	/**
	 * 保存业务员提成营业额
	 * @param orderPushmoneyRecord
	 */
	public void savePushMoneyRecord(List<OrderPushmoneyRecord> list);
	
	/**
	 * 查看业务员营业额明细 
	 * @param orderPushmoneyRecord
	 * @return
	 */
	public List<OrderPushmoneyRecord> queryDetailsForPush(OrderPushmoneyRecord orderPushmoneyRecord);
}
