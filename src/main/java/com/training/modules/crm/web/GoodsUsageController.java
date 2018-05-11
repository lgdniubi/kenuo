package com.training.modules.crm.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.GoodsUsage;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.GoodsUsageService;
import com.training.modules.crm.service.UserDetailService;

/**
 * kenuo 产品使用记录相关
 * @author：sharp 
 * @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/goodsUsage")
public class GoodsUsageController extends BaseController {

	@Autowired
	private GoodsUsageService goodsUsageService;
	@Autowired
	private UserDetailService userDetailService;
	
	/**
	 * 默认返回用户信息
	 * @param 
	 * @return UserDetail
	 */
	@ModelAttribute
	public UserDetail  get(@RequestParam(required = false) String userId) {
		if (StringUtils.isNotBlank(userId)) {
			UserDetail detail=  userDetailService.getUserNickname(userId); 
		    return detail;
		} else {
			return new UserDetail();
		}
	}
	/**
	 * 查找用户使用记录
	 * @param userId
	 * @return list
	 */
	@RequestMapping(value = "list")
	public String consingn(GoodsUsage entity, HttpServletRequest request, HttpServletResponse response, Model model) {
			try {
				Page<GoodsUsage> page = goodsUsageService.getConsignList(new Page<GoodsUsage>(request, response), entity);
				model.addAttribute("page", page);
				model.addAttribute("detail",entity);
				model.addAttribute("userId", entity.getUserId());
				model.addAttribute("franchiseeId", entity.getFranchiseeId());
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		
		return "modules/crm/goodsUsageList";
	}

	/**
	 * 返回编辑使用记录页面
	 * @param GoodsUages实体类
	 * @return usageForm
	 */
	@RequestMapping(value = {"add"})
	public String editGoodsUsage(GoodsUsage entity, @RequestParam(value ="userId") String userId, @RequestParam(value ="franchiseeId") String franchiseeId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String usageId = entity.getUsageId();
		if (null == usageId || usageId.trim().length()<=0) {
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		} else {
			GoodsUsage result= goodsUsageService.get(usageId);
			model.addAttribute("goodsUsage", result);
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		}
		return "modules/crm/usageForm";
	}
	
	/**
	 * 返回编辑使用记录页面
	 * @param GoodsUages实体类
	 * @return usageForm
	 */
	@RequestMapping(value = {"update"})
	public String updateGoodsUsage(GoodsUsage entity, @RequestParam(value ="userId") String userId, @RequestParam(value ="franchiseeId") String franchiseeId,
			HttpServletRequest request, HttpServletResponse response, Model model) {

		String usageId = entity.getUsageId();
		if (null == usageId || usageId.trim().length()<=0) {
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		} else {
			GoodsUsage result= goodsUsageService.get(usageId);
			model.addAttribute("goodsUsage", result);
			model.addAttribute("userId", userId);
			model.addAttribute("franchiseeId", franchiseeId);
		}
		return "modules/crm/usageForm";
	}

	/**
	 * 保存记录
	 * @param 
	 * @return redirect+crm/goodsUsage/list
	 */
	@RequestMapping(value = "save")
	public String saveGoodsUsage(GoodsUsage entity, HttpServletRequest request, HttpServletResponse response, Model model) {

		String usageId;
		try {
			usageId = entity.getUsageId();
			if (null!=usageId&&usageId.trim().length()>0) {
				goodsUsageService.updateSigle(entity);
			}else{
				goodsUsageService.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:"+adminPath+"/crm/goodsUsage/list?userId="+entity.getUserId()+"&franchiseeId="+entity.getFranchiseeId();
	}
}
