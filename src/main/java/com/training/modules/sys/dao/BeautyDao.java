/**
 * 项目名称:	kenuo
 * 创建人:	zhanlan
 * 创建时间:	2017年6月7日 下午3:03:57
 * 修改人:	
 * 修改时间:	2017年6月7日 下午3:03:57
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.BeautyCountData;

/**
 * 类名称:	BeautyDao
 * 类描述:	技师统计数据操作接口
 * 创建人:	zhanlan 
 * 创建时间:	2017年6月7日 下午3:03:57
 */
@MyBatisDao
public interface BeautyDao extends TreeDao<BeautyCountData>{

	/**
	 * 方法说明:	获取定时器截止的  预约id
	 * 创建时间:	2017年6月8日 上午9:43:30
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 上午9:43:30
	 * @return
	 */
	public int findApptOrderId();
	/**
	 * 方法说明:	获取定时器截止的  评论id
	 * 创建时间:	2017年6月8日 上午9:41:45
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月8日 上午9:41:45
	 * @return
	 */
	public int findCommId();
	/**
	 * 方法说明:	查询技师的所有统计数据
	 * 创建时间:	2017年6月7日 下午3:51:47
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月7日 下午3:51:47
	 * @return
	 */
	public List<BeautyCountData> queryBeautyCountData(Map<String, Object> map);
	/**
	 * 方法说明:	修改train_beauty_statistics 表中技师的统计数据
	 * 创建时间:	2017年6月7日 下午5:53:52
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月7日 下午5:53:52
	 * @param b
	 */
	public int updateBeautyCountData(@Param("list")List<BeautyCountData> list);
	/**
	 * 方法说明:	将技师的统计数据插入train_beauty_statistics 表
	 * 创建时间:	2017年6月7日 下午5:21:40
	 * 创建人:	zhanlan
	 * 修改记录:	修改人	修改记录	2017年6月7日 下午5:21:40
	 * @param b
	 */
	public int addBeautyCountData(List<BeautyCountData> list);
}
