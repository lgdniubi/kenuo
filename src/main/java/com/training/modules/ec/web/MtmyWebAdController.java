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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.MtmyWebAdDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.MtmyWebAd;
import com.training.modules.ec.service.MtmyWebAdService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 首页广告图Controller
 * @author xiaoye  2017年5月8日
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/webAd")
public class MtmyWebAdController extends BaseController{
	
	@Autowired
	private MtmyWebAdService mtmyWebAdService;
	
	@Autowired
	private MtmyWebAdDao mtmyWebAdDao;
	
	/**
	 * 首页广告图列表
	 * @param mtmyWebAd
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(MtmyWebAd mtmyWebAd,HttpServletRequest request,HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		try{
			Page<MtmyWebAd> page = mtmyWebAdService.findPage(new Page<MtmyWebAd>(request, response), mtmyWebAd);
			model.addAttribute("page",page);
			model.addAttribute("mtmyWebAd",mtmyWebAd);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看广告图分类列表失败!", e);
			logger.error("查看广告图分类列表失败：" + e.getMessage());
			
		}
		return "modules/ec/mtmyWebAdList";
	}
	
	/**
	 * 跳转增加首页广告图页面
	 * @param mtmyWebAd
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(MtmyWebAd mtmyWebAd,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(mtmyWebAd.getMtmyWebAdId() != 0){
				mtmyWebAd = mtmyWebAdService.getMtmyWebAd(mtmyWebAd.getMtmyWebAdId());
			}
			model.addAttribute("mtmyWebAd", mtmyWebAd);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转增加首页广告图页面", e);
			logger.error("跳转增加首页广告图页面出错信息：" + e.getMessage());
		}
		return "modules/ec/mtmyWebAdForm";
	}
	
	/**
	 * 修改首页广告图的状态
	 * @param request
	 * @param mtmyWebAd
	 * @return
	 */
	@RequestMapping(value="updateIsShow")
	@ResponseBody
	public Map<String, String> updateIsShow(HttpServletRequest request,MtmyWebAd mtmyWebAd) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String isShow = request.getParameter("isShow");
			mtmyWebAd = mtmyWebAdService.getMtmyWebAd(mtmyWebAd.getMtmyWebAdId());
			if("0".equals(isShow)){
				mtmyWebAd.setIsShow(isShow);
				mtmyWebAdService.updateIsShow(mtmyWebAd);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}else if("1".equals(isShow)){
				mtmyWebAd.setIsShow(isShow);
				mtmyWebAdService.updateIsShow(mtmyWebAd);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISSHOW", isShow);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "修改首页广告图的状态失败", e);
			logger.error("修改首页广告图的状态失败：" + e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
	/**
	 * 添加首页广告图
	 * @param mtmyWebAd
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(MtmyWebAd mtmyWebAd,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			if(mtmyWebAd.getMtmyWebAdId() == 0){
				mtmyWebAdService.insertMtmyWebAd(mtmyWebAd);
				addMessage(redirectAttributes, "添加成功！");
			}else{
				mtmyWebAdService.updateMtmyWebAd(mtmyWebAd);
				addMessage(redirectAttributes, "修改成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存首页广告图", e);
			logger.error("方法：save，保存首页广告图出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存首页广告图失败");
		}
		return "redirect:" + adminPath + "/ec/webAd/list?categoryId="+mtmyWebAd.getCategoryId();
	}
	
	/**
	 * 逻辑删除首页广告图
	 * @param mtmyWebAd
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String delete(MtmyWebAd mtmyWebAd,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			mtmyWebAdService.delMtmyWebAd(mtmyWebAd.getMtmyWebAdId());
			addMessage(redirectAttributes, "删除成功");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除首页广告图", e);
			logger.error("方法：delete，删除首页广告图出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除首页广告图失败");
		}
		return "redirect:" + adminPath + "/ec/webAd/list?categoryId="+mtmyWebAd.getCategoryId();
	}
	
	/**
	 * 首页广告图对应商品列表
	 * @param mtmyWebAd
	 * @param model
	 * @return
	 */
	@RequestMapping(value="mtmyWebAdGoodsList")
	public String mtmyWebAdGoodsList(MtmyWebAd mtmyWebAd,Goods goods,Model model,HttpServletRequest request,HttpServletResponse response){
		try{
			goods.setAdId(mtmyWebAd.getMtmyWebAdId());
			Page<Goods> page = mtmyWebAdService.findGoodsPage(new Page<Goods>(request, response),goods);
			mtmyWebAd = mtmyWebAdService.getMtmyWebAd(mtmyWebAd.getMtmyWebAdId());
			model.addAttribute("page", page);
			model.addAttribute("mtmyWebAd", mtmyWebAd);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "首页广告图对应商品列表", e);
			logger.error("首页广告图对应商品列表出错信息：" + e.getMessage());
		}
		return "modules/ec/mtmyWebAdGoodsList";
	}
	
	/**
	 * 添加修改首页广告图对应的商品
	 * @param goods
	 * @param request
	 * @param model
	 * @param flag
	 * @return
	 */
	@RequestMapping(value="mtmyWebAdGoodsForm")
	public String mtmyWebAdGoodsForm(Goods goods,HttpServletRequest request,Model model,String flag){
		try{
			goods.setAdId(Integer.valueOf(request.getParameter("adId")));
			List<Goods> goodsList = mtmyWebAdDao.findGoodsList(goods);
			model.addAttribute("goods", goods);
			model.addAttribute("goodsList",goodsList);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转增加首页广告图对应的商品页面", e);
			logger.error("跳转增加首页广告图页面对应的商品出错信息：" + e.getMessage());
		}
		return "modules/ec/mtmyWebAdGoodsForm";
	}
	
	/**
	 * 保存首页广告图对应的商品 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="saveMtmyWebAdGoods")
	@ResponseBody
	public String saveMtmyWebAdGoods(HttpServletRequest request,RedirectAttributes redirectAttributes){
		try{
			String goodsIds = request.getParameter("goodsIds");
			int adId = Integer.valueOf(request.getParameter("adId"));
			mtmyWebAdService.saveMtmyWebAdGoods(adId, goodsIds);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
	
	/**
	 * 删除首页广告图对应的商品 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delGoods")
	@ResponseBody
	public String delGoods(HttpServletRequest request){
		try{
			int goodsId = Integer.valueOf(request.getParameter("goodsId"));
			int adId = Integer.valueOf(request.getParameter("adId"));
			mtmyWebAdService.delGoods(adId, goodsId);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}
}
