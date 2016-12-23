package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.CourierDao;
import com.training.modules.ec.entity.Courier;
/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class CourierService extends TreeService<CourierDao,Courier> {
	
	@Autowired
	private CourierDao courierDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<Courier> findCourier(Page<Courier> page, Courier courier) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		courier.setPage(page);
		// 执行分页查询
		page.setList(courierDao.findList(courier));
		return page;
	}
	
	


}
