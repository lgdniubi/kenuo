package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.ReturnedGoodsDao;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.utils.ScopeUtils;

/**
 * 退货订单处理service
 * 
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class ReturnedGoodsService extends CrudService<ReturnedGoodsDao, ReturnedGoods> {

	@Autowired
	private ReturnedGoodsDao returnedGoodsDao;
	@Autowired
	private OrdersDao ordersDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private SaleRebatesLogDao saleRebatesLogDao;
	@Autowired
	private OrderGoodsDetailsDao orderGoodsDetailsDao;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<ReturnedGoods> findReturnList(Page<ReturnedGoods> page, ReturnedGoods returnedGoods) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		returnedGoods.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("a", "orderOrRet"));
		// 设置分页参数
		returnedGoods.setPage(page);
		// 执行分页查询
		page.setList(returnedGoodsDao.findList(returnedGoods));
		return page;
	}

	/**
	 * 查询退货商品照片
	 * 
	 * @param id
	 * @return
	 */
	public List<ReturnedGoodsImages> selectImgById(String id) {
		return returnedGoodsDao.selectImgById(id);
	}

	/**
	 * 审核
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void saveEdite(ReturnedGoods returnedGoods) {
		String currentUser = UserUtils.getUser().getName();
		returnedGoods.setAuditBy(currentUser);
		if ("11".equals(returnedGoods.getReturnStatus())) { // 申请退货退款
			//判断是否为虚拟商品;是:直接状态为15退款中,入库状态为"空" -1.
			if(returnedGoods.getIsReal() == 1){
				if(returnedGoods.getIsConfirm() == -10){//当拒绝退货时
					returnedGoods.setReturnStatus(-10 + "");
				}else{
					returnedGoods.setReturnStatus(15 + "");
				}
				returnedGoods.setIsStorage(-1 + "");
			}else{
				returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			}
			returnedGoodsDao.saveEdite(returnedGoods);//添加退货信息到mtmy_returned_goods表中
			
			Orders orders = ordersDao.get(returnedGoods.getOrderId());
			orders.setOrderid(returnedGoods.getId());
			orders.setParentid(returnedGoods.getOrderId());
			orders.setOrderamount(-returnedGoods.getReturnAmount());
			orders.setOrderstatus(3);
			orders.setOfficeId(returnedGoods.getOfficeId());
			if (null == orders.getChannelFlag() || "null".equals(orders.getChannelFlag())) {
				orders.setChannelFlag("bm");
			}
			ordersDao.insertReturn(orders); // 订单表 插入退货信息
			
			//查询是否有退货记录,并且不是退货
			if(saleRebatesLogDao.selectNumByOrderId(returnedGoods.getOrderId()) == 0 && returnedGoods.getIsConfirm() != -10){//如果无退货记录
				saleRebatesLogDao.updateSale(returnedGoods.getOrderId());// 插入分销日志
			}
			if (returnedGoods.getIsConfirm() == -10) {
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
				orderGoods.setAfterSaleNum(-returnedGoods.getReturnNum());
				orderGoodsDao.updateIsAfterSales(orderGoods);
				
				//退货处理
				orderGoods = orderGoodsDao.selectOrderGoodsByRecid(orderGoods.getRecid());
				//把数据存储到mtmy_order_goods_details表中
				returnedGoods = returnedGoodsDao.get(returnedGoods);//先查询returnedGoods数据
				OrderGoodsDetails ogd = new OrderGoodsDetails();
				ogd.setOrderId(returnedGoods.getOrderId());
				ogd.setGoodsMappingId(returnedGoods.getGoodsMappingId());
				if(returnedGoods.getIsReal() == 1){
					ogd.setItemAmount(returnedGoods.getReturnNum()*orderGoods.getSingleRealityPrice());
					ogd.setItemCapitalPool(returnedGoods.getReturnNum()*orderGoods.getSingleNormPrice());
				}else if(returnedGoods.getIsReal() == 0){
					ogd.setItemAmount(0);
					ogd.setItemCapitalPool(0);	
				}
				ogd.setServiceTimes(returnedGoods.getReturnNum());
				ogd.setType(0);
				ogd.setAdvanceFlag("4");
				ogd.setCreateBy(UserUtils.getUser());
				orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);
			}

		}
		
		if ("21".equals(returnedGoods.getReturnStatus())) { // 申请换货
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);
			if (returnedGoods.getIsConfirm() == -20) {
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
				orderGoods.setAfterSaleNum(-returnedGoods.getReturnNum());
				orderGoodsDao.updateIsAfterSales(orderGoods);
			}
		}
		
	}

	/**
	 * 后台商品退货
	 * 
	 * @param returnedGoods
	 */
	public void saveReturn(ReturnedGoods returnedGoods) {
		Date date = new Date();
		SimpleDateFormat simd = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String str = simd.format(date);
		String id = "01" + str + returnedGoods.getUserId();
		Orders orders = new Orders();
		orders = ordersDao.get(returnedGoods.getOrderId());
		if (orders != null) {
			if (orders.getOfficeId() != null) {
				returnedGoods.setOfficeId(orders.getOfficeId());
			} else {
				returnedGoods.setOfficeId("1000001");
			}

			returnedGoods.setIsReal(orders.getIsReal());
		}

		returnedGoods.setId(id);
		returnedGoods.setApplyDate(date);
		//虚拟商品退货时,由于在returnForm.jsp中,虚拟商品售后次数使用ServiceTimes.
		if(orders.getIsReal() == 1){
			returnedGoods.setReturnNum(returnedGoods.getServiceTimes());
		}
		if (returnedGoods.getApplyType() == 0) {
			returnedGoods.setReturnStatus("11");
		} else if (returnedGoods.getApplyType() == 1) {
			returnedGoods.setReturnStatus("21");
			returnedGoods.setReturnAmount(0);
		}
		returnedGoodsDao.insertReturn(returnedGoods);
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
		orderGoods.setAfterSaleNum(returnedGoods.getReturnNum());
		orderGoodsDao.updateIsAfterSales(orderGoods);
		
		//退货处理
		orderGoods = orderGoodsDao.selectOrderGoodsByRecid(orderGoods.getRecid());

		OrderGoodsDetails ogd = new OrderGoodsDetails();
		ogd.setOrderId(returnedGoods.getOrderId());
		ogd.setGoodsMappingId(returnedGoods.getGoodsMappingId());
		ogd.setItemAmount(returnedGoods.getServiceTimes()*orderGoods.getSingleRealityPrice());
		ogd.setItemCapitalPool(returnedGoods.getServiceTimes()*orderGoods.getSingleNormPrice());
		ogd.setServiceTimes(-returnedGoods.getServiceTimes());
		ogd.setType(2);
		ogd.setAdvanceFlag("4");
		ogd.setCreateBy(UserUtils.getUser());
		orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);;
		
	}

	/**
	 * 强制取消
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public int insertForcedCancel(ReturnedGoods returnedGoods) {
		int num = returnedGoodsDao.insertForcedCancel(returnedGoods);
		// orderGoodsDao.updateIsAfterSales(returnedGoods.getGoodsMappingId());
		return num;
	}

	/**
	 * 确认入库
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public int confirmTake(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.confirmTake(returnedGoods);
	}

	/**
	 * 重新发货
	 * 
	 * @return
	 */
	public int UpdateShipping(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.UpdateShipping(returnedGoods);
	}

	/**
	 * 确认退款
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public int updateReturnMomeny(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.updateReturnMomeny(returnedGoods);
	}

	/**
	 * 申请退款
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public int confirmApply(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.confirmApply(returnedGoods);
	}

	/**
	 * 重新发货
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public int againSendApply(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.againSendApply(returnedGoods);
	}
	
	/**
	 * 根据用户Id查找记录 
	 * @param returnedGoods
	 * @return
	 */
	public Page<ReturnedGoods> findListByUser(Page<ReturnedGoods> page, ReturnedGoods returnedGoods) {
		
		// 设置分页参数
		returnedGoods.setPage(page);
		// 执行分页查询
		page.setList(returnedGoodsDao.findListByUser(returnedGoods));
		return page;
	}

}
