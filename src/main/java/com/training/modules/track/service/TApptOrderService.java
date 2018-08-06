package com.training.modules.track.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.modules.track.dao.ITApptOrderDao;
import com.training.modules.track.entity.TApptOrder;

/**
 * 类名称：	TApptOrderService
 * 类描述：	埋点-预约Service层
 * 创建人： 	kele
 * 创建时间：    	22018年7月14日 下午2:19:45
 */
@Service
@Transactional(readOnly = false)
public class TApptOrderService {

	@Autowired
	private ITApptOrderDao iTApptOrderDao;
	
	/**
	 * 方法说明：	根据预约ID，查询预约订单详情
	 * 创建时间：	2018年7月14日 下午2:19:45
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午2:19:45
	 * @param map
	 * @return
	 */
	public TApptOrder queryApptOrderDetail(Map<String, Object> map) {
		return iTApptOrderDao.queryApptOrderDetail(map);
	}
	
}
