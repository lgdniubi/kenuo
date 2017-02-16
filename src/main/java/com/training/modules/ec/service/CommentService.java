package com.training.modules.ec.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.CommentDao;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.MtmyComment;

/**
 * 评论Service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class CommentService extends CrudService<CommentDao, Comment>{
	
	/**
	 * 商品
	 * 查询所有用户评论
	 */
	public Page<Comment> findPage(Page<Comment> page,Comment comment){
		comment.setPage(page);
		return page.setList(dao.findAllList(comment));
	}
	/**
	 * 商品
	 * 查询单个评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findRealByCid(Comment comment){
		return dao.findRealByCid(comment);
	}
	/**
	 * 商品
	 * 回复商品评论
	 * @param comment
	 */
	public void insterRealComment(Comment comment){
		dao.insert(comment);
	}
	/**
	 * 美容师
	 * 查询所有用户评论
	 * @param page
	 * @param comment
	 * @return
	 */
	public Page<Comment> findBeautyPage(Page<Comment> page,Comment comment){
		comment.setPage(page);
		return page.setList(dao.findList(comment));
	}
	/**
	 * 美容师
	 * 查看单个评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findBeautyByCid(Comment comment){
		return dao.findBeautyByCid(comment);
	}
	/**
	 * 美容师
	 * 回复美容师评论
	 * @param comment
	 */
	public void insterbeautyComment(Comment comment){
		dao.insterbeautyComment(comment);
	}
	/**
	 * 修改单个用户所涉及的商品评论
	 * @param comment
	 */
	public void updateRealComment(Comment comment){
		dao.updateRealComment(comment);
	}; 
	/**
	 * 修改单个用户所涉及的美容师评论
	 * @param comment
	 */
	public void updateBeautyComment(Comment comment){
		dao.updateBeautyComment(comment);
	}; 
	/**
	 * 导出商品评论
	 * @param MtmyComment
	 * @return
	 */
	public Page<MtmyComment> exportGoodsComment(Page<MtmyComment> page,MtmyComment mtmyComment){
		mtmyComment.setPage(page);
		return page.setList(dao.exportGoodsComment(mtmyComment));
	}
}
