package com.training.modules.ec.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.TextFormat.ParseException;

/**
 * 订单帮助类
 * @author kele
 * @version 2016-6-24
 */
public class OrderUtils {

	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(OrderUtils.class);
	
	/**
     * 指定日期加上天数后的日期
     * @param num 为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException 
	 * @throws java.text.ParseException 
     */
    public static String plusDay(int num,String newDate) throws Exception{
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currdate = format.parse(newDate);
        logger.info("###当前的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        logger.info("###增加天数以后的日期：" + enddate);
        return enddate;
    } 
}
