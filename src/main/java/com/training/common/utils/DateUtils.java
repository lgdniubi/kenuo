/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.common.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 获取当前年份
	 * @return
	 */
	public static int getYears(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);    //获取年
	}
	/**
	 * 获取当前年第几周
	 * @return
	 */
	public static YearAndWeek getWeekOfYear(){
		YearAndWeek yaw = new YearAndWeek();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		calendar.setMinimalDaysInFirstWeek(7); 
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		if(month == 0){
			if(weekOfYear == 1){
				year = year - 1;
				weekOfYear = getMaximum(calendar,year);
			}else if(weekOfYear >10){
				year = year -1;
				weekOfYear = weekOfYear - 1;
			}
		}else{
			weekOfYear = weekOfYear - 1;
		}
		
		
		yaw.setWeek(weekOfYear);
		yaw.setYear(year);
		return yaw;
	}
	public static YearAndWeek getWeekOfYear0(){
		YearAndWeek yaw = new YearAndWeek();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		//设置一号所在的周为年的第一周
		calendar.setMinimalDaysInFirstWeek(1); 
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		
		if(month == 12 && weekOfYear == 1){
			year += 1;
		}
		
		yaw.setWeek(weekOfYear);
		yaw.setYear(year);
		return yaw;
	}
	/**
	 * 获取指定年的最大周
	 * @param year
	 * @return
	 */
	public static int getMaximum(Calendar calendar,int year){
		calendar.set(Calendar.YEAR,year);
		calendar.setMinimalDaysInFirstWeek(7); 
		return calendar.getMaximum(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		System.out.println(getWeekOfYear0());
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
	}
	public static class YearAndWeek{
		private int year;
		private int week;
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getWeek() {
			return week;
		}
		public void setWeek(int week) {
			this.week = week;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return year+""+week;
		}
		
	}
	
	/**
	 * 获取当前时间是那一年的第几周
	 * @return
	 */
	public static YearAndWeek getNowWeekOfYear(){
		YearAndWeek yaw = new YearAndWeek();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		//设置一号所在的周为年的第一周
		calendar.setMinimalDaysInFirstWeek(1); 
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		
		if(month == 12 && weekOfYear == 1){
			year += 1;
		}
		
		yaw.setWeek(weekOfYear);
		yaw.setYear(year);
		return yaw;
	}
	/**
	 * 获取当前时间的上一周是那一年那一周
	 * @return
	 */
	public static YearAndWeek getLastWeekOfYear(int flag){
		YearAndWeek yaw = new YearAndWeek();
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY); 
		
		calendar.setMinimalDaysInFirstWeek(1); 
		calendar.add(Calendar.WEEK_OF_YEAR, flag);
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		
		
		if(month == 12 && weekOfYear == 1){
			year += 1;
		}
		
		yaw.setWeek(weekOfYear);
		yaw.setYear(year);
		return yaw;
	}
	/**
     * date2比date1多的天数
     * @param date1 减数
     * @param date2 被减数
     * @return    
     */
	public static int differentDays(Date date1,Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) {// 同一年
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {// 闰年
					timeDistance += 366;
				} else {// 不是闰年
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {// 不同年
			System.out.println("判断day2 - day1 : " + (day2 - day1));
			return day2 - day1;
		}
	}
}
