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
import com.training.modules.train.dao.TrainMenuDao;
import com.training.modules.train.entity.PCMenu;
import com.training.modules.train.service.TrainMenuService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 菜单Controller
 * 
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/train/menu")
public class TrainMenuController extends BaseController {

	@Autowired
	private TrainMenuService trainMenuService;
	
	@Autowired
	private TrainMenuDao trainMenuDao;
	
	@ModelAttribute("pCMenu")
	public PCMenu get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return trainMenuService.getMenu(id);
		}else{
			return new PCMenu();
		}
	}

	@RequiresPermissions("train:menu:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<PCMenu> list = Lists.newArrayList();
		List<PCMenu> sourcelist = trainMenuService.findAllMenu();
		PCMenu.sortList(list, sourcelist, PCMenu.getRootId(), true);
        model.addAttribute("list", list);
		return "modules/train/trainMenuList";
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
		List<PCMenu> menu = trainMenuDao.findByPidforChild(id);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(menu, jsonConfig);
		return json.toString();
	}
	
	@RequiresPermissions(value={"train:menu:view","train:menu:add","train:menu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(@ModelAttribute("pCMenu")PCMenu menu, Model model) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new PCMenu(PCMenu.getRootId()));
		}
		menu.setParent(trainMenuService.getMenu(menu.getParent().getId()));
		// 获取排序号，最末节点排序号+30
		if (StringUtils.isBlank(menu.getId())){
			List<PCMenu> list = Lists.newArrayList();
			List<PCMenu> sourcelist = trainMenuService.findAllMenu();
			PCMenu.sortList(list, sourcelist, menu.getParentId(), false);
			if (list.size() > 0){
				menu.setSort(list.get(list.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("menu", menu);
		return "modules/train/trainMenuForm";
	}
	
	@RequiresPermissions(value={"train:menu:add","train:menu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(@ModelAttribute("pCMenu")PCMenu menu, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin()){
			addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
			return "redirect:" + adminPath + "/train/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/menu/";
		}
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		trainMenuService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:" + adminPath + "/train/menu/";
	}
	
	/**
	 * 验证 菜单名称是否重复
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkName")
	public boolean checkName(String name,String oldName,Integer parentId) {
		if (name.equals(oldName)) return true;
		boolean flag =trainMenuService.checkName(name,parentId) == 0;
		return flag;
	}
	
	@RequiresPermissions("train:menu:del")
	@RequestMapping(value = "delete")
	public String delete(@ModelAttribute("pCMenu")PCMenu menu, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/menu/";
		}
//		if (PCMenu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
			trainMenuService.deleteMenu(menu);
			addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + adminPath + "/train/menu/";
	}

	@RequiresPermissions("train:menu:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/menu/";
		}
//		if (PCMenu.isRoot(id)){
//			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
//		}else{
		String idArray[] =ids.split(",");
		for(String id : idArray){
			PCMenu menu = trainMenuService.getMenu(id);
			if(menu != null){
				trainMenuService.deleteMenu(trainMenuService.getMenu(id));
			}
		}
			
		addMessage(redirectAttributes, "删除菜单成功");
//		}
		return "redirect:" + adminPath + "/train/menu/";
	}
	@RequiresPermissions("user")
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/train/menuTree";
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(String parentId, Model model) {
		model.addAttribute("parentId", parentId);
		return "modules/train/menuTreeselect";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("train:menu:updateSort")
	@RequestMapping(value = "updateSort")
	public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/train/menu/";
		}
    	for (int i = 0; i < ids.length; i++) {
    		PCMenu menu = new PCMenu(ids[i]);
    		menu.setSort(sorts[i]);
    		trainMenuService.updateMenuSort(menu);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:" + adminPath + "/train/menu/";
	}
	
	/**
	 * isShowHide是否显示隐藏菜单
	 * @param extId
	 * @param isShowHidden
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<PCMenu> list = trainMenuService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			PCMenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				if(isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
