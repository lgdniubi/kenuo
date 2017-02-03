package com.training.modules.ec.dao;


import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.MtmyArticle;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.ec.entity.MtmyArticleComment;

/**
 * mtmy文章类别Dao接口
 * @author coffee
 *
 */
@MyBatisDao
public interface MtmyArticleDao extends CrudDao<MtmyArticle>{
	/**
	 * 审核文章
	 * @param mtmyArticle
	 */
	public void auditArticle(MtmyArticle mtmyArticle);
	/**
	 * 分页查看文章评论
	 * @param mtmyArticleComment
	 * @return
	 */
	public List<MtmyArticleComment> findArticleComment(MtmyArticleComment mtmyArticleComment);
	/**
	 * 删除文章评论
	 * @param mtmyArticleComment
	 */
	public void deleteComment(MtmyArticleComment mtmyArticleComment);
	/**
	 * 分页查询所有文章分类
	 * @param mtmyArticleCategory
	 * @return
	 */
	public List<MtmyArticleCategory> findListCategory(MtmyArticleCategory mtmyArticleCategory);
	/**
	 * 保存文章类别
	 * @param mtmyArticleCategory
	 * @return
	 */
	public void saveCategory(MtmyArticleCategory mtmyArticleCategory);
	/**
	 * 修改文章类别
	 * @param mtmyArticleCategory
	 */
	public void updateCategory(MtmyArticleCategory mtmyArticleCategory);
	/**
	 * 删除文章分类
	 * @param mtmyArticleCategory
	 */
	public void deleteCategory(MtmyArticleCategory mtmyArticleCategory);
	
	/**
	 * 分页查询体验报名管理
	 * @return
	 */
	public List<MtmyArticle> findApplyPage(MtmyArticle mtmyArticle);
	
    /**
     * 更新文章的部分：排序，分类，是否置顶，是否推荐，是否显示
     * @param mtmyArticle
     */
	public void updateForPart(MtmyArticle mtmyArticle);
	
	/**
	 * 更新是否置顶
	 * @param mtmyArticle
	 */
	public void updateIsTop(MtmyArticle mtmyArticle);
	
	/**
	 * 更新是否推荐
	 * @param mtmyArticle
	 */
	public void updateIsRecommend(MtmyArticle mtmyArticle);
	
	/**
	 * 更新是否显示
	 * @param mtmyArticle
	 */
	public void updateIsShow(MtmyArticle mtmyArticle);
	/**
	 * 定时发布
	 * @param articles
	 */
	public void updateIsTask(MtmyArticle mtmyArticle);
	
	/**
	 * 定时发布
	 * @return
	 */
	public int taskArticles();
	
	/**
	 * 分页查看文章所有评论
	 * @param mtmyArticleComment
	 * @return
	 */
	public List<MtmyArticleComment> findAllArticleComment(MtmyArticleComment mtmyArticleComment);
}
