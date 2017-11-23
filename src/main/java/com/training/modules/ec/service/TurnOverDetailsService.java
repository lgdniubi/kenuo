package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.TurnOverDetailsDao;
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.TurnOverDetails;

/**
 * 营业额明细表Service
 * @author xiaoye  2017年11月16日
 *
 */
@Service
@Transactional(readOnly = false)
public class TurnOverDetailsService extends CrudService<TurnOverDetailsDao, TurnOverDetails>{
	
	@Autowired
	private TurnOverDetailsDao turnOverDetailsDao;
	
	/**
	 * 同步details表的数据到营业额明细表中
	 * @param turnOverDetails
	 */
	public void saveTurnOverDetails(TurnOverDetails turnOverDetails){
		turnOverDetailsDao.saveTurnOverDetails(turnOverDetails);
	}
	
	/**
	 * 根据订单id查询该订单的店营业额
	 * @return
	 */
	public List<TurnOverDetails> selectDetailsByOrderId(String orderId){
		return turnOverDetailsDao.selectDetailsByOrderId(orderId);
	}
	
	/**
	 * 保存店营业额中处理预约金的那条记录的归属店铺
	 * @param belongOfficeId
	 * @param turnOverDetailsId
	 */
	public void updateBelongOffice(String belongOfficeId,int turnOverDetailsId,String settleBy){
		turnOverDetailsDao.updateBelongOffice(belongOfficeId, turnOverDetailsId,settleBy);
	}
	
	/**
	 * 根据店营业额详情表查询业务员营业额详情
	 * @return
	 */
	public List<TurnOverDetails> selectPushDetails(String orderId){
		return turnOverDetailsDao.selectPushDetails(orderId);
	}
	
	/**
	 * 查询单个店营业额对应的业务员营业额详情
	 * @return
	 */
	public TurnOverDetails selectOneDetails(int turnOverDetailsId){
		return turnOverDetailsDao.selectOneDetails(turnOverDetailsId);
	}
	
	/**
	 * 保存业务员提成营业额
	 * @param orderPushmoneyRecord
	 */
	public void savePushMoneyRecord(List<OrderPushmoneyRecord> list){
		turnOverDetailsDao.savePushMoneyRecord(list);
	}
	
	/**
	 * 查看业务员营业额明细 
	 * @param turnOverDetailsId
	 * @return
	 */
	public Page<OrderPushmoneyRecord> queryDetailsForPush(Page<OrderPushmoneyRecord> page,OrderPushmoneyRecord orderPushmoneyRecord){
		// 设置分页参数
		orderPushmoneyRecord.setPage(page);
		// 执行分页查询
		page.setList(turnOverDetailsDao.queryDetailsForPush(orderPushmoneyRecord));
		return page;
	}
}
