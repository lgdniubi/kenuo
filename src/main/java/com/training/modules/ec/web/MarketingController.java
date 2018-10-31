package com.training.modules.ec.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

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
		return "modules/ec/goMarketingPage";
	}
	
	/**
	 * 跳转到营销系统
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping("/goMarketSystem")
    public void testRedSSS(HttpServletResponse response) throws Exception{
        
        //获取当前登录用户的手机号
        String phoneNo = UserUtils.getUser().getMobile();
        //获取跳转营销工具系统的url
        String mkmUrl = ParametersFactory.getMtmyParamValues("marketing_goMkmSystem");
        

        response.setContentType( "text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<form name='paySubmit' method='post'  action='"+mkmUrl+"' >");
        out.println("<input type='hidden' name='phoneNo' value='"+phoneNo+"'>"); //如有多个，则写多个hidden即可
        out.println("</form>");
        out.println("<script>");
        out.println("  document.paySubmit.submit()");
        out.println("</script>");
    }
}
