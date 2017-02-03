package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Activity;
/**
 * 活动dao
 * @author yangyang
 *
 */

@MyBatisDao
public interface ActivityDao extends TreeDao<Activity> {
	
	
	/**
	 * 保存数据
	 * @param activity
	 * @return
	 */
	public int insertAction(Activity activity);
	
	/**
	 * 活动开启关闭
	 * @param activity
	 * @return
	 */
	public int updateStatus(Activity activity);
	
	/**
	 * 查询红包是否有领取记录
	 * @param id
	 * @return
	 */
	public int numByCouponId(int id);
	
	/**
	 * 查询活动过期
	 * @return
	 */
	public List<Activity> selectActionCloseTime();
	/**
	 * 更改过期状态
	 * @param id
	 * @return
	 */
	public int updateOutTime(int id);

}
