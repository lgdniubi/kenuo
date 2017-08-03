package com.training.modules.ec.web;


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
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCard;
import com.training.modules.ec.service.GoodsCardService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 卡项-Controller层
 * @author 土豆
 * @version 2017-7-26
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsCard")
public class GoodsCardController extends BaseController{

	@Autowired
	private GoodsCardService goodsCardService;
	
	/**
	 * 分页查询商品属性
	 * @param goodsCard
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String list(GoodsCard goodsCard,Model model, HttpServletRequest request, HttpServletResponse response){
		
		Page<GoodsCard> page = goodsCardService.findPage(new Page<GoodsCard>(request, response), goodsCard);
		model.addAttribute("page", page);
		
		return "modules/ec/goodsList";
	}
	
	/**
	 * 添加价格
	 * @return
	 */
	@RequestMapping(value = {"fromPrice"})
	public String fromPrice(GoodsCard goodsCard,Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		model.addAttribute("goodsCard", goodsCard);
		return "modules/ec/goodsCardFromPrice";
	}
	/**
	 * 添加套卡信息
	 * @param goods
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"save"})
	public String save(Goods goods, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			goodsCardService.saveGoods(goods,request);
			addMessage(redirectAttributes, "保存/修改 卡项'" + goods.getGoodsName() + "'成功");
		} catch (Exception e) {
			logger.error("添加 卡项 出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "添加 卡项 出现异常", e);
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	
	
	/**
	 * 删除卡项中的商品
	 * @param goodsattribute
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"delete"})
	@ResponseBody
	public String delete(GoodsCard goodsCard, HttpServletRequest request){
		//删除
		String goodsCardId = request.getParameter("goodsCardId");
		logger.info("#####删除卡项中商品Id："+goodsCardId);
		if(null != goodsCardId && !"".equals(goodsCardId)){
			goodsCard.setGoodsCardId(Integer.parseInt(goodsCardId));
			goodsCardService.delete(goodsCard);
			return "success";
		}else{
			return "error";
		}
	}
	
}
