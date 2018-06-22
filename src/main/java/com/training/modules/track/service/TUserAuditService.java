package com.training.modules.track.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.track.dao.ITUserAuditDao;
import com.training.modules.track.entity.TUserAudit;

/**
 * 类名称：	TGoodsService
 * 类描述：	神策埋点-商品相关Service层
 * 创建人： 	bigdata
 * 创建时间：    	2018年1月18日 下午2:16:20
 */
@Service
@Transactional(readOnly = false)
public class TUserAuditService extends CrudService<ITUserAuditDao,TUserAudit>{

	@Autowired
	private ITUserAuditDao iTUserAuditDao;
	
	/**
	 * 方法说明：	查询用户审核详情
	 * 创建时间：	2018年6月14日 下午3:59:30
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年6月14日 下午3:59:30
	 * @param map
	 * @return
	 */
	public TUserAudit queryUserAuditDetails(Map<String, Object> map) {
		return iTUserAuditDao.queryUserAuditDetails(map);
	}
}
