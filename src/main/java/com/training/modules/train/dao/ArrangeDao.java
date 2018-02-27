package com.training.modules.train.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Office;
import com.training.modules.train.entity.ArrangeBeautician;
import com.training.modules.train.entity.ArrangeEquipment;
import com.training.modules.train.entity.ArrangeShop;

/**
 * 文章类别Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface ArrangeDao extends CrudDao<ArrangeShop>{
	//查询市场下所有店铺
	public List<Office> findOffice(String officeId);
	//查询店铺上班时间
	public List<Integer> findArrange(@Param(value="officeId")String officeId,@Param(value="year")int year,@Param(value="month")int month);
	//查询店铺美容师上班详情
	public List<ArrangeBeautician> findArrangeBeautician(@Param(value="officeId")String officeId,@Param(value="day")int day,@Param(value="month")int month,@Param(value="year")int year);
	//查询店铺设备上班详情
	public List<ArrangeBeautician> findArrangeEquipment(@Param(value="officeId")String officeId,@Param(value="day")int day,@Param(value="month")int month,@Param(value="year")int year);

	
	//查询店铺下特殊美容师
	public List<ArrangeBeautician> findAllBeautician(@Param(value="officeId")String officeId,@Param(value="year")int year,@Param(value="month")int month);
	//查询特殊美容师排班详情
	public List<ArrangeShop> findBeautician(@Param(value="id")String id,@Param(value="year")int year,@Param(value="month")int month);
	//清空特殊特殊美容师排班
	public void delBeautician(List<ArrangeShop> list);
	//保存特殊美容师排班
	public void saveBeautician(List<ArrangeShop> list);
	
	
	//查询店铺下普通美容师
	public List<ArrangeBeautician> findAllOrdinary(@Param(value="officeId")String officeId,@Param(value="year")int year,@Param(value="month")int month);
	
	
	//查询店铺下特殊设备
	public List<ArrangeBeautician> findAllEquipment(@Param(value="officeId")String officeId,@Param(value="year")int year,@Param(value="month")int month);
	//查询特殊设备排班详情
	public List<ArrangeEquipment> findEquipment(@Param(value="equipmentId")int equipmentId,@Param(value="year")int year,@Param(value="month")int month);
	//清空特殊设备排班
	public void delEquipment(List<ArrangeEquipment> list);
	//保存特殊设备排班
	public void saveEquipment(List<ArrangeEquipment> list);
}
