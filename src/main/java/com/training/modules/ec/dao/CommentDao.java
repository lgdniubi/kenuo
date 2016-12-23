package com.training.modules.ec.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Comment;


/**
 * 评论接口
 * @author coffee
 *
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment>{
	/**
	 * 查看单个商品评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findRealByCid(Comment comment);
	/**
	 * 回复美容师评论
	 * @param comment
	 */
	public void insterbeautyComment(Comment comment);
	/**
	 * 查看单个美容师评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findBeautyByCid(Comment comment);
	/**
	 * 修改单个用户所涉及的商品评论
	 * @param comment
	 */
	public void updateRealComment(Comment comment); 
	/**
	 * 修改单个用户所涉及的美容师评论
	 * @param comment
	 */
	public void updateBeautyComment(Comment comment); 
}
