package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLiveOrder;
import com.training.modules.train.entity.TrainLiveRewardRecord;
import com.training.modules.train.entity.TrainLiveSku;

/**
 * 直播dao
 * @author yangyang
 *
 */
@MyBatisDao
public interface TrainLiveAuditDao extends TreeDao<TrainLiveAudit>{
	/**
	 * 直播审核
	 * @return
	 */
	public List<TrainLiveAudit> selectOutLive();
	
	/**
	 * 过期数据修改
	 * @param id
	 * @return
	 */
	public int updateLiveOut(String id);
	
	/**
	 * 查询将要直播的数据
	 * @return
	 */
	public List<TrainLiveAudit> selectWantLive();
	
	/**
	 * 分页分页查询直播Sku配置 
	 * @return
	 */
	public List<TrainLiveSku> findSkuList(TrainLiveSku trainLiveSku);
	
	/**
	 * 分页分页查询直播订单列表
	 * @return
	 */
	public List<TrainLiveOrder> findOrderList(TrainLiveOrder trainLiveOrder);
	
	/**
	 * 根据trainLiveSkuId查询Sku配置
	 * @param trainLiveSkuId
	 * @return
	 */
	public TrainLiveSku findByTrainLiveSkuId(int trainLiveSkuId);
	
	/**
	 * 保存Sku配置
	 * @param trainLiveSku
	 */
	public void saveSku(TrainLiveSku trainLiveSku);
	
	/**
	 * 根据直播id查找Sku配置价格
	 * @param id
	 * @return
	 */
	public double findSkuPrice(String id);
	
	/**
	 * 查看云币贡献榜
	 * @param trainLiveRewardRecord
	 * @return
	 */
	public List<TrainLiveRewardRecord> findCloudContribution(TrainLiveRewardRecord trainLiveRewardRecord);
	/**
	 * 云币贡献管理
	 * @param trainLiveAudit
	 * @return
	 */
	public List<TrainLiveAudit> liveIntegralsList(TrainLiveAudit trainLiveAudit);
	/**
	 * 商家总云币(临时版本)
	 * @return
	 */
	public int findOfficeIntegrals();
	/**
	 * 删除直播商家权限ID
	 * @param id
	 */
	public void deleteJurisdiction(String id);
	/**
	 * 插入直播商家权限ID
	 * @param id
	 */
	public void insertJurisdiction(@Param("auditId")String auditId,@Param("id")String id);

	/**
	 * 添加直播推荐(只有一个能够推荐)
	 * @param trainLiveAudit
	 */
	public void addRecommend(TrainLiveAudit trainLiveAudit);

	/**
	 * 修改全部直播为不推荐
	 */
	public void updateRecommend();
	
	/**
	 * 
	 * @Title: delCheck
	 * @Description: TODO 删除校验
	 * @param trainLiveCategoryId
	 * @return:
	 * @return: List<TrainLiveAudit>
	 * @throws
	 * 2017年12月19日 兵子
	 */
	public Integer delCheck(String trainLiveCategoryId);

	/**
	 * 
	 * @Title: findAuditList
	 * @Description: TODO 根据分类id查询所有直播
	 * @param trainLiveAudit
	 * @return:
	 * @return: List<TrainLiveAudit>
	 * @throws
	 * 2017年12月20日 兵子
	 */
	public List<TrainLiveAudit> findAuditList(TrainLiveAudit trainLiveAudit);

	/**
	 * 
	 * @Title: transferCategory
	 * @Description: TODO 修改直播分类
	 * @param auditId
	 * @param categoryId:
	 * @return: void
	 * @throws
	 * 2017年12月20日 兵子
	 */
	public int transferCategory(@Param(value = "auditId") String auditId, @Param(value = "categoryId") String categoryId);

	/**
	 * 直播列表修改每天美耶权限
	 * @param trainLiveAudit
	 */
	public void saveFormMtmy(TrainLiveAudit trainLiveAudit);

}
