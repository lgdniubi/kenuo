package com.training.modules.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainsBannerDao;
import com.training.modules.train.entity.FranchiseeBanner;
import com.training.modules.train.entity.TrainsBanner;


/**
 * 对账Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class TrainsBannerService extends CrudService<TrainsBannerDao, TrainsBanner>{
	
	@Autowired
	private TrainsBannerDao trainsBannerDao;
	
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
	public void save(TrainsBanner trainsBanner,FranchiseeBanner fBanner){
		trainsBannerDao.insertBanner(trainsBanner);
		String soFranchiseeIds = trainsBanner.getSoFranchiseeIds();
		fBanner.setBannerId(trainsBanner.getAdId());
		if (StringUtils.isNotBlank(trainsBanner.getUser().getCompany().getId()) && "1".equals(trainsBanner.getUser().getCompany().getId())) {
			String[] soFIds = soFranchiseeIds.split(",");
			for (String soFId : soFIds) {
				fBanner.setSoFranchisee(Integer.valueOf(soFId));
				trainsBannerDao.insertFranchiseeBanner(fBanner);
			}
		}else{
			fBanner.setSoFranchisee(fBanner.getCreateFranchisee());
			trainsBannerDao.insertFranchiseeBanner(fBanner);
		}
	}
	/**
	 * 修改
	 * @param trainsBanner
	 * @param fBanner 
	 */
	public void update(TrainsBanner trainsBanner, FranchiseeBanner fBanner){
		Integer num = dao.update(trainsBanner);
		fBanner.setBannerId(trainsBanner.getAdId());
		if (num > 0 && num != null) {
			fBanner.setCreateFranchisee(Integer.valueOf(trainsBanner.getUser().getCompany().getId()));
			trainsBannerDao.delteFranchiseeBanner(fBanner);
			if (StringUtils.isNotBlank(trainsBanner.getUser().getCompany().getId()) && "1".equals(trainsBanner.getUser().getCompany().getId())) {
				String[] soFIds = trainsBanner.getSoFranchiseeIds().split(",");
				for (String soId : soFIds) {
					fBanner.setSoFranchisee(Integer.valueOf(soId));
					trainsBannerDao.insertFranchiseeBanner(fBanner);
				}
			}else{
				fBanner.setSoFranchisee(fBanner.getCreateFranchisee());
				trainsBannerDao.insertFranchiseeBanner(fBanner);
			}
		}
	}
	
	public void deleteBanner(TrainsBanner trainsBanner){
		dao.deleteBanner(trainsBanner);
	}
	/**
	 * 保存广告图操作记录
	 * @param trainsBanner
	 */
	public void saveAdvertLog(TrainsBanner trainsBanner) {
		User user = UserUtils.getUser();
		trainsBanner.setCreateBy(user);
		dao.saveAdvertLog(trainsBanner);
	}
	
	/**
	 * 查找日志列表
	 * @param page
	 * @param trainsBanner
	 * @return
	 */
	public Page<TrainsBanner> findLogPage(Page<TrainsBanner> page, TrainsBanner trainsBanner) {
		trainsBanner.setPage(page);
		page.setList(dao.findLogList(trainsBanner));
		return page;
	}
}
