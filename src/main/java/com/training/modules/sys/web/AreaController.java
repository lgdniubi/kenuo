/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.web;

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
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.service.AreaService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;


/**
 * 区域Controller
 * 
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;
	
	@ModelAttribute("area")
	public Area get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return areaService.get(id);
		}else{
			return new Area();
		}
	}
	
	/**
	 * 首页
	 * @param area
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:area:list")
	@RequestMapping(value = {""})
	public String index(Area area,Model model){
		return "modules/sys/areaIndex";
	}
	
	/**
	 * 查询所有数据
	 * @param area
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:area:list")
	@RequestMapping(value = {"list"})
	public String list(Area area, Model model) {
		
		/*//查询所有或者查询第一条数据时
		if(area.getId() == null || "1".equals(area.getId())){
			area.setId("");
		}
		model.addAttribute("list", areaService.findAreaByParentIdsLike(area));*/
		return "modules/sys/areaLists";
	}

	/**
	 * 区域管理-查看/新增/修改
	 * @param area
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:area:view","sys:area:add","sys:area:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Area area, Model model) {
		if (area.getParent() !=null && area.getParent().getId() != null){
			area.setParent(areaService.get(area.getParent().getId()));
		}
		if(area.getId()==null || area.getId()==""){
			List<Area> l = areaService.findListByPID(area.getParentId());
			long size=0;
			if(l.size()==0){
				size=0;
				area.setCode(area.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
			}else{
				for (int i = 0; i < l.size(); i++) {
					Area e = l.get(i);
					size=Long.valueOf(e.getCode())+1;
				}
				area.setCode(String.valueOf(size));
			}
		}
		model.addAttribute("area", area);
		return "modules/sys/areaForm";
	}
	
	/**
	 * 区域管理-新增/修改
	 * @param area
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"sys:area:add","sys:area:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area/";
		}
		if (!beanValidator(model, area)){
			return form(area, model);
		}
		areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		return "redirect:" + adminPath + "/sys/area/";
	}
	
	/**
	 * 区域管理-删除
	 * @param area
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:area:del")
	@RequestMapping(value = "delete")
	public String delete(Area area, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/area/list";
		}
		areaService.delete(area);
		addMessage(redirectAttributes, "删除区域成功");
		return "redirect:" + adminPath + "/sys/area/list";
	}

	/**
	 * 左侧-区域树状加载
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**新的
	 * 左侧-区域树状加载
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "newTreeData")
	public List<Map<String, Object>> newTreeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> list = areaService.findAll();
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				map.put("code", "区域编码:("+e.getCode()+")");
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 通过父类id查询其子节点
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findListByPID")
	public List<Map<String, Object>> findListByPID(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Area a = new Area();
		a.setType("3");
		List<Area> list = areaService.findList(a);
		for (int i=0; i<list.size(); i++){
			Area e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 根据父类id查询子类数据
	 * 用于异步加载树形table
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findByPidforChild")
	public String findByPidforChild(@RequestParam(required=false) String id, HttpServletResponse response) {
		List<Area> areaList = Lists.newArrayList();
		Area area = new Area();
		area.setId(id);
		areaList = areaService.findByPidforChild(area);
		
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(areaList, jsonConfig);
		return json.toString();
	}
	
}
