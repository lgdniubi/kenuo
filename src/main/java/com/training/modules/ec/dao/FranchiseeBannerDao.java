package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyFranchiseeBanner;

/**
 * 商家主页banner图接口
 * @author 土豆  2018.2.26
 *
 */

@MyBatisDao
public interface FranchiseeBannerDao extends CrudDao<MtmyFranchiseeBanner>{

	/**
	 * 根据ID查询数据
	 * @param mtmyFranchiseeBanner
	 * @return
	 */
	MtmyFranchiseeBanner getMtmyFranchiseeBannerById(MtmyFranchiseeBanner mtmyFranchiseeBanner);

	/**
	 * 修改商家主页banner图是否显示
	 * @param mtmyFranchiseeBanner
	 */
	void changIsShow(MtmyFranchiseeBanner mtmyFranchiseeBanner);
	
	/**
	 * 将当前商家的所有banner图都不显示
	 * @param franchiseeId
	 */
	public void changeAll(int franchiseeId);
	
	/**
	 * 查询某商家的最新创建的商家主页banner图
	 * @param franchiseeId
	 */
	public MtmyFranchiseeBanner getMtmyFranchiseeBannerByCreateDate(int franchiseeId);
	
}
