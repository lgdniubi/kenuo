package com.training.modules.ec.web;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.training.modules.ec.entity.MtmySaleRelieve;
import com.training.modules.ec.entity.OrderGoods;
import com.training.modules.ec.entity.Orders;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.entity.UsersAccounts;
import com.training.modules.ec.service.CustomerService;
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
	@Autowired
	private CustomerService customerService;
	
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
	public String adduserindex(Users users,Model model) {
        do{
        	String str = getUserNickname();
            users.setNickname(str);
        }while(mtmyUsersService.findUserBynickName(users)>0);
        model.addAttribute("users", users);
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
	
		//校验昵称是否重复
		if(mtmyUsersService.findUserBynickName(users)>0){
			addMessage(redirectAttributes, "添加用户"+users.getName()+"失败,昵称重复");
		}else{
			users.setPassword(SystemService.entryptPassword(users.getPassword()));
			mtmyUsersService.addUsers(users);
			//新增用户时插入用户账目表
			mtmyUsersDao.insertAccounts(users);
			//新增用户时插入用户统计表
			mtmyUsersDao.insterSaleStats(users);
			//新增用户时插入新客表、新客日志表，统计绑定店铺、绑定的美容师
			customerService.saveCustomerFranchisee(users.getUserid(),UserUtils.getUser().getOffice().getId());
			addMessage(redirectAttributes, "添加用户"+users.getName()+"成功");
		}
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
	
	/**
	 * 根据用户ID，查询用户账户信息
	 * @param users
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:finduseraccounts"},logical=Logical.OR)
	@RequestMapping(value = "finduseraccounts")
	public String finduseraccounts(UsersAccounts usersAccounts,HttpServletRequest request,Model model){
		usersAccounts = mtmyUsersService.findUserAccounts(usersAccounts);
		model.addAttribute("usersAccounts", usersAccounts);
		return "modules/ec/userAccountsInfo";
	}
	
	/**
	 * 异常用户处理页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:mtmyuser:userexceptionpage"},logical=Logical.OR)
	@RequestMapping(value = "userexceptionpage")
	public String userexceptionpage(){
		return "modules/ec/userExceptions";
	}
	
	/**
	 * 异常用户查询
	 * @param users
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "finduserexception")
	public String finduserexception(HttpServletRequest request,Model model){
		String mobile = request.getParameter("mobile");
		model.addAttribute("mobile", mobile);
		if(null != mobile && mobile.trim().length() > 0){
			Users train_user = mtmyUsersService.findTrainsUserForMobile(mobile);
			Users mtmy_user = mtmyUsersService.findMtmyUserForMobile(mobile);
			
			model.addAttribute("train_user", train_user);
			model.addAttribute("mtmy_user", mtmy_user);
			
			if(null != mtmy_user){
				List<Orders> mtmyOrders = mtmyUsersDao.findMtmyOrderForUser(mtmy_user.getUserid());
				if(null != mtmyOrders && mtmyOrders.size() > 0){
					model.addAttribute("mtmyOrders", mtmyOrders);
				}
			}else{
				model.addAttribute("message", mobile+":没有查到到该用户");
			}
		}else{
			model.addAttribute("message", mobile+"：未查询到用户信息");
		}
		return "modules/ec/userExceptions";
	}
	
	/**
	 * 异常用户处理
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "userexceptionhandling")
	public String userexceptionhandling(HttpServletRequest request,Model model){
		String mobile = request.getParameter("mobile");
		model.addAttribute("mobile", mobile);
		if(null != mobile && mobile.trim().length() > 0){
			
			Users mtmy_user = mtmyUsersService.findMtmyUserForMobile(mobile);
			if(null != mtmy_user){
				if("Z".equals(mtmy_user.getLayer())){
					//1：非妃子校用户+Z身份+无成功订单，做物理删除每天美耶用户。
					mtmyUsersService.delMtmyUser(mtmy_user.getUserid());
				}else if("B".equals(mtmy_user.getLayer())){
					//2：非妃子校用户+B身份+无成功订单，先做删除关系操作（关系、邀请码），再物理删除每天美耶用户。
					MtmySaleRelieve sale = mtmyUsersService.findMtmySaleRelieve(mtmy_user.getUserid());
					if(null != sale){
						mtmyUsersService.delMtmyUserSaleInvitationcodes(sale.getInvitationCode());
						mtmyUsersService.delMtmyUserSaleRelations(mtmy_user.getUserid());
						mtmyUsersService.delMtmyUser(mtmy_user.getUserid());
					}
				}
				model.addAttribute("message", mobile+":处理完成");
			}else{
				model.addAttribute("message", mobile+":没有查到到该用户");
			}
		}else{
			model.addAttribute("message", "处理失败，必要参数为空");
		}
		return "modules/ec/userExceptions";
	}
	
	/**
	 * 生成昵称的方法
	 * @return
	 */
	public String getUserNickname(){
		//昵称生成
		String str = "mm";  
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		str += formatter.format(date);
        Random rand = new Random();  
        for(int i=0;i<5;i++){  
            int num = rand.nextInt(3);  
            switch(num){  
                case 0:  
                    char c1 = (char)(rand.nextInt(26)+'a');//生成随机小写字母   
                    str += c1;  
                    break;  
                case 1:  
                    char c2 = (char)(rand.nextInt(26)+'A');//生成随机大写字母   
                    str += c2;  
                    break;  
                case 2:  
                    str += rand.nextInt(10);//生成随机数字  
            }  
        }
        return str;
	}
}
