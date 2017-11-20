package com.training.modules.train.service;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
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
	public List<Position> findPositionNotValues(String values) {
		String[] pValues = values.split(",");
		return positionDao.findPositionNotValues(pValues);
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
	
	
	
	
}
