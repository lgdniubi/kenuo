/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Lists;
import com.training.common.persistence.Page;
import com.training.common.service.BaseService;
import com.training.common.service.TreeService;
import com.training.common.utils.StringUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Fvo;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeAcount;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.entity.UserRoleOffice;
import com.training.modules.sys.entity.OfficeLog;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.ProtocolModelDao;
import com.training.modules.train.entity.ContractInfoVo;
import com.training.modules.train.entity.ModelFranchisee;

/**
 * 机构Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {
	
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private ProtocolModelDao protocolModelDao;
	
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}
	
	public List<Office> findListbyPID(String pid){
		return dao.findListbyPID(pid);
	}
	
	public List<Office> findListByAreaId(String id){
		return dao.findListByAreaId(id);
	}
	
	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		return dao.findByParentIdsLikeAuth(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return dao.getByCode(code);
	}
	
	/**
	 * 根据roleID找到office对象
	 * @author kele
	 * @param roleid
	 * @return
	 */
	@Transactional(readOnly = true)
	public Office getRolebyOff(String roleid){
		return dao.getRolebyOff(roleid);
	}
	/**
	 * 当实体店铺中无数据时添加 有时修改
	 * @param office
	 */
	@Transactional(readOnly = false)
	public void saveOfficeInfo(Office office){
		office.getOfficeInfo().setDetails(HtmlUtils.htmlUnescape(office.getOfficeInfo().getDetails()));
		dao.saveOfficeInfo(office);
	}
	/**
	 * 通过ID查询实体店铺详细信息
	 * @param officeInfo
	 * @return
	 */
	@Transactional(readOnly = false)
	public OfficeInfo findbyid(Office office){
		return dao.findbyid(office);
	}
	/**
	 * 删除机构时关联删除实体店铺信息
	 */
	@Transactional(readOnly = false)
	public void deleteOfficeInfo(Office office){
		dao.deleteOfficeInfo(office);
	}
	/**
	 * 添加加盟商时  同时添加到sys_office机构表中
	 * @param franchisee
	 */
	@Transactional(readOnly = false)
	public void franchiseeSaveOffice(Office office){
		dao.franchiseeSaveOffice(office);
	}
	/**
	 * 添加加盟商时  同时添加到sys_office机构表中
	 * @param franchisee
	 */
	@Transactional(readOnly = false)
	public void franchiseeUpdateOffice(Office office){
		dao.franchiseeUpdateOffice(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	};
	/**
	 * 通过code查询对应店铺所属加盟商
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public OfficeInfo findFNameByCode(OfficeInfo officeInfo){
		return dao.findFNameByCode(officeInfo);
	};
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		//  id  为空   为新增机构
		if(!"".equals(office.getId())){
			String falg = updateCode(office);
			if(!falg.equals("true")){
				office.setCode(falg);
			}
		}else{
			redisClientTemplate.set("pc_fzx_cache_" + office.getId(), office.getId());
			String code = insterCode(office);
			office.setCode(code);
		}
		//begin  用于查询当前机构 所属商家
		int codenum=office.getCode().length();
		int b=codenum%4;
		String c=office.getCode().substring(0, b+4);
		OfficeInfo o = new OfficeInfo();
		o.setFranchiseeCode(c);
		String a=findFNameByCode(o).getFranchiseeId();
		Franchisee franchisee = new Franchisee();
		franchisee.setId(a);
		office.setFranchisee(franchisee);
		//end 
		//更新子类的归属商家
		if(!"".equals(office.getId())){
			updateFranchisee(office);
		}
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	// 修改机构时更新机构的商家id
	public void updateFranchisee(Office office){
		//  office 为新office   oldOffice  为修改前office
		Office oldOffice = dao.get(office.getId());
		if(!office.getParent().getId().equals(oldOffice.getParent().getId())){
			List<Office> childList = dao.finAllByPId(office);//包含的当前父类
			if (childList != null && childList.size() > 0) {
				for (Office off : childList) {
					off.setFranchisee(office.getFranchisee());
					officeDao.updateFranchisee(off);
				}
			}
		}
	}
	//修改机构时
	public String updateCode(Office office){
		//  office 为新office   oldOffice  为修改前office
		Office oldOffice = dao.get(office.getId());
		//当父类发生变化时
		if(!office.getParent().getId().equals(oldOffice.getParent().getId())){
			List<Office> l = findListbyPID(office.getParentId());
			long size=0;
			String newCode = null;
			String oldCode = office.getCode();
			if(l.size()==0){
				size=0;
				newCode = dao.get(office.getParent().getId()).getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0");
			}else{
				for (int i = 0; i < l.size(); i++) {
					Office e = l.get(i);
					if(e.getCode().length()>=13){
						String oldSting =  e.getCode().substring(e.getCode().length()-5,e.getCode().length());
						String newString = String.valueOf(Long.valueOf(oldSting)+1);
						String [] ss = {"0000","000","00","0"};
						if(newString.length()<5){
							newString = ss[newString.length()-1] + newString;
						}
						if(e.getCode().endsWith(oldSting)){
							String a = e.getCode().substring(0, e.getCode().lastIndexOf(oldSting));
							newCode = a + newString;
						}
					}else{
						newCode=String.valueOf(Long.valueOf(e.getCode())+1);
					}
				}
			}
			logger.info("更新机构父类是新生成code："+newCode);
			dao.updateCodeFirst(oldCode);
			String code = "coffee" + oldCode;
			dao.updateCodeLast(code, newCode);
			return newCode;
		}
		return "true";
	}
//	新增机构时  可以修改上级机构   所以code需要重新生成
	public String insterCode(Office office){
		
		List<Office> l = findListbyPID(office.getParentId());
		long size=0;
		String newCode = null;
		if(l.size()==0){
			size=0;
			newCode =  get(office.getParent().getId()).getCode()  + StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0");
		}else{
			for (int i = 0; i < l.size(); i++) {
				Office e = l.get(i);
				if(e.getCode().length()>=13){
					String oldSting =  e.getCode().substring(e.getCode().length()-5,e.getCode().length());
					String newString = String.valueOf(Long.valueOf(oldSting)+1);
					String [] ss = {"0000","000","00","0"};
					if(newString.length()<5){
						newString = ss[newString.length()-1] + newString;
					}
					if(e.getCode().endsWith(oldSting)){
						String a = e.getCode().substring(0, e.getCode().lastIndexOf(oldSting));
						newCode = a + newString;
					}
				}else{
					newCode=String.valueOf(Long.valueOf(e.getCode())+1);
				}
			}
		}
		return newCode;
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		//删除店铺需要删除数据权限
		deleteUserOffice(office.getId());
	}
	
	private void deleteUserOffice(String officeId) {
		//删除sys_user_Office
		dao.deleteUserOfficeById(officeId);
		//删除fzx_user_role_office
		List<UserRoleOffice> officeOne = dao.findUserRoleOffice(officeId,1);//查询数量等于1的
		if(officeOne != null && officeOne.size()>0){
			dao.deleteUserRoleOfficeById(officeId);
			dao.deleteUserRole(officeOne);
		}
		List<UserRoleOffice> officeMore = dao.findUserRoleOffice(officeId,2);//查询数量大于1的
		if(officeMore != null && officeMore.size()>0){
			dao.deleteUserRoleOfficeById(officeId);
		}
	}

	/**
	 * 导出店铺数据
	 * @param office
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<OfficeInfo> exportOffice(Office office){
		return dao.exportOffice(office);
	}
	/**
	 * 验证机构是否存在
	 * @param officeName
	 * @return
	 */
	@Transactional(readOnly = false)
	public OfficeInfo verifyOfficeName(String upOfficeCode,int grade){
		return dao.verifyOfficeName(upOfficeCode,grade);
	}
	/**
	 * 通过父类id验证机构名称是否存在
	 * @param office
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<Office> verifyOfficeNameByPid(Office office){
		return dao.verifyOfficeNameByPid(office);
	}
	/**
	 * 通过code查询其下面的所有子机构名称
	 * @param upOfficeCode
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<OfficeInfo> verifyOfficeNameByCode(String upOfficeCode){
		return dao.verifyOfficeNameByCode(upOfficeCode);
	}
	/**
	 * 验证区域是否存在
	 * @param areaName
	 * @return
	 */
	@Transactional(readOnly = false)
	public OfficeInfo  verifyAreaName(String areaCode){
		return dao.verifyAreaName(areaCode);
	}
	@Transactional(readOnly = false)
	public void saveOfficeInfo2(OfficeInfo officeInfo){
		dao.saveOfficeInfo2(officeInfo);
	}
	
	/**
	 * 根据父类id查询子类数据 用于异步加载树形table
	 * @return
	 */
	public List<Office> findByPidforChild(Office office){
		return dao.findByPidforChild(office);
	}
	
	/**
	 * 根据选择的市场查找其对应的店铺
	 * @param id
	 * @return
	 */
	public List<Office> selectOfficeById(String id){
		return dao.selectOfficeById(id);
	}
	
	/**
	 * 根据美容师id查找其归属店铺或者市场
	 * @param id
	 * @return
	 */
	public Office selectForSpec(String id){
		return dao.selectForSpec(id);
	}
	
	/**
	 * 验证店铺下是否有员工
	 * @param id
	 * @return
	 */
	public int delConfirm(Office office){
		return dao.delConfirm(office);
	}
	
	/**
	 * 修改店铺是否属性
	 * @param office
	 */
	@Transactional(readOnly = false)
	public void updateisyesno(String id,String type,String isyesno){
		dao.updateisyesno(id,type,isyesno);
	}

	/**
	 * 
	 * @Title: findOfficeByUserIdAndFzxRoleId
	 * @Description: TODO 查询当前用户下角色的权限
	 * @param roleId
	 * @param id
	 * @return:
	 * @return: List<Office>
	 * @throws
	 * 2017年10月27日
	 */
	public List<Office> findOfficeByUserIdAndFzxRoleId(int roleId, String id) {
		return officeDao.findOfficeByUserIdAndFzxRoleId(roleId,id);
	}

	/**
	 * 
	 * @Title: newOfficeTreeData
	 * @Description: TODO 根据商家id查询此商家下的归属机构
	 * @param compId
	 * @return:
	 * @return: List<Office>
	 * @throws
	 * 2017年11月1日
	 */
	public List<Office> newOfficeTreeData(String compId) {
		List<Office> officeList = Lists.newArrayList();
		User user = UserUtils.getUser();
		Office office = new Office();
		office.setId(compId);
		office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a"));
		officeList = officeDao.newOfficeTreeData(office);
		return officeList;
	}
	
	/**
	 * 实物订单发货到店查询店铺详情
	 * @param id
	 * @return
	 */
	public OfficeInfo selectOfficeDetails(String id){
		return dao.selectOfficeDetails(id);
	}

	/**
	 * 操作店铺时保存日志记录
	 * @param officeLog
	 */
	@Transactional(readOnly = false)
	public void saveOfficeLog(OfficeLog officeLog) {
		dao.saveOfficeLog(officeLog);
	}

	/**
	 * 删除店铺时保存日志记录
	 * @param officeLog
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveOfficeLogDel(OfficeLog officeLog) {
		dao.saveOfficeLogDel(officeLog);
	}

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
	public Office checkOfficeCode(Office office) {
		return officeDao.checkOfficeCode(office);
	}
	
	/**
	 * 查询店铺关闭店日志
	 * @param officeId
	 * @return
	 */
	public Page<OfficeLog> queryOfficeLog(Page<OfficeLog> page,OfficeLog officeLog){
		officeLog.setPage(page);
		page.setList(officeDao.queryOfficeLog(officeLog));
		return page;
	}
	
	/**
	 * 查询机构账户
	 * @param officeId
	 * @return
	 */
	public OfficeAcount findOfficeAcount(String officeId){
		return this.officeDao.findOfficeAcount(officeId);
	}
	/**
	 * 变更信用额度
	 * @param OfficeAcount
	 */
	@Transactional(readOnly = false)
	public void updateOfficeCreditLimit(OfficeAcount OfficeAcount){
		//缓存锁
		RedisLock redisLock = new RedisLock(redisClientTemplate, "account_lock_office_id"+OfficeAcount.getOfficeId());
		redisLock.lock();
		double usedLimit = this.officeDao.queryusedLimit(OfficeAcount);
		if(OfficeAcount.getUseLimit() > usedLimit)
			throw new RuntimeException("可用额度发生改变，暂不可修改");
		this.officeDao.updateOfficeCreditLimit(OfficeAcount);
		redisLock.unlock();
	}
	
	/**
	 * 创建账户
	 * @param OfficeAcount
	 */
	@Transactional(readOnly = false)
	public void saveOfficeAcount(OfficeAcount officeAcount){
		this.officeDao.saveOfficeAcount(officeAcount);
	}
	/**
	 * 查找支付方式，
	 * @param id
	 * @return
	 */
	public ModelFranchisee findPayType(String id) {
		return officeDao.findPayType(id);
	}
	
	public Fvo queryFvo(String office_id){
		return officeDao.queryFvo(office_id);
	}
	
	/**
	 * 修改签约信息，重签协议删除之前的协议
	 * @param franchisee_id	商家
	 */
	@Transactional(readOnly = false)
	public void deleteProtocolShopById(String franchisee_id) {
		protocolModelDao.deleteProtocolShopById(Integer.valueOf(franchisee_id));
	}


	public List<Office> findDyOffice() {
		return officeDao.findDyOffice();
	}

	/**
	 * 签约信息中更改了店铺地址或者名称同步
	 * 签约信息中更改了店铺地址或者名称同步
	 * @param contractInfo
	 */
	@Transactional(readOnly = false)
	public void updateAddressAndName(ContractInfoVo contractInfo, String oldOfficeAddress, String oldOfficeName) {
		if(oldOfficeAddress !=null && !oldOfficeAddress.equals(contractInfo.getOffice_address())){
			officeDao.updateOfficeInfoDetailAddress(contractInfo);
		}
		if(oldOfficeName !=null && !oldOfficeName.equals(contractInfo.getOffice_name())){
			officeDao.updateOfficeInfoOfficeName(contractInfo);
		}
	}
}
