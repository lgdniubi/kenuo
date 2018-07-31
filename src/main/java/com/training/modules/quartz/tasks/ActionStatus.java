package com.training.modules.quartz.tasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.training.common.utils.BeanUtil;
import com.training.modules.ec.dao.ActivityCouponUserDao;
import com.training.modules.ec.entity.ActionInfo;
import com.training.modules.ec.entity.Activity;
import com.training.modules.ec.entity.ActivityCoupon;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.service.ActionInfoService;
import com.training.modules.ec.service.ActivityService;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.quartz.tasks.utils.CommonService;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 抢购活动定时开启关闭，并商品上下架
 * @author yangyang
 *
 */
@Component
public class ActionStatus extends CommonService{
	
	public static final String GOOD_UNSHELVE_KEY = "GOOD_UNSHELVE_KEY"; //商品下架
	private Logger logger = Logger.getLogger(ActionStatus.class);
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static ActionInfoService actionInfoService;
	private static GoodsService goodsService;
	private static ActivityService activityService;
	private static ActivityCouponUserDao activityCouponUserDao;
	
	static{
		actionInfoService = (ActionInfoService) BeanUtil.getBean("actionInfoService");
		goodsService = (GoodsService) BeanUtil.getBean("goodsService");
		activityService=(ActivityService) BeanUtil.getBean("activityService");
		activityCouponUserDao = (ActivityCouponUserDao)BeanUtil.getBean("activityCouponUserDao");
	}
	
	/**
	 * 订单过期定时任务
	 */
	public void actionStatus(){
		logger.info("[actionStatus],start,开启活动设置,开始时间："+df.format(new Date()));
		HttpServletRequest request=null;
		//添加日志
		TaskLog taskLog = new TaskLog();
		Date startDate;	//开始时间
		Date endDate;	//结束时间
		long runTime;	//运行时间
		
		startDate = new Date();
		taskLog.setJobName("actionStatus");
		taskLog.setStartDate(startDate);
		
		int actionId = 0;//当前订单号
		User user = UserUtils.getUser();
		try {
			List<ActionInfo> statrlist = actionInfoService.selectActionStartTime();
			logger.info("[开启时间]，扫面状态为开启,活动开启时间已到的数据，活动数据："+statrlist.size());
			if(statrlist.size()>0){
				for(ActionInfo vo : statrlist){
					actionId = vo.getActionId();//设置当天订单号，若出现异常，在日志中查询
					//将活动下的所有商品上架
					logger.info("[商品上架]，活动id："+vo.getActionId());
					Goods goods=new Goods();
					goods.setActionId(vo.getActionId());
					goods.setIsOnSale("1");
					goods.setIsAppshow("1");
					int num=goodsService.updateGoodsStauts(goods);
					List<Goods> list=actionInfoService.ActionGoodslist(vo.getActionId());
					for (int i = 0; i < list.size(); i++) {
						//用户商品上下架Regis缓存
						if(goods.getIsOnSale().equals("1")){
							//上架
							redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, list.get(i).getGoodsId()+"");
						}
					}
					//上架数量
					logger.info("[商品上架]，商品上架数量："+num);
					//修改活动已经执行定时器
					ActionInfo actionInfo=new ActionInfo();
					actionInfo.setActionId(vo.getActionId());
					actionInfo.setExecuteStatus(1);
					actionInfoService.updateExecuteStatus(actionInfo);
					

				}
			}
			
			List<ActionInfo>  endList=actionInfoService.selectActionCloseTime();
			logger.info("[关闭时间]，扫面活动状态为开启关闭，并且时间已到的数据，活动数据："+endList.size());
			if(endList.size()>0){
				for(ActionInfo vo : endList){
					actionId = vo.getActionId();//设置当天订单号，若出现异常，在日志中查询
					
					//将活动下的所有商品上架
					logger.info("[商品下架]，活动id："+vo.getActionId());
					Goods goods=new Goods();
					goods.setActionId(vo.getActionId());
					goods.setIsOnSale("0");
					goods.setIsAppshow("1");
					int num=goodsService.updateGoodsStauts(goods);
					//下架数量
					logger.info("[商品下架]，商品下架数量："+num);
					List<Goods> list=actionInfoService.ActionGoodslist(vo.getActionId());
					for (int i = 0; i < list.size(); i++) {
						//用户商品上下架Regis缓存
						if(goods.getIsOnSale().equals("0")){
							//下架
							redisClientTemplate.hset(GOOD_UNSHELVE_KEY, list.get(i).getGoodsId()+"", "0");
						}
					}
					ActionInfo actionInfo=new ActionInfo();
					actionInfo.setUpdateBy(user);
					actionInfo.setActionId(vo.getActionId());
					actionInfo.setStatus(3);
					int stnum=actionInfoService.updateStatus(actionInfo);
					logger.info("[活动过期]，过期活动数量："+stnum);
					

				}
			}
			int activityId=0;	
			List<Activity> activitieslist=activityService.selectActionCloseTime();
			logger.info("[红包活动]，扫描已过期的数据数："+activitieslist.size());
			if(activitieslist.size()>0){
				for(Activity vo : activitieslist){
					activityId=Integer.parseInt(vo.getId()); // 活动id
					logger.info("[过期活动]，更新过期红包活动状态：id:"+activityId);
					activityService.updateOutTime(activityId);
					
					// 获取活动下所有红包
					List<ActivityCoupon> list=activityService.Couponlist(String.valueOf(activityId));
					ActivityCoupon activityCoupon = new ActivityCoupon();
					activityCoupon.setId(String.valueOf(activityId));
					activityCoupon.setStatus(2);
					// 修改过期活动下红包状态为结束
					activityService.updateCouponStatus(activityCoupon);
					for (ActivityCoupon a : list) {
						activityCouponUserDao.updateCouponUser(Integer.valueOf(a.getId()));
					}
				}
			}
			
			taskLog.setJobDescription("[自动开启]，扫瞄活动开始，开启活动个数：["+statrlist.size()+"]"+"[自动结束]，已结束活动数量：["+endList.size()+"]");
			taskLog.setStatus(0);//任务状态
			
		} catch (Exception e) {
			logger.error("#####【定时任务orderTimeOut】过期订单,当前订单号["+actionId+"]出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "抢购活动定时器", e);
			taskLog.setStatus(1);
			taskLog.setExceptionMsg(e.getMessage().substring(0, e.getMessage().length()>2500?2500:e.getMessage().length()));
		}finally{
			endDate = new Date();//结束时间
			runTime = (endDate.getTime() - startDate.getTime());//运行时间
			taskLog.setEndDate(new Date());	//结束时间
			taskLog.setRunTime(runTime);
			taskService.saveTaskLog(taskLog);
		}
		
		logger.info("[actionStatus],end,活动状态设置,结束时间："+df.format(new Date()));
	}
}
