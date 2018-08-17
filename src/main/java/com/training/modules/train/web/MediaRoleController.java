package com.training.modules.train.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.training.modules.train.dao.MediaRoleDao;
import com.training.modules.train.entity.MediaRole;
import com.training.modules.train.entity.TrainModel;
import com.training.modules.train.service.MediaMenuService;
import com.training.modules.train.service.MediaRoleService;
import com.training.modules.train.service.TrainModelService;


/**
 * 自媒体角色管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/mdrole")
public class MediaRoleController extends BaseController{
	
	@Autowired
	private MediaRoleService mediaRoleService;
	@Autowired
	private MediaMenuService mediaMenuService;
	@Autowired
	private TrainModelService trainModelService;
	@Autowired
	private MediaRoleDao mediaRoleDao;
	
	/**
	 * PC角色list
	 * @param model
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:mdrole:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(Model model,MediaRole mediaRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			Page<MediaRole> page=mediaRoleService.findList(new Page<MediaRole>(request, response), mediaRole);
			model.addAttribute("page", page);
			model.addAttribute("mediaRole", mediaRole);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询PC角色", e);
			logger.error("查询PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/mediaRoleList";
	}
	/**
	 * PC角色详情
	 * @param model
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:mdrole:view", "train:mdrole:add", "train:mdrole:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,MediaRole mediaRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			String opflag = mediaRole.getOpflag();
			if(mediaRole.getRoleId() != 0){
				mediaRole = mediaRoleService.get(mediaRole);
			}else{
				mediaRole = new MediaRole();
			}
			List<TrainModel> modList = trainModelService.findDYModelList();	//查找登云的所有的版本类型
			model.addAttribute("mediaRole", mediaRole);
			model.addAttribute("modList", modList);
			model.addAttribute("opflag", opflag);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询PC角色详情", e);
			logger.error("查询PC角色详情错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/train/mediaRoleForm";
	}
	
	/**
	 * 保存角色
	 * @param model
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,MediaRole mediaRole,String oldEname,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			mediaRoleService.savemediaRole(mediaRole);
			addMessage(redirectAttributes, "保存/修改角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存PC角色", e);
			logger.error("保存PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/mdrole/list";
	}
	
	/**
	 * 删除角色
	 * @param model
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:mdrole:del"}, logical = Logical.OR)
	@RequestMapping(value = "delete")
	public String delete(Model model,MediaRole mediaRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			mediaRoleService.delete(mediaRole);
			mediaRoleService.deleteUserRoleForRoleId(mediaRole.getRoleId());
			addMessage(redirectAttributes, "删除角色成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除PC角色", e);
			logger.error("删除PC角色错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/mdrole/list";
	}
	/**
	 * 角色权限设置
	 * @param model
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:mdrole:auth"}, logical = Logical.OR)
	@RequestMapping(value = "auth")
	public String auth(Model model,MediaRole mediaRole,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			model.addAttribute("mediaMenu", mediaMenuService.findAllMenuByModid(mediaRole));
			model.addAttribute("mediaRole", mediaRoleService.findRoleMenu(mediaRole));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "PC角色权限设置", e);
			logger.error("PC角色权限设置错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/train/mdrole/list";
		}
		return "modules/train/mediaRoleAuth";
	}
	/**
	 * 查询所有菜单(未删除、显示)
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response,Integer modeid) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		MediaRole mdrole = new MediaRole();
		if (modeid != null){
			mdrole.setModeid(modeid);
		}
		List<MediaRole> list = mediaRoleDao.findList(mdrole);
		for (int i = 0; i < list.size(); i++) {
			MediaRole e = list.get(i);
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
	public List<Map<String, Object>> newTreeData(HttpServletResponse response,String mediaRoleIds) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<MediaRole> list = mediaRoleDao.findList(null);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = Maps.newHashMap();
			MediaRole e = list.get(i);
			map.put("id", e.getRoleId());
			map.put("pId", 0);
			map.put("name", e.getName());
			if (!"".equals(mediaRoleIds) && mediaRoleIds != null) {
				String[] pcids = mediaRoleIds.split(",");
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
	 * @param mediaRole
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveAuth")
	public String saveAuth(Model model,MediaRole mediaRole,String oldMenuIds,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(!oldMenuIds.equals(mediaRole.getMenuIds())){
				mediaRoleService.saveRoleMenu(mediaRole);
			}
			addMessage(redirectAttributes, "保存角色菜单权限成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存PC角色菜单权限", e);
			logger.error("保存PC角色菜单权限错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/mdrole/list";
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
	public boolean checkEnname(String oldEnname,String ename,Integer modeid) {
		if("sjgly".equals(ename) && modeid != null){
			if ( StringUtils.isBlank(oldEnname)){//如果是空表示添加
				return mediaRoleService.checkEnname(modeid) == 0;
			} else {//如果不是空，表示修改
				return mediaRoleService.checkEnname(modeid) <= 1;
			}
		}
		return true;
	}
	
	/**
	 * 验证 角色名称是否重复
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkRoleName")
	public boolean checkName(String name,String oldName,Integer modeid) {
		if (StringUtils.isNotBlank(name)){//如果是空表示添加
			if (StringUtils.isBlank(oldName)){//如果是空表示添加
				return mediaRoleService.checkRoleName(name,modeid)==0;
			}else {//如果不是空，表示修改
				return mediaRoleService.checkRoleName(name,modeid)<=1;
			}
		}
		return false;	
	}
}
