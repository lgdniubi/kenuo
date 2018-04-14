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
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.MediaMenuDao;
import com.training.modules.train.entity.MediaMenu;
import com.training.modules.train.service.MediaMenuService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 菜单Controller
 * 
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/train/mdmenu")
public class MediaMenuController extends BaseController {

	@Autowired
	private MediaMenuService mediaMenuService;
	
	@Autowired
	private MediaMenuDao mediaMenuDao;
	
	@ModelAttribute("mediaMenu")
	public MediaMenu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return mediaMenuService.getMenu(id);
		}else{
			return new MediaMenu();
		}
	}

	@RequiresPermissions("train:mdmenu:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<MediaMenu> list = Lists.newArrayList();
		List<MediaMenu> sourcelist = mediaMenuService.findAllMenu();
		MediaMenu.sortList(list, sourcelist, MediaMenu.getRootId(), true);
        model.addAttribute("list", list);
		return "modules/train/mediaMenuList";
	}
	/**
	 * 通过id加载子类
	 * @param id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public String getChildren(@RequestParam(required=false) String id, HttpServletResponse response) {
		List<MediaMenu> menu = mediaMenuDao.findByPidforChild(id);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(menu, jsonConfig);
		return json.toString();
	}
	
	@RequiresPermissions(value={"train:mdmenu:view","train:mdmenu:add","train:mdmenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MediaMenu menu, Model model) {
//		public String form(@ModelAttribute("pCMenu")MediaMenu menu, Model model) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new MediaMenu(MediaMenu.getRootId()));
		}
		menu.setParent(mediaMenuService.getMenu(menu.getParent().getId()));
		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())){
			List<MediaMenu> list = Lists.newArrayList();
			List<MediaMenu> sourcelist = mediaMenuService.findAllMenu();
			MediaMenu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0){
				menu.setSort(list.get(list.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "modules/train/mediaMenuForm";
	}
	
	@RequiresPermissions(value={"train:mdmenu:add","train:mdmenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MediaMenu menu, Model model, RedirectAttributes redirectAttributes) {
//		public String save(@ModelAttribute("pCMenu")MediaMenu menu, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin()){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/mdmenu/";
		}
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		mediaMenuService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:" + adminPath + "/train/mdmenu/";
	}
	
	@RequiresPermissions("train:mdmenu:del")
	@RequestMapping(value = "delete")
	public String delete(MediaMenu menu, RedirectAttributes redirectAttributes) {
//		public String delete(@ModelAttribute("pCMenu")MediaMenu menu, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/mdmenu/";
		}
//		if (PCMenu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
			mediaMenuService.deleteMenu(menu);
			addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + adminPath + "/train/mdmenu/";
	}

	@RequiresPermissions("train:mdmenu:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/mdmenu/";
		}
//		if (PCMenu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
		String idArray[] =ids.split(",");
		for(String id : idArray){
			MediaMenu menu = mediaMenuService.getMenu(id);
			if(menu != null){
				mediaMenuService.deleteMenu(mediaMenuService.getMenu(id));
			}
		}
			
		addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + adminPath + "/train/mdmenu/";
	}
//	@RequiresPermissions("user")
//	@RequestMapping(value = "tree")
//	public String tree() {
//		return "modules/train/menuTree";
//	}

//	@RequiresPermissions("user")
//	@RequestMapping(value = "treeselect")
//	public String treeselect(String parentId, Model model) {
//		model.addAttribute("parentId", parentId);
//		return "modules/train/menuTreeselect";
//	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("train:mdmenu:updateSort")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/mdmenu/";
		}
    	for (int i = 0; i < ids.length; i++) {
    		MediaMenu menu = new MediaMenu(ids[i]);
    		menu.setSort(sorts[i]);
    		mediaMenuService.updateMenuSort(menu);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + adminPath + "/train/mdmenu/";
	}
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
//	@RequiresPermissions("user")
//	@ResponseBody
//	@RequestMapping(value = "treeData")
//	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
//		List<Map<String, Object>> mapList = Lists.newArrayList();
//		List<MediaMenu> list = mediaMenuService.findAllMenu();
//		for (int i=0; i<list.size(); i++){
//			MediaMenu e = list.get(i);
//			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
//				if(isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")){
//					continue;
//				}
//				Map<String, Object> map = Maps.newHashMap();
//				map.put("id", e.getId());
//				map.put("pId", e.getParentId());
//				map.put("name", e.getName());
//				mapList.add(map);
//			}
//		}
//		return mapList;
//	}
}
