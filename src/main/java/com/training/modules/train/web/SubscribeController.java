package com.training.modules.train.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.utils.IdGen;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainOfflineSubscribeTime;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.service.TrainOfflineSubscribeTimeService;	
import com.training.modules.train.utils.CategorysUtils;

/**
 * 课程线下预约
 * @author kele
 * @version 2016年3月18日
 */
@Controller
@RequestMapping(value = "${adminPath}/train/subscribe")
public class SubscribeController extends BaseController{

	@Autowired
	private TrainOfflineSubscribeTimeService trainOfflineSubscribeTimeService;
	
	@Autowired
	private TrainCategorysService trainCategorysService;
	
	/**
	 * 添加线下预约
	 * @return
	 */
	@RequestMapping(value = {"addsubscribe", ""})
	public String addsubscribe(TrainCategorys trainCategorys,HttpServletRequest request, HttpServletResponse response, Model model) {
		trainCategorys.setPriority(1);
		//添加数据权限
		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		//查询1级分类
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		model.addAttribute("listone", listone);
		
		String categoryId = request.getParameter("categoryId");
		trainCategorys = trainCategorysService.get(categoryId);
		model.addAttribute("trainCategorys", trainCategorys);
		
		//查询状态为[1发布]的列表
		TrainOfflineSubscribeTime trainOfflineSubscribeTime = new TrainOfflineSubscribeTime();
		trainOfflineSubscribeTime.setCategoryId(categoryId);
		trainOfflineSubscribeTime.setStatus(0);//发布
		model.addAttribute("findStatusList", trainOfflineSubscribeTimeService.findStatusList(trainOfflineSubscribeTime));
		
		return "modules/train/addofflinesubscribe";
	}
	
	/**
	 * 课程线下预约-保存
	 * @return
	 */
	@RequiresPermissions(value={"train:subscribe:savesubscribe"},logical=Logical.OR)
	@RequestMapping(value = {"savesubscribe", ""})
	public String savesubscribe(TrainOfflineSubscribeTime trainOfflineSubscribeTime,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes){
		try {
			if(null != trainOfflineSubscribeTime.getSubscribeDate()){
				User user = UserUtils.getUser();
				
				for (int i = 0; i < trainOfflineSubscribeTime.getSubscribeDate().length; i++) {
					if(null != trainOfflineSubscribeTime.getSubscribeDate()[i]){
						TrainOfflineSubscribeTime subscribeTime = new TrainOfflineSubscribeTime();
						subscribeTime.setSubscribeId(IdGen.uuid());
						subscribeTime.setCategoryId(trainOfflineSubscribeTime.getCategoryId());
						subscribeTime.setUser(user);
						subscribeTime.setStatus(0);//发布
						subscribeTime.setSubscribeTime(trainOfflineSubscribeTime.getSubscribeDate()[i]);
						trainOfflineSubscribeTimeService.save(subscribeTime);
					}
				}
			}
			addMessage(redirectAttributes, "保存 '课程线下预约' 成功");
			return "redirect:" + adminPath + "/train/studentsubscribe/findsetlist?categoryId="+trainOfflineSubscribeTime.getCategoryId();
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存课程线下预约出现异常，请与管理员联系");
			logger.error("保存课程线下预约出现异常,异常信息为："+e.getMessage());
			return "redirect:" + adminPath + "/train/studentsubscribe/findsetlist?categoryId="+trainOfflineSubscribeTime.getCategoryId();
		}
	}
	
	/**
	 * 人工控制，预约时间开关
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"subscribeupdatestatus", ""})
	public @ResponseBody Map<String, String> subscribeupdatestatus(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> jsonMap = new HashMap<String, String>();
		String subscribeId = request.getParameter("subscribeId");//预约ID
		String status = request.getParameter("status");//status[0发布;1终止]
		
		if((null == subscribeId || "".equals(subscribeId))
				&& (null == status || "".equals(status))){
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "参数为空");
			return jsonMap;
		}
		TrainOfflineSubscribeTime subscribetime = new TrainOfflineSubscribeTime();
		//预约时间小于当前时间，修改状态为[1终止]
		subscribetime.setSubscribeId(subscribeId);
		subscribetime.setStatus(Integer.parseInt(status));//终止
		int i = trainOfflineSubscribeTimeService.updateStatus(subscribetime);
		if(i == 1){
			jsonMap.put("STATUS", "SUCCESS");
		}else{
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败");
		}
		return jsonMap;
	}
	
}
