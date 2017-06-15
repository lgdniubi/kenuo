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
		if(commentId == null){
			commentId = 0;
		}
		Integer apptOrderId = QuartzStartConfigUtils.queryValue("mtmy_appt_order_shop");
		if(apptOrderId == null){
			apptOrderId = 0;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commentId", commentId);
		map.put("apptOrderId", apptOrderId);
		
		//查询店铺的统计数据   并更新train_shop_statistics
		List<ShopCountData> shopList = dao.queryShopCountData(map);
		//遍历查询结果    区别出train_shop_statistics表中   id不存在的数据
		List<ShopCountData> addList  = new ArrayList<ShopCountData>();
		Iterator<ShopCountData> iterator = shopList.iterator();
		while(iterator.hasNext()){
			ShopCountData shopCountData = iterator.next();
			if(shopCountData.getIsExist().equals("NULL")){
				addList.add(shopCountData);
				iterator.remove();
			}
		}
		//修改的数据不为null  才会执行修改操作
		if(shopList.size() != 0){
			dao.updateShopCountData(shopList);
		}
		//需要添加的数据不为null  才执行添加操作
		if(addList.size() != 0){
			dao.addShopCountData(addList);
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
