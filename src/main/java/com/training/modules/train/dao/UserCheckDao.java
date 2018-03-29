package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.UserCheck;

/**
 * 用户审核
 * @author 
 * @version 2018年3月14日
 */
@MyBatisDao
public interface UserCheckDao extends CrudDao<UserCheck>{

	/**
	 * 根据用户审核
	 * @param modEname
	 * @return
	 */

	public void editUserCheck(UserCheck userCheck);

	public UserCheck getUserCheck(UserCheck userCheck);

	/**
	 * 保存权限设置信息
	 * @param modelFranchisee
	 */
	public void saveModelFranchisee(ModelFranchisee modelFranchisee);

	public ModelFranchisee getModelFranchiseeByUserid(String userid);

	/**
	 * 修改设置信息
	 * @param modelFranchisee
	 */
	public void editModelFranchisee(ModelFranchisee modelFranchisee);

	public void saveFranchisee(UserCheck find);

	/**
	 * 保存权益信息后，设置用户type
	 * @param string
	 */
	public void updateUserType(@Param("type") String type,@Param("userid")String userid,@Param("franchid")String franchid);

	/**
	 * 获取企业的权益信息
	 * @param userid
	 * @return
	 */
	public ModelFranchisee getQYModelFranchiseeByUserid(String userid);

	public List<String> selectCids(String userid);
	
}
