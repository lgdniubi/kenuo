package com.training.modules.train.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Articles;
import com.training.modules.train.entity.ArticlesCategory;
import com.training.modules.train.entity.ArticlesComment;

/**
 * 文章类别Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface ArticlesListDao extends CrudDao<Articles>{
	
	/**
	 * 编辑/审核 文章
	 * @param articles
	 */
	public void auditArticles(Articles articles);
	/**
	 * 定时发布
	 * @param articles
	 */
	public void updateIsTask(Articles articles);
	/**
	 * 查看文章评论
	 * @param articlesComment
	 * @return
	 */
	public List<ArticlesComment> findArticlesComment(ArticlesComment articlesComment);
	/**
	 * 查看文章评论集合
	 * @param articlesComment
	 * @return
	 */
	public List<ArticlesComment> findArticlesCommentList(ArticlesComment articlesComment);
	/**
	 * 删除评论
	 * @param articlesComment
	 */
	public void deleteComment(ArticlesComment articlesComment);
	/**
	 * 修改文章状态
	 * @param articleId
	 * @param flag
	 * @param isyesno
	 * @return
	 */
	public int updateArticleIS(@Param(value="articleId")int articleId,@Param(value="flag")String flag,@Param(value="isyesno")int isyesno,@Param(value="userId")String userId);
	/**
	 * 删除文章
	 * @param articles
	 */
	public void deleteArticle(Articles articles);
	/**
	 * 查询文章所有分类
	 * @param articlesCategory
	 * @return
	 */
	public List<ArticlesCategory> findCategory(ArticlesCategory articlesCategory);
	/**
	 * 保存文章分类
	 * @param articlesCategory
	 */
	public void saveCategory(ArticlesCategory articlesCategory);
	/**
	 * 修改文章分类
	 * @param articlesCategory
	 */
	public void updateCategory(ArticlesCategory articlesCategory);
	/**
	 * 删除文章分类
	 * @param articlesCategory
	 */
	public void deleteCategory(ArticlesCategory articlesCategory);
	/**
	 * 定时发布
	 * @return
	 */
	public int taskArticles();
}
