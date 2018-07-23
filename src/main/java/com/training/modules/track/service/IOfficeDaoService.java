package com.training.modules.track.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.modules.track.dao.ITOfficeDao;
import com.training.modules.track.entity.TOffice;

/**
 * 类名称：  	IOfficeDaoService
 * 类描述：	埋点-机构Service层
 * 创建人：  	kele
 * 创建时间：	2018年7月14日 下午4:47:45
 */
@Service
@Transactional(readOnly = false)
public class IOfficeDaoService {

	@Autowired
	private ITOfficeDao iTOfficeDao;
	
	/**
	 * 方法说明：	查询机构信息
	 * 创建时间：	2018年7月14日 下午4:46:05
	 * 创建人：	kele
	 * 修改记录：	修改人	修改记录	2018年7月14日 下午4:46:05
	 * @param map
	 * @return
	 */
	public TOffice queryOfficeDetail(Map<String, Object> map) {
		return iTOfficeDao.queryOfficeDetail(map);
	}
	
}
