package com.training.modules.train.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.training.common.utils.IdGen;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.CategoryLesson;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.service.TrainCategorysService;
import com.training.modules.train.utils.ScopeUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 添加课程
 * @author kele
 * 2016年3月9日
 */
@Controller
@RequestMapping(value = "${adminPath}/train/categorys")
public class CategorysController extends BaseController{
	
	@Autowired
	private TrainCategorysService trainCategorysService;
	
	@RequestMapping(value = {"/"})
	public void categorys(){
		
	}
	
	/**
	 * 课程分类管理列表
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:findalllist"},logical=Logical.OR)
	@RequestMapping(value = {"findalllist", ""})
	public String findalllist(TrainCategorys trainCategorys,HttpServletRequest request, HttpServletResponse response, Model model){
		//默认加载父类为0的分类   当为搜索时跳过此赋值
		/*if(null!=trainCategorys.getName() || !"".equals(trainCategorys.getName())){
			trainCategorys.setParentId("0");
		}*/
		//添加数据权限
	//	trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
	// office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a"));	
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		List<TrainCategorys> list = Lists.newArrayList();
		List<TrainCategorys> sourcelist = trainCategorysService.findList(trainCategorys);
		TrainCategorys.sortList(list, sourcelist, TrainCategorys.getRootId(), true);
		if(list.size() == 0){
			model.addAttribute("list", sourcelist);
		}else{
			model.addAttribute("list", list);
		}
		model.addAttribute("trainCategorys", trainCategorys);
		return "modules/train/categorysList";
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
		TrainCategorys trainCategorys = new TrainCategorys();
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));

		trainCategorys.setParentId(id);//根据一级分类的ID找到二级分类
		List<TrainCategorys> listtow = trainCategorysService.findcategoryslist(trainCategorys);//二级分类
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(listtow, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 添加课程分类
	 * 查询1级分类
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:addcategorys","train:categorys:juniorcategorys"},logical=Logical.OR)
	@RequestMapping(value = {"addcategorys", ""})
	public String addcategorys(TrainCategorys trainCategorys,HttpServletRequest request, HttpServletResponse response, Model model){
		
		String categoryid = trainCategorys.getCategoryId();
		
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		
		List<TrainCategorys> list = new ArrayList<TrainCategorys>(); 
		if(null == categoryid || "".equals(categoryid)){
			trainCategorys.setPriority(1);
			//查询1级分类
			list = trainCategorysService.findcategoryslist(trainCategorys);
			model.addAttribute("list", list);
		}else{
			trainCategorys = trainCategorysService.get(trainCategorys.getCategoryId());
			model.addAttribute("trainCategorys", trainCategorys);
		}
		model.addAttribute("list", list);
		return "modules/train/addcategorys";
	}
	
	/**
	 * 查询“一级”课程分类
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"listone", ""})
	public String listone(TrainCategorys trainCategorys, HttpServletRequest request, HttpServletResponse response, Model model){
		trainCategorys.setPriority(1);//一级分类
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
				
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		model.addAttribute("listone", listone);
		return "modules/train/addcourse";
	}
	
	/**
	 * 异步查询“一级”课程分类
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"ajaxlistone", ""})
	public @ResponseBody Map<String, List<TrainCategorys>> ajaxlistone(TrainCategorys trainCategorys, HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String, List<TrainCategorys>> jsonMap = new HashMap<String, List<TrainCategorys>>();
		trainCategorys.setPriority(1);//一级分类
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
				
		List<TrainCategorys> listone = trainCategorysService.findcategoryslist(trainCategorys);
		jsonMap.put("listone",listone);
		return jsonMap;
	}
	/**
	 * 查询“二级”课程分类
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"listtow", ""})
	public @ResponseBody Map<String, List<TrainCategorys>> listtow(TrainCategorys trainCategorys, HttpServletRequest request, HttpServletResponse response){
		Map<String, List<TrainCategorys>> jsonMap = new HashMap<String, List<TrainCategorys>>();
		
		//添加数据权限
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));

		trainCategorys.setParentId(trainCategorys.getCategoryId());//根据一级分类的ID找到二级分类
		List<TrainCategorys> listtow = trainCategorysService.findcategoryslist(trainCategorys);//二级分类
		jsonMap.put("listtow",listtow);
		return jsonMap;
	}
	
	/**
	 * 
	 * @Title: newlisttow
	 * @Description: TODO 根据商家id一级分类
	 * @throws
	 * 2018年1月26日 兵子
	 */
	@ResponseBody
	@RequestMapping(value = "oneCategory")
	public List<TrainCategorys> oneCategory(TrainCategorys trainCategorys,String compId, HttpServletRequest request, HttpServletResponse response){
		trainCategorys.setPriority(1);//一级分类
		//添加数据权限
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));

		Franchisee franchisee = new Franchisee();
		franchisee.setId(compId);
		trainCategorys.setFranchisee(franchisee);
		List<TrainCategorys> listtow = trainCategorysService.findOneCategoryslist(trainCategorys);//二级分类
		return listtow;
	}
	
	/**
	 * 添加课程分类
	 * @param trainCategorys
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequiresPermissions(value={"train:categorys:savecategorys"},logical=Logical.OR)
	@RequestMapping(value = {"savecategorys", ""})
	public String savecategorys(TrainCategorys trainCategorys, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException{
		try {
			String categoryid = IdGen.uuid();
			String firstcategory = request.getParameter("firstcategory");
			String parentid;//父类ID
			int priority;//优先级（目前只有2级分类，因此只有 1 、2）
			trainCategorys.setCategoryId(categoryid);//类别ID	(Java自动生成ID)
			
			String parentvalue = request.getParameter("parentvalue");
			String categoryvalue = request.getParameter("categoryvalue");
			
			//添加数据权限
			User user = UserUtils.getUser();//获取当前登录用户
			String officeCode = user.getOffice().getCode();
			trainCategorys.setCreateuser(user.getId());
			trainCategorys.setOfficeCode(officeCode);
			
			if(null != parentvalue && "0".equals(parentvalue)
					&& null != categoryvalue && categoryvalue.length() > 1){
				//添加下级分类
				parentid = categoryvalue;
				priority = 2;
				trainCategorys.setCateType("0");//下级分类默认为公共属性。 分类类型（0：公共；1：定制）
			}else{
				//选择的二级分类，则父类ID为一级分类
				if(null != firstcategory && "2".equals(firstcategory)){
					parentid = request.getParameter("secondcategory");
					priority = 2;
					trainCategorys.setCateType("0");//下级分类默认为公共属性。 分类类型（0：公共；1：定制）
				}else{
					//选择的一级分类，则重新定义ID
					parentid = "0";//一级分类定义为0
					priority = 1;
				}
			}
			
			trainCategorys.setParentId(parentid);//父类ID
			trainCategorys.setPriority(priority);
			trainCategorys.setStatus(0);
			trainCategorysService.save(trainCategorys);//保存课程分类信息
			
			addMessage(redirectAttributes, "保存课程分类'" + trainCategorys.getName() + "'成功");
			
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存课程分类出现异常,请与管理员联系");
			logger.error("保存课程分类出现异常,异常信息为："+e.getMessage());
		}
		return "redirect:" + adminPath + "/train/categorys/findalllist";
	}
	
	/**
	 * 查看课程分类
	 * @param trainCategorys
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:form"},logical=Logical.OR)
	@RequestMapping(value = {"form", ""})
	public String form(TrainCategorys trainCategorys,HttpServletRequest request, Model model){
		
		String opflag = request.getParameter("opflag");
		trainCategorys.setPriority(1);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
		//查询1级分类
		List<TrainCategorys> list = trainCategorysService.findcategoryslist(trainCategorys);
		
		trainCategorys = trainCategorysService.get(trainCategorys.getCategoryId());
		model.addAttribute("trainCategorys", trainCategorys);
		model.addAttribute("list", list);
		model.addAttribute("opflag", opflag);
		return "modules/train/categorysForm";
	}
	
	/**
	 * 修改课程分类
	 * @param trainCategorys
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:updatecategorys"},logical=Logical.OR)
	@RequestMapping(value = {"updatecategorys", ""})
	public String updatecategorys(TrainCategorys trainCategorys, RedirectAttributes redirectAttributes){
		User user = UserUtils.getUser();//获取当前登录用户
		trainCategorys.setCreateuser(user.getId());
		//分类数据权限发生变化时   其分类下的课程、试题同时更新
		TrainCategorys oldtrainCategorys = trainCategorysService.get(trainCategorys.getCategoryId());
		//当为一级分类时  界面传过来的权限 为 Franchisee 对象    二级为 Office 对象
		if(trainCategorys.getPriority() == 1){
			if(!trainCategorys.getFranchisee().getId().equals(oldtrainCategorys.getOffice().getId())){
				trainCategorysService.syncUpdate(oldtrainCategorys.getOffice().getId(), trainCategorys.getFranchisee().getId());
			}
		}else{
			if(!trainCategorys.getOffice().getId().equals(oldtrainCategorys.getOffice().getId())){
				trainCategorysService.syncUpdate(oldtrainCategorys.getOffice().getId(), trainCategorys.getOffice().getId());
			}
		}
		trainCategorysService.updatecategorys(trainCategorys);
		addMessage(redirectAttributes, "修改课程分类成功");
		return "redirect:" + adminPath + "/train/categorys/findalllist";
	}
	
	/**
	 * 删除课程分类
	 * @param trainCategorys
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:deletecategorys"},logical=Logical.OR)
	@RequestMapping(value = {"deletecategorys", ""})
	public String deletecategorys(TrainCategorys trainCategorys, RedirectAttributes redirectAttributes){
		trainCategorysService.deletecategorys(trainCategorys);
		addMessage(redirectAttributes, "删除课程分类成功");
		return "redirect:" + adminPath + "/train/categorys/findalllist";
	}
	
	/**
	 * 批量删除课程分类
	 * @param trainCategorys
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:deletecategorys"},logical=Logical.OR)
	@RequestMapping(value = {"batchdeletecategorys", ""})
	public String batchdeletecategorys(String[] ids, RedirectAttributes redirectAttributes){
		if(null != ids && ids.length > 0){
			for (int i = 0; i < ids.length; i++) {
				TrainCategorys trainCategorys = new TrainCategorys(ids[i]);
				trainCategorysService.deletecategorys(trainCategorys);
			}
			addMessage(redirectAttributes, "批量删除课程分类成功");
		}
		return "redirect:" + adminPath + "/train/categorys/findalllist";
	}
	
	/**
	 * 获取课程categoryid JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String type,
			 @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		
		//添加数据权限
		TrainCategorys trainCategorys = new TrainCategorys();
//		trainCategorys = CategorysUtils.categorysFilter(trainCategorys);
		trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));

		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TrainCategorys> list = trainCategorysService.findcategoryslist(trainCategorys);
		for (int i=0; i<list.size(); i++){
			TrainCategorys e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getCategoryId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				if (type != null && "4".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 获取课程lessonid JSON数据。
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeDataLesson")
	public List<Map<String, Object>> treeDataLesson(@RequestParam(required=false) String type,String categoryId,
			 @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CategoryLesson> list = trainCategorysService.findCategoryLessonid(categoryId);
		for (int i=0; i<list.size(); i++){
			CategoryLesson e = list.get(i);
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getLessonId());
				map.put("pId", e.getCategoryId());
				map.put("name", e.getLessonName());
				if (type != null && ("3".equals(type)||"4".equals(type))){
					map.put("isParent", true);
				}
				mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 课程分类区域管理
	 * @param trainCategorys
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:categorys:officeAuth"},logical=Logical.OR)
	@RequestMapping(value = {"officeAuth", ""})
	public String officeAuth(TrainCategorys trainCategorys,Model model){
		model.addAttribute("officelist", UserUtils.getOfficeList());
		return "modules/train/officeAuth"; 
	}
	/**
	 * 修改分类状态
	 * @param flag
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateIsShow")
	public Map<String, String> updateIsShow(String categoryId,String isShow){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			int ISSHOW = Integer.parseInt(isShow);
			if(!StringUtils.isEmpty(categoryId) && (ISSHOW == 0 || ISSHOW == 1)){
				TrainCategorys trainCategorys = new TrainCategorys();
				trainCategorys.getSqlMap().put("dsf", ScopeUtils.dataScopeFilter("t","category"));
				trainCategorys.setParentId(categoryId);//根据一级分类的ID找到二级分类
				List<TrainCategorys> list = trainCategorysService.findcategoryslist(trainCategorys);//二级分类
				String categoryIds = categoryId;
				for (TrainCategorys categorys : list) {
					categoryIds = categoryIds + "," + categorys.getCategoryId();
				}
				TrainCategorys t = trainCategorysService.get(categoryId);
				// 当修改为展示时
				if(ISSHOW == 0){
					// 上级机构不为 0 
					if(!"0".equals(t.getParentId())){
						// 上级机构为 不展示
						TrainCategorys tt = trainCategorysService.get(t.getParentId());
						if(tt.getIsShow() == 1){
							categoryIds += "," + tt.getCategoryId();
						};
					}
				}
				String ids[] =categoryIds.split(",");
				trainCategorysService.updateIsShow(ids,ISSHOW);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
				jsonMap.put("CATEGORYIDS", categoryIds);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("分类管理-修改分类状态 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	/**
	 * 修改分类状态
	 * @param flag
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateIsOpen")
	public Map<String, String> updateIsOpen(String categoryId,String isOpen){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			int ISOPEN = Integer.parseInt(isOpen);
			if(!StringUtils.isEmpty(categoryId) && (ISOPEN == 0 || ISOPEN == 1)){
				String categoryIds = categoryId;
				String ids[] =categoryIds.split(",");
				trainCategorysService.updateIsOpen(ids,ISOPEN);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISOPEN", isOpen);
				jsonMap.put("CATEGORYIDS", categoryIds);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("分类管理-修改分类状态 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}
