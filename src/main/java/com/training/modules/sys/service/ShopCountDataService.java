/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月7日 下午5:04:02
 * 修改人:	
 * 修改时间:	2017年6月7日 下午5:04:02
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.common.utils.BeanUtil;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.ShopCountDataDao;
import com.training.modules.sys.entity.ShopCountData;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称:	ShopCountDataService
 * 类描述:	店铺统计数据业务层
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月9日 上午10:13:49
 */
@Service
@Transactional(readOnly = true)
public class ShopCountDataService extends TreeService<ShopCountDataDao, ShopCountData> {

	protected static RedisClientTemplate redisClientTemplate;
	static{
		redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
	}
	/**
	 * 方法说明:	查询店铺的所有统计数据   并更新 train_shop_statistics表数据   返回截止数据id
	 * 创建时间:	2017年6月7日 下午5:12:23
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月7日 下午5:12:23
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> completeShopCountData(){
		//先从数据库中取出上次存入的定位id
		Integer commentId = QuartzStartConfigUtils.queryValue("mtmy_shop_comment");
//		Integer apptOrderId = QuartzStartConfigUtils.queryValue("mtmy_appt_order_shop");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commentId", commentId);
//		map.put("apptOrderId", apptOrderId);
		
		//查询店铺的统计数据 
//		List<ShopCountData> apptDataList = dao.queryShopApptData(apptOrderId);
		Map<String, String> shopMap = redisClientTemplate.hgetAll("SHOP_SUBSCRIBE_NUM_KEY");
		
		List<ShopCountData> commentDataList = dao.queryShopCommentData(commentId);
		//排除预约数据 集合中BeautyId为null的数据
		/*Iterator<ShopCountData> iterator1 = apptDataList.iterator();
		while(iterator1.hasNext()){
			ShopCountData shopCountData = iterator1.next();
			if(shopCountData.getShopId() == null){
				iterator1.remove();
			}
			if(shopCountData.getApptCount() == null){
				shopCountData.setApptCount(0);
			}
			if(shopCountData.getEvaluationCount() == null){
				shopCountData.setEvaluationCount(0);
			}
			if(shopCountData.getEvaluationScore() == null){
				shopCountData.setEvaluationScore((float) 0.0);
			}
		}*/
		//排除评论数据 集合中BeautyId为null的数据
		Iterator<ShopCountData> iterator2 = commentDataList.iterator();
		while(iterator2.hasNext()){
			ShopCountData shopCountData = iterator2.next();
			if(shopCountData.getShopId() == null){
				iterator2.remove();
			}
			if(shopCountData.getApptCount() == null){
				shopCountData.setApptCount(0);
			}
			if(shopCountData.getEvaluationCount() == null){
				shopCountData.setEvaluationCount(0);
			}
			if(shopCountData.getEvaluationScore() == null){
				shopCountData.setEvaluationScore((float) 0.0);
			}
		}
		// 新增或修改 train_shop_statistics 表统计数据
		// 更新预约数据
		Set<Entry<String, String>> entrySet = shopMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String shopId = entry.getKey();
			String apptCount = entry.getValue();
			int appt_count = Integer.parseInt(apptCount);
			ShopCountData apptData = new ShopCountData();
			apptData.setShopId(shopId);
			apptData.setApptCount(appt_count);
			apptData.setEvaluationCount(0);
			apptData.setEvaluationScore((float) 0.0);
			dao.updateShopCountData(apptData);
		}
		/*if(apptDataList.size() != 0){
			for (ShopCountData apptData : apptDataList) {
				dao.updateShopCountData(apptData);
			}
		}*/
		// 更新评论数据
		if(commentDataList.size() != 0){
			for (ShopCountData commentData : commentDataList) {
				dao.updateShopCountData(commentData);
			}
		}
		//获取查询数据的定位id
		Map<String, Object> map2 = new HashMap<String, Object>();
		Integer new_commentId = dao.findCommentId();
		if(new_commentId == null){
			new_commentId = 0;
		}
		Integer new_apptOrderId = dao.findApptOrderId();
		map2.put("mtmy_shop_comment", new_commentId);
		map2.put("mtmy_appt_order_shop", new_apptOrderId);
		return map2;
	}
}
