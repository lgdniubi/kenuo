package com.training.modules.train.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.modules.train.entity.TrainOfflineSubscribeTime;
import com.training.modules.train.entity.TrainOfflineTestSubscribe;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.service.TrainOfflineSubscribeTimeService;
import com.training.modules.train.service.TrainOfflineTestSubscribeService;

/**
 * 学生课程预约
 * @author kele
 * @version 2016-4-6
 */
@Controller
@RequestMapping(value = "${adminPath}/train/studentsubscribe")
public class StudentSubscribeController {
	
	@Autowired
	private TrainOfflineTestSubscribeService trainOfflineTestSubscribeService;
	
	@Autowired
	private TrainOfflineSubscribeTimeService trainOfflineSubscribeTimeService;
	
	@Autowired
	private TrainCategorysService trainCategorysService;
	
	/**
	 * 学生线下预约集合列表
	 * @param trainOfflineTestSubscribe
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:studentsubscribe:findsetlist")
	@RequestMapping(value = {"findsetlist"})
	public String findsetlist(TrainOfflineTestSubscribe trainOfflineTestSubscribe,HttpServletRequest request, HttpServletResponse response, Model model){
		
		String categoryId = request.getParameter("categoryId");
		
		//查询状态为[0发布]并且小于当前时间的列表
		TrainOfflineSubscribeTime trainOfflineSubscribeTime = new TrainOfflineSubscribeTime();
		trainOfflineSubscribeTime.setCategoryId(categoryId);
		trainOfflineSubscribeTime.setStatus(0);//发布
		trainOfflineSubscribeTime.setSubscribeTime(new Date());//小于当前时间
		List<TrainOfflineSubscribeTime> findStatusList = trainOfflineSubscribeTimeService.findStatusList(trainOfflineSubscribeTime);
		
		if(null != findStatusList && findStatusList.size() > 0){
			for (int i = 0; i < findStatusList.size(); i++) {
				TrainOfflineSubscribeTime subscribetime = findStatusList.get(i);
				//预约时间小于当前时间，修改状态为[1终止]
				subscribetime.setStatus(1);//终止
				trainOfflineSubscribeTimeService.updateStatus(subscribetime);
			}
			
		}
		
		//查询状态为[1发布]的列表
		trainOfflineSubscribeTime.setCategoryId(categoryId);
		trainOfflineSubscribeTime.setStatus(0);//发布
		trainOfflineSubscribeTime.setSubscribeTime(null);
		model.addAttribute("findStatusList", trainOfflineSubscribeTimeService.findStatusList(trainOfflineSubscribeTime));
		
		trainOfflineTestSubscribe.setTrainCategorys(trainCategorysService.get(categoryId));
		Page<TrainOfflineTestSubscribe> page = trainOfflineTestSubscribeService.findSetList(new Page<TrainOfflineTestSubscribe>(request, response), trainOfflineTestSubscribe);
		model.addAttribute("page", page);
		model.addAttribute("trainOfflineTestSubscribe", trainOfflineTestSubscribe);
		
		return "modules/train/stuofflinesubscribeSetList";
	}
	
	/**
	 * 查询学生预约详细信息
	 * @param trainOfflineTestSubscribe
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:studentsubscribe:findlist")
	@RequestMapping(value = {"findlist"})
	public String findlist(TrainOfflineTestSubscribe trainOfflineTestSubscribe,HttpServletRequest request, HttpServletResponse response, Model model){
		String categoryId = request.getParameter("categoryId");
		String subscribeId = request.getParameter("subscribeId");
		
		trainOfflineTestSubscribe.setCategoryId(categoryId);
		trainOfflineTestSubscribe.setSubscribeId(subscribeId);

		Page<TrainOfflineTestSubscribe> page = trainOfflineTestSubscribeService.findList(new Page<TrainOfflineTestSubscribe>(request, response), trainOfflineTestSubscribe);
		model.addAttribute("page", page);
		model.addAttribute("trainOfflineTestSubscribe", trainOfflineTestSubscribe);
		return "modules/train/stuofflinesubscribeViewList";
	}
	
	
}
