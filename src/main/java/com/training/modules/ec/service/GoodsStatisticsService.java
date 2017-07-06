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
package com.training.modules.ec.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.GoodsStatisticsDao;
import com.training.modules.ec.entity.GoodsStatisticsCountData;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称:	GoodsStatisticsService
 * 类描述:	商品统计数据业务层
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
@Service
@Transactional(readOnly = false)
public class GoodsStatisticsService extends TreeService<GoodsStatisticsDao, GoodsStatisticsCountData> {

	@Autowired
	private GoodsStatisticsDao goodsStatisticsDao;
	
	/**
	 * 方法说明:	查询商品的所有统计数据   并更新 mtmy_goods_statistics表数据   返回截止数据id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	
	 * 修改记录	2017年6月19日
	 * @return
	 */
	
	public Map<String, Object> completeGoodsStatistics(){
		//先从数据库中取出上次存入的定位id
		Integer commentId = QuartzStartConfigUtils.queryValue("mtmy_goods_commentId");
		Integer recId = QuartzStartConfigUtils.queryValue("mtmy_goods_recId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mtmy_goods_recId", recId);
		map.put("mtmy_goods_commentId", commentId);
		
		//查询商品的统计数据   并更新mtmy_goods_statistics
		List<GoodsStatisticsCountData> list = goodsStatisticsDao.selectGoodsMapping(recId);//查询出商品的购买数量
		//判断集合是否为空,空:直接返回map值,不空:更新商品统计表
		if(list.size() !=0 ){
			//有需要添加的数据  执行添加操作
			for (GoodsStatisticsCountData goodsStatisticsCountData : list) {
				goodsStatisticsDao.addGoodsStatisticsCountData(goodsStatisticsCountData);
			}
		}
		//查询商品的评分和评价数量
		List<GoodsStatisticsCountData> commentList = goodsStatisticsDao.selectComment(commentId);
		if(commentList.size() !=0 ){
			for (GoodsStatisticsCountData goodsStatisticsCountData : commentList) {
				goodsStatisticsDao.updateGoodsStatisticsCountData(goodsStatisticsCountData);
			}
		}
		//获取查询数据的定位id
		Map<String, Object> map2 = new HashMap<String, Object>();
		int new_commentId = goodsStatisticsDao.findCommentId();
		int new_recId = goodsStatisticsDao.findGoodsId();
		map2.put("mtmy_goods_commentId", new_commentId);
		map2.put("mtmy_goods_recId", new_recId);
		return map2;
	}
}
