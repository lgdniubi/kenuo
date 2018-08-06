/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.HandbookTypeDao;
import com.training.modules.train.entity.ContractInfoVo;
import com.training.modules.train.entity.PayInfo;
import com.training.modules.train.entity.HandbookType;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.ProtocolUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = false)
public class HandbookTypeService extends CrudService<HandbookTypeDao,HandbookType> {

	@Autowired
	private HandbookTypeDao handbookTypeDao;

	/**
	 * 查找协议类型列表
	 * @return
	 */
	public List<HandbookType> findTypeList() {
		return dao.findTypeList();
	}

	public Integer findQuestionList(String id) {
		return handbookTypeDao.findQuestionList(id);
	}

	
	
	
}