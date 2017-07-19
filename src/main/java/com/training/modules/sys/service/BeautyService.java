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
import com.training.modules.sys.dao.BeautyDao;
import com.training.modules.sys.entity.BeautyCountData;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称:	BeautyService
 * 类描述:	技师统计数据业务层
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月7日 下午5:04:02
 */
@Service
@Transactional(readOnly = true)
public class BeautyService extends TreeService<BeautyDao, BeautyCountData> {

	protected static RedisClientTemplate redisClientTemplate;
	static{
		redisClientTemplate = (RedisClientTemplate) BeanUtil.getBean("redisClientTemplate");
	}
	/**
	 * 方法说明:	查询技师的所有统计数据   并更新 train_beauty_statistics表数据   返回截止数据id
	 * 创建时间:	2017年6月7日 下午5:12:23
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月7日 下午5:12:23
	 * @return
	 */
	@Transactional(readOnly = false)
	public Map<String, Object> completeBeautyCountData(){
		//先从数据库中取出上次存入的定位id
		Integer commId = QuartzStartConfigUtils.queryValue("mtmy_beauty_comment");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commId", commId);
		
		//查询技师的统计数据
		List<BeautyCountData> commentDataList = dao.queryBeautyCommentData(commId);
		
		//从redis缓存中取出店铺的已完成订单的数据集合
		Map<String, String> beautyMap = redisClientTemplate.hgetAll("BEAUTICIAN_SUBSCRIBE_NUM_KEY");
		
		//排除评论数据 集合中BeautyId为null的数据
		// 更新评论数据
		Iterator<BeautyCountData> iterator2 = commentDataList.iterator();
		while(iterator2.hasNext()){
			BeautyCountData commentData = iterator2.next();
			if(commentData.getBeautyId() == null){
				iterator2.remove();
			}
			if(commentData.getEvaluationCount() == null){
				commentData.setEvaluationCount(0);
			}
			if(commentData.getEvaluationScore() == null){
				commentData.setEvaluationScore((float) 0.0);
			}
			if(commentData.getApptCount() == null){
				commentData.setApptCount(0);
			}
		}
		// 更新评论数据
		if(commentDataList.size() != 0){
			for (BeautyCountData commentData : commentDataList) {
				dao.updateBeautyCommenttData(commentData);
			}
		}
		
		// 更新预约数据
		Set<Entry<String, String>> entrySet = beautyMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String beautyId = entry.getKey();
			String apptCount = entry.getValue();
			int appt_count = Integer.parseInt(apptCount);
			BeautyCountData apptData = new BeautyCountData();
			
			apptData.setApptCount(appt_count);
			apptData.setBeautyId(beautyId);
			apptData.setEvaluationCount(0);
			apptData.setEvaluationScore((float) 0.0);
			dao.updateBeautyApptData(apptData); 
		}
	
		//获取查询数据的定位id
		Map<String, Object> map2 = new HashMap<String, Object>();
		int new_commId = dao.findCommId();
		map2.put("mtmy_beauty_comment", new_commId);
		return map2;
	}
}
