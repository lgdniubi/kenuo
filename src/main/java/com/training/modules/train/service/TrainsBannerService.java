package com.training.modules.train.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.TrainsBannerDao;
import com.training.modules.train.entity.TrainsBanner;


/**
 * 对账Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainsBannerService extends CrudService<TrainsBannerDao, TrainsBanner>{
	
	/**
	 * 分页查询banner图
	 */
	public Page<TrainsBanner> findPage(Page<TrainsBanner> page,TrainsBanner trainsBanner){
		trainsBanner.setPage(page);
		page.setList(dao.findList(trainsBanner));
		return page;
	}
	/**
	 * 查询单个banner图详情
	 * @param bannerId
	 * @return
	 */
	public TrainsBanner getBanner(int adId){
		return dao.getBanner(adId);
	}
	/**
	 * 保存
	 */
	public void save(TrainsBanner trainsBanner){
		dao.insert(trainsBanner);
	}
	/**
	 * 修改
	 * @param trainsBanner
	 */
	public void update(TrainsBanner trainsBanner){
		dao.update(trainsBanner);
	}
	
	public void deleteBanner(TrainsBanner trainsBanner){
		dao.deleteBanner(trainsBanner);
	}
}
