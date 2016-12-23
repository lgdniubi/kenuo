package com.training.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.modules.sys.dao.IRuleParamMapper;
import com.training.modules.sys.entity.RuleParam;

/**
 * 系统规则参数-Service
 * @author kele
 * @version 2016-10-19
 */
@Service
public class RuleParamService {

	@Autowired
	private IRuleParamMapper iRuleParamMapper;
	
	/**
	 * 查询所有Trains系统规则参数
	 * @param map
	 * @return
	 */
	public List<RuleParam> findAllTrainsRuleParam(){
		return this.iRuleParamMapper.findAllTrainsRuleParam();
	}
	
	/**
	 * 查询所有Mtmy系统规则参数
	 * @param map
	 * @return
	 */
	public List<RuleParam> findAllMtmyRuleParam(){
		return this.iRuleParamMapper.findAllMtmyRuleParam();
	}
}
