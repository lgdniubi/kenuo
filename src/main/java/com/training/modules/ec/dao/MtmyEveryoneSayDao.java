package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyEveryoneSay;

/**
 * 大家都在说Dao
 * @author xiaoye   2017年6月7日
 *
 */
@MyBatisDao
public interface MtmyEveryoneSayDao extends CrudDao<MtmyEveryoneSay>{
	
	/**
	 * 根据id查找相应的说说
	 * @param mtmyEveryoneSayId
	 * @return
	 */
	public MtmyEveryoneSay getMtmyEveryoneSay(int mtmyEveryoneSayId);
	
	/**
	 * 根据id查找相应说说的回复
	 * @param mtmyEveryoneSayId
	 * @return
	 */
	public List<MtmyEveryoneSay> getMtmyEveryoneSayForResponse(MtmyEveryoneSay mtmyEveryoneSay);
	
	/**
	 * 逻辑删除说说
	 * @param mtmyEveryoneSayId
	 */
	public void delMtmyEveryoneSay(int mtmyEveryoneSayId);
}
