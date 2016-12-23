package com.training.modules.train.dao;


import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.Article;
import com.training.modules.train.entity.ArticleCategory;
import com.training.modules.train.entity.ArticleComment;

/**
 * 文章类别Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface ArticleListDao extends CrudDao<ArticleCategory>{
		/**
		 * 添加文章类别
		 * @param articleCategory
		 * @return
		 */
		public void addArticlCategory(ArticleCategory articleCategory);
		/**
		 * 修改文章类别
		 * @param articleCategory
		 */
		public void saveArticlCategory(ArticleCategory articleCategory);
		/**
		 * 查询当前用户所有的文章类别
		 * @return
		 */
		public List<ArticleCategory> lookAllCategory(ArticleCategory articleCategory);
		/**
		 * 添加文章
		 * @param articleCategory
		 */
		public void addArticl(Article article);
		/**
		 * 查询所有文章
		 * @param article
		 * @return
		 */
		public List<Article> lookAllArticle(Article article);
		/**
		 * 查询单个文章
		 * @param article
		 * @return
		 */
		public Article detail(Article article);
		/**
		 * 修改文章
		 * @param article
		 */
		public void updateArticle(Article article);
		/**
		 * 删除文章
		 */
		public void deleteAll(Article article);
		/**
		 * 通过类别查所有的文章的个数
		 * @param articleCategory
		 * @return
		 */
		public int lookAllArticleByCategory(ArticleCategory articleCategory);
		/**
		 * 删除文章类别
		 */
		public void updateArticlCategory(ArticleCategory articleCategory);
		/**
		 * 查询单个文章的所有评论
		 * @param articleComment
		 * @return
		 */
		public List<ArticleComment> findListComment(ArticleComment articleComment);
		/**
		 * 删除单条文章评论
		 * @param articleComment
		 */
		public void updateComment(ArticleComment articleComment);
		
		
}
