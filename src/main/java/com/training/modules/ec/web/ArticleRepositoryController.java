/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.ArticleImage;
import com.training.modules.ec.entity.ArticleIssueLogs;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.ec.entity.ArticleRepositoryCategory;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.ec.service.ArticleRepositoryService;
import com.training.modules.ec.service.MtmyArticleService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ArticlesCategory;
import com.training.modules.train.service.ArticlesListService;

/**
 * 文章资源
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/articles")
public class ArticleRepositoryController extends BaseController {
	
	@Autowired
	private ArticleRepositoryService articleRepositoryService;
	@Autowired
	private MtmyArticleService mtmyArticleService;
	@Autowired
	private ArticlesListService articlesListService;
	/**
	 * 文章列表
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			Page<ArticleRepository> page = articleRepositoryService.findPage(new Page<ArticleRepository>(request,response), articleRepository);
			model.addAttribute("page", page);
			
			List<ArticleRepositoryCategory> categoryList = articleRepositoryService.findCategory(articleRepositoryCategory);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章列表", e);
			logger.error("查看文章列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章列表出现异常,请与管理员联系");
		}
		return "modules/ec/articleRepositoryList";
	}
	/**
	 * 添加文章跳转
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "articleForm")
	public String articleForm(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			List<ArticleRepositoryCategory> categoryList = articleRepositoryService.findCategory(articleRepositoryCategory);
			model.addAttribute("categoryList", categoryList);
			if(articleRepository.getArticleId() == 0){
				List<ArticleImage> imageList = new ArrayList<ArticleImage>();
				imageList.add(new ArticleImage());
				imageList.add(new ArticleImage());
				imageList.add(new ArticleImage());
				articleRepository.setImageList(imageList);
			}else{
				//查询文章
				articleRepository = articleRepositoryService.get(articleRepository);
				articleRepository.setContents(HtmlUtils.htmlEscape(articleRepository.getContents()));
				//查询文章首图
				List<ArticleImage> imageList = articleRepositoryService.findImages(articleRepository);
				int a = 3 - imageList.size();
				for (int i = 0; i < a; i++) {
					imageList.add(new ArticleImage());
				}
				articleRepository.setImageList(imageList);
			}
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑/添加文章", e);
			logger.error("编辑/添加文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "编辑/添加文章出现异常,请与管理员联系");
		}
		return "modules/ec/articleRepositoryForm";
	}
	/**
	 * 保存修改文章
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveArticle")
	public String saveArticle(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articleRepository.setContents(HtmlUtils.htmlUnescape(articleRepository.getContents()));
			articleRepositoryService.saveArticle(articleRepository);
			addMessage(redirectAttributes, "保存/修改文章 "+articleRepository.getTitle()+" 成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存或修改文章资源", e);
			logger.error("保存或修改文章资源错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章 "+articleRepository.getTitle()+" 失败！");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 删除文章
	 * @param articleRepository
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:del"},logical=Logical.OR)
	@RequestMapping(value = "deleteArticle")
	public String deleteArticle(ArticleRepository articleRepository,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			User user = UserUtils.getUser();
			articleRepository.setUpdateBy(user);
			articleRepositoryService.delete(articleRepository);
			addMessage(redirectAttributes, "删除文章成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章", e);
			logger.error("删除文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章失败,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 发布文章跳转
	 * @param articleRepository
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:issue"},logical=Logical.OR)
	@RequestMapping(value = "issueArticle")
	public String issueArticle(ArticleRepository articleRepository,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//每天美耶文章分类
			MtmyArticleCategory mtmyArticleCategory = new MtmyArticleCategory();
			List<MtmyArticleCategory> mtmyCategoryList= mtmyArticleService.findCategory(mtmyArticleCategory);
			model.addAttribute("mtmyCategoryList", mtmyCategoryList);
			//妃子校文章分类
			ArticlesCategory articlesCategory = new ArticlesCategory();
			List<ArticlesCategory> trainsCategoryList = articlesListService.findCategory(articlesCategory);
			model.addAttribute("trainsCategoryList", trainsCategoryList);
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发布文章跳转", e);
			logger.error("发布文章跳转错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "发布文章跳转失败,请与管理员联系");
			return "redirect:" + adminPath + "/ec/articles/categoryList";
		}
		return "modules/ec/articleIssueForm";
	}
	/**
	 * 发布文章
	 * @param type
	 * @param mtmyCategoryId
	 * @param trainsCategoryId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:issue"},logical=Logical.OR)
	@RequestMapping(value = "sendArticles")
	public String sendArticles(ArticleRepository articleRepository,String type,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if("1".equals(type)){
				String trainsCategoryId = request.getParameter("trainsCategoryId");
				articleRepositoryService.sendTrainArticle("train", articleRepository.getArticleId(), Integer.parseInt(trainsCategoryId));
				addMessage(redirectAttributes, "发布文章到妃子校成功");
			}else if("2".equals(type)){
				String mtmyCategoryId = request.getParameter("mtmyCategoryId");
				articleRepositoryService.sendTrainArticle("mtmy", articleRepository.getArticleId(), Integer.parseInt(mtmyCategoryId));
				addMessage(redirectAttributes, "发布文章到每天美耶成功");
			}else{
				addMessage(redirectAttributes, "发布文章参数有误,请与管理员联系");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发布文章", e);
			logger.error("发布文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "发布文章出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 查看发布日志
	 * @param articleRepository
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:findLogs"},logical=Logical.OR)
	@RequestMapping(value = "findLogs")
	public String findLogs(ArticleIssueLogs articleIssueLogs,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			Page<ArticleIssueLogs> page = articleRepositoryService.findLogs(new Page<ArticleIssueLogs>(request,response), articleIssueLogs);
			model.addAttribute("page", page);
			model.addAttribute("articleId", articleIssueLogs.getArticleId());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章发布日志", e);
			logger.error("查看文章发布日志错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章发布日志出现异常,请与管理员联系");
		}
		return "modules/ec/articleIssueLogs";
	}
	/**
	 * 文章分类列表
	 * @param articleRepositoryCategory
	 * @param request 
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:categoryList"},logical=Logical.OR)
	@RequestMapping(value = "categoryList")
	public String categoryList(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<ArticleRepositoryCategory> page = articleRepositoryService.findPageCategory(new Page<ArticleRepositoryCategory>(request,response), articleRepositoryCategory);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章分类列表", e);
			logger.error("查看文章分类列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章分类列表出现异常,请与管理员联系");
		}
		return "modules/ec/articleCategoryList";
	}
	/**
	 * 保存或修改文章分类
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveCategory")
	public String saveCategory(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articleRepositoryService.saveCategory(articleRepositoryCategory);
			if(articleRepositoryCategory.getCategoryId() <= 0){
				addMessage(redirectAttributes, "保存文章分类 "+articleRepositoryCategory.getName()+" 成功！");
			}else{
				addMessage(redirectAttributes, "修改文章分类 "+articleRepositoryCategory.getName()+" 成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改文章分类", e);
			logger.error("保存/修改文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/categoryList";
	}
	/**
	 * 删除文章分类
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:categoryDel"},logical=Logical.OR)
	@RequestMapping(value = "deleteCategory")
	public String deleteCategory(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articleRepositoryService.deleteCategory(articleRepositoryCategory);
			addMessage(redirectAttributes, "删除文章分类成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章分类", e);
			logger.error("删除文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/categoryList";
	}
	
	public static void main(String[] args) {
		System.out.println(HtmlUtils.htmlEscape("<>"));
		System.out.println(HtmlUtils.htmlUnescape("&lt;&gt;"));
	}
}