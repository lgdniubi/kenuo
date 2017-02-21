package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.SubjectDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.Subject;
import com.training.modules.ec.service.SubjectService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 主题图管理
 * @author 小叶 2017年2月17日
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/subject")
public class SubjectController extends BaseController{
	
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectDao subjectDao;
	
	/**
	 * 主题图列表
	 * @param subject
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(Subject subject,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<Subject> page = subjectService.findPage(new Page<Subject>(request, response), subject);
			model.addAttribute("page", page);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "主题图列表", e);
			logger.error("主题图列表出错信息：" + e.getMessage());
		}
		return "modules/ec/subjectList";
	}
	
	/**
	 * 增加主题图
	 * @param subject
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(Subject subject,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(subject.getSubId() != 0){
				subject = subjectService.getSubject(subject.getSubId());
				model.addAttribute("subject", subject);
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转增加主题图页面", e);
			logger.error("跳转增加主题图页面出错信息：" + e.getMessage());
		}
		return "modules/ec/subjectForm";
	}
	
	/**
	 * 添加主题图
	 * @param subject
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(Subject subject,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			if(subject.getSubId() == 0){
				subjectService.insertSubject(subject);
				addMessage(redirectAttributes, "添加成功！");
			}else{
				subjectService.updateSubject(subject);
				addMessage(redirectAttributes, "修改成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存主题图", e);
			logger.error("方法：save，保存主题图出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存主题图失败");
		}
		return "redirect:" + adminPath + "/ec/subject/list";
	}
	
	/**
	 * 逻辑删除主题图
	 * @param subject
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String delete(Subject subject,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			subjectService.delSubject(subject.getSubId());
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除主题图", e);
			logger.error("方法：delete，删除主题图出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除主题图失败");
		}
		return "redirect:" + adminPath + "/ec/subject/list";
	}
	
	/**
	 * 主题图对应商品列表
	 * @param subject
	 * @param model
	 * @return
	 */
	@RequestMapping(value="subjectGoodsList")
	public String subjectGoodsList(Subject subject,Goods goods,Model model,HttpServletRequest request,HttpServletResponse response){
		try{
			goods.setSubId(subject.getSubId());
			Page<Goods> page = subjectService.findGoodsPage(new Page<Goods>(request, response),goods);
			subject = subjectService.getSubject(subject.getSubId());
			model.addAttribute("page", page);
			model.addAttribute("subject", subject);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "主题图对应商品列表", e);
			logger.error("主题图对应商品列表出错信息：" + e.getMessage());
		}
		return "modules/ec/subjectGoodsList";
	}
	
	/**
	 * 添加修改主题图对应的商品
	 * @param goods
	 * @param request
	 * @param model
	 * @param flag
	 * @return
	 */
	@RequestMapping(value="subjectGoodsForm")
	public String subjectGoodsForm(Goods goods,HttpServletRequest request,Model model,String flag){
		try{
			goods.setSubId(Integer.valueOf(request.getParameter("subId")));
			List<Goods> goodsList = subjectDao.findGoodsList(goods);
			model.addAttribute("goods", goods);
			model.addAttribute("goodsList",goodsList);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转增加主题图对应的商品页面", e);
			logger.error("跳转增加主题图页面对应的商品出错信息：" + e.getMessage());
		}
		return "modules/ec/subjectGoodsForm";
	}
	
	/**
	 * 保存主题图对应的商品 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="saveSubjectGoods")
	@ResponseBody
	public String saveSubjectGoods(HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			String goodsIds = request.getParameter("goodsIds");
			int subId = Integer.valueOf(request.getParameter("subId"));
			subjectService.saveSubjectGoods(subId, goodsIds);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
	
	/**
	 * 删除主题图对应的商品 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delGoods")
	@ResponseBody
	public String delGoods(HttpServletRequest request){
		try{
			int goodsId = Integer.valueOf(request.getParameter("goodsId"));
			int subId = Integer.valueOf(request.getParameter("subId"));
			subjectService.delGoods(subId, goodsId);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
}
