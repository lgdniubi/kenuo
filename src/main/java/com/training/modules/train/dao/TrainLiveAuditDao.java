package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.TrainLiveAudit;
import com.training.modules.train.entity.TrainLiveOrder;
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
}
