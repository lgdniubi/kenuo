package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.ReturnedGoodsDao;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.ec.entity.SaleRebatesLog;
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
		List<SaleRebatesLog> list = new ArrayList<SaleRebatesLog>();
		String currentUser = UserUtils.getUser().getName();
		returnedGoods.setAuditBy(currentUser);
		if ("11".equals(returnedGoods.getReturnStatus())) { // 申请退货退款
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);
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
			list = saleRebatesLogDao.selectByOrderId(returnedGoods.getOrderId()); // 插入分销日志
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					SaleRebatesLog saleRebatesLog = new SaleRebatesLog();
					saleRebatesLog.setOrderId(returnedGoods.getOrderId());
					saleRebatesLog.setDepth(list.get(i).getDepth());
					saleRebatesLog.setOrderAmount(-returnedGoods.getReturnAmount());
					saleRebatesLog.setBalancePercent(list.get(i).getBalancePercent());
					saleRebatesLog.setBalanceAmount(-list.get(i).getBalancePercent() * returnedGoods.getReturnAmount());
					saleRebatesLog.setIntegralPercent(list.get(i).getIntegralPercent());
					saleRebatesLog.setIntegralAmount((int) -list.get(i).getIntegralPercent());
					saleRebatesLog.setRebateFlag(0);
					saleRebatesLog.setRabateDate(list.get(i).getRabateDate());
					saleRebatesLog.setDelFlag("-1");
					saleRebatesLogDao.updateSale(saleRebatesLog);
				}
			}
			if (returnedGoods.getIsConfirm() == -10) {
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
				orderGoods.setAfterSaleNum(-returnedGoods.getReturnNum());
				orderGoodsDao.updateIsAfterSales(orderGoods);
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
