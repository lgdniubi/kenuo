package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.OfficeRecommend;
import com.training.modules.ec.entity.OfficeRecommendMapping;

/**
 * 店铺推荐Dao
 * @author xiaoye 2017年9月12日
 *
 */
@MyBatisDao
public interface OfficeRecommendDao extends CrudDao<OfficeRecommend> {
	
	/**
	 * 根据id获取店铺推荐组
	 * @param officeRecommendId
	 * @return
	 */
	public OfficeRecommend getOfficeRecommend(int officeRecommendId);
	
	/**
	 * 新增店铺推荐组
	 * @param officeRecommend
	 */
	public void insertOfficeRecommend(OfficeRecommend officeRecommend);
	
	
	/**
	 * 修改店铺推荐组 
	 * @param officeRecommend
	 */
	public void updateOfficeRecommend(OfficeRecommend officeRecommend);
	
	/**
	 * 逻辑删除店铺推荐组 
	 * @param officeRecommendId
	 */
	public void deleteOfficeRecommend(int officeRecommendId);
	
	/**
	 * 店铺推荐组是否显示
	 * @param officeRecommend
	 */
	public void updateIsShow(OfficeRecommend officeRecommend);
	
	/**
	 * 店铺推荐组对应的店铺列表
	 * @param officeRecommendMapping
	 * @return
	 */
	public List<OfficeRecommendMapping> selectOfficeForRecommend(OfficeRecommendMapping officeRecommendMapping);
	
	/**
	 * 删除某一组推荐店铺组中的某个店铺
	 * @param OfficeRecommendMappingId
	 */
	public void delOffice(int OfficeRecommendMappingId);
	
	/**
	 * 店铺推荐组添加店铺
	 * @param OfficeRecommendMapping
	 */
	public void insertOffice(OfficeRecommendMapping OfficeRecommendMapping);
	
	/**
	 * 根据officeRecommendMappingId获得推荐组中的某个店铺
	 * @param officeRecommendMappingId
	 * @return
	 */
	public OfficeRecommendMapping getOfficeRecommendMapping(int officeRecommendMappingId);
	
	/**
	 * 修改某组中某店铺的信息 
	 * @param officeRecommendMapping
	 */
	public void updateOfficeMessage(OfficeRecommendMapping officeRecommendMapping);
	
	/**
	 * 将所有的推荐组都设置为不显示
	 */
	public void changeAll();
	
	/**
	 * 查询最近创建的那个推荐组
	 * @return
	 */
	public int selectIdByCreatDate();
}
