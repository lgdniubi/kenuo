package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.PdTemplateArea;
import com.training.modules.ec.entity.PdTemplatePrice;
/**
 * 物流模板计费区域dao
 * @author dalong
 *
 */

@MyBatisDao
public interface PdTemplateAreaDao extends TreeDao<PdTemplateArea> {
	/**
	 * 批量保存物流模板计费区域
	 * @param areaIds
	 * @param templatePriceId
	 */
	void saveTemplatePriceAreas(@Param("areaIds")String[] areaIds, @Param("templatePriceId")Integer templatePriceId);

	List<PdTemplateArea> getTemplateAreaList(@Param("priceId")int priceId);

	void deleteTemplateAreaByIds(List<PdTemplatePrice> templatePrices);

}
