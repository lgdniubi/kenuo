package com.training.modules.train.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainLiveActivityRatio;
import com.training.modules.train.entity.TrainLiveExchangeRatio;
import com.training.modules.train.service.TrainLiveActivityRatioService;

/**
 * 充值兑换配置表对应的活动Controller
 * @author xiaoye  2017年3月13日
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/train/ratio")
public class TrainLiveActivityRatioController extends BaseController{
	
	@Autowired
	private TrainLiveActivityRatioService trainLiveActivityRatioService;
	
	/**
	 * 充值兑换配置表对应的活动列表
	 * @param trainLiveActivityRatio
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(TrainLiveActivityRatio trainLiveActivityRatio,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try{
			Page<TrainLiveActivityRatio> page = trainLiveActivityRatioService.findPage(new Page<TrainLiveActivityRatio>(request, response), trainLiveActivityRatio);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看充值兑换配置表对应的活动列表失败!", e);
			logger.error("查看充值兑换配置表对应的活动列表失败：" + e.getMessage());
		}
		return "modules/train/LiveActivityRatioList";
	}
	
	/**
	 * 跳转充值兑换配置表对应的活动的增加页面
	 * @param trainLiveActivityRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(TrainLiveActivityRatio trainLiveActivityRatio,HttpServletRequest request,Model model){
		try{
			if(trainLiveActivityRatio.getTrainLiveActivityRatioId() != 0){
				trainLiveActivityRatio = trainLiveActivityRatioService.getTrainLiveActivityRatio(trainLiveActivityRatio.getTrainLiveActivityRatioId());
			}
			model.addAttribute("trainLiveActivityRatio",trainLiveActivityRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转充值兑换配置表对应的活动的增加页面失败!", e);
			logger.error("跳转充值兑换配置表对应的活动的增加页面失败：" + e.getMessage());
		}
		return "modules/train/LiveActivityRatioForm";
	}
	
	/**
	 * 保存充值兑换配置表对应的活动
	 * @param trainLiveActivityRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(TrainLiveActivityRatio trainLiveActivityRatio,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(trainLiveActivityRatio.getTrainLiveActivityRatioId() == 0){
				trainLiveActivityRatioService.insert(trainLiveActivityRatio);
				addMessage(redirectAttributes,"添加充值兑换配置表对应的活动成功");
			}else{
				trainLiveActivityRatioService.update(trainLiveActivityRatio);
				addMessage(redirectAttributes,"修改充值兑换配置表对应的活动成功");
			}
		}catch(Exception e ){
			BugLogUtils.saveBugLog(request, "保存充值兑换配置表对应的活动失败!", e);
			logger.error("保存充值兑换配置表对应的活动失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存充值兑换配置表对应的活动失败");
		}                                 
		return "redirect:" + adminPath + "/train/ratio/list";
	}
	
	/**
	 * 逻辑删除充值兑换配置表对应的活动
	 * @param trainLiveActivityRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="del")
	public String delete(TrainLiveActivityRatio trainLiveActivityRatio,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			trainLiveActivityRatioService.deleteTrainLiveActivityRatio(trainLiveActivityRatio.getTrainLiveActivityRatioId());
			addMessage(redirectAttributes, "删除充值兑换配置表对应的活动成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除充值兑换配置表对应的活动失败!", e);
			logger.error("删除充值兑换配置表对应的活动失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除充值兑换配置表对应的活动失败");
		}
		return "redirect:" + adminPath + "/train/ratio/list";
	}
	
	/**
	 * 修改充值兑换配置表对应的活动的状态
	 * @param request
	 * @param trainLiveActivityRatio
	 * @return
	 */
	@RequestMapping(value="updateIsShow")
	public String updateIsShow(HttpServletRequest request,TrainLiveActivityRatio trainLiveActivityRatio) {
		try {
			String flag = request.getParameter("flag");
			trainLiveActivityRatioService.updateAll();
			if("0".equals(flag)){
				trainLiveActivityRatio.setIsShow(flag);
				trainLiveActivityRatioService.updateIsShow(trainLiveActivityRatio);
			}else if("1".equals(flag)){
				int trainLiveActivityRatioId = trainLiveActivityRatioService.selectIdByCreateDate();
				trainLiveActivityRatio = trainLiveActivityRatioService.getTrainLiveActivityRatio(trainLiveActivityRatioId);
				trainLiveActivityRatio.setIsShow("0");
				trainLiveActivityRatioService.updateIsShow(trainLiveActivityRatio);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改充值兑换配置表对应的活动的状态失败", e);
			logger.error("修改充值兑换配置表对应的活动的状态失败：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/train/ratio/list";
	}
	
	/**
	 * 进入兑换比例配置列表
	 * @param trainLiveActivityRatio
	 * @param trainLiveExchangeRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="exchangeRatioList")
	public String exchangeRatioList(TrainLiveActivityRatio trainLiveActivityRatio,TrainLiveExchangeRatio trainLiveExchangeRatio,HttpServletRequest request,Model model){
		try{
			trainLiveExchangeRatio.setActivityId(trainLiveActivityRatio.getTrainLiveActivityRatioId());
			List<TrainLiveExchangeRatio> page0 = trainLiveActivityRatioService.newFindPage(trainLiveExchangeRatio.getActivityId(),"0");
			List<TrainLiveExchangeRatio> page1 = trainLiveActivityRatioService.newFindPage(trainLiveExchangeRatio.getActivityId(),"1");
			model.addAttribute("page0", page0);
			model.addAttribute("page1", page1);	
			model.addAttribute("trainLiveExchangeRatio",trainLiveExchangeRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看直播兑换比例配置列表失败!", e);
			logger.error("查看直播兑换比例配置列表失败：" + e.getMessage());
		}
		return "modules/train/exchangeRatioList";
	}
	
	/**
	 * 修改是否显示
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "changeIsShow")
	@ResponseBody
	public Map<String, String> changeIsShow(HttpServletRequest request,TrainLiveExchangeRatio trainLiveExchangeRatio) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isShow = request.getParameter("isShow");
			trainLiveExchangeRatio = trainLiveActivityRatioService.getTrainLiveExchangeRatio(trainLiveExchangeRatio.getExchangeRatioId());
			int num = trainLiveActivityRatioService.selectNum(trainLiveExchangeRatio);
			if("0".equals(isShow)){
				if(num >= 6){
					jsonMap.put("STATUS", "ERROR");
					jsonMap.put("MESSAGE", "处于显示状态的已经有六个，不可再多显示一个");
				}else{
					trainLiveExchangeRatio.setIsShow(isShow);
					trainLiveActivityRatioService.changeIsShow(trainLiveExchangeRatio);
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISSHOW", isShow);
				}
			}else if("1".equals(isShow)){
				trainLiveExchangeRatio.setIsShow(isShow);
				trainLiveActivityRatioService.changeIsShow(trainLiveExchangeRatio);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改是否显示失败", e);
			logger.error("修改是否显示失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 跳转兑换比例的编辑页面
	 * @param trainLiveExchangeRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="addExchangeRatioForm")
	public String addExchangeRatioForm(TrainLiveExchangeRatio trainLiveExchangeRatio,HttpServletRequest request,Model model,String flag){
		try{
			if("update".equals(flag)){
				trainLiveExchangeRatio.setExchangeRatioId(Integer.valueOf(request.getParameter("id")));
				trainLiveExchangeRatio = trainLiveActivityRatioService.getTrainLiveExchangeRatio(trainLiveExchangeRatio.getExchangeRatioId()); 
			}else{
				trainLiveExchangeRatio.setActivityId(Integer.valueOf(request.getParameter("id")));
			}
			model.addAttribute("trainLiveExchangeRatio",trainLiveExchangeRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转兑换比例的编辑页面失败!", e);
			logger.error("跳转兑换比例的编辑页面失败：" + e.getMessage());
		}
		return "modules/train/addExchangeRatioForm";
	}
	
	/**
	 * 保存兑换比例
	 * @param trainLiveExchangeRatio
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveExchangeRatio")
	public String saveExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio,RedirectAttributes redirectAttributes,HttpServletRequest request,String flag){
		try{
			if("add".equals(flag)){
				trainLiveExchangeRatio.setActivityId(Integer.valueOf(request.getParameter("id")));
				trainLiveActivityRatioService.insertExchangeRatio(trainLiveExchangeRatio);
				return "success";
			}else{
				trainLiveExchangeRatio.setExchangeRatioId(Integer.valueOf(request.getParameter("id")));
				trainLiveActivityRatioService.updateExchangeRatio(trainLiveExchangeRatio);
				return "success";
			}
		}catch(Exception e){
			return "error";
		}
	}
	
	/**
	 * 删除某一兑换比例
	 * @param tabBackground
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delExchangeRatio")
	@ResponseBody
	public String delExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio,RedirectAttributes redirectAttributes){
		try{
			trainLiveActivityRatioService.delExchangeRatio(trainLiveExchangeRatio);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
}
