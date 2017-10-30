package com.training.modules.ec.web;

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
import com.training.modules.ec.entity.MtmyEveryoneSay;
import com.training.modules.ec.service.MtmyEveryoneSayService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 大家都在说Controller
 * @author xiaoye  2017年6月7日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/everyoneSay")
public class MtmyEveryoneSayController extends BaseController{
	
	@Autowired
	private MtmyEveryoneSayService mtmyEveryoneSayService;
	
	/**
	 * 大家都在说列表
	 * @param mtmyEveryoneSay
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(MtmyEveryoneSay mtmyEveryoneSay,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<MtmyEveryoneSay> page = mtmyEveryoneSayService.findPage(new Page<MtmyEveryoneSay>(request, response), mtmyEveryoneSay);
			model.addAttribute("page", page);
			model.addAttribute("mtmyEveryoneSay", mtmyEveryoneSay);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "大家都在说列表", e);
			logger.error("大家都在说列表出错信息：" + e.getMessage());
		}
		return "modules/ec/everyoneSayList";
	}
	
	/**
	 * 查看说说详情
	 * @param mtmyEveryoneSay
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(MtmyEveryoneSay mtmyEveryoneSay,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(mtmyEveryoneSay.getMtmyEveryoneSayId() != 0){
				MtmyEveryoneSay mtmyEveryoneSayQuest = mtmyEveryoneSayService.getMtmyEveryoneSay(mtmyEveryoneSay.getMtmyEveryoneSayId());
				Page<MtmyEveryoneSay> page = mtmyEveryoneSayService.getMtmyEveryoneSayForResponse(new Page<MtmyEveryoneSay>(request, response), mtmyEveryoneSay);
				model.addAttribute("page", page);
				model.addAttribute("mtmyEveryoneSayQuest", mtmyEveryoneSayQuest);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转查看说说详情页面", e);
			logger.error("跳转查看说说详情页面出错信息：" + e.getMessage());
		}
		return "modules/ec/everyoneSayForm";
	}
	
	/**
	 * 逻辑删除说说
	 * @param mtmyEveryoneSay
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String delete(MtmyEveryoneSay mtmyEveryoneSay,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			mtmyEveryoneSayService.delMtmyEveryoneSay(mtmyEveryoneSay.getMtmyEveryoneSayId());
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除说说", e);
			logger.error("方法：delete，删除说说出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除说说失败");
		}
		return "redirect:" + adminPath + "/ec/everyoneSay/list";
	}
	
	/**
	 * 物理删除说说对应的回 
	 * @param mtmyEveryoneSay
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delResponse")
	@ResponseBody
	public String delResponse(MtmyEveryoneSay mtmyEveryoneSay,HttpServletRequest request){
		try{
			mtmyEveryoneSayService.deleteResponse(mtmyEveryoneSay.getParentId(), mtmyEveryoneSay.getMtmyEveryoneSayId());
			return "success";
		}catch (Exception e) {
			BugLogUtils.saveBugLog(request, "物理删除说说对应的回 ", e);
			logger.error("物理删除说说对应的回 出错信息：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 修改说说是否显示
	 * @param mtmyEveryoneSay
	 * @param request
	 * @return
	 */
	@RequestMapping(value="updateIsShow")
	@ResponseBody
	public String updateIsShow(MtmyEveryoneSay mtmyEveryoneSay,HttpServletRequest request){
		try{
			mtmyEveryoneSayService.updateIsShow(mtmyEveryoneSay);
			return "SUCCESS";
		}catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改说说是否显示 ", e);
			logger.error("修改说说是否显示 出错信息：" + e.getMessage());
			return "ERROR";
		}
	}
}
