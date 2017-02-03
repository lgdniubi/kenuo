package com.training.modules.train.dao;

import java.util.Map;

import com.training.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface ShopWeekScoreDao {

	public int insertOfficeWeekScore(Map<String, Object> map);
}
