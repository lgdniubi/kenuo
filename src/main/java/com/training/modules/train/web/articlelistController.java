package com.training.modules.train.web;

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
import com.training.common.utils.IdGen;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.Article;
import com.training.modules.train.entity.ArticleCategory;
import com.training.modules.train.entity.ArticleComment;
import com.training.modules.train.service.ArticleListService;
import com.training.modules.train.utils.ExamUtils;


/**
 * 文章管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/articlelist")
public class articlelistController extends BaseController{
	@Autowired
	private ArticleListService articleListService;
	/**
	 * 文章管理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:articlelist"},logical=Logical.OR)
	@RequestMapping(value = {"articlelist", ""})
	public String articlelist(Article article,HttpServletRequest request, HttpServletResponse response, Model model){
		ArticleCategory articleCategory=new ArticleCategory();
		//文章类别数据权限
		articleCategory=ExamUtils.articleCategoryFilter(articleCategory);
		//获取所属文章类别
		List<ArticleCategory> categoryList = articleListService.lookAllCategory(articleCategory);
		model.addAttribute("categoryList", categoryList);
		//点击搜索按钮传值
		model.addAttribute("article", article);
		//文章类别数据权限
		article=ExamUtils.articleFilter(article);
		Page<Article> page=articleListService.findPage(new Page<Article>(request, response), article);
		model.addAttribute("page", page);
		return "modules/train/articleList";
	}
	/**
	 * 跳转到发布文章界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:sendArticle"},logical=Logical.OR)
	@RequestMapping(value = {"sendArticle", ""})
	public String sendArticle(ArticleCategory articleCategory,HttpServletRequest request, HttpServletResponse response, Model model){
		//文章类别数据权限
		articleCategory=ExamUtils.articleCategoryFilter(articleCategory);
		List<ArticleCategory> categoryList = articleListService.lookAllCategory(articleCategory);
		model.addAttribute("categoryList", categoryList);
		return "modules/train/article_send";
	}
	/**
	 * 添加文章
	 * @param articleCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"train:articlelist:addArticle"},logical=Logical.OR)
	@RequestMapping(value = {"addArticle", ""})
	public String saveArticle(Article article,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
//		String s2 = HtmlUtils.htmlUnescape(article.getContent());  
//        System.out.println(s2); 
        article.setContent(HtmlUtils.htmlUnescape(article.getContent()));
		if("".equals(article.getArticleId())){
			String id=IdGen.uuid();
			User currentUser = UserUtils.getUser();
			article.setArticleId(id);
			article.setCreateuser(currentUser.getId());
			article.setDelflag(0);
			articleListService.addArticl(article);
			addMessage(redirectAttributes, "发布文章成功");
		}else{
			articleListService.updateArticle(article);
			addMessage(redirectAttributes, "文章修改成功");
		}
		//   重定向
		return "redirect:" + adminPath + "/train/articlelist/articlelist";
	}
	/**
	 * 查询单个文章
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"train:articlelist:lookdetail"},logical=Logical.OR)
	@RequestMapping(value = {"lookdetail", ""})
	public String lookdetail(Article article,HttpServletRequest request, HttpServletResponse response, Model model){
		Article at=articleListService.detail(article);
		at.setContent(HtmlUtils.htmlEscape(at.getContent()));
		
		ArticleCategory articleCategory=new ArticleCategory();
		//文章类别数据权限
		articleCategory=ExamUtils.articleCategoryFilter(articleCategory);
		List<ArticleCategory> categoryList = articleListService.lookAllCategory(articleCategory);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("article", at);
		
		//num为1  查看文章  不为1 则为修改文章
		model.addAttribute("num", "1");
		return "modules/train/article_send";

	}
	/**
	 * 修改文章
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:updatedetail"},logical=Logical.OR)
	@RequestMapping(value = {"updatedetail", ""})
	public String updatedetail(Article article,String num,HttpServletRequest request, HttpServletResponse response, Model model){
		Article at=articleListService.detail(article);
		at.setContent(HtmlUtils.htmlEscape(at.getContent()));
		
		ArticleCategory articleCategory=new ArticleCategory();
		//文章类别数据权限
		articleCategory=ExamUtils.articleCategoryFilter(articleCategory);
		List<ArticleCategory> categoryList = articleListService.lookAllCategory(articleCategory);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("article", at);
		return "modules/train/article_send";

	}
	/**
	 * 文章类别管理
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:articleCategory"},logical=Logical.OR)
	@RequestMapping(value = {"articleCategory", ""})
	public String articlCategory(ArticleCategory articleCategory,HttpServletRequest request, HttpServletResponse response, Model model){
		//文章类别数据权限
		articleCategory=ExamUtils.articleCategoryFilter(articleCategory);
		List<ArticleCategory> categoryList = articleListService.lookAllCategory(articleCategory);
		model.addAttribute("categoryList", categoryList);
		return "modules/train/articleCategory";
	}
	/**
	 * 保存文章类别
	 * @param articleCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"train:articlelist:saveArticlCategory"},logical=Logical.OR)
	@RequestMapping(value = {"saveArticlCategory", ""})
	public String saveArticlCategory(ArticleCategory articleCategory,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		//categoryId为空的时候为添加分类
		if("".equals(articleCategory.getCategoryId())){
			String id=IdGen.uuid();
			User currentUser = UserUtils.getUser();
			articleCategory.setCategoryId(id);
			articleCategory.setCreateuser(currentUser.getId());
			articleCategory.setStatus(0);
			articleListService.addArticlCategory(articleCategory);
			addMessage(redirectAttributes, "添加文章类别成功");
		}else{
			//categoryId不为空的时候为修改分类
			articleListService.saveArticlCategory(articleCategory);
			addMessage(redirectAttributes, "修改文章类别成功");
		}
		//   重定向
		return "redirect:" + adminPath + "/train/articlelist/articleCategory";
	}
	/**
	 * 删除单个文章
	 * @param article
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:deleteOne"},logical=Logical.OR)
	@RequestMapping(value = {"deleteOne", ""})
	public String deleteOne(Article article,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		article.setDelflag(-1);
		articleListService.deleteAll(article);
		//   重定向
		return "redirect:" + adminPath + "/train/articlelist/articlelist";
	}
	/**
	 * 批量删除已发布文章
	 * @param article
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articlelist:deleteAll"},logical=Logical.OR)
	@RequestMapping(value = {"deleteAll", ""})
	public String deleteAll(Article article,String ids,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		String idArray[] =ids.split(",");
		for(String id : idArray){
			article.setArticleId(id);
			article.setDelflag(-1);
			articleListService.deleteAll(article);
		}
		//   重定向
		return "redirect:" + adminPath + "/train/articlelist/articlelist";
	}
	/**
	 * 删除类别
	 * @param articleCategory
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"train:articlelist:deleteCategory"},logical=Logical.OR)
	@RequestMapping(value = {"deleteCategory", ""})
	public String deleteCategory(ArticleCategory articleCategory,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		//   当类别下存在文章时  不可删除类别
		if(articleListService.lookAllArticleByCategory(articleCategory)>0){
			addMessage(redirectAttributes, "请先删除类别下的文章");
		}else{
			articleCategory.setStatus(-1);
			articleListService.updateArticlCategory(articleCategory);
			addMessage(redirectAttributes, "删除文章类别成功");
		}
		return "redirect:" + adminPath + "/train/articlelist/articleCategory";
	}
	/**
	 * 查询单个文章下的所有评论
	 * @param articleComment
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
//	@RequiresPermissions(value={"train:articlelist:findListComment"},logical=Logical.OR)
	@RequestMapping(value = {"findListComment", ""})
	public String findListComment(ArticleComment articleComment,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		Page<ArticleComment> page=articleListService.findListComment(new Page<ArticleComment>(request, response), articleComment);
		model.addAttribute("page", page);
		return "modules/train/articleReview";
	}
	/**
	 * 删除单条文章评论
	 * @param articleComment
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 */
	@RequiresPermissions(value={"train:articlelist:updateComment"},logical=Logical.OR)
	@RequestMapping(value = {"updateComment", ""})
	public String updateComment(ArticleComment articleComment,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
		articleComment.setStatus(-1);
		articleListService.updateComment(articleComment);
		addMessage(redirectAttributes, "删除文章评论成功");
		return "redirect:" + adminPath + "/train/articlelist/findListComment?articleId="+articleComment.getArticleId();
	}
}
