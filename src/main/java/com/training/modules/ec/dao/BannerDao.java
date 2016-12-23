package com.training.modules.ec.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Banner;


/**
 * banner图接口
 * @author coffee
 *
 */
@MyBatisDao
public interface BannerDao extends CrudDao<Banner>{
	/**
	 * 修改banner标识，是否（显示、分享）
	 * @param banner
	 */
	public void updateflag(Banner banner);
}
