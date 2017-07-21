/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.ec.web;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.GoodsCategoryDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.entity.ArticleAuthorPhoto;
import com.training.modules.ec.entity.ArticleImage;
import com.training.modules.ec.entity.ArticleIssueLogs;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.ec.entity.ArticleRepositoryCategory;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyArticle;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.ec.service.ArticleRepositoryService;
import com.training.modules.ec.service.MtmyArticleService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.TrainRuleParamDao;
import com.training.modules.train.entity.Articles;
import com.training.modules.train.entity.ArticlesCategory;
import com.training.modules.train.entity.TrainRuleParam;
import com.training.modules.train.service.ArticlesListService;

import net.sf.json.JSONObject;  

/**
 * 文章资源
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/articles")
public class ArticleRepositoryController extends BaseController {
	
	@Autowired
	private ArticleRepositoryService articleRepositoryService;
	@Autowired
	private MtmyArticleService mtmyArticleService;
	@Autowired
	private ArticlesListService articlesListService;
	@Autowired
	private TrainRuleParamDao trainRuleParamDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsCategoryDao goodsCategoryDao;
	/**
	 * 文章列表
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes) {
		try {
			Page<ArticleRepository> page = articleRepositoryService.findPage(new Page<ArticleRepository>(request,response), articleRepository);
			model.addAttribute("page", page);
			
			List<ArticleRepositoryCategory> categoryList = articleRepositoryService.findCategory(articleRepositoryCategory);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章列表", e);
			logger.error("查看文章列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章列表出现异常,请与管理员联系");
		}
		return "modules/ec/articleRepositoryList";
	}
	/**
	 * 添加文章跳转
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "articleForm")
	public String articleForm(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//每天美耶文章分类
			MtmyArticleCategory mtmyArticleCategory = new MtmyArticleCategory();
			List<MtmyArticleCategory> mtmyCategoryList= mtmyArticleService.findCategory(mtmyArticleCategory);
			model.addAttribute("mtmyCategoryList", mtmyCategoryList);
			//发布每天美耶常用分类
			List<MtmyArticleCategory> mtmyCateCommList = articleRepositoryService.findMtmyCateComm();
			model.addAttribute("mtmyCateCommList", mtmyCateCommList);
			//妃子校文章分类
			ArticlesCategory articlesCategory = new ArticlesCategory();
			List<ArticlesCategory> trainsCategoryList = articlesListService.findCategory(articlesCategory);
			model.addAttribute("trainsCategoryList", trainsCategoryList);
			//发布妃子校常用分类
			List<ArticlesCategory> trainCateCommList = articleRepositoryService.findTrainCateComm();
			model.addAttribute("trainCateCommList", trainCateCommList);
			// 查询常用分类
			List<ArticleRepositoryCategory> commonCate= articleRepositoryService.findCategoryCommon();
			model.addAttribute("commonCate", commonCate);
			// 查询常用作者
			List<ArticleAuthorPhoto> authorList = articleRepositoryService.findAllAuthor();
			model.addAttribute("authorList", authorList);
			
			List<ArticleRepositoryCategory> categoryList = articleRepositoryService.findCategory(articleRepositoryCategory);
			model.addAttribute("categoryList", categoryList);
			if(articleRepository.getArticleId() == 0){
				List<ArticleImage> imageList = new ArrayList<ArticleImage>();
				imageList.add(new ArticleImage());
				imageList.add(new ArticleImage());
				imageList.add(new ArticleImage());
				articleRepository.setImageList(imageList);
			}else{
				//查询文章
				articleRepository = articleRepositoryService.get(articleRepository);
				if(articleRepository.getLabelGoodsId() != 0){
					Goods goods = goodsDao.get(String.valueOf(articleRepository.getLabelGoodsId()));
					articleRepository.setGoodsname(goods.getGoodsName());
					articleRepository.setGoodsCategoryName(goodsCategoryDao.get(goods.getGoodsCategoryId()).getName());
				}
				articleRepository.setContents(HtmlUtils.htmlEscape(articleRepository.getContents()));
				//查询文章首图
				List<ArticleImage> imageList = articleRepositoryService.findImages(articleRepository);
				int a = 3 - imageList.size();
				for (int i = 0; i < a; i++) {
					imageList.add(new ArticleImage());
				}
				articleRepository.setImageList(imageList);
			}
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑/添加文章", e);
			logger.error("编辑/添加文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "编辑/添加文章出现异常,请与管理员联系");
		}
		return "modules/ec/articleRepositoryForm";
	}
	/**
	 * 存为草稿
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveArticle")
	public String saveArticle(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			// 文章类型  1 草稿  0 发布
			articleRepository.setType(1);
			if("".equals(articleRepository.getNewLabelGoodsId()) || articleRepository.getNewLabelGoodsId() == null ){
				articleRepository.setLabelGoodsId(0);
			}else{
				articleRepository.setLabelGoodsId(Integer.valueOf(articleRepository.getNewLabelGoodsId()));
			}
			articleRepositoryService.saveArticle(articleRepository);
			addMessage(redirectAttributes, "保存/修改文章草稿 "+articleRepository.getTitle()+" 成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存或修改文章草稿", e);
			logger.error("保存或修改文章资源错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改草稿 "+articleRepository.getTitle()+" 失败！");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 编辑界面  发布文章
	 * @param articleRepository
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "confirmIssue")
	public String confirmIssue(ArticleRepository articleRepository,ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			// 文章类型  1 草稿  0 发布
			articleRepository.setType(0);
			if("".equals(articleRepository.getNewLabelGoodsId()) || articleRepository.getNewLabelGoodsId() == null ){
				articleRepository.setLabelGoodsId(0);
			}else{
				articleRepository.setLabelGoodsId(Integer.valueOf(articleRepository.getNewLabelGoodsId()));
			}
			articleRepositoryService.saveArticle(articleRepository);
			String[] strs=request.getParameter("checkType").split(","); 
			for (int i = 0; i < strs.length; i++) {
				if("train".equals(strs[i])){
					String trainsCategoryId = request.getParameter("trainsCategoryId");
					articleRepositoryService.sendTrainArticle("train", articleRepository.getArticleId(), Integer.parseInt(trainsCategoryId));
					String trainsTaskDate = request.getParameter("trainsTaskDate");
					//妃子校发现实体类
					Articles articles = new Articles();
					articles.setArticleId(articleRepository.getArticleId());
					articles.setIsTask(1);
					if(trainsTaskDate.isEmpty()){
						articles.setTaskDate(new Date());
					}else{
						articles.setTaskDate(DateUtils.parseDate(trainsTaskDate));
					}
					articlesListService.updateIsTask(articles);
				}else if("mtmy".equals(strs[i])){
					String mtmyCategoryId = request.getParameter("mtmyCategoryId");
					articleRepositoryService.sendTrainArticle("mtmy", articleRepository.getArticleId(), Integer.parseInt(mtmyCategoryId));
					String mtmyTaskDate = request.getParameter("mtmyTaskDate");
					//每天美耶发现实体类
					MtmyArticle mtmyArticle = new MtmyArticle();
					mtmyArticle.setId(String.valueOf(articleRepository.getArticleId()));
					mtmyArticle.setIsTask(1);
					if(mtmyTaskDate.isEmpty()){
						mtmyArticle.setTaskDate(new Date());
					}else{
						mtmyArticle.setTaskDate(DateUtils.parseDate(mtmyTaskDate));
					}
					mtmyArticleService.updateIsTask(mtmyArticle);
				}
			}
			addMessage(redirectAttributes, "保存/修改/发布文章 "+articleRepository.getTitle()+" 成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改/发布文章资源", e);
			logger.error("保存/修改/发布文章资源错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改/发布文章 "+articleRepository.getTitle()+" 失败！");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 删除文章
	 * @param articleRepository
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:del"},logical=Logical.OR)
	@RequestMapping(value = "deleteArticle")
	public String deleteArticle(ArticleRepository articleRepository,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			User user = UserUtils.getUser();
			articleRepository.setUpdateBy(user);
			articleRepositoryService.delete(articleRepository);
			addMessage(redirectAttributes, "删除文章成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章", e);
			logger.error("删除文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章失败,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 发布文章跳转
	 * @param articleRepository
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:issue"},logical=Logical.OR)
	@RequestMapping(value = "issueArticle")
	public String issueArticle(ArticleRepository articleRepository,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			//每天美耶文章分类
			MtmyArticleCategory mtmyArticleCategory = new MtmyArticleCategory();
			List<MtmyArticleCategory> mtmyCategoryList= mtmyArticleService.findCategory(mtmyArticleCategory);
			model.addAttribute("mtmyCategoryList", mtmyCategoryList);
			//发布每天美耶常用分类
			List<MtmyArticleCategory> mtmyCateCommList = articleRepositoryService.findMtmyCateComm();
			model.addAttribute("mtmyCateCommList", mtmyCateCommList);
			//妃子校文章分类
			ArticlesCategory articlesCategory = new ArticlesCategory();
			List<ArticlesCategory> trainsCategoryList = articlesListService.findCategory(articlesCategory);
			model.addAttribute("trainsCategoryList", trainsCategoryList);
			//发布妃子校常用分类
			List<ArticlesCategory> trainCateCommList = articleRepositoryService.findTrainCateComm();
			model.addAttribute("trainCateCommList", trainCateCommList);
			model.addAttribute("articleRepository", articleRepository);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发布文章跳转", e);
			logger.error("发布文章跳转错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "发布文章跳转失败,请与管理员联系");
			return "redirect:" + adminPath + "/ec/articles/list";
		}
		return "modules/ec/articleIssueForm";
	}
	/**
	 * 发布文章
	 * @param type
	 * @param mtmyCategoryId
	 * @param trainsCategoryId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:issue"},logical=Logical.OR)
	@RequestMapping(value = "sendArticles")
	public String sendArticles(ArticleRepository articleRepository,String checkType,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			String[] strs = checkType.split(","); 
			for (int i = 0; i < strs.length; i++) {
				if("train".equals(strs[i])){
					String trainsCategoryId = request.getParameter("trainsCategoryId");
					articleRepositoryService.sendTrainArticle("train", articleRepository.getArticleId(), Integer.parseInt(trainsCategoryId));
					String trainsTaskDate = request.getParameter("trainsTaskDate");
					//妃子校发现实体类
					Articles articles = new Articles();
					articles.setArticleId(articleRepository.getArticleId());
					articles.setIsTask(1);
					if(trainsTaskDate.isEmpty()){
						articles.setTaskDate(new Date());
					}else{
						articles.setTaskDate(DateUtils.parseDate(trainsTaskDate));
					}
					articlesListService.updateIsTask(articles);
				}else if("mtmy".equals(strs[i])){
					String mtmyCategoryId = request.getParameter("mtmyCategoryId");
					articleRepositoryService.sendTrainArticle("mtmy", articleRepository.getArticleId(), Integer.parseInt(mtmyCategoryId));
					String mtmyTaskDate = request.getParameter("mtmyTaskDate");
					//每天美耶发现实体类
					MtmyArticle mtmyArticle = new MtmyArticle();
					mtmyArticle.setId(String.valueOf(articleRepository.getArticleId()));
					mtmyArticle.setIsTask(1);
					if(mtmyTaskDate.isEmpty()){
						mtmyArticle.setTaskDate(new Date());
					}else{
						mtmyArticle.setTaskDate(DateUtils.parseDate(mtmyTaskDate));
					}
					mtmyArticleService.updateIsTask(mtmyArticle);
				}
			}
			addMessage(redirectAttributes, "发布文章成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "发布文章", e);
			logger.error("发布文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "发布文章出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/list";
	}
	/**
	 * 查看发布日志
	 * @param articleRepository
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:findLogs"},logical=Logical.OR)
	@RequestMapping(value = "findLogs")
	public String findLogs(ArticleIssueLogs articleIssueLogs,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			Page<ArticleIssueLogs> page = articleRepositoryService.findLogs(new Page<ArticleIssueLogs>(request,response), articleIssueLogs);
			model.addAttribute("page", page);
			model.addAttribute("articleId", articleIssueLogs.getArticleId());
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章发布日志", e);
			logger.error("查看文章发布日志错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章发布日志出现异常,请与管理员联系");
		}
		return "modules/ec/articleIssueLogs";
	}
	/**
	 * 文章分类列表
	 * @param articleRepositoryCategory
	 * @param request 
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:categoryList"},logical=Logical.OR)
	@RequestMapping(value = "categoryList")
	public String categoryList(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			Page<ArticleRepositoryCategory> page = articleRepositoryService.findPageCategory(new Page<ArticleRepositoryCategory>(request,response), articleRepositoryCategory);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查看文章分类列表", e);
			logger.error("查看文章分类列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查看文章分类列表出现异常,请与管理员联系");
		}
		return "modules/ec/articleCategoryList";
	}
	/**
	 * 保存或修改文章分类
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveCategory")
	public String saveCategory(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articleRepositoryService.saveCategory(articleRepositoryCategory);
			if(articleRepositoryCategory.getCategoryId() <= 0){
				addMessage(redirectAttributes, "保存文章分类 "+articleRepositoryCategory.getName()+" 成功！");
			}else{
				addMessage(redirectAttributes, "修改文章分类 "+articleRepositoryCategory.getName()+" 成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改文章分类", e);
			logger.error("保存/修改文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/categoryList";
	}
	/**
	 * 删除文章分类
	 * @param articleRepositoryCategory
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:articles:categoryDel"},logical=Logical.OR)
	@RequestMapping(value = "deleteCategory")
	public String deleteCategory(ArticleRepositoryCategory articleRepositoryCategory, HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			articleRepositoryService.deleteCategory(articleRepositoryCategory);
			addMessage(redirectAttributes, "删除文章分类成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章分类", e);
			logger.error("删除文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章分类出现异常,请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/articles/categoryList";
	}
	/**
	 * 截图
	 * @param path
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param request
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("null")
	@RequestMapping(value = "ajaxCutImg")
	public String ajaxCutImg(String path,int x,int y,int width,int height,HttpServletRequest request){
		JSONObject josn = null;
		JSONObject josnIO = null;
		try {
			// 下载照片到本地
			josnIO = convertIO(path,request,logger);
			if("200".equals(josnIO.get("status"))){
				// 截图保存到本地
				String string = cutImg(josnIO.get("msg").toString(), x, y, width, height,request,logger);
				if("true".equals(string)){
					// 上传本地照片到服务器
					josn = postImg(josnIO.get("msg").toString(),request,logger);
				}else{
					josn.put("status", "-1");
					josn.put("msg","截图出现异常,请联系管理员");
				}
			}else{
				josn.put("status", "-1");
				josn.put("msg","截图出现异常,请联系管理员");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "截图出现异常", e);
			logger.error("截图错误信息:"+e.getMessage());
			josn.put("status", "-1");
	        josn.put("msg","截图出现异常,请联系管理员");
	        return josn.toString();
		}
		boolean success = (new File(josnIO.get("msg").toString())).delete();
        if (success) {
            System.out.println("删除本地照片成功: " + josnIO.get("msg").toString());
        } else {
            System.out.println("删除本地照片失败: " + josnIO.get("msg").toString());
        }
        System.out.println(josn.get("msg"));
		return josn.toString();
	}
	// 下载图片到本地
	public JSONObject convertIO(String path,HttpServletRequest request,org.slf4j.Logger logger){
		// 返回结果集  
        JSONObject josn = new JSONObject();
	    try {
	    	//new一个URL对象  
			URL url = new URL(path);
			//打开链接  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        //设置请求方式为"GET"  
	        conn.setRequestMethod("GET");  
	        //超时响应时间为5秒  
	        conn.setConnectTimeout(5 * 1000);  
	        //通过输入流获取图片数据  
	        InputStream inStream = conn.getInputStream(); 
	        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
	        byte[] data = readInputStream(inStream);  
	        // 截取原照片后缀
	        String string = path.substring(path.lastIndexOf(".") + 1);
	        TrainRuleParam trainRuleParam  =new TrainRuleParam();
			trainRuleParam.setParamKey("excel_path"); 
//			String newURL = request.getServletContext().getRealPath("/") + "static/cutImg/down_"+new java.util.Date().getTime()+"_photo."+string;
	        String newURL = trainRuleParamDao.findParamByKey(trainRuleParam).getParamValue() + "/static/cutImg/down_"+new java.util.Date().getTime()+"_photo."+string;
	        //new一个文件对象用来保存图片，默认保存当前工程根目录  
	        File imageFile = new File(newURL);  
	        //创建输出流  
	        FileOutputStream outStream = new FileOutputStream(imageFile);  
	        //写入数据  
	        outStream.write(data);  
	        //关闭输出流  
	        outStream.close();
	        josn.put("status", "200");
	        josn.put("msg",newURL);
	        return josn;
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "截图上传--下载图片到本地出现异常", e);
			logger.error("截图上传--下载图片到本地错误信息:"+e.getMessage());
			josn.put("status", "-1");
	        josn.put("msg","截图出现异常,请联系管理员");
	        return josn;
		}
	}
	public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    } 
    public static String cutImg(String srcImageFile, int x, int y, int destWidth, int destHeight,HttpServletRequest request,org.slf4j.Logger logger) {
        try {
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            logger.info("#######照片原始宽度srcWidth= " + srcWidth + "_____原始高度srcHeight= " + srcHeight);
			double scale = 1;
            if(srcWidth >= 350 && srcHeight >= 350){
            	if(srcWidth >= srcHeight){
            		scale = (double)srcWidth/(double)350;
            	}else if(srcWidth < srcHeight){
            		scale = (double)srcHeight/(double)350;
            	}else{
            		scale = 0;
            	}
            }else if(srcWidth > 350 && srcHeight <= 350){
            	scale = (double)srcWidth/(double)350;
            }else if(srcHeight > 350 && srcWidth <= 350){
            	scale = (double)srcHeight/(double)350;
            }else{
            	scale = 1;
            }
            x = (int)(x*scale);
            y = (int)(y*scale);
            destWidth = (int)(destWidth*scale);
            destHeight = (int)(destHeight*scale);
            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
                // 改进的想法:是否可用多线程加快切割速度
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight,BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                g.dispose();
                // 截取原照片后缀
    	        String string = srcImageFile.substring(srcImageFile.lastIndexOf(".") + 1);
                // 输出为文件   覆盖原文件
                ImageIO.write(tag, string, new File(srcImageFile));
                return "true";
            }
        } catch (Exception e) {
        	BugLogUtils.saveBugLog(request, "截图上传--截图出现异常", e);
			logger.error("截图上传--截图错误信息:"+e.getMessage());
            return "false";
        }
        return "false";
    }
	 // 上传本地照片到服务器
	public static JSONObject postImg(String newURL,HttpServletRequest request,org.slf4j.Logger logger) {
        HttpClient client = new HttpClient();
        // 返回结果集  
        JSONObject resJson = new JSONObject();
        try {
            PostMethod postMethod = new PostMethod(ParametersFactory.getMtmyParamValues("uploader_url"));
            // FilePart：用来上传文件的类  
            FilePart filePart = new FilePart("img", new File(newURL));
            Part[] parts = {
                filePart
            };
            // 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装  
            MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(mre);
            // 执行请求，返回状态码  
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                System.out.println("上传到服务器请求成功，返回信息：" + postMethod.getResponseBodyAsString());
                resJson.put("status", "200");
                resJson.put("msg",postMethod.getResponseBodyAsString());
                return resJson;
            } else {
            	System.out.println("请求上传图片，请求失败。");
                resJson.put("status", "-1");
                resJson.put("msg", "上传图片，请求失败。");
                return resJson;
            }
        } catch(Exception e) {
            resJson.put("status", "-1");
            resJson.put("msg", "系统异常");
            System.out.println("请求上传图片，请求失败。"+e.toString());
            return resJson;
        }
    }
	/**
	 * 添加作者
	 * @param articleAuthorPhoto
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addAuthor")
	public Map<String, Object> addAuthor(ArticleAuthorPhoto articleAuthorPhoto,HttpServletRequest request){
		Map<String, Object> map = Maps.newHashMap();
		try {
			articleRepositoryService.addAuthor(articleAuthorPhoto);
			map.put("status", "200");
			map.put("msg", articleAuthorPhoto.getAuthorId());
		} catch (Exception e) {
			map.put("status", "-1");
			BugLogUtils.saveBugLog(request, "添加作者--异步添加作者出现异常", e);
			logger.error("添加作者--异步添加作者异常信息:"+e.getMessage());
		}
		return map;
	}
	/**
	 * 删除作者
	 * @param articleAuthorPhoto
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delAuthor")
	public Map<String, Object> delAuthor(ArticleAuthorPhoto articleAuthorPhoto,HttpServletRequest request){
		Map<String, Object> map = Maps.newHashMap();
		try {
			articleRepositoryService.delAuthor(articleAuthorPhoto);
			map.put("status", "200");
		} catch (Exception e) {
			map.put("status", "-1");
			BugLogUtils.saveBugLog(request, "删除作者--异步删除作者出现异常", e);
			logger.error("删除作者--异步删除作者异常信息:"+e.getMessage());
		}
		return map;
	}
}