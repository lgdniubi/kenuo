package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CategoryRecommendedDao;
import com.training.modules.ec.dao.FranchiseeBannerDao;
import com.training.modules.ec.entity.MtmyCategoryRecommended;
import com.training.modules.ec.entity.MtmyFranchiseeBanner;

/**
 * 品类推荐的Service
 * @author 蜜蜂 
 *
 */
@Service
@Transactional(readOnly = false)
public class CategoryRecommendedService extends CrudService<CategoryRecommendedDao, MtmyCategoryRecommended>{
	
	@Autowired
	private CategoryRecommendedDao categoryRecommendedDao;
	
	/**
	 * 分页查询品类推荐
	 */
	public Page<MtmyCategoryRecommended> findPage(Page<MtmyCategoryRecommended> page,MtmyCategoryRecommended mtmyCategoryRecommended){
		mtmyCategoryRecommended.setPage(page);
		page.setList(dao.findList(mtmyCategoryRecommended));
		return page;
	}
//	/**
//	 * 根据ID查询数据
//	 * @param mtmyFranchiseeBanner
//	 * @return
//	 */
//	public MtmyFranchiseeBanner getMtmyFranchiseeBannerById(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
//		return franchiseeBannerDao.getMtmyFranchiseeBannerById(mtmyFranchiseeBanner);
//	}
//	/**
//	 * 根据id修改商家主页banner图
//	 * @param mtmyFranchiseeBanner
//	 */
//	public void update(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
//		franchiseeBannerDao.update(mtmyFranchiseeBanner);
//	}
//	
//	/**
//	 * 修改商家主页banner图是否显示
//	 * @param appStartPage
//	 * @return
//	 */
//	public void changIsShow(MtmyFranchiseeBanner mtmyFranchiseeBanner) {
//		franchiseeBannerDao.changeAll(mtmyFranchiseeBanner.getFranchiseeId());
//		if("1".equals(mtmyFranchiseeBanner.getIsShow())){
//			franchiseeBannerDao.changIsShow(mtmyFranchiseeBanner);
//		}else{
//			mtmyFranchiseeBanner = franchiseeBannerDao.getMtmyFranchiseeBannerByCreateDate(mtmyFranchiseeBanner.getFranchiseeId());
//			mtmyFranchiseeBanner.setIsShow("1");
//			franchiseeBannerDao.changIsShow(mtmyFranchiseeBanner);
//		}
//	}
}
