package com.training.modules.track.dao;

import java.util.Map;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.track.entity.TApptOrder;

/**
 * 类名称：	ITApptOrderDao
 * 类描述：	埋点-预约Dao层
 * 创建人： 	bigdata
 * 创建时间：    	2018年7月14日14:16:15
 */
@MyBatisDao
public interface ITApptOrderDao {

	/**
	 * 方法说明：	根据预约ID，查询预约订单详情
	 * 创建时间：	2018年1月22日 下午3:02:55
	 * 创建人：	bigdata
	 * 修改记录：	修改人	修改记录	2018年1月22日 下午3:02:55
	 * @param apptId
	 * @return
	 */
	public TApptOrder queryApptOrderDetail(Map<String, Object> map);
	
	/**
	 * 方法说明：	根据预约ID，查询预约详情
	 * 创建时间：	2018年8月21日10:32:37
	 * 创建人：	xiaoye
	 * @param apptId
	 * @return
	 */
	public TApptOrder queryApptDetail(Map<String, Object> map);
}
