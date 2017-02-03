package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.ArticlesListDao;
import com.training.modules.train.entity.Articles;
import com.training.modules.train.entity.ArticlesCategory;
import com.training.modules.train.entity.ArticlesComment;


/**
 * 文章类别service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ArticlesListService extends CrudService<ArticlesListDao, Articles>{
	
	/**
	 * 分页查询文章
	 */
	public Page<Articles> findPage(Page<Articles> page, Articles articles) {
		articles.setPage(page);
		page.setList(dao.findList(articles));
		return page;
	}
	/**
	 * 修改文章状态
	 * @param articleId
	 * @param flag
	 * @param isyesno
	 * @return
	 */
	public int updateArticleIS(String articleId,String flag,String isyesno){
		User currentUser = UserUtils.getUser();
		return dao.updateArticleIS(Integer.parseInt(articleId), flag, Integer.parseInt(isyesno),currentUser.getId());
	}
	/**
	 * 编辑/审核 文章
	 * @param articles
	 */
	public void auditArticles(Articles articles){
		User currentUser = UserUtils.getUser(); 
		articles.setUpdateBy(currentUser);
		dao.auditArticles(articles);
	}
	/**
	 * 定时发布
	 * @param articles
	 */
	public void updateIsTask(Articles articles){
		User currentUser = UserUtils.getUser(); 
		articles.setUpdateBy(currentUser);
		dao.updateIsTask(articles);
	}
	/**
	 * 查看文章评论
	 * @param page
	 * @param articlesComment
	 * @return
	 */
	public Page<ArticlesComment> findArticlesComment(Page<ArticlesComment> page, ArticlesComment articlesComment) {
		articlesComment.setPage(page);
		page.setList(dao.findArticlesComment(articlesComment));
		return page;
	}
	/**
	 * 查看文章评论集合
	 * @param page
	 * @param articlesComment
	 * @return
	 */
	public Page<ArticlesComment> findArticlesCommentList(Page<ArticlesComment> page, ArticlesComment articlesComment){
		articlesComment.setPage(page);
		page.setList(dao.findArticlesCommentList(articlesComment));
		return page;
	}
	/**
	 * 删除评论
	 * @param articlesComment
	 */
	public void deleteComment(ArticlesComment articlesComment){
		dao.deleteComment(articlesComment);
	}
	/**
	 * 删除文章
	 * @param articles
	 */
	public void deleteArticle(Articles articles){
		dao.deleteArticle(articles);
	}
	/**
	 * 查看文章分类 
	 * @param articlesCategory
	 * @return
	 */
	public List<ArticlesCategory> findCategory(ArticlesCategory articlesCategory){
		return dao.findCategory(articlesCategory);
	}
	
	/**
	 * 查询文章所有分类
	 * @param page
	 * @param articlesCategory
	 * @return
	 */
	public Page<ArticlesCategory> findPageCategory(Page<ArticlesCategory> page,ArticlesCategory articlesCategory){
		articlesCategory.setPage(page);
		page.setList(dao.findCategory(articlesCategory));
		return page;
	}
	/**
	 * 修改保存文章分类
	 * @param articlesCategory
	 */
	public void saveCategory(ArticlesCategory articlesCategory){
		User currentUser = UserUtils.getUser(); 
		if(0 == articlesCategory.getCategoryId()){
			articlesCategory.setCreateBy(currentUser);
			dao.saveCategory(articlesCategory);
		}else{
			articlesCategory.setUpdateBy(currentUser);
			dao.updateCategory(articlesCategory);
		}
	}
	/**
	 * 删除文章分类
	 * @param articleRepositoryCategory
	 */
	public void deleteCategory(ArticlesCategory articlesCategory){
		dao.deleteCategory(articlesCategory);
	}
	/**
	 * 定时发布
	 * @return
	 */
	public int taskArticles(){
		return dao.taskArticles();
	}
}
