package com.training.modules.ec.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.web.BaseController;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/ec/marketing")
public class MarketingController extends BaseController {
	
	/**
	 * 跳转到营销工具后台
	 * @return
	 */
	@RequestMapping(value = "loginMkm")
	public String loginMkm(Model model) {
		
		//获取当前登录用户的手机号
		String phoneNo = UserUtils.getUser().getMobile();
		
		//获取跳转营销工具系统的url
		String mkmUrl = ParametersFactory.getMtmyParamValues("marketing_goMkmSystem");
		mkmUrl += "?phoneNo=";
		mkmUrl += phoneNo;
		
		model.addAttribute("mkmUrl", mkmUrl);
		
		return "modules/ec/goMarketingPage";
	}
}
