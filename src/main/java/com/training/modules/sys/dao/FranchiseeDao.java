package com.training.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Franchisee;

/**
 * 加盟商管理DAO接口
 * @author kele
 * @version 2016-6-4 16:17:23
 */
@MyBatisDao
public interface FranchiseeDao extends TreeDao<Franchisee>{

	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Franchisee> findListbyPID(String pid);
	
	/**
	 * 添加数据
	 * @param franchisee
	 * @return
	 */
	public void insertFranchisee(Franchisee franchisee);
	
	/**
	 * 修改数据
	 */
	public int update(Franchisee franchisee);

	/**
	 * 修改公共商品服务标识
	 * @param franchisee
	 */
	public void updatePublicServiceFlag(Franchisee franchisee);

	/**
	 * 保存   把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
	 * @param id
	 * @param parentId
	 * @param parentIds
	 * @param name
	 * @param type
	 * @param sort
	 * @param isRealFranchisee
	 * @param iconUrl
	 * @param publicServiceFlag
	 * @param status
	 * @param createBy
	 * @param updateBy
	 * @param remarks
	 */
	public void saveMtmyFranchisee(@Param(value="id")String id, @Param(value="parentId")String parentId, @Param(value="parentIds")String parentIds, @Param(value="name")String name, @Param(value="type")String type,
			@Param(value="sort")Integer sort, @Param(value="iconUrl")String iconUrl, @Param(value="createId")String createId, @Param(value="remarks")String remarks);

	/**
	 * 修改   把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
	 * @param id
	 * @param name
	 * @param type
	 * @param iconUrl
	 * @param updateId
	 * @param remarks
	 */
	public void updateMtmyFranchisee(@Param(value="id")String id, @Param(value="name")String name, @Param(value="type")String type, @Param(value="iconUrl")String iconUrl, @Param(value="updateId")String updateId,
			@Param(value="remarks")String remarks);

	/**
	 * 删除   把trains库中的sys_franchisee的修改信息同步到mtmydb中mtmy_franchisee
	 * @param id
	 */
	public void deleteMtmyFranchisee(@Param(value="id")String id);

}
