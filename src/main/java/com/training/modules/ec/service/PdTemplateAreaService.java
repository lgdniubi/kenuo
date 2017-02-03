package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PdTemplateAreaDao;
import com.training.modules.ec.entity.PdTemplateArea;
import com.training.modules.ec.entity.PdTemplatePrice;
/**
 * 物流模板计费区域Service
 * @author dalong
 *
 */
@Service
@Transactional(readOnly = false)
public class PdTemplateAreaService extends TreeService<PdTemplateAreaDao,PdTemplateArea> {
	 
	/**
	 * 批量保存物流模板计费区域
	 * @param areaIds
	 * @param templatePriceId
	 */
	public void saveTemplatePriceAreas(String[] areaIds,Integer templatePriceId){
		dao.saveTemplatePriceAreas(areaIds,templatePriceId);
	}

	public List<PdTemplateArea> getTemplateAreaList(int priceId) {
		return dao.getTemplateAreaList(priceId);
	}

	public void deleteTemplateAreaByIds(List<PdTemplatePrice> templatePrices) {
		dao.deleteTemplateAreaByIds(templatePrices);
	}
}
