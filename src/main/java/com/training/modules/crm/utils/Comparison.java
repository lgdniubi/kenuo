package com.training.modules.crm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comparison {
	

	private static final Map<String, String> cols = new HashMap<String, String>();
	static{
		//比较userDetail
		cols.put("name","名字");
		cols.put("nickname","昵称");
		cols.put("idCard","身份证");
		cols.put("remark","备注");
		cols.put("officeName","所属店铺");
		cols.put("beautyName","所属美容师");
		cols.put("birthDay","生日");
		cols.put("character","性格");
		cols.put("constellation","星座");
		cols.put("isMarrige","是否结婚");
		cols.put("weddingDay","结婚纪念日");
		cols.put("isEstate","房产情况");
		cols.put("isMember","是否会员");
		cols.put("carBrand","汽车品牌");
		cols.put("children","孩子情况");
		cols.put("occupation","职业");
		cols.put("income","月收入");
		cols.put("menstrualDate","例假日期");
		cols.put("menstrualPeroid","例假周期");
		cols.put("weight","体重");
		cols.put("height","身高");
		cols.put("criticalDiseases","疾病");
		cols.put("source","客户来源");
		cols.put("usingBrand","现用品牌");
		cols.put("intrest","兴趣");
		cols.put("taboo","禁忌");
		cols.put("hate","厌恶");
		cols.put("promotionAgent","推广人员");

		//比较联系信息
		cols.put("qq","qq号码");
		cols.put("wechat","微信号码");
		cols.put("email","电子邮箱");
		cols.put("companyName","公司名称");
		cols.put("address", "住址");
	}
	/** 
	 * 遍历实体类的属性和数据类型以及属性值 
	 * @param model 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */  
	public static String compareObj(Object beforeObj,Object afterObj) throws NoSuchMethodException,  
	                IllegalAccessException, IllegalArgumentException,  
	                InvocationTargetException {  
		 //返回的String    
	    StringBuilder sBuilder = new StringBuilder();
		// 获取实体类的所有属性，返回Field数组  
	    Field[] field = afterObj.getClass().getDeclaredFields();  
	    // 遍历所有属性  
	    for (int j = 0; j < field.length; j++) {  
	    	    sBuilder.append("  ");
	    	    // 获取属性的名字  
	            String typeName = field[j].getName();  
	            // 将属性的首字符大写，方便构造get，set方法  
	            String nameUpperCase = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);  
	            // 获取属性的类型  
	            String type = field[j].getGenericType().toString();  
	            // 如果type是类类型，则前面包含"class "，后面跟类名  
        
	            if (type.equals("class java.lang.String")) {
	            	if ("beautyId".equals(typeName)||"officeId".equals(typeName)) {
						
					}else{
	                    Method m = afterObj.getClass().getMethod("get" + nameUpperCase);  
	                    // 调用getter方法获取属性值  
	                    String afterValue = (String) m.invoke(afterObj);  
	                    System.out.println("数据类型为：String");  
	                    if (afterValue != null && afterValue.trim().length()>0){  
	                    	String beforeValue = (String) m.invoke(beforeObj); 
	                    	if (afterValue.equals(beforeValue)) {
	                    		System.out.println("属性值相同");  
							}else{
								if (null==beforeObj) {
	                    			sBuilder.append(cols.get(typeName)+"新增"+afterValue+";"); 
								}else {
									sBuilder.append(cols.get(typeName)+":"+beforeValue+"--"+afterValue+";"); 
								} 
							}
	                    }
					}
	            }  
	            if (type.equals("class java.lang.Double")) {  
	                    Method m = afterObj.getClass().getMethod("get" +nameUpperCase);  
	                    Double afterValue = (Double) m.invoke(afterObj);  
	                    System.out.println("数据类型为：Integer");  
	                    if (afterValue != null) {  
	                    	Double beforeValue = (Double) m.invoke(beforeObj);  
	                    	if (beforeValue==afterValue) {
	                    		
	                    	}else {
	                    		if (null==beforeObj) {
	                    			sBuilder.append(cols.get(typeName)+"新增"+afterValue+";"); 
								}else {
									sBuilder.append(cols.get(typeName)+":"+beforeValue+"--"+afterValue+";"); 
								}
							}
	                    }
	            }  
	            if (type.equals("class java.util.Date")) {  
	                    Method m = afterObj.getClass().getMethod("get" + nameUpperCase);  
	                    Date afterValue = (Date)m.invoke(afterObj);  
	                    System.out.println("数据类型为：Date");  
	                    if (afterValue != null) {  
	                    	java.util.Date beforeValue = (java.util.Date) m.invoke(beforeObj); 
	                    	if (afterValue.equals(beforeValue)) {
	                    		System.out.println(sBuilder);
							}else{
								if (null==beforeObj) {
	                    			sBuilder.append(cols.get(typeName)+"新增"+afterValue+";"); 
								}else {
									sBuilder.append(cols.get(typeName)+":"+beforeValue+"--"+afterValue+";"); 
								}
							}
	                    }  
	            } 
	    }  
	    return sBuilder.toString();
	}  
}