package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsDetailSum;
import com.training.modules.ec.entity.OfficeAccount;
import com.training.modules.ec.entity.OfficeAccountLog;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;
/**
 * 订单商品详情记录表
 * @author dalong
 *
 */

@MyBatisDao
public interface OrderGoodsDetailsDao extends TreeDao<OrderGoodsDetails> {

	/**
	 * 保存订单商品详情记录
	 * @param details
	 */
	void saveOrderGoodsDetails(OrderGoodsDetails details);

	/**
	 * 通过主订单id查询订单的计费信息
	 * @param orderid
	 * @return
	 */
	Orders getOrderGoodsDetailListByOid(@Param("orderid")String orderid);

	/**
	 * 通过子订单id查询计费信息
	 * @param i
	 * @return
	 */
	OrderGoods getOrderGoodsDetailListByMid(@Param("mid")int i);
	/**
	 * 查看商品订单充值记录
	 * @param orderId 
	 * @param orders
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	List<OrderGoodsDetails> getMappinfOrderView(@Param("recid")Integer recid);
	
	/**
	 * 计算欠款
	 * @param recId
	 * @return
	 */
	public  GoodsDetailSum selectDetaiSum(String recId);
	
	/**
	 * 处理预约金
	 * @param recId
	 */
	public void updateAdvanceFlag(@Param(value="recId")String recId);
	
	/**
	 * 根据office_id查找其对应的登云账户的金额
	 */
	public double selectByOfficeId(String officeId);
	
	/**
	 * 根据office_id对登云美业或者店铺的登云账户进行更新
	 */
	public void updateByOfficeId(@Param(value="amount")double amount, @Param(value="officeId")String officeId);
	
	
	/**
	 * 根据office_id对登云美业或者店铺的登云账户进行插入 
	 */
	public void insertByOfficeId(OfficeAccount officeAccount);
	
	/**
	 * 判断店铺的登云账户是否存在
	 */
	public int selectShopByOfficeId(String officeId);
	
	/**
	 * 查询用户购买商品预约的店铺id
	 */
	public String selectShopId(String goodsMappingId);
	
	/**
	 * 当对登云账户进行操作时，插入log日志 
	 */
	public void insertOfficeAccountLog(OfficeAccountLog officeAccountLog);
	
	/**
	 * 查询预约状态
	 * @param recId
	 * @return
	 */
	public int findApptStatus(int recId);
	
	/**
	 * 根据mapping_id获取其订单余款
	 * @param recId
	 * @return
	 */
	public OrderGoodsDetails selectOrderBalance(int recId);

	/**
	 * 根据订单号查询套卡的剩余金额
	 * @param ogd
	 * @return
	 */
	OrderGoodsDetails getOrderGoodsDetailSurplusAmountByOid(OrderGoodsDetails ogd);
	
	/**
	 * 查询details表中AdvanceFlag=4的最新一条记录中SurplusAmount(套卡剩余金额)
	 * @param orderId
	 * @return
	 */
	public int getSurplusAmount(String orderId);
	
	/**
	 * 查询details表中预约金那条的记录
	 * @param orderId
	 * @return
	 */
	public OrderGoodsDetails selectDetailsForAdvance(String orderId);
}
