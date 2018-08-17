/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.HandbookType;
import com.training.modules.train.entity.ProtocolModel;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.ProtocolUser;
import com.training.modules.train.entity.Question;

/**
 * 菜单DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface HandbookTypeDao extends CrudDao<HandbookType> {

	public List<HandbookType> findTypeList();

	public Integer findQuestionList(String hid);

	
	
}
