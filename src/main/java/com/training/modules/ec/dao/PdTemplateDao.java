package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.PdTemplate;
/**
 * 模板dao
 * @author dalong
 *
 */

@MyBatisDao
public interface PdTemplateDao extends TreeDao<PdTemplate> {

	List<PdTemplate> getAreaList(PdTemplate template);

	/**
	 * 保存模板
	 * @param template
	 */
	void saveTemplate(PdTemplate template);

	PdTemplate getTemplate(@Param("templateId")Integer templateId);

}
