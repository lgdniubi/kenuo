/**
 * 项目名称:	kenuo
 * 创建人:	土豆
 * 创建时间:	2017年6月19日
 * 修改人:	
 * 修改时间:	2017年6月19日
 * 修改备注:	
 * @Version
 *
 */
package com.training.modules.ec.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.TreeService;
import com.training.modules.ec.dao.ArticlesStatisticsDao;
import com.training.modules.ec.entity.ArticlesStatisticsCountData;
import com.training.modules.sys.utils.QuartzStartConfigUtils;

/**
 * 类名称:	ArticlesStatisticsService
 * 类描述:	文章统计数据业务层
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
@Service
@Transactional(readOnly = false)
public class ArticlesStatisticsService extends TreeService<ArticlesStatisticsDao, ArticlesStatisticsCountData> {

	@Autowired
	private ArticlesStatisticsDao articlesStatisticsDao;
	
	
	/**
	 * 方法说明:	查询文章的所有统计数据   并更新 mtmy_articles_statistics表数据   返回截止数据id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @return
	 */
	
	public Map<String, Object> completeArticlesStatistics(){
		//先从数据库中取出上次存入的定位id
		Integer commentId = QuartzStartConfigUtils.queryValue("mtmy_articles_comment");
		Integer likeId = QuartzStartConfigUtils.queryValue("mtmy_articles_like");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mtmy_articles_comment", commentId);
		map.put("mtmy_articles_like", likeId);
		
		//查询文章的评论数量   并更新mtmy_articles_statistics
		List<ArticlesStatisticsCountData> commentList = dao.queryArticlesComment(commentId);
		if(commentList.size() != 0){
			//修改的数据不为null  才会执行修改操作
			if(commentList.size() != 0){
				for (ArticlesStatisticsCountData articlesStatisticsCountData : commentList) {
					dao.addArticles(articlesStatisticsCountData);
				}
			}
		}
		//查询文章的点赞数量
		List<ArticlesStatisticsCountData> likeList = dao.queryArticlesLike(likeId);
		if(likeList.size() != 0){
			//修改的数据不为null  才会执行修改操作
			if(likeList.size() != 0){
				for (ArticlesStatisticsCountData articlesStatisticsCountData : likeList) {
					dao.addArticles(articlesStatisticsCountData);
				}
			}
		}
		//获取查询数据的定位id
		Map<String, Object> map2 = new HashMap<String, Object>();
		Integer new_commentId = dao.findCommentId();
		Integer new_likeId = dao.findLikeId();
		map2.put("mtmy_articles_comment", new_commentId);
		map2.put("mtmy_articles_like", new_likeId);
		return map2;
	}
	
	/**
	 * 先获取文章的ID,浏览量 保存到数据库中
	 * @param articlesStatisticsCountData
	 */
	public void addArticles(ArticlesStatisticsCountData articlesStatisticsCountData) {
		articlesStatisticsDao.addArticles(articlesStatisticsCountData);
	}

	/**
	 * 获取到文章的分享量,更新到  文章统计表数据中
	 * @param articlesStatisticsCountData
	 */
	public void updateArticles(ArticlesStatisticsCountData articlesStatisticsCountData) {
		articlesStatisticsDao.updateArticles(articlesStatisticsCountData);
	}

}
