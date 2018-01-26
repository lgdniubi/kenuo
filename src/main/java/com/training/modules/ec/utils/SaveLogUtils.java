package com.training.modules.ec.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import com.training.common.utils.DateUtils;

import net.sf.json.JSONObject;

public class SaveLogUtils {
	//  保存日志
	@SuppressWarnings("unchecked")
	public static String saveLog(JSONObject json,String str,Object oldT,Object newT) {
		List<String> propertyList = (List<String>) json.get("property");	// 需要加入日志的属性
		List<String> nameList = (List<String>) json.get("name");			// 需要加入日志属性对应的属性名
	
		StringBuilder string = new StringBuilder();//存放修改日志
		// 无法验证
		if(null != str && !str.isEmpty()){
			string.append(str);
		}
		// 存在比较对象时进入循环
		if (propertyList.size() > 0 && nameList.size() > 0) {
			
			Class<Object> cOldT = (Class<Object>) oldT.getClass();	// 修改前信息
			Class<Object> cNewT = (Class<Object>) newT.getClass();	// 修改后信息	
				
			Field[] files = cOldT.getDeclaredFields();	// 获取用户对象里面所有的属性
				
			for (Field field : files) {	// 循环对象里对应的所有属性
				for (int i = 0; i < propertyList.size(); i++) { // 循环需要加入日志的属性
					
					if(field.getName().equals(propertyList.get(i))){ // 当对象里属性与需加入日志的属性相同时 进入
						String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
						try {
							Method mOldT = (Method) cOldT.getMethod(getMethodName);
							Method mNewT = (Method) cNewT.getMethod(getMethodName);
							try {
								//属性为时间格式  则进行格式化时间
								Object valOldT;
								Object valNewT;
								
								if(mOldT.getReturnType().getName().contains("Date")){
									valOldT = (mOldT.invoke(oldT) == null || "".equals(mOldT.invoke(oldT))) ? "无" : DateUtils.formatDateTime((Date) mOldT.invoke(oldT)); 
									valNewT = (mNewT.invoke(newT) == null || "".equals(mNewT.invoke(newT))) ? "无" : DateUtils.formatDateTime((Date) mNewT.invoke(newT));
								}else{
									valOldT = mOldT.invoke(oldT);
									valNewT = mNewT.invoke(newT);
								}
								
								valOldT = (valOldT == null || "".equals(valOldT)) ? "无" : valOldT;
								valNewT = (valNewT == null || "".equals(valNewT)) ? "无" : valNewT;
								
								if (!valOldT.equals(valNewT)) {
									string.append(nameList.get(i)+":修改前("+String.valueOf(valOldT)+"),修改后("+String.valueOf(valNewT)+")--");
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} 
						} catch (NoSuchMethodException e) {
							
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return string.toString();
	}
}

