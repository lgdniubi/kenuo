package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.entity.OfficeAccount;
import com.training.modules.ec.entity.OfficeAccountLog;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnedGoods;

/**
 * 订单商品详情记录表service
 * 
 * @author dalong
 *
 */

@Service
@Transactional(readOnly = false)
public class OrderGoodsDetailsService extends TreeService<OrderGoodsDetailsDao, OrderGoodsDetails> {

	/**
	 * 保存订单商品详情记录
	 * @param details
	 */
	public void saveOrderGoodsDetails(OrderGoodsDetails details) {
		dao.saveOrderGoodsDetails(details);
	}
	/**
	 * 通过主订单id查询订单的计费信息
	 * @param orderid
	 * @return
	 */
	public Orders getOrderGoodsDetailListByOid(String orderid) {
		return dao.getOrderGoodsDetailListByOid(orderid);
	}
	
	/**
	 * 通过子订单id查询计费信息
	 * @param i
	 * @return
	 */
	public OrderGoods getOrderGoodsDetailListByMid(int i) {
		return dao.getOrderGoodsDetailListByMid(i);
	}

	/**
	 * 查看商品订单充值记录
	 * @param orderId 
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	public List<OrderGoodsDetails> getMappinfOrderView(Integer recid) {
		return dao.getMappinfOrderView(recid);
	}
	
	/**
	 * 处理预约金
	 * @param recId
	 */
	public void updateAdvanceFlag(String recId){
		dao.updateAdvanceFlag(recId);
	}
	
	/**
	 * 根据office_id查找其对应的登云账户的金额
	 */
	public double selectByOfficeId(String officeId){
		return dao.selectByOfficeId(officeId);
	}
	
	/**
	 * 根据office_id对登云美业或者店铺的登云账户进行更新
	 */
	public void updateByOfficeId(double amount, String officeId){
		dao.updateByOfficeId(amount,officeId);
	}
	
	
	/**
	 * 根据office_id对登云美业或者店铺的登云账户进行插入 
	 */
	public void insertByOfficeId(OfficeAccount officeAccount){
		dao.insertByOfficeId(officeAccount);
	}
	
	/**
	 * 判断店铺的登云账户是否存在
	 */
	public int selectShopByOfficeId(String officeId){
		return dao.selectShopByOfficeId(officeId);
	}
	
	/**
	 * 查询用户购买商品预约的店铺id
	 */
	public String selectShopId(String goodsMappingId){
		return dao.selectShopId(goodsMappingId);
	}
	
	/**
	 * 当对登云账户进行操作时，插入log日志 
	 */
	public void insertOfficeAccountLog(OfficeAccountLog officeAccountLog){
		dao.insertOfficeAccountLog(officeAccountLog);
	}
	
	/**
	 * 查询预约状态
	 * @param recId
	 * @return
	 */
	public int findApptStatus(int recId){
		return dao.findApptStatus(recId);
	}
	
	/**
	 * 根据mapping_id获取其订单余款
	 * @param recId
	 * @return
	 */
	public OrderGoodsDetails selectOrderBalance(int recId){
		return dao.selectOrderBalance(recId);
	}
	
	/**
	 * 根据订单id查询下单时候的总的app实付
	 * @param orderId
	 * @return
	 */
	public double queryAppSum(String orderId){
		return dao.queryAppSum(orderId);
	}
	/**
	 * 查询审核需要的条件,判断有无'平欠款'记录
	 * @param returnedGoods
	 * @return
	 */
	public OrderGoodsDetails getCountArrearage(ReturnedGoods returnedGoods) {
		return dao.getCountArrearage(returnedGoods);
	}
}
