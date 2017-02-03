package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.PdTemplate;
import com.training.modules.ec.entity.PdWhTemplates;
/**
 * 仓库模板dao
 * @author dalong
 *
 */

@MyBatisDao
public interface PdWhTemplatesDao extends TreeDao<PdWhTemplates> {

	List<PdWhTemplates> getAreaList(PdWhTemplates whTemplates);

	/**
	 * 保存模板
	 * @param template
	 */
	void saveWhTemplate(PdWhTemplates whTemplates);

	void savePriceAndArea(List<String> list, String priceId);

	List<PdTemplate> getTemplateList(@Param("houseId")Integer houseId);

	void updateDate(PdWhTemplates templates);

	void updateStatus(PdTemplate template);

	void updateStatusAll();

	void updateTemplateDelFlag(PdTemplate template);
}
