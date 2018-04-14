/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.training.common.utils.CacheUtils;
import com.training.common.utils.SpringContextHolder;
import com.training.modules.sys.dao.RoleDao;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.Role;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.training.modules.train.dao.MediaMenuDao;
import com.training.modules.train.entity.MediaMenu;

/**
 * 用户工具类
 * 
 * @version 2013-12-05
 */
public class MediaMenuUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);

	public static final String USER_CACHE = "mediaUserCache";
	public static final String USER_CACHE_ID_ = "media_id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "mediaLn";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "media_oid_";

	public static final String CACHE_ROLE_LIST = "mediaRoleList";
	public static final String CACHE_MENU_LIST = "mediaMenuList";
	public static final String CACHE_AREA_LIST = "mediaAreaList";
	public static final String CACHE_OFFICE_LIST = "mediaOfficeList";
	public static final String CACHE_OFFICE_ALL_LIST = "mediaOfficeAllList";
	
	
	private static MediaMenuDao mediaMenuDao = SpringContextHolder.getBean(MediaMenuDao.class);

	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<MediaMenu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<MediaMenu> menuList = (List<MediaMenu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = mediaMenuDao.findAllList(new MediaMenu());
			}else{
				MediaMenu m = new MediaMenu();
				m.setUserId(user.getId());
				menuList = mediaMenuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	// ============== User Cache ==============
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}
	
	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	/**
	 * 
	 * @param key
	 * @Description:清楚缓存的菜单
	 */
	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}
}