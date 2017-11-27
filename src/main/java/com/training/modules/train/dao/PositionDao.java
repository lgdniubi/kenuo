package com.training.modules.train.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Position;

/**
 * 
 * @className PositionDao
 * @description TODO 职位dao
 * @author chenbing
 * @date 2017年11月16日 兵子
 *
 *
 */
@MyBatisDao
public interface PositionDao extends CrudDao<Position>{

	/**
	 * 
	 * @Title: deletePosition
	 * @Description: TODO 删除职位
	 * @param key
	 * @param position:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void deletePosition(Position position);

	/**
	 * 
	 * @Title: findPositionNotValues
	 * @Description: TODO 查询未添加的职位
	 * @param values
	 * @return:
	 * @return: List<Position>
	 * @throws
	 * 2017年11月17日 兵子
	 */
	public List<Position> findPositionNotValues(List<String> pValues);

	/**
	 * 
	 * @Title: deletePositionById
	 * @Description: TODO 删除职位
	 * @param position:
	 * @return: void
	 * @throws
	 * 2017年11月20日 兵子
	 */
	public void deletePositionById(Position position);

	/**
	 * 
	 * @Title: savePosition
	 * @Description: TODO 保存职位
	 * @param position:
	 * @return: void
	 * @throws
	 * 2017年11月20日 兵子
	 */
	public void savePosition(Position position);

	/**
	 * 
	 * @Title: getPositionkey
	 * @Description: TODO 获取当前商家已存在的职位key
	 * @param user
	 * @return:
	 * @return: List<String>
	 * @throws
	 * 2017年11月24日 兵子
	 */
	public List<String> getPositionkey(String userId);

	
}
