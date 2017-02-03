package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.TrainLiveUserDao;
import com.training.modules.train.entity.TrainLiveUser;

@Service
@Transactional(readOnly = false)
public class TrainLiveUserService  extends CrudService<TrainLiveUserDao,TrainLiveUser>{
	
	@Autowired
	private TrainLiveUserDao trainLiveUserDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public Page<TrainLiveUser> findLiveUser(Page<TrainLiveUser> page, TrainLiveUser trainLiveUser) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		trainLiveUser.setPage(page);
		// 执行分页查询
		page.setList(trainLiveUserDao.findList(trainLiveUser));
		return page;
	}
	
	/**
	 * 保存数据
	 * @param trainLiveAudit
	 * @return
	 */
	public int update(TrainLiveUser trainLiveUser){
		return trainLiveUserDao.update(trainLiveUser);
	}
	
	/**
	 * 导入会员数据
	 * @param trainLiveUser
	 * @return
	 */
	public int insertLiveUser(TrainLiveUser trainLiveUser){
		return trainLiveUserDao.insertLiveUser(trainLiveUser);
	}
	
	/**
	 * 查询用户数据
	 * @param mobile
	 * @return
	 */
	public User findByMobile(String mobile){
		return userDao.getByMobile(mobile);
	}
	
	/**
	 * 删除数据
	 * @param trainLiveUser
	 * @return
	 */
	public int deleteUser(TrainLiveUser trainLiveUser){
		return trainLiveUserDao.deleteUser(trainLiveUser);
	}
}
