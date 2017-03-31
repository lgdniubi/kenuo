package com.training.modules.crm.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.CrmDict;
import com.training.modules.crm.service.CrmDictService;

/**    
* kenuo      
* @description：字典记录
* @author：sharp   
* @date：2017年3月7日            
*/
@Controller
@RequestMapping(value = "${adminPath}/crm/dict")
public class CrmDictController extends BaseController {

	@Autowired
	private CrmDictService dictService;
	
	@ModelAttribute
	public CrmDict get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return dictService.get(id);
		}else{
			return new CrmDict();
		}
	}
	
	@RequiresPermissions("crm:dict:list")
	@RequestMapping(value = {"list", ""})
	public String list(CrmDict crmDict, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> typeList = dictService.findTypeList();
		model.addAttribute("typeList", typeList);
        Page<CrmDict> page = dictService.findPage(new Page<CrmDict>(request, response), crmDict); 
        model.addAttribute("page", page);
		return "modules/crm/dictList";
	}

	@RequiresPermissions(value={"crm:dict:view","crm:dict:add","crm:dict:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CrmDict dict, Model model) {
		model.addAttribute("dict", dict);
		return "modules/crm/dictForm";
	}

	@RequiresPermissions(value={"crm:dict:add","crm:dict:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")//@Valid 
	public String save(CrmDict dict, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/crm/dict/?repage&type="+dict.getType();
		}
		if (!beanValidator(model, dict)){
			return form(dict, model);
		}
		dictService.save(dict);
		addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
		return "redirect:" + adminPath + "/crm/dict/?repage&type="+dict.getType();
	}
	
	@RequiresPermissions("crm:dict:del")
	@RequestMapping(value = "delete")
	public String delete(CrmDict dict, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/crm/dict/?repage";
		}
		dictService.delete(dict);
		model.addAttribute("dict", dict);
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/crm/dict/?repage&type="+dict.getType();
	}
	
	
	/**
	 * 批量删除角色
	 */
	@RequiresPermissions("crm:role:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/crm/dict/?repage";
		}
		String idArray[] =ids.split(",");
		for(String id : idArray){
			CrmDict dict = dictService.get(id);
			dictService.delete(dict);
		}
		addMessage(redirectAttributes, "删除字典成功");
		return "redirect:" + adminPath + "/crm/dict/?repage";
	}

	/**
	 * @param 
	 * @return List<Map<String,Object>>
	 * @description
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		CrmDict dict = new CrmDict();
		dict.setType(type);
		List<CrmDict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			CrmDict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e.getParentId());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
	
	@ResponseBody
	@RequestMapping(value = "listData")
	public List<CrmDict> listData(@RequestParam(required=false) String type) {
		CrmDict dict = new CrmDict();
		dict.setType(type);
		return dictService.findList(dict);
	}
	
	@ResponseBody
	@RequestMapping(value = "dictTree")
	public List<Map<String, Object>> dictTree(@RequestParam(required=false) String type, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		CrmDict dict = new CrmDict();
		dict.setType(type);
		dict.getPage().setOrderBy("sort");
		List<CrmDict> list = dictService.findList(dict);
		for (int i=0; i<list.size(); i++){
			CrmDict e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getValue());
			map.put("name", StringUtils.replace(e.getLabel(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

}
