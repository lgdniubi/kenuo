package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CustomerDao;
import com.training.modules.ec.entity.Customer;
import com.training.modules.sys.dao.OfficeDao;
import com.training.modules.sys.entity.Office;

/**
 * 新客表对应的Service
 * @author xiaoye   2017年10月26日
 *
 */
@Service
@Transactional(readOnly = false)
public class CustomerService extends CrudService<CustomerDao, Customer>{
	
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private OfficeDao officeDao; 
	
	/**
	 * Crm新增用户时在新客表保存绑定的商家、店铺、美容师
	 * @param customer
	 */
	public void saveCustomer(int userId,String officeId,String beautyId){
		Customer customer = new Customer();
		Office office = officeDao.get(officeId);
		customer.setUserId(userId);
		customer.setOfficeId(office.getId());
		customer.setOfficePids(office.getParentIds());
		customer.setFranchiseeId(Integer.valueOf(office.getFranchisee().getId()));
		customer.setBeautyId(beautyId);
		customerDao.insertCustomer(customer);
		customerDao.insertCustomerLog(customer);
	}
	
	/**
	 * 消费者管理中新增用户时将当前登录人的归属商家保存为该新用户的
	 * @param officeId
	 */
	public void saveCustomerFranchisee(int userId,String officeId){
		//当当前登录人的归属商家不是登云时，进行保存操作
		if(!"1".equals(officeId) && officeId != "1"){
			Customer customer = new Customer();
			Office office = officeDao.get(officeId);
			customer.setUserId(userId);
			customer.setFranchiseeId(Integer.valueOf(office.getFranchisee().getId()));
			customerDao.insertCustomer(customer);
			customerDao.insertCustomerLog(customer);
		}
	}
	
	/**
	 * Crm中修改用户信息时保存其归属店铺，美容师
	 * @param userId
	 * @param officeId
	 * @param beautyId
	 */
	public void saveCustomerOfficeBrauty(int userId,String officeId,String beautyId){
		Office office = officeDao.get(officeId);
		int franchiseeId = Integer.valueOf(office.getFranchisee().getId());
		int result = customerDao.selectFranchisee(userId,franchiseeId);
		if(result == 1){       //当该店铺的归属商家在新店表中有记录时
			Customer oldCustomer = customerDao.selectFranchiseeOffice(userId, franchiseeId);
			if(oldCustomer.getBeautyId() == null){  //当该用户即将绑定的店铺对应的归属商家未绑定相关店铺时
				Customer customer = new Customer();
				customer.setUserId(userId);
				customer.setOfficeId(officeId);
				customer.setOfficePids(office.getParentIds());
				customer.setFranchiseeId(franchiseeId);
				customer.setBeautyId(beautyId);
				customer.setCusId(oldCustomer.getCusId());
				customerDao.insertOfficeOrBeauty(customer);
				customerDao.insertCustomerLog(customer);
			}
		}else if(result == 0){  //当该店铺的归属商家在新店表中无记录时，插入其归属商家，归属店铺，归属美容师
			Customer customer = new Customer();
			customer.setUserId(userId);
			customer.setOfficeId(officeId);
			customer.setOfficePids(office.getParentIds());
			customer.setFranchiseeId(franchiseeId);
			customer.setBeautyId(beautyId);
			customerDao.insertCustomer(customer);
			customerDao.insertCustomerLog(customer);
		}
	}
}
