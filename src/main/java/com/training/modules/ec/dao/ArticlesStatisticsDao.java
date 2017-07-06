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
package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.TreeDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.ArticlesStatisticsCountData;

/**
 * 类名称:	ArticlesStatisticsCountDataDao
 * 类描述:	文章统计数据操作接口
 * 创建人:	土豆 
 * 创建时间:	2017年6月19日
 */
@MyBatisDao
public interface ArticlesStatisticsDao extends TreeDao<ArticlesStatisticsCountData>{

	/**
	 * 方法说明:	获取定时器截止的  评论id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @return
	 */
	public Integer findCommentId();
	/**
	 * 方法说明:	获取定时器截止的  点赞数id
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @return
	 */
	public Integer findLikeId();
	/**
	 * 方法说明:	查询文章的所有统计数据
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @return
	 */
	public List<ArticlesStatisticsCountData> queryArticlesCountData(Map<String, Object> map);

	/**
	 * 方法说明:	修改mtmy_articles_statistics 表中文章的统计数据(根据传送的字段进行修改)
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @param b
	 */
	public void addArticles(ArticlesStatisticsCountData articlesStatisticsCountData);
	
	/**
	 * 方法说明:	修改mtmy_articles_statistics 表中文章的统计数据(根据传送的字段进行修改)
	 * 创建时间:	2017年6月19日
	 * 创建人:	土豆
	 * 修改记录:	修改人	修改记录	2017年6月19日
	 * @param b
	 */
	public void updateArticles(ArticlesStatisticsCountData articlesStatisticsCountData);
	/**
	 * 查询文章的评论数量
	 * @param commentId
	 * @return
	 */
	public List<ArticlesStatisticsCountData> queryArticlesComment(@Param("commentId")int commentId);
	/**
	 * 查询文章的点赞数量
	 * @param likeId
	 * @return
	 */
	public List<ArticlesStatisticsCountData> queryArticlesLike(@Param("likeId")int likeId);
	
}
