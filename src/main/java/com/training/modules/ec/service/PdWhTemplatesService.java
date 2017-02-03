package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.PdWhTemplatesDao;
import com.training.modules.ec.entity.PdTemplate;
import com.training.modules.ec.entity.PdWhTemplates;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 活动
 * @author dalong
 *
 */
@Service
@Transactional(readOnly = false)
public class PdWhTemplatesService extends TreeService<PdWhTemplatesDao,PdWhTemplates> {
	 
	@Autowired
	private PdWhTemplatesDao whTemplatesDao;
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<PdWhTemplates> findTemplateList(Page<PdWhTemplates> page, PdWhTemplates whTemplates) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		whTemplates.setPage(page);
		// 执行分页查询
		page.setList(whTemplatesDao.findAllList(whTemplates));
		return page;
	}


	public void saveWhTemplate(PdWhTemplates whTemplate) {
		User user = UserUtils.getUser();
		whTemplate.setCreateBy(user);
		whTemplate.setUpdateBy(user);
		whTemplatesDao.saveWhTemplate(whTemplate);
	}


	public List<PdTemplate> getTemplateList(Integer houseId) {
		return whTemplatesDao.getTemplateList(houseId);
	}


	public void updateDate(Integer templateId) {
		User user = UserUtils.getUser();
		PdWhTemplates templates = new PdWhTemplates();
		templates.setUpdateBy(user);
		templates.setTemplateId(templateId);
		whTemplatesDao.updateDate(templates);
	}


	public void updateStatus(PdTemplate template) {
		whTemplatesDao.updateStatus(template);
	}


	public void updateStatusAll() {
		whTemplatesDao.updateStatusAll();
	}

	public void updateTemplateDelFlag(PdTemplate template) {
		whTemplatesDao.updateTemplateDelFlag(template);
	}
}
