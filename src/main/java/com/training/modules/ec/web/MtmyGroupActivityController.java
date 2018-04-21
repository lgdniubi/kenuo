package com.training.modules.ec.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.MtmyGroupActivity;
import com.training.modules.ec.entity.MtmyGroupActivityGoods;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.ec.service.MtmyGroupActivityService;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 团购活动
 * @author coffee
 * @date 2018年3月30日
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/groupActivity")
public class MtmyGroupActivityController extends BaseController {

	@Autowired
	private MtmyGroupActivityService mtmyGroupActivityService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	@ModelAttribute
	public MtmyGroupActivity get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return mtmyGroupActivityService.get(id);
		} else {
			return new MtmyGroupActivity();
		}
	}

	/**
	 * 分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(MtmyGroupActivity mtmyGroupActivity, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<MtmyGroupActivity> page = mtmyGroupActivityService.findList(new Page<MtmyGroupActivity>(request, response), mtmyGroupActivity);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购活动列表页", e);
			logger.error("团购活动列表页错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		return "modules/ec/mtmyGroupActivityList";
	}
	
	/**
	 * 创建、修改、查看团购项目
	 * @param mtmyGroupActivity
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:groupActivity:add","ec:groupActivity:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MtmyGroupActivity mtmyGroupActivity, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			mtmyGroupActivity.getParent().setId((mtmyGroupActivity.getParent().getId() == null || "".equals(mtmyGroupActivity.getParent().getId())) ? "0" : mtmyGroupActivity.getParent().getId());
			if(!mtmyGroupActivity.getParent().getId().equals("0")){
				mtmyGroupActivity.setParent(mtmyGroupActivityService.get(mtmyGroupActivity.getParent().getId()));
			}else{
				// 第一层级父类为0
				MtmyGroupActivity parent = new MtmyGroupActivity();
				parent.setId("0");
				mtmyGroupActivity.setParent(parent);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购活动列表页", e);
			logger.error("团购活动列表页错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
			return "redirect:" + adminPath + "/ec/groupActivity/mtmyGroupActivityList";
		}
		return "modules/ec/mtmyGroupActivityForm";
	} 
	
	/**
	 * 保存团购项目
	 * @param mtmyGroupActivity
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(MtmyGroupActivity mtmyGroupActivity, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if(mtmyGroupActivity.getParent() != null && !"0".equals(mtmyGroupActivity.getParent().getId()) && !"".equals(mtmyGroupActivity.getParent().getId())){
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				mtmyGroupActivity.setActivityStartTime(sdf.parse(mtmyGroupActivity.getActivityStartTimeStr()));
				mtmyGroupActivity.setActivityEndTime(sdf.parse(mtmyGroupActivity.getActivityEndTimeStr()));
			}
			mtmyGroupActivityService.saveMtmyGroupActivity(mtmyGroupActivity);
			addMessage(redirectAttributes, "保存团购项目 "+mtmyGroupActivity.getName()+" 成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存团购活动", e);
			logger.error("保存团购活动错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		redirectAttributes.addAttribute("parent.id", mtmyGroupActivity.getParent().getId());
		return "redirect:" + adminPath + "/ec/groupActivity/list";
	}
	
	/**
	 * 删除团购项目
	 * @param mtmyGroupActivity
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(MtmyGroupActivity mtmyGroupActivity, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			mtmyGroupActivityService.delete(mtmyGroupActivity);
			mtmyGroupActivityService.deleteActivityGoodsByActivity(mtmyGroupActivity);
			addMessage(redirectAttributes, "删除团购项目成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除团购活动", e);
			logger.error("删除团购活动错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		redirectAttributes.addAttribute("parent.id", mtmyGroupActivity.getParent().getId());
		return "redirect:" + adminPath + "/ec/groupActivity/list";
	}
	
	/**
	 * 团购商品列表
	 * @param mtmyGroupActivityGoods
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "goodsList")
	public String goodsList(MtmyGroupActivityGoods mtmyGroupActivityGoods, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			Page<MtmyGroupActivityGoods> page = mtmyGroupActivityService.findGoodsList(new Page<MtmyGroupActivityGoods>(request, response), mtmyGroupActivityGoods);
			model.addAttribute("page", page);
			model.addAttribute("mtmyGroupActivityGoods", mtmyGroupActivityGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购商品列表", e);
			logger.error("团购商品列表错误信息:"+e.getMessage());
			redirectAttributes.addAttribute("parent.id", mtmyGroupActivityGoods.getMtmyGroupActivity().getId());
			return "redirect:" + adminPath + "/ec/groupActivity/list";
		}
		return "modules/ec/mtmyGroupActivityGoodsList";
	}
	/**
	 * 团购商品列表
	 * @param mtmyGroupActivityGoods
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "goodsForm")
	public String goodsFrom(MtmyGroupActivityGoods mtmyGroupActivityGoods, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			if(mtmyGroupActivityGoods.getgId() != 0){
				mtmyGroupActivityGoods = mtmyGroupActivityService.findGoodsForm(mtmyGroupActivityGoods);
				Goods goods = new Goods(); goods.setGoodsId(mtmyGroupActivityGoods.getGoods().getGoodsId());
				List<GoodsSpecPrice> goodsspecpricelist = goodsService.findGoodsSpecPrice(goods);
				mtmyGroupActivityGoods.getGoods().setGoodsSpecPricesList(goodsspecpricelist);
			}
			model.addAttribute("mtmyGroupActivityGoods", mtmyGroupActivityGoods);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购商品列表", e);
			logger.error("团购商品列表错误信息:"+e.getMessage());
			redirectAttributes.addAttribute("mtmyGroupActivity.id", mtmyGroupActivityGoods.getMtmyGroupActivity().getId());
		}
		return "modules/ec/mtmyGroupActivityGoodsForm";
	}
	
	/**
	 * 根据商品id 查询信息
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "Getgood")
	public Goods Getgood(String id, HttpServletRequest request, HttpServletResponse response) {
		Goods goods = null;
		try {
			goods = goodsService.get(id);
			if(goods!= null){
				List<GoodsSpecPrice> goodsspecpricelist = goodsService.findGoodsSpecPrice(goods);
				goods.setGoodsSpecPricesList(goodsspecpricelist);
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购商品--根据商品id查询信息", e);
			logger.error("团购商品--根据商品id查询信息:"+e.getMessage());
		}
		return goods;
	}
	/**
	 * 修改团购商品详情
	 * @param mtmyGroupActivityGoods
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveGoods")
	public String saveGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			mtmyGroupActivityService.groupActivityGoods(mtmyGroupActivityGoods);	// 新增/修改团购内商品
			List<GoodsSpecPrice> goodsspecpricelist = new ArrayList<GoodsSpecPrice>();
			String[] specKey = request.getParameterValues("specKey"); // 获取所有的规格key
			for (int i = 0; i < specKey.length; i++) {
				GoodsSpecPrice goodsSpecPrice = new GoodsSpecPrice();
				goodsSpecPrice.setGoodsId(Integer.toString(mtmyGroupActivityGoods.getGoods().getGoodsId()));
				goodsSpecPrice.setSpecKey(specKey[i]);
				goodsSpecPrice.setGroupActivityPrice(Double.valueOf(request.getParameter("specKey_"+specKey[i])));
				goodsspecpricelist.add(goodsSpecPrice);
			}
			mtmyGroupActivityService.updateActivityGoodsSpec(goodsspecpricelist);
			redirectAttributes.addAttribute("mtmyGroupActivity.id", mtmyGroupActivityGoods.getMtmyGroupActivity().getId());
			addMessage(redirectAttributes, "保存/修改团购内商品成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "保存/修改团购内商品", e);
			logger.error("保存/修改团购内商品错误信息:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/ec/groupActivity/goodsList";
	}
	
	/**
	 * 删除团购项目内的商品
	 * @param mtmyGroupActivity
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "deleteGoods")
	public String deleteGoods(MtmyGroupActivityGoods mtmyGroupActivityGoods, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			mtmyGroupActivityService.deleteActivityGoods(mtmyGroupActivityGoods);
			addMessage(redirectAttributes, "删除团购项目内商品");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "删除团购项目内商品", e);
			logger.error("删除团购项目内商品错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "操作出现异常，请与管理员联系");
		}
		redirectAttributes.addAttribute("mtmyGroupActivity.id", mtmyGroupActivityGoods.getMtmyGroupActivity().getId());
		return "redirect:" + adminPath + "/ec/groupActivity/goodsList";
	}
	
	/**
	 * 补仓界面
	 * @param mtmyGroupActivityGoods
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "specstocks")
	public String specstocks(MtmyGroupActivityGoods mtmyGroupActivityGoods, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			List<GoodsSpecPrice> goodsspecpricelist = goodsService.findGoodsSpecPrice(mtmyGroupActivityGoods.getGoods());
			model.addAttribute("mtmyGroupActivityGoods", mtmyGroupActivityGoods);
			model.addAttribute("goodsspecpricelist", goodsspecpricelist);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "补仓--获取规格数据", e);
			logger.error("补仓--获取规格数据错误信息:"+e.getMessage());
		}
		return "modules/ec/mtmyGroupActivitygoodsSpecStocks";
	}
	/**
	 * 补仓
	 * @param mtmyGroupActivityGoods
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "savespecstocks")
	public String savespecstocks(MtmyGroupActivityGoods mtmyGroupActivityGoods,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			String[] specKey = request.getParameterValues("specKey"); // 获取所有的规格key
			for (int i = 0; i < specKey.length; i++) {
				String goodsId = String.valueOf(mtmyGroupActivityGoods.getGoods().getGoodsId());
				String goodsSpecKey = specKey[i];
				int storeCount = Integer.valueOf(request.getParameter("specKey_storeCount_"+specKey[i]));
				// 团购库存补仓操作
				redisClientTemplate.sadd(RedisConfig.GOODS_GROUPACTIVITY_IDS_HASH, goodsId);
				if(StringUtils.isBlank(redisClientTemplate.get(RedisConfig.GOODS_GROUPACTIVITY_STORECOUNT_PREFIX+goodsId))){
					redisClientTemplate.sadd(RedisConfig.GOODS_GROUPACTIVITY_IDS_HASH, goodsId);
					redisClientTemplate.set(RedisConfig.GOODS_GROUPACTIVITY_STORECOUNT_PREFIX+goodsId,String.valueOf(storeCount));
					redisClientTemplate.sadd(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_HASH,goodsId+"#"+goodsSpecKey);
					redisClientTemplate.set(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey,String.valueOf(storeCount));
				}else{
					boolean str = redisClientTemplate.exists(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey);
					if(str){
						RedisLock redisLock = new RedisLock(redisClientTemplate, RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey);
						redisLock.lock();
						redisClientTemplate.incrBy(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey, storeCount);
						redisClientTemplate.incrBy(RedisConfig.GOODS_GROUPACTIVITY_STORECOUNT_PREFIX+goodsId,storeCount);
						redisLock.unlock();
					}else{
						redisClientTemplate.incrBy(RedisConfig.GOODS_GROUPACTIVITY_STORECOUNT_PREFIX+goodsId,storeCount);
						redisClientTemplate.sadd(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_HASH,goodsId+"#"+goodsSpecKey);
						redisClientTemplate.set(RedisConfig.GOODS_GROUPACTIVITY_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey,String.valueOf(storeCount));
					}
				}
				// 原始库存
				redisClientTemplate.sadd(RedisConfig.GOODS_IDS_HASH, goodsId);
				if(StringUtils.isBlank(redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId))){
					redisClientTemplate.sadd(RedisConfig.GOODS_IDS_HASH, goodsId);
					redisClientTemplate.set(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,String.valueOf(-storeCount));
					redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH,goodsId+"#"+goodsSpecKey);
					redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey,String.valueOf(-storeCount));
				}else{
					boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey);
					if(str){
						RedisLock redisLock = new RedisLock(redisClientTemplate, RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey);
						redisLock.lock();
						redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey, -storeCount);
						redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,-storeCount);
						redisLock.unlock();
					}else{
						redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,-storeCount);
						redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH,goodsId+"#"+goodsSpecKey);
						redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+goodsSpecKey,String.valueOf(-storeCount));
					}
				}
			}
			addMessage(redirectAttributes, "团购项目内商品补仓:成功");
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "团购项目内商品补仓", e);
			logger.error("团购项目内商品补仓错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "团购项目内商品补仓:失败");
		}
		redirectAttributes.addAttribute("mtmyGroupActivity.id", mtmyGroupActivityGoods.getMtmyGroupActivity().getId());
		return "redirect:" + adminPath + "/ec/groupActivity/goodsList";
	}
}
