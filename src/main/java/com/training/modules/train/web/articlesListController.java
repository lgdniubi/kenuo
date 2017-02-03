package com.training.modules.train.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.Articles;
import com.training.modules.train.entity.ArticlesCategory;
import com.training.modules.train.entity.ArticlesComment;
import com.training.modules.train.service.ArticlesListService;


/**
 * 文章管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/train/articleslist")
public class articlesListController extends BaseController{
	
	@Autowired
	private ArticlesListService articlesListService;
	/**
	 * 分页查询文章
	 * @param articles
	 * @param articlesCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "list")
	public String ArticlesList(Articles articles,ArticlesCategory articlesCategory,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		if(0 == articles.getFlag()){
			articles.setAuditFlag(0);
		}else if(1 == articles.getFlag()){
			articles.setAuditFlag(1);
		}else if(2 == articles.getFlag()){
			articles.setDelFlag("1");
		}
		Page<Articles> page=articlesListService.findPage(new Page<Articles>(request, response), articles);
		model.addAttribute("page", page);
		List<ArticlesCategory> categoryList = articlesListService.findCategory(articlesCategory);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("articles", articles);
		return "modules/train/articlesList";
	}
	/**
	 * 修改文章状态
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"train:articleslist:updateArticleIS"},logical=Logical.OR)
	@RequestMapping(value = "updateArticleIS")
	public Map<String, String> updateArticleIS(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String articleId = request.getParameter("articleId");
			String flag = request.getParameter("FLAG");
			String isyesno = request.getParameter("ISYESNO");
			if(!StringUtils.isEmpty(articleId) && !StringUtils.isEmpty(flag) && !StringUtils.isEmpty(isyesno)){
				int num = articlesListService.updateArticleIS(articleId, flag, isyesno);
				if(num != 0){
					jsonMap.put("STATUS", "OK");
					jsonMap.put("ISYESNO", isyesno);
				}else{
					jsonMap.put("STATUS", "ERROR");
					jsonMap.put("MESSAGE", "修改失败,必要参数为空");
				}
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("修改文章出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "删除文章", e);
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	/**
	 * 编辑文章  跳转
	 * @param articles
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(Articles articles,HttpServletRequest request, RedirectAttributes redirectAttributes,Model model){
		int flag = articles.getFlag();
		try {
			String key = request.getParameter("key");
			articles = articlesListService.get(articles);
			articles.setFlag(flag);
			model.addAttribute("articles", articles);
			ArticlesCategory articlesCategory = new ArticlesCategory();
			List<ArticlesCategory> categoryList = articlesListService.findCategory(articlesCategory);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("key", key);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑/审核文章", e);
			logger.error("编辑文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作文章出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/train/articleslist/list?flag="+flag; 
		}
		return "modules/train/articlesForm";
	}
	/**
	 * 修改文章
	 * @param articles
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "auditArticles")
	public String auditArticles(Articles articles,HttpServletRequest request, RedirectAttributes redirectAttributes,Model model){
		try {
			String key = request.getParameter("key");
			if(key.equals("YEStask")){
				articlesListService.updateIsTask(articles);
				if(articles.getIsTask() == 0){
					addMessage(redirectAttributes, "取消定时发布成功");
				}else{
					addMessage(redirectAttributes, "定时发布成功");
				}
				articles.setFlag(0);
			}else{
				if(articles.getFlag() == 0){
					articlesListService.auditArticles(articles);
					addMessage(redirectAttributes, "编辑文章成功");
				}else if(articles.getFlag() == 1){
					articlesListService.auditArticles(articles);
					addMessage(redirectAttributes, "审核文章成功");
					articles.setFlag(0);
				}else{
					addMessage(redirectAttributes, "操作文章出现异常，请与管理员联系");
					return "redirect:" + adminPath + "/train/articleslist/list?flag="+articles.getFlag(); 
				}
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑/审核文章", e);
			logger.error("编辑/审核文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/articleslist/list?flag="+articles.getFlag(); 
	}
	/**
	 * 删除文章
	 * @param mtmyArticle
	 * @param model
	 * @return  
	 */
	@RequiresPermissions(value={"train:articleslist:del"},logical=Logical.OR)
	@RequestMapping(value = "deleteArticle")
	public String deleteArticle(Articles articles,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			articlesListService.deleteArticle(articles);
			addMessage(redirectAttributes, "删除文章成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章", e);
			logger.error("删除文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/articleslist/list?flag=2"; 
	}
	/**
	 * 查看文章评论
	 * @param articles
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"train:articleslist:findArticleComment"},logical=Logical.OR)
	@RequestMapping(value = "findArticleComment")
	public String findArticleComment(ArticlesComment articlesComment,HttpServletRequest request, RedirectAttributes redirectAttributes,Model model, HttpServletResponse response){
		try {
			Page<ArticlesComment> page = articlesListService.findArticlesComment(new Page<ArticlesComment>(request,response), articlesComment);
			model.addAttribute("page", page);
			model.addAttribute("articleId", articlesComment.getArticleId());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章评论", e);
			logger.error("查看文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章评论出现异常，请与管理员联系");
		}
		return "modules/train/articlesComment";
	}
	/**
	 * 删除文章评论
	 * @param mtmyArticleComment
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:articleslist:deleteComment"},logical=Logical.OR)
	@RequestMapping(value = "deleteComment")
	public String deleteComment(ArticlesComment articlesComment,HttpServletRequest request,Model model,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			articlesListService.deleteComment(articlesComment);
			addMessage(redirectAttributes, "删除评论成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章评论", e);
			logger.error("删除文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章评论出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/articleslist/findArticleComment?articleId="+articlesComment.getArticleId(); 
	}
	/**
	 * 审核评论
	 * @param articlesComment
	 * @param request
	 * @param redirectAttributes
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findArticleCommentList")
	public String findArticleCommentList(ArticlesComment articlesComment,HttpServletRequest request, RedirectAttributes redirectAttributes,Model model, HttpServletResponse response){
		if(0 == articlesComment.getFlag()){
			articlesComment.setDelFlag("0");
		}else{
			articlesComment.setDelFlag("1");
		}
		Page<ArticlesComment> page = articlesListService.findArticlesCommentList(new Page<ArticlesComment>(request,response), articlesComment);
		model.addAttribute("page", page);
		List<ArticlesCategory> categoryList = articlesListService.findCategory(new ArticlesCategory());
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("articlesComment", articlesComment);
		return "modules/train/articlesCommentList";
	}
	/**
	 * 审核删除评论
	 * @param articlesComment
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"train:articleslist:deleteCommentList"},logical=Logical.OR)
	@RequestMapping(value = "deleteCommentList")
	public String deleteCommentList(ArticlesComment articlesComment,HttpServletRequest request,Model model,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			articlesListService.deleteComment(articlesComment);
			addMessage(redirectAttributes, "删除评论成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章评论", e);
			logger.error("删除文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章评论出现异常，请与管理员联系");
		}
		redirectAttributes.addFlashAttribute("articlesComment", articlesComment);
		return "redirect:" + adminPath + "/train/articleslist/findArticleCommentList"; 
	}
	/**
	 * 文章分类列表
	 * @param articlesCategory
	 * @param request 
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "categoryList")
	public String categoryList(ArticlesCategory articlesCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<ArticlesCategory> page = articlesListService.findPageCategory(new Page<ArticlesCategory>(request,response), articlesCategory);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章分类列表", e);
			logger.error("查看文章分类列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章分类列表出现异常,请与管理员联系");
		}
		return "modules/train/articlesCategory";
	}
	/**
	 * 保存或修改文章分类
	 * @param articlesCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveCategory")
	public String saveCategory(ArticlesCategory articlesCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articlesListService.saveCategory(articlesCategory);
			if(articlesCategory.getCategoryId() <= 0){
				addMessage(redirectAttributes, "保存文章分类 "+articlesCategory.getName()+" 成功！");
			}else{
				addMessage(redirectAttributes, "修改文章分类 "+articlesCategory.getName()+" 成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改文章分类", e);
			logger.error("保存/修改文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/articleslist/categoryList";
	}
	/**
	 * 删除文章分类
	 * @param articlesCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deleteCategory")
	public String deleteCategory(ArticlesCategory articlesCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articlesListService.deleteCategory(articlesCategory);
			addMessage(redirectAttributes, "删除文章分类成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章分类", e);
			logger.error("删除文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/train/articleslist/categoryList";
	}
}
