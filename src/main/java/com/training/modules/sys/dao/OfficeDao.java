/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeAcount;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.OfficeLog;
import com.training.modules.sys.entity.UserRoleOffice;
import com.training.modules.train.entity.ModelFranchisee;

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
	
	/**
	 * 
	 * @Title: newOfficeTreeData
	 * @Description: TODO 根据商家id查询此商家的下的归属机构
	 * @param compId
	 * @return:
	 * @return: List<Office>
	 * @throws
	 * 2017年11月1日
	 */
	public List<Office> newOfficeTreeData(Office office);
	
	/**
	 * 实物订单发货到店查询店铺详情
	 * @param id
	 * @return
	 */
	public OfficeInfo selectOfficeDetails(String id);
	
	/**
	 * 操作店铺时保存日志记录
	 * @param officeLog
	 */
	public void saveOfficeLog(OfficeLog officeLog);
	
	/**
	 * 删除店铺时保存日志记录
	 * @param officeLog
	 */
	public void saveOfficeLogDel(OfficeLog officeLog);
	/**
	 * 
	 * @Title: checkOfficeCode
	 * @Description: TODO 验证机构唯一编码
	 * @param office
	 * @return:
	 * @return: Office
	 * @throws
	 * 2017年12月26日 兵子
	 */
	public Office checkOfficeCode(Office office);
	/**
	 * 
	 * @Title: finAllByPId
	 * @Description: TODO 根据id查询所有的子类
	 * @throws
	 * 2018年2月1日 兵子
	 */
	public List<Office> finAllByPId(Object obj);
	
	/**
	 * 
	 * @Title: updateFranchisee
	 * @Description: TODO 修改机构时更新机构的商家id
	 * @throws
	 * 2018年2月6日 兵子
	 */
	public void updateFranchisee(Office off);
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
	 * 查找该机构所属商家的支付方式
	 * @param id
	 * @return
	 */
	public ModelFranchisee findPayType(String id);
	/**
	 * 删除机构时删除数据
	 * @param officeId
	 */
	public void deleteUserOfficeById(String officeId);
	
	public List<UserRoleOffice> findUserRoleOffice(@Param("officeId")String officeId, @Param("flag")Integer flag);
	/**
	 * 删除一条数据权限
	 * @param officeId
	 */
	public void deleteUserRoleOfficeById(String officeId);
	
	public void deleteUserRole(List<UserRoleOffice> list);
	
	/**
	 * 查询店铺关闭店日志
	 * @param officeId
	 * @return
	 */
	public List<OfficeLog> queryOfficeLog(OfficeLog officeLog);
}