package com.training.modules.train.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.PositionDao;
import com.training.modules.train.entity.Position;


/**
 * 
 * @className PositionService
 * @description TODO 职位Service
 * @author chenbing
 * @date 2017年11月16日 兵子
 *
 *
 */
@Service
@Transactional(readOnly = false)
public class PositionService extends CrudService<PositionDao,Position>{

	@Autowired
	private PositionDao positionDao;
	
	/**
	 * 
	 * @Title: deletePosition
	 * @Description: TODO 删除职位
	 * @param ids
	 * @param position:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void deletePosition(String ids, Position position) {
		if (StringUtils.isNotBlank(ids)) {
			String[] id = ids.split(",");
			for (String key : id) {
				position.setValue(key);
				positionDao.deletePosition(position);
			}
			
		}
	}

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
	public List<Position> findPositionNotValues(String values,User user) {
		String[] pValues = values.split(",");
		List<String> positionList = positionDao.getPositionkey(user.getId());
		if (pValues != null && pValues.length > 0) {
			for (String value : pValues) {
				positionList.add(value);
			}
		}
		return positionDao.findPositionNotValues(positionList);
	}

	/**
	 * 
	 * @Title: savePosition
	 * @Description: TODO 保存职位
	 * @param values
	 * @param position:
	 * @return: void
	 * @throws
	 * 2017年11月20日 兵子
	 */
	public void savePosition(String values, Position position) {
		if (StringUtils.isNotBlank(values) && position.getDepartment().getdId() != null) {
			positionDao.deletePositionById(position);
			String[] pValue = values.split(",");
			for (String value : pValue) {
				position.setValue(value);
				positionDao.savePosition(position);
			}
		}
	}

	/**
	 * 
	 * @param exValues 
	 * @Title: searchPosition
	 * @Description: TODO 职位查询
	 * @param searchValue
	 * @param positionkey
	 * @return:
	 * @return: List<Position>
	 * @throws
	 * 2018年1月9日 兵子
	 */
	public List<Position> searchPosition(String[] exValues, String searchValue, List<String> positionkey) {
		if (exValues != null && exValues.length > 0) {
			for (String values : exValues) {
				positionkey.add(values);
			}
		}
		return positionDao.searchPosition(searchValue,positionkey);
	}
	
}
