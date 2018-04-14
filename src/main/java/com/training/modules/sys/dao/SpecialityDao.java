package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Speciality;
import com.training.modules.sys.entity.UserSpeciality;


/**
 * 特长dao
 * @author water
 *
 */

@MyBatisDao
public interface SpecialityDao extends CrudDao<Speciality> {
	
	/**
	 * 查询所有特长
	 */
	
	public List<Speciality> findAllList();
	
	/**
	 * 根据条件查找数据
	 */
	public List<Speciality> findAllListBy(String framid);
	/**
	 * 插入特长中间表
	 * @param list
	 * @return
	 */
	public int insertSpecialityorm(List<UserSpeciality> list);
	/**
	 * 删除特长表中间冗余部分数据并且从新更新
	 * @param userid
	 * @return
	 */
	
	public	int  deletormSpec(String userid);
	/**
	 * 根据用户id 查找特长
	 * @param userid
	 * @return
	 */
	public List<Speciality> findlistByuserid(String userid);
	
	/**
	 * 插入数据
	 * @param speciality
	 * @return
	 */
	public	int insertSpec(Speciality speciality);
	/**
	 * 更新数据
	 * @param speciality
	 * @return
	 */
	public int updateSpec(Speciality speciality);
	/**
	 * 删除数据
	 * @param speciality
	 * @return
	 */
	public int	deleteSpeciality(Speciality speciality);

	public int validDel(Speciality speciality);

}
