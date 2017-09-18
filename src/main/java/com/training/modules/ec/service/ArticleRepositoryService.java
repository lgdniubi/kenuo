package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ArticleRepositoryDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.entity.ArticleAuthorPhoto;
import com.training.modules.ec.entity.ArticleImage;
import com.training.modules.ec.entity.ArticleIssueLogs;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.ec.entity.ArticleRepositoryCategory;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.entity.ArticlesCategory;


/**
 * 文章资源service
 * @author coffee
 *
 */
@Service
@Transactional(readOnly = false)
public class ArticleRepositoryService extends CrudService<ArticleRepositoryDao, ArticleRepository>{
	
	@Autowired
	private GoodsDao goodsDao;
	/**
	 * 分页查询文章
	 */
	public Page<ArticleRepository> findPage(Page<ArticleRepository> page,ArticleRepository articleRepository){
		articleRepository.setPage(page);
		page.setList(dao.findAllList(articleRepository));
		return page;
	}
	/**
	 * 保存文章
	 */
	public void saveArticle(ArticleRepository articleRepository){
		articleRepository.setContents(HtmlUtils.htmlUnescape(articleRepository.getContents()));
		User user = UserUtils.getUser();
		if(articleRepository.getLabelGoodsId() != 0){
			Goods goods = goodsDao.get(String.valueOf(articleRepository.getLabelGoodsId()));
			articleRepository.setLabelGoodsName(goods.getGoodsName());
			articleRepository.setLabelActionType(goods.getActionType());
		}
		
		if(articleRepository.getArticleId() == 0){
			articleRepository.setCreateBy(user);
			dao.insert(articleRepository);
		}else{
			articleRepository.setUpdateBy(user);
			dao.update(articleRepository);
		}
		if(articleRepository.getImageType() > 0 && articleRepository.getImageType() <= 3){
			saveImages(articleRepository);
		}else{
			dao.delImages(articleRepository);
		}
	}
	/**
	 * 保存文章首图
	 * @param articleRepository
	 */
	public void saveImages(ArticleRepository articleRepository){
		List<ArticleImage> list = new ArrayList<ArticleImage>();
		String[] arry = articleRepository.getArticleImage().getImgUrl().split(",");
		for(int i = 0; i < articleRepository.getImageType(); i++){
			ArticleImage articleImage = new ArticleImage();
			articleImage.setArticleId(articleRepository.getArticleId());
			articleImage.setImgUrl(arry[i]);
			list.add(articleImage);
		}
		dao.delImages(articleRepository);
		dao.insertImages(list);
	}
	/**
	 * 查询文章首图
	 * @param articleRepository
	 * @return
	 */
	public List<ArticleImage> findImages(ArticleRepository articleRepository){
		return dao.findImages(articleRepository);
	}
	/**
	 * 发布文章
	 * @param type
	 * @param articleId
	 * @param categoryId
	 */
	public void sendTrainArticle(String type,int articleId,int categoryId){
		User currentUser = UserUtils.getUser();
		int num = dao.findArticle(articleId, type);
		ArticleRepository a = new ArticleRepository();
		a.setArticleId(articleId);
		List<ArticleImage> imageList = findImages(a);
		if(num == 0){
			if("train".equals(type)){
				dao.sendTrainArticle(articleId, categoryId,currentUser.getId());	// 2017年9月2日 currentUser.getId() 暂时无用 
				if(imageList.size() > 0){
					dao.sendTrainArticleImg(imageList);
				}
			}else if("mtmy".equals(type)){
				dao.sendMtmyArticle(articleId, categoryId,currentUser.getId());		// 2017年9月2日 currentUser.getId() 暂时无用 
				if(imageList.size() > 0){
					dao.sendMtmyArticleImg(imageList);					
				}
			}
		}else{
			ArticleRepository articleRepository = new ArticleRepository();
			articleRepository.setArticleId(articleId);
			ArticleRepository article = dao.get(articleRepository);
			article.setCategoryId(categoryId);
			article.setUpdateBy(currentUser);
			if("train".equals(type)){
				dao.updateTrainArticle(article);
				dao.delTrainArticleImg(articleId);
				if(imageList.size() > 0){
					dao.sendTrainArticleImg(imageList);
				}
			}else if("mtmy".equals(type)){
				dao.updateMtmyArticle(article);
				dao.delMtmyArticleImg(articleId);
				if(imageList.size() > 0){
					dao.sendMtmyArticleImg(imageList);
				}
			}
		}
		dao.saveLogs(articleId, type, categoryId, currentUser.getId());
	}
	/**
	 * 查看发布日志
	 * @param articleId
	 * @return
	 */
	public Page<ArticleIssueLogs> findLogs(Page<ArticleIssueLogs> page,ArticleIssueLogs articleIssueLogs){
		articleIssueLogs.setPage(page);
		page.setList( dao.findLogs(articleIssueLogs));
		return page;
	}
	/**
	 * 查询文章分类
	 * @return
	 */
	public List<ArticleRepositoryCategory> findCategory(ArticleRepositoryCategory articleRepositoryCategory){
		return dao.findCategory(articleRepositoryCategory);
	}
	
	/**
	 * 查询文章所有分类
	 * @param page
	 * @param articleRepositoryCategory
	 * @return
	 */
	public Page<ArticleRepositoryCategory> findPageCategory(Page<ArticleRepositoryCategory> page,ArticleRepositoryCategory articleRepositoryCategory){
		articleRepositoryCategory.setPage(page);
		page.setList(dao.findCategory(articleRepositoryCategory));
		return page;
	}
	/**
	 * 修改保存文章分类
	 * @param articleRepositoryCategory
	 */
	public void saveCategory(ArticleRepositoryCategory articleRepositoryCategory){
		User currentUser = UserUtils.getUser(); 
		if(0 == articleRepositoryCategory.getCategoryId()){
			articleRepositoryCategory.setCreateBy(currentUser);
			dao.saveCategory(articleRepositoryCategory);
		}else{
			articleRepositoryCategory.setUpdateBy(currentUser);
			dao.updateCategory(articleRepositoryCategory);
		}
	}
	/**
	 * 删除文章分类
	 * @param articleRepositoryCategory
	 */
	public void deleteCategory(ArticleRepositoryCategory articleRepositoryCategory){
		dao.deleteCategory(articleRepositoryCategory);
	}
	/**
	 * 查询常用文章分类
	 * @return
	 */
	public List<ArticleRepositoryCategory> findCategoryCommon(){
		User currentUser = UserUtils.getUser(); 
		return dao.findCategoryCommon(currentUser.getId());
	}
	/**
	 * 添加用户常用作者
	 * @param authorName
	 * @param photoUrl
	 */
	public void addAuthor(ArticleAuthorPhoto articleAuthorPhoto){
		User currentUser = UserUtils.getUser(); 
		articleAuthorPhoto.setCreateBy(currentUser);
		dao.addAuthor(articleAuthorPhoto);
	}
	/**
	 * 删除作者
	 * @param articleAuthorPhoto
	 */
	public void delAuthor(ArticleAuthorPhoto articleAuthorPhoto){
		dao.delAuthor(articleAuthorPhoto);
	}
	/**
	 * 查询所有作者信息
	 * @param articleAuthorPhoto
	 * @return
	 */
	public List<ArticleAuthorPhoto> findAllAuthor(){
		ArticleAuthorPhoto articleAuthorPhoto = new ArticleAuthorPhoto();
		articleAuthorPhoto.setCreateBy(UserUtils.getUser());
		return dao.findAllAuthor(articleAuthorPhoto);
	}
	/**
	 * 查询发布妃子校常用分类
	 * @return
	 */
	public List<ArticlesCategory> findTrainCateComm(){
		return dao.findTrainCateComm(UserUtils.getUser().getId());
	}
	/**
	 * 查询发布每天美耶常用分类
	 * @return
	 */
	public List<MtmyArticleCategory> findMtmyCateComm(){
		return dao.findMtmyCateComm(UserUtils.getUser().getId());
	}
}
