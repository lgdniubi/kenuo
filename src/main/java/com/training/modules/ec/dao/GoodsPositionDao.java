package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.GoodsPosition;

/**
 * 商品分类-DAO层
 * @author 土豆
 * @version 2017-10-9
 */
@MyBatisDao
public interface GoodsPositionDao extends TreeDao<GoodsPosition>{

	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<GoodsPosition> findListbyPID(String pid);
	
}
