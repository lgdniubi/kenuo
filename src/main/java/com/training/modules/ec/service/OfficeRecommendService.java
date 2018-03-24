package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.OfficeRecommendDao;
import com.training.modules.ec.entity.OfficeRecommend;
import com.training.modules.ec.entity.OfficeRecommendMapping;

/**
 * 店铺推荐Service
 * @author xiaoye  2017年9月12日
 *
 */
@Service
@Transactional(readOnly = false)
public class OfficeRecommendService extends CrudService<OfficeRecommendDao, OfficeRecommend>{
	
	@Autowired 
	private OfficeRecommendDao officeRecommendDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param officeRecommend
	 * @return
	 */
	public Page<OfficeRecommend> findList(Page<OfficeRecommend> page, OfficeRecommend officeRecommend) {
		
		// 设置分页参数
		officeRecommend.setPage(page);
		// 执行分页查询
		page.setList(officeRecommendDao.findList(officeRecommend));
		return page;
	}
	
	/**
	 * 根据id获取店铺推荐组
	 * @param officeRecommendId
	 * @return
	 */
	public OfficeRecommend getOfficeRecommend(int officeRecommendId){
		return officeRecommendDao.getOfficeRecommend(officeRecommendId);
	}
	
	/**
	 * 新增店铺推荐组
	 * @param officeRecommend
	 */
	public void insertOfficeRecommend(OfficeRecommend officeRecommend){
		officeRecommendDao.insertOfficeRecommend(officeRecommend);
	}
	
	
	/**
	 * 修改店铺推荐组 
	 * @param officeRecommend
	 */
	public void updateOfficeRecommend(OfficeRecommend officeRecommend){
		officeRecommendDao.updateOfficeRecommend(officeRecommend);
	}
	
	/**
	 * 逻辑删除店铺推荐组 
	 * @param officeRecommendId
	 */
	public void deleteOfficeRecommend(int officeRecommendId){
		officeRecommendDao.deleteOfficeRecommend(officeRecommendId);
	}
	
	/**
	 * 店铺推荐组是否显示
	 * @param officeRecommend
	 */
	public void updateIsShow(OfficeRecommend officeRecommend){
		officeRecommendDao.updateIsShow(officeRecommend);
	}
	
	/**
	 * 店铺推荐组对应的店铺列表
	 * @param page
	 * @param officeRecommendMapping
	 * @return
	 */
	public Page<OfficeRecommendMapping> findNewPage(Page<OfficeRecommendMapping> page,OfficeRecommendMapping officeRecommendMapping){
		officeRecommendMapping.setPage(page);
		page.setList(officeRecommendDao.selectOfficeForRecommend(officeRecommendMapping));
		return page;
	}
	
	/**
	 * 删除某一组推荐店铺组中的某个店铺
	 * @param OfficeRecommendMappingId
	 */
	public void delOffice(int OfficeRecommendMappingId){
		officeRecommendDao.delOffice(OfficeRecommendMappingId);
	}
	
	/**
	 * 店铺推荐组添加店铺
	 * @param OfficeRecommendMapping
	 */
	public void insertOffice(OfficeRecommendMapping OfficeRecommendMapping){
		officeRecommendDao.insertOffice(OfficeRecommendMapping);
	}
	
	/**
	 * 根据officeRecommendMappingId获得推荐组中的某个店铺
	 * @param officeRecommendMappingId
	 * @return
	 */
	public OfficeRecommendMapping getOfficeRecommendMapping(int officeRecommendMappingId){
		return officeRecommendDao.getOfficeRecommendMapping(officeRecommendMappingId);
	}
	
	/**
	 * 修改某组中某店铺的信息 
	 * @param officeRecommendMapping
	 */
	public void updateOfficeMessage(OfficeRecommendMapping officeRecommendMapping){
		officeRecommendDao.updateOfficeMessage(officeRecommendMapping);
	}
	
	/**
	 * 将所有的推荐组都设置为不显示
	 */
	public void changeAll(OfficeRecommend officeRecommend){
		officeRecommendDao.changeAll(officeRecommend);
	}
	
	/**
	 * 查询推荐组对应的推荐店铺id
	 * @param recommendId
	 * @return
	 */
	public String selectOfficeId(int recommendId){
		return officeRecommendDao.selectOfficeId(recommendId);
	}
	
}
