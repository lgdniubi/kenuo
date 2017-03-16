package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainLiveActivityRatioDao;
import com.training.modules.train.entity.TrainLiveActivityRatio;
import com.training.modules.train.entity.TrainLiveExchangeRatio;

/**
 * 充值兑换配置表对应的活动Service
 * @author xiaoye  2017年3月13日
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainLiveActivityRatioService extends CrudService<TrainLiveActivityRatioDao,TrainLiveActivityRatio>{
	
	@Autowired
	private TrainLiveActivityRatioDao trainLiveActivityRatioDao;
	/**
	 * 分页查询 充值兑换配置表对应的活动
	 * @param page
	 * @param trainLiveActivityRatio
	 * @return
	 */
	public Page<TrainLiveActivityRatio> findPage(Page<TrainLiveActivityRatio> page,TrainLiveActivityRatio trainLiveActivityRatio){
		trainLiveActivityRatio.setPage(page);
		page.setList(dao.findList(trainLiveActivityRatio));
		return page;
	}
	
	/**
	 * 根据id找到对应的TrainLiveActivityRatio
	 * @param trainLiveActivityRatioId
	 * @return
	 */
	public TrainLiveActivityRatio getTrainLiveActivityRatio(int trainLiveActivityRatioId){
		return trainLiveActivityRatioDao.getTrainLiveActivityRatio(trainLiveActivityRatioId);
	}
	
	/**
	 * 保存TrainLiveActivityRatio
	 * @param trainLiveActivityRatio
	 */
	public void insert(TrainLiveActivityRatio trainLiveActivityRatio){
		User user = UserUtils.getUser();
		trainLiveActivityRatio.setCreateBy(user);
		dao.insert(trainLiveActivityRatio);
	}
	
	/**
	 * 修改TrainLiveActivityRatio
	 * @param trainLiveActivityRatio
	 */
	public void update(TrainLiveActivityRatio trainLiveActivityRatio){
		User user = UserUtils.getUser();
		trainLiveActivityRatio.setUpdateBy(user);
		dao.update(trainLiveActivityRatio);
	}
	
	/**
	 * 逻辑删除充值兑换配置表对应的活动 同时删除对应的兑换比例配置
	 * @param trainLiveActivityRatioId
	 */
	public void deleteTrainLiveActivityRatio(int trainLiveActivityRatioId){
		trainLiveActivityRatioDao.deleteTrainLiveActivityRatio(trainLiveActivityRatioId);
		trainLiveActivityRatioDao.deleteActivityExchangeRatio(trainLiveActivityRatioId);
	}
	
	/**
	 * 将所有的活动的状态都修改为1，不可用 
	 */
	public void updateAll(){
		trainLiveActivityRatioDao.updateAll();
	}
	
	/**
	 * 修改某一组的状态
	 * @param trainLiveActivityRatio
	 */
	public void updateIsShow(TrainLiveActivityRatio trainLiveActivityRatio){
		trainLiveActivityRatioDao.updateIsShow(trainLiveActivityRatio);
	}
	
	/**
	 * 查询出最近创建的那一组的id
	 */
	public int selectIdByCreateDate(){
		return trainLiveActivityRatioDao.selectIdByCreateDate();
	}
	
	/**
	 * 查询 直播兑换比例配置
	 * @return
	 */
	public List<TrainLiveExchangeRatio> newFindPage(int activityId,String flag){
		return trainLiveActivityRatioDao.newFindList(activityId,flag);
	}
	
	/**
	 * 修改兑换比例配置的是否显示状态
	 * @param trainLiveExchangeRatio
	 */
	public void changeIsShow(TrainLiveExchangeRatio trainLiveExchangeRatio){
		trainLiveActivityRatioDao.changeIsShow(trainLiveExchangeRatio);
	}
	
	/**
	 * 根据id找到对应的兑换比例配置
	 * @param exchangeRatioId
	 * @return
	 */
	public TrainLiveExchangeRatio getTrainLiveExchangeRatio(int exchangeRatioId){
		return trainLiveActivityRatioDao.getTrainLiveExchangeRatio(exchangeRatioId);
	}
	
	/**
	 * 新增兑换比例配置
	 * @param trainLiveExchangeRatio
	 */
	public void insertExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio){
		User user = UserUtils.getUser();
		trainLiveExchangeRatio.setCreateBy(user);
		trainLiveActivityRatioDao.insertExchangeRatio(trainLiveExchangeRatio);
	}
	
	/**
	 * 更新兑换比例配置 
	 * @param trainLiveExchangeRatio
	 */
	public void updateExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio){
		User user = UserUtils.getUser();
		trainLiveExchangeRatio.setUpdateBy(user);
		trainLiveActivityRatioDao.updateExchangeRatio(trainLiveExchangeRatio);
	}
	
	/**
	 * 逻辑删除某一兑换比例配置 
	 * @param trainLiveExchangeRatio
	 */
	public void delExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio){
		trainLiveActivityRatioDao.delExchangeRatio(trainLiveExchangeRatio);
	}
	
	/**
	 * 查找处于显示状态的兑换比例配置的个数
	 * @param exchangeType
	 * @return
	 */
	public int selectNum(TrainLiveExchangeRatio trainLiveExchangeRatio){
		return trainLiveActivityRatioDao.selectNum(trainLiveExchangeRatio);
	}
}
