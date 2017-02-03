package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PdWareHouseDao;
import com.training.modules.ec.entity.PdWareHouse;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 仓库service
 * @author dalong
 *
 */
@Service
@Transactional(readOnly = false)
public class PdWareHouseService extends TreeService<PdWareHouseDao,PdWareHouse> {
	
	@Autowired
	private PdWareHouseDao wareHouseDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<PdWareHouse> findCourier(Page<PdWareHouse> page, PdWareHouse courierHouse) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		courierHouse.setPage(page);
		// 执行分页查询
		page.setList(wareHouseDao.findAllList(courierHouse));
		return page;
	}
	
	/**
	 * 添加
	 * @param courierHouse
	 */
	public void insertWareHouse(PdWareHouse wareHouse) {
		String areaId = wareHouse.getArea().getId();
		Area area = wareHouseDao.getAreaById(areaId);
		String parentIds = area.getParentIds();
		parentIds = parentIds+areaId;
		String[] areaIds = parentIds.split(",");
		List<String> areaids = new ArrayList<String>();
		wareHouse.setProvince(areaIds[2]);
		areaids.add(areaIds[2]);
		wareHouse.setCity(areaIds[3]);
		areaids.add(areaIds[3]);
		if(areaIds.length == 5){
			wareHouse.setDistrict(areaIds[4]);
			areaids.add(areaIds[4]);
		}
		List<Area> areas = wareHouseDao.getAreaByIds(areaids);
		String address = wareHouse.getAddress();
		String _address = "";
		if(areas!=null && areas.size() > 0){
			for (Area _area : areas) {
				_address = _address +_area.getName();
			}
		}
		_address += address;
		User user = UserUtils.getUser();
		wareHouse.setCreateBy(user);
		wareHouse.setFranchiseeId(wareHouse.getCompany().getId());
		wareHouse.setAreaId(wareHouse.getArea().getId());
		wareHouse.setAddress(_address);
		wareHouse.setHouseDelFlag(0);
		wareHouseDao.insertWareHouse(wareHouse);
	}

	/**
	 * 修改
	 * @param courierHouse
	 */
	public void updateWareHouse(PdWareHouse wareHouse) {
		String areaId = wareHouse.getArea().getId();
		Area area = wareHouseDao.getAreaById(areaId);
		String parentIds = area.getParentIds();
		parentIds = parentIds+areaId;
		String[] areaIds = parentIds.split(",");
		List<String> areaids = new ArrayList<String>();
		wareHouse.setProvince(areaIds[2]);
		areaids.add(areaIds[2]);
		wareHouse.setCity(areaIds[3]);
		areaids.add(areaIds[3]);
		if(areaIds.length == 5){
			wareHouse.setDistrict(areaIds[4]);
			areaids.add(areaIds[4]);
		}
		List<Area> areas = wareHouseDao.getAreaByIds(areaids);
		String address = wareHouse.getAddress();
		String _address = "";
		if(areas!=null && areas.size() > 0){
			for (Area _area : areas) {
				_address = _address +_area.getName();
			}
		}
		_address += address;
		wareHouse.setAddress(_address);
		User user = UserUtils.getUser();
		wareHouse.setUpdateBy(user);
		wareHouse.setUpdateDate(new Date());
		wareHouseDao.updateWareHouse(wareHouse);
	}

	/**
	 * 逻辑删除
	 * @param courierHouse
	 */
	public void updateWareHouseDelFlag(PdWareHouse courierHouse) {
		wareHouseDao.updateWareHouseDelFlag(courierHouse);
	}
	
	


}
