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
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.dao.MtmyGoodsSubheadDao;
import com.training.modules.ec.entity.GoodsSubhead;
import com.training.modules.ec.entity.GoodsSubheadGoods;
import com.training.modules.ec.service.MtmyGoodsSubheadService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 商品副标题Controller
 * @author xiaoye
 *
 */
@Controller
@RequestMapping(value="${adminPath}/ec/goodsSubhead")
public class MtmyGoodsSubheadController extends BaseController{
	
	@Autowired
	private MtmyGoodsSubheadService mtmyGoodsSubheadService;
	@Autowired
	private MtmyGoodsSubheadDao mtmyGoodsSubheadDao;
	@Autowired
	private GoodsDao goodsDao;
	
	/**
	 *商品副标题列表 
	 * @param goodsSubhead
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="list")
	public String list(GoodsSubhead goodsSubhead,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			Page<GoodsSubhead> page = mtmyGoodsSubheadService.findPage(new Page<GoodsSubhead>(request, response), goodsSubhead);
			model.addAttribute("page", page);
			model.addAttribute("goodsSubhead", goodsSubhead);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "商品副标题列表", e);
			logger.error("商品副标题列表出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsSubheadList";
	}
	
	/**
	 * 编辑商品副标题
	 * @param goodsSubhead
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="form")
	public String form(GoodsSubhead goodsSubhead,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{
			if(goodsSubhead.getGoodsSubheadId() != 0){
				goodsSubhead = mtmyGoodsSubheadService.getGoodsSubhead(goodsSubhead.getGoodsSubheadId());
			}
			model.addAttribute("goodsSubhead", goodsSubhead);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转编辑商品副标题页面", e);
			logger.error("跳转编辑商品副标题出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsSubheadForm";
	}
	
	/**
	 * 保存商品副标题
	 * @param goodsSubhead
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(GoodsSubhead goodsSubhead,RedirectAttributes redirectAttributes,HttpServletRequest request){
		try{
			if(goodsSubhead.getGoodsSubheadId() == 0){
				mtmyGoodsSubheadService.insertGoodsSubhead(goodsSubhead);
				addMessage(redirectAttributes, "添加成功！");
			}else{
				mtmyGoodsSubheadService.updateGoodsSubhead(goodsSubhead);
				addMessage(redirectAttributes, "修改成功！");
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "保存商品副标题", e);
			logger.error("方法：save，保存商品副标题出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "保存商品副标题失败");
		}
		return "redirect:" + adminPath + "/ec/goodsSubhead/list";
	}
	
	/**
	 * 商品副标题活动的开启和关闭
	 * @param goodsSubhead
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="open")
	public String open(GoodsSubhead goodsSubhead,HttpServletRequest request,Model model,RedirectAttributes redirectAttributes){
		try{
			if("0".equals(goodsSubhead.getStatus())){
				mtmyGoodsSubheadService.changeGoodsSubheadStatus(goodsSubhead);
				addMessage(redirectAttributes, "开启成功！");
			}else if("1".equals(goodsSubhead.getStatus())){
				mtmyGoodsSubheadService.changeGoodsSubheadStatus(goodsSubhead);
				addMessage(redirectAttributes, "关闭成功！");
			}
			List<Integer> list = mtmyGoodsSubheadDao.selectGoodsId(goodsSubhead.getGoodsSubheadId());
			if(list.size() > 0){
				for(int goodsId:list){
					//这里清楚商品的缓存，记得添加，咖啡将合并代码的时候再添加，谨记
				}
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "商品副标题活动的开启和关闭", e);
			logger.error("商品副标题活动的开启和关闭出错信息：" + e.getMessage());
			addMessage(redirectAttributes, "开启或关闭失败");
		}
		return "redirect:" + adminPath + "/ec/goodsSubhead/list";
	}
	
	/**
	 *商品副标题活动对应的商品列表 
	 * @param goodsSubheadGoods
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="goodsSubheadGoodsList")
	public String goodsSubheadGoodsList(GoodsSubheadGoods goodsSubheadGoods,GoodsSubhead goodsSubhead,HttpServletRequest request, HttpServletResponse response,Model model) {
		try{	
			goodsSubheadGoods.setGoodsSubhead(goodsSubhead);
			Page<GoodsSubheadGoods> page = mtmyGoodsSubheadService.selectGoodsByGoodsSubheadId(new Page<GoodsSubheadGoods>(request, response), goodsSubheadGoods);
			goodsSubhead = mtmyGoodsSubheadService.getGoodsSubhead(goodsSubhead.getGoodsSubheadId());
			model.addAttribute("page", page);
			model.addAttribute("goodsSubheadGoods", goodsSubheadGoods);
			model.addAttribute("goodsSubhead", goodsSubhead);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "商品副标题活动对应的商品列表 ", e);
			logger.error("商品副标题活动对应的商品列表 出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsSubheadGoodsList";
	}
	
	/**
	 * 修改商品副标题对应的商品
	 * @param goodsSubhead
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="goodsSubheadGoodsForm")
	public String goodsSubheadGoodsForm(GoodsSubheadGoods goodsSubheadGoods,GoodsSubhead goodsSubhead,HttpServletRequest request,Model model){
		try{
			goodsSubheadGoods.setGoodsSubhead(goodsSubhead);
			List<GoodsSubheadGoods> goodsList = mtmyGoodsSubheadDao.selectGoodsByGoodsSubheadId(goodsSubheadGoods);
			model.addAttribute("goodsList",goodsList);
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转修改商品副标题对应的商品页面", e);
			logger.error("跳转修改商品副标题对应的商品出错信息：" + e.getMessage());
		}
		return "modules/ec/goodsSubheadGoodsForm";
	}
	
	/**
	 * 给商品副标题添加商品
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="saveGoodsSubheadGoods")
	@ResponseBody
	public Map<String,String> saveGoodsSubheadGoods(GoodsSubhead goodsSubhead,HttpServletRequest request,RedirectAttributes redirectAttributes){
		Map<String,String> map = new HashMap<String,String>();
		int successNum = 0;
		int failureNum = 0;
		StringBuilder failureMsg = new StringBuilder();
		try{
			goodsSubhead = mtmyGoodsSubheadService.getGoodsSubhead(goodsSubhead.getGoodsSubheadId());
			String goodsIds = request.getParameter("goodsIds");
			if(!"".equals(goodsIds) && goodsIds != null){
				String goodsIdArray[] = goodsIds.split(",");
				for(String goodsId:goodsIdArray){
					if(!"".equals(goodsId)){
						int num = mtmyGoodsSubheadService.selectGoodsIsUsed(goodsSubhead.getStartDate(), goodsSubhead.getEndDate(), Integer.valueOf(goodsId));
						if(num > 0){
							String goodsName = goodsDao.getgoods(goodsId).getGoodsName();
							failureMsg.append("商品id:"+goodsId+","+"商品名称："+goodsName+",已被其他副标题活动占用;"+"\n");
							failureNum++;
						}else{
							mtmyGoodsSubheadService.insertGoodsForGoodsSubhead(goodsSubhead.getGoodsSubheadId(), Integer.valueOf(goodsId));
							successNum++;
						}
					}
				}
			}
			map.put("failureMsg", String.valueOf(failureMsg));
			map.put("failureNum", String.valueOf(failureNum));
			map.put("successNum",String.valueOf(successNum));
			map.put("result", "success");
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转修改商品副标题对应的商品页面", e);
			logger.error("跳转修改商品副标题对应的商品出错信息：" + e.getMessage());
			map.put("result","error");
		}
		return map;
	}
	
	/**
	 * 批量删除商品副标题对应的商品
	 * @param goodsSubhead
	 * @param request
	 * @return
	 */
	@RequestMapping(value="deleteAll")
	@ResponseBody
	public String deleteAll(HttpServletRequest request){
		try{
			String ids = request.getParameter("ids");
			String idArray[] = ids.split(",");
			for(String id:idArray){
				mtmyGoodsSubheadService.deleteGoods(Integer.valueOf(id));
			}
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "批量删除商品副标题对应的商品", e);
			logger.error("批量删除商品副标题对应的商品出错信息：" + e.getMessage());
			return "error";
		}
	}
	
	/**
	 *删除商品副标题对应的商品
	 * @param goodsSubhead
	 * @param request
	 * @return
	 */
	@RequestMapping(value="delGoods")
	@ResponseBody
	public String delGoods(HttpServletRequest request){
		try{
			mtmyGoodsSubheadService.deleteGoods(Integer.valueOf(request.getParameter("goodsSubheadGoodsId")));
			return "success";
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "删除商品副标题对应的商品", e);
			logger.error("删除商品副标题对应的商品出错信息：" + e.getMessage());
			return "error";
		}
	}
}
