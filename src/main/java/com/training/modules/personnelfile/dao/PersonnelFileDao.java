/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.personnelfile.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.PersonnelFile;

/**
 * 人事档案DAO接口
 * @author ouwei
 *  2016-12-1
 */
@MyBatisDao
public interface PersonnelFileDao extends CrudDao<PersonnelFile> {
	
	/**
	 * 查询所有人事档案
	 * @param personnelFile
	 * @return 
	 */
	public List<PersonnelFile> findPersonnelFileList(PersonnelFile personnelFile);
	
	/**
	 * 查询添加页面需要回带的数据
	 * @param findPortionInfo
	 * @return
	 */
	public PersonnelFile findPortionInfo(PersonnelFile findPortionInfo);

	/**
	 * 查看人事档案
	 * @param personnelFile
	 * @return
	 */
	public PersonnelFile getPersonnelFileBefor(PersonnelFile personnelFile);

	/**
	 * 修改sys_user表中得图片路径，和是否已建档
	 * @param personnelFile
	 */
	public void updateUserInfo(PersonnelFile personnelFile);
}

