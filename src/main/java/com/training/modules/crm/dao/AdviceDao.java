package com.training.modules.crm.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.crm.entity.Complain;
import com.training.modules.sys.entity.User;

/**
 * @author：星星
 * @description：投诉咨询dao类 
 * 2017年3月10日
 */
@MyBatisDao
public interface AdviceDao extends CrudDao<Complain>{

	public int saveQuestion(Complain complain);
	
	public void saveSolve(Complain complain);
	
	public Complain detailed(Complain complain);
	
	public List<Complain> procedure(Complain complain);

	public Complain selectHandle(Complain complain);

	public void creatHandle(Complain complains);
	
	public void saveHandle(Complain complain);

	public Complain selectMember(Complain complain);
	
	public Complain selectMemb(Complain complain);
	
	public List<Complain> selectStatus(Complain complain);

	public void creatResult(Complain complain);

	public Complain selectId(Complain complain);

	public List<User> findUser(User user);

	public Complain getUser(String mobile);

	public List<Complain> getRedirectUserId(Complain complain);

	public int getCount(Complain complain);

	public List<Complain> selectSeek(Complain complain);


}
