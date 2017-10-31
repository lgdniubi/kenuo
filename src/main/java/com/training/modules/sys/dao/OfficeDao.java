/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeInfo;

/**
 * 机构DAO接口
 * 
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
	public Office getByCode(String code);
	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Office> findListbyPID(String pid);
	/**
	 * 通过区域id查询店铺
	 * @param id
	 * @return
	 */
	public List<Office> findListByAreaId(String id);
	/**
	 * 根据roleID找到office对象
	 * @author kele
	 * @param roleid
	 * @return
	 */
	public Office getRolebyOff(String roleid);
	/**
	 * 当实体店铺中无数据时添加 有时修改
	 * @param office
	 */
	public void saveOfficeInfo(Office office);
	/**
	 * 通过ID查询实体店铺详细信息
	 * @param officeInfo
	 * @return
	 */
	public OfficeInfo findbyid(Office office);
	/**
	 * 通过ID查询实体店铺详细信息
	 * @param officeInfo
	 * @return
	 */
	public OfficeInfo OfficeInfoByid(String id);
	
	/**
	 * 删除机构时关联删除实体店铺信息
	 * @param office
	 */
	public void deleteOfficeInfo(Office office);
	/**
	 * 添加加盟商时  同时添加到sys_office机构表中
	 * @param franchisee
	 */
	public void franchiseeSaveOffice(Office office);
	/**
	 * 修改加盟商时  修改sys_office机构表中
	 * @param franchisee
	 */
	public void franchiseeUpdateOffice(Office office);
	/**
	 * 通过code查询对应店铺所属加盟商
	 * @param code
	 * @return
	 */
	public OfficeInfo findFNameByCode(OfficeInfo officeInfo);
	/**
	 * 导出店铺数据
	 * @param office
	 * @return
	 */
	public List<OfficeInfo> exportOffice(Office office);
	/**
	 * 验证机构是否存在
	 * @param officeName
	 * @return
	 */
	public OfficeInfo verifyOfficeName(@Param("upOfficeCode")String upOfficeCode,@Param("grade")int grade);
	/**
	 * 通过code查询其下面的所有子机构名称
	 * @return
	 */
	public List<OfficeInfo> verifyOfficeNameByCode(String upOfficeCode);
	/**
	 * 通过父类id验证机构名称是否存在
	 * @param office
	 * @return
	 */
	public List<Office> verifyOfficeNameByPid(Office office);
	/**
	 * 验证区域是否存在
	 * @param areaName
	 * @return
	 */
	public OfficeInfo verifyAreaName(String areaCode);
	/**
	 * 保存店铺到sys_office_info表中
	 * @param officeInfo
	 */
	public void saveOfficeInfo2(OfficeInfo officeInfo);
	
	/**
	 * 根据父类id查询子类数据 用于异步加载树形table
	 * @return
	 */
	public List<Office> findByPidforChild(Office office);
	/**
	 * 替换code前先在code前拼接特定的字符
	 * @param office
	 */
	public void updateCodeFirst(@Param("oldCode")String oldCode);
	/**
	 * 替换code
	 * @param oldCode
	 * @param newCode
	 */
	public void updateCodeLast(@Param("oldCode")String oldCode,@Param("newCode")String newCode);
	/**
	 * 获取当前用户有权限访问的部门
	 * @param office
	 * @return
	 */
	public List<Office> findByParentIdsLikeAuth(Office office);
	
	/**
	 * 根据选择的市场查找其对应的店铺
	 * @param id
	 * @return
	 */
	public List<Office> selectOfficeById(String id);
	
	/**
	 * 根据美容师id查找其归属店铺或者市场
	 * @param id
	 * @return
	 */
	public Office selectForSpec(String id);
	
	/**
	 * 验证店铺下是否有员工
	 * @param id
	 * @return
	 */
	public int delConfirm(Office office);
	
	/**
	 * 修改店铺是否属性
	 * @param id
	 * @param type
	 * @param isyesno
	 */
	public void updateisyesno(@Param("id")String id,@Param("type")String type,@Param("isyesno")String isyesno);
	
	/**
	 * 
	 * @Title: findOfficeByUserIdAndFzxRoleId
	 * @Description: TODO 查询当前用户拥有的角色对应的权限
	 * @param roleId
	 * @param id
	 * @return:
	 * @return: List<Office>
	 * @throws
	 * 2017年10月27日
	 */
	public List<Office> findOfficeByUserIdAndFzxRoleId(int roleId, String id);
	
}