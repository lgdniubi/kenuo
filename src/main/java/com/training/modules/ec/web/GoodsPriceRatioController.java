package com.training.modules.ec.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsPriceRatio;
import com.training.modules.ec.service.GoodsPriceRatioService;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 城市异价controller  
 * @author xiaoye   2017年12月27日
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsPriceRatio")
public class GoodsPriceRatioController extends BaseController{
	
	@Autowired
	private GoodsPriceRatioService goodsPriceRatioService;
	@Autowired
	private GoodsDao goodsDao;
	
	/**
	 * 城市异价比例列表
	 * @param goodsPriceRatio
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(!"".equals(goodsPriceRatio.getNewRatio()) && goodsPriceRatio.getNewRatio() != null){
				String[] result = goodsPriceRatio.getNewRatio().split("-");
				goodsPriceRatio.setMinRatio(Double.valueOf(result[0]));
				goodsPriceRatio.setMaxRatio(Double.valueOf(result[1]));
			}
			Page<GoodsPriceRatio> page=goodsPriceRatioService.findPage(new Page<GoodsPriceRatio>(request, response), goodsPriceRatio);
			model.addAttribute("page", page);
			model.addAttribute("goodsPriceRatio", goodsPriceRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "城市异价比例列表", e);
			logger.error("城市异价比例列表出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsPriceRatioList";
	}
	
	/**
	 * 查看/修改城市异价页面
	 * @param goodsPriceRatio
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(goodsPriceRatio.getGoodsPriceRatioId() != 0){
				goodsPriceRatio = goodsPriceRatioService.queryCityAndGoods(goodsPriceRatio.getGoodsPriceRatioId());
			}
			model.addAttribute("goodsPriceRatio", goodsPriceRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "查看/修改城市异价页面", e);
			logger.error("查看/修改城市异价页面出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsPriceRatioForm";
	}
	
	/**
	 * 跳转城市异价添加商品页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="addGoodsForm")
	public String addGoodsForm(HttpServletRequest request,Model model){
		try{
			List<Goods> goodsList = new ArrayList<Goods>();
			String goodsIds = request.getParameter("goodsIds");
			if(!"".equals(goodsIds) && goodsIds != null){
				goodsList = goodsDao.selectGoodsList(goodsIds);
			}
			model.addAttribute("goodsList",goodsList);
			model.addAttribute("cityIds",request.getParameter("cityIds"));
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转城市异价添加商品页面", e);
			logger.error("跳转城市异价添加商品页面出错信息：" + e.getMessage());
		}
		return "modules/ec/addGoodsForm";
	}
	
	/**
	 * 保存城市异价
	 * @param goodsPriceRatio
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="save")
	public String save(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			List<Integer> list = new ArrayList<Integer>();
			if(!"".equals(goodsPriceRatio.getGoodsIds()) && goodsPriceRatio.getGoodsIds() != null){
				String[] goodsIds = goodsPriceRatio.getGoodsIds().split(",");
				for(String goodsId:goodsIds){
					list.add(Integer.valueOf(goodsId));
				}
			}
			goodsPriceRatio.setGoodsIdsList(list);
			
			if(goodsPriceRatio.getGoodsPriceRatioId() == 0){
				goodsPriceRatio.setCreateBy(UserUtils.getUser());
				goodsPriceRatioService.insertRatio(goodsPriceRatio);
				
				goodsPriceRatioService.insertCityAndGoods(goodsPriceRatio);
				addMessage(redirectAttributes, "添加成功！");
			}else{
				goodsPriceRatio.setUpdateBy(UserUtils.getUser());
				goodsPriceRatioService.updateRatio(goodsPriceRatio);
				
				goodsPriceRatioService.deleteAll(goodsPriceRatio.getGoodsPriceRatioId());
				goodsPriceRatioService.insertCityAndGoods(goodsPriceRatio);
				addMessage(redirectAttributes, "修改成功！");
			}
			
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存城市异价", e);
			logger.error("保存城市异价出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存城市异价失败");
		}
		return "redirect:" + adminPath + "/ec/goodsPriceRatio/list";
	}
	
	/**
	 * 物理删除城市异价
	 * @param goodsPriceRatio
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="del")
	public String del(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if(goodsPriceRatio.getGoodsPriceRatioId() != 0){
				goodsPriceRatioService.deleteRatio(goodsPriceRatio.getGoodsPriceRatioId());
				goodsPriceRatioService.deleteAll(goodsPriceRatio.getGoodsPriceRatioId());
				addMessage(redirectAttributes, "删除成功");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除城市异价", e);
			logger.error("删除城市异价出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "删除城市异价失败");
		}
		return "redirect:" + adminPath + "/ec/goodsPriceRatio/list";
	}
	
	/**
	 * 查看城市异价对应的城市
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewCity")
	public String viewCity(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request,Model model){
		try{
			if(goodsPriceRatio.getGoodsPriceRatioId() != 0){
				goodsPriceRatio = goodsPriceRatioService.queryCityAndGoods(goodsPriceRatio.getGoodsPriceRatioId());
			}
			model.addAttribute("goodsPriceRatio",goodsPriceRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转查看城市异价对应的城市页面", e);
			logger.error("跳转查看城市异价对应的城市页面出错信息：" + e.getMessage());
		}
		return "modules/ec/viewCityForm";
	}
	
	/**
	 * 查看城市异价对应的商品
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="viewGoods")
	public String viewGoods(GoodsPriceRatio goodsPriceRatio,HttpServletRequest request,Model model){
		try{
			if(goodsPriceRatio.getGoodsPriceRatioId() != 0){
				goodsPriceRatio = goodsPriceRatioService.queryCityAndGoods(goodsPriceRatio.getGoodsPriceRatioId());
			}
			model.addAttribute("goodsPriceRatio",goodsPriceRatio);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转查看城市异价对应的商品页面", e);
			logger.error("跳转查看城市异价对应的商品页面出错信息：" + e.getMessage());
		}
		return "modules/ec/viewGoodsForm";
	}
}
