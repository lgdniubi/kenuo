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
import com.training.modules.ec.service.MtmyUsersService;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.SystemService;
import com.training.modules.sys.utils.BugLogUtils;

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
	public String getUserList(UserDetail userDetail, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			String keyword =userDetail.getKeyword();
			//判断是否为手机号
			if (null!=keyword && keyword.trim().length()==11 && StringUtils.isNumeric(keyword)) {
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
	public String getUserDetail(String userId, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			if ( null==userId ||userId.trim().length()<=0 ) {
				model.addAttribute("userId", userId);
			} else {
				// 取得用户详情
				UserDetail userDetail = userDetailService.get(userId);
				if (null!=userDetail) {
					userDetail.setUserId(userId);
					// 计算年龄
					Date birthday = userDetail.getBirthday();
					Integer age = BirthdayUtil.getAge(birthday);
					userDetail.setAge(age);
					// 取得操作日志
					UserOperatorLog log = new UserOperatorLog();
					log.setOperatorType("1");
					log.setUserId(userId);
					List<UserOperatorLog> logList = logService.findList(log);
					// 取得联系信息
					UserContactInfo contactInfo = contactInfoService.getUserContactInfo(userId);
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
					model.addAttribute("logList", logList);
					model.addAttribute("detail", userDetail);
					model.addAttribute("userId", userId);
				}else{
					//
					UserDetail nameEntity = userDetailService.getUserNickname(userId);
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
		if (null!=userId && userId.trim().length()>0) {
			try {
				UserDetail exists =  userDetailService.get(entity.getUserId());
				UserContactInfo exists2 = contactInfoService.get(entity.getUserId());
				UserOperatorLog log = new UserOperatorLog();
				if (null!=exists && null!=exists2 ) {
					try {
						userDetailService.updateMtmyUsers(entity);
						userDetailService.updateSingle(entity);
						contactInfoService.updateSingle(info);
						String detailChange = Comparison.compareObj(exists,entity);
						String infoChange = Comparison.compareObj(exists2,info);
						log.setUserId(userId);
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
						userDetailService.updateMtmyUsers(entity);
						contactInfoService.save(info);
						userDetailService.updateSingle(entity);
						String detailChange = Comparison.compareObj(exists,entity);
						log.setUserId(userId);
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
						userDetailService.updateMtmyUsers(entity);
						String infoChange = Comparison.compareObj(exists2,info);
						log.setUserId(userId);
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
						userDetailService.updateMtmyUsers(entity);
						log.setUserId(userId);
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
		return "redirect:"+adminPath+"/crm/user/userDetail/?userId="+userId;
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
	public String account(String userId,HttpServletRequest request, HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
	
		try {
			UsersAccounts account = new UsersAccounts();
			account.setUserId(Integer.valueOf(userId));
			UsersAccounts entity = mtmyUsersDao.findUserAccounts(account);
			int total = couponService.getAvaliableNumber(userId);
			model.addAttribute("coupon",total);
			model.addAttribute("account",entity);
			model.addAttribute("userId",userId);
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

