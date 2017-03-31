package com.training.modules.crm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理skinFile的value 提取字符串中的数字
 * 
 * @author：sharp @date：2017年3月17日
 */
public class StringHandel {

	/**
	 * @param
	 * @return String
	 * @description 最后一个字段如果有,String会加上,
	 */
	public static Object getValue(Object object) {
		String temString = String.valueOf(object) ;
		if (temString instanceof String) {
			try {
				Pattern pattern = Pattern.compile("[0-9]{1,}");
				if (!"".equals(object) || null != object) {
					String string = "";
					String[] stepOne = temString.split(",");
					for (int i = 0; i < stepOne.length; i++) {
						String[] stepTwo = stepOne[i].split("#");
						for (int j = 0; j < stepTwo.length; j++) {
							Matcher matcher = pattern.matcher(stepTwo[j]);
							if (matcher.matches()) {
								if (i < (stepOne.length - 1)) {
									string += stepTwo[j] + "$";
								} else {
									string += stepTwo[j];
								}
							}
						}
					}
					return string;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}