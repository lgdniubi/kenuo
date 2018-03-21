package com.training.modules.ec.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.StarBeauty;
import com.training.modules.ec.entity.StarBeautyMapping;
import com.training.modules.ec.service.StarBeautyService;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 明星技师自由配置表Controller
 * @author 土豆   2018-3-7
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/starBeauty")
public class StarBeautyController extends BaseController{

	@Autowired
	private StarBeautyService starBeautyService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private FranchiseeService franchiseeService;
	
	/**
	 * 查看明星技师组列表
	 * @param tabBackground
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(StarBeauty starBeauty,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<StarBeauty> page = starBeautyService.findPage(new Page<StarBeauty>(request, response), starBeauty);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看明星技师组列表失败!", e);
			logger.error("查看明星技师组列表失败：" + e.getMessage());
		}
		
		return "modules/ec/starBeautyList";
	}
	
	/**
	 * 查询/跳转  明星技师组
	 * @param starBeauty
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("form")
	public String form(StarBeauty starBeauty,Model model,HttpServletRequest request){
		try {
			//查询商家保护功能中的所有商家
			Franchisee franchisee = new Franchisee();
			franchisee.setIsRealFranchisee("1");
			List<Franchisee> list = franchiseeService.findList(franchisee);
			model.addAttribute("list", list);
			
			if(StringUtils.isNotEmpty(starBeauty.getId())){
				starBeauty = starBeautyService.getStarBeautyById(starBeauty);
				model.addAttribute("starBeauty", starBeauty);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据明星技师组id查询form失败!", e);
			logger.error("根据明星技师组id查询form失败：" + e.getMessage());
		}
		return "modules/ec/starBeautyForm";
	}
	
	/**
	 * 添加/修改  明星技师组
	 * @param starBeauty
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("save")
	public String save(StarBeauty starBeauty,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			User user = UserUtils.getUser();
			int isOpen = starBeauty.getIsOpen();//商家保护是否公开
			if(StringUtils.isEmpty(starBeauty.getId())){//添加
				if(isOpen != 0){//如果isOpen=0,商家id为空;isOpen是商家id,修改为1,并且填写商家id
					starBeauty.setFranchiseeId(starBeauty.getIsOpen()+"");
					starBeauty.setIsOpen(1);
				}
				
				starBeauty.setCreateBy(user);
				starBeautyService.saveStarBeauty(starBeauty);
				addMessage(redirectAttributes, "添加 明星技师组成功！");
			}else{ //修改
				starBeauty.setUpdateBy(user);
				starBeautyService.updateStarBeauty(starBeauty);
				addMessage(redirectAttributes, "修改 明星技师组成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存明星技师组失败!", e);
			logger.error("保存明星技师组失败：" + e.getMessage());
		}
		return"redirect:" + adminPath + "/ec/starBeauty/list";
	}
	
	/**
	 * 删除  明星技师组
	 * @param starBeauty
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("del")
	public Map<String, String> del(StarBeauty starBeauty,Model model,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			//删除记录
			starBeautyService.delStarBeauty(starBeauty);
			map.put("STATUS", "OK");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除明星技师组失败!", e);
			logger.error("删除明星技师组失败：" + e.getMessage());
			map.put("STATUS", "ERROR"); 
			map.put("MESSAGE", "删除失败");
		}
		return map;
	}
	
	/**
	 * 修改状态-->只有一个会显示.
	 * @param starBeauty
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "updateType")
	@ResponseBody
	public Map<String, String> updateType(StarBeauty starBeauty,RedirectAttributes redirectAttributes,HttpServletRequest request){
		Map<String, String> map = null;
		try {
			map = starBeautyService.updateType(starBeauty);
		} catch (Exception e) {
			logger.error("明星技师组修改状态错误信息："+e.getMessage());
			BugLogUtils.saveBugLog(request, "明星技师组修改状态失败", e);
			map = new HashMap<String, String>();
			map.put("STATUS", "ERROR");
			map.put("MESSAGE", "修改失败");
		}
		return map;
	}
	
	/**
	 * 根据明星技师组id查询明星技师列表
	 * @param tabBackground
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="starBeautyMappingList")
	public String starBeautyMappingList(StarBeautyMapping starBeautyMapping,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			String isShow = request.getParameter("isShow");//是否展示
			String franchiseeId = request.getParameter("franchiseeId");//商家保护的商家id
			Page<StarBeautyMapping> page = starBeautyService.findMappingPage(new Page<StarBeautyMapping>(request, response), starBeautyMapping);
			model.addAttribute("isShow", isShow);
			model.addAttribute("franchiseeId", franchiseeId);
			model.addAttribute("starId", starBeautyMapping.getStarId());
			model.addAttribute("num", page.getList().size());
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看明星技师组列表失败!", e);
			logger.error("查看明星技师组列表失败：" + e.getMessage());
		}
		
		return "modules/ec/starBeautyMappingList";
	}
	
	/**
	 * 查询/跳转  明星技师列表
	 * @param starBeauty
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("mappingform")
	public String mappingform(StarBeautyMapping starBeautyMapping,Model model,HttpServletRequest request, HttpServletResponse response){
		String starId = request.getParameter("starId");
		String franchiseeId = request.getParameter("franchiseeId");//商家保护的商家id
		try {
			model.addAttribute("franchiseeId", franchiseeId);
			model.addAttribute("user", new User());
			model.addAttribute("starId", starId);
			model.addAttribute("starBeautyMapping", starBeautyMapping);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据组id查询明星技师列表失败!", e);
			logger.error("根据组id查询明星技师列表失败：" + e.getMessage());
		}
		return "modules/ec/starBeautyMappingUpdateForm";
	}
	
	/**
	 * 查找符合条件的美容师
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="querySysUser")
	public String querySysUser(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			String starId = request.getParameter("starId");
			Page<User> page = systemService.starBeautyFindUser(new Page<User>(request, response), user);
			model.addAttribute("franchiseeId", user.getCompany().getId());
			model.addAttribute("starId", starId);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找符合条件的美容师", e);
			logger.error("查找符合条件的美容师出错信息：" + e.getMessage());
		}
		return "modules/ec/starBeautyMappingUpdateForm";
	}
	
	/**
	 * 保存选择的明星技师
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveStarBeautyMapping")
	public String StarBeautyMapping(StarBeautyMapping starBeautyMapping, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try{
			User user = UserUtils.getUser();
			if(starBeautyMapping.getMappingId() == 0){
				starBeautyMapping.setCreateBy(user);
				starBeautyService.saveStarBeautyMapping(starBeautyMapping);
				addMessage(redirectAttributes, "添加明星技师成功！");
				return "success";
			}else{//修改明星技师的信息
				starBeautyMapping.setUpdateBy(user);
				starBeautyService.updateStarBeautyMapping(starBeautyMapping);
				addMessage(redirectAttributes, "修改明星技师成功！");
				return "success";
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找符合条件的美容师", e);
			logger.error("查找符合条件的美容师出错信息：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 根据明星技师表id查询明星技师
	 * @param starBeautyMapping
	 * @return
	 */
	@RequestMapping("starBeautyMappingform")
	public String starBeautyMappingform(StarBeautyMapping starBeautyMapping, Model model, HttpServletRequest request, HttpServletResponse response){
		try {
			if(starBeautyMapping.getMappingId() != 0){
				starBeautyMapping = starBeautyService.getStarBeautyMappingByMappingId(starBeautyMapping);
			}
			model.addAttribute("starBeautyMapping", starBeautyMapping);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据明星技师组id查询form失败!", e);
			logger.error("根据明星技师组id查询form失败：" + e.getMessage());
		}
		return "modules/ec/starBeautyMappingForm";
	}
	
	/**
	 * 删除  明星技师
	 * @param starBeauty
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delStarBeautyMapping")
	public String delStarBeautyMapping(StarBeautyMapping starBeautyMapping,Model model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try{
			starBeautyService.delStarBeautyMapping(starBeautyMapping);
			addMessage(redirectAttributes, "删除明星技师成功！");
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除明星技师失败", e);
			logger.error("删除明星技师失败信息：" + e.getMessage());
			return "error";
		}
	}
}
