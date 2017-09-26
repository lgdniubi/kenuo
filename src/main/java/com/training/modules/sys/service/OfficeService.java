/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.training.common.service.TreeService;
import com.training.common.utils.StringUtils;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.OfficeInfo;
import com.training.modules.sys.entity.OfficeLog;
import com.training.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * 
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

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
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
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
	 * 操作店铺时保存日志记录
	 * @param officeLog
	 */
	@Transactional(readOnly = false)
	public void saveOfficeLog(OfficeLog officeLog) {
		dao.saveOfficeLog(officeLog);
	}

	/**
	 * 根据id查询本身及其子类
	 * @param office
	 * @return
	 */
	public List<Office> getOfficeListById(Office office) {
		return dao.getOfficeListById(office);
	}
}
