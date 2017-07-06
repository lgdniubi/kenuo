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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		List<GoodsStatisticsCountData> list = goodsStatisticsDao.queryGoodsStatisticsCountData(map);
		
		//判断集合是否为空,空:直接返回map值,不空:更新商品统计表
		if(list.size() !=0){
			//排除集合中GoodsId为null的数据
			Iterator<GoodsStatisticsCountData> iterator = list.iterator();
			while(iterator.hasNext()){
				GoodsStatisticsCountData goodsStatisticsCountData = iterator.next();
				if(goodsStatisticsCountData.getGoodsId() == null){
					iterator.remove();
				}
				if(goodsStatisticsCountData.getBuyCount() == null){
					goodsStatisticsCountData.setBuyCount(0);
				}
				if(goodsStatisticsCountData.getEvaluationCount() == null){
					goodsStatisticsCountData.setEvaluationCount(0);
				}
				if(goodsStatisticsCountData.getEvaluationScore() == null){
					goodsStatisticsCountData.setEvaluationScore((float) 0.0);
				}	
			}
			
			List<GoodsStatisticsCountData> addList  = new ArrayList<GoodsStatisticsCountData>();
			//遍历查询结果    区别出mtmy_goods_statistics表中   id不存在的数据
			Iterator<GoodsStatisticsCountData> iterator2 = list.iterator();
			while(iterator2.hasNext()){
				GoodsStatisticsCountData goodsStatisticsCountData = iterator2.next();
				if(goodsStatisticsCountData.getIsExist().equals("NULL")){
					addList.add(goodsStatisticsCountData);
					iterator2.remove();
				} 
			}
			//有需要添加的数据  执行添加操作
			if(addList.size() != 0){
				for (GoodsStatisticsCountData goodsStatisticsCountData : addList) {
					goodsStatisticsDao.addGoodsStatisticsCountData(goodsStatisticsCountData);
				}
			}
			//修改的数据不是null 才会执行修改操作
			if(list.size() != 0){
				for (GoodsStatisticsCountData goodsStatisticsCountData : list) {
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
		return map;
	}
}
