package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.dao.OrderGoodsDetailsDao;
import com.training.modules.ec.dao.TurnOverDetailsDao;
import com.training.modules.ec.entity.OfficeAccount;
import com.training.modules.ec.entity.OfficeAccountLog;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Reservation;
import com.training.modules.ec.entity.TurnOverDetails;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.ec.service.ReservationService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 有预约金的商品，预约完成时按次数给店铺打预约金和补偿金
 * @author xiaoye
 *
 */
@Component
public class ShareAdvancePrice extends CommonService{
	
	private static ReservationService reservationService;
	private static OrdersService ordersService;
	private static OrderGoodsDetailsDao orderGoodsDetailsDao;
	private static TurnOverDetailsDao turnOverDetailsDao;
	
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Logger logger = Logger.getLogger(SubBeforeDay.class);
	
	static{
		reservationService = (ReservationService) BeanUtil.getBean("reservationService");
		ordersService = (OrdersService) BeanUtil.getBean("ordersService");
		orderGoodsDetailsDao = (OrderGoodsDetailsDao) BeanUtil.getBean("orderGoodsDetailsDao");
		turnOverDetailsDao = (TurnOverDetailsDao) BeanUtil.getBean("turnOverDetailsDao");
	}

	public void shareAdvancePrice(){
		
		logger.info("[work0],start,有预约金的商品，预约完成时按次数给店铺打预约金和补偿金,开始时间："+df.format(new Date()));
		
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("shareAdvancePrice");
		taskLog.setStartDate(startDate);
		
		try {
		    String startTime = QuartzStartConfigUtils.queryMtmyValue("share_advance_price_start_time"); //上线的时间
			String time = QuartzStartConfigUtils.queryMtmyValue("mtmy_advance_time"); //上次符合条件的最后一个预约的时间
			
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			Date lastTime = sdf.parse(time);                                      
			Date itemStartTime = sdf.parse(startTime);                             //项目上线的时间
			DecimalFormat formater = new DecimalFormat("#0.##");
			
			Map<String, Object> map = new HashMap<String, Object>();
			User user = new User();
			user.setId("1");
			
			List<Reservation> reservationList = reservationService.queryApptOrderForAdvancePrice(lastTime,itemStartTime);
			if(reservationList.size() > 0){
				for(int i=0;i<reservationList.size();i++){
					OrderGoods orderGoods = ordersService.selectOrderGoodsByRecid(Integer.valueOf(reservationList.get(i).getDetailsMappingId()));  
					double detailsTotalAmount = orderGoods.getTotalAmount();       //预约金用了红包、折扣以后实际付款的钱
					double advance = orderGoods.getAdvancePrice();                 //预约金
					int goodsType = orderGoods.getGoodsType();                    //商品区分(0: 老商品 1: 新商品)
					int totalTimes = orderGoods.getTotalTimes();                    //商品的总次数
					
					//该订单下预约已完成和爽约的个数
					int completeNum = reservationService.queryCompleteNum(reservationList.get(i).getGroupId(),reservationList.get(i).getGoodsMappingId(),reservationList.get(i).getReservationId());
					
					if(i == reservationList.size()-1){
						map.put("mtmy_advance_time", sdf.format(reservationList.get(i).getUpdateDate()));
					}
					
					//获取当前用户预约对应的店铺id
					String shopId = orderGoodsDetailsDao.selectShopId(reservationList.get(i).getReservationId()); 
					
					//同步数据到营业额明细表
					double appSum = orderGoodsDetailsDao.queryAppOrdersSum(reservationList.get(i).getOrderId());
					double realTurnOverMoney = 0.0;     //每次要打给商家的营业额
					
					double avgTurnOverMoney = Double.parseDouble(formater.format(appSum/totalTimes));     //前(n-1)次平分的钱
					double lastTurnOverMoney = Double.parseDouble(formater.format(appSum - avgTurnOverMoney*(totalTimes-1)));   //最后一次平分的钱 
							
					if((orderGoods.getIsreal() == 2) || (orderGoods.getIsreal() == 3) || (orderGoods.getIsreal() == 1 && totalTimes != 999)){
						if(completeNum == totalTimes){   //最后一次预约完成
							realTurnOverMoney = lastTurnOverMoney;
						}else{    //前n-1次预约完成
							realTurnOverMoney = avgTurnOverMoney;
						}
					}else if(orderGoods.getIsreal() == 1 && totalTimes == 999){   //时限卡第一次就打全部
						if(completeNum == 1){
							realTurnOverMoney = appSum;
						}else{
							continue;
						}
					}
					
					TurnOverDetails turnOverDetails1 = new TurnOverDetails();
					turnOverDetails1.setOrderId(reservationList.get(i).getOrderId());
					turnOverDetails1.setDetailsId(reservationList.get(i).getOrderId());
					turnOverDetails1.setType(1);
					turnOverDetails1.setAmount(realTurnOverMoney);
					turnOverDetails1.setUseBalance(0);
					turnOverDetails1.setStatus(1);
					turnOverDetails1.setUserId(reservationList.get(i).getUserId());
					turnOverDetails1.setBelongOfficeId(shopId);
					turnOverDetails1.setCreateBy(reservationList.get(i).getUpdateBy());
					turnOverDetails1.setSettleDate(new Date());
					turnOverDetailsDao.saveTurnOverDetails(turnOverDetails1);
					
					//若为老商品，则对店铺有补偿
					if(goodsType == 0){
						//若预约金大于0
						if(advance > 0){   
							//对登云账户进行操作
							if(detailsTotalAmount > 0){
								double claimMoney = 0.0;   //补偿金  补助不超过20
								double realMoney = 0.0;     //每次要打给商家的金额
								
								if(detailsTotalAmount * 0.2 >= 20){
									claimMoney = detailsTotalAmount + 20;
								}else{
									claimMoney = detailsTotalAmount * 1.2;
								}
								
								double avgMoney = Double.parseDouble(formater.format(claimMoney/totalTimes));     //前(n-1)次平分的钱
								double lastMoney = Double.parseDouble(formater.format(claimMoney - avgMoney*(totalTimes-1)));   //最后一次平分的钱 
										
								if((orderGoods.getIsreal() == 2) || (orderGoods.getIsreal() == 3) || (orderGoods.getIsreal() == 1 && totalTimes != 999)){
									if(completeNum == totalTimes){   //最后一次预约完成
										realMoney = lastMoney;
									}else{    //前n-1次预约完成
										realMoney = avgMoney;
									}
								}else if(orderGoods.getIsreal() == 1 && totalTimes == 999){   //时限卡第一次就打全部
									if(completeNum == 1){
										realMoney = claimMoney;
									}else{
										continue;
									}
								}
								
								OfficeAccountLog officeAccountLog = new OfficeAccountLog();
								
								double amount = orderGoodsDetailsDao.selectByOfficeId("1");   //登云账户的钱
								double afterAmount = Double.parseDouble(formater.format(amount - realMoney));
								orderGoodsDetailsDao.updateByOfficeId(afterAmount, "1");   //更新登云账户金额
								
								//登云账户减少钱时对日志进行操作
								officeAccountLog.setOrderId(reservationList.get(i).getOrderId());
								officeAccountLog.setOfficeId("1");
								officeAccountLog.setType("1");
								officeAccountLog.setOfficeFrom("1");
								officeAccountLog.setAmount(realMoney);
								officeAccountLog.setCreateBy(user);
								officeAccountLog.setApptId(String.valueOf(reservationList.get(i).getReservationId()));
								orderGoodsDetailsDao.insertOfficeAccountLog(officeAccountLog);
								
								//若无该店铺的账户
								if(orderGoodsDetailsDao.selectShopByOfficeId(shopId) == 0){    
									OfficeAccount officeAccount = new OfficeAccount();
									officeAccount.setAmount(realMoney);
									officeAccount.setOfficeId(shopId);
									orderGoodsDetailsDao.insertByOfficeId(officeAccount);
								}else{         
									double shopAmount = orderGoodsDetailsDao.selectByOfficeId(shopId);   //账户中店铺的钱
									double afterShopAmount =  Double.parseDouble(formater.format(shopAmount + realMoney));
									orderGoodsDetailsDao.updateByOfficeId(afterShopAmount, shopId);
								}
								//店铺的账户减少钱时对日志进行操作
								officeAccountLog.setOrderId(reservationList.get(i).getOrderId());
								officeAccountLog.setOfficeId(shopId);
								officeAccountLog.setType("0");
								officeAccountLog.setOfficeFrom("1");
								officeAccountLog.setAmount(realMoney);
								officeAccountLog.setCreateBy(user);
								officeAccountLog.setApptId(String.valueOf(reservationList.get(i).getReservationId()));
								orderGoodsDetailsDao.insertOfficeAccountLog(officeAccountLog);
							}
						}
					}
					
				}
				QuartzStartConfigUtils.addMtmyQuartzStartConfig(map);
				
			}
			
		} catch (Exception e) {
			logger.error("#####【定时任务shareAdvancePrice】有预约金的商品，预约完成时按次数给店铺打预约金和补偿金,出现异常，异常信息为："+e.getMessage());
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally {
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[work0],end,有预约金的商品，预约完成时按次数给店铺打预约金和补偿金,结束时间："+df.format(new Date()));
	}
	
}
