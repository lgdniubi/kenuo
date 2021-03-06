package com.training.modules.ec.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyEveryoneSayDao;
import com.training.modules.ec.entity.MtmyEveryoneSay;

/**
 * 大家都在说Service
 * @author xiaoye
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyEveryoneSayService extends CrudService<MtmyEveryoneSayDao, MtmyEveryoneSay>{
	
	@Autowired
	private MtmyEveryoneSayDao mtmyEveryoneSayDao;
	
	/**
	 * 根据id查找相应的说说
	 * @param mtmyEveryoneSayId
	 * @return
	 */
	public MtmyEveryoneSay getMtmyEveryoneSay(int mtmyEveryoneSayId){
		return mtmyEveryoneSayDao.getMtmyEveryoneSay(mtmyEveryoneSayId);
	}
	
	/**
	 * 根据id查找相应说说的回复
	 * @param mtmyEveryoneSayId
	 * @return
	 */
	public Page<MtmyEveryoneSay> getMtmyEveryoneSayForResponse(Page<MtmyEveryoneSay> page, MtmyEveryoneSay mtmyEveryoneSay){
		mtmyEveryoneSay.setPage(page);
		page.setList(mtmyEveryoneSayDao.getMtmyEveryoneSayForResponse(mtmyEveryoneSay));
		return page;
	}
	
	/**
	 * 逻辑删除说说
	 * @param mtmyEveryoneSayId
	 */
	public void delMtmyEveryoneSay(int mtmyEveryoneSayId){
		mtmyEveryoneSayDao.delMtmyEveryoneSay(mtmyEveryoneSayId);
	}
	
	/**
	 * 物理删除说说对应的回 
	 * @param parentId
	 * @param mtmyEveryoneSayId
	 */
	public void deleteResponse(@Param(value="parentId")String parentId,@Param(value="mtmyEveryoneSayId")int mtmyEveryoneSayId){
		mtmyEveryoneSayDao.deleteResponse(parentId, mtmyEveryoneSayId);
	}

	/**
	 * 修改说说是否显示
	 * @param mtmyEveryoneSay
	 */
	public void updateIsShow(MtmyEveryoneSay mtmyEveryoneSay) {
		mtmyEveryoneSayDao.updateIsShow(mtmyEveryoneSay);
	}
}
