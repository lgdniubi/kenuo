package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainActivityCourseDao;
import com.training.modules.train.entity.TrainActivityCourse;
import com.training.modules.train.entity.TrainActivityCourseContent;
import com.training.modules.train.entity.TrainOrder;


/**
 * 妃子校活动课程service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainActivityCourseService extends CrudService<TrainActivityCourseDao,TrainActivityCourse>{
	
	/**
	 * 妃子校活动课程list
	 * @param page
	 * @param fzxMenu
	 * @return
	 */
	public Page<TrainActivityCourse> findList(Page<TrainActivityCourse> page, TrainActivityCourse trainActivityCourse) {
		// 设置分页参数
		trainActivityCourse.setPage(page);
		// 执行分页查询
		page.setList(dao.findList(trainActivityCourse));
		return page;
	}
	
	/**
	 * 保存妃子校活动关联的内容
	 * @param list
	 */
	public void saveContent(List<TrainActivityCourseContent> list,int acId){
		// 保存内容前先删除
		dao.delContent(acId);
		dao.saveContent(list);
	}
	/**
	 * 修改课程活动状态
	 * @param acId
	 * @param flag
	 * @param isyesno
	 * @return
	 */
	public int updateFlag(String acId,String flag,String isyesno){
		String userId = UserUtils.getUser().getId();
		return dao.updateFlag(Integer.valueOf(acId), flag, Integer.valueOf(isyesno),userId);
	}
	
	/**
	 * 查询订单列表
	 * @return
	 */
	public Page<TrainOrder> findTrainOrder(Page<TrainOrder> page,TrainOrder trainOrder){
		trainOrder.setPage(page);
		page.setList(dao.findTrainOrder(trainOrder));
		return page;
	}

	/**
	 * 保存课程
	 * @param trainActivityCourse
	 */
	public void saveCourse(TrainActivityCourse trainActivityCourse) {
		super.save(trainActivityCourse);
		if(trainActivityCourse.getIsOpen() == 1){
			int acId = trainActivityCourse.getAcId();
			String[] cids = trainActivityCourse.getFranchiseeId().split(",");
			dao.insertCourseOffice(acId,cids);
		}
		
	}

	/**
	 * 通过课程id查询商家id
	 * @param acId
	 * @return
	 */
	public String findCompanyIds(int acId) {
		return dao.findCompanyIds(acId);
	}
}
