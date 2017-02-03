package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.train.entity.CategoryLesson;
import com.training.modules.train.entity.TrainCategorys;

/**
 * 课程分类DAO
 * @author kele
 * @version 2016年3月14日
 */
@MyBatisDao
public interface TrainCategorysDao extends CrudDao<TrainCategorys>{
	
	/**
	 * 根据优先级，查询课程分类
	 * @param trainCategorys
	 * @return
	 */
	public List<TrainCategorys> findcategoryslist(TrainCategorys trainCategorys);
	
	/**
	 * 通告管理-考试推送-树状二级（一级分类、二级分类）
	 * @return
	 */
	public List<TrainCategorys> findCategoryLesson(TrainCategorys trainCategorys);
	
	/**
	 * 通告管理-课程推送-树状三级（一级分类、二级分类、课程）
	 * @param categoryId
	 * @return
	 */
	public List<CategoryLesson> findCategoryLessonid(String categoryId);
	
	/**
	 * 通告管理-查看功能-获取分类名称
	 * @param oaNotify
	 * @return
	 */
	public OaNotify findbyid(OaNotify oaNotify);
	
	/**
	 * 分类数据权限发生变化时   同步更新课程、试题 
	 * @param oldOfficeId
	 * @param newOfficeId
	 */
	public void syncUpdateLesson(@Param("oldOfficeId")String oldOfficeId,@Param("newOfficeId")String newOfficeId);
	public void syncUpdateExam(@Param("oldOfficeId")String oldOfficeId,@Param("newOfficeId")String newOfficeId);
	/**
	 * 修改分类状态
	 * @param trainCategorys
	 */
	public void updateIsShow(@Param(value="ids")String[] ids,@Param(value="isShow")int isShow);
	
}
