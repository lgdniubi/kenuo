package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.PushmoneyRecordLog;
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
	 * 根据pushmoneyRecordId查询相关业务员
	 * @param pushmoneyRecordId
	 * @return
	 */
	OrderPushmoneyRecord getOrderPushmoneyRecordById(@Param("pushmoneyRecordId")int pushmoneyRecordId);
	
	/**
	 * 根据订单id查询相关业务员
	 * @param orderid
	 * @return
	 */
	List<OrderPushmoneyRecord> getOrderPushmoneyRecordByOrderId(@Param("orderid")String orderid);

	/**
	 * 删除订单提成人员信息
	 * @param orderPushmoneyRecord
	 */
	void deleteSysUserInfo(OrderPushmoneyRecord orderPushmoneyRecord);
	
	/**
	 * 修改提成人员的提成金额
	 * @param orderPushmoneyRecord
	 */
	void updatePushMoney(OrderPushmoneyRecord orderPushmoneyRecord);
	
	/**
	 * 保存修改订单的提成人员的提成金额日志
	 * @param pushmoneyRecordLog
	 */
	void insertPushMoneyLog(PushmoneyRecordLog pushmoneyRecordLog);

	/**
	 * 通过业务员id(属于妃子校的)查询业务员归属机构
	 * @param orderPushmoneyRecord
	 * @return
	 */
	OrderPushmoneyRecord getOfficeIdByUserId(OrderPushmoneyRecord orderPushmoneyRecord);

}
