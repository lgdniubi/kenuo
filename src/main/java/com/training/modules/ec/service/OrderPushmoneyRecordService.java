package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderPushmoneyRecordDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;

/**
 * 订单提成记录Service
 * 
 * @author dalong
 *
 */

@Service
@Transactional(readOnly = false)
public class OrderPushmoneyRecordService extends TreeService<OrderPushmoneyRecordDao, OrderPushmoneyRecord> {

	/**
	 * 保存人员提成信息
	 * @param orderPushmoneyRecord
	 */
	public void saveOrderPushmoneyRecord(OrderPushmoneyRecord orderPushmoneyRecord) {
		dao.saveOrderPushmoneyRecord(orderPushmoneyRecord);
	}

	/**
	 * 根据订单id查询相关业务员
	 * @param orderid
	 * @return
	 */
	public List<OrderPushmoneyRecord> getOrderPushmoneyRecordByOrderId(String orderid) {
		return dao.getOrderPushmoneyRecordByOrderId(orderid);
	}
	
	/**
	 * 删除订单下的所有业务员
	 * @param orderid
	 */
	public void deleteOrderPushmoneyRecord(String orderid) {
		dao.deleteOrderPushmoneyRecord(orderid);
	}
	/**
	 * 删除订单提成人员信息
	 * @param mtmyUserId
	 */
	public void deleteSysUserInfo(int pushmoneyRecordId) {
		dao.deleteSysUserInfo(pushmoneyRecordId);
	}

}
