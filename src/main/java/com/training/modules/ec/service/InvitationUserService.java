package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.InvitationUserDao;
import com.training.modules.ec.entity.InvitationUser;

/**
 * 红包管理
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class InvitationUserService extends CrudService<InvitationUserDao,InvitationUser>{
	
	@Autowired
	private InvitationUserDao invitationUserDao;


	
	
	/**
	 * 邀请明细分页查询
	 * @param page
	 * @param invitationUser分页查询
	 * @return
	 */
	public Page<InvitationUser> findInvitationUser(Page<InvitationUser> page, InvitationUser invitationUser) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		invitationUser.setPage(page);
		// 执行分页查询
		page.setList(invitationUserDao.findList(invitationUser));
		return page;
	}
	
	
	/**
	 * 被邀请人分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public List<InvitationUser> findByList(InvitationUser invitationUser) {
		
		return invitationUserDao.findByList(invitationUser);
		
	}
	

}
