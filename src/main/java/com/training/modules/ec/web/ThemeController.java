package com.training.modules.ec.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.ThemeDao;
import com.training.modules.ec.entity.Theme;
import com.training.modules.ec.entity.ThemeMapping;
import com.training.modules.ec.service.ThemeService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 热门主题Controller
 * @author xiaoye   2017年9月13日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/theme")
public class ThemeController extends BaseController{

	@Autowired
	private ThemeService themeService;
	@Autowired
	private ThemeDao themeDao;
	@Autowired
	private FranchiseeService franchiseeService;
	
	/**
	 * 热门主题列表
	 * @param theme
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Theme theme,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			Page<Theme> page = themeService.findPage(new Page<Theme>(request, response), theme);
			model.addAttribute("page",page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "热门主题列表", e);
			logger.error("热门主题列表出错信息：" + e.getMessage());
		}
		return "modules/ec/themeList";
	}
	
	/**
	 * 跳转编辑热门主题页面
	 * @param theme
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(Theme theme,HttpServletRequest request,Model model){
		try{
			List<Franchisee> list = franchiseeService.findList(new Franchisee());
			if(theme.getThemeId() != 0){
				theme = themeService.getTheme(theme.getThemeId());
			}
			model.addAttribute("theme",theme);
			model.addAttribute("list",list);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑热门主题页面", e);
			logger.error("跳转编辑热门主题页面出错信息：" + e.getMessage());
		}
		return "modules/ec/themeForm";
	}
	
	/**
	 * 保存热门主题
	 * @param theme
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(Theme theme,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(theme.getThemeId() == 0){
				theme.setCreateBy(UserUtils.getUser());
				themeService.insertTheme(theme);
				addMessage(redirectAttributes, "添加热门主题成功！");
			}else{
				theme.setUpdateBy(UserUtils.getUser());
				themeService.updateTheme(theme);
				addMessage(redirectAttributes, "修改热门主题成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存热门主题", e);
			logger.error("保存热门主题出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存热门主题失败");
		}
		return "redirect:" + adminPath + "/ec/theme/list";
	}
	
	/**
	 * 逻辑删除热门主题
	 * @param theme
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String del(Theme theme,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(theme.getThemeId() != 0){
				themeService.delTheme(theme.getThemeId());
				addMessage(redirectAttributes, "删除成功");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除热门主题", e);
			logger.error("删除热门主题出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除热门主题失败");
		}
		return "redirect:" + adminPath + "/ec/theme/list";
	}
	
	/**
	 * 修改热门主题是否显示或是否推荐
	 * @param theme
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="updateType")
	public String updateType(Theme theme,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(theme.getThemeId() != 0){
				themeService.updateType(theme);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "修改热门主题是否显示或是否推荐", e);
			logger.error("修改热门主题是否显示或是否推荐出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "修改失败");
		}
		return "redirect:" + adminPath + "/ec/theme/list";
	}
	
	/**
	 * 查询热门主题对应的商品列表
	 * @param theme
	 * @param themeMapping
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="themeGoodsList")
	public String themeGoodsList(Theme theme,ThemeMapping themeMapping,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			themeMapping.setThemeId(theme.getThemeId());
			Page<ThemeMapping> page = themeService.selectGoodsForTheme(new Page<ThemeMapping>(request,response),themeMapping);
			model.addAttribute("page", page);
			model.addAttribute("themeMapping",themeMapping);
			model.addAttribute("isOpen", theme.getIsOpen());
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "热门主题对应的商品列表", e);
			logger.error("热门主题对应的商品列表出错信息：" + e.getMessage());
		}
		return "modules/ec/themeGoodsList";
	}
	
	/**
	 * 热门主题对应的文章列表
	 * @param theme
	 * @param themeMapping
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="themeArticlesList")
	public String themeArticlesList(Theme theme,ThemeMapping themeMapping,HttpServletRequest request,HttpServletResponse response,Model model){
		try{
			themeMapping.setThemeId(theme.getThemeId());
			String articlesIds = themeService.selectArticlesIds(theme.getThemeId());
			Page<ThemeMapping> page = themeService.selectArticlesForTheme(new Page<ThemeMapping>(request,response),themeMapping,articlesIds);
			model.addAttribute("page", page);
			model.addAttribute("themeMapping",themeMapping);
			model.addAttribute("articlesIds",articlesIds);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "热门主题对应的文章列表", e);
			logger.error("热门主题对应的文章列表出错信息：" + e.getMessage());
		}
		return "modules/ec/themeArticlesList";
	}
	
	/**
	 * 跳转热门主题添加商品页面
	 * @param themeMapping
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="themeGoodsForm")
	public String themeGoodsForm(ThemeMapping themeMapping,HttpServletRequest request,Model model){
		try{
			List<ThemeMapping> goodsList = themeDao.selectGoodsForTheme(themeMapping);
			model.addAttribute("goodsList",goodsList);
			model.addAttribute("isOpen", request.getParameter("isOpen"));
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转热门主题添加商品页面", e);
			logger.error("跳转热门主题添加商品页面出错信息：" + e.getMessage());
		}
		return "modules/ec/themeGoodsForm";
	}
	
	/**
	 * 跳转热门主题添加文章页面
	 * @param themeMapping
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="themeArticlesForm")
	public String themeArticlesForm(ThemeMapping themeMapping,HttpServletRequest request,Model model){
		try{

		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转热门主题添加文章页面", e);
			logger.error("跳转热门主题添加文章页面出错信息：" + e.getMessage());
		}
		return "modules/ec/themeArticlesForm";
	}
	
	/**
	 * 保存热门主题对应的商品或文章
	 * @param themeMapping
	 * @param request
	 * @return
	 */
	@RequestMapping(value="saveThemeSomeThing")
	@ResponseBody
	public String saveThemeSomeThing(ThemeMapping themeMapping,HttpServletRequest request){
		String data = "";
		try{
			String someIds = request.getParameter("someIds");
			if(!"".equals(someIds) && someIds != null){
				String someIdArray[] = someIds.split(",");
				for(String someId:someIdArray){
					if(!"".equals(someId)){
						themeMapping.setSomeId(Integer.valueOf(someId));
						themeMapping.setType(Integer.valueOf(request.getParameter("type")));
						themeService.insertSomeThing(themeMapping);
					}
				}
			}
			data = "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存热门主题对应的商品或文章", e);
			logger.error("保存热门主题对应的商品或文章出错信息：" + e.getMessage());
			data = "error";
		}
		return data.toString();
	}
	
	/**
	 * 删除热门主题对应的商品或文章
	 * @param themeMapping
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delGoods")
	@ResponseBody
	public String delGoods(ThemeMapping themeMapping,HttpServletRequest request){
		try{
			String ids = request.getParameter("ids");
			String idArray[] = ids.split(",");
			for(String id:idArray){
				themeService.delSomeThing(Integer.valueOf(id));
			}
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除热门主题对应的商品或文章", e);
			logger.error("删除热门主题对应的商品或文章出错信息：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 删除热门主题对应的文章
	 * @param themeMapping
	 * @param request
	 * @return
	 */
	@RequestMapping(value="deleteArticles")
	@ResponseBody
	public String deleteArticles(ThemeMapping themeMapping,HttpServletRequest request){
		try{
			String themeId = request.getParameter("themeId");
			String ids = request.getParameter("ids");
			String idArray[] = ids.split(",");
			for(String id:idArray){
				themeService.deleteArticles(Integer.valueOf(themeId),Integer.valueOf(id));
			}
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "批量删除热门主题对应的商品或文章", e);
			logger.error("批量删除热门主题对应的商品或文章出错信息：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 调接口获得每天美耶文章分类
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeArticlesCategoryData")
	public List<Map<String, Object>> treeArticlesCategoryData(HttpServletRequest request,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		try{
			JSONObject jsonObject = new JSONObject();
			String mtmyMediaArticleCategoryList =	ParametersFactory.getMtmyParamValues("mtmy_queryMediaArticleCategoryList");	
			logger.info("##### web接口路径:"+mtmyMediaArticleCategoryList);	
			String parpm = "{\"sourcePlatform\":\"mtmy\"}";
			String url=mtmyMediaArticleCategoryList;
			String result = WebUtils.postMediaObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
			if("200".equals(jsonObject.get("code"))){
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				List<Map> list = (List<Map>)JSONArray.toCollection(jsonArray,Map.class);
				for(int i=0; i<list.size(); i++){
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", list.get(i).get("articleCateId"));
					map.put("name", list.get(i).get("articleCateName"));
					mapList.add(map);
		 		}
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "调用接口:获得每天美耶文章分类", e);
			logger.error("调用接口:获得每天美耶文章分类错误信息:"+e.getMessage());
		}
		return mapList;
	}
	
	/**
	 * 调接口获得发现的文章
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeArticlesData")
	public List<Map<String, Object>> treeArticlesData(HttpServletRequest request,String categoryId,String title,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		try{
			JSONObject jsonObject = new JSONObject();
			String queryMediaArticleDetailList =	ParametersFactory.getMtmyParamValues("mtmy_queryMediaArticleDetailList");	
			logger.info("##### web接口路径:"+queryMediaArticleDetailList);	
			String parpm = "{\"sourcePlatform\":\"mtmy\",\"articleCateId\":\""+categoryId+"\",\"articleTitle\":\""+title+"\"}";
			String url=queryMediaArticleDetailList;
			String result = WebUtils.postMediaObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
			if("200".equals(jsonObject.get("code"))){
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				List<Map> list = (List<Map>)JSONArray.toCollection(jsonArray,Map.class);
				for(int i=0; i<list.size(); i++){
					Map<String, Object> map = Maps.newHashMap();
					map.put("id", list.get(i).get("articleId"));
					map.put("name", list.get(i).get("articleTitle"));
					mapList.add(map);
		 		}
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "调用接口:获得发现的文章", e);
			logger.error("调用接口:获得发现的文章错误信息:"+e.getMessage());
		}
		return mapList;
	}
}
