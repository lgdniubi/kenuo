package com.training.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Speciality;
import com.training.modules.sys.service.SpecialityService;
import com.training.modules.sys.utils.UserUtils;

/**
 * 特长管理
 * @author zhnagyang
 * @version 2016-8-25 09:59:07
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/speciality")
public class SpecialityController extends BaseController {

	@Autowired
	private SpecialityService specialityService;

	/**
	 * 加载页面
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:speciality:list")
	@RequestMapping(value = { "list" })
	public String list(Speciality speciality, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Speciality> page = specialityService.findSpeciality(new Page<Speciality>(request, response), speciality);
		model.addAttribute("page", page);
		return "modules/sys/specialityList";
	}

	/**
	 * 编辑新增数据
	 * @param speciality
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "sys:speciality:view", "sys:speciality:add","sys:speciality:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Speciality speciality, Model model) {
		if(null != speciality.getId() && speciality.getId().length() > 0){
			speciality = specialityService.get(speciality.getId());
		}
		model.addAttribute("speciality", speciality);
		return "modules/sys/specialityForm";
	}

	/**
	 * 保存数据
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "sys:speciality:add", "sys:speciality:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(Speciality speciality, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		String currentUser = UserUtils.getUser().getName();
		String currentFranchisee = UserUtils.getUser().getCompany().getId();
		speciality.setCreateby(currentUser);
		speciality.setFranchiseeid(currentFranchisee);

		// 保存用户信息
		specialityService.saveSpeciality(speciality);
		addMessage(redirectAttributes, "保存特长'" + speciality.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/speciality/list";
	}

	/**
	 * 删除数据
	 * @param speciality
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:speciality:del")
	@RequestMapping(value = "delete")
	public String delete(Speciality speciality, RedirectAttributes redirectAttributes) {
		specialityService.deleteSpeciality(speciality);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + adminPath + "/sys/speciality/list?repage";
	}

	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type 类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
			@RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
			@RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Speciality> list = specialityService.findALLList();
		for (int i = 0; i < list.size(); i++) {
			Speciality e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()))) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)) {
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
}
