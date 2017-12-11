package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Department;
import com.training.modules.train.entity.Position;

/**
 * 
 * @className DepartmentDao
 * @description TODO 部门管理dao
 * @author chenbing
 * @date 2017年11月14日 兵子
 *
 *
 */
@MyBatisDao
public interface DepartmentDao extends CrudDao<Department>{

	/**
	 * 
	 * @Title: getDepartmentName
	 * @Description: TODO 验证部门名称
	 * @param name
	 * @return:
	 * @return: Department
	 * @throws
	 * 2017年11月15日 兵子
	 */
	public Department getDepartmentName(@Param(value="name") String name,@Param(value="companyId") String companyId);

	/**
	 * 
	 * @Title: saveDepartment
	 * @Description: TODO 保存部门
	 * @param department:
	 * @return: void
	 * @throws
	 * 2017年11月15日 兵子
	 */
	public void saveDepartment(Department department);

	/**
	 * 
	 * @Title: getDepartment
	 * @Description: TODO 获取单个部门数据
	 * @param department
	 * @return:
	 * @return: Department
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public Department getDepartment(Department department);

	/**
	 * 
	 * @Title: deletePositionById
	 * @Description: TODO 删除对应部门下的职位
	 * @param dId:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void deletePositionById(Integer dId);
	/**
	 * 
	 * @Title: deleteDepartment
	 * @Description: TODO 删除部门
	 * @param dId:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void deleteDepartment(Department department);

	/**
	 * 
	 * @Title: editDepartment
	 * @Description: TODO 修改部门
	 * @param department:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void editDepartment(Department department);

	/**
	 * 
	 * @Title: getPosition
	 * @Description: TODO 查询相关部门的职位
	 * @param department
	 * @return:
	 * @return: List<Department>
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public List<Position> getPosition(Department department);

	

}
