package com.training.modules.ec.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ThemeDao;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.ec.entity.Theme;
import com.training.modules.ec.entity.ThemeMapping;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.utils.ParametersFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 热门活动Service 
 * @author xiaoye 2017年9月13日
 *
 */
@Service
@Transactional(readOnly = false)
public class ThemeService extends CrudService<ThemeDao, Theme>{
	
	@Autowired
	private ThemeDao themeDao;
	
	/**
	 * 根据themeId获取热门主题
	 * @param themeId
	 * @return
	 */
	public Theme getTheme(int themeId){
		return themeDao.getTheme(themeId);
	}
	
	/**
	 * 添加热门主题 
	 * @param theme
	 */
	public void insertTheme(Theme theme){
		themeDao.insertTheme(theme);
	}
	
	/**
	 * 修改热门主题
	 * @param theme
	 */
	public void updateTheme(Theme theme){
		themeDao.updateTheme(theme);
	}
	
	/**
	 * 逻辑删除热门主题
	 * @param themeId
	 */
	public void delTheme(int themeId){
		themeDao.delTheme(themeId);
	}
	
	/**
	 * 修改热门主题是否显示或是否推荐
	 * @param theme
	 */
	public void updateType(Theme theme){
		themeDao.updateType(theme);
	}
	
	/**
	 * 查询热门主题对应的商品
	 * @param themeMapping
	 * @return
	 */
	public Page<ThemeMapping> selectGoodsForTheme(Page<ThemeMapping> page,ThemeMapping themeMapping){
		themeMapping.setPage(page);
		page.setList(themeDao.selectGoodsForTheme(themeMapping));
		return page;
	}
	
	/**
	 * 查询热门主题对应的文章
	 * @param themeMapping
	 * @return
	 */
	public Page<ThemeMapping> selectArticlesForTheme(Page<ThemeMapping> page,ThemeMapping themeMapping,String articlesIds){
		
		List<ThemeMapping> mapList = Lists.newArrayList();
		JSONObject jsonObject = new JSONObject();
		String queryMediaArticleDetailListByArticleIdList = ParametersFactory.getMtmyParamValues("mtmy_queryMediaArticleDetailListByArticleIdList");	
		logger.info("##### web接口路径:"+queryMediaArticleDetailListByArticleIdList);	
		String parpm = "{\"sourcePlatform\":\"mtmy\",\"articleIdList\":\""+articlesIds+"\"}";
		String url=queryMediaArticleDetailListByArticleIdList;
		String result = WebUtils.postMediaObject(parpm, url);
		jsonObject = JSONObject.fromObject(result);
		logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		if("200".equals(jsonObject.get("code"))){
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			List<Map> list = (List<Map>)JSONArray.toCollection(jsonArray,Map.class);
			for(int i=0; i<list.size(); i++){
				ThemeMapping newThemeMapping = new ThemeMapping();
				ArticleRepository articleRepository = new ArticleRepository();
				articleRepository.setArticleId(((Integer.parseInt(list.get(i).get("articleId").toString()))));
				articleRepository.setTitle(String.valueOf(list.get(i).get("articleTitle")));
				newThemeMapping.setArticleRepository(articleRepository);
				mapList.add(newThemeMapping);
	 		}
		}
		
		themeMapping.setPage(page);
		page.setList(mapList);
		return page;
	}
	
	/**
	 * 热门主题添加商品或文章
	 */
	public void insertSomeThing(ThemeMapping themeMapping){
		themeDao.insertSomeThing(themeMapping);
	}
	
	/**
	 * 删除热门主题对应的商品或文章
	 * @param themeMappingId
	 */
	public void delSomeThing(int themeMappingId){
		themeDao.delSomeThing(themeMappingId);
	}
	
	/**
	 * 查询热门主题对应的文章
	 * @param themeId
	 * @return
	 */
	public String selectArticlesIds(int themeId){
		return themeDao.selectArticlesIds(themeId);
	}
	
	/**
	 * 批量删除热门主题对应的文章
	 * @param themeId
	 * @param articleId
	 */
	public void deleteArticles(@Param(value="themeId")int themeId,@Param(value="someId")int someId){
		themeDao.deleteArticles(themeId, someId);
	}
}
