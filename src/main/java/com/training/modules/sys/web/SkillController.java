package com.training.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.training.modules.sys.entity.Skill;
import com.training.modules.sys.service.SkillService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 技能标签管理
 * @author 小叶  2016.12.27
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/skill")
public class SkillController extends BaseController{
	
	@Autowired
	private SkillService skillService;

	/**
	 * 分页查询技能标签列表
	 * @param skill
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:skill:list")
	@RequestMapping(value="list")
	public String list(Skill skill, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			Page<Skill> page = skillService.findList(new Page<Skill>(request, response), skill);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "技能标签列表", e);
			logger.error("技能标签列表出错信息：" + e.getMessage());
		}
		return "modules/sys/skillList";
	}
	
	/**
	 * 编辑新增技能标签
	 * @param skill
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:skill:view", "sys:skill:add","sys:skill:edit"})
	@RequestMapping(value="form")
	public String form(Skill skill,Model model,HttpServletRequest request){
		try{
			if(skill.getSkillId() != 0){
				skill = skillService.get(String.valueOf(skill.getSkillId()));
			}
			model.addAttribute("skill", skill);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增技能标签页面", e);
			logger.error("跳转新增技能标签页面出错信息：" + e.getMessage());
		}
		return "modules/sys/skillForm";
	}
	
	/**
	 * 新增编辑技能标签
	 * @param skill
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "sys:skill:add", "sys:skill:edit" })
	@RequestMapping(value = "save")
	public String save(Skill skill, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try{
			skillService.saveSkill(skill);
			addMessage(redirectAttributes, "保存技能标签成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存技能标签", e);
			logger.error("方法：save，保存技能标签出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存技能标签失败");
		}
		return "redirect:" + adminPath + "/sys/skill/list";
	}
	
	/**
	 * 逻辑删除技能标签
	 * @param skill
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:skill:del")
	@RequestMapping(value = "delete")
	public String delete(Skill skill, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try{
			skillService.deleteSkill(skill);
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除技能标签", e);
			logger.error("方法：delete，删除技能标签出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除技能标签失败");
		}
		return "redirect:" + adminPath + "/sys/skill/list";
	}

	/**
	 * 获取机构JSON数据。(只查找显示的)
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Skill> list = skillService.findAllList();
		for (int i = 0; i < list.size(); i++) {
			Skill e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getSkillId());
				map.put("name", e.getName());
				
				mapList.add(map);
		}
		return mapList;
	}
	
		/**
		 * 获取机构JSON数据。(无论显示还是隐藏都查出来)
		 * @param response
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "newTreeData")
		public List<Map<String, Object>> newTreeData(HttpServletResponse response) {
			List<Map<String, Object>> mapList = Lists.newArrayList();
			List<Skill> list = skillService.newFindAllList();
			for (int i = 0; i < list.size(); i++) {
				Skill e = list.get(i);
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", e.getSkillId());
					map.put("name", e.getName());
					
					mapList.add(map);
			}
			return mapList;
	}
	
	/**
	 * 技能标签是否显示
	 * @param request
	 * @param equipmentLabel
	 * @return
	 */
	@RequestMapping(value = "changeIsShow")
	@ResponseBody
	public Map<String, String> changeIsShow(HttpServletRequest request,Skill skill) {
		
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isyesno = request.getParameter("isShow");
			if("1".equals(isyesno)){
				int sum = skillService.selectGoodsisOnSale(skill.getSkillId());
				if(sum == 0){
					skillService.updateIsShow(skill);
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISYESNO", isyesno);
				}else{
					jsonMap.put("STATUS", "NO");
				}
			}else if("0".equals(isyesno)){
				skillService.updateIsShow(skill);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISYESNO", isyesno);
			}
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改技能标签是否显示失败", e);
			logger.error("修改技能标签是否显示失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 验证技能标签名称是否存在
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name,HttpServletRequest request) {
		String type = "";
		try{
			if (name != null && name.equals(oldName)) {
				type = "true";
			} else if (name != null && skillService.getByName(name) <= 0) {
				type = "true";
			}else{
				type = "false";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "验证技能标签名称是否存在错误", e);
			logger.error("验证技能标签名称是否存在出错信息：" + e.getMessage());
		}
		return type;
	}
}
