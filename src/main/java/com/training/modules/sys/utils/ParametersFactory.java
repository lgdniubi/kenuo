package com.training.modules.sys.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.training.common.utils.BeanUtil;
import com.training.modules.sys.entity.RuleParam;
import com.training.modules.sys.service.RuleParamService;

/**
 * 静态参数配置加载类
 * 
 * @author kele
 * @version 2016-10-19
 */
public class ParametersFactory {
	
	private static final Log logger = LogFactory.getLog(ParametersFactory.class);

	// 妃子校系统参数表
	private static Map<String, String> trainsParamMap = new HashMap<String, String>();

	// 每天美耶系统参数表
	private static Map<String, String> mtmyParamMap = new HashMap<String, String>();

	private static RuleParamService ruleParamService;
	
	static {
		//根据springId获取Service
		ruleParamService = (RuleParamService) BeanUtil.getBean("ruleParamService");
	}

	/**
	 * 项目加载
	 */
	public static void init(){
		// TODO 妃子校系统参数表加载
		refrashTrainsFormMap();

		// TODO 每天美耶系统参数表加载
		refrashMtmyFormMap();
	}
	
	/**
	 * 妃子校系统参数表加载
	 */
	public static void refrashTrainsFormMap() {
		List<RuleParam> trainRuleParams = ruleParamService.findAllTrainsRuleParam();
		logger.debug("#####[加载：妃子校系统参数表加载,加载数量：]"+trainRuleParams.size());
		if(trainRuleParams.size() > 0){
			for (int i = 0; i < trainRuleParams.size(); i++) {
				RuleParam ruleParam = trainRuleParams.get(i);
				trainsParamMap.put(ruleParam.getParamKey(), ruleParam.getParamValue());
			}
		}
		
	}

	/**
	 * 每天美耶系统参数表加载
	 */
	public static void refrashMtmyFormMap() {
		List<RuleParam> mtmyRuleParams = ruleParamService.findAllMtmyRuleParam();
		logger.debug("#####[加载：每天美耶系统参数表加载,加载数量：]"+mtmyRuleParams.size());
		if(mtmyRuleParams.size() > 0){
			for (int i = 0; i < mtmyRuleParams.size(); i++) {
				RuleParam ruleParam = mtmyRuleParams.get(i);
				mtmyParamMap.put(ruleParam.getParamKey(), ruleParam.getParamValue());
			}
		}
	}

	/**
	 * 妃子校系统参数
	 * 获取静态参数ParMap列表key对应的值
	 */
	public static String getTrainsParamValues(String key) {
		if (trainsParamMap.containsKey(key)) {
			return (String) trainsParamMap.get(key);
		}
		return "-1";
	}

	/**
	 * 每天美耶系统参数
	 * 获取静态参数ParMap列表key对应的值
	 */
	public static String getMtmyParamValues(String key) {
		if (mtmyParamMap.containsKey(key)) { 
			return (String) mtmyParamMap.get(key);
		}
		return "-1";
	}

}
