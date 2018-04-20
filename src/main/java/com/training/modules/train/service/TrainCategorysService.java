package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.train.dao.TrainCategorysDao;
import com.training.modules.train.entity.CategoryLesson;
import com.training.modules.train.entity.TrainCategorys;

/**
 * 课程分类Service
 * @author kele
 * @version 2016年3月14日
 */
@Service
@Transactional(readOnly = false)
public class TrainCategorysService extends CrudService<TrainCategorysDao,TrainCategorys> {
	
	@Autowired
	private TrainCategorysDao tCategorysDao;
	/**
	 * 根据优先级，查询课程分类
	 * @param priority	优先级（1：一级分类； 2：二级分类）
	 * @return
	 */
	public List<TrainCategorys> findcategoryslist(TrainCategorys trainCategorys){
		List<TrainCategorys> list = dao.findcategoryslist(trainCategorys);
		return list;
	}
	
	/**
	 * 修改课程分类
	 * @param trainCategorys
	 * @return
	 */
	public int updatecategorys(TrainCategorys trainCategorys){
		return dao.update(trainCategorys);
	}
	
	/**
	 * 删除课程分类
	 * @param trainCategorys
	 * @return
	 */
	public int deletecategorys(TrainCategorys trainCategorys){
		return dao.delete(trainCategorys);
	}
	
//	/**
//	 * 树形两级
//	 * @return
//	 */
//	public List<TrainCategorys> findList(TrainCategorys trainCategorys){
//		return dao.findCategoryLesson(trainCategorys);
//	}
	/**
	 * 树形三级
	 * @param categoryId
	 * @return
	 */
	public List<CategoryLesson> findCategoryLessonid(String categoryId){
		return dao.findCategoryLessonid(categoryId);
	}
	
	public OaNotify findbyid(OaNotify oaNotify){
		return dao.findbyid(oaNotify);
	}
	/**
	 * 
	 * @param oldOfficeId
	 * @param newOfficeId
	 */
	public void syncUpdate(String oldOfficeId,String newOfficeId){
		dao.syncUpdateExam(oldOfficeId, newOfficeId);
		dao.syncUpdateLesson(oldOfficeId, newOfficeId);
	}
	/**
	 * 修改分类状态
	 * @param trainCategorys
	 */
	public void updateIsShow(String[] ids,int isShow){
		dao.updateIsShow(ids,isShow);
	}
	/**
	 * 修改分类状态
	 * @param trainCategorys
	 */
	public void updateIsOpen(String[] ids,int isOpen){
		dao.updateIsOpen(ids,isOpen);
	}

	/**
	 * 
	 * @Title: findsonCategoryslist
	 * @Description: TODO 根据商家id和以及分类查询二级分类
	 * @throws
	 * 2018年1月26日 兵子
	 */
	public List<TrainCategorys> findOneCategoryslist(TrainCategorys trainCategorys) {
		return tCategorysDao.findOneCategoryslist(trainCategorys);
	}
}
