package com.training.modules.train.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.Department;
import com.training.modules.train.entity.Position;
import com.training.modules.train.service.DepartmentService;

/**
 * 
 * @className departmentController
 * @description TODO 部门管理
 * @author chenbing
 * @date 2017年11月14日 兵子
 *
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/department")
public class DepartmentController extends BaseController{
	
	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * 
	 * @Title: list
	 * @Description: TODO 查询全部部门，以及根据部门名称查询
	 * @param department
	 * @param model
	 * @param request
	 * @param response
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月14日 兵子
	 */
	@RequestMapping(value = { "list", "" })
	public String list(Department department,Model model,HttpServletRequest request, HttpServletResponse response){
		Page<Department> page = departmentService.findDepartment(new Page<Department>(request, response), department);
		model.addAttribute("page", page);
		return "modules/train/departmentList";
	}
	
	/**
	 * 
	 * @Title: form
	 * @Description: TODO 查看、修改、添加页面跳转
	 * @param department
	 * @param model
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月16日 兵子
	 */
	@RequiresPermissions(value={"train:department:add","train:department:edit","train:department:view"})
	@RequestMapping(value="form")
	public String form(Department department,Model model){
		if (department.getdId() != null) {
			department =  departmentService.getDepartment(department);
		}
		model.addAttribute("department", department);
		return "modules/train/departmentForm";
	}
	
	/**
	 * 
	 * @Title: verificationName
	 * @Description: TODO 验证部门名称
	 * @param name
	 * @param model
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月15日 兵子
	 */
	@ResponseBody
	@RequestMapping(value="checkName")
	public String checkName(String oldName,String name){
		if (name != null && name.equals(oldName)) {
			return "true";
		} else if (name != null && departmentService.getDepartmentName(name) == null) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 
	 * @Title: save
	 * @Description: TODO 保存部门
	 * @param department
	 * @param model
	 * @param oldName
	 * @param redirectAttributes
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月15日 兵子
	 */
	@RequestMapping(value="save")
	public String save(Department department,Model model,String oldName,RedirectAttributes redirectAttributes,
			HttpServletRequest request,HttpServletResponse response){
		try {
			if(Global.isDemoMode()){
				addMessage(redirectAttributes, "演示模式，不允许操作！");
				return "redirect:" + adminPath + "/train/department/list?repage";
			}
			if (!"true".equals(checkName(oldName, department.getName()))){
				addMessage(model, "保存部门'" + department.getName() + "'失败, 部门已存在");
				return form(department, model);
			}
			departmentService.saveDepartment(department);
			addMessage(redirectAttributes, "保存部门成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校角色", e);
			logger.error("保存部门错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/department/list?repage";
	}
	
	/**
	 * 
	 * @Title: deleteAll
	 * @Description: TODO 删除部门
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月16日 兵子
	 */
	@RequiresPermissions("train:department:del")
	@RequestMapping(value="deleteAll")
	public String deleteAll(String ids,RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response){
		try {
			if (StringUtils.isNotBlank(ids)) {
				departmentService.deleteDepartment(ids);
				addMessage(redirectAttributes, "删除成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除部门", e);
			logger.error("保存部门错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/department/list?repage";
	}
	
	/**
	 * 
	 * @Title: addPosition
	 * @Description: TODO 添加职位页面跳转
	 * @param department
	 * @param model
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年11月16日 兵子
	 */
	@RequiresPermissions("train:position:view")
	@RequestMapping(value="positionList")
	public String positionList(Department department,Model model){
		List<Position> positionList = Lists.newArrayList();
		if (department.getdId() != null) {
			positionList = departmentService.getPosition(department);
		}
		model.addAttribute("positionList", positionList);
		model.addAttribute("department", department);
		return "modules/train/positionList";
	}
}
