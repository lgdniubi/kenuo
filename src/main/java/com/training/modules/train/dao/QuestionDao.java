/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.HandbookType;
import com.training.modules.train.entity.Question;

/**
 * 菜单DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface QuestionDao extends CrudDao<Question> {

	public List<Question> findTypeList();

	public void saveQuestionHandbook(@Param("id")String id, @Param("typeId")String typeId);

	public void updateIsOpen(Question question);
	/**
	 * 根据问题id删除中间数据
	 * @param id
	 */
	public void deleteQuestionHandbook(String id);
	
	/**
	 * 根据问题id查找类型集合
	 * @param id
	 * @return
	 */
	public List<HandbookType> findBookType(String id);

	
	
}
