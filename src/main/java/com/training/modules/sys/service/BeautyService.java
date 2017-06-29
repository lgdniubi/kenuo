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

import java.util.ArrayList;
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
		
		//查询技师的统计数据   并更新train_beauty_statistics
		List<BeautyCountData> list = dao.queryBeautyCountData(map);
		//排除集合中BeautyId为null的数据
		Iterator<BeautyCountData> iterator = list.iterator();
		while(iterator.hasNext()){
			BeautyCountData beautyCountData = iterator.next();
			if(beautyCountData.getBeautyId() == null){
				iterator.remove();
			}
			if(beautyCountData.getApptCount() == null){
				beautyCountData.setApptCount(0);
			}
			if(beautyCountData.getEvaluationCount() == null){
				beautyCountData.setEvaluationCount(0);
			}
			if(beautyCountData.getEvaluationScore() == null){
				beautyCountData.setEvaluationScore((float) 0.0);
			}	
		}
		
		List<BeautyCountData> addList  = new ArrayList<BeautyCountData>();
		//遍历查询结果    区别出train_shop_statistics表中   id不存在的数据
		Iterator<BeautyCountData> iterator2 = list.iterator();
		while(iterator2.hasNext()){
			BeautyCountData beautyCountData = iterator2.next();
			if(beautyCountData.getIsExist().equals("NULL")){
				addList.add(beautyCountData);
				iterator2.remove();
			} 
		}
		//有需要添加的数据  执行添加操作
		if(addList.size() != 0){
			dao.addBeautyCountData(addList);
		}
		//修改的数据不是null 才会执行修改操作
		if(list.size() != 0){
			dao.updateBeautyCountData(list);
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
