package com.training.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.sys.dao.FranchiseeDao;
import com.training.modules.sys.entity.Franchisee;

/**
 * 加盟商管理Service
 * @author kele
 * @version 2016-6-4 16:16:30
 */
@Service
@Transactional(readOnly = false)
public class FranchiseeService extends TreeService<FranchiseeDao,Franchisee>{

	@Autowired
	private FranchiseeDao franchiseeDao;
	
	/**
	 * 查询所有信息
	 */
	public List<Franchisee> findAllList(Franchisee franchisee){
		return franchiseeDao.findAllList(franchisee);
	}
	
	/**
	 * 通过父类ID查询  子类（用于机构管理--添加下级菜单时查询下级菜单中code最大的那个）
	 * @param pid	父类ID
	 * @return
	 */
	public List<Franchisee> findListbyPID(String pid){
		return franchiseeDao.findListbyPID(pid);
	}
	
	/**
	 * 保存
	 */
	public String saveFranchisee(Franchisee franchisee){
		franchiseeDao.insertFranchisee(franchisee);
		return franchisee.getId();
	}
	
	/**
	 * 修改
	 * 
	 * @param franchisee
	 * @return
	 */
	public int update(Franchisee franchisee){
		int result = franchiseeDao.update(franchisee); 
		if(1 == result){
			return 1;
		}else{
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {
				
			}
		}
		return result;
	}
}
