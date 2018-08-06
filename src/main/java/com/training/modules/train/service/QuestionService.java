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
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.QuestionDao;
import com.training.modules.train.entity.ContractInfoVo;
import com.training.modules.train.entity.PayInfo;
import com.training.modules.train.entity.Question;
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
public class QuestionService extends CrudService<QuestionDao,Question> {

	@Autowired
	private QuestionDao questionDao;

	/**
	 * 查找协议类型列表
	 * @return
	 */
	public List<Question> findTypeList() {
		return dao.findTypeList();
	}

	/**
	 * 保存手册内容，还保存手册--分类id
	 * @param question
	 */
	public void saveQuestion(Question question) {
		boolean isNewRecord = question.getIsNewRecord();
		super.save(question);
		if (isNewRecord){
			questionDao.saveQuestionHandbook(question);
		}
	}

	/**
	 * 更改启用状态
	 * @param question
	 */
	public void updateIsOpen(Question question) {
		questionDao.updateIsOpen(question);
	}

	
	
	
}