package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveActivityRatio;
import com.training.modules.train.entity.TrainLiveExchangeRatio;

/**
 * 充值兑换配置表对应的活动Dao
 * @author xiaoye  2017年3月13日
 *
 */
@MyBatisDao
public interface TrainLiveActivityRatioDao extends CrudDao<TrainLiveActivityRatio>{
	/**
	 * 根据id找到对应的TrainLiveActivityRatio
	 * @param trainLiveActivityRatioId
	 * @return
	 */
	public TrainLiveActivityRatio getTrainLiveActivityRatio(int trainLiveActivityRatioId);
	
	/**
	 * 逻辑删除充值兑换配置表对应的活动 同时删除对应的兑换比例配置
	 * @param trainLiveActivityRatioId
	 */
	public void deleteTrainLiveActivityRatio(int trainLiveActivityRatioId);
	public void deleteActivityExchangeRatio(int trainLiveActivityRatioId);
	
	/**
	 * 将所有的活动的状态都修改为1，不可用 
	 */
	public void updateAll();
	
	/**
	 * 修改某一组的状态
	 * @param trainLiveActivityRatio
	 */
	public void updateIsShow(TrainLiveActivityRatio trainLiveActivityRatio);
	
	/**
	 * 查询出最近创建的那一组的id
	 */
	public int selectIdByCreateDate();
	
	/**
	 * 查询直播兑换比例配置
	 * @return
	 */
	public List<TrainLiveExchangeRatio> newFindList(@Param(value="activityId")int activityId,@Param(value="flag")String flag);
	
	/**
	 * 修改兑换比例配置的是否显示状态
	 * @param trainLiveExchangeRatio
	 */
	public void changeIsShow(TrainLiveExchangeRatio trainLiveExchangeRatio);
	
	/**
	 * 根据id找到对应的兑换比例配置
	 * @param exchangeRatioId
	 * @return
	 */
	public TrainLiveExchangeRatio getTrainLiveExchangeRatio(int exchangeRatioId);
	
	/**
	 * 新增兑换比例配置
	 * @param trainLiveExchangeRatio
	 */
	public void insertExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio);
	
	/**
	 * 更新兑换比例配置 
	 * @param trainLiveExchangeRatio
	 */
	public void updateExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio);
	
	/**
	 * 逻辑删除某一兑换比例配置 
	 * @param trainLiveExchangeRatio
	 */
	public void delExchangeRatio(TrainLiveExchangeRatio trainLiveExchangeRatio);
	
	/**
	 * 查找处于显示状态的兑换比例配置的个数
	 * @param trainLiveExchangeRatio
	 * @return
	 */
	public int selectNum(TrainLiveExchangeRatio trainLiveExchangeRatio);
}
