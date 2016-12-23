package com.training.modules.train.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.modules.train.dao.ShopWeekScoreDao;

@Service
public class ShopWeekScoreService {

	@Autowired
	private ShopWeekScoreDao shopWeekScoreDao;
	
	public int insertOfficeWeekScore(Map<String, Object> map){
		return this.shopWeekScoreDao.insertOfficeWeekScore(map);
	}
}
