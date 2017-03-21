package com.training.modules.train.web;

import java.util.HashMap;
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
import com.training.modules.train.entity.TrainLiveGift;
import com.training.modules.train.service.TrainLiveGiftService;

/**
 * 直播送的礼物Controller
 * @author xiaoye 2017年3月16日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/train/liveGift")
public class TrainLiveGiftController extends BaseController{
	
	@Autowired
	private TrainLiveGiftService trainLiveGiftService;
	
	/**
	 * 查看直播送的礼物列表
	 * @param trainLiveGift
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(TrainLiveGift trainLiveGift,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try{
			Page<TrainLiveGift> page = trainLiveGiftService.findPage(new Page<TrainLiveGift>(request, response), trainLiveGift);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看直播送的礼物列表失败!", e);
			logger.error("查看直播送的礼物列表失败：" + e.getMessage());
		}
		return "modules/train/trainLiveGiftList";
	}
	
	/**
	 * 跳转编辑礼物页面
	 * @param trainLiveGift
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(TrainLiveGift trainLiveGift,HttpServletRequest request,Model model){
		try{
			if(trainLiveGift.getTrainLiveGiftId() != 0){
				trainLiveGift = trainLiveGiftService.getTrainLiveGift(trainLiveGift.getTrainLiveGiftId());
			}
			model.addAttribute("trainLiveGift",trainLiveGift);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑礼物页面失败!", e);
			logger.error("跳转编辑礼物页面失败：" + e.getMessage());
		}
		return "modules/train/trainLiveGiftForm";
	}
	
	/**
	 * 保存礼物
	 * @param trainLiveGift
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(TrainLiveGift trainLiveGift,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(trainLiveGift.getTrainLiveGiftId() == 0){
				trainLiveGiftService.insert(trainLiveGift);
				addMessage(redirectAttributes,"添加礼物成功");
			}else{
				trainLiveGiftService.update(trainLiveGift);
				addMessage(redirectAttributes,"修改礼物成功");
			}
		}catch(Exception e ){
			BugLogUtils.saveBugLog(request, "保存礼物失败!", e);
			logger.error("保存礼物失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存礼物失败");
		}                                 
		return "redirect:" + adminPath + "/train/liveGift/list";
	}
	
	/**
	 * 逻辑删除礼物
	 * @param trainLiveActivityRatio
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="del")
	public String delete(TrainLiveGift trainLiveGift,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			trainLiveGiftService.deleteGift(trainLiveGift.getTrainLiveGiftId());
			addMessage(redirectAttributes, "逻辑删除礼物成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "逻辑删除礼物失败!", e);
			logger.error("逻辑删除礼物失败：" + e.getMessage());
			addMessage(redirectAttributes, "逻辑删除礼物失败");
		}
		return "redirect:" + adminPath + "/train/liveGift/list";
	}
	
	/**
	 * 修改礼物的状态
	 * @param request
	 * @param trainLiveGift
	 * @return
	 */
	@RequestMapping(value="updateIsShow")
	@ResponseBody
	public Map<String, String> updateIsShow(HttpServletRequest request,TrainLiveGift trainLiveGift) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isShow = request.getParameter("isShow");
			trainLiveGift = trainLiveGiftService.getTrainLiveGift(trainLiveGift.getTrainLiveGiftId());
			int num = trainLiveGiftService.selectNum();
			if("0".equals(isShow)){
				if(num >= 16){
					jsonMap.put("STATUS", "ERROR");
					jsonMap.put("MESSAGE", "处于显示状态的已经有16个，不可再多显示一个");
				}else{
					trainLiveGift.setIsShow(isShow);
					trainLiveGiftService.updateIsShow(trainLiveGift);
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISSHOW", isShow);
				}
			}else if("1".equals(isShow)){
				trainLiveGift.setIsShow(isShow);
				trainLiveGiftService.updateIsShow(trainLiveGift);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改礼物的状态失败", e);
			logger.error("修改礼物的状态失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 修改礼物是否能连发
	 * @param request
	 * @param trainLiveGift
	 * @return
	 */
	@RequestMapping(value="updateIsBatter")
	@ResponseBody
	public Map<String, String> updateIsBatter(HttpServletRequest request,TrainLiveGift trainLiveGift) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isBatter = request.getParameter("isBatter");
			trainLiveGift.setIsBatter(isBatter);
			trainLiveGiftService.updateIsBatter(trainLiveGift);
			jsonMap.put("STATUS", "OK");
			jsonMap.put("ISBATTER", isBatter);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改礼物是否能连发失败", e);
			logger.error("修改礼物是否能连发失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}
