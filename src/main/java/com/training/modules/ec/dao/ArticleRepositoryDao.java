package com.training.modules.ec.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ArticleAuthorPhoto;
import com.training.modules.ec.entity.ArticleImage;
import com.training.modules.ec.entity.ArticleIssueLogs;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.ec.entity.ArticleRepositoryCategory;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.train.entity.ArticlesCategory;

/**
 * 文章资源dao
 * @author coffee
 *
 */
@MyBatisDao
public interface ArticleRepositoryDao extends CrudDao<ArticleRepository>{
	
	/**
	 * 删除文章首图
	 */
	public void delImages(ArticleRepository articleRepository);
	/**
	 * 保存文章首图
	 * @param list
	 */
	public void insertImages(List<ArticleImage> list);
	/**
	 * 查看文章首图
	 * @param articleRepository
	 * @return
	 */
	public List<ArticleImage> findImages(ArticleRepository articleRepository);
	/**
	 * 查询妃子校、每天美耶是否存在文章
	 * @param articleId
	 * @return
	 */
	public int findArticle(@Param(value="articleId")int articleId,@Param(value="type")String type);
	/**
	 * 发布到妃子校
	 * @param articleId
	 * @param categoryId
	 */
	public void  sendTrainArticle(@Param(value="articleId")int articleId,@Param(value="categoryId")int categoryId,@Param(value="userId")String userId);
	public void  sendTrainArticleImg(List<ArticleImage> list);
	/**
	 * 发布到每天美耶
	 * @param articleId
	 * @param categoryId
	 */
	public void  sendMtmyArticle(@Param(value="articleId")int articleId,@Param(value="categoryId")int categoryId,@Param(value="userId")String userId);
	public void  sendMtmyArticleImg(List<ArticleImage> list);
	/**
	 * 修改妃子校文章
	 * @param categoryId
	 * @param articleRepositoryCategory
	 */
	public void  updateTrainArticle(ArticleRepository articleRepository);
	public void  delTrainArticleImg(int articleId);
	/**
	 * 修改每天美耶文章
	 * @param categoryId
	 * @param articleRepositoryCategory
	 */
	public void  updateMtmyArticle(ArticleRepository articleRepository);
	public void  delMtmyArticleImg(int articleId);
	/**
	 * 发布文章日志
	 * @param articleId
	 * @param type
	 * @param categoryId
	 * @param userId
	 */
	public void saveLogs(@Param(value="articleId")int articleId,@Param(value="type")String type,@Param(value="categoryId")int categoryId,@Param(value="userId")String userId);
	/**
	 * 查看日志
	 * @param articleId
	 * @return
	 */
	public List<ArticleIssueLogs> findLogs(ArticleIssueLogs articleIssueLogs);
	/**
	 * 查询文章类别
	 * @return
	 */
	public List<ArticleRepositoryCategory> findCategory(ArticleRepositoryCategory articleRepositoryCategory);
	/**
	 * 保存文章分类
	 * @param articleRepositoryCategory
	 */
	public void saveCategory(ArticleRepositoryCategory articleRepositoryCategory);
	/**
	 * 修改文章分类
	 * @param articleRepositoryCategory
	 */
	public void updateCategory(ArticleRepositoryCategory articleRepositoryCategory);
	/**
	 * 删除文章分类
	 * @param articleRepositoryCategory
	 */
	public void deleteCategory(ArticleRepositoryCategory articleRepositoryCategory);
	/**
	 * 查询常用文章分类
	 * @return
	 */
	public List<ArticleRepositoryCategory> findCategoryCommon(String userId);
	/**
	 * 添加用户常用作者
	 * @param articleAuthorPhoto
	 * @param userId
	 */
	public void addAuthor(ArticleAuthorPhoto articleAuthorPhoto);
	/**
	 * 删除作者
	 * @param articleAuthorPhoto
	 */
	public void delAuthor(ArticleAuthorPhoto articleAuthorPhoto);
	/**
	 * 查询所有作者信息
	 * @param articleAuthorPhoto
	 * @return
	 */
	public List<ArticleAuthorPhoto> findAllAuthor(ArticleAuthorPhoto articleAuthorPhoto);
	/**
	 * 查询发布妃子校常用分类
	 * @return
	 */
	public List<ArticlesCategory> findTrainCateComm(String userId);
	/**
	 * 查询发布每天美耶常用分类
	 * @return
	 */
	public List<MtmyArticleCategory> findMtmyCateComm(String userId);
	
}
