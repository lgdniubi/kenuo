package com.training.modules.ec.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.IntegralLogDao;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.dao.OrdersDao;
import com.training.modules.ec.dao.ReturnedGoodsDao;
import com.training.modules.ec.dao.SaleRebatesLogDao;
import com.training.modules.ec.entity.IntegralsLog;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.OrderGoodsDetails;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
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
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private IntegralLogDao integralLogDao;
	
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
		boolean flag = true;
		
		if ("11".equals(returnedGoods.getReturnStatus())) { // 申请退货退款
			
			//不是实物时,当商品同意退货时,使用这个判断.(同意退货会在returned_goods表中插入新的数据,所以需要在插入之前进行查询是否已经售后过)
			if (returnedGoods.getIsConfirm() == 12 && returnedGoods.getIsReal() != 0) {
				//判断商品是否为实物或者虚拟,是则查询商品之前是否有过售后
				if(returnedGoods.getIsReal() ==0 || returnedGoods.getIsReal() ==1){
					//先查询订单的退货成功记录,有记录就不扣减云币
					int num = returnedGoodsDao.getReturnedGoods(returnedGoods);
					flag = num>0;
				}else if(returnedGoods.getIsReal() ==2 || returnedGoods.getIsReal() ==3){
					flag = false;//套卡和通用卡只能有一次售后
				}
			}
			
			//判断是否为虚拟商品;是:直接状态为15退款中,入库状态为"空" -1.
			if(returnedGoods.getIsReal() == 0){
				returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			}else{
				if(returnedGoods.getIsConfirm() == -10){//当拒绝退货时
					returnedGoods.setReturnStatus(-10 + "");
				}else{
					returnedGoods.setReturnStatus(15 + "");
				}
				returnedGoods.setIsStorage(-1 + "");
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
				
				//---------------------------退货处理  detials  begin------------------------------
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
				
				if(returnedGoods.getIsReal() == 2){//套卡需要把剩余金额加回来,返回金额
					//查询details表中AdvanceFlag=4的最新一条记录中SurplusAmount(套卡剩余金额)
					int SurplusAmount = orderGoodsDetailsDao.getSurplusAmount(returnedGoods.getOrderId());
					ogd.setSurplusAmount(-SurplusAmount);
				}
				orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);
				//---------------------------退货处理  detials  end---------------------------
				
				if(returnedGoods.getIsReal() == 2 || returnedGoods.getIsReal() == 3){//套卡和通用卡的子项售后数量
					//在mapping表中都存入了after_sale_num售后数量 ,需要在减去
					List<OrderGoods> oglist = orderGoodsDao.getAfterSaleNumByRid(orderGoods);
					for (OrderGoods og : oglist) {
						og.setAfterSaleNum(-og.getAfterSaleNum());
						orderGoodsDao.updateIsAfterSales(og);
					}
				}
			}
			
			//------------------------------同意退货  扣减云币----begin--------------------------
			if (returnedGoods.getIsConfirm() == 12) {
				//实物可以多次售后,而且是根据购买数量来扣减云币 = 商品云币 * 退货数量
				int integral = orderGoodsDao.getintegralByRecId(returnedGoods.getGoodsMappingId());
				if(integral != 0){
					if(returnedGoods.getIsReal() == 0){
						integral = integral * returnedGoods.getReturnNum();
						updateIntegrals(orders, returnedGoods,integral);
					}else{
						if(!flag){
							//查询mapping表中,云币的数量
							updateIntegrals(orders, returnedGoods,integral);
						}
					}
				}
			}
			//------------------------------同意退货  扣减云币---end---------------------------
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
		
		//当实物和虚拟时,会需要商品售后数量插入mapping表中的after_sale_num.(而通用卡本身也需要,但是售后数量在后面查询)
		OrderGoods orderGoods = new OrderGoods();
		if(orders.getIsReal() == 0 || orders.getIsReal() == 1){
			orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
			orderGoods.setAfterSaleNum(returnedGoods.getReturnNum());
			orderGoodsDao.updateIsAfterSales(orderGoods);
		}
		
		//退货处理 detials begin
		orderGoods = orderGoodsDao.selectOrderGoodsByRecid(orderGoods.getRecid());
		OrderGoodsDetails ogd = new OrderGoodsDetails();
		
		ogd.setOrderId(returnedGoods.getOrderId());
		ogd.setGoodsMappingId(returnedGoods.getGoodsMappingId());
		//判断是否为套卡,存入剩余金额surplusAmount
		if(orders.getIsReal() == 2){
			OrderGoodsDetails ogd1 = orderGoodsDetailsDao.getOrderGoodsDetailSurplusAmountByOid(ogd);
			ogd.setSurplusAmount(-ogd1.getSurplusAmount());
		}
		//查询出通用卡的售后次数(mapping表中的service_times - appt_order表中的预约次数)
		ReturnedGoods commonNum = returnedGoodsDao.getCommonNum(returnedGoods);
		if(orders.getIsReal() == 3){
			ogd.setServiceTimes(-commonNum.getServiceTimes());
		}else{//除通用卡之外,实物和虚拟商品都会存入售后数量,或者次数
			ogd.setServiceTimes(-returnedGoods.getServiceTimes());
		}
		//套卡和通用卡中不存入此字段
		if(orders.getIsReal() == 0 || orders.getIsReal() == 1){
			ogd.setItemAmount(returnedGoods.getServiceTimes()*orderGoods.getSingleRealityPrice());
			ogd.setItemCapitalPool(returnedGoods.getServiceTimes()*orderGoods.getSingleNormPrice());
		}
		ogd.setType(2);
		ogd.setAdvanceFlag("4");
		ogd.setCreateBy(UserUtils.getUser());
		
		orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);
		//退货处理 detials end
		
		//套卡售后  子项商品保存mtmy_returned_goods_card
		if(orders.getIsReal() == 2){
			//通过recid查询售后子项
			List<OrderGoods> og = orderGoodsDao.getOrderGoodsCard(orderGoods);
			//循环卡项子项,把实物售后数量写入
			ReturnedGoods rg = new ReturnedGoods();
			int j = 0 ;
			for (int i = 0; i < og.size(); i++) {
				rg.setReturnedId(id);
				rg.setGoodsMappingId(og.get(i).getRecid()+"");
				rg.setIsReal(og.get(i).getIsreal());
				if(og.get(i).getIsreal() == 0){//当子项是实物时,把售后数量写入
					rg.setReturnNum(returnedGoods.getReturnNums().get(j));
					
					//当实物时,给mapping表中插入after_sale_num
					orderGoods.setRecid(og.get(i).getRecid());
					orderGoods.setAfterSaleNum(returnedGoods.getReturnNums().get(j));
					orderGoodsDao.updateIsAfterSales(orderGoods);
					
					j++;
				}
				rg.setGoodsName(og.get(i).getGoodsname());
				returnedGoodsDao.insertReturnGoodsCard(rg);
			}
			//查询套卡子项虚拟商品的剩余次数(购买次数-预约次数)
			List<ReturnedGoods> returnedGoodsNum = returnedGoodsDao.getReturnNum(returnedGoods);
			for (ReturnedGoods rgn : returnedGoodsNum) {
				rgn.setReturnedId(id);
				//修改套卡子项虚拟商品的剩余次数
				returnedGoodsDao.updateCardNum(rgn);
				
				//当虚拟商品时,给mapping表中插入after_sale_num
				orderGoods.setRecid(Integer.valueOf(rgn.getGoodsMappingId()));
				orderGoods.setAfterSaleNum(rgn.getReturnNum());
				orderGoodsDao.updateIsAfterSales(orderGoods);
			}
			
		}
		//通用卡售后  子项商品保存mtmy_returned_goods_card
		if(orders.getIsReal() == 3){
			//修改售后表中通用卡的售后次数
			commonNum.setId(id);
			returnedGoodsDao.updateCommonNum(commonNum);
			
			//往mapping表中插入通用卡的售后数量
			orderGoods.setRecid(Integer.parseInt(commonNum.getGoodsMappingId()));
			orderGoods.setAfterSaleNum(commonNum.getServiceTimes());
			orderGoodsDao.updateIsAfterSales(orderGoods);
			
			//查询出通用卡的子项,并把实物的售后数量写入   虚拟的为0
			//通过recid查询售后子项
			
			List<OrderGoods> og = orderGoodsDao.getOrderGoodsCard(orderGoods);
			//循环卡项子项,把实物售后数量写入
			ReturnedGoods rg = new ReturnedGoods();
			int j = 0 ;
			for (int i = 0; i < og.size(); i++) {
				rg.setReturnedId(id);
				rg.setGoodsMappingId(og.get(i).getRecid()+"");
				//给mapping插入数据
				orderGoods.setRecid(og.get(i).getRecid());
				
				rg.setIsReal(og.get(i).getIsreal());
				if(og.get(i).getIsreal() == 0){//当子项是实物时,把售后数量写入
					rg.setReturnNum(returnedGoods.getReturnNums().get(j));
					
					//给mapping表中插入after_sale_num
					orderGoods.setAfterSaleNum(returnedGoods.getReturnNums().get(j));
					
					j++;
				}else if(og.get(i).getIsreal() == 1){
					rg.setReturnNum(0);
					//给mapping表中插入after_sale_num
					orderGoods.setAfterSaleNum(0);
				}
				rg.setGoodsName(og.get(i).getGoodsname());
				returnedGoodsDao.insertReturnGoodsCard(rg);
				
				//mapping和returned_goods及其卡项子项表时对应的,所以售后数量一样
				orderGoodsDao.updateIsAfterSales(orderGoods);
			}
		}
	}

	/**
	 * 扣减云币执行方法,并且添加操作记录
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void updateIntegrals(Orders orders,ReturnedGoods returnedGoods ,int integral) {
		//查看缓存中,用户是否存在
		boolean str = redisClientTemplate.exists("mtmy_id_"+returnedGoods.getUserId());
		if(str){
			RedisLock redisLock = new RedisLock(redisClientTemplate, "mtmy_id_"+returnedGoods.getUserId());
			redisLock.lock();
			redisClientTemplate.incrBy("mtmy_id_"+returnedGoods.getUserId(),-integral);
			redisLock.unlock();
		}else{//当用户不存在缓存,直接扣减mtmy_user_accounts表中的云币
			orders.setUserid(returnedGoods.getUserId());
			orders.setUserIntegral(integral);
			ordersDao.updateIntegralAccount(orders);
		}
		//把操作的记录存入mtmy_integrals_log表中
		IntegralsLog integralsLog = new IntegralsLog();
		integralsLog.setUserId(returnedGoods.getUserId());
		integralsLog.setIntegralType(1);
		integralsLog.setIntegralSource(0);
		integralsLog.setActionType(21);
		integralsLog.setIntegral(-integral);
		integralsLog.setOrderId(returnedGoods.getOrderId());
		integralsLog.setRemark("商品赠送(扣减)");
		integralLogDao.insertIntegrals(integralsLog);
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

	/**
	 * 确定商品是否售后    (是:正在售后    或者   已经售后)
	 * @param returnedGoods
	 * @return
	 */
	public boolean getReturnGoodsNum(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getReturnGoodsNum(returnedGoods) >0;
	}

	/**
	 * 查询卡项子项实物的售后数量
	 * @param returnedGoods
	 * @return
	 */
	public List<ReturnedGoods> getRealnum(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getRealnum(returnedGoods);
	}

}
