/**
 * 项目名称:	kenuo
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 * 修改人:	
 * 修改时间:	2017年6月19日
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsStatisticsCountData;

/**
 * 类名称:	GoodsStatisticsDao
 * 类描述:	商品统计数据操作接口
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
@MyBatisDao
public interface GoodsStatisticsDao extends TreeDao<GoodsStatisticsCountData>{

	/**
	 * 方法说明:	获取定时器截止的  商品id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @return
	 */
	public int findGoodsId();
	/**
	 * 方法说明:	获取定时器截止的  评论id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @return
	 */
	public int findCommentId();
	
	/**
	 * 方法说明:	查询商品的所有统计数据
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @return
	 */
	public List<GoodsStatisticsCountData> queryGoodsStatisticsCountData(Map<String, Object> map);
	
	/**
	 * 方法说明:	修改mtmy_goods_statistics 表中商品的统计数据
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @param b
	 */
	public void updateGoodsStatisticsCountData(GoodsStatisticsCountData goodsStatisticsCountData);
	
	/**
	 * 方法说明:	将商品的统计数据插入mtmy_goods_statistics 表
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @param b
	 */
	public void addGoodsStatisticsCountData(GoodsStatisticsCountData goodsStatisticsCountData);
}
