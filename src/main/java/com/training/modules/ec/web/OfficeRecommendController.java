package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.OfficeRecommend;
import com.training.modules.ec.entity.OfficeRecommendMapping;
import com.training.modules.ec.service.OfficeRecommendService;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 店铺推荐Controller
 * @author xiaoye 2017年9月12日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/officeRecommend")
public class OfficeRecommendController extends BaseController{
	
	@Autowired
	private OfficeRecommendService officeRecommendService;
	@Autowired
	private FranchiseeService franchiseeService;
	
	/**
	 * 店铺推荐列表
	 * @param officeRecommend
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(OfficeRecommend officeRecommend,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			Page<OfficeRecommend> page = officeRecommendService.findList(new Page<OfficeRecommend>(request,response),officeRecommend);
			model.addAttribute("page",page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "店铺推荐列表", e);
			logger.error("店铺推荐列表出错信息：" + e.getMessage());
		}
		return "modules/ec/officeRecommendList";
	}
	
	/**
	 * 跳转编辑店铺推荐组
	 * @param officeRecommend
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(OfficeRecommend officeRecommend,HttpServletRequest request,Model model){
		try{
			Franchisee franchisee = new Franchisee();
			franchisee.setIsRealFranchisee("1");
			List<Franchisee> list = franchiseeService.findList(franchisee);
			
			if(officeRecommend.getOfficeRecommendId() != 0){
				officeRecommend = officeRecommendService.getOfficeRecommend(officeRecommend.getOfficeRecommendId());
				model.addAttribute("officeRecommend",officeRecommend);
			}
			model.addAttribute("list",list);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑店铺推荐组", e);
			logger.error("跳转编辑店铺推荐组出错信息：" + e.getMessage());
		}
		return "modules/ec/officeRecommendForm";
	}
	
	/**
	 * 保存铺推荐组
	 * @param officeRecommend
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(OfficeRecommend officeRecommend,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(officeRecommend.getOfficeRecommendId() == 0){
				officeRecommend.setCreateBy(UserUtils.getUser());
				officeRecommendService.insertOfficeRecommend(officeRecommend);
				addMessage(redirectAttributes, "添加推荐组成功！");
			}else{
				officeRecommend.setUpdateBy(UserUtils.getUser());
				officeRecommendService.updateOfficeRecommend(officeRecommend);
				addMessage(redirectAttributes, "修改推荐组成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存推荐组失败!", e);
			logger.error("保存推荐组失败：" + e.getMessage());
			addMessage(redirectAttributes,"保存推荐组失败");
		}
		return "redirect:" + adminPath + "/ec/officeRecommend/list";
	}
	
	/**
	 * 逻辑删除店铺推荐组
	 * @param officeRecommend
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String del(OfficeRecommend officeRecommend,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(officeRecommend.getOfficeRecommendId() != 0){
				officeRecommendService.deleteOfficeRecommend(officeRecommend.getOfficeRecommendId());
				addMessage(redirectAttributes, "删除成功");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除店铺推荐组失败!", e);
			logger.error("删除店铺推荐组失败：" + e.getMessage());
			addMessage(redirectAttributes, "删除失败");
		}
		return "redirect:" + adminPath + "/ec/officeRecommend/list";
	}
	
	/**
	 * 修改店铺推荐组是否显示
	 * @param officeRecommend
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="updateType")
	public String updateType(OfficeRecommend officeRecommend,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			officeRecommendService.changeAll(officeRecommend);
			if(officeRecommend.getOfficeRecommendId() != 0){
				if(officeRecommend.getIsShow() == 1){
					officeRecommendService.updateIsShow(officeRecommend);
				}
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "修改店铺推荐组是否显示", e);
			logger.error("修改店铺推荐组是否显示出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "修改状态失败");
		}
		return "redirect:" + adminPath + "/ec/officeRecommend/list";
	}
	
	/**
	 * 查看店铺推荐组以及推荐店铺
	 * @param officeRecommend
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="togetherForm")
	public String togetherForm(OfficeRecommend officeRecommend,OfficeRecommendMapping officeRecommendMapping,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			if(officeRecommend.getOfficeRecommendId() != 0){
				officeRecommend = officeRecommendService.getOfficeRecommend(officeRecommend.getOfficeRecommendId());
				officeRecommendMapping.setRecommendId(officeRecommend.getOfficeRecommendId());
				Page<OfficeRecommendMapping> page = officeRecommendService.findNewPage(new Page<OfficeRecommendMapping>(request, response), officeRecommendMapping);
				model.addAttribute("officeRecommend",officeRecommend);
				model.addAttribute("page",page);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看店铺推荐组以及推荐店铺失败!", e);
			logger.error("查看店铺推荐组以及推荐店铺失败：" + e.getMessage());
		}
		return "modules/ec/officeRecommendTogetherForm";
	}
	
	/**
	 * 店铺推荐组对应的店铺列表
	 * @param officeRecommend
	 * @param officeRecommendMapping
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="addOffice")
	public String addOffice(OfficeRecommend officeRecommend,OfficeRecommendMapping officeRecommendMapping,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			if(officeRecommend.getOfficeRecommendId() != 0){
				officeRecommendMapping.setRecommendId(officeRecommend.getOfficeRecommendId());
				Page<OfficeRecommendMapping> page = officeRecommendService.findNewPage(new Page<OfficeRecommendMapping>(request, response), officeRecommendMapping);
				String oldOfficeIds = officeRecommendService.selectOfficeId(officeRecommend.getOfficeRecommendId());
				model.addAttribute("page",page);
				model.addAttribute("officeRecommendMapping",officeRecommendMapping);
				model.addAttribute("oldOfficeIds",oldOfficeIds);
				model.addAttribute("franchiseeId", officeRecommend.getFranchiseeId());
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "店铺推荐组对应的店铺列表", e);
			logger.error("店铺推荐组对应的店铺列表出错信息：" + e.getMessage());
		}
		return "modules/ec/addOfficeList";
	}
	
	/**
	 * 跳转添加店铺推荐组中的店铺页面
	 * @param officeRecommendMapping
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="addOfficeForm")
	public String addOfficeForm(OfficeRecommendMapping officeRecommendMapping,HttpServletRequest request,Model model){
		try{
			if(officeRecommendMapping.getOfficeRecommendMappingId() != 0){
				officeRecommendMapping = officeRecommendService.getOfficeRecommendMapping(officeRecommendMapping.getOfficeRecommendMappingId());
			}
			model.addAttribute("officeRecommendMapping",officeRecommendMapping);
			model.addAttribute("franchiseeId", request.getParameter("franchiseeId"));
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转添加店铺推荐组中的店铺页面", e);
			logger.error("跳转添加店铺推荐组中的店铺出错信息：" + e.getMessage());
		}
		return "modules/ec/addOfficeForm";
	}
	
	/**
	 * 异步保存店铺
	 * @param officeRecommendMapping
	 * @param request
	 * @return
	 */
	@RequestMapping(value="saveOffice")
	@ResponseBody
	public String saveOffice(OfficeRecommendMapping officeRecommendMapping,HttpServletRequest request){
		String data = "";
		try{
			String flag = request.getParameter("flag");
			if("add".equals(flag)){
				officeRecommendService.insertOffice(officeRecommendMapping);
			}else if("edit".equals(flag)){
				officeRecommendService.updateOfficeMessage(officeRecommendMapping);
			}
			data = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "异步保存店铺", e);
			logger.error("异步保存店铺出错信息：" + e.getMessage());
			data = "error";
		}
		return data.toString();
	}
	
	/**
	 *  删除某一组推荐店铺组中的某个店铺
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delOffice")
	@ResponseBody
	public String delOffice(HttpServletRequest request){
		try{
			officeRecommendService.delOffice(Integer.valueOf(request.getParameter("officeRecommendMappingId")));
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除商品副标题对应的商品", e);
			logger.error("删除商品副标题对应的商品出错信息：" + e.getMessage());
			return "error";
		}
	}
	
}
