package com.training.modules.train.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.train.dao.ArticleListDao;
import com.training.modules.train.entity.Article;
import com.training.modules.train.entity.ArticleCategory;
import com.training.modules.train.entity.ArticleComment;


/**
 * 文章类别service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ArticleListService extends CrudService<ArticleListDao, ArticleCategory>{
	/**
	 * 添加文章类别
	 * @param articleCategory
	 */
	public void addArticlCategory(ArticleCategory articleCategory){
		dao.addArticlCategory(articleCategory);
	}
	/**
	 * 修改文章类别
	 * @param articleCategory
	 */
	public void saveArticlCategory(ArticleCategory articleCategory){
		dao.saveArticlCategory(articleCategory);
	}
	/**
	 * 查看所有属于自己的文章类别
	 * @param articleCategory
	 * @return
	 */
	public List<ArticleCategory> lookAllCategory(ArticleCategory articleCategory){
		return dao.lookAllCategory(articleCategory);
	}
	/**
	 * 添加文章
	 * @param articleCategory
	 */
	public void addArticl(Article article){
		dao.addArticl(article);
	}
	/**
	 * 查询所有文章
	 * @param page
	 * @param article
	 * @return
	 */
	public Page<Article> findPage(Page<Article> page, Article article) {
		article.setPage(page);
		page.setList(dao.lookAllArticle(article));
		return page;
	}
	/**
	 * 查询单个文章
	 * @param article
	 * @return
	 */
	public Article detail(Article article){
		return dao.detail(article);
	}
	/**
	 * 修改文章
	 * @param article
	 */
	public void updateArticle(Article article){
		dao.updateArticle(article);
	}
	/**
	 * 删除文章
	 * @param article
	 */
	public void deleteAll(Article article){
		dao.deleteAll(article);
	}
	/**
	 * 通过类别查所有的文章
	 * @param articleCategory
	 * @return
	 */
	public int lookAllArticleByCategory(ArticleCategory articleCategory){
		return dao.lookAllArticleByCategory(articleCategory);
	}
	/**
	 * 删除文章类别
	 * @param articleCategory
	 */
	public void updateArticlCategory(ArticleCategory articleCategory){
		dao.updateArticlCategory(articleCategory);
	}
	/**
	 * 查询单个文章的所有评论
	 * @param articleComment
	 * @return
	 */
	public Page<ArticleComment> findListComment(Page<ArticleComment> page,ArticleComment articleComment){
		articleComment.setPage(page);
		page.setList(dao.findListComment(articleComment));
		return page;
	}
	/**
	 * 删除文章类别
	 * @param articleComment
	 */
	public void updateComment(ArticleComment articleComment){
		dao.updateComment(articleComment);
	}
}
