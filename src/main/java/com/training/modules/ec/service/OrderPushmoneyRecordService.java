package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderPushmoneyRecordDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.PushmoneyRecordLog;

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
	 * 根据pushmoneyRecordId查询相关业务员
	 * @param pushmoneyRecordId
	 * @return
	 */
	public OrderPushmoneyRecord getOrderPushmoneyRecordById(int pushmoneyRecordId) {
		return dao.getOrderPushmoneyRecordById(pushmoneyRecordId);
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
	 * 删除订单提成人员信息
	 * @param orderPushmoneyRecord
	 */
	public void deleteSysUserInfo(OrderPushmoneyRecord orderPushmoneyRecord) {
		dao.deleteSysUserInfo(orderPushmoneyRecord);
	}
	
	/**
	 * 修改提成人员的提成金额
	 * @param orderPushmoneyRecord
	 */
	public void updatePushMoney(OrderPushmoneyRecord orderPushmoneyRecord){
		dao.updatePushMoney(orderPushmoneyRecord);
	}
	
	/**
	 * 保存修改订单的提成人员的提成金额日志
	 * @param pushmoneyRecordLog
	 */
	public void insertPushMoneyLog(PushmoneyRecordLog pushmoneyRecordLog){
		dao.insertPushMoneyLog(pushmoneyRecordLog);
	}

	/**
	 * 通过业务员id(属于妃子校的)查询业务员归属机构
	 * @param orderPushmoneyRecord 
	 * @return
	 */
	public OrderPushmoneyRecord getOfficeIdByUserId(OrderPushmoneyRecord orderPushmoneyRecord) {
		return dao.getOfficeIdByUserId(orderPushmoneyRecord);
	}

}
