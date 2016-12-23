package com.training.modules.ec.web;


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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.service.MtmyUsersService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.service.OrdersService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.UserUtils;

/**
 * 每天每夜用户Controller
 * 
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyuser")
public class MtmyUsersController extends BaseController{
	
	@Autowired
	private MtmyUsersService mtmyUsersService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrderGoodsService ordergoodService;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	
	/**
	 * 查询所有用户数据
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:list"},logical=Logical.OR)
	@RequestMapping(value = "list")
	public String list(Users users,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<Users> page=mtmyUsersService.findPage(new Page<Users>(request, response), users);
		model.addAttribute("page", page);
		model.addAttribute("users", users);
		return "modules/ec/userList";
	}
	/**
	 * 异步加载用户手机号和座机
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:lookmobile"},logical=Logical.OR)
	@RequestMapping(value = "lookmobile" )
	public @ResponseBody Users lookmobile(Users users,HttpServletRequest request, HttpServletResponse response,Model model){
		users=mtmyUsersService.findUserById(users);
	//	System.out.println(users.getPhone()+"             "+users.getMobile());
		return users;
	}
	/**
	 * 修改用户电话或重置密码
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:lookmobile"},logical=Logical.OR)
	@RequestMapping(value = "updateUser" )
	public String updateUser(Users users,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		//获取当前用户id
		User currentUser = UserUtils.getUser();
		users.setId(currentUser.getId());
//		System.out.println("===== uid="+users.getUserid()+"   手机="+users.getMobile()+"   电话="+users.getPhone()+"    密码="+users.getPassword());
		if(users.getPassword().length() >= 6){
			users.setPassword(SystemService.entryptPassword(users.getPassword()));
		}
		mtmyUsersService.updateUser(users);
		if(users.getPassword() != null){
			addMessage(redirectAttributes, "重置用户密码成功");
		}else{
			addMessage(redirectAttributes, "修改用户信息成功");
		}
	//   重定向
		return "redirect:" + adminPath + "/ec/mtmyuser/list";
	}
	/**
	 * 冻结用户
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:lockUser"},logical=Logical.OR)
	@RequestMapping(value = "lockUser" )
	public String lockUser(Users users,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		//获取当前用户id
		User currentUser = UserUtils.getUser();
		users.setId(currentUser.getId());
		mtmyUsersService.lockUser(users);
		
		if(users.getIslock() == 0){
			addMessage(redirectAttributes, "用户"+users.getName()+"解除冻结成功");
		}else{
			addMessage(redirectAttributes, "用户"+users.getName()+"冻结成功");
		}
		return "redirect:" + adminPath + "/ec/mtmyuser/list";
	}
	/**
	 * 查询用户所有订单
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:lookOrderByUser"},logical=Logical.OR)
	@RequestMapping(value = "lookOrderByUser" )
	public String lookOrderByUser(Orders orders,HttpServletRequest request, HttpServletResponse response,Model model){
		Page<Orders> page = ordersService.findOrders(new Page<Orders>(request, response),orders);
        model.addAttribute("page",page);
        model.addAttribute("userid",orders.getUserid());
		return "modules/ec/userOrdersList";
	}
	/**
	 * 查询订单下的商品
	 * @param orderGoods
	 * @param id
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"ec:mtmyuser:userOrdersFrom"},logical=Logical.OR)
	@RequestMapping(value = "userOrdersFrom")
	public String goodslist(String orderid,Model model) {
		List<OrderGoods> orderlist=ordergoodService.orderlist(orderid);
		model.addAttribute("orderlist", orderlist);
		return "modules/ec/userOrdersFrom";
	}
	/**
	 * 异步加载订单收货地址
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions(value={"ec:mtmyuser:lookAddress"},logical=Logical.OR)
	@RequestMapping(value = "lookAddress" )
	public @ResponseBody Orders lookAddress(Orders orders,HttpServletRequest request, HttpServletResponse response,Model model){
		orders=ordersService.get(orders.getOrderid());
		return orders;
	}
	/**
	 * 添加会员界面跳转
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:adduserindex"},logical=Logical.OR)
	@RequestMapping(value = "adduserindex")
	public String adduserindex() {
		return "modules/ec/adduser";
	}
	/**
	 * 添加会员
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "adduser")
	public String adduser(Users users,HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
	
		users.setPassword(SystemService.entryptPassword(users.getPassword()));
		mtmyUsersService.addUsers(users);
		//新增用户时插入用户账目表
		mtmyUsersDao.insertAccounts(users);
		//新增用户时插入用户统计表
		mtmyUsersDao.insterSaleStats(users);
		addMessage(redirectAttributes, "添加用户"+users.getName()+"成功");
		return "redirect:" + adminPath + "/ec/mtmyuser/list";
	}
	/**
	 * 验证昵称是否存在
	 * @param users
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifynickname")
	public String verifynickname(Users users){
		int num = mtmyUsersService.findUserBynickName(users);
		if(num != 0){
			return "false";
		}else{
			return "true";
		}
	}
	/**
	 * 添加用户时验证手机号是否存在
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifymobile" )
	public String verifyMobile(Users users,String oldMobile,HttpServletRequest request, HttpServletResponse response,Model model){
		int num=mtmyUsersService.findUserBymobile(users);
		if(num != 0){
			if(users.getMobile().equals(oldMobile)){
				return "true";
			}else{
				return "false";
			}
		}else{
			return "true";
		}
	}
	/**
	 * 创建订单时根据手机号获取用户信息
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "verifyPhone" )
	public String verifyPhone(String mobile){
		
		if (mobile.length()>0){
			if(mtmyUsersService.getUserByPhone(mobile).size()>0) {
				return "true";
			}
			
		}
		return "false";
	}
	
	/**
	 * 通知--》查询用户信息
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "oaList")
	public Map<String, List<Users>> oaList(Users users,HttpServletRequest request, HttpServletResponse response,Model model) {
		Map<String,List<Users>> jsonMap=new HashMap<String, List<Users>>();
		List<Users> list = mtmyUsersService.treeFindList(users);
		jsonMap.put("list",list);
		return jsonMap;
	}
	/**
	 * 更新条件查询
	 * @param users
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "findAllBy")
	public List<Map<String, Object>> findAllBy(Users users,HttpServletRequest request, HttpServletResponse response,Model model) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Users> list = mtmyUsersService.findAllBy(users);
		for (int i=0; i<list.size(); i++){
			Users e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getUserid());
			map.put("name", e.getNickname()+"("+e.getMobile()+")");
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 健康档案（不做）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userarchives")
	public String userarchives(Model model) {
		return "modules/ec/userarchives";
	}
	/**
	 * 健康档案增加（不做）
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addrecord")
	public String addrecord(Model model) {
		return "modules/ec/addrecord";
	}
}
