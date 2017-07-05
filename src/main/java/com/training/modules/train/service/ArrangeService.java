package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.sys.entity.Office;
import com.training.modules.train.dao.ArrangeDao;
import com.training.modules.train.entity.ArrangeBeautician;
import com.training.modules.train.entity.ArrangeEquipment;
import com.training.modules.train.entity.ArrangeShop;


/**
 * 文章类别service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ArrangeService extends CrudService<ArrangeDao,ArrangeShop>{
	//查询市场下所有店铺
	public List<Office> findOffice(String officeId){
		return dao.findOffice(officeId);
	}
	//查询店铺上班时间
	public List<Integer> findArrange(String officeId,int month){
		return dao.findArrange(officeId, month);
	} 
	//查询店铺美容师上班详情
	public List<ArrangeBeautician> findArrangeBeautician(String officeId,int day,int month){
		return dao.findArrangeBeautician(officeId, day, month);
	}
	//查询店铺设备上班详情
	public List<ArrangeBeautician> findArrangeEquipment(String officeId,int day,int month){
		return dao.findArrangeEquipment(officeId, day, month);
	}
	//查询店铺下的特殊美容师
	public List<ArrangeBeautician> findAllBeautician(String officeId,int month){
		return dao.findAllBeautician(officeId,month);
	}
	//查询特殊美容师排班详情
	public List<ArrangeShop> findBeautician(String id,int month){
		return dao.findBeautician(id,month);
	}
	//保存特殊美容师排班
	public void saveBeautician(List<String> ids,List<ArrangeShop> list){
		if(ids.size() > 0){
			dao.delBeautician(ids);
		}
		if(list.size() > 0){
			dao.saveBeautician(list);
		}
	}
	//查询店铺下的普通美容师
	public List<ArrangeBeautician> findAllOrdinary(String officeId,int month){
		return dao.findAllOrdinary(officeId, month);
	}
	//查询店铺下的特殊设备
	public List<ArrangeBeautician> findAllEquipment(String officeId,int month){
		return dao.findAllEquipment(officeId,month);
	}
	//查询特殊设备排班详情
	public List<ArrangeEquipment> findEquipment(int equipmentId,int month){
		return dao.findEquipment(equipmentId, month);
	}
	//保存特殊设备排班
	public void saveEquipment(List<String> ids,List<ArrangeEquipment> list){
		if(ids.size() > 0){
			dao.delEquipment(ids);
		}
		if(list.size() > 0){
			dao.saveEquipment(list);
		}
	}
}
