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
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.ec.entity.ReturnedGoods;
import com.training.modules.ec.service.ReturnedGoodsService;

/**    
* kenuo      
* @author：sharp   
* @date：2017年3月27日            
*/
@Controller
@RequestMapping(value = "${adminPath}/crm/coustomerService")
public class CoustomerServiceController extends BaseController {

	@Autowired
	private ReturnedGoodsService returnedGoodsService;
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
	 * 查找用户售后
	 * @param userId
	 * @return "modules/crm/coustomerService"
	 */
	@RequestMapping(value = { "list", "" })
	public String getByUser(ReturnedGoods returnedGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReturnedGoods> page = returnedGoodsService.findListByUser(new Page<ReturnedGoods>(request, response),returnedGoods);
		model.addAttribute("page", page);
		model.addAttribute("userId",returnedGoods.getUserId());
		return"modules/crm/coustomerService";
	}
}
