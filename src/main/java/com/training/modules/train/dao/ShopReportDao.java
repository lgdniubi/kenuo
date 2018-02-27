package com.training.modules.train.dao;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ShopReport;

/**
 * 店务报表Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface ShopReportDao extends CrudDao<ShopReport>{
	
	/**
	 * 查询所有有过绑定的用户
	 * @return
	 */
	public List<ShopReport> findUserList();
	/**
	 * 插入用户消费报表
	 * @param list
	 * @return
	 */
	public int insertUserReport(@Param(value="list")List<ShopReport> list,@Param(value="date")Date date);
	
	/**
	 * 插入美容师绩效报表（不含时限卡）
	 * @param list
	 * @return
	 */
	public int insertBeauticianAchievement(@Param(value="date")Date date);

	/**
	 * 插入美容师绩效报表（时限卡）
	 * @param list
	 * @return
	 */
	public int insertBeauticianAchievementCard(@Param(value="date")Date date);
	
	/**
	 * 查询所有有绑定用户的店铺
	 * @return
	 */
	public List<ShopReport> findShopGoodsList();
	/**
	 * 插入店铺商品报表
	 * @param list
	 * @return
	 */
	public int insertShopGoodsList(@Param(value="list")List<ShopReport> list,@Param(value="date")Date date);
}
