package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.MtmyArticleDao;
import com.training.modules.ec.entity.MtmyArticle;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.ec.entity.MtmyArticleComment;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;


/**
 * mtmy文章类别service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class MtmyArticleService extends CrudService<MtmyArticleDao, MtmyArticle>{
	/**
	 * 分页查询文章
	 */
	public Page<MtmyArticle> findPage(Page<MtmyArticle> page,MtmyArticle mtmyArticle){
		mtmyArticle.setPage(page);
		page.setList(dao.findAllList(mtmyArticle));
		return page;
	}
	/**
	 * 保存文章
	 * @param mtmyArticle
	 */
	public void saveArticle(MtmyArticle mtmyArticle){
		dao.insert(mtmyArticle);
	}
	/**
	 * 修改文章
	 * @param mtmyArticle
	 */
	public void updateArticle(MtmyArticle mtmyArticle){
		dao.update(mtmyArticle);
	}
	/**
	 * 查询文章
	 * @param id
	 * @return
	 */
	public MtmyArticle getArticle(String id){
		return dao.get(id);
	}
	/**
	 * 审核文章
	 * @param mtmyArticle
	 */
	public void auditArticle(MtmyArticle mtmyArticle){
		dao.auditArticle(mtmyArticle);
	}
	/**
	 * 分页查询文章评论
	 * @param mtmyArticleComment
	 * @return
	 */
	public Page<MtmyArticleComment> findArticleComment(Page<MtmyArticleComment> page,MtmyArticleComment mtmyArticleComment){
		mtmyArticleComment.setPage(page);
		page.setList(dao.findArticleComment(mtmyArticleComment));
		return page;
	}
	/**
	 * 删除文章评论
	 * @param mtmyArticleComment
	 */
	public void deleteComment(MtmyArticleComment mtmyArticleComment){
		dao.deleteComment(mtmyArticleComment);
	}
	/**
	 * 删除文章
	 * @param mtmyArticle
	 */
	public void deleteArticle(MtmyArticle mtmyArticle){
		dao.delete(mtmyArticle);
	}
	/**
	 * 分页查询文章分类
	 * @param page
	 * @param mtmyArticleCategory
	 * @return
	 */
	public Page<MtmyArticleCategory> findCategoryPage(Page<MtmyArticleCategory> page, MtmyArticleCategory mtmyArticleCategory) {
		mtmyArticleCategory.setPage(page);
		page.setList(dao.findListCategory(mtmyArticleCategory));
		return page;
	}
	/**
	 * 查询文章分类
	 * @param mtmyArticleCategory
	 * @return
	 */
	public List<MtmyArticleCategory> findCategory(MtmyArticleCategory mtmyArticleCategory){
		return dao.findListCategory(mtmyArticleCategory);
	}
	/**
	 * 保存文章分类
	 * @param mtmyArticleCategory
	 * @return
	 */
	public void saveCategory(MtmyArticleCategory mtmyArticleCategory){
		dao.saveCategory(mtmyArticleCategory);
	}
	/**
	 * 修改文章分类
	 * @param mtmyArticleCategory
	 */
	public void updateCatrgory(MtmyArticleCategory mtmyArticleCategory){
		dao.updateCategory(mtmyArticleCategory);
	}
	/**
	 * 删除文章分类
	 * @param mtmyArticleCategory
	 */
	public void deleteCategory(MtmyArticleCategory mtmyArticleCategory){
		dao.deleteCategory(mtmyArticleCategory);
	}
	/**
	 * 分页查询体验报名管理
	 */
	public Page<MtmyArticle> findApplyPage(Page<MtmyArticle> page,MtmyArticle mtmyArticle){
		mtmyArticle.setPage(page);
		page.setList(dao.findApplyPage(mtmyArticle));
		return page;
	}
	
	/**
	 *  更新文章的部分：排序，分类，是否置顶，是否推荐，是否显示
	 * @param mtmyArticle
	 */
	public void updateForPart(MtmyArticle mtmyArticle){
		dao.updateForPart(mtmyArticle);
	}
	
	/**
	 * 更新是否置顶
	 * @param mtmyArticle
	 */
	public void updateIsTop(MtmyArticle mtmyArticle){
		dao.updateIsTop(mtmyArticle);
	};
	
	/**
	 * 更新是否推荐
	 * @param mtmyArticle
	 */
	public void updateIsRecommend(MtmyArticle mtmyArticle){
		dao.updateIsRecommend(mtmyArticle);
	};
	
	/**
	 * 更新是否显示
	 * @param mtmyArticle
	 */
	public void updateIsShow(MtmyArticle mtmyArticle){
		dao.updateIsShow(mtmyArticle);
	};
	
	/**
	 * 定时发布
	 * @param articles
	 */
	public void updateIsTask(MtmyArticle mtmyArticle){
		User currentUser = UserUtils.getUser(); 
		mtmyArticle.setUpdateBy(currentUser);
		dao.updateIsTask(mtmyArticle);
	}
	
	/**
	 * 定时发布
	 * @return
	 */
	public int taskArticles(){
		return dao.taskArticles();
	}
	
	/**
	 * 分页查看文章评论
	 * @param mtmyArticleComment
	 * @return
	 */
	public Page<MtmyArticleComment> findAllArticleComment(Page<MtmyArticleComment> page,MtmyArticleComment mtmyArticleComment){
		mtmyArticleComment.setPage(page);
		page.setList(dao.findAllArticleComment(mtmyArticleComment));
		return page;
	}
}
