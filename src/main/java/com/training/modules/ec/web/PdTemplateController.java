package com.training.modules.ec.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.PdTemplate;
import com.training.modules.ec.service.PdTemplateService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 活动
 * 
 * @author dalong
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/pdTemplate")
public class PdTemplateController extends BaseController {

	@Autowired
	private PdTemplateService templateService;
	@ModelAttribute
	public PdTemplate get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return templateService.get(id);
		} else {
			return new PdTemplate();
		}
	}

	/**
	 * 分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = {"ec:wareHouse:view"})
	@RequestMapping(value = { "list", "" })
	public String list(PdTemplate template, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<PdTemplate> page = templateService.findTemplateList(new Page<PdTemplate>(request, response), template);
			String wareHouseId = request.getParameter("houseId");
			model.addAttribute("page", page);
			model.addAttribute("wareHouseId", wareHouseId);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "获取模板列表页信息错误", e);
			logger.error("获取模板列表页信息错误:"+e.getMessage());
		}
		return "modules/ec/templateList";
	}
	
	@RequiresPermissions(value = {"ec:wareHouse:add","ec:wareHouse:edit"})
	@RequestMapping(value = "form")
	public String form(PdTemplate template, HttpServletRequest request, Model model){
		try {
			List<PdTemplate> templates = getAreaJson(request);
			model.addAttribute("template", template);
			model.addAttribute("templates",templates);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "模板添加页面获取信息错误", e);
			logger.error("模板添加页面获取信息错误:"+e.getMessage());
		}
		return "modules/ec/templateForm";
	}
	/**
	 * 获取地区
	 * @return
	 */
	public List<PdTemplate> getAreaJson(HttpServletRequest request){
		List<PdTemplate> provinceList = new ArrayList<PdTemplate>();
		try {
			PdTemplate cPrice = new PdTemplate();
			cPrice.setType("2");
			provinceList = templateService.getAreaList(cPrice);//省得地区集合
			if(provinceList!=null && provinceList.size() > 0){
				for (PdTemplate province : provinceList) {
					province.setType("3");
					List<PdTemplate> cityList = templateService.getAreaList(province);//某一个省下得城市集合
					province.setCityList(cityList);
				}
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "获取地区错误", e);
			logger.error("获取地区错误："+e.getMessage());
		}
		return provinceList;
	}
	
	/**
	 * 保存模板
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(PdTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			templateService.saveTemplate(template);
			addMessage(redirectAttributes, "保存模板成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存模板失败！");
			BugLogUtils.saveBugLog(request, "保存模板错误", e);
			logger.error("保存模板:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/pdTemplate/list?houseId="+template.getHouseId();
	}
	/**
	 * 获取修改页面得数据
	 * @param template
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "modifiedPage")
	public String modifiedPage(PdTemplate template, Model model, HttpServletRequest request) {
		try {
			List<PdTemplate> templates = getAreaJson(request);
			PdTemplate _template = templateService.modifiedPage(template.getTemplateId());
			_template.setHouseId(template.getHouseId());
			model.addAttribute("template", _template);
			model.addAttribute("templates",templates);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "获取模板修改页面数据错误", e);
			logger.error("获取模板修改页面数据错误:"+e.getMessage());
		}
		return "modules/ec/templateModifiedPage";
	}
	/**
	 * 修改模板
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "update")
	public String update(PdTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			templateService.update(template);
			addMessage(redirectAttributes, "修改模板成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "修改模板失败！");
			BugLogUtils.saveBugLog(request, "修改模板错误", e);
			logger.error("修改模板错误:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/pdTemplate/list?houseId="+template.getHouseId();
	}
	/**
	 * 删除模板
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = {"ec:wareHouse:del"})
	@RequestMapping(value = "delete")
	public String delete(PdTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			templateService.deleteTemplate(template);
			addMessage(redirectAttributes, "删除模板成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除模板失败！");
			BugLogUtils.saveBugLog(request, "删除模板错误", e);
			logger.error("删除模板错误:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/pdTemplate/list?houseId="+template.getHouseId();
	}
	/**
	 * 复制模板
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "copy")
	public String copy(PdTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			templateService.copy(template);
			addMessage(redirectAttributes, "复制模板成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "复制模板失败！");
			BugLogUtils.saveBugLog(request, "复制模板错误", e);
			logger.error("复制模板错误:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/pdTemplate/list?houseId="+template.getHouseId();
	}
	/**
	 * 修改为通用模板方法
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "updateStatus")
	public String updateStatus(PdTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			templateService.updateStatus(template);
			addMessage(redirectAttributes, "设置通用模板成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "复制模板失败！");
			BugLogUtils.saveBugLog(request, "设置通用模板错误", e);
			logger.error("设置通用模板错误:"+e.getMessage());
		}
		
		return "redirect:" + adminPath + "/ec/pdTemplate/list?houseId="+template.getHouseId();
	}
}
