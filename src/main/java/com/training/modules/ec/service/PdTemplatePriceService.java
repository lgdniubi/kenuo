package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PdTemplatePriceDao;
import com.training.modules.ec.entity.PdTemplatePrice;
/**
 * 活动
 * @author dalong
 *
 */
@Service
@Transactional(readOnly = false)
public class PdTemplatePriceService extends TreeService<PdTemplatePriceDao,PdTemplatePrice> {
	 
	@Autowired
	private PdTemplatePriceDao templatePriceDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<PdTemplatePrice> findTemplatePriceList(Page<PdTemplatePrice> page, PdTemplatePrice templatePrice) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		templatePrice.setPage(page);
		// 执行分页查询
		page.setList(templatePriceDao.findAllList(templatePrice));
		return page;
	}

	public void saveTemplatePrice(PdTemplatePrice templatePrice) {
		templatePriceDao.saveTemplatePrice(templatePrice);
	}

	public List<PdTemplatePrice> getTemplatePriceList(Integer templateId) {
		return templatePriceDao.getTemplatePriceList(templateId);
	}

	public List<PdTemplatePrice> getTemplatePrices(Integer templateId) {
		return templatePriceDao.getTemplatePrices(templateId);
	}

	public void deleteTemplatePriceById(Integer templateId) {
		templatePriceDao.deleteTemplatePriceById(templateId);
	}
}
