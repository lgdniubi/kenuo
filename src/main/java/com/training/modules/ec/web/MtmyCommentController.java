package com.training.modules.ec.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.DateUtils;
import com.training.common.utils.excel.ExportExcel;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Comment;
import com.training.modules.ec.entity.MtmyComment;
import com.training.modules.ec.service.CommentService;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 评论 Controller
 * @author coffee
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/mtmycomment")
public class MtmyCommentController extends BaseController{
	
	@Autowired
	private CommentService commentService;
	
	/**
	 * 商品评论列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "realComment")
	public String realComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<Comment> page=commentService.findPage(new Page<Comment>(request, response), comment);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/ec/realComment";
	}
	/**
	 * 查看单个商品评论
	 * @param request
	 * @param response
	 * @param model
	 * @param comment
	 * @return
	 */
	@RequestMapping(value ="oneRealComment")
	public @ResponseBody Map<String,List<Comment>> OneRealComment(HttpServletRequest request, HttpServletResponse response,Model model,Comment comment){
		Map<String,List<Comment>> jsonMap=new HashMap<String, List<Comment>>();
		List<Comment> nowcomment=commentService.findRealByCid(comment);
		for (int i = 0; i < nowcomment.size(); i++) {
			if(nowcomment.get(i).getParentId() == 0){
				nowcomment.get(i).setNewTime(DateUtils.formatDateTime(nowcomment.get(i).getAddTime()));
			}
		}
	//	System.out.println(nowcomment.size()+"=======================");
		jsonMap.put("nowcomment",nowcomment);
		return jsonMap;
	}
	/**
	 * 回复商品评论
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="replyRealComment")
	public String replyRealComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		User currentUser = UserUtils.getUser();
		comment.setReplyId(currentUser.getId());
		commentService.insterRealComment(comment);
		//修改单个用户所涉及的商品评论
		commentService.updateRealComment(comment);
		commentService.updateGoodsIsReplay(comment);
		addMessage(redirectAttributes, "回复用户评论成功");
		return "redirect:" + adminPath + "/ec/mtmycomment/realComment";
	}
	/**
	 * 美容师评论列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "beautyComment")
	public String beautyComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<Comment> page=commentService.findBeautyPage(new Page<Comment>(request, response), comment);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/ec/beautyComment";
	}
	/**
	 * 查看单个美容师评论
	 * @param request
	 * @param response
	 * @param model
	 * @param comment
	 * @return
	 */
	@RequestMapping(value ="oneBeautyComment")
	public @ResponseBody Map<String,List<Comment>> oneBeautyComment(HttpServletRequest request, HttpServletResponse response,Model model,Comment comment){
		Map<String,List<Comment>> jsonMap=new HashMap<String, List<Comment>>();
		List<Comment> nowcomment=commentService.findBeautyByCid(comment);
		for (int i = 0; i < nowcomment.size(); i++) {
			if(nowcomment.get(i).getParentId() == 0){
				nowcomment.get(i).setNewTime(DateUtils.formatDateTime(nowcomment.get(i).getAddTime()));
			}
		}
//		System.out.println(nowcomment.size()+"=======================");
		jsonMap.put("nowcomment",nowcomment);
		return jsonMap;
	}
	/**
	 * 回复美容师评论
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="replybeautyComment")
	public String replybeautyComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		User currentUser = UserUtils.getUser();
		comment.setReplyId(currentUser.getId());
		commentService.insterbeautyComment(comment);
		//修改单个用户所涉及的商品评论
		commentService.updateBeautyComment(comment);
		commentService.updateBeautyIsReplay(comment);
		addMessage(redirectAttributes, "回复用户评论成功");
		return "redirect:" + adminPath + "/ec/mtmycomment/beautyComment";
	}
	
	/**
	 * 导出商品评论
	 * 
	 * @param mtmyComment
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(MtmyComment mtmyComment, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = "商品评论数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<MtmyComment> page = commentService.exportGoodsComment(new Page<MtmyComment>(request, response, -1), mtmyComment);
			new ExportExcel("商品评论数据", MtmyComment.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出商品评论数据！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/mtmycomment/realComment?repage";
	}
	
	/**
	 * 商品评论列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shopComment")
	public String shopComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<Comment> page=commentService.findShopPage(new Page<Comment>(request, response), comment);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/ec/shopComment";
	}
	/**
	 * 查看单个店铺评论
	 * @param request
	 * @param response
	 * @param model
	 * @param comment
	 * @return
	 */
	@RequestMapping(value ="oneShopComment")
	public @ResponseBody Map<String,List<Comment>> oneShopComment(HttpServletRequest request, HttpServletResponse response,Model model,Comment comment){
		Map<String,List<Comment>> jsonMap=new HashMap<String, List<Comment>>();
		List<Comment> nowcomment=commentService.findShopByCid(comment);
		for (int i = 0; i < nowcomment.size(); i++) {
			if(nowcomment.get(i).getParentId() == 0){
				nowcomment.get(i).setNewTime(DateUtils.formatDateTime(nowcomment.get(i).getAddTime()));
			}
		}
		jsonMap.put("nowcomment",nowcomment);
		return jsonMap;
	}
	/**
	 * 回复店铺评论
	 * @param comment
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="replyShopComment")
	public String replyShopComment(Comment comment,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		User currentUser = UserUtils.getUser();
		comment.setReplyId(currentUser.getId());
		commentService.insterShopComment(comment);
		//修改单个用户所涉及的商品评论
		commentService.updateShopComment(comment);
		commentService.updateShopIsReplay(comment);
		addMessage(redirectAttributes, "回复用户评论成功");
		return "redirect:" + adminPath + "/ec/mtmycomment/realComment";
	}
}
