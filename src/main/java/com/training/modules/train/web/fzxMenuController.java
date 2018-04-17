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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.config.Global;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.dao.FzxMenuDao;
import com.training.modules.train.entity.FzxMenu;
import com.training.modules.train.service.FzxMenuService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;


/**
 * 妃子校菜单管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/fzxMenu")
public class fzxMenuController extends BaseController{
	
	@Autowired
	private FzxMenuService fzxMenuService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;		//redis缓存Service
	@Autowired
	private FzxMenuDao FzxMenuDao;
	
	/**
	 * 妃子校菜单list
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:fzxMenu:list"},logical=Logical.OR)
	@RequestMapping(value = {"list", ""})
	public String list(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		List<FzxMenu> list = Lists.newArrayList();
		List<FzxMenu> sourcelist = fzxMenuService.findAllMenu();
		FzxMenu.sortList(list, sourcelist, FzxMenu.getRootId(), true);
        model.addAttribute("list", list);
		return "modules/train/fzxMenuList";
	}
	
	/**
	 * 
	 * @Title: getChildren
	 * @Description: TODO 根据id查询加载子类
	 * @param id
	 * @param response
	 * @return: String
	 * @throws
	 * 2017年10月16日
	 */
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public String getChildren(@RequestParam(required=false) Integer menuId) {
		List<FzxMenu> fzxMenus = FzxMenuDao.findByPidforChild(menuId);
		//转为json格式  menuDao.findByPidforChild(id);
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(fzxMenus, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 妃子校菜单详情
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxMenu:view", "train:fzxMenu:add", "train:fzxMenu:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(Model model,FzxMenu fzxMenu){
		//因为传递过来的数据只有id所以根据id去查询数据库将其放入菜单中
		if(fzxMenu !=null && fzxMenu.getMenuId() != null){
			fzxMenu = fzxMenuService.get(fzxMenu);
		}
		if (fzxMenu.getParent()==null||fzxMenu.getParent().getMenuId() ==null){
			//String id = fzxMenu.getRootId();
			fzxMenu.setParent(new FzxMenu(1));
		}
		fzxMenu.setParent(fzxMenuService.getFzxMenu(fzxMenu.getParent().getMenuId()));
		// 获取排序号，最末节点排序号+30
		if (fzxMenu.getMenuId() == null){
			List<FzxMenu> list = Lists.newArrayList();
			List<FzxMenu> sourcelist = fzxMenuService.findAllMenu();
			FzxMenu.sortList(list, sourcelist, fzxMenu.getParentId(), false);
			if (list.size() > 0){
				fzxMenu.setSort(list.get(list.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("fzxMenu", fzxMenu);
		return "modules/train/fzxMenuForm";
	}
	
	/**
	 * 保存菜单
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			if(fzxMenu != null){
					fzxMenuService.saveFzxMenu(fzxMenu);
					addMessage(redirectAttributes, "保存/修改菜单成功!");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存妃子校菜单", e);
			logger.error("保存妃子校菜单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxMenu/list";
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
	public String checkEnname(String oldEnname, String enname) {
		if (enname != null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname != null &&  fzxMenuService.checkEnname(enname) == 0) {
			return "true";
		}
		return "false";
	}
	/**
	 * 删除菜单
	 * @param model
	 * @param fzxMenu
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "train:fzxMenu:del"}, logical = Logical.OR)
	@RequestMapping(value = "delete")
	public String delete(Model model,FzxMenu fzxMenu,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			// 查询所有拥有该菜单的用户
			List<User> list = fzxMenuService.findUserByMenu(fzxMenu.getMenuId());
			if(fzxMenu.getMenuId() != 0){
				for (int i = 0; i < list.size(); i++) {
					redisClientTemplate.del("UTOKEN_"+list.get(i).getId());
				}
			}
			fzxMenuService.delete(fzxMenu);
			fzxMenuService.deleteRoleMenu(fzxMenu.getMenuId());
			addMessage(redirectAttributes, "删除菜单成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除妃子校菜单", e);
			logger.error("删除妃子校菜单错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/fzxMenu/list";
	}
	
	/**
	 * 
	 * @Title: treeData
	 * @Description: TODO 查询菜单树
	 * @param extId
	 * @param isShowHide
	 * @param response
	 * @return:
	 * @return: List<Map<String,Object>>
	 * @throws
	 * 2017年10月18日
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId,@RequestParam(required=false) String isShowHide, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<FzxMenu> list = fzxMenuService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			FzxMenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getMenuId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				if(isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")){
					continue;
				}
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getMenuId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 
	 * @Title: updateSort
	 * @Description: TODO 批量修改菜单排序
	 * @param ids
	 * @param sorts
	 * @param redirectAttributes
	 * @return:
	 * @return: String
	 * @throws
	 * 2017年10月18日
	 */
	@RequiresPermissions("train:fzxMenu:updateSort")
	@RequestMapping(value = "updateSort")
	public String updateSort(Integer[] menuIds, Integer[] sorts, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try {
			if(Global.isDemoMode()){
				addMessage(redirectAttributes, "演示模式，不允许操作！");
				return "redirect:" + adminPath + "/train/fzxMenu/";
			}
			fzxMenuService.updateMenuSort(menuIds,sorts);
			addMessage(redirectAttributes, "保存菜单排序成功!");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除妃子校菜单", e);
			logger.error("保存妃子校排序错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存菜单排序失败，请重新操作!");
		}
		return "redirect:" + adminPath + "/train/fzxMenu/";
	}
	
}
