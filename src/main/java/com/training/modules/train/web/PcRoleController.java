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
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.dao.PcRoleDao;
import com.training.modules.train.entity.PcRole;
import com.training.modules.train.entity.TrainModel;
import com.training.modules.train.service.PcRoleService;
import com.training.modules.train.service.TrainMenuService;
import com.training.modules.train.service.TrainModelService;


/**
 * PC角色管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/pcRole")
public class PcRoleController extends BaseController{
	
	@Autowired
	private PcRoleService pcRoleService;
	@Autowired
	private TrainMenuService trainMenuService;
	@Autowired
	private TrainModelService trainModelService;
	@Autowired
	private PcRoleDao pcRoleDao;
	
	/**
	 * PC角色list
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:pcRole:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(Model model,PcRole pcRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<PcRole> page=pcRoleService.findList(new Page<PcRole>(request, response), pcRole);
			model.addAttribute("page", page);
			model.addAttribute("pcRole", pcRole);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询PC角色", e);
			logger.error("查询PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/pcRoleList";
	}
	/**
	 * PC角色详情
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:pcRole:view", "train:pcRole:add", "train:pcRole:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,PcRole pcRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(pcRole.getRoleId() != 0){
				pcRole = pcRoleService.get(pcRole);
			}else{
				pcRole = new PcRole();
			}
			List<TrainModel> modList = trainModelService.findList(new TrainModel());	//查找所有的版本类型
			model.addAttribute("pcRole", pcRole);
			model.addAttribute("modList", modList);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询PC角色详情", e);
			logger.error("查询PC角色详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/pcRoleForm";
	}
	
	/**
	 * 保存角色
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,PcRole pcRole,String oldEname,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			pcRoleService.savepcRole(pcRole);
			addMessage(redirectAttributes, "保存/修改角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存PC角色", e);
			logger.error("保存PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/pcRole/list";
	}
	
	/**
	 * 删除角色
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:pcRole:del"}, logical = Logical.OR)
	@RequestMapping(value = "delete")
	public String delete(Model model,PcRole pcRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			pcRoleService.delete(pcRole);
			pcRoleService.deleteUserRoleForRoleId(pcRole.getRoleId());
			addMessage(redirectAttributes, "删除角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除PC角色", e);
			logger.error("删除PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/pcRole/list";
	}
	/**
	 * 角色权限设置
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:pcRole:auth"}, logical = Logical.OR)
	@RequestMapping(value = "auth")
	public String auth(Model model,PcRole pcRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			model.addAttribute("pcMenu", trainMenuService.findAllMenuByModid(pcRole));
			model.addAttribute("pcRole", pcRoleService.findRoleMenu(pcRole));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "PC角色权限设置", e);
			logger.error("PC角色权限设置错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/train/pcRole/list";
		}
		return "modules/train/pcRoleAuth";
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
		List<PcRole> list = pcRoleDao.findList(null);
		for (int i = 0; i < list.size(); i++) {
			PcRole e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getRoleId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 查询所有菜单(未删除、显示)
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "newTreeData")
	public List<Map<String, Object>> newTreeData(HttpServletResponse response,String pcRoleIds) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<PcRole> list = pcRoleDao.findList(null);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			PcRole e = list.get(i);
			map.put("id", e.getRoleId());
			map.put("pId", 0);
			map.put("name", e.getName());
			if (!"".equals(pcRoleIds) && pcRoleIds != null) {
				String[] pcids = pcRoleIds.split(",");
				for (int j = 0; j < pcids.length; j++) {
					if (e.getRoleId() == Integer.valueOf(pcids[j])) {
						map.clear();
					}
				}
			}
			if (map.get("id") != null) {
				mapList.add(map);
			}
		}
		return mapList;
	}
	/**
	 * 保存角色菜单权限
	 * @param model
	 * @param pcRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAuth")
	public String saveAuth(Model model,PcRole pcRole,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(pcRole.getMenuIds())){
				pcRoleService.saveRoleMenu(pcRole);
			}
			addMessage(redirectAttributes, "保存角色菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存PC角色菜单权限", e);
			logger.error("保存PC角色菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/pcRole/list";
	}
	
	/**
	 * 验证英文名称是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, Integer oldModeid,Integer modeid) {
		if("sjgly".equals(oldEnname)){
			if (oldModeid == modeid){
				return "true";
			} else if (modeid != null &&  pcRoleService.checkEnname(modeid) != 0) {
				return "false";
			}
		}
		return "true";
	}
}
