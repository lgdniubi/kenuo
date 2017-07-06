package com.training.modules.train.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainActivityCourse;
import com.training.modules.train.entity.TrainActivityCourseContent;
import com.training.modules.train.entity.TrainOrder;

/**
 * 妃子校活动课程Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface TrainActivityCourseDao extends CrudDao<TrainActivityCourse>{
	
	/**
	 * 保存妃子校活动关联的内容
	 * @param list
	 */
	public void saveContent(List<TrainActivityCourseContent> list);
	
	/**
	 * 修改课程活动状态
	 * @param acId
	 * @param flag
	 * @param isyesno
	 * @return
	 */
	public int updateFlag(@Param(value="acId")int acId,@Param(value="flag")String flag,@Param(value="isyesno")int isyesno,@Param(value="userId")String userId);
	
	/**
	 * 删除妃子校活动关联的内容
	 * @param acId
	 */
	public void delContent(int acId);
	
	/**
	 * 查询订单列表
	 * @return
	 */
	public List<TrainOrder> findTrainOrder(TrainOrder trainOrder);
}
