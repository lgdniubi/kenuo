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
import com.training.modules.ec.entity.OrderPushmoneyRecord;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.entity.ReturnedGoodsImages;
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.entity.User;
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
	 * 实物审核
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void saveReturnedKind(ReturnedGoods returnedGoods) {
		String currentUser = UserUtils.getUser().getId();
		returnedGoods.setAuditBy(currentUser);
		if (returnedGoods.getIsConfirm() == 12 || returnedGoods.getIsConfirm() == -10) { // 申请退货退款
			//申请类型只会是退货并退款或者仅退款-->仅退款:售后状态为15
			if(returnedGoods.getApplyType() == 2 && returnedGoods.getIsConfirm() == 12){//同意审核且仅退款-->售后状态直接变成15
				returnedGoods.setReturnStatus("15");//改变售后状态
			}else{
				returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");//改变售后状态
			}
			returnedGoodsDao.saveEdite(returnedGoods);//添加退货信息到mtmy_returned_goods表中
			
			//所有商品全部售后审核通过,订单状态改为'已完成',将预约金状态默认平掉.注意一个订单多个商品
			if(returnedGoods.getIsConfirm() == 12){//同意退货
				boolean flag = true;
				List<OrderGoods> orderGoodsList = orderGoodsDao.getOrderNum(returnedGoods);//获取该订单中商品总购买数量
				if(orderGoodsList.size()>0){//确定存在购买的商品
					for (OrderGoods orderGoods : orderGoodsList) {//循环集合,查询每个商品的实际售后数量(如果全部售后)
						int returnNum = returnedGoodsDao.getOrderGoodsReturnNum(orderGoods);//获取该订单中单个实物商品总售后数量
						if(orderGoods.getGoodsnum() != returnNum){//数量相同,说明全部商品售后通过
							flag = false;//没有完全售后
							break;
						}
					}
				}
				//订单中的商品已经全部售后;
				if(flag){
					//实物订单状态'待发货'和'待收货'修改为'已完成'状态(同意售后审核)
					ordersDao.editOrderstatusForfinish(returnedGoods.getOrderId());
					
					//平预约金状态
					for (OrderGoods orderGoods : orderGoodsList) {
						returnedGoods.setOrderId(orderGoods.getOrderid());
						returnedGoods.setGoodsMappingId(orderGoods.getRecid()+"");
						orderGoodsDetailsDao.editAdvanceFlag(returnedGoods);
					}
				}
				
				//审核同意,平欠款(出现的情况:申请时,仅换货(此状态不平欠款和不处理预约金),而在审核时,更改为退货并退款或者仅退款,需要平欠款记录)
				if(returnedGoods.getApplyType() != 1){
					applyArrearage(returnedGoods);//平欠款方法
				}
			}
			
			//------审核之后,售后数量和售后金额可能会修改--begin-------
			//修改mapping表中商品本身的次数
			int afterSaleNum = returnedGoods.getReturnNum()-returnedGoods.getOldReturnNum();
			if(afterSaleNum != 0){
				OrderGoods og = new OrderGoods();
				og.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
				og.setAfterSaleNum(afterSaleNum);
				orderGoodsDao.updateIsAfterSales(og);
			}
			//------审核之后,售后数量和售后金额可能会修改--end-------
			
			//-------------订单表 插入退货信息-------begin---------------
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
			//-------------订单表 插入退货信息-------end---------------
			
			//------------插入分销日志--------begin---------------
			//查询是否有退货记录,并且不是退货
			if(saleRebatesLogDao.selectNumByOrderId(returnedGoods.getOrderId()) == 0 && returnedGoods.getIsConfirm() != -10){//如果无退货记录
				saleRebatesLogDao.updateSale(returnedGoods.getOrderId());// 插入分销日志
			}
			//------------插入分销日志--------end---------------
			
			//------------拒绝售后(调用统一方法)--------begin---------------
			if (returnedGoods.getIsConfirm() == -10) {//拒绝退货
				refuseService(returnedGoods);//取消 平欠款和平预约金记录
			}
			//------------拒绝售后(调用统一方法)--------end---------------
			
			//------------------------------同意退货  扣减云币----begin--------------------------
			if (returnedGoods.getIsConfirm() == 12) {
				//实物可以多次售后,而且是根据购买数量来扣减云币 = 商品云币 * 退货数量
				int integral = orderGoodsDao.getintegralByRecId(returnedGoods.getGoodsMappingId());
				if(integral != 0){
					integral = integral * returnedGoods.getReturnNum();
					updateIntegrals(orders, returnedGoods,integral);
				}
			}
			//------------------------------同意退货  扣减云币---end---------------------------
		}
		
		//---------------申请换货---begin---------------------------
		if (returnedGoods.getIsConfirm() == 22 || returnedGoods.getIsConfirm() == -20) { // 申请换货
			
			cancelArrearage(returnedGoods);//取消平预约金和平欠款记录(申请类型是仅换货(请查看方法具体说明))
			
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);//修改换货状态
			
			if(returnedGoods.getIsConfirm() == -20){//拒绝换货,直接退还数量(实物换货,只需要扣减mapping表中的几率.detail表中不需要)
				refuseService(returnedGoods);//退还申请扣减的数据(detail表和mapping表)
			}
			
		}
		//---------------申请换货---end---------------------------
	}
	/**
	 * 虚拟审核
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void saveReturned(ReturnedGoods returnedGoods) {
		String currentUser = UserUtils.getUser().getId();
		returnedGoods.setAuditBy(currentUser);
		boolean flag = true;
		
		if (returnedGoods.getIsConfirm() == 12 || returnedGoods.getIsConfirm() == -10) { // 申请退货退款
			
			//虚拟商品同意退货时,扣减云币(只有第一次售后扣减).(同意退货会在returned_goods表中插入新的数据,所以需要在插入之前进行查询是否已经售后过)
			if (returnedGoods.getIsConfirm() == 12) {
				//虚拟商品之前是否有过售后
				//先查询订单的退货成功记录,有记录就不扣减云币
				flag = returnedGoodsDao.getReturnedGoods(returnedGoods)>0;
				
				//审核同意,平欠款(出现的情况:申请时,仅换货(此状态不平欠款和不处理预约金),而在审核时,更改为退货并退款或者仅退款,需要平欠款记录)
				if(returnedGoods.getApplyType() != 1){
					applyArrearage(returnedGoods);//平欠款方法
				}
			}
			
			//------审核之后,售后次数和售后金额可能会修改--begin-------
			//修改mapping表中商品本身的次数
			int afterSaleNum = returnedGoods.getReturnNum()-returnedGoods.getOldReturnNum();
			if(afterSaleNum != 0){
				//修改detail表中的扣减的次数
				orderGoodsDetailsDao.updateDetailServiceTimes(returnedGoods);//修改detail表中的扣减的次数
				OrderGoods og = new OrderGoods();
				og.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
				og.setAfterSaleNum(afterSaleNum);
				orderGoodsDao.updateIsAfterSales(og);
			}
			//------审核之后,售后次数和售后金额可能会修改--end-------
			
			//----------- 售后表中插入售后信息     begin---------
			//申请类型只会是退货并退款或者仅退款-->仅退款:售后状态为15
			if(returnedGoods.getApplyType() == 2 && returnedGoods.getIsConfirm() == 12){//同意审核且仅退款-->售后状态直接变成15
				returnedGoods.setReturnStatus("15");//改变售后状态
				returnedGoods.setIsStorage(0 + "");
			}else{
				returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");//改变售后状态
			}
			returnedGoodsDao.saveEdite(returnedGoods);//添加退货信息到mtmy_returned_goods表中
			//----------- 售后表中插入售后信息     end---------
			
			//-----------订单表 插入退货信息     begin---------
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
			//-----------订单表 插入退货信息     end---------
			
			//-----------订单表 插入退货信息     begin---------
			//查询是否有退货记录,并且不是退货
			if(saleRebatesLogDao.selectNumByOrderId(returnedGoods.getOrderId()) == 0 && returnedGoods.getIsConfirm() != -10){//如果无退货记录
				saleRebatesLogDao.updateSale(returnedGoods.getOrderId());// 插入分销日志
			}
			//-----------插入分销日志     end---------
			
			//------------拒绝售后(调用统一方法)--------begin---------------
			if (returnedGoods.getIsConfirm() == -10) {//拒绝退货
				refuseService(returnedGoods);//取消 平欠款和平预约金记录
			}
			//------------拒绝售后(调用统一方法)--------end---------------
			
			//---------------同意退货  扣减云币----begin--------------------------
			if (returnedGoods.getIsConfirm() == 12) {
				//实物可以多次售后,而且是根据购买数量来扣减云币 = 商品云币 * 退货数量
				int integral = orderGoodsDao.getintegralByRecId(returnedGoods.getGoodsMappingId());
				if(integral != 0){
					if(!flag){
						//查询mapping表中,云币的数量
						updateIntegrals(orders, returnedGoods,integral);
					}
				}
			}
			//---------------同意退货  扣减云币---end---------------------------
		}
		
		//---------------申请换货---begin---------------------------
		if (returnedGoods.getIsConfirm() == 22 || returnedGoods.getIsConfirm() == -20) { // 申请换货
			
			cancelArrearage(returnedGoods);//取消平预约金和平欠款记录(申请类型是仅换货(请查看方法具体说明))
			
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);//修改换货状态
			
			if(returnedGoods.getIsConfirm() == -20){//拒绝换货,直接退还数量
				refuseService(returnedGoods);//退还申请扣减的数据(detail表和mapping表)
			}
		}
		//---------------申请换货---end---------------------------
	}
	/**
	 * 套卡审核
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void saveEditeSuit(ReturnedGoods returnedGoods) {
		String currentUser = UserUtils.getUser().getId();
		returnedGoods.setAuditBy(currentUser);
		
		if (returnedGoods.getIsConfirm() == 12 || returnedGoods.getIsConfirm() == -10) { // 申请退货退款
			
			//申请类型只会是退货并退款或者仅退款-->仅退款:售后状态为15
			if(returnedGoods.getIsConfirm() == -10){//当拒绝退货时
				returnedGoods.setReturnStatus(-10 + "");
			}else{
				if(returnedGoods.getApplyType() == 2){//审核同意后,仅退款:售后状态:退款中,仓库状态:未入库
					returnedGoods.setReturnStatus(15 + "");
					returnedGoods.setIsStorage(0 + "");
				}else{
					returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
				}
				
				//审核同意,平欠款(出现的情况:申请时,仅换货(此状态不平欠款和不处理预约金),而在审核时,更改为退货并退款或者仅退款,需要平欠款记录)
				if(returnedGoods.getApplyType() != 1){
					applyArrearage(returnedGoods);//平欠款方法
				}
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
				
				refuseService(returnedGoods);//取消 平欠款和平预约金记录
				
				//审核拒绝,退还套卡子项的商品数量(只插入不做删除)
				List<ReturnedGoods> oglist = returnedGoodsDao.getSuitCard(returnedGoods);
				OrderGoods orderGoods = new OrderGoods();
				for (ReturnedGoods rg : oglist) {
					//拒绝:减去mapping表中扣减的数量
					orderGoods.setRecid(Integer.parseInt(rg.getGoodsMappingId()));
					orderGoods.setAfterSaleNum(-rg.getReturnNum());
					orderGoodsDao.updateIsAfterSales(orderGoods);//在mapping表中都存入了after_sale_num售后数量 ,需要在减去
					
					//拒绝:退还套卡的子项记录
					rg.setReturnNum(-rg.getReturnNum());
					returnedGoodsDao.insertReturnGoodsCard(rg);
				}
			}
			
			//------------------------------同意退货  扣减云币----begin--------------------------
			if (returnedGoods.getIsConfirm() == 12) {
				//实物可以多次售后,而且是根据购买数量来扣减云币 = 商品云币 * 退货数量
				int integral = orderGoodsDao.getintegralByRecId(returnedGoods.getGoodsMappingId());
				if(integral != 0){
					//查询mapping表中,云币的数量
					updateIntegrals(orders, returnedGoods,integral);
				}
			}
			//------------------------------同意退货  扣减云币---end---------------------------
		}
		
		if (returnedGoods.getIsConfirm() == 22 || returnedGoods.getIsConfirm() == -20) { // 申请换货
			
			cancelArrearage(returnedGoods);//取消平预约金和平欠款记录(申请类型是仅换货(请查看方法具体说明))
			
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);//修改换货状态
			
			if(returnedGoods.getIsConfirm() == -20){//拒绝换货,直接退还数量
				refuseService(returnedGoods);//退还申请扣减的数据(detail表和mapping表)
			}
		}
		
	}
	/**
	 * 通用卡审核
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void saveReturnedCommon(ReturnedGoods returnedGoods) {
		String currentUser = UserUtils.getUser().getId();
		returnedGoods.setAuditBy(currentUser);
		boolean flag = true;
		
		if (returnedGoods.getIsConfirm() == 12 || returnedGoods.getIsConfirm() == -10) { // 申请退货退款
			//-----------修改售后表中的状态-----begin----------------------------------------------
			//不是实物时,当商品同意退货时,使用这个判断.(同意退货会在returned_goods表中插入新的数据,所以需要在插入之前进行查询是否已经售后过)
			if (returnedGoods.getIsConfirm() == 12) {
				//判断商品是否为实物或者虚拟,是则查询商品之前是否有过售后
				//先查询订单的退货成功记录,有记录就不扣减云币
				flag = returnedGoodsDao.getReturnedGoods(returnedGoods)>0;
				
				//审核同意,平欠款(出现的情况:申请时,仅换货(此状态不平欠款和不处理预约金),而在审核时,更改为退货并退款或者仅退款,需要平欠款记录)
				if(returnedGoods.getApplyType() != 1){
					applyArrearage(returnedGoods);//平欠款方法
				}
			}
			//售后次数和售后金额可能会修改(实物子项数量也可能修改.)
			//修改mapping表中通用卡本身的次数
			int afterSaleNum = returnedGoods.getReturnNum()-returnedGoods.getOldReturnNum();
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
			if(afterSaleNum != 0){
				//修改detail表中的扣减的次数
				orderGoodsDetailsDao.updateDetailServiceTimes(returnedGoods);
				orderGoods.setAfterSaleNum(afterSaleNum);
				orderGoodsDao.updateIsAfterSales(orderGoods);
			}
			//实物子项数量也可能修改.
			List<OrderGoods> ogList = orderGoodsDao.getOrderGoodsCard(orderGoods);
			//循环卡项子项,把实物售后数量写入
			ReturnedGoods rg = new ReturnedGoods();
			int j = 0 ;
			for (int i = 0; i < ogList.size(); i++) {
				if(ogList.get(i).getIsreal() == 0){//子项为实物,存入mapping表中修改售后数量
					//根据售后id和子项商品id,修改实物数量
					rg.setReturnedId(returnedGoods.getId());
					rg.setGoodsMappingId(returnedGoods.getRecIds().get(j)+"");
					rg.setReturnNum(returnedGoods.getReturnNums().get(j)-returnedGoods.getOldReturnNums().get(j));
					returnedGoodsDao.updateCardNum(rg);//修改子项表中的数据
					//修改mapping表中子项售后数据
					orderGoods.setRecid(returnedGoods.getRecIds().get(j));
					orderGoods.setAfterSaleNum(returnedGoods.getReturnNums().get(j)-returnedGoods.getOldReturnNums().get(j));
					orderGoodsDao.updateIsAfterSales(orderGoods);
					j++;
				}
			}
				
			//申请类型只会是退货并退款或者仅退款-->仅退款:售后状态为15
			if(returnedGoods.getApplyType() == 2 && returnedGoods.getIsConfirm() == 12){//同意审核且仅退款-->售后状态直接变成15
				returnedGoods.setReturnStatus("15");//改变售后状态
				returnedGoods.setIsStorage(0 + "");
			}else{
				returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");//改变售后状态
			}
			returnedGoodsDao.saveEdite(returnedGoods);//添加退货信息到mtmy_returned_goods表中
			//-----------修改售后表中的状态-----e----------------------------------------------
			
			//-----------订单表 插入退货信息-----begin----------------------------------------------
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
			//-----------订单表 插入退货信息-----end----------------------------------------------
			
			//-----------插入分销日志-----begin----------------------------------------------
			//查询是否有退货记录,并且不是退货
			if(saleRebatesLogDao.selectNumByOrderId(returnedGoods.getOrderId()) == 0 && returnedGoods.getIsConfirm() != -10){//如果无退货记录
				saleRebatesLogDao.updateSale(returnedGoods.getOrderId());// 插入分销日志
			}
			//-----------插入分销日志-----end----------------------------------------------
			
			//-----------拒绝售后-----begin----------------------------------------------
			if (returnedGoods.getIsConfirm() == -10) {
				refuseService(returnedGoods);//取消 平欠款和平预约金记录
				//----------通用卡的子项售后数量------begin------------
				//审核拒绝时,子项的售后数量需要返还
				//循环卡项子项,把实物售后数量写入
				int k = 0 ;
				for (int i = 0; i < ogList.size(); i++) {
					rg.setReturnedId(returnedGoods.getId());
					rg.setGoodsMappingId(ogList.get(i).getRecid()+"");
					rg.setIsReal(ogList.get(i).getIsreal());
					if(ogList.get(i).getIsreal() == 0){//当子项是实物时,把售后数量写入
						rg.setReturnNum(-returnedGoods.getReturnNums().get(k));
						//修改mapping表中实物子项售后数据
						orderGoods.setRecid(returnedGoods.getRecIds().get(k));
						orderGoods.setAfterSaleNum(-returnedGoods.getReturnNums().get(k));
						orderGoodsDao.updateIsAfterSales(orderGoods);
						k++;
					}else if(ogList.get(i).getIsreal() == 1){
						rg.setReturnNum(0);
					}
					rg.setGoodsName(ogList.get(i).getGoodsname());
					returnedGoodsDao.insertReturnGoodsCard(rg);
				}
				//----------通用卡的子项售后数量------end------------
			}
			//-----------拒绝售后-----begin----------------------------------------------
			
			//------------------------------同意退货  扣减云币----begin--------------------------
			if (returnedGoods.getIsConfirm() == 12) {
				//实物可以多次售后,而且是根据购买数量来扣减云币 = 商品云币 * 退货数量
				int integral = orderGoodsDao.getintegralByRecId(returnedGoods.getGoodsMappingId());
				if(integral != 0){
					if(!flag){
						//查询mapping表中,云币的数量
						updateIntegrals(orders, returnedGoods,integral);
					}
				}
			}
			//------------------------------同意退货  扣减云币---end---------------------------
		}
		
		if (returnedGoods.getIsConfirm() == 22 || returnedGoods.getIsConfirm() == -20) { // 申请换货
			
			cancelArrearage(returnedGoods);//取消平预约金和平欠款记录(申请类型是仅换货(请查看方法具体说明))
			
			returnedGoods.setReturnStatus(returnedGoods.getIsConfirm() + "");
			returnedGoodsDao.saveEdite(returnedGoods);//修改换货状态
			
			if(returnedGoods.getIsConfirm() == -20){//拒绝换货,直接退还数量
				refuseService(returnedGoods);//退还申请扣减的数据(detail表和mapping表)
			}
		}
	}

	/**
	 * 实物商品--申请售后
	 * 
	 * @param returnedGoods
	 */
	public void saveReturnKind(ReturnedGoods returnedGoods) {
		//------------------售后表插入新数据-----begin-------------------------------
		User user = UserUtils.getUser();//获取当前操作用户
		Date date = new Date();
		SimpleDateFormat simd = new SimpleDateFormat("YYYYMMddHHmmssSSS");
		String str = simd.format(date);
		String id = "01" + str + returnedGoods.getUserId();
		Orders orders = new Orders();
		orders = ordersDao.get(returnedGoods.getOrderId());
		//保存数据到售后表中
		if (orders != null) {
			if (orders.getOfficeId() != null) {
				returnedGoods.setOfficeId(orders.getOfficeId());
			} else {
				returnedGoods.setOfficeId("1000001");
			}

			returnedGoods.setIsReal(orders.getIsReal());
		}
		returnedGoods.setId(id);
		returnedGoods.setApplyDate(date);//添加申请日期
		if (returnedGoods.getApplyType() == 1) {//售后申请类型是:仅换货
			returnedGoods.setReturnStatus("21");
			returnedGoods.setReturnAmount(0);
		} else {//退货并退款或者仅退款
			returnedGoods.setReturnStatus("11");
		}
		returnedGoods.setChannelFlag("bm");
		returnedGoods.setApplyBy(user.getId());//添加申请人(获取当前登录人的信息)
		returnedGoodsDao.insertReturn(returnedGoods);
		//------------------售后表插入新数据-----end-------------------------------
		
		applyReturn(returnedGoods,user);//售后申请,插入mapping表中和detail表中
	}
	/**
	 * 虚拟商品--申请售后
	 * 
	 * @param returnedGoods
	 */
	public void saveReturn(ReturnedGoods returnedGoods) {
		//--------售后表插入新数据-----begin-------------------------------
		User user = UserUtils.getUser();//获取当前操作用户
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
		returnedGoods.setApplyDate(date);//添加申请日期
		if (returnedGoods.getApplyType() == 1) {
			returnedGoods.setReturnStatus("21");
			returnedGoods.setReturnAmount(0);
		} else {
			returnedGoods.setReturnStatus("11");
		}
		returnedGoods.setChannelFlag("bm");
		returnedGoods.setApplyBy(user.getId());//添加申请人(获取当前登录人的信息)
		returnedGoodsDao.insertReturn(returnedGoods);
		//--------售后表插入新数据-----end-------------------------------
		
		applyReturn(returnedGoods,user);//售后申请,插入mapping表中和detail表中
	}
	/**
	 * 套卡商品--申请售后
	 * 
	 * @param returnedGoods
	 */
	public void saveReturnSuit(ReturnedGoods returnedGoods) {
		User user = UserUtils.getUser();//获取当前操作用户
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
		returnedGoods.setApplyDate(date);//添加申请日期
		if (returnedGoods.getApplyType() == 1) {
			returnedGoods.setReturnStatus("21");
			returnedGoods.setReturnAmount(0);
		} else {
			returnedGoods.setReturnStatus("11");
		}
		returnedGoods.setChannelFlag("bm");
		returnedGoods.setApplyBy(user.getId());//添加申请人(获取当前登录人的信息)
		returnedGoodsDao.insertReturn(returnedGoods);
		
		//
		applyReturn(returnedGoods,user);//售后申请,插入mapping表中和detail表中
		
		//套卡售后  子项商品保存mtmy_returned_goods_card
		//通过recid查询售后子项
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
		List<OrderGoods> og = orderGoodsDao.getOrderGoodsCard(orderGoods);
		//循环卡项子项,把实物售后数量写入
		ReturnedGoods rg = new ReturnedGoods();
		for (int i = 0; i < og.size(); i++) {
			if(og.get(i).getIsreal() == 0){
				//当子项是实物时,把售后数量写入
				rg.setReturnedId(id);
				rg.setGoodsMappingId(og.get(i).getRecid()+"");
				rg.setIsReal(og.get(i).getIsreal());
				rg.setReturnNum(og.get(i).getGoodsnum());
				rg.setGoodsName(og.get(i).getGoodsname());
				returnedGoodsDao.insertReturnGoodsCard(rg);
				
				//当实物时,给mapping表中插入after_sale_num
				orderGoods.setRecid(og.get(i).getRecid());
				orderGoods.setAfterSaleNum(og.get(i).getGoodsnum());
				orderGoodsDao.updateIsAfterSales(orderGoods);
			}
		}
		//查询套卡子项虚拟商品的剩余次数(购买次数-预约次数)
		List<ReturnedGoods> returnedGoodsNum = returnedGoodsDao.getReturnNum(returnedGoods);
		for (ReturnedGoods rgn : returnedGoodsNum) {
			rgn.setReturnedId(returnedGoods.getId());
			//修改套卡子项虚拟商品的剩余次数
			rgn.setReturnNum(rgn.getReturnNum());
			returnedGoodsDao.insertReturnGoodsCard(rgn);
			
			//当虚拟商品时,给mapping表中插入after_sale_num
			orderGoods.setRecid(Integer.valueOf(rgn.getGoodsMappingId()));
			orderGoods.setAfterSaleNum(rgn.getReturnNum());
			orderGoodsDao.updateIsAfterSales(orderGoods);
		}
	}
	/**
	 * 通用卡商品--申请售后
	 * 
	 * @param returnedGoods
	 */
	public void saveReturnCommon(ReturnedGoods returnedGoods) {
		//--------售后表插入新记录----begin--------------------------------------------------
		User user = UserUtils.getUser();//获取当前操作用户
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
		returnedGoods.setApplyDate(date);//添加申请日期
		if (returnedGoods.getApplyType() == 1) {
			returnedGoods.setReturnStatus("21");
			returnedGoods.setReturnAmount(0);
		} else {
			returnedGoods.setReturnStatus("11");
		}
		returnedGoods.setChannelFlag("bm");
		returnedGoods.setApplyBy(user.getId());//添加申请人(获取当前登录人的信息)
		returnedGoodsDao.insertReturn(returnedGoods);
		//--------售后表插入新记录----end--------------------------------------------------
		
		applyReturn(returnedGoods,user);//售后申请,插入mapping表中和detail表中
		
		//--------查询出通用卡的子项,并把实物的售后数量写入   虚拟的为0-----------------
		//通过recid查询售后子项
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
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
		//--------查询出通用卡的子项,并把实物的售后数量写入   虚拟的为0-------------------
	}

	/**
	 * 申请售后,执行的统一方法方法(mapping表插入售后数量和detail表插入售后记录)(平欠款和平预约金)
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void applyReturn(ReturnedGoods returnedGoods, User user) {
		//------------------修改mapping表中售后商品数量-----begin-------------------------------
		//当实物和虚拟时,会需要商品售后数量插入mapping表中的after_sale_num.(而通用卡本身也需要,但是售后数量在后面查询)
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
		orderGoods.setAfterSaleNum(returnedGoods.getReturnNum());
		orderGoodsDao.updateIsAfterSales(orderGoods);
		//------------------修改mapping表中售后商品数量-----end-------------------------------
		
		//------------------退货处理 detials-----begin-------------------------------
		//------------------平欠款记录 detials-----begin-------------------------------
		//判断申请类型不是仅换货且存在欠款,需要平欠款,存入detail表中
		OrderGoodsDetails ogd1 = orderGoodsDetailsDao.getArrearageByOrderId(returnedGoods);//根据订单id获取订单余额,订单欠款和app欠款金额
		if(returnedGoods.getApplyType() != 1 && ogd1.getOrderArrearage() > 0){
			ogd1.setOrderBalance(-ogd1.getOrderBalance());//平欠款,扣除订单余款
			ogd1.setOrderArrearage(-ogd1.getOrderArrearage());//平欠款,扣除订单欠款
			ogd1.setAppArrearage(-ogd1.getAppArrearage());//平欠款,扣除app欠款金额
			ogd1.setType(3);
			ogd1.setAdvanceFlag("5");
			ogd1.setCreateOfficeId(user.getOffice().getId());
			ogd1.setCreateBy(user);
			orderGoodsDetailsDao.saveOrderGoodsDetails(ogd1);
		}
		//------------------平欠款记录 detials-----end-------------------------------
		
		//------------------申请退货记录 detials-----begin-------------------------------
		if(returnedGoods.getIsReal() != 0){//实物不需要存入detail表中,虚拟和通用卡需要
			OrderGoodsDetails ogd = new OrderGoodsDetails();
			//退货商品信息存入detials中
			ogd.setOrderId(returnedGoods.getOrderId());
			ogd.setGoodsMappingId(returnedGoods.getGoodsMappingId());
			ogd.setReturnedId(returnedGoods.getId());
			ogd.setServiceTimes(-returnedGoods.getReturnNum());
			if(returnedGoods.getIsReal() == 2){//套卡需要存入'套卡剩余金额'
				//判断是否为套卡,存入剩余金额surplusAmount
				double surplusAmount = orderGoodsDetailsDao.getOrderGoodsDetailSurplusAmountByOid(ogd);
				ogd.setSurplusAmount(-surplusAmount);
			}
			ogd.setType(2);
			ogd.setAdvanceFlag("4");
			ogd.setCreateOfficeId(user.getOffice().getId());
			ogd.setCreateBy(user);
			orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);
		}
		//------------------申请退货记录 detials-----end-------------------------------
		//------------------退货处理 detials-----end-------------------------------
	}
	/**
	 * 审核拒绝售后,执行的统一方法
	 * 
	 * @param returnedGoods
	 * @return
	 */
	public void refuseService(ReturnedGoods returnedGoods) {
		//退还mapping表中的售后商品数量
		OrderGoods orderGoods = new OrderGoods();
		orderGoods.setRecid(Integer.parseInt(returnedGoods.getGoodsMappingId()));
		orderGoods.setAfterSaleNum(-returnedGoods.getReturnNum());
		orderGoodsDao.updateIsAfterSales(orderGoods);
		//---------------------------取消 平欠款和平预约金记录 begin------------------------------
		//先查询商品是否存在申请售后记录或者已同意的记录
		if(returnedGoodsDao.getReturnSaleNum(returnedGoods) == 0){
			//查询'平预约金'和'平欠款'记录
			int countAdvanceFlag = orderGoodsDetailsDao.getCountAdvanceFlag(returnedGoods);//查询'平预约金'记录
			if(countAdvanceFlag>0){
				orderGoodsDetailsDao.cancelAdvanceFlag(returnedGoods);//取消'平预约金'记录
			}
			int countArrearage = orderGoodsDetailsDao.getCountArrearage(returnedGoods);//查询审核需要的条件,判断无'平欠款'记录
			if(countArrearage > 0){//平欠款是-欠款.小于0
				orderGoodsDetailsDao.deleteArrearage(returnedGoods);//删除'平欠款'记录
			}
		}
		//---------------------------取消 平欠款和平预约金记录 end------------------------------
		
		//---------------------------退货处理  detials  begin------------------------------
		//把数据存储到mtmy_order_goods_details表中
		if(returnedGoods.getIsReal() != 0){//实物不需要存入detail表字段,但是虚拟和通用卡需要存入次数.
			returnedGoods = returnedGoodsDao.get(returnedGoods);//先查询returnedGoods数据
			OrderGoodsDetails ogd = new OrderGoodsDetails();
			ogd.setOrderId(returnedGoods.getOrderId());
			ogd.setGoodsMappingId(returnedGoods.getGoodsMappingId());
			if(returnedGoods.getIsReal() == 2 && !"bm".equals(returnedGoods.getChannelFlag())){
				ogd.setServiceTimes(1);
			}else{
				ogd.setServiceTimes(returnedGoods.getReturnNum());			
			}
			ogd.setType(2);
			ogd.setAdvanceFlag("4");
			ogd.setCreateOfficeId(UserUtils.getUser().getOffice().getId());
			ogd.setCreateBy(UserUtils.getUser());
			if(returnedGoods.getIsReal() == 2){//拒绝套卡售后,退还'套卡剩余金额'
				//查询details表中AdvanceFlag=4的最新一条记录中SurplusAmount(套卡剩余金额)
				double SurplusAmount = orderGoodsDetailsDao.getSurplusAmount(returnedGoods.getOrderId());
				ogd.setSurplusAmount(-SurplusAmount);
			}
			orderGoodsDetailsDao.saveOrderGoodsDetails(ogd);
		}
		//---------------------------退货处理  detials  end---------------------------
	}
	
	/**
	 * 平预约金和平欠款的方法
	 * 审核同意后,没有平欠款和平预约金记录
	 * (前端先申请换货,在审核时更改为退货并退款或者仅退款时,如果存在欠款和预约金未处理,审核同意时需要平欠款和平预约记录)
	 * @param returnedGoods
	 * @return
	 */
	public void applyArrearage(ReturnedGoods returnedGoods) {
		User user = UserUtils.getUser();//获取当前操作用户
		if(returnedGoods.getIsReal() != 0){//除实物之外的商品都需要处理预约金(实物不用处理预约金)
			//平预约金方法
			int countAdvanceFlag = orderGoodsDetailsDao.getCountAdvanceFlag(returnedGoods);//查询是否存在平预约金记录
			if(countAdvanceFlag == 0){//没有平预约金记录
				orderGoodsDetailsDao.editAdvanceFlag(returnedGoods);//平预约金方法
			}
		}
		//平欠款方法(1.先确认是否存在'平欠款记录'. 2.执行平欠款)
		int countArrearage = orderGoodsDetailsDao.getCountArrearage(returnedGoods);//查询审核需要的条件,判断无'平欠款'记录
		if(countArrearage == 0){//没有平欠款记录
			OrderGoodsDetails ogd1 = orderGoodsDetailsDao.getArrearageByOrderId(returnedGoods);//根据订单id获取订单余额,订单欠款和app欠款金额
			if(returnedGoods.getApplyType() != 1 && ogd1.getOrderArrearage() > 0){//存在欠款和存在预约金
				ogd1.setOrderBalance(-ogd1.getOrderBalance());//平欠款,扣除订单余款
				ogd1.setOrderArrearage(-ogd1.getOrderArrearage());//平欠款,扣除订单欠款
				ogd1.setAppArrearage(-ogd1.getAppArrearage());//平欠款,扣除app欠款金额
				ogd1.setType(3);
				ogd1.setAdvanceFlag("5");
				ogd1.setCreateOfficeId(user.getOffice().getId());
				ogd1.setCreateBy(user);
				orderGoodsDetailsDao.saveOrderGoodsDetails(ogd1);
			}
		}
	}
	/**
	 * 取消平预约金和取消平欠款的方法
	 * 考虑一下存在多个售后或者只有一次售后
	 * (前端先申请退货并退款(仅退款),在审核时更改为仅换货时,如果存在平欠款和平预约金记录,审核同意时需要取消平欠款和取消平预约记录)
	 * @param returnedGoods
	 * @return
	 */
	public void cancelArrearage(ReturnedGoods returnedGoods) {
		//先查询商品是否存在申请售后记录或者已同意的记录(除当前售后订单外的记录)
		if(returnedGoodsDao.getReturnSaleNum(returnedGoods) == 0){//查询是否除本身之外的售后记录,没有:查看是否有平欠款和平预约金记录,有就取消记录.  存在:不需要取消记录
			//查询'平预约金'和'平欠款'记录
			int countAdvanceFlag = orderGoodsDetailsDao.getCountAdvanceFlag(returnedGoods);//查询'平预约金'记录
			if(countAdvanceFlag>0){
				orderGoodsDetailsDao.cancelAdvanceFlag(returnedGoods);//取消'平预约金'记录
			}
			int countArrearage = orderGoodsDetailsDao.getCountArrearage(returnedGoods);//查询审核需要的条件,判断无'平欠款'记录
			if(countArrearage > 0){//平欠款是-欠款.小于0
				orderGoodsDetailsDao.deleteArrearage(returnedGoods);//删除'平欠款'记录
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

	/**
	 * 获取订单商品中的已退款金额
	 * @param returnedGoods
	 * @return
	 */
	public double getSurplusReturnAmount(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getSurplusReturnAmount(returnedGoods);
	}

	/**
	 * 获取营业额信息
	 * @param returnedGoods
	 * @return
	 */
	public TurnOverDetails getTurnover(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getTurnover(returnedGoods);
	}
	
	/**
	 * 获取业务员营业额
	 * @param returnedGoods
	 * @return
	 */
	public OrderPushmoneyRecord getOrderPushmoneyRecord(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getOrderPushmoneyRecord(returnedGoods);
	}

	/**
	 * 获取店营业额
	 * @param returnedGoods
	 * @return
	 */
	public TurnOverDetails getMtmyTurnoverDetails(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getMtmyTurnoverDetails(returnedGoods);
	}

	/**
	 * 获取店铺营业额的操作日志
	 * @param page
	 * @param turnOverDetails
	 * @return
	 */
	public Page<TurnOverDetails> findMtmyTurnoverDetailsList(Page<TurnOverDetails> page,
			TurnOverDetails turnOverDetails) {
		// 设置分页参数
		turnOverDetails.setPage(page);
		// 执行分页查询
		page.setList(returnedGoodsDao.findMtmyTurnoverDetailsList(turnOverDetails));
		return page;
	}

	/**
	 * 获取店营业额明细列表
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getMtmyTurnoverDetailsList(TurnOverDetails turnOverDetails) {
		return returnedGoodsDao.getMtmyTurnoverDetailsList(turnOverDetails);
	}

	/**
	 * 保存店营业额增减值
	 * @param turnOverDetails
	 */
	public void saveMtmyTurnoverDetails(TurnOverDetails turnOverDetails) {
		User user = UserUtils.getUser();
		//业务员的营业额信息添加到表中   (先查询信息)
		List<TurnOverDetails> list = dao.getMtmyTurnoverDetailsList(turnOverDetails);
		String amounts = turnOverDetails.getAmounts();
		String[] amount = amounts.split(",");
		for (int i = 0; i < amount.length; i++) {
			list.get(i).setDetailsId(turnOverDetails.getDetailsId());
			list.get(i).setType(3);
			list.get(i).setStatus(4);
			list.get(i).setAmount(Double.parseDouble(amount[i]));
			list.get(i).setCreateBy(user);
			dao.saveMtmyTurnoverDetails(list.get(i));
		}
	}

	/**
	 * 查询每个业务员的售后审核扣减的营业额
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getReturnedAmountList(TurnOverDetails turnOverDetails) {
		return dao.getReturnedAmountList(turnOverDetails);
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
	 * 查询每个业务员的售后审核扣减的营业额
	 * @param turnOverDetails
	 * @return
	 */
	public List<OrderPushmoneyRecord> getReturnedPushmoneyList(TurnOverDetails turnOverDetails) {
		return dao.getReturnedPushmoneyList(turnOverDetails);
	}

	/**
	 * 获取各个部门的营业额合计
	 * @param orderId
	 * @return
	 */
	public List<OrderPushmoneyRecord> getSumBeauticianTurnover(String orderId) {
		return dao.getSumBeauticianTurnover(orderId);
	}

	/**
	 * 保存业务员营业额
	 * @param orderPushmoneyRecord
	 */
	public void saveBeauticianTurnover(OrderPushmoneyRecord orderPushmoneyRecord) {
		User user = UserUtils.getUser();
		//业务员的营业额信息添加到表中   (先查询信息)
		List<OrderPushmoneyRecord> list = dao.getBeauticianTurnoverList(orderPushmoneyRecord.getOrderId());
		//把获取到的金额全部切割
		String pushMoneys = orderPushmoneyRecord.getPushMoneys();
		String[] split = pushMoneys.split(",");
		for (int i = 0; i < split.length; i++) {
			list.get(i).setOrderId(orderPushmoneyRecord.getOrderId());//
			list.get(i).setReturnedId(orderPushmoneyRecord.getReturnedId());
			list.get(i).setTurnOverDetailsId(list.get(i).getPushmoneyRecordId());
			list.get(i).setType(3);
			list.get(i).setPushMoney(Double.parseDouble(split[i]));
			list.get(i).setCreateBy(user);
			dao.saveBeauticianTurnover(list.get(i));
		}
	}

	/**
	 * 获取业务员退款营业额(type=3)
	 * @param turnOverDetails
	 * @return
	 */
	public List<OrderPushmoneyRecord> getOrderPushmoneyRecordListView(TurnOverDetails turnOverDetails) {
		return dao.getOrderPushmoneyRecordListView(turnOverDetails);
	}

	/**
	 * 获取店铺退款营业额(type=3)
	 * @param turnOverDetails
	 * @return
	 */
	public List<TurnOverDetails> getMtmyTurnoverDetailsListView(TurnOverDetails turnOverDetails) {
		return dao.getMtmyTurnoverDetailsListView(turnOverDetails);
	}

	/**
	 * 获取业务员退货营业额的操作日志记录
	 * @param page
	 * @param orderPushmoneyRecord
	 * @return
	 */
	public Page<OrderPushmoneyRecord> getReturnedBeauticianLog(Page<OrderPushmoneyRecord> page, OrderPushmoneyRecord orderPushmoneyRecord) {
		// 设置分页参数
		orderPushmoneyRecord.setPage(page);
		// 执行分页查询
		page.setList(returnedGoodsDao.getReturnedBeauticianLog(orderPushmoneyRecord));
		return page;
	}

	/**
	 * 获取不包含自己本身的总的售后金额和售后数量
	 * @return
	 */
	public ReturnedGoods getAmountAndNum(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getAmountAndNum(returnedGoods);
	}

	/**
	 * 根据商品id查询不是当前售后订单id的实物售后数量
	 * @param returnedGoods
	 * @return
	 */
	public int getRealReturnNum(ReturnedGoods returnedGoods) {
		return returnedGoodsDao.getRealReturnNum(returnedGoods);
	}
	
	/**
	 * 根据退货id查询子项实物的售后数量
	 * @param orderIds
	 * @return
	 */
	public List<ReturnedGoods> selectKinderSon(String orderIds){
		return returnedGoodsDao.selectKinderSon(orderIds);
	}
	
	/**
	 * 根据订单id查询该订单的售后订单信息
	 * @param orderIds
	 * @return
	 */
	public List<ReturnedGoods> queryReturnList(String orderIds){
		return returnedGoodsDao.queryReturnList(orderIds);
	}
}
