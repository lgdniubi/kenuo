package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyCategoryRecommended;
import com.training.modules.ec.entity.MtmyFranchiseeBanner;

/**
 * 品类推荐接口
 * @author 蜜蜂  2018.12.1
 *
 */

@MyBatisDao
public interface CategoryRecommendedDao extends CrudDao<MtmyCategoryRecommended>{

//	/**
//	 * 根据ID查询数据
//	 * @param mtmyFranchiseeBanner
//	 * @return
//	 */
//	MtmyFranchiseeBanner getMtmyFranchiseeBannerById(MtmyFranchiseeBanner mtmyFranchiseeBanner);
//
//	/**
//	 * 修改商家主页banner图是否显示
//	 * @param mtmyFranchiseeBanner
//	 */
//	void changIsShow(MtmyFranchiseeBanner mtmyFranchiseeBanner);
//	
//	/**
//	 * 将当前商家的所有banner图都不显示
//	 * @param franchiseeId
//	 */
//	public void changeAll(int franchiseeId);
//	
//	/**
//	 * 查询某商家的最新创建的商家主页banner图
//	 * @param franchiseeId
//	 */
//	public MtmyFranchiseeBanner getMtmyFranchiseeBannerByCreateDate(int franchiseeId);

}
