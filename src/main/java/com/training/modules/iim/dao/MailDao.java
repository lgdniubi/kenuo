/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.iim.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.iim.entity.Mail;

/**
 * 发件箱DAO接口
 * 
 * @version 2015-11-15
 */
@MyBatisDao
public interface MailDao extends CrudDao<Mail> {
	public int getCount(MailDao entity);
}