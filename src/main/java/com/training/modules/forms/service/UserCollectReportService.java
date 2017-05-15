package com.training.modules.forms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.forms.dao.UserCollectReportDao;
import com.training.modules.forms.entity.UserCollectReport;
import com.training.modules.forms.entity.UserInfoReport;

/**
 * 妃子校用户汇总
 * @author stone
 * @date 2017年4月24日
 */
@Service
@Transactional(readOnly = false)
public class UserCollectReportService extends CrudService<UserCollectReportDao,UserCollectReport>{
	
	@Autowired
	private UserCollectReportDao userCollectReportDao;
	
	/**妃子校用户汇总
	 * 分页查询
	 * @param page
	 * @param userCollectReport
	 * @return
	 */
	public Page<UserCollectReport> collectListByTime(Page<UserCollectReport> page, UserCollectReport userCollectReport) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//goodsReport.getSqlMap().put("dsf", dataScopeFilter(orders.getCurrentUser(), "o", "a"));
		userCollectReport.getSqlMap().put("dsf", dataScopeFilter(userCollectReport.getCurrentUser(),"o"));
		// 设置分页参数
		userCollectReport.setPage(page);
		// 执行查询
		page.setList(userCollectReportDao.collectListByTime(userCollectReport));
		return page;
	}

	/**妃子校用户汇总
	 * 无条件分页查询
	 * @param page
	 * @param userCollectReport
	 * @return
	 */
	public Page<UserCollectReport> firstCollectList(Page<UserCollectReport> page, UserCollectReport userCollectReport) {
		// 设置分页参数
		userCollectReport.setPage(page);
		// 执行查询
		page.setList(userCollectReportDao.firstCollectList(userCollectReport));
		return page;
	}

	/**妃子校用户信息
	 * 分页查询
	 * @param page
	 * @param userCollectReport
	 * @return
	 */
	public Page<UserInfoReport> infoList(Page<UserInfoReport> page, UserInfoReport userInfoReport) {
		// 设置分页参数
		userInfoReport.setPage(page);
		// 执行查询
		page.setList(userCollectReportDao.infoList(userInfoReport));
		return page;
	}

}
