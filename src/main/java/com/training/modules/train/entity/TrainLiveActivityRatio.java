package com.training.modules.train.entity;

import com.training.common.persistence.DataEntity;

/**
 * 充值兑换配置表对应的活动实体类
 * @author xiaoye  2017年3月13日
 *
 */
public class TrainLiveActivityRatio extends DataEntity<TrainLiveActivityRatio>{

	private static final long serialVersionUID = 1L;
	
	private int trainLiveActivityRatioId;   //id
	private String name;                    //活动名称
	private String isShow;                  //是否显示
	
	private String flag;   //标识
	public int getTrainLiveActivityRatioId() {
		return trainLiveActivityRatioId;
	}
	public void setTrainLiveActivityRatioId(int trainLiveActivityRatioId) {
		this.trainLiveActivityRatioId = trainLiveActivityRatioId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
