package com.training.modules.ec.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.BannerDao;
import com.training.modules.ec.entity.Banner;


/**
 * 对账Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class BannerService extends CrudService<BannerDao, Banner>{
	
	/**
	 * 分页查询banner图
	 */
	public Page<Banner> findPage(Page<Banner> page,Banner banner){
		banner.setPage(page);
		page.setList(dao.findList(banner));
		return page;
	}
	/**
	 * 查询单个banner图详情
	 * @param bannerId
	 * @return
	 */
	public Banner get(Banner banner){
		return dao.get(banner);
	}
	/**
	 * 保存
	 */
	public void save(Banner banner){
		dao.insert(banner);
	}
	/**
	 * 修改
	 * @param banner
	 */
	public void update(Banner banner){
		dao.update(banner);
	}
	
	/**
	 * 修改banner标识，是否（显示、分享）
	 * @param banner
	 */
	public void updateflag(Banner banner){
		dao.updateflag(banner);
	}
}
