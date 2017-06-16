package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.web.BaseController;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;

import net.sf.json.JSONObject;

/**
 * 主页静态化Controller
 * @author xiaoye  2017年6月12日
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/staticIndex")
public class StaticIndexController extends BaseController{
	
	
	/**
	 * 静态主页预览
	 * @return
	 */
	@RequestMapping(value = "previews")
	public String previews(RedirectAttributes redirectAttributes,HttpServletRequest request,Model model){
		JSONObject jsonObject = null;
		try {
			String generateStaticIndex = ParametersFactory.getMtmyParamValues("generateStaticIndex");
			
			logger.info("##### web接口路径:"+generateStaticIndex);
			String url=generateStaticIndex;
			String parpm = "{}";
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
			if("200".equals(jsonObject.get("result"))){
				model.addAttribute("goal", jsonObject.get("data"));
			}else{
				addMessage(redirectAttributes, "静态主页预览失败:"+jsonObject.get("message"));
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:静态主页预览", e);
			logger.error("调用接口:静态主页预览错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "静态主页预览出现异常，请与管理员联系");
		}
		return "modules/ec/staticIndex";
	}
	
	/**
	 * 发布静态主页
	 * @return
	 */
	@RequestMapping(value = "publish")
	public String publish(RedirectAttributes redirectAttributes,HttpServletRequest request){
		JSONObject jsonObject = null;
		try {
			String publishStaticIndex = ParametersFactory.getMtmyParamValues("publishStaticIndex");
			
			logger.info("##### web接口路径:"+publishStaticIndex);
			String url=publishStaticIndex;
			String parpm = "{}";
			String result = WebUtils.postObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",message:"+jsonObject.get("message"));
			if("200".equals(jsonObject.get("result"))){
				addMessage(redirectAttributes, "发布成功");
			}else{
				logger.error("##### 发布静态主页失败：失败信息:"+jsonObject.get("message"));
				addMessage(redirectAttributes, "发布失败");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "调用接口:发布静态主页", e);
			logger.error("调用接口:发布静态主页错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "发布静态主页出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
}
