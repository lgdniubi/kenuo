package com.training.modules.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Theme;
import com.training.modules.ec.entity.ThemeMapping;

/**
 * 热门主题实体类Dao
 * @author xiaoye   2017年9月13日
 *
 */
@MyBatisDao
public interface ThemeDao extends CrudDao<Theme>{
	
	/**
	 * 根据themeId获取热门主题
	 * @param themeId
	 * @return
	 */
	public Theme getTheme(int themeId);
	
	/**
	 * 添加热门主题 
	 * @param theme
	 */
	public void insertTheme(Theme theme);
	
	/**
	 * 修改热门主题
	 * @param theme
	 */
	public void updateTheme(Theme theme);
	
	/**
	 * 逻辑删除热门主题
	 * @param themeId
	 */
	public void delTheme(int themeId);
	
	/**
	 * 修改热门主题是否显示或是否推荐
	 * @param theme
	 */
	public void updateType(Theme theme);
	
	/**
	 * 查询热门主题对应的商品
	 * @param themeMapping
	 * @return
	 */
	public List<ThemeMapping> selectGoodsForTheme(ThemeMapping themeMapping);
	
	/**
	 * 查询热门主题对应的文章
	 * @param themeMapping
	 * @return
	 */
	public List<ThemeMapping> selectArticlesForTheme(ThemeMapping themeMapping);
	
	/**
	 * 热门主题添加商品或文章
	 */
	public void insertSomeThing(ThemeMapping themeMapping);
	
	/**
	 * 删除热门主题对应的商品或文章
	 * @param themeMappingId
	 */
	public void delSomeThing(int themeMappingId);
	
	/**
	 * 查询热门主题对应的文章
	 * @param themeId
	 * @return
	 */
	public String selectArticlesIds(int themeId);
	
	/**
	 * 批量删除热门主题对应的文章
	 * @param themeId
	 * @param articleId
	 */
	public void deleteArticles(@Param(value="themeId")int themeId,@Param(value="someId")int someId);
}
