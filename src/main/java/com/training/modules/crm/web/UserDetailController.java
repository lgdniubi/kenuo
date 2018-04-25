package com.training.modules.crm.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.dao.AvaliableCouponDao;
import com.training.modules.crm.entity.AvaliableCoupon;
import com.training.modules.crm.entity.CrmDict;
import com.training.modules.crm.entity.UserContactInfo;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.entity.UserOperatorLog;
import com.training.modules.crm.service.CrmDictService;
import com.training.modules.crm.service.UserContactInfoService;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.crm.service.UserOperatorLogService;
import com.training.modules.crm.utils.BirthdayUtil;
import com.training.modules.crm.utils.Comparison;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.entity.UsersAccounts;
import com.training.modules.ec.service.CustomerService;
import com.training.modules.ec.service.MtmyUsersService;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * kenuo 
 * @description:用户详细信息
 * @author：sharp 
 * @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/user")
public class UserDetailController extends BaseController {

	@Autowired
	private  UserDetailService userDetailService;
	@Autowired
	private UserContactInfoService contactInfoService;
	@Autowired
	private UserOperatorLogService logService;
	@Autowired
	private CrmDictService crmDictService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private MtmyUsersService mtmyUsersService;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private AvaliableCouponDao couponService;
	@Autowired
	private CustomerService customerService;
	
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
	 * @param
	 * @return String
	 * @description
	 */
	@RequestMapping(value = "userList")
	public String getUserList(UserDetail userDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			//先获取当前操作人的归属商家
			User user = UserUtils.getUser();
			String franchiseeId = user.getCompany().getId();
			if(franchiseeId.equals("1") && userDetail != null && StringUtils.isEmpty(userDetail.getFranchiseeId())){
				userDetail.setFranchiseeId("1000001");//如果是登云级,默认"可诺"
				userDetail.setFranchiseeName("可诺丹婷");
			}else if(!franchiseeId.equals("1") && userDetail != null && StringUtils.isEmpty(userDetail.getFranchiseeId())){
				userDetail.setFranchiseeId(franchiseeId);//不是登云级,获取操作人的所属商家
				userDetail.setFranchiseeName(user.getCompany().getName());
			}
			model.addAttribute("isfranchisee", franchiseeId);//当前操作人是登云级,展示可选择商家
			
			String keyword =userDetail.getKeyword();
			//判断是否为手机号
			if (StringUtils.isNotEmpty(keyword)) {
				//用手机号无权限过滤去查	
				Page<UserDetail> page  = userDetailService.getUserWithoutScope(new Page<UserDetail>(request, response), userDetail);
				//如果查到一条
				if (page.getList().size()>0) {
					model.addAttribute("detail", userDetail);
					model.addAttribute("page", page);
				}
			}else{
				Page<UserDetail> page = userDetailService.getUserList(new Page<UserDetail>(request, response), userDetail);
				model.addAttribute("detail", userDetail);
				model.addAttribute("page", page);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			addMessage(model, "查询出现问题或者超时");
			e.printStackTrace();
		}
		return "modules/crm/userList";
	}

	/**
	 * @param userId
	 * @return UserDetail
	 * @description 根据userId获取用户详细信息
	 */
	// @RequiresPermissions("crm:userInfo:view")
	@RequestMapping(value = "userDetail")
	public String getUserDetail(String userId,String franchiseeId, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			if ( null==userId ||userId.trim().length()<=0 ) {
				model.addAttribute("userId", userId);
			} else {
				UserDetail uDetail = new UserDetail();
				uDetail.setUserId(userId);
				uDetail.setFranchiseeId(franchiseeId);
				
				//获取该用户的绑定店铺信息
				UserDetail officeDetail = userDetailService.getOfficeByDetail(uDetail);
				
				// 取得用户详情
				uDetail = userDetailService.get(uDetail);
				if (null!=uDetail) {
					if(null != officeDetail){//该用户的绑定店铺不存在,就不存
						uDetail.setBeautyId(officeDetail.getBeautyId());
						uDetail.setBeautyName(officeDetail.getBeautyName());
						uDetail.setOfficeId(officeDetail.getOfficeId());
						uDetail.setOfficeName(officeDetail.getOfficeName());
					}
					
					uDetail.setUserId(userId);
					// 计算年龄
					Date birthday = uDetail.getBirthday();
					Integer age = BirthdayUtil.getAge(birthday);
					uDetail.setAge(age);
					/*// 取得操作日志
					UserOperatorLog log = new UserOperatorLog();
					log.setOperatorType("1");
					log.setUserId(userId);
					List<UserOperatorLog> logList = logService.findList(log);*/
					// 取得联系信息
					UserContactInfo contactInfo = new UserContactInfo();
					contactInfo.setUserId(userId);
					contactInfo.setFranchiseeId(franchiseeId);
					contactInfo = contactInfoService.get(contactInfo);
					// 取得性格列表
					CrmDict entity = new CrmDict();
					entity.setType("character");
					List<CrmDict> character = crmDictService.findList(entity);
					// 取得星座
					entity.setType("constellation");
					List<CrmDict> constellation = crmDictService.findList(entity);
					// 取得婚姻状况
					entity.setType("is_marrige");
					List<CrmDict> isMarrige = crmDictService.findList(entity);
					// 取得房产情况
					entity.setType("is_estate");
					List<CrmDict> isEstate = crmDictService.findList(entity);
					// 取得汽车品牌
					entity.setType("car_brand");
					List<CrmDict> carBrand = crmDictService.findList(entity);
					// 取得客户职业
					entity.setType("occupation");
					List<CrmDict> occupation = crmDictService.findList(entity);
					// 取得月收入
					entity.setType("monthly_income");
					List<CrmDict> income = crmDictService.findList(entity);
					// 取得体重
					entity.setType("weight");
					List<CrmDict> weight = crmDictService.findList(entity);
					// 取得客户来源
					entity.setType("source");
					List<CrmDict> source = crmDictService.findList(entity);

					model.addAttribute("isMarrige", isMarrige);
					model.addAttribute("constellation", constellation);
					model.addAttribute("isEstate", isEstate);
					model.addAttribute("carBrand", carBrand);
					model.addAttribute("occupation", occupation);
					model.addAttribute("income", income);
					model.addAttribute("weight", weight);
					model.addAttribute("source", source);
					model.addAttribute("character", character);
					model.addAttribute("info", contactInfo);
					model.addAttribute("detail", uDetail);
					model.addAttribute("userId", userId);
					model.addAttribute("franchiseeId", franchiseeId);
				}else{
					//
					UserDetail nameEntity = userDetailService.getUserNickname(userId);
					if(null != officeDetail){//该用户的绑定店铺不存在,就不存
						nameEntity.setBeautyId(officeDetail.getBeautyId());
						nameEntity.setBeautyName(officeDetail.getBeautyName());
						nameEntity.setOfficeId(officeDetail.getOfficeId());
						nameEntity.setOfficeName(officeDetail.getOfficeName());
					}
					// 取得性格列表
					CrmDict entity = new CrmDict();
					entity.setType("character");
					List<CrmDict> character = crmDictService.findList(entity);
					// 取得星座
					entity.setType("constellation");
					List<CrmDict> constellation = crmDictService.findList(entity);
					// 取得婚姻状况
					entity.setType("is_marrige");
					List<CrmDict> isMarrige = crmDictService.findList(entity);
					// 取得房产情况
					entity.setType("is_estate");
					List<CrmDict> isEstate = crmDictService.findList(entity);
					// 取得汽车品牌
					entity.setType("car_brand");
					List<CrmDict> carBrand = crmDictService.findList(entity);
					// 取得客户职业
					entity.setType("occupation");
					List<CrmDict> occupation = crmDictService.findList(entity);
					// 取得月收入
					entity.setType("monthly_income");
					List<CrmDict> income = crmDictService.findList(entity);
					// 取得体重
					entity.setType("weight");
					List<CrmDict> weight = crmDictService.findList(entity);
					// 取得客户来源
					entity.setType("source");
					List<CrmDict> source = crmDictService.findList(entity);

					model.addAttribute("isMarrige", isMarrige);
					model.addAttribute("constellation", constellation);
					model.addAttribute("isEstate", isEstate);
					model.addAttribute("carBrand", carBrand);
					model.addAttribute("occupation", occupation);
					model.addAttribute("income", income);
					model.addAttribute("weight", weight);
					model.addAttribute("source", source);
					model.addAttribute("detail", nameEntity);
					model.addAttribute("character", character);
					model.addAttribute("userId", userId);
					model.addAttribute("franchiseeId", franchiseeId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BugLogUtils.saveBugLog(request, "用户详情", e);
			logger.error("用户详情：" + e.getMessage());
		}
		return "modules/crm/userInfo";
	}

	/**
	 * @param userDetail
	 * @return void
	 */
	@Transactional(readOnly= false)
	@RequestMapping(value = "saveDetail")
	public String saveUsetDetail(UserDetail entity,UserContactInfo info, HttpServletRequest request, HttpServletResponse response,Model model) {
		String userId = entity.getUserId();
		String franchiseeId = entity.getFranchiseeId();
		if (null!=userId && userId.trim().length()>0) {
			try {
				UserDetail exists =  userDetailService.get(entity);
				UserContactInfo exists2 = contactInfoService.get(info);
				UserOperatorLog log = new UserOperatorLog();
				if (null!=exists && null!=exists2 ) {
					try {
						if(!"".equals(entity.getOfficeId()) && entity.getOfficeId() != null){
							customerService.saveCustomerOfficeBrauty(Integer.valueOf(entity.getUserId()),entity.getOfficeId(),entity.getBeautyId());
						}
						userDetailService.updateSingle(entity);
						contactInfoService.updateSingle(info);
						String detailChange = Comparison.compareObj(exists,entity);
						String infoChange = Comparison.compareObj(exists2,info);
						log.setUserId(userId);
						log.setFranchiseeId(franchiseeId);
						log.setOperatorType("1");
						if ("未作修改".equals(infoChange)) {
							log.setContent(detailChange);
						}else if ("未作修改".equals(detailChange)) {
							log.setContent(infoChange);
						}else{
							log.setContent(infoChange+detailChange);
						}
						logService.save(log);
					} catch (Exception e) {
						addMessage(model, "保存失败");
						logger.debug(e.getMessage());
						e.printStackTrace();
					}
				}else if (null!=exists && null==exists2 ) {
					try {
						if(!"".equals(entity.getOfficeId()) && entity.getOfficeId() != null){
							customerService.saveCustomerOfficeBrauty(Integer.valueOf(entity.getUserId()),entity.getOfficeId(),entity.getBeautyId());
						}
						contactInfoService.save(info);
						userDetailService.updateSingle(entity);
						String detailChange = Comparison.compareObj(exists,entity);
						log.setUserId(userId);
						log.setFranchiseeId(franchiseeId);
						log.setOperatorType("1");
						log.setContent("修改用户详细信息"+detailChange);
						logService.save(log);
					} catch (Exception e) {
						addMessage(model, "保存失败");
						logger.debug(e.getMessage());
						e.printStackTrace();
					}
				}else if (null==exists && null!=exists2 ) {
					try {
						contactInfoService.updateSingle(info);
						userDetailService.save(entity);
						if(!"".equals(entity.getOfficeId()) && entity.getOfficeId() != null){
							customerService.saveCustomerOfficeBrauty(Integer.valueOf(entity.getUserId()),entity.getOfficeId(),entity.getBeautyId());
						}
						String infoChange = Comparison.compareObj(exists2,info);
						log.setUserId(userId);
						log.setFranchiseeId(franchiseeId);
						log.setOperatorType("1");
						log.setContent("创建新的用户详细记录;"+infoChange);
						logService.save(log);
					} catch (Exception e) {
						addMessage(model, "保存失败");
						logger.debug(e.getMessage());
						e.printStackTrace();
					}
				}else if(null==exists && null==exists2 ) {
					try {
						contactInfoService.save(info);
						userDetailService.save(entity);
						if(!"".equals(entity.getOfficeId()) && entity.getOfficeId() != null){
							customerService.saveCustomerOfficeBrauty(Integer.valueOf(entity.getUserId()),entity.getOfficeId(),entity.getBeautyId());
						}
						log.setUserId(userId);
						log.setFranchiseeId(franchiseeId);
						log.setOperatorType("1");
						log.setContent("创建新的用户详细记录");
						logService.save(log);
					} catch (Exception e) {
						addMessage(model, "保存失败");
						logger.debug(e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		return "redirect:"+adminPath+"/crm/user/userDetail/?userId="+userId+"&franchiseeId="+franchiseeId;
	}
	
	/**
	 * 获取基本资料中的操作记录
	 * @param log
	 */
	@RequestMapping("logDetail")
	public String logDetail(UserOperatorLog log, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			// 取得操作日志
			Page<UserOperatorLog> page = logService.findList(new Page<UserOperatorLog>(request, response), log);
			model.addAttribute("page", page);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			addMessage(model, "查询出现问题或者超时");
			e.printStackTrace();
		}
		return "modules/crm/logDetail";
	}
	
	/**
	 * 查找所属门店下的所有美容师
	 * @param
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "beautyTree")
	public List<Map<String, Object>> beautyTree(String officeId,HttpServletRequest request, HttpServletResponse response,
			Model model) {

		User user = new User();
		Office office = new Office();
		office.setId(officeId);
		user.setOffice(office);
		List<Map<String, Object>> mapList = Lists.newArrayList();
		
		Page<User> page = systemService.newFindUser(new Page<User>(request, response,-1), user);
		List<User> list = page.getList();
		
		for(int i = 0; i < list.size(); i++){
			User o = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", o.getId());
			map.put("name", o.getName());
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 添加会员界面跳转
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"crm:user:adduserindex"},logical=Logical.OR)
	@RequestMapping(value = "adduserindex")
	public String adduserindex(Users users,Model model) {
		do{
        	String str = getUserNickname();
        	users.setNickname(str);
        }while(mtmyUsersService.findUserBynickName(users)>0);
        model.addAttribute("users", users);
		return "modules/crm/addMtmyUser";
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
			customerService.saveCustomer(users.getUserid(),users.getOfficeId(),users.getBeautyId());
			addMessage(redirectAttributes, "添加用户"+users.getName()+"成功");
		}
		return "redirect:" + adminPath + "/crm/user/userList";
	}
	
	/**
	 * 客户账户状态
	 * @param 
	 * @return String
	 */
	@RequestMapping(value = "account")
	public String account(String userId, @RequestParam(value ="franchiseeId") String franchiseeId,HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
	
		try {
			UsersAccounts account = new UsersAccounts();
			account.setUserId(Integer.valueOf(userId));
			UsersAccounts entity = mtmyUsersDao.findUserAccounts(account);
			int total = couponService.getAvaliableNumber(userId);
			model.addAttribute("coupon",total);
			model.addAttribute("account",entity);
			model.addAttribute("userId",userId);
			model.addAttribute("franchiseeId",franchiseeId);
		} catch (NumberFormatException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return "modules/crm/userAccount";
	}
	
	/**
	 * 账户可用红包
	 * @param 
	 * @return String
	 */
	@RequestMapping(value = "couponList")
	public String couponList(AvaliableCoupon entity,HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
	
		try {
			List<AvaliableCoupon> avaliableList = couponService.findAvaliableList(entity);
			model.addAttribute("coupon",avaliableList);
		} catch (NumberFormatException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return "modules/crm/userCouponList";
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

