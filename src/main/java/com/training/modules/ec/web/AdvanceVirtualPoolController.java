package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.AdvanceVirtualPool;
import com.training.modules.ec.service.AdvanceVirtualPoolService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 预付款虚拟池
 * @author 小叶  2017-1-10 
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/advanceVirtualPool")
public class AdvanceVirtualPoolController extends BaseController{
	@Autowired
	private AdvanceVirtualPoolService advanceVirtualPoolService;
	
	/**
	 * 预付款虚拟池列表
	 * @param advanceVirtualPool
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(AdvanceVirtualPool advanceVirtualPool, HttpServletRequest request, HttpServletResponse response, Model model){
		try{
			User user = UserUtils.getUser();
			advanceVirtualPool.setOfficeId(user.getOffice().getId());
			Page<AdvanceVirtualPool> page = advanceVirtualPoolService.findList(new Page<AdvanceVirtualPool>(request, response), advanceVirtualPool);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "预付款虚拟池列表", e);
			logger.error("预付款虚拟池列表出错信息：" + e.getMessage());
		}
		return "modules/ec/advanceVirtualPoolList";
	}
	
	/**
	 * 导出预付款虚拟池
	 * @param advanceVirtualPool
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:advanceVirtualPool:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(AdvanceVirtualPool advanceVirtualPool, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			User user = UserUtils.getUser();
			advanceVirtualPool.setOfficeId(user.getOffice().getId());
			String fileName = "预付款虚拟池数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<AdvanceVirtualPool> page = advanceVirtualPoolService.findList(new Page<AdvanceVirtualPool>(request, response,-1), advanceVirtualPool);
			new ExportExcel("预付款虚拟池数据", AdvanceVirtualPool.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出预付款虚拟池失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/advanceVirtualPool/list";
	}
}
