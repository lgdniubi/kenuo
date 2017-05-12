package com.training.modules.train.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.Collections3;
import com.training.common.web.BaseController;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.FzxRoleDao;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.service.FzxMenuService;
import com.training.modules.train.service.FzxRoleService;


/**
 * 妃子校角色管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/fzxRole")
public class fzxRoleController extends BaseController{
	
	@Autowired
	private FzxRoleService fzxRoleService;
	@Autowired
	private FzxMenuService fzxMenuService;
	@Autowired
	private FzxRoleDao fzxRoleDao;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	
	/**
	 * 妃子校角色list
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:fzxRole:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(Model model,FzxRole fzxRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<FzxRole> page=fzxRoleService.findList(new Page<FzxRole>(request, response), fzxRole);
			model.addAttribute("page", page);
			model.addAttribute("fzxRole", fzxRole);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校角色", e);
			logger.error("查询妃子校角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/fzxRoleList";
	}
	/**
	 * 妃子校角色详情
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxRole:view", "train:fzxRole:add", "train:fzxRole:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,FzxRole fzxRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(fzxRole.getRoleId() != 0){
				fzxRole = fzxRoleService.get(fzxRole);
			}else{
				fzxRole = new FzxRole();
			}
			model.addAttribute("fzxRole", fzxRole);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询妃子校角色详情", e);
			logger.error("查询妃子校角色详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/fzxRoleForm";
	}
	
	/**
	 * 保存角色
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,FzxRole fzxRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			fzxRoleService.saveFzxRole(fzxRole);
			addMessage(redirectAttributes, "保存/修改角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校角色", e);
			logger.error("保存妃子校角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxRole/list";
	}
	
	/**
	 * 删除角色
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxRole:del"}, logical = Logical.OR)
	@RequestMapping(value = "delete")
	public String delete(Model model,FzxRole fzxRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			List<User> list = fzxRoleService.findRoleUserAllList(fzxRole.getRoleId());	// 查询角色下所有用户
			fzxRoleService.delete(fzxRole);
			fzxRoleService.deleteUserRoleForRoleId(fzxRole.getRoleId());
			for (int i = 0; i < list.size(); i++) {
				redisClientTemplate.del("UTOKEN_"+list.get(i).getId());
			}
			addMessage(redirectAttributes, "删除角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除妃子校角色", e);
			logger.error("删除妃子校角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxRole/list";
	}
	/**
	 * 角色权限设置
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxRole:auth"}, logical = Logical.OR)
	@RequestMapping(value = "auth")
	public String auth(Model model,FzxRole fzxRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			model.addAttribute("fzxMenu", fzxMenuService.findAllList());
			model.addAttribute("fzxRole", fzxRoleService.findRoleMenu(fzxRole));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "妃子校角色权限设置", e);
			logger.error("妃子校角色权限设置错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/train/fzxRole/list";
		}
		return "modules/train/fzxRoleAuth";
	}
	/**
	 * 查询所有菜单(未删除、显示)
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<FzxRole> list = fzxRoleDao.findList(null);
		for (int i = 0; i < list.size(); i++) {
			FzxRole e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getRoleId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 保存角色菜单权限
	 * @param model
	 * @param fzxRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAuth")
	public String saveAuth(Model model,FzxRole fzxRole,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(fzxRole.getMenuIds())){
				fzxRoleService.saveRoleMenu(fzxRole);
			}
			addMessage(redirectAttributes, "保存角色菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校角色菜单权限", e);
			logger.error("保存妃子校角色菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxRole/list";
	}
	/**
	 * 分配用户
	 * @param model
	 * @param fzxRole
	 * @param oldMenuIds
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "assign")
	public String assign(Model model,User user,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<User> page = fzxRoleService.findRoleUser(new Page<User>(request, response), user);
			model.addAttribute("page", page);
			FzxRole fzxRole = new FzxRole();
			fzxRole.setRoleId(user.getFzxRole().getRoleId());
			fzxRole = fzxRoleService.get(fzxRole);
			model.addAttribute("page", page);
			model.addAttribute("user", user);
			model.addAttribute("fzxRole", fzxRole);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "分配用户列表", e);
			logger.error("分配用户列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/fzxRoleAssign";
	}
	/**
	 * 移除用户
	 * @param model
	 * @param userId
	 * @param fzxRoleId
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "outrole")
	public String outrole(Model model,String userId, int fzxRoleId,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			FzxRole fzxRole = new FzxRole();
			fzxRole.setRoleId(fzxRoleId);
			fzxRole = fzxRoleService.get(fzxRole);	// 查询妃子校角色
			User user = systemService.getUser(userId);
			if (UserUtils.getUser().getId().equals(userId)) {
				addMessage(redirectAttributes, "无法从角色【" + fzxRole.getName() + "】中移除用户【" + user.getName() + "】自己！");
			}else{
				fzxRoleService.outUserInRole(userId, fzxRoleId);
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + fzxRole.getName() + "】中移除成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "移除妃子校角色", e);
			logger.error("移除妃子校角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxRole/assign?fzxRole.roleId="+fzxRoleId;
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "usertoFzxRole")
	public String usertoFzxRole(FzxRole fzxRole, Model model,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			User user = new User();
			user.setFzxRole(fzxRole);
			List<User> userList = fzxRoleService.findRoleUserList(user);
			model.addAttribute("fzxRole", fzxRole);
			model.addAttribute("userList", userList);
			model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
			model.addAttribute("officeList", officeService.findAll());
			System.out.println(Collections3.extractToString(userList, "name", ","));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "角色分配 -- 打开角色分配对话框", e);
			logger.error("角色分配 -- 打开角色分配对话框错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/sys/selectUserToRole";
	}
	
	/**
	 * 角色分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "assignrole")
	public String assignRole(FzxRole fzxRole, String[] idsArr,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			StringBuilder msg = new StringBuilder();
			int newNum = 0;
			for (int i = 0; i < idsArr.length; i++) {
				User user =  systemService.getUser(idsArr[i]);
				FzxRole f = fzxRoleService.findUserFzxRole(idsArr[i]);
				if(user != null && (f == null || (!f.getFzxRoleIds().contains(String.valueOf(fzxRole.getRoleId()))))){
					redisClientTemplate.del("UTOKEN_"+idsArr[i]);
					FzxRole newFzxRole = new FzxRole();
					newFzxRole.setRoleId(fzxRole.getRoleId());
					newFzxRole.setId(idsArr[i]);
					fzxRoleDao.insertUserRole(newFzxRole);
					msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + fzxRole.getName() + "】！");
					newNum++;
				}
			}
			addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存角色分配", e);
			logger.error("保存角色分配框错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxRole/assign?fzxRole.roleId="+fzxRole.getRoleId();
	}
}
