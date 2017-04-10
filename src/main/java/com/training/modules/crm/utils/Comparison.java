package com.training.modules.crm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Comparison {
	

	private static final Map<String, String> cols = new HashMap<String, String>();
	
	//用于select的列
	private static final Map<String,Map<String,String>> selectCols = new HashMap<String,Map<String,String>>();
	
	private static final Map<String, String> selectSex = new HashMap<String, String>();
	private static final Map<String, String> selectStar = new HashMap<String, String>();
	private static final Map<String, String> selectChracter = new HashMap<String, String>();
	private static final Map<String, String> selectMarrige = new HashMap<String, String>();
	private static final Map<String, String> selectEstate = new HashMap<String, String>();
	private static final Map<String, String> selectMember = new HashMap<String, String>();
	private static final Map<String, String> selectCar = new HashMap<String, String>();
	private static final Map<String, String> selectChildren = new HashMap<String, String>();
	private static final Map<String, String> selectOccupation= new HashMap<String, String>();
	private static final Map<String, String> selectIncome = new HashMap<String, String>();
    
	private static Object lockForCompare = new Object();
	
	static{
		//性别
		selectSex.put("0", "保密");
		selectSex.put("1", "男");
		selectSex.put("2", "女");
        //星座
		selectStar.put("1","白羊座");
		selectStar.put("2","金牛座");
		selectStar.put("3","双子座");
		selectStar.put("4","巨蟹座");
		selectStar.put("5","狮子座");
		selectStar.put("6","处女座");
		selectStar.put("7","天秤座");
		selectStar.put("8","天蝎座");
		selectStar.put("9","射手座");
		selectStar.put("10","摩羯座");
		selectStar.put("11","水瓶座");
		selectStar.put("12","双鱼座");
		//性格
		selectChracter.put("1","外向开朗");
		selectChracter.put("2","内向言少");
		selectChracter.put("3","中性");
		//婚姻情况
		selectMarrige.put("1", "已婚");
		selectMarrige.put("2", "未婚");
		selectMarrige.put("3", "未知");
		//房产情况
		selectEstate.put("1","已购买");
		selectEstate.put("2","未购买");
		selectEstate.put("3","未知");
		//是否会员
		selectMember.put("1","会员");
		selectMember.put("0","非会员");

		//汽车品牌
		selectCar.put("1", "奥迪");
		selectCar.put("3", "宾利");
		selectCar.put("2", "宝马");
		selectCar.put("4", "本田");
		//子女个数
		selectChildren.put("0","0");
		selectChildren.put("1","1");
		selectChildren.put("2","2");
		selectChildren.put("3","3");
		selectChildren.put("4","4");
		//客户职业
		selectOccupation.put("1","白领精英");
		selectOccupation.put("2","相夫教子");
		selectOccupation.put("3","公司老板");
		selectOccupation.put("4","个体户");
		//月收入
		selectIncome.put("1","5000及以下");
		selectIncome.put("2","10000到20000");
		selectIncome.put("3","5000到10000");
		selectIncome.put("4","20000以上");		
		//比较userDetail
		cols.put("name","真实姓名");
		cols.put("nickname","客户昵称");
		cols.put("sex","客户性别");
		cols.put("idCard","身份证号");
		cols.put("remark","备注");
		cols.put("birthday","出生日期");
		cols.put("lunarBirthday","阴历生日");
		cols.put("character","客户性格");
		cols.put("constellation","客户星座");
		cols.put("isMarrige","结婚状况");
		cols.put("weddingDay","结婚纪念");
		cols.put("isEstate","房产情况");
		cols.put("isMember","是否会员");
		cols.put("carBrand","汽车品牌");
		cols.put("children","子女人数");
		cols.put("occupation","客户职业");
		cols.put("income","月收入");
		cols.put("menstrualDate","例假日期");
		cols.put("menstrualPeroid","例假天数");
		cols.put("weight","体重");
		cols.put("height","身高");
		cols.put("criticalDiseases","重大疾病");
		cols.put("source","客户来源");
		cols.put("usingBrand","现用美容品牌");
		cols.put("intrest","兴趣爱好");
		cols.put("taboo","客户忌讳");
		cols.put("hate","客户厌恶");
		cols.put("promotionAgent","推广人员");
		//比较联系信息
		cols.put("qq","qq号码");
		cols.put("wechat","微信号码");
		cols.put("email","电子邮箱");
		cols.put("companyName","公司名称");
		cols.put("address", "住址");
		//select 所需要的列
		selectCols.put("sex",selectSex);
		selectCols.put("constellation",selectStar);
		selectCols.put("character",selectChracter);
		selectCols.put("isMarrige",selectMarrige);
		selectCols.put("isEstate",selectEstate);
		selectCols.put("isMember",selectMember);
		selectCols.put("carBrand",selectCar);
		selectCols.put("children",selectChildren);
		selectCols.put("income",selectIncome);
		selectCols.put("occupation",selectOccupation);
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
	    StringBuffer sBuilder = new StringBuffer();
		// 获取实体类的所有属性，返回Field数组  
	    Field[] field = afterObj.getClass().getDeclaredFields();  
	    //转换日期
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd"); 
	    // 遍历所有属性  
        synchronized(lockForCompare){ 
        
        for (int j = 0; j < field.length; j++) {  
	    	    // 获取属性的名字  
	            String typeName = field[j].getName();  
	            // 将属性的首字符大写，方便构造get，set方法  
	            String nameUpperCase = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);  
	            // 获取属性的类型  
	            String type = field[j].getGenericType().toString();  
	            // 如果type是类类型，则前面包含"class "，后面跟类名  
                if (cols.containsKey(typeName)) {
                	//string 类型的
                	if (type.equals("class java.lang.String")) {
    	            	if ("beautyId".equals(typeName)||"officeId".equals(typeName)||"sex".equals(typeName)) {
    						
    					}else{
    	                    Method m = afterObj.getClass().getMethod("get" + nameUpperCase);  
    	                    // 调用getter方法获取属性值  
    	                    String afterValue = (String) m.invoke(afterObj);  
    	                    String beforeValue = (String) m.invoke(beforeObj); 
    	                    if (afterValue != null && afterValue.trim().length()>0){  
    	                    	if (afterValue.equals(beforeValue)) {
    	                    		
    							}else{
    	    	                    System.out.println("数据类型为：String"+typeName+afterValue+"null"+beforeValue);  

    								if(null==beforeValue || beforeValue.trim().length()<=0) {
	    									if (selectCols.containsKey(typeName)) {
	    										sBuilder.append(cols.get(typeName)+" :新增--"+selectCols.get(typeName).get(afterValue)+";"); 
											}else {
												sBuilder.append(cols.get(typeName)+" :新增--"+afterValue+";  ");
											}
    								}else {
    									if (selectCols.containsKey(typeName)) {
    									sBuilder.append(cols.get(typeName)+": "+selectCols.get(typeName).get(beforeValue)
    											+"--"+selectCols.get(typeName).get(afterValue)+"; "); 
    									}else{
    										sBuilder.append(cols.get(typeName)+": "+beforeValue+"--"+afterValue+"; "); 
    									}
    								} 
    							}
    	                    }else if (null != beforeValue && beforeValue.trim().length()>0) {
								sBuilder.append(cols.get(typeName)+":删除--"+beforeValue+"  "); 
							}
    					}
    	            }  
    	            if (type.equals("class java.lang.Double")) {  
    	                    Method m = afterObj.getClass().getMethod("get" +nameUpperCase);  
    	                    Double afterValue = (Double) m.invoke(afterObj);  
    	                    System.out.println("数据类型为：double");
    	                    Double beforeValue = (Double) m.invoke(beforeObj); 
    	                    if (null != afterValue) { 
	    	                    System.out.println("数据类型为：double"+typeName+afterValue+"null"+beforeValue);  
    	                    	if (beforeValue==afterValue || beforeValue.compareTo(afterValue)==0) {
									
    	                    	}else {
    	                    		if (beforeValue==0) {
    	                    			sBuilder.append(cols.get(typeName)+":新增--"+afterValue+"; "); 
    								}else {
    									sBuilder.append(cols.get(typeName)+":修改--"+beforeValue+"--"+afterValue+"; "); 
    								}
    							}
    	                    }else if (null!= beforeValue) {
								sBuilder.append(cols.get(typeName)+":删除--"+beforeValue+" "); 
							}
    	            }  
    	            if (type.equals("class java.util.Date")) {  
    	                    Method m = afterObj.getClass().getMethod("get" + nameUpperCase);  
    	                    Date afterValue = (Date)m.invoke(afterObj);  
    	                    System.out.println("数据类型为：Date");  
	                    	java.util.Date beforeValue = (java.util.Date) m.invoke(beforeObj); 
    	                    if (afterValue != null) {  
    	                    	if (afterValue.equals(beforeValue)) {
    	                    		System.out.println(sBuilder);
    							}else{
    								if (null==beforeValue) {
    	                    			sBuilder.append(cols.get(typeName)+"：新增--"+format.format(afterValue)+";"); 
    								}else {
    									sBuilder.append(cols.get(typeName)+"：修改--"+format.format(beforeValue)+"--"+format.format(afterValue)+";"); 
    								}
    							}
    	                    } else if (null!= beforeValue) {
    	                    	sBuilder.append(cols.get(typeName)+":删除--"+format.format(beforeValue)+" "); 
							} 
    	            }
				}
	         }
		    if (sBuilder.toString().trim().length()<=0) {
				sBuilder.append("未作修改");
			}
        }
	    return sBuilder.toString();
	}  
}