package com.training.modules.train.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.BeautyWeekScore;

@MyBatisDao
public interface BeautyWeekScoreDao {

	public int insertWeekScore(BeautyWeekScore score);
	
	public List<BeautyWeekScore> queryWeekScoreByGroupByOfficeCode(Map<String,Object> map);
}
