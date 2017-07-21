package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.MtmyComment;


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
	
	/**
	 * 导出商品评论
	 * @param MtmyComment
	 * @return
	 */
	public List<MtmyComment> exportGoodsComment(MtmyComment mtmyComment);
	
	/**
	 * 根据用户ID查找美容师评论
	 * @param Comment
	 * @return
	 */
	public List<Comment> findBeautyByUserId(Comment comment);
	/**
	 * 查询商品星级、总评论数
	 * @param goodsId
	 * @return
	 */
	public Map<String, Object> findCommentByGoodId(int goodsId);
	
	/**
	 * 查询店铺评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findShopList(Comment comment);
	/**
	 * 根据id查询店铺评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findShopByid(Comment comment);
	/**
	 * 回复店铺评论
	 * @param comment
	 */
	public void insterShopComment(Comment comment);
	/**
	 * 修改单个用户所涉及的店铺评论
	 * @param comment
	 */
	public void updateShopComment(Comment comment);
	
}
