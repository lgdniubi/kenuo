package com.training.modules.train.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.modules.train.dao.BeautyWeekScoreDao;
import com.training.modules.train.entity.BeautyWeekScore;

@Service
public class BeautyWeekScoreService {
	@Autowired
	private BeautyWeekScoreDao beautyWeekScoreDao;
	
	public int insertWeekScore(BeautyWeekScore score){
		return this.beautyWeekScoreDao.insertWeekScore(score);
	}
	public List<BeautyWeekScore> queryWeekScoreByGroupByOfficeCode(Map<String,Object> map){
		return this.beautyWeekScoreDao.queryWeekScoreByGroupByOfficeCode(map);
	}
}
