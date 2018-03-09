package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.FranchiseeBannerDao;
import com.training.modules.ec.entity.MtmyFranchiseeBanner;

/**
 * 商家主页banner的Service
 * @author 土豆  2018.2.26
 *
 */
@Service
@Transactional(readOnly = false)
public class FranchiseeBannerService extends CrudService<FranchiseeBannerDao, MtmyFranchiseeBanner>{
	
	@Autowired
	private FranchiseeBannerDao franchiseeBannerDao;
	
	/**
	 * 分页查询启动页广告图
	 */
	public Page<MtmyFranchiseeBanner> findPage(Page<MtmyFranchiseeBanner> page,MtmyFranchiseeBanner mtmyFranchiseeBanner){
		mtmyFranchiseeBanner.setPage(page);
		page.setList(dao.findList(mtmyFranchiseeBanner));
		return page;
	}
	/**
	 * 根据ID查询数据
	 * @param mtmyFranchiseeBanner
	 * @return
	 */
	public MtmyFranchiseeBanner getMtmyFranchiseeBannerById(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
		return franchiseeBannerDao.getMtmyFranchiseeBannerById(mtmyFranchiseeBanner);
	}
	/**
	 * 根据id修改商家主页banner图
	 * @param mtmyFranchiseeBanner
	 */
	public void update(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
		franchiseeBannerDao.update(mtmyFranchiseeBanner);
	}
	
	/**
	 * 修改商家主页banner图是否显示
	 * @param appStartPage
	 * @return
	 */
	public void changIsShow(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
		franchiseeBannerDao.changeAll(mtmyFranchiseeBanner.getFranchiseeId());
		if("1".equals(mtmyFranchiseeBanner.getIsShow())){
			franchiseeBannerDao.changIsShow(mtmyFranchiseeBanner);
		}else{
			mtmyFranchiseeBanner = franchiseeBannerDao.getMtmyFranchiseeBannerByCreateDate(mtmyFranchiseeBanner.getFranchiseeId());
			mtmyFranchiseeBanner.setIsShow("1");
			franchiseeBannerDao.changIsShow(mtmyFranchiseeBanner);
		}
	}
}
