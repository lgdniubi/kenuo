package com.training.modules.ec.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.web.BaseController;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/ec/kf")
public class KfController extends BaseController {
	
	/**
	 * 跳转到智齿客服系统
	 * @return
	 */
	@RequestMapping(value = "loginZcSystem")
	public String loginZcSystem(Model model) {
		
		String systemFlag = "mtmy";
		
		//获取当前登录用户的手机号
		String phoneNo = UserUtils.getUser().getMobile();
		
		//获取跳转智齿系统的url
		String zcUrl = ParametersFactory.getMtmyParamValues("zc_goZcSystem");
		zcUrl += "?phoneNo=";
		zcUrl += phoneNo;
		zcUrl += "&systemFlag=" + systemFlag;
		
		model.addAttribute("zcUrl", zcUrl);
		
		return "modules/ec/goKfPage";
	}
	
	/**
	 * 跳转到客服反馈与建议页面
	 * @return
	 */
	@RequestMapping(value = "goKfFeedbackSuggest")
	public String goKfFeedbackSuggest(Model model) {
		
		//获取跳转到客服反馈与建议页面的url
		String kefuFeedbackSuggestUrl = ParametersFactory.getMtmyParamValues("zc_feedbackSuggestList");
		
		model.addAttribute("kefuFeedbackSuggestUrl", kefuFeedbackSuggestUrl);
		return "modules/ec/goKefuFeedbackSuggest";
	}
}
