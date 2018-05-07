/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.restlet.engine.util.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.config.Global;
import com.training.common.persistence.Page;
import com.training.common.security.Digests;
import com.training.common.security.shiro.session.SessionDAO;
import com.training.common.service.BaseService;
import com.training.common.service.ServiceException;
import com.training.common.track.utils.TrackUtils;
import com.training.common.utils.CacheUtils;
import com.training.common.utils.Encodes;
import com.training.common.utils.StringUtils;
import com.training.modules.ec.dao.MtmyUsersDao;
import com.training.modules.ec.entity.Users;
import com.training.modules.ec.utils.SaveLogUtils;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.DictDao;
import com.training.modules.sys.dao.MenuDao;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.dao.RoleDao;
import com.training.modules.sys.dao.SkillDao;
import com.training.modules.sys.dao.SpecialityDao;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.dao.UserinfoDao;
import com.training.modules.sys.dao.UserinfocontentDao;
import com.training.modules.sys.entity.Dict;
import com.training.modules.sys.entity.Menu;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.Skill;
import com.training.modules.sys.entity.Speciality;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.entity.UserLog;
import com.training.modules.sys.entity.UserOfficeCode;
import com.training.modules.sys.entity.UserSkill;
import com.training.modules.sys.entity.UserSpeciality;
import com.training.modules.sys.entity.UserVo;
import com.training.modules.sys.entity.Userinfo;
import com.training.modules.sys.entity.Userinfocontent;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.LogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.FzxRole;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private DictDao dictDao;
	//@Autowired
	//private SystemAuthorizingRealm systemRealm;
	@Autowired
	private UserinfoDao userinfoDao;
	@Autowired
	private SpecialityDao specialityDao;
	@Autowired
	private UserinfocontentDao userinfocontentDao;
	@Autowired
	private MtmyUsersDao mtmyUsersDao;
	@Autowired
	private SkillDao skillDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	// -- User Service --//

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	/**
	 * 验证身份证号码
	 * 
	 * @param idCard
	 * @return
	 */
	public User getUserByIdCard(String idCard) {
		return userDao.getUserByIdCard(idCard);
	}

	/**
	 * 验证工号
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByNO(String no) {
		return userDao.getByNO(no);
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public User getByMobile(String mobile) {
		return userDao.getByMobile(mobile);
	}

	/**
	 * 验证手机号
	 * 
	 * @param mobile
	 * @return
	 */
	public User getByNo(String no) {
		return userDao.getByNO(no);
	}
	/**
	 * 创建订单时根据手机号获取用户信息
	 * @param phone
	 * @return
	 */
	public List<User> getUserByPhone(String phone){
		return userDao.getUserByPhone(phone);
	}
	/**
	 * 保存用户操作日志
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUserLog(User user) {
		userDao.saveUserLog(user);
	}

	/**
	 * 根据用户查找用户完善信息
	 * 
	 * @param loginName
	 * @return
	 */
	public Userinfo getUserInfoByUserId(String userid) {
		Userinfo userinfo = userinfoDao.findByuserId(userid);
		List<Userinfocontent> infolist = new ArrayList<Userinfocontent>();
		if (userinfo != null) {

			if (userinfo.getInfocontlist() != null) {
				infolist = userinfo.getInfocontlist();
				if (userinfo.getInfocontlist().size() < 3) {
					int cursize = 3 - userinfo.getInfocontlist().size();
					for (int i = 0; i < cursize; i++) {
						Userinfocontent userinfocontent = new Userinfocontent();
						userinfocontent.setUserid("142512");
						userinfocontent.setName("生活图片");
						infolist.add(userinfocontent);
					}
					userinfo.setInfocontlist(infolist);
				}
			}

		}

		return userinfo;
	}

	/**
	 * 根据用户查找用户完善信息
	 * 
	 * @param loginName
	 * @return
	 */
	public Speciality getSpecialityByUserId(String userid) {
		String id = "";
		String name = "";
		Speciality speciality = new Speciality();
		List<Speciality> list = specialityDao.findlistByuserid(userid);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				id = id + list.get(i).getId() + ",";
				name = name + list.get(i).getName() + ",";
			}
			id = id.substring(0, id.lastIndexOf(","));
			name = name.substring(0, name.lastIndexOf(","));
			speciality.setId(id);
			speciality.setName(name);
		}

		return speciality;
	}

	/**
	 * 根据用户id获取该用户的技能标签
	 * @param userid
	 * @return
	 */
	public Skill getSkillByUserId(String userid){
		String id = "";
		String name = "";
		Skill skill = new Skill();
		List<Skill> list = skillDao.findSkillListByuserid(userid);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				id = id + list.get(i).getSkillId() + ",";
				name = name + list.get(i).getName() + ",";
			}
			id = id.substring(0, id.lastIndexOf(","));
			name = name.substring(0, name.lastIndexOf(","));
			skill.setId(id);
			skill.setName(name);
		}
		return skill;
	}
	
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		
		//2016-11-4 kele update 新的数据权限
		//user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		
		
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}
	
	/**
	 * 分页查询美容师的信息
	 * @param page
	 * @param user
	 * @return
	 */
	public Page<User> newFindUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		
		//2016-11-4 kele update 新的数据权限
		//user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		
		
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.newFindList(user));
		return page;
	}
	
	/**
	 * 明星技师使用的查询美容师方法(只是明星技师查询使用2018-3-8)
	 * @param page
	 * @param user
	 * @param isOpen 
	 * @return
	 */
	public Page<User> starBeautyFindUser(Page<User> page, User user) {
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.starBeautyFindUser(user));
		return page;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>) CacheUtils.get(UserUtils.USER_CACHE,
				UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null) {
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}
	/**
	 * 推送加载用户
	 * @param user
	 * @return
	 */
	public List<User> findOaUser(User user){
		user.setParentDel(0);
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		return userDao.findOaUser(user);
	}
	/**
	 * 保存修改日志
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUserLog1(User users) {
		User user = userDao.get(users.getId());
		Dict dict = new Dict();
		dict.setValue(user.getUserType());
		dict.setType("sys_user_type");
		Dict d = dictDao.findDict(dict);
		UserLog userLog = new UserLog();
		String officeName = officeDao.get(user.getOffice().getId()).getName();
		//获取用户权限
		String roleName = "";
		user.setRoleList(roleDao.findList(new Role(user)));
		List<String> List = user.getRoleIdList();
		for (String string : List) {
			Role role = userDao.findRoleName(string);
			roleName = roleName + role.getName() + "、";
		}
		userLog.setContent("上级机构:" + officeName + "|工号:" + user.getNo()
						 + "</br>登录名:" + user.getLoginName() + "|姓名:" + user.getName()
						 + "</br>身份证号:" + user.getIdCard() + "|手机号:"+ user.getMobile() 
						 + "</br>入职时间:"+ DateUtils.format(user.getInductionTime(), "yyyy-MM-dd") + "|用户类型:"+ d.getLabel()
						 + "</br>用户角色:" + roleName);
		user.setUserLog(userLog);
		userDao.saveUserLog(user);
	}
	/**
	 * 用户日志
	 * @param oldUser
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public String specialLog(User oldUser,User user){
		StringBuffer str = new StringBuffer();	// 用于存储一些特殊的日志
		if(!oldUser.getPassword().equals(user.getPassword())){
			str.append("密码:用户更新过密码--");
		}
		if(oldUser.getCompany() != null){
			if(!oldUser.getCompany().getId().equals(user.getCompany().getId())){
				str.append("所属商家:修改前("+oldUser.getCompany().getName()+"),修改后("+user.getCompany().getName()+")--");
			}
		}else{
			str.append("所属商家:修改前(此商家异常,原商家id:"+oldUser.getCompany().getId()+"),修改后("+user.getCompany().getName()+")--");
		}
		if(oldUser.getOffice() != null){
			if(!oldUser.getOffice().getId().equals(user.getOffice().getId())){
				str.append("所属店铺:修改前("+oldUser.getOffice().getName()+"),修改后("+user.getOffice().getName()+")--");
			}
		}else{
			str.append("所属店铺:修改前(此店铺异常,原店铺id:"+oldUser.getOffice().getId()+"),修改后("+user.getOffice().getName()+")--");
		}
		if(oldUser.getSex() != null){
			if(!(oldUser.getSex()).equals(user.getSex())){
				if("1".equals(oldUser.getSex())){
					str.append("性别:修改前(男),修改后(女)--");
				}else if("2".equals(oldUser.getSex())){
					str.append("性别:修改前(女),修改后(男)--");
				}
			}
		}else{
			if("1".equals(user.getSex())){
				str.append("性别:修改前(无),修改后(男)--");
			}else if("2".equals(user.getSex())){
				str.append("性别:修改前(无),修改后(女)--");
			}
		}
	
		if(!(oldUser.getUserType()).equals(user.getUserType())){
			Dict oldDict = new Dict();
			oldDict.setValue(oldUser.getUserType());
			oldDict.setType("sys_user_type");
			Dict oldD = dictDao.findDict(oldDict);
			Dict newDict = new Dict();
			newDict.setValue(user.getUserType());
			newDict.setType("sys_user_type");
			Dict newD = dictDao.findDict(newDict);
			str.append("职位:修改前("+oldD.getLabel()+"),修改后("+newD.getLabel()+")--");
		}
		return str.toString();
	}
	/**
	 * 保存离职日志
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUserLog2(User user) {
		UserLog userLog = new UserLog();
		userLog.setContent("离职备注：" + user.getDelRemarks());
		user.setUserLog(userLog);
		userDao.saveUserLog(user);
	}
	
	/**
	 * 由离职变为在职日志
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUserLog3(User user) {
		UserLog userLog = new UserLog();
		userLog.setContent("在职备注：由离职变为在职");
		user.setUserLog(userLog);
		userDao.saveUserLog(user);
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		// UserLog userLog = new UserLog();
		
		User currentUser = UserUtils.getUser();//记录图片上传时,获取当前登录用户的信息
		String lifeImgUrls = "";//记录图片上传时,图片格式为string类型(格式必须如此)
		List<String> oldLifeImgUrls = new ArrayList<String>();//生活照调用接口(必传数据,修改之前的照片信息,格式必须如此)

		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			if("2".equals(user.getResult())){
				int mtmyUserId = mtmyUsersDao.getUserByMobile(user.getMobile()).getUserid();
				if("B".equals(user.getLayer())){
					mtmyUsersDao.deleteFromSaleRelations(mtmyUserId);
				}
				mtmyUsersDao.updateLayer(mtmyUserId);
				user.setMtmyUserId(mtmyUserId);
				userDao.insert(user);
			}else{
				mtmyUsersDao.trainsInsertMtmy(user);
				logger.info("#####[保存妃子校用户时插入每天美耶--返回每天美耶id]:"+user.getMtmyUserId());
				
				/*##########[神策埋点{sign_up}-Begin]##########*/
				TrackUtils.trackSyncMtmyUser(user);
				/*##########[神策埋点end]##########*/
				
				//新增用户时插入用户账目表
				Users users = new Users();
				users.setUserid(user.getMtmyUserId());
				mtmyUsersDao.insertAccounts(users);
				//新增用户时插入用户统计表
				mtmyUsersDao.insterSaleStats(users);
				userDao.insert(user);
			}
			
			//美容师头像
			if(user.getPhoto() != null && user.getPhoto().length() != 0){
				reservationTime(1, currentUser.getCreateBy().getId(), user.getPhoto(), "", lifeImgUrls, user.getId(), "bm", null, oldLifeImgUrls);
			}
			
			// userinfo.preInsert();
			if (user.getUserinfo() != null) {
				user.getUserinfo().preInsert();
				user.getUserinfo().setUserid(user.getId());
				user.getUserinfo().setNativearea(user.getUserinfo().getAreaP().getId());
				user.getUserinfo().setWorkarea(user.getUserinfo().getAreaC().getId());
				userinfoDao.insertUserinfo(user.getUserinfo()); // 创建新的用户
			}
			if (user.getSpeciality() != null) {
				List<UserSpeciality> list = SpeArryTolist(user); // 获取拼接的特长list
				if (list.size() > 0) {
					specialityDao.insertSpecialityorm(list); // 插入特长表
				}

			}

			if (user.getUserinfocontent() != null) {
				List<Userinfocontent> contlist = livePicTolist(user); // 获取图片信息list
				if (contlist.size() > 0) {
					userinfocontentDao.insertPiclive(contlist);
					
					//美容师生活图
					for (Userinfocontent ufc : contlist) {
						lifeImgUrls +="," + ufc.getUrl();
					}
					lifeImgUrls = lifeImgUrls.substring(1);
					reservationTime(2, currentUser.getCreateBy().getId(), null, null, lifeImgUrls, user.getId(), "bm", null, oldLifeImgUrls);
				}
				
			}
			
			if(user.getSkill() != null){
				List<UserSkill> list = SpeArrSkillList(user); // 获取拼接的技能标签list
				if (list.size() > 0) {
					skillDao.insertUserSkill(list);     // 插入技能标签表
				}
			}
			// 2017年9月1日 新用户默认商家权限为当前商家
			userDao.insertFranchiseeAuth(user.getId(), user.getCompany().getId());
			//saveFzxRoleOfficeById("4",user.getOffice().getId(),user.getId());
			//给用户设置默认的妃子校角色和权限
			FzxRole fzxRole  = new FzxRole();
			fzxRole.setRoleId(4);
			//user.setId(user.getId());
			user.setFzxRole(fzxRole);
			userDao.saveFzxRoleByUser(user);
			userDao.saveOfficeById(user.getReturnId(),user.getOffice().getId());
		} else {
//			saveUserLog1(user);
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			//保存用户日志
			String str = specialLog(oldUser,user);

			//判断美容师头像是否修改   和之前的美容师头像进行比较
			String photo = user.getPhoto() == null ? "" : user.getPhoto();//修改之后的美容师头像
			String oldPhoto = oldUser.getPhoto() == null ? "" : oldUser.getPhoto();//修改之前的美容师头像
			if(!oldPhoto.equals(photo)){
				reservationTime(1, currentUser.getCreateBy().getId(), user.getPhoto(), oldUser.getPhoto(), lifeImgUrls, user.getId(), "bm", null, oldLifeImgUrls);
			}
			
			JSONObject json = new JSONObject();
			json.put("property", "[\"no\",\"name\",\"loginName\",\"idCard\",\"inductionTime\",\"email\",\"phone\",\"mobile\"]");
			json.put("name", "[\"工号\",\"姓名\",\"登录名\",\"身份证号码\",\"入职日期\",\"邮箱\",\"电话\",\"手机号码\"]");
			String string = SaveLogUtils.saveLog(json,str.toString(),oldUser,user);
			if(!"".equals(string) && null != string){
				UserLog userLog = new UserLog();
				userLog.setContent(string);
				user.setUserLog(userLog);
				user.preUpdate();
				userDao.saveUserLog(user);
				redisClientTemplate.del("UTOKEN_"+user.getId());	// 清除用户缓存 用户TOKEN失效
			}
			
			
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}

			if (user.getUserinfo() != null) {
				// 更新用户数据
				Userinfo uinfo = userinfoDao.findByuserId(user.getId()); // 查询数据库里面的用户是否存在

				if (uinfo != null) {
					user.getUserinfo().setUserid(user.getId());
					user.getUserinfo().setNativearea(user.getUserinfo().getAreaP().getId());
					user.getUserinfo().setWorkarea(user.getUserinfo().getAreaC().getId());
					userinfoDao.updateUserinfo(user.getUserinfo()); // 如果数据存在
																	// 更新数据

				} else {
					user.getUserinfo().preInsert();
					user.getUserinfo().setUserid(user.getId());
					user.getUserinfo().setNativearea(user.getUserinfo().getAreaP().getId());
					user.getUserinfo().setWorkarea(user.getUserinfo().getAreaC().getId());
					userinfoDao.insertUserinfo(user.getUserinfo()); // 如果不存在
																	// 则插入数据

				}
				List<UserSpeciality> list = SpeArryTolist(user); // 获得特长list
				if (list.size() > 0) {
					specialityDao.deletormSpec(user.getId());
					specialityDao.insertSpecialityorm(list);
				}
				
				List<UserSkill> skillList = SpeArrSkillList(user); // 获得技能标签list
				skillDao.deletormSpec(user.getId());
				if (skillList.size() > 0) {
					skillDao.insertUserSkill(skillList);
				}

				List<Userinfocontent> contlist = livePicTolist(user); // 获取图片信息list
				if (contlist.size() > 0) {
					//美容师生活照修改之前,先查询是否存在旧照片
					oldLifeImgUrls = userinfocontentDao.findByUserId(user.getId());
					
					userinfocontentDao.deletPicByuser(user.getId());
					userinfocontentDao.insertPiclive(contlist);

					//美容师生活图
					lifeImgUrls = user.getUserinfocontent().getUrl();//必须是String类型的数据
					reservationTime(2, currentUser.getCreateBy().getId(), null, null, lifeImgUrls, user.getId(), "bm", null, oldLifeImgUrls);
				}
			}
			
//begin     修改妃子校用户同时更新每天美耶用户开始   修改时间2017年1月23日
			//用于验证每天美耶用户   
			Users u = new Users();
			u.setId(currentUser.getId());
			u.setUserid(user.getMtmyUserId());
			u.setMobile(user.getMobile());
			
			//更新妃子校手机号时  同时更新每天美耶用户手机号
			if(!oldUser.getMobile().equals(user.getMobile())){
				// 每天美耶存在该用户时 更新  相反 新增
				if(mtmyUsersDao.get(u) != null){
					mtmyUsersDao.update(u);
				}else{
					mtmyUsersDao.trainsInsertMtmy(user);
					logger.info("#####[保存妃子校用户时插入每天美耶--返回每天美耶id]:"+user.getMtmyUserId());
					
					/*##########[神策埋点{sign_up}-Begin]##########*/
					TrackUtils.trackSyncMtmyUser(user);
					/*##########[神策埋点end]##########*/
					
					//新增用户时插入用户账目表
					Users users = new Users();
					users.setUserid(user.getMtmyUserId());
					mtmyUsersDao.insertAccounts(users);
					//新增用户时插入用户统计表
					mtmyUsersDao.insterSaleStats(users);
				}
			}else{
				if(mtmyUsersDao.get(u) == null){
					mtmyUsersDao.trainsInsertMtmy(user);
					logger.info("#####[保存妃子校用户时插入每天美耶--返回每天美耶id]:"+user.getMtmyUserId());
					
					/*##########[神策埋点{sign_up}-Begin]##########*/
					TrackUtils.trackSyncMtmyUser(user);
					/*##########[神策埋点end]##########*/
					
					//新增用户时插入用户账目表
					Users users = new Users();
					users.setUserid(user.getMtmyUserId());
					mtmyUsersDao.insertAccounts(users);
					//新增用户时插入用户统计表
					mtmyUsersDao.insterSaleStats(users);
				}
			}
//end     修改妃子校用户同时更新每天美耶用户结束
			
//			用户商家发生变化时,数据权限跟着变化
			if(oldUser.getCompany() != null){
				if(!oldUser.getCompany().getId().equals(user.getCompany().getId())){
					userDao.deleteFranchiseeAuth(user);
					userDao.insertFranchiseeAuth(user.getId(), user.getCompany().getId());
				}
			}else{
				userDao.deleteFranchiseeAuth(user);
				userDao.insertFranchiseeAuth(user.getId(), user.getCompany().getId());
			}
			
			user.preUpdate();
			userDao.update(user);
			/**
			 * 此处更新完用户之后将用户数据同步到报货，调用报货接口
			 */
			if (StringUtils.isNotBlank(user.getId())) {
				User toUser = userDao.get(user);
				String weburl = ParametersFactory.getMtmyParamValues("modifyToUser");
    			logger.info("##### web接口路径:"+weburl);
    			String parpm = "{\"user_id\":\""+toUser.getId()+"\",\"user_name\":\""+toUser.getName()+"\",\"franchisee_id\":"+toUser.getCompany().getId()+","
    					+ "\"user_mobile\":\""+toUser.getMobile()+"\",\"login_name\":\""+toUser.getLoginName()+"\","
    							+ "\"office_id\":\""+toUser.getOffice().getId()+"\",\"office_name\":\""+toUser.getOffice().getName()+"\"}";
    			String url=weburl;
    			String result = WebUtils.postCSObject(parpm, url);
    			JSONObject jsonObject = JSONObject.fromObject(result);
    			logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
    			if(!"200".equals(jsonObject.get("result"))){
    				return;
    			}
			}
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
			// // 清除权限缓存
			// systemRealm.clearAllCachedAuthorizationInfo();
		}
	}

	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		/**
		 * 用户物理删除修改为逻辑删除，修改时间为：2016-4-13 userDao.delete(user);
		 */
		relieveUser(user);
		saveUserLog2(user);
		userDao.deleteByLogic(user);

		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		
		/**
		 * 删除用户时将用户数据t同步到供应链
		 */
		String weburl = ParametersFactory.getMtmyParamValues("modifyToUser");
		logger.info("##### web接口路径:"+weburl);
		String parpm = "{\"user_id\":\""+user.getId()+"\",\"user_name\":\""+user.getName()+"\",\"franchisee_id\":"+user.getCompany().getId()+","
				+ "\"user_mobile\":\""+user.getMobile()+"\",\"login_name\":\""+user.getLoginName()+"\",\"user_status\":"+1+","
						+ "\"office_id\":\""+user.getOffice().getId()+"\",\"office_name\":\""+user.getOffice().getName()+"\"}";
		String url=weburl;
		String result = WebUtils.postCSObject(parpm, url);
		JSONObject jsonObject = JSONObject.fromObject(result);
		logger.info("##### web接口返回数据：result:"+jsonObject.get("result")+",msg:"+jsonObject.get("msg"));
	}
	
	/**
	 * 将逻辑删除的用户用户还原成在职
	 */
	@Transactional(readOnly = false)
	public void onJob(User user) {
		saveUserLog3(user);
		userDao.onJob(user);

		// 清除用户缓存
		UserUtils.clearCache(user);
	}
	
	
	
	@Transactional(readOnly = false)
	public int deleteUserByIdCard(String idcard) {
		/**
		 * 用户物理删除修改为逻辑删除，修改时间为：2016-4-13 userDao.delete(user);
		 */
		User user = userDao.getUserByIdCard(idcard);
		relieveUser(user);
		return userDao.userDeleteByidCard(idcard);

	}
	/**
	 * 删除用户时  同时更新其下面的分销团队所有人员来源为  11 （离职美容师）   等分销系统一起上线
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void relieveUser(User user){
		Users users = new Users();
		users.setUserid(user.getMtmyUserId());
		users.setSource(11);
		mtmyUsersDao.relieveUser(users);
	}
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	public Collection<Session> getActiveSessions() {
		return sessionDao.getActiveSessions(false);
	}

	// -- Role Service --//

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}

	public List<Role> findRole(Role role) {
		return roleDao.findList(role);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<Role> findAllRoleName() {
		return roleDao.findAllList(new Role());
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.insert(role);
		} else {
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0) {
			roleDao.insertRoleOffice(role);
		}
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	// -- Menu Service --//

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {

		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();

		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())) {
			menu.preInsert();
			menuDao.insert(menu);
		} else {
			menu.preUpdate();
			menuDao.update(menu);
		}

		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n   " + Global.getConfig("productName"));
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 特长数组拼接
	 * 
	 * @param user
	 * @return
	 */
	public List<UserSpeciality> SpeArryTolist(User user) {
		List<UserSpeciality> list = new ArrayList<UserSpeciality>();
		if (user.getSpeciality().getId() != null) {
			String spec = user.getSpeciality().getId();

			String[] arry = spec.split(",");
			for (int i = 0; i < arry.length; i++) {
				UserSpeciality usspe = new UserSpeciality();
				usspe.setUserid(user.getId());
				usspe.setSpecialityid(arry[i]);
				list.add(usspe);
			}

		}

		return list;
	}

	/**
	 * 技能标签拼接
	 * @param user
	 * @return
	 */
	public List<UserSkill> SpeArrSkillList(User user){
		List<UserSkill> list = new ArrayList<UserSkill>();
		if (user.getSkill().getId() != null) {
			String spec = user.getSkill().getId();
			if(!"".equals(spec) && spec != null){
				String[] arry = spec.split(",");
				for (int i = 0; i < arry.length; i++) {
					UserSkill usspe = new UserSkill();
					usspe.setUserId(user.getId());
					usspe.setSkillId(Integer.valueOf(arry[i]));
					list.add(usspe);
				}
			}
			
		}
		return list;
	}
	/**
	 * 图片数组拼接
	 * 
	 * @param user
	 * @return
	 */
	public List<Userinfocontent> livePicTolist(User user) {
		List<Userinfocontent> list = new ArrayList<Userinfocontent>();
		if (user.getUserinfocontent().getUrl().length() > 0) {
			String spec = user.getUserinfocontent().getUrl();

			String[] arry = spec.split(",");
			for (int i = 0; i < arry.length; i++) {
				Userinfocontent usspe = new Userinfocontent();
				usspe.preInsert();
				usspe.setUserid(user.getId());
				usspe.setName("生活照");
				usspe.setUrl(arry[i]);
				usspe.setType(1);
				list.add(usspe);
			}

		}

		return list;
	}

	/**
	 * 根据机构部门编号 查询机构
	 * 
	 * @param code
	 * @return
	 */
	public Office getoffice(String code) {
		return officeDao.getByCode(code);
	}

	/**
	 * 获取用户工号最大值
	 * 
	 * @return
	 */
	public int getNo(String officeid) {
		return userDao.getNO(officeid);
	}

	/**
	 * 查询当前用户前两条数据操作日志
	 * 
	 * @param userid
	 * @return
	 */
	public List<UserLog> findUserLog(String userid) {
		return userDao.findUserLog(userid);
	}

	/**
	 * 查询用户所有操作日志
	 * 
	 * @param page
	 * @param userLog
	 * @return
	 */
	public Page<UserLog> findUserLogList(Page<UserLog> page, UserLog userLog) {
		userLog.setPage(page);
		page.setList(userDao.findUserLogList(userLog));
		return page;
	}
	
	public List<UserOfficeCode> queryUserOfficeCodes(){
		return userDao.queryUserOfficeCodes();
	}
	
	public List<UserVo> queryUserAll(){
		return userDao.queryUserAll();
	}
	/**
	 * 查询角色下的用户
	 * @param user
	 * @return
	 */
	public List<User> findRoleUser(User user){
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		return userDao.findRoleUser(user);
	}
	
	/**
	 * 验证导入的用户的工号是否已经存在 
	 */
	public int selectNo(String no){
		return userDao.selectNo(no);
	}
	
	/**
	 * 验证用户是否为特殊美容师 
	 * @param id
	 * @return
	 */
	public int selectSpecBeautician(String id){
		return userDao.selectSpecBeautician(id);
	}
	
	/**
	 * 是否推荐 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void updateStatus(User user){
		userDao.updateStatus(user);
	}

	/**
	 * 
	 * @Title: saveUserAndFzxRole
	 * @Description: TODO 保存用户权限
	 * @param user
	 * @return:
	 * @return: Integer
	 * @throws
	 * 2017年10月26日
	 */
	public Integer saveUserAndFzxRole(User user) {
		
		return null;
	}

	/**
	 * 保存用户的角色和权限
	 * @param fzxRoleId
	 * @param officeIds
	 * @param userId
	 */
	@Transactional(readOnly = false)
	public void saveFzxRoleOfficeById(String fzxRoleId, String officeIds, String userId) {
		User user  = new User();
		FzxRole fzxRole  = new FzxRole();
		fzxRole.setRoleId(Integer.valueOf(fzxRoleId));
		user.setId(userId);
		user.setFzxRole(fzxRole);
		userDao.saveFzxRoleByUser(user);
		if (user.getReturnId() != null && !"".equals(officeIds)) {
			String[] officeId = officeIds.split(",");
			for (String offId : officeId) {
				userDao.saveOfficeById(user.getReturnId(),offId);
			}
		}
		redisClientTemplate.del("UTOKEN_"+userId);
	}

	/**
	 * 根据id删除权限
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void delFzxRoleByUser(Integer id,String userId,String roleId) {
		//先删除权限
		userDao.deleteOfficeById(id);
		//然后删除角色
		userDao.deleteFzxRoleByUser(userId,roleId);
		redisClientTemplate.del("UTOKEN_"+userId);
	}

	/**
	 * 修改用户权限
	 * @param id
	 */
	@Transactional(readOnly = false)
	public void updateOfficeById(String id,String officeIds,String userId) {
		String[] offIds = officeIds.split(",");
		userDao.deleteOfficeById(Integer.valueOf(id));
		if (!"".equals(officeIds)) {
			for (String ofId : offIds) {
				userDao.updateOfficeById(Integer.valueOf(id),ofId);
			}
		}
		redisClientTemplate.del("UTOKEN_"+userId);
	}

	
	/**
	 * 记录店铺首图、美容院和美容师图片上传相关信息
	 * @param beauticianId
	 * @param serviceMin
	 * @param shopId
	 * @param labelId
	 * @param request
	 * @return
	 */
	private void reservationTime(int type, String createBy, String fileUrl, String oldUrl, String lifeImgUrls, String userId, String client, HttpServletRequest request, List<String> oldLifeImgUrls) {
		JSONObject jsonObject = new JSONObject();
		try {
			String webReservationTime =	ParametersFactory.getMtmyParamValues("uploader_picture_url");
			if(!"-1".equals(webReservationTime)){
				logger.info("##### web接口路径:"+webReservationTime);
				String parpm = "";
				if(type == 1){
					parpm = "{\"type\":\""+type+"\",\"create_by\":\""+createBy+"\",\"file_url\":\""+fileUrl+"\",\"old_url\":\""+oldUrl+"\",\"life_img_urls\":\""+lifeImgUrls+"\",\"user_id\":\""+userId+"\",\"client\":\""+client+"\"}";
				}else if(type == 2){
					parpm = "{\"type\":\""+type+"\",\"create_by\":\""+createBy+"\",\"file_url\":\"\",\"old_url\":"+JSONArray.fromObject(oldLifeImgUrls)+",\"life_img_urls\":\""+lifeImgUrls+"\",\"user_id\":\""+userId+"\",\"client\":\""+client+"\"}";
				}
				String url=webReservationTime;
				System.out.println(parpm);
				String result = WebUtils.postTrainObject(parpm, url);
				jsonObject = JSONObject.fromObject(result);
				logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
			}
			
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "记录店铺首图、美容院和美容师图片上传相关信息", e);
			logger.error("调用接口:记录店铺首图、美容院和美容师图片上传相关信息:"+e.getMessage());
		}
	}
}
