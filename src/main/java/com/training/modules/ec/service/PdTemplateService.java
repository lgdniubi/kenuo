package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PdTemplateDao;
import com.training.modules.ec.entity.PdTemplate;
import com.training.modules.ec.entity.PdTemplateArea;
import com.training.modules.ec.entity.PdTemplatePrice;
import com.training.modules.ec.entity.PdWhTemplates;
import com.training.modules.sys.entity.Area;
/**
 * 物流模板表Service
 * @author dalong
 *
 */
@Service
@Transactional(readOnly = false)
public class PdTemplateService extends TreeService<PdTemplateDao,PdTemplate> {
	 
	@Autowired
	private PdTemplateDao templateDao;
	@Autowired
	private PdWhTemplatesService whTemplatesService;
	@Autowired
	private PdTemplatePriceService templatePriceService;
	@Autowired
	private PdTemplateAreaService templateAreaService;
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<PdTemplate> findTemplateList(Page<PdTemplate> page, PdTemplate template) throws Exception {
		template.setPage(page);
		//根据库房id获取总模板记录
		List<PdTemplate> pdTemplates = whTemplatesService.getTemplateList(template.getHouseId());
		if(pdTemplates != null && pdTemplates.size() > 0){
			for (PdTemplate pdTemplate : pdTemplates) {
				PdTemplate _template = templateDao.getTemplate(pdTemplate.getTemplateId());
				pdTemplate.setTemplateName(_template.getTemplateName());
				//根据模板id查询不是默认记录得计费信息
				List<PdTemplatePrice> templatePrices = templatePriceService.getTemplatePriceList(pdTemplate.getTemplateId());
				for (PdTemplatePrice pdTemplatePrice : templatePrices) {
					List<PdTemplateArea> templateAreas = templateAreaService.getTemplateAreaList(pdTemplatePrice.getPriceId());
					pdTemplatePrice.setTemplateAreas(templateAreas);
				}
				pdTemplate.setTemplatePrices(templatePrices);
			}
		}
		// 执行分页查询
		page.setList(pdTemplates);
		return page;
	}
/*	public Page<PdTemplate> findTemplateList(Page<PdTemplate> page, PdTemplate template) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		template.setPage(page);
		// 执行分页查询
		page.setList(templateDao.findAllList(template));
		return page;
	}*/

	public List<PdTemplate> getAreaList(PdTemplate template) throws Exception {
		return templateDao.getAreaList(template);
	}

	public void saveTemplate(PdTemplate template) throws Exception {
		/**
		 * 保存模板
		 */
		template.setTemplateAreaId(template.getArea().getId());
		template.setTemplateAddress(template.getArea().getName()+","+template.getTemplateAddress());
		templateDao.saveTemplate(template);
		/**
		 * 保存仓库模板中间表
		 */
		saveWhTemplate(template);
		/**
		 * 保存物流模板区域计费默认记录
		 */
		saveTemplatePrice(template);
		/**
		 * 保存物流模板区域计费表并且保存区域
		 */
		savePriceAndArea(template);
	}

	/**
	 * 保存仓库模板中间表
	 */
	private void saveWhTemplate(PdTemplate template) {
		PdWhTemplates whTemplate = new PdWhTemplates();
		whTemplate.setWarehouseId(template.getHouseId());
		whTemplate.setTemplateId(template.getTemplateId());
		whTemplate.setIsDelFlag(0);
		whTemplatesService.saveWhTemplate(whTemplate);
	}
	/**
	 * 保存物流模板区域计费表并且保存区域
	 */
	private void savePriceAndArea(PdTemplate template) throws Exception {
		List<Integer> firstWeights = template.getFirstWeights();
		List<Double> firstPrices = template.getFirstPrices();
		List<Integer> addWeights = template.getAddWeights();
		List<Double> addPrices = template.getAddPrices();
		if(addPrices != null && addPrices.size() > 0){
			for (int i = 0; i < addPrices.size(); i++) {
				PdTemplatePrice _templatePrice = new PdTemplatePrice();
				_templatePrice.setTemplateId(template.getTemplateId());
				_templatePrice.setFirstWeight(firstWeights.get(i));
				_templatePrice.setFirstPrice(firstPrices.get(i));
				_templatePrice.setAddWeight(addWeights.get(i));
				_templatePrice.setAddPrice(addPrices.get(i));
				templatePriceService.saveTemplatePrice(_templatePrice);
				List<String> areaIds = template.getAreaIds();
				String _areaIds = areaIds.get(i);
				String[] areaIdList = _areaIds.split("#");
				saveTemplatePriceAreas(areaIdList,_templatePrice.getPriceId());
			}
		}
	}

	/**
	 * 保存物流模板区域计费默认记录
	 */
	private void saveTemplatePrice(PdTemplate template) {
		PdTemplatePrice templatePrice = new PdTemplatePrice();
		templatePrice.setTemplateId(template.getTemplateId());
		templatePrice.setFirstWeight(template.getFirstWeightsDefault());
		templatePrice.setFirstPrice(template.getFirstPricesDefault());
		templatePrice.setAddWeight(template.getAddWeightsDefault());
		templatePrice.setAddPrice(template.getAddPricesDefault());
		templatePrice.setIsDefault(template.getIsDefault());
		templatePriceService.saveTemplatePrice(templatePrice);
	}
	/**
	 * 批量保存物流模板计费区域
	 * @param areaIds
	 * @param integer
	 */
	public void saveTemplatePriceAreas(String[] areaIds,int priceId) throws Exception{
		templateAreaService.saveTemplatePriceAreas(areaIds,priceId);
	}

	public PdTemplate modifiedPage(Integer templateId) throws Exception {
		/**
		 * 获得当前模板信息
		 */
		PdTemplate template = templateDao.getTemplate(templateId);
		String[] address = template.getTemplateAddress().split(",");
		template.setTemplateAreaName(address[0]);
		template.setTemplateAddress(address[1]);
		/**
		 * 查询当前模板中计费记录
		 */
		List<PdTemplatePrice> templatePrices = templatePriceService.getTemplatePrices(templateId);
		if(templatePrices != null && templatePrices.size() > 0){
			for (PdTemplatePrice pdTemplatePrice : templatePrices) {
				if(pdTemplatePrice.getIsDefault() == 1){
					continue;
				}
				/**
				 * 获取模板计费中得地区域
				 */
				List<PdTemplateArea> templateAreas = templateAreaService.getTemplateAreaList(pdTemplatePrice.getPriceId());
				String areaids = "";
				for (PdTemplateArea pdTemplateArea : templateAreas) {
					areaids += pdTemplateArea.getAreaId()+"#";
				}
				String _areaids = areaids.substring(0, areaids.length()-1);
				pdTemplatePrice.setAreaIds(_areaids);
				pdTemplatePrice.setTemplateAreas(templateAreas);
			}
		}
		template.setTemplatePrices(templatePrices);
		return template;
	}

	/**
	 * 修改模板信息
	 * @param template
	 */
	public void update(PdTemplate template) throws Exception {
		//修改仓库对应模板的修改日期
		whTemplatesService.updateDate(template.getTemplateId());
		//修改模板表的数据
		template.setTemplateAreaId(template.getArea().getId());
		template.setTemplateAddress(template.getArea().getName()+","+template.getTemplateAddress());
		templateDao.update(template);
		//清除模板计费表并且重新添加新的模板计费信息
		templatePriceService.deleteTemplatePriceById(template.getTemplateId());
		saveTemplatePrice(template);
		//清除模板计费区域数据
		List<PdTemplatePrice> templatePrices = template.getTemplatePrices();
		if(templatePrices!=null && templatePrices.size()>0){
			templateAreaService.deleteTemplateAreaByIds(templatePrices);
		}
		savePriceAndArea(template);
		
	}
	/**
	 * 复制模板信息
	 * @param template
	 */
	public void copy(PdTemplate template) throws Exception {
		//获取仓库对应的模板
		PdTemplate _template = templateDao.getTemplate(template.getTemplateId());
		_template.setTemplateName(_template.getTemplateName()+"（副本）");
		_template.setHouseId(template.getHouseId());
		String templateAddress = _template.getTemplateAddress();
		String[] addressName = templateAddress.split(",");
		Area area = new Area();
		area.setId(_template.getTemplateAreaId());
		area.setName(addressName[0]);
		_template.setArea(area);
		_template.setTemplateAddress(addressName[1]);
		List<Integer> firstWeights = new ArrayList<Integer>();
		List<Double> firstPrices = new ArrayList<Double>();
		List<Integer> addWeights = new ArrayList<Integer>();
		List<Double> addPrices = new ArrayList<Double>();
		List<String> areaIds = new ArrayList<String>();
		//根据模板id查询不是默认记录得计费信息
		List<PdTemplatePrice> templatePrices = templatePriceService.getTemplatePrices(template.getTemplateId());
		for (int i = 0; i < templatePrices.size(); i++) {
			PdTemplatePrice pdTemplatePrice = templatePrices.get(i);
			Integer firstWeight = pdTemplatePrice.getFirstWeight();
			Double firstPrice = pdTemplatePrice.getFirstPrice();
			Integer addWeight = pdTemplatePrice.getAddWeight();
			Double addPrice = pdTemplatePrice.getAddPrice();
			if(pdTemplatePrice.getIsDefault() == 1){
				_template.setFirstWeightsDefault(firstWeight);
				_template.setFirstPricesDefault(firstPrice);
				_template.setAddWeightsDefault(addWeight);
				_template.setAddPricesDefault(addPrice);
				_template.setIsDefault(pdTemplatePrice.getIsDefault());
			}else{
				firstWeights.add(firstWeight);
				firstPrices.add(firstPrice);
				addWeights.add(addWeight);
				addPrices.add(addPrice);
				List<PdTemplateArea> templateAreas = templateAreaService.getTemplateAreaList(pdTemplatePrice.getPriceId());
				String areaId = "";
				for (PdTemplateArea pdTemplateArea : templateAreas) {
					areaId += pdTemplateArea.getAreaId()+"#";
				}
				areaIds.add(areaId.substring(0, areaId.length()-1));
			}
		}
		_template.setFirstWeights(firstWeights);
		_template.setFirstPrices(firstPrices);
		_template.setAddWeights(addWeights);
		_template.setAddPrices(addPrices);
		_template.setAreaIds(areaIds);
		saveTemplate(_template);
	}

	/**
	 * 修改为通用模板方法
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	public void updateStatus(PdTemplate template) throws Exception {
		whTemplatesService.updateStatusAll();
		whTemplatesService.updateStatus(template);
	}

	public void deleteTemplate(PdTemplate template) throws Exception {
		whTemplatesService.updateTemplateDelFlag(template);
	}
}
