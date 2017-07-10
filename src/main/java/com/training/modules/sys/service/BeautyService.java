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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.common.service.TreeService;
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
		Integer apptOrderId = QuartzStartConfigUtils.queryValue("mtmy_appt_order_beauty");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commId", commId);
		map.put("apptOrderId", apptOrderId);
		
		//查询技师的统计数据
		List<BeautyCountData> apptDataList = dao.queryBeautyApptData(apptOrderId);
		List<BeautyCountData> commentDataList = dao.queryBeautyCommentData(commId);
		
		//排除预约数据 集合中BeautyId为null的数据
		Iterator<BeautyCountData> iterator1 = apptDataList.iterator();
		while(iterator1.hasNext()){
			BeautyCountData apptData = iterator1.next();
			if(apptData.getBeautyId() == null){
				iterator1.remove();
			}
			if(apptData.getApptCount() == null){
				apptData.setApptCount(0);
			}
			if(apptData.getEvaluationCount() == null){
				apptData.setEvaluationCount(0);
			}
			if(apptData.getEvaluationScore() == null){
				apptData.setEvaluationScore((float) 0.0);
			}
		}
		//排除评论数据 集合中BeautyId为null的数据
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
		
		// 新增或修改 train_beauty_statistics 表统计数据
		// 更新预约数据
		if(apptDataList.size() != 0){
			for (BeautyCountData apptData : apptDataList) {
				dao.updateBeautyCountData(apptData);
			}
		}
		// 更新评论数据
		if(commentDataList.size() != 0){
			for (BeautyCountData commentData : commentDataList) {
				dao.updateBeautyCountData(commentData);
			}
		}
		//获取查询数据的定位id
		Map<String, Object> map2 = new HashMap<String, Object>();
		int new_commId = dao.findCommId();
		int new_apptOrderId = dao.findApptOrderId();
		map2.put("mtmy_beauty_comment", new_commId);
		map2.put("mtmy_appt_order_beauty", new_apptOrderId);
		return map2;
	}
}
