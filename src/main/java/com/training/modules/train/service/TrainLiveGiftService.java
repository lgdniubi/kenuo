package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainLiveGiftDao;
import com.training.modules.train.entity.TrainLiveGift;

/**
 * 直播送的礼物Service
 * @author xiaoye  2017年3月16日
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainLiveGiftService extends CrudService<TrainLiveGiftDao,TrainLiveGift>{

	@Autowired
	private TrainLiveGiftDao trainLiveGiftDao;
	
	/**
	 * 分页查询直播送的礼物
	 * @param page
	 * @param trainLiveActivityRatio
	 * @return
	 */
	public Page<TrainLiveGift> findPage(Page<TrainLiveGift> page,TrainLiveGift trainLiveGift){
		trainLiveGift.setPage(page);
		page.setList(dao.findList(trainLiveGift));
		return page;
	}
	
	/**
	 * 根据 trainLiveGiftId查找相应的礼物
	 * @param trainLiveGiftId
	 * @return
	 */
	public TrainLiveGift getTrainLiveGift(int trainLiveGiftId){
		return trainLiveGiftDao.getTrainLiveGift(trainLiveGiftId);
	}
	
	/**
	 * 新增礼物
	 * @param trainLiveGift
	 */
	public void insert(TrainLiveGift trainLiveGift){
		User user = UserUtils.getUser();
		trainLiveGift.setCreateBy(user);
		trainLiveGiftDao.insert(trainLiveGift);
	}
	
	/**
	 * 更新礼物
	 * @param trainLiveGift
	 */
	public void update(TrainLiveGift trainLiveGift){
		User user = UserUtils.getUser();
		trainLiveGift.setUpdateBy(user);
		trainLiveGiftDao.update(trainLiveGift);
	}
	
	/**
	 * 逻辑删除礼物
	 * @param trainLiveGift
	 */
	public void deleteGift(int trainLiveGiftId){
		trainLiveGiftDao.deleteGift(trainLiveGiftId);
	}
	
	/**
	 * 更改礼物的状态
	 * @param trainLiveGift
	 */
	public void updateIsShow(TrainLiveGift trainLiveGift){
		trainLiveGiftDao.updateIsShow(trainLiveGift);
	}
	
	/**
	 * 查找处于显示状态的礼物的个数
	 * @return
	 */
	public int selectNum(){
		return trainLiveGiftDao.selectNum();
	}
	
	/**
	 * 更改礼物是否能连发
	 * @param trainLiveGift
	 */
	public void updateIsBatter(TrainLiveGift trainLiveGift){
		trainLiveGiftDao.updateIsBatter(trainLiveGift);
	}
}
