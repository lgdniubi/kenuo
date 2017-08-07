package com.training.modules.train.utils;

import java.math.BigDecimal;

public class Utils {

	public static int round(double value, int scale, int roundingMode)
	{
	BigDecimal bigData = new BigDecimal(value);
	bigData = bigData.setScale(scale, roundingMode);
	int dv = bigData.intValue();
	return dv;
	}
	
}
