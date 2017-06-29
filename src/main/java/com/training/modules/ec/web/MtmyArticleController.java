package com.training.modules.ec.web;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.CommentDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyArticle;
import com.training.modules.ec.entity.MtmyArticleCategory;
import com.training.modules.ec.entity.MtmyArticleComment;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.ec.service.MtmyArticleService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;


/**
 * mtmy文章管理
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmyArticleList")
public class MtmyArticleController extends BaseController{
	
	@Autowired
	private MtmyArticleService mtmyArticleService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CommentDao commentDao;
	
	
	/**
	 * 文章列表
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(MtmyArticle mtmyArticle,MtmyArticleCategory mtmyArticleCategory,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if(0 == mtmyArticle.getFlag()){
				mtmyArticle.setAuditFlag(0);
			}else if(1 == mtmyArticle.getFlag()){
				mtmyArticle.setAuditFlag(1);
			}else if(2 == mtmyArticle.getFlag()){
				mtmyArticle.setDelFlag("1");
			}
			Page<MtmyArticle> page = mtmyArticleService.findPage(new Page<MtmyArticle>(request, response), mtmyArticle);
			model.addAttribute("page", page);
			//查询文章所有分类
			List<MtmyArticleCategory> categoryList= mtmyArticleService.findCategory(mtmyArticleCategory);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("mtmyArticle", mtmyArticle);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询文章列表", e);
			logger.error("查询文章列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查询文章列表出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyArticleList";
	}
	/**
	 * 添加文章跳转
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:sendMtmyArticle")
	@RequestMapping(value = "sendMtmyArticle")
	public String sendMtmyArticle(Model model){
		//查询文章所有分类
		List<MtmyArticleCategory> categoryList= mtmyArticleService.findCategory(new MtmyArticleCategory());
		model.addAttribute("categoryList", categoryList);
		return "modules/ec/mtmySendMtmyArticle";
	}
	/**
	 * 通过商品id加载商品详细  生成商品卡片
	 * @param goods
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loadGoods")
	public Goods loadGoods(Goods goods,HttpServletRequest request){
		try {
			goods = goodsService.get(goods);
			// 查询商品星级、总评论数
			Map<String, Object> map = commentDao.findCommentByGoodId(goods.getGoodsId());
			double num = Double.valueOf(map.get("rank").toString()) * 2;
			if(num > (int)num){
				num = (int)num + 1;
			}else{
				num = (int)num;
			}
			// 若无等级  默认为五颗星
			if(num == 0){
				num = 10;
			}
			NumberFormat nFromat = NumberFormat.getPercentInstance();
			String rank = nFromat.format(num/10);
			goods.setRank(rank);
			goods.setCommentNum(Integer.valueOf(map.get("commentNum").toString()));
			goods.setBuyCount(Integer.valueOf(map.get("buyCount").toString()));
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "生成商品卡片", e);
			logger.error("生成商品卡片错误信息:"+e.getMessage());
		}
		return goods;
	}
	/**
	 * 保存或修改文章
	 * @param cont
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(MtmyArticle mtmyArticle,HttpServletRequest request, HttpServletResponse response, Model model,RedirectAttributes redirectAttributes){
		try {
			User currentUser = UserUtils.getUser();
			mtmyArticle.setContents(HtmlUtils.htmlUnescape(mtmyArticle.getContents()));
			if("".equals(mtmyArticle.getId())){
				//新增文章
				mtmyArticle.setCreateBy(currentUser);
				mtmyArticleService.saveArticle(mtmyArticle);
				addMessage(redirectAttributes, "保存文章 "+mtmyArticle.getTitle()+" 成功！");
			}else{
				mtmyArticle.setUpdateBy(currentUser);
				mtmyArticleService.updateArticle(mtmyArticle);
				addMessage(redirectAttributes, "修改文章 "+mtmyArticle.getTitle()+" 成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存或修改文章", e);
			logger.error("保存或修改文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/list?flag=1";
	}
	/**
	 * 编辑查询文章
	 * @param mtmyArticle
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:updateArticle")
	@RequestMapping(value = "updateArticle")
	public String updateArticle(MtmyArticle mtmyArticle,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try {
			mtmyArticle = mtmyArticleService.getArticle(mtmyArticle.getId());
			mtmyArticle.setContents(HtmlUtils.htmlEscape(mtmyArticle.getContents()));
			model.addAttribute("mtmyArticle", mtmyArticle);
			//查询文章所有分类
			List<MtmyArticleCategory> categoryList= mtmyArticleService.findCategory(new MtmyArticleCategory());
			model.addAttribute("categoryList", categoryList);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "编辑文章--查询文章", e);
			logger.error("编辑文章--查询文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查询文章出现异常，请与管理员联系");
		}
		return "modules/ec/mtmySendMtmyArticle";
	}
	/**
	 * 查看文章
	 * @param mtmyArticle
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findArticle")
	public String findArticle(MtmyArticle mtmyArticle,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try {
			//调接口
			addMessage(redirectAttributes, "等待调接口");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询文章", e);
			logger.error("查询文章错误信息:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/list";
	}
	/**
	 * 审核文章
	 * @param mtmyArticle
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:auditArticle")
	@RequestMapping(value = "auditArticle")
	public String auditArticle(MtmyArticle mtmyArticle,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try {
			User currentUser = UserUtils.getUser();
			mtmyArticle.setUpdateBy(currentUser);
			mtmyArticleService.auditArticle(mtmyArticle);
			addMessage(redirectAttributes, "文章审核通过！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "审核文章", e);
			logger.error("审核文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "审核文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/list?flag=0";
	}
	/**
	 * 查询文章评论
	 * @param mtmyArticle
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "findArticleComment")
	public String findArticleComment(MtmyArticleComment mtmyArticleComment,HttpServletRequest request,Model model,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			model.addAttribute("articlesId", mtmyArticleComment.getArticlesId());
			Page<MtmyArticleComment> page = mtmyArticleService.findArticleComment(new Page<MtmyArticleComment>(request,response), mtmyArticleComment);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询文章评论", e);
			logger.error("查询文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查询文章评论出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyArticleComment";
	}
	/**
	 * 删除文章评论
	 * @param mtmyArticleComment
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:deleteComment")
	@RequestMapping(value = "deleteComment")
	public String deleteComment(MtmyArticleComment mtmyArticleComment,HttpServletRequest request,Model model,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			mtmyArticleService.deleteComment(mtmyArticleComment);
			addMessage(redirectAttributes, "删除评论成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章评论", e);
			logger.error("删除文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章评论出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/findArticleComment?articlesId="+mtmyArticleComment.getArticlesId(); 
	} 
	/**
	 * 删除文章
	 * @param mtmyArticle
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:deleteArticle")
	@RequestMapping(value = "deleteArticle")
	public String deleteArticle(MtmyArticle mtmyArticle,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			mtmyArticleService.deleteArticle(mtmyArticle);
			addMessage(redirectAttributes, "删除文章成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章", e);
			logger.error("删除文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/list?flag=2"; 
	}
	/**
	 * 文章分类
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:categoryList")
	@RequestMapping(value = "categoryList")
	public String categoryList(MtmyArticleCategory mtmyArticleCategory,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<MtmyArticleCategory> page = mtmyArticleService.findCategoryPage(new Page<MtmyArticleCategory>(request,response), mtmyArticleCategory);
		model.addAttribute("page", page);
		return "modules/ec/mtmyArticleCategoryList";
	}
	/**
	 * 保存或修改文章分类
	 * @param mtmyArticleCategory
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:saveCategory")
	@RequestMapping(value = "saveCategory")
	public String saveCategory(MtmyArticleCategory mtmyArticleCategory,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try {
			User currentUser = UserUtils.getUser();
			if("".equals(mtmyArticleCategory.getId())){
				//新增
				mtmyArticleCategory.setCreateBy(currentUser);
				mtmyArticleService.saveCategory(mtmyArticleCategory);
				addMessage(redirectAttributes, "保存文章分类 "+mtmyArticleCategory.getName()+" 成功！");
			}else{
				//修改
				mtmyArticleCategory.setUpdateBy(currentUser);
				mtmyArticleService.updateCatrgory(mtmyArticleCategory);
				addMessage(redirectAttributes, "修改文章分类 "+mtmyArticleCategory.getName()+" 成功！");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存或修改文章分类", e);
			logger.error("保存或修改文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存/修改文章分类出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/categoryList";
	}
	/**
	 * 删除文章分类
	 * @param mtmyArticleCategory
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:deleteCategory")
	@RequestMapping(value = "deleteCategory")
	public String deleteCategory(MtmyArticleCategory mtmyArticleCategory,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try {
			mtmyArticleService.deleteCategory(mtmyArticleCategory);
			addMessage(redirectAttributes, "删除文章分类成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章分类", e);
			logger.error("删除文章分类错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章分类出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/categoryList";
	}
	
	@RequestMapping(value = "applyList")
	public String applyList(MtmyArticle mtmyArticle,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			Page<MtmyArticle> page = mtmyArticleService.findApplyPage(new Page<MtmyArticle>(request, response), mtmyArticle);
			model.addAttribute("page", page);
			model.addAttribute("mtmyArticle", mtmyArticle);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询报名信息", e);
			logger.error("查询报名信息错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查询报名信息出现异常，请与管理员联系");
		}
		return "modules/ec/experienceApply";
	}
	
	/**
	 * 编辑文章     小叶    2016.12.22
	 * @param mtmyArticle
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:editArticle")
	@RequestMapping(value="editArticle")
	public String editArticle(MtmyArticle mtmyArticle,HttpServletRequest request,RedirectAttributes redirectAttributes,Model model){
		int flag = mtmyArticle.getFlag();
		try{
			String key = request.getParameter("key");
			model.addAttribute("key", key);
			
			mtmyArticle = mtmyArticleService.getArticle(mtmyArticle.getId());
			mtmyArticle.setFlag(flag);
			model.addAttribute("mtmyArticle",mtmyArticle);
			//查询文章所有分类
			List<MtmyArticleCategory> categoryList= mtmyArticleService.findCategory(new MtmyArticleCategory());
			model.addAttribute("categoryList", categoryList);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "编辑文章", e);
			logger.error("编辑文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "编辑文章出现异常，请与管理员联系");
		}
		return "modules/ec/editArticle";
	}
	
	/**
	 * 更新文章的部分：排序，分类，是否置顶，是否推荐，是否显示   小叶   2016.12.22
	 * @param mtmyArticle
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="saveEdit")
	public String saveEdit(MtmyArticle mtmyArticle,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			String key = request.getParameter("key");
			if(key.equals("YEStask")){
				mtmyArticleService.updateIsTask(mtmyArticle);
				if(mtmyArticle.getIsTask() == 0){
					
					addMessage(redirectAttributes, "取消定时发布成功");
				}else{
					addMessage(redirectAttributes, "定时发布成功");
				}
				mtmyArticle.setFlag(0);
			}else{
				mtmyArticleService.updateForPart(mtmyArticle);
				addMessage(redirectAttributes, "保存编辑文章成功");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存编辑文章", e);
			logger.error("保存编辑文章错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "保存编辑文章出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/list";
	}
	
	/**
	 * 更改三个是否的状态：是否置顶，是否推荐，是否显示
	 * @param mtmyArticle
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="change")
	@ResponseBody
	public Map<String, String> change(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try{
			String id = request.getParameter("id");
			String parameter = request.getParameter("parameter");
			int flag = Integer.valueOf(request.getParameter("flag"));
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(parameter) && !StringUtils.isEmpty(String.valueOf(flag))){
				MtmyArticle mtmyArticle = new MtmyArticle();
				mtmyArticle.setId(id);
				if("isTop".equals(parameter)){
					mtmyArticle.setIsTop(flag);
					mtmyArticleService.updateIsTop(mtmyArticle);
				}else if("isRecommend".equals(parameter)){
					mtmyArticle.setIsRecommend(flag);
					mtmyArticleService.updateIsRecommend(mtmyArticle);
				}else if("isShow".equals(parameter)){
					mtmyArticle.setIsShow(flag);
					mtmyArticleService.updateIsShow(mtmyArticle);
				}
			jsonMap.put("STATUS", "OK");
			jsonMap.put("FLAG", String.valueOf(flag));
		    }else{
		    	jsonMap.put("STATUS", "ERROR");
		    	jsonMap.put("MESSAGE", "修改失败,必要参数为空");	
		    }
		}catch(Exception e ){
			logger.error("修改出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}	
	
	/**
	 * 审核评论列表
	 * @return
	 */
	@RequestMapping(value = "mtmyArticleCommentList")
	public String MtmyArticleCommentList(MtmyArticleCategory mtmyArticleCategory,MtmyArticleComment mtmyArticleComment,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if(0 == mtmyArticleComment.getFlag()){
				
			}else if(2 == mtmyArticleComment.getFlag()){
				mtmyArticleComment.setDelFlag("1");
			}
			Page<MtmyArticleComment> page = mtmyArticleService.findAllArticleComment(new Page<MtmyArticleComment>(request,response),mtmyArticleComment);
			model.addAttribute("page", page);
			//查询文章所有分类
			List<MtmyArticleCategory> categoryList= mtmyArticleService.findCategory(mtmyArticleCategory);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("mtmyArticleComment",mtmyArticleComment);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询文章评论列表", e);
			logger.error("查询文章评论列表错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "查询文章列表评论出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyArticleCommentList";
	}
	
	/**
	 * 评论管理中的删除评论
	 * @param mtmyArticleComment
	 * @param request
	 * @param model
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ec:mtmyArticleList:deleteArticle")
	@RequestMapping(value="deleteManagerComment")
	public String deleteManagerComment(MtmyArticleComment mtmyArticleComment,HttpServletRequest request,Model model,HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			mtmyArticleService.deleteComment(mtmyArticleComment);
			addMessage(redirectAttributes, "删除评论成功！");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除文章评论", e);
			logger.error("删除文章评论错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "删除文章评论出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/mtmyArticleList/mtmyArticleCommentList"; 
	} 
	
	/**
	 * 修改文章是否为头条
	 * @param request
	 * @param mtmyArticle
	 * @return
	 */
	@RequestMapping(value="updateIsTopline")
	@ResponseBody
	public Map<String, String> updateIsTopline(HttpServletRequest request,MtmyArticle mtmyArticle) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isTopline = request.getParameter("isTopline");
			mtmyArticle = mtmyArticleService.getArticle(mtmyArticle.getId());
			if("0".equals(isTopline)){
				mtmyArticle.setIsTopline(isTopline);
				mtmyArticleService.updateIsTopline(mtmyArticle);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISTOPLINE", isTopline);
			}else if("1".equals(isTopline)){
				mtmyArticle.setIsTopline(isTopline);
				mtmyArticleService.updateIsTopline(mtmyArticle);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISTOPLINE", isTopline);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改文章是否为头条失败", e);
			logger.error("修改文章是否为头条失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
}
