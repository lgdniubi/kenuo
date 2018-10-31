/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.OfficeAcount;

/**
 * 机构DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeAcountDao extends CrudDao<OfficeAcount> {

	public List<OfficeAcount> findOfficeList(OfficeAcount officeAcount);
	
	/**
	 * 查询机构账户
	 * @param officeId
	 * @return
	 */
	public OfficeAcount findOfficeAcount(String officeId);
	/**
	 * 变更信用额度
	 * @param OfficeAcount
	 */
	public void updateOfficeCreditLimit(OfficeAcount OfficeAcount);
	/**
	 * 创建账户
	 * @param OfficeAcount
	 */
	public void saveOfficeAcount(OfficeAcount officeAcount);
	/**  
	* <p>Title: 获取结构可用额度</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月29日  
	* @version 3.0.0  
	*/  
	public double queryusedLimit(OfficeAcount officeAcount);

	/**
	 * 额度修改日志
	 * @param officeAcount
	 */
	public void saveOfficeCreditLog(OfficeAcount officeAcount);

	public List<OfficeAcount> findCreditLogList(OfficeAcount officeAcount);
}