/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.config.Global;
import com.training.common.utils.CacheUtils;
import com.training.common.utils.Exceptions;
import com.training.common.utils.SpringContextHolder;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.dao.BugLogDao;
import com.training.modules.sys.dao.MenuDao;
import com.training.modules.sys.entity.BugLog;
import com.training.modules.sys.entity.Menu;
import com.training.modules.sys.entity.User;

/**
 * 字典工具类
 * 
 * @version 2014-11-7
 */
public class BugLogUtils {
	
	public static final String CACHE_MENU_BUGLOG_PATH_MAP = "menuNamePathMap";
	
	private static BugLogDao bugLogDao = SpringContextHolder.getBean(BugLogDao.class);
	private static MenuDao BugmenuDao = SpringContextHolder.getBean(MenuDao.class);
	
	/**
	 * 保存日志
	 */
	public static void saveBugLog(HttpServletRequest request, String title,Exception ex){
		saveBugLog(request, null, title,ex);
	}
	
	/**
	 * 保存日志
	 */
	public static void saveBugLog(HttpServletRequest request, Object handler, String title,Exception ex){
		User user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			BugLog buglog = new BugLog();
			buglog.setTitle(title);
			if(ex.toString().length()>100){
				buglog.setContent(ex.toString().substring(0,100));
			}else{
				buglog.setContent(ex.toString());
			}
			buglog.setCreateUser(user.getName());
			buglog.setUserAgent(request.getHeader("user-agent"));
			buglog.setRequestUri(request.getRequestURI());
			// 异步保存日志
			new SaveLogThread(buglog, handler,ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private BugLog buglog;
		private Object handler;
		private Exception ex;
		
		public SaveLogThread(BugLog buglog, Object handler,Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.buglog = buglog;
			this.handler = handler;
			this.ex=ex;

		}
		
		@Override
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(buglog.getTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					Method m = ((HandlerMethod)handler).getMethod();
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
				}
				buglog.setTitle(getMenuNamePath(buglog.getRequestUri(), permission));
			}
			
			buglog.setException(Exceptions.getStackTraceAsString(ex));
			
			// 保存日志信息
			buglog.preInsert();
			bugLogDao.insert(buglog);
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission){
		String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_BUGLOG_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<Menu> menuList = BugmenuDao.findAllList(new Menu());
			for (Menu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						if (Menu.getRootId().equals(id)){
							continue; // 过滤跟节点
						}
						for (Menu m : menuList){
							if (m.getId().equals(id)){
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}
				
			}
			CacheUtils.put(CACHE_MENU_BUGLOG_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}

	
}
