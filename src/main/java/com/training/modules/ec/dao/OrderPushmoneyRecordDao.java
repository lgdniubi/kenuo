package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
/**
 * 订单提成记录Dao
 * @author dalong
 *
 */

@MyBatisDao
public interface OrderPushmoneyRecordDao extends TreeDao<OrderPushmoneyRecord> {

	/**
	 * 保存人员提成今昔
	 * @param orderPushmoneyRecord
	 */
	void saveOrderPushmoneyRecord(OrderPushmoneyRecord orderPushmoneyRecord);

	/**
	 * 根据订单id查询相关业务员
	 * @param orderid
	 * @return
	 */
	List<OrderPushmoneyRecord> getOrderPushmoneyRecordByOrderId(@Param("orderid")String orderid);

	/**
	 * 删除当前订单下所有业务员提成
	 * @param orderid
	 */
	void deleteOrderPushmoneyRecord(@Param("orderid")String orderid);
	/**
	 * 删除订单提成人员信息
	 * @param mtmyUserId
	 */
	void deleteMtmyUserInfo(@Param("mtmyUserId")Integer mtmyUserId);

}
