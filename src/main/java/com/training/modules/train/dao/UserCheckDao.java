package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.PcRole;
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

	/**
	 * 查看手艺人权限信息根据user id
	 * @param userid
	 * @return
	 */
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

	/**
	 * 从pc_role查
	 * 根据该用户申请的商家版本id，ename=sjgly，officeid=1，franchiseeid=1查询超级管理员id
	 * @param modelFranchisee
	 * @return
	 */
	public int findByModidAndEname(ModelFranchisee modelFranchisee);

	/**
	 * 向pc_user_role中插入一条记录
	 * @param userid
	 * @param roleid
	 */
	public void insertPcUserRole(@Param("userid")String userid, @Param("roleid")int roleid);

	/**
	 * 从fzx_role查
	 * @param modelFranchisee
	 * @return
	 */
	public int findByModidAndEnameFzx(ModelFranchisee modelFranchisee);
	//向fzx_user_role中插入一条记录
	public void insertFzxUserRole(@Param("userid")String userid, @Param("roleid")int fzx_roleid);

	/**
	 * 从pc_role中查询基础的角色
	 * @param franchid
	 * @return
	 */
	public List<PcRole> findPcRoleByModid(ModelFranchisee modelFranchisee);

	/**
	 * 创建基础角色
	 * @param pcrole
	 */
	public void insertPcCommonRole(PcRole pcrole);

	public List<FzxRole> findFzxRoleByModid(ModelFranchisee modelFranchisee);

	public void insertFzxCommonRole(FzxRole fzxRole);

	/**
	 * 发送推送消息时，通过userid找到客户端id
	 * @param userid
	 * @return
	 */
	public String findCidByUserid(String userid);

	/**
	 * 从fzx_role查询mod_id为syr的roleid插入fzx_user_role
	 * 设置手艺人角色，权益设置后
	 * @param modelFranchisee
	 * @Description:
	 */
	public void insertFzxUserRoleForsyr(ModelFranchisee modelFranchisee);

	/**
	 * @return
	 * @Description:找出商家code中最大的一个
	 */
	public Long findMaxCode();

	public void deletePcUserRole(String userid);

	public void deleteFzxUserRole(String userid);

	public void deletePcCommonRole(String franchiseeid);

	public void deleteFzxCommonRole(String franchiseeid);
	
}
