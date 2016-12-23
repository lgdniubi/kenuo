package com.training.modules.train.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.InvitationUserFZDao;
import com.training.modules.train.entity.InvitationUserFZ;

/**
 * 红包管理
 * @author water
 *
 */

@Service
@Transactional(readOnly = false)
public class InvitationUserFZService extends CrudService<InvitationUserFZDao,InvitationUserFZ>{
	
	@Autowired
	private InvitationUserFZDao invitationUserFZDao;


	
	
	/**
	 * 邀请明细分页查询
	 * @param page
	 * @param invitationUser分页查询
	 * @return
	 */
	public Page<InvitationUserFZ> findInvitationUser(Page<InvitationUserFZ> page, InvitationUserFZ invitationUserFZ) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		invitationUserFZ.setPage(page);
		// 执行分页查询
		page.setList(invitationUserFZDao.findList(invitationUserFZ));
		return page;
	}
	
	
	/**
	 * 被邀请人分页查询
	 * @param page
	 * @param user分页查询
	 * @return
	 */
	public List<InvitationUserFZ> findByList(InvitationUserFZ invitationUserFZ) {
		
		return invitationUserFZDao.findByList(invitationUserFZ);
		
	}
	

}
