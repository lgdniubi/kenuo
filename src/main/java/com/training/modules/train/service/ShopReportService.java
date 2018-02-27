package com.training.modules.train.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.common.utils.DateUtils;
import com.training.common.utils.ListSplitUtils;
import com.training.modules.quartz.dao.ITaskDao;
import com.training.modules.quartz.entity.TaskLog;
import com.training.modules.train.dao.ShopReportDao;
import com.training.modules.train.entity.ShopReport;


/**
 * 店务报表service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ShopReportService extends CrudService<ShopReportDao,ShopReport>{
	
	@Autowired
	private ShopReportDao shopReportDao;
	@Autowired
	private ITaskDao iTaskDao;
	
	/**
	 * 用户消费统计
	 * @param jobName
	 */
	@SuppressWarnings("unchecked")
	public void reportUserConsume(String jobName){
		//查询所有有过绑定的用户ID
		List<ShopReport> userList = shopReportDao.findUserList();
		if(null != userList){
			Map<String, Object> map = findNewestTasksLogs(jobName);
			if((Integer)map.get("status") == 1){
				System.out.println("###定时器:"+jobName+",当日执行多次!");
			}else{
				if(userList.size() > 500){
					List<Object> list = ListSplitUtils.listSplit("店务报表-所有绑定店铺用户,定时器："+jobName, userList, 500);
					for (int i = 0; i < list.size(); i++) {
						List<ShopReport> listSplit = (List<ShopReport>)list.get(i);
						shopReportDao.insertUserReport(listSplit,(Date)map.get("data"));
					}
				}else{
					shopReportDao.insertUserReport(userList,(Date)map.get("data"));
				}
			}
		}else{
			System.out.println("###定时器:"+jobName+",无绑定店铺的用户!");
		}
	} 

	/**
	 * 美容师绩效统计
	 * @param jobName
	 */
	public void reportBeauticianAchievement(String jobName){
		Map<String, Object> map = findNewestTasksLogs(jobName);
		if((Integer)map.get("status") == 1){
			System.out.println("###定时器:"+jobName+",当日执行多次!");
		}else{
			//插入美容师绩效报表（不含时限卡）
			shopReportDao.insertBeauticianAchievement((Date)map.get("data"));
			//插入美容师绩效报表（时限卡）
			shopReportDao.insertBeauticianAchievementCard((Date)map.get("data"));
		}
	}
	
	/**
	 * 店铺商品统计
	 * @param jobName
	 */
	@SuppressWarnings("unchecked")
	public void reportShopGoods(String jobName){
		//查询所有有过绑定的用户ID
		List<ShopReport> shopGoodsList = shopReportDao.findShopGoodsList();
		if(null != shopGoodsList){
			Map<String, Object> map = findNewestTasksLogs(jobName);
			if((Integer)map.get("status") == 1){
				System.out.println("###定时器:"+jobName+",当日执行多次!");
			}else{
				if(shopGoodsList.size() > 500){
					List<Object> list = ListSplitUtils.listSplit("店务报表-所有绑定店铺用户,定时器："+jobName, shopGoodsList, 500);
					for (int i = 0; i < list.size(); i++) {
						List<ShopReport> listSplit = (List<ShopReport>)list.get(i);
						shopReportDao.insertShopGoodsList(listSplit,(Date)map.get("data"));
					}
				}else{
					shopReportDao.insertShopGoodsList(shopGoodsList,(Date)map.get("data"));
				}
			}
		}else{
			System.out.println("###定时器:"+jobName+",无绑定店铺的用户!");
		}
	} 
	
	/**
	 * 获取店务报表开始执行时间
	 * @param jobName 定时器任务名称
	 * @return
	 */
	public Map<String, Object> findNewestTasksLogs(String jobName){
		Map<String, Object> map = new HashMap<>();
		TaskLog taskLog = new TaskLog();
		taskLog.setJobName(jobName);
		taskLog.setStatus(0);
		// 2017-7-29 新增  查询店务报表定时器最后一次执行正常的时间
		TaskLog t = iTaskDao.findNewestTasksLogs(taskLog);
		if(null != t){
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//当前时间
			String afterString=sdf.format(new Date());
			//最后一次执行正常的时间
			String beforeString=sdf.format(t.getCreateDate());
			Date after = new Date();
			Date before = new Date();
			try {
				after = sdf.parse(afterString);
				before =  sdf.parse(beforeString) ;
			} catch (ParseException e) {
				System.out.println("####报表定时器校验时间报错:"+e.getMessage());
				e.printStackTrace();
			}
			int a =(int)DateUtils.getDistanceOfTwoDate(before,after);
			if(a == 0){
				// 最后一次执行时间为当天
				map.put("status", 1);
			}else{
				map.put("status", 2);
				map.put("data", t.getCreateDate());
			}
		}else{
			// 此定时期第一次执行
			map.put("status", 0);
			map.put("data", null);
		}
		return map;
	}
}
