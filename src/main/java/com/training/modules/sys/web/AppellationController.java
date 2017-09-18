package com.training.modules.sys.web;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.dao.AppellationDao;
import com.training.modules.sys.entity.Appellation;
import com.training.modules.sys.service.AppellationService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 称谓标签Controller
 * @author xiaoye   2017年9月15日
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/appellation")
public class AppellationController extends BaseController{

	@Autowired
	private AppellationService appellationService;
	@Autowired
	private AppellationDao appellationDao; 
	
	/**
	 * 称谓标签列表
	 * @param appellation
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Appellation appellation,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			Page<Appellation> page = appellationService.findPage(new Page<Appellation>(request, response), appellation);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "称谓标签列表", e);
			logger.error("称谓标签列表出错信息：" + e.getMessage());
		}
		return "modules/sys/appellationList";
	}
	
	/**
	 * 跳转编辑称谓标签页面
	 * @param appellation
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(Appellation appellation,HttpServletRequest request,Model model){
		try{
			if(appellation.getAppellationId() != 0){
				appellation = appellationService.getAppellation(appellation.getAppellationId());
			}
			model.addAttribute("appellation",appellation);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑称谓标签页面", e);
			logger.error("跳转编辑称谓标签页面出错信息：" + e.getMessage());
		}
		return "modules/sys/appellationForm";
	}
	
	/**
	 * 保存称谓标签
	 * @param appellation
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(Appellation appellation,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(appellation.getAppellationId() == 0){
				appellation.setCreateBy(UserUtils.getUser());
				appellationService.insertAppellation(appellation);
				addMessage(redirectAttributes, "添加成功！");
			}else{
				appellation.setUpdateBy(UserUtils.getUser());
				appellationService.updateAppellation(appellation);
				addMessage(redirectAttributes, "修改成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存称谓标签失败!", e);
			logger.error("保存称谓标签失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存称谓标签失败");
		}
		return "redirect:" + adminPath + "/sys/appellation/list";
	}
	
	/**
	 * 逻辑删除称谓标签组
	 * @param appellation
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String del(Appellation appellation,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			if(appellation.getAppellationId() != 0){
				appellationService.delAppellation(appellation.getAppellationId());
				addMessage(redirectAttributes,"删除成功");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除称谓标签失败!", e);
			logger.error("删除称谓标签失败：" + e.getMessage());
			addMessage(redirectAttributes,"删除称谓标签失败");
		}
		return "redirect:" + adminPath + "/sys/appellation/list";
	}
	
	/**
	 * 获取称谓标签JSON数据
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletRequest request){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		try{
			List<Appellation> list = appellationDao.findList();
			for (int i = 0; i < list.size(); i++) {
				Appellation e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getAppellationId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "获取称谓标签JSON数据", e);
			logger.error("获取称谓标签JSON数据出错信息：" + e.getMessage());
		}
		return mapList;
	}
}
