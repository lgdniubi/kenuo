package com.training.modules.train.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.DepartmentDao;
import com.training.modules.train.entity.Department;
import com.training.modules.train.entity.Position;

/**
 * 
 * @className DepartmentService
 * @description TODO 部门管理service
 * @author chenbing
 * @date 2017年11月14日 兵子
 *
 *
 */
@Service
@Transactional(readOnly = false)
public class DepartmentService  extends CrudService<DepartmentDao, Department>{

	@Autowired
	private DepartmentDao departmentDao;

	/**
	 * 
	 * @Title: findDepartment
	 * @Description: TODO 查询部门
	 * @param page
	 * @param department
	 * @return:
	 * @return: Page<Department>
	 * @throws
	 * 2017年11月14日 兵子
	 */
	public Page<Department> findDepartment(Page<Department> page, Department department) {
		User user = UserUtils.getUser();
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(),"o"));
		department.setOffice(new Office(user.getCompany().getId()));
		// 设置分页参数
		department.setPage(page);
		// 执行分页查询
		page.setList(departmentDao.findList(department));
		return page;
	}

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
	public Department getDepartmentName(String name,String companyId) {
		return departmentDao.getDepartmentName(name,companyId);
	}

	/**
	 * 
	 * @Title: saveDepartment
	 * @Description: TODO 保存部门
	 * @param department:
	 * @return: void
	 * @throws
	 * 2017年11月15日 兵子
	 */
	public void saveDepartment(Department department) {
		User user = UserUtils.getUser();
		Office office = new Office();
		office.setId(user.getCompany().getId());
		department.setOffice(office);
		if (department.getdId() == null) {
			//department.setIsNewRecord(true);
			department.preInsert();
			departmentDao.saveDepartment(department);
		}
		if (department.getdId() != null) {
			department.preUpdate();
			departmentDao.editDepartment(department);
		}
	}

	/**
	 * 
	 * @Title: getDepartment
	 * @Description: TODO 获得单个部门数据
	 * @param department:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public Department getDepartment(Department department) {
		return departmentDao.getDepartment(department);
	}

	/**
	 * 
	 * @Title: deleteDepartment
	 * @Description: TODO 删除部门,删除部门时先去删除部门下的职位
	 * @param ids:
	 * @return: void
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public void deleteDepartment(String ids) {
		Department department = new Department();
		department.preUpdate();
		if (StringUtils.isNotBlank(ids)) {
			String[] id = ids.split(",");
			for (String dId : id) {
				departmentDao.deletePositionById(Integer.valueOf(dId));
				department.setdId(Integer.valueOf(dId));
				departmentDao.deleteDepartment(department);
			}
		}
	}

	/**
	 * 
	 * @Title: getPosition
	 * @Description: TODO 获得相关部门的所有职位
	 * @param department
	 * @return:
	 * @return: Page<Department>
	 * @throws
	 * 2017年11月16日 兵子
	 */
	public List<Position> getPosition(Department department) {
		return departmentDao.getPosition(department);
	}
	
}
