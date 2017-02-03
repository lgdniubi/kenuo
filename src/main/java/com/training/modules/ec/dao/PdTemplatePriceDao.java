package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.PdTemplatePrice;
/**
 * 模板dao
 * @author dalong
 *
 */

@MyBatisDao
public interface PdTemplatePriceDao extends TreeDao<PdTemplatePrice> {


	/**
	 * 保存模板
	 * @param template
	 */
	void saveTemplatePrice(PdTemplatePrice templatePrice);

	List<PdTemplatePrice> getTemplatePriceList(@Param("templateId")Integer templateId);

	List<PdTemplatePrice> getTemplatePrices(@Param("templateId")Integer templateId);

	void deleteTemplatePriceById(@Param("templateId")Integer templateId);


}
