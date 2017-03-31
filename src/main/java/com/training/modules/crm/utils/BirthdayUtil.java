package com.training.modules.crm.utils;

import java.util.Calendar;
import java.util.Date;

	/**    
	* kenuo      
	* @description 根據出生日期計算生日  
	* @author：sharp   
	* @date：2017年3月10日            
	*/
public class BirthdayUtil {

	public static int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
				age -= 1;
			}
		}
		return age;
	}
}