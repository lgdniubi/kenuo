package com.training.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.CookieUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.SpecBeautician;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.service.SpecBeauticianService;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;


/**
 * 特殊美容师管理
 * @author 小叶  2016-12-29 
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/specBeautician")
public class SpecBeauticianController extends BaseController{

	@Autowired
	private SpecBeauticianService specBeauticianService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 分页查询特殊美容师
	 * @param specBeautician
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:specBeautician:list")
	@RequestMapping(value="list")
	public String list(SpecBeautician specBeautician, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			//若不是从重定向进来的，则清除cookie中的数据
			if(!"1".equals(request.getParameter("removeCookie")) && request.getParameter("removeCookie") != "1"){
				CookieUtils.getCookie(request, response, "specBeauticianCookie","/", true);
			}
			//若是从查询列表页进来的，则将查询条件保存到cookie
			if(!"".equals(request.getParameter("cookieData")) && request.getParameter("cookieData") != null){
				CookieUtils.setCookie(response, "specBeauticianCookie",request.getParameter("cookieData"),60*30);
			}
			
			Page<SpecBeautician> page = specBeauticianService.findList(new Page<SpecBeautician>(request, response), specBeautician);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "特殊美容师列表", e);
			logger.error("特殊美容师列表出错信息：" + e.getMessage());
		}
		return "modules/sys/specBeauticianList";
	}
	
	/**
	 * 新增特殊美容师
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"sys:specBeautician:add"})
	@RequestMapping(value="form")
	public String form(Model model,HttpServletRequest request){
		try{
			model.addAttribute("user", new User());
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转新增特殊美容师页面", e);
			logger.error("跳转新增特殊美容师页面出错信息：" + e.getMessage());
		}
		return "modules/sys/specBeauticianForm";
	}
	
	/**
	 * 保存特殊美容师
	 * @param specBeautician
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = {"sys:specBeautician:add"})
	@RequestMapping(value = "save")
	public String save(SpecBeautician specBeautician, HttpServletRequest request, Model model,RedirectAttributes redirectAttributes) {
		try{
			String ids = request.getParameter("ids");
			String[] arry = ids.split(",");
			for(int i = 0; i < arry.length; i++){
				if(specBeauticianService.get(arry[i]) != null){
					break;
				}
				//获取当前登录用户的office的id
				User nowUser = UserUtils.getUser();
				specBeautician.setOfficeId(nowUser.getOffice().getId());
				specBeautician.setCreateBy(nowUser);
				
				//根据得到的美容师的id，获取该美容师的信息赋值给其对应的特殊美容师的属性
				specBeautician.setUserId(arry[i]);
				User user = systemService.getUser(arry[i]);
				specBeautician.setUserName(user.getName());
				specBeautician.setUserPhone(user.getMobile());
				
				//根据得到的美容师的id，获取其所属市场，所属店铺
				Office office = officeService.selectForSpec(arry[i]);
				//判断当前用户时属于店铺还是市场，如果是店铺，则既保存店铺又保存市场，否则只保存店铺
				if("1".equals(office.getGrade())){
					specBeautician.setBazaarId(office.getParent().getId());
					specBeautician.setShopId(office.getId());
				}else{
					specBeautician.setBazaarId(office.getId());
				}
				specBeauticianService.newDeleteSpecBeautician(specBeautician);
				specBeauticianService.insertSpecBeautician(specBeautician);
			}
			addMessage(redirectAttributes, "保存特殊美容师成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存特殊美容师", e);
			logger.error("方法：save，保存特殊美容师出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存特殊美容师失败");
		}
		return "redirect:" + adminPath + "/sys/specBeautician/list";
	}
	
	/**
	 * 逻辑删除特殊美容师
	 * @param specBeautician
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:specBeautician:del")
	@RequestMapping(value = "delete")
	public String delete(SpecBeautician specBeautician, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		try{
			specBeauticianService.deleteSpecBeautician(specBeautician);
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除特殊美容师", e);
			logger.error("方法：delete，删除特殊美容师出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除特殊美容师失败");
		}
		return "redirect:" + adminPath + "/sys/specBeautician/list?removeCookie=1&"+CookieUtils.getCookie(request, "specBeauticianCookie");
	}
	
	/**
	 * 查找符合条件的美容师
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="listForSpecial")
	public String listForSpecial(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		try{
			Page<User> page = systemService.newFindUser(new Page<User>(request, response), user);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查找符合条件的美容师", e);
			logger.error("查找符合条件的美容师出错信息：" + e.getMessage());
		}
		return "modules/sys/specBeauticianForm";
	}

}
