/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import java.util.List;
import java.util.Map;

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
import com.training.common.utils.Collections3;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.service.TrainMenuService;

/**
 * 角色Controller
 * 
 * @version 2013-12-05
 */
@Controller
@RequestMapping(value = "${adminPath}/train/role")
public class TrainRoleController extends BaseController {

	@Autowired
	private TrainMenuService trainMenuService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return trainMenuService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("train:role:list")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = trainMenuService.findRole(role);
		model.addAttribute("list", list);
		return "modules/train/roleList";
	}

	@RequiresPermissions(value={"train:role:view","train:role:add","train:role:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice()==null){
			//此处获取错误，获取的是用户的机构信息
			//role.setOffice(UserUtils.getUser().getOffice());
			//应该获取，角色的机构信息
			role.setOffice(officeService.getRolebyOff(role.getId()));
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", trainMenuService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/train/roleForm";
	}
	
	@RequiresPermissions("train:role:auth")
	@RequestMapping(value = "auth")
	public String auth(Role role, Model model) {
		if (role.getOffice()==null){
			role.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", trainMenuService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/train/roleAuth";
	}
	
	@RequiresPermissions(value={"train:role:assign","train:role:auth","train:role:add","train:role:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		if (!beanValidator(model, role)){
			return list(role, model);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return list(role, model);
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 英文名已存在");
			return list(role, model);
		}
		trainMenuService.saveRole(role);
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		return "redirect:" + adminPath + "/train/role/?repage";
	}
	
	@RequiresPermissions("train:role:del")
	@RequestMapping(value = "delete")
	public String delete(Role role, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
//		if (Role.isAdmin(id)){
//			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
////		}else if (UserUtils.getUser().getRoleIdList().contains(id)){
////			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
//		}else{
			trainMenuService.deleteRole(role);
			addMessage(redirectAttributes, "删除角色成功");
//		}
		return "redirect:" + adminPath + "/train/role/?repage";
	}
	
	/**
	 * 批量删除角色
	 */
	@RequiresPermissions("train:role:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		String idArray[] =ids.split(",");
		StringBuffer msg = new StringBuffer();
		for(String id : idArray){
			Role role = trainMenuService.getRole(id);
			if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
				msg.append( "越权操作，只有超级管理员才能修改["+role.getName()+"]数据！<br/>");
			}else{
				trainMenuService.deleteRole(role);
				msg.append( "删除角色["+role.getName()+"]成功<br/>");
				
			}
		}
		addMessage(redirectAttributes, msg.toString());
		return "redirect:" + adminPath + "/train/role/?repage";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:role:assign")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
//		List<User> userList = traintemService.findUser(new User(new Role(role.getId())));
		List<User> userList = trainMenuService.findRoleUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		return "modules/train/roleAssign";
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:role:assign")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
//		List<User> userList = traintemService.findUser(new User(new Role(role.getId())));
		List<User> userList = trainMenuService.findRoleUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/train/selectUserToRole";
	}
	
	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * @param officeId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = new User();
		user.setOffice(new Office(officeId));
		Page<User> page = trainMenuService.findUser(new Page<User>(1, -1), user);
		for (User e : page.getList()) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	/**
	 * 角色生成树状
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (Role r : trainMenuService.findAllRole()) {
			Map<String, Object> map = Maps.newHashMap();
				map.put("id", r.getId());
				map.put("name", r.getName());
				mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 角色分配 -- 从角色中移除用户
	 * @param userId
	 * @param roleId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("train:role:assign")
	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/role/assign?id="+roleId;
		}
		Role role = trainMenuService.getRole(roleId);
		User user = trainMenuService.getUser(userId);
		if (UserUtils.getUser().getId().equals(userId)) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		}else {
			if (user.getRoleList().size() <= 1){
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
			}else{
				Boolean flag = trainMenuService.outUserInRole(role, user);
				if (!flag) {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
				}else {
					addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
				}
			}		
		}
		return "redirect:" + adminPath + "/train/role/assign?id="+role.getId();
	}
	
	/**
	 * 角色分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("train:role:assign")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/role/assign?id="+role.getId();
		}
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = trainMenuService.assignUserToRole(role, trainMenuService.getUser(idsArr[i]));
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:" + adminPath + "/train/role/assign?id="+role.getId();
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && trainMenuService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && trainMenuService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}
}
