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
	/**
	 * 根据用户ID查找美容师评论
	 * @param MtmyComment
	 * @return
	 */
	public List<Comment> findCommentByUserId(Comment comment){
		return dao.findBeautyByUserId(comment);
	}
	/**
	 * 店铺
	 * 查询所有用户评论
	 * @param page
	 * @param comment
	 * @return
	 */
	public Page<Comment> findShopPage(Page<Comment> page, Comment comment) {
		comment.setPage(page);
		List<Comment> list = dao.findShopList(comment);
		for (Comment com : list) {
			Float fl = Float.valueOf(com.getShopRank());
			int fl1 = (int) (fl*10);
			int c = fl1%10;
			if(c <= 9 && c >= 6 ){
				com.setShopRank((float)(fl1/10+1));
			}else if(c >= 1 && c <= 4){
				com.setShopRank((float) (fl1/10+0.5));
			}else{
				com.setShopRank(fl);
			}
		}
		return page.setList(list);
	}
	/**
	 * 店铺
	 * 查看单个评论
	 * @param comment
	 * @return
	 */
	public List<Comment> findShopByCid(Comment comment) {
		return dao.findShopByid(comment);
	}
	/**
	 * 店铺
	 * 回复店铺评论
	 * @param comment
	 */
	public void insterShopComment(Comment comment) {
		dao.insterShopComment(comment);
	}
	/**
	 * 修改单个用户所涉及的店铺评论
	 * @param comment
	 */
	public void updateShopComment(Comment comment) {
		dao.updateShopComment(comment);
	}
	
	/**
	 * 查询某一预约下对美容师的评论
	 * @param reservationId
	 * @return
	 */
	public List<Comment> queryBeautyForReservation(int reservationId){
		return dao.queryBeautyForReservation(reservationId);
	}
	
	/**
	 * 查询某一预约下对店铺的评论
	 * @param reservationId
	 * @return
	 */
	public List<Comment> queryShopForReservation(int reservationId){
		return dao.queryShopForReservation(reservationId);
	}
	
}
