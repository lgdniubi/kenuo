package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderPushmoneyRecordDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.PushmoneyRecordLog;
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

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

	/**
	 * 查询营业额是否为第二次编辑(根据售后id是否存在)
	 * @param turnOverDetails
	 * @return
	 */
	public List<OrderPushmoneyRecord> findOrderPushmoneyRecordList(TurnOverDetails turnOverDetails) {
		return dao.findOrderPushmoneyRecordList(turnOverDetails);
	}

	/**
	 * 保存业务员营业额
	 * @param orderPushmoneyRecord
	 */
	public void saveEdite(OrderPushmoneyRecord orderPushmoneyRecord) {
		User user = UserUtils.getUser();
		//业务员的营业额信息添加到表中   (先查询信息)
		List<OrderPushmoneyRecord> list = dao.getList(orderPushmoneyRecord.getOrderId());
		//把获取到的金额全部切割
		String pushMoneys = orderPushmoneyRecord.getPushMoneys();
		String[] split = pushMoneys.split(",");
		for (int i = 0; i < split.length; i++) {
			list.get(i).setOrderId(orderPushmoneyRecord.getOrderId());
			list.get(i).setReturnedId(orderPushmoneyRecord.getReturnedId());
			list.get(i).setPushmoneyRecordId(list.get(i).getPushmoneyRecordId());
			list.get(i).setPushMoney(Double.parseDouble(split[i]));
			list.get(i).setType(3);
			list.get(i).setCreateBy(user);
			dao.save(list.get(i));
		}
	}

	/**
	 * 获取各个部门的营业额合计
	 * @param orderId
	 * @return
	 */
	public List<OrderPushmoneyRecord> getSumTurnover(String orderId) {
		return dao.getSumTurnover(orderId);
	}

	/**
	 * 查询每个业务员的售后审核扣减的营业额
	 * @param turnOverDetails
	 * @return
	 */
	public List<OrderPushmoneyRecord> getReturnedPushmoneyList(TurnOverDetails turnOverDetails) {
		return dao.getReturnedPushmoneyList(turnOverDetails);
	}

}
