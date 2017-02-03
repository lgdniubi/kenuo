package com.training.modules.ec.web;

import java.util.Date;
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
import com.training.modules.ec.entity.ActionInfo;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.service.ActionInfoService;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.UserUtils;

/**
 * 抢购活动
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/action")
public class ActionInfoController extends BaseController {

	public static final String GOOD_UNSHELVE_KEY = "GOOD_UNSHELVE_KEY"; //商品下架
	
	
	@Autowired
	private ActionInfoService actionInfoService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	@ModelAttribute
	public ActionInfo get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return actionInfoService.get(id);
		} else {
			return new ActionInfo();
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
	@RequiresPermissions("ec:action:view")
	@RequestMapping(value = { "list", "" })
	public String list(ActionInfo actionInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 查询更新已经过期的数据
		// if(UpdateStatus==0){
		//int num = actionInfoService.updateOldStatus();
	//	System.err.println("更新过期数据" + num);
		// UpdateStatus=1;
		// }

		Page<ActionInfo> page = actionInfoService.findAction(new Page<ActionInfo>(request, response), actionInfo);
		model.addAttribute("page", page);
		return "modules/ec/actionInfoList";
	}


	/**
	 * 创建活动，修改
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:action:view", "ec:action:add", "ec:action:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, ActionInfo actionInfo, Model model) {
		try {
			if(actionInfo.getActionId()>0){
				actionInfo=get(actionInfo.getActionId()+"");
			}
			model.addAttribute("actionInfo",actionInfo);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("创建活动页面：" + e.getMessage());
		}

		return "modules/ec/createActionForm";
	}
	
	/**
	 * 活动查看
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:action:view", "ec:action:add", "ec:action:edit" }, logical = Logical.OR)
	@RequestMapping(value = "viewform")
	public String viewForm(HttpServletRequest request, ActionInfo actionInfo, Model model) {
		try {
			if(actionInfo.getActionId()>0){
				actionInfo=get(actionInfo.getActionId()+"");
			}
			List<Goods> list=actionInfoService.ActionGoodslist(actionInfo.getActionId());
			model.addAttribute("list",list);
			model.addAttribute("actionInfo",actionInfo);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("创建活动页面：" + e.getMessage());
		}

		return "modules/ec/actionVimeForm";
	}
	
	
	/**
	 * 抢购商品列表
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:action:view", "ec:action:add", "ec:action:edit" }, logical = Logical.OR)
	@RequestMapping(value = "addActionGoodsList")
	public String addActionGoodsList(HttpServletRequest request, ActionInfo actionInfo, Model model) {
		try {
			List<Goods> list=actionInfoService.ActionGoodslist(actionInfo.getActionId());
			model.addAttribute("list",list);
			model.addAttribute("actionInfo", actionInfo);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "抢购商品列表", e);
			logger.error("抢购商品列表：" + e.getMessage());
		}

		return "modules/ec/addActionGoodsList";
	}
	
	/**
	 * 添加活动商品
	 * 
	 * @param coupon
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addActionGoodsForm")
	public String addActionGoodsForm(HttpServletRequest request, ActionInfo actionInfo, Model model) {
		try {
			model.addAttribute("actionInfo", actionInfo);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "添加抢购商品", e);
			logger.error("添加抢购商品页面：" + e.getMessage());
		}

		return "modules/ec/addActionGoodsForm";
	}
	
	
	/**
	 * 保存数据
	 * 
	 * @param speciality
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value = { "ec:action:add" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(ActionInfo actionInfo, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
			if(actionInfo.getActionId()>0){
				actionInfo.setUpdateBy(user);
				actionInfoService.update(actionInfo);
				addMessage(redirectAttributes, "修改活动成功！");
			}else{
				actionInfo.setCreateBy(user);
				actionInfo.setStatus(2);
				actionInfoService.insertAction(actionInfo);
				addMessage(redirectAttributes, "创建活动成功！");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建活动", e);
			logger.error("方法：save，创建活动：" + e.getMessage());
			addMessage(redirectAttributes, "创建活动失败");
		}

		return "redirect:" + adminPath + "/ec/action/list";

	}
	
	
	/**
	 * 开启，关闭活动
	 * 
	 * @param redEnvelope
	 * @param redirectAttributes
	 * @return
	 */

	@RequiresPermissions("ec:action:edit")
	@RequestMapping(value = "actionStatus")
	public String actionStatus(HttpServletRequest request, ActionInfo actionInfo, RedirectAttributes redirectAttributes) {
	
		try {
			User user = UserUtils.getUser();
			actionInfo.setUpdateBy(user);
			

			//活动开启
			if(actionInfo.getStatus()==1){
				actionInfo = get(actionInfo.getActionId()+"");
				
				//当系统当前时间大于活动开启时间时，点击开启时，上架商品
				Date now = new Date();
				if(now.getTime() >= actionInfo.getShowTime().getTime()){
					//活动开启
					actionInfo.setStatus(1);//商品上架
					actionInfo.setExecuteStatus(1);//已上架，不可定时器扫描
					actionInfoService.updateStatus(actionInfo);//修改活动状态
					
					
					//循环上架活动商品
					List<Goods> list=actionInfoService.ActionGoodslist(actionInfo.getActionId());
					Goods goods=new Goods();
					goods.setActionId(actionInfo.getActionId());
					goods.setIsOnSale("1");
					actionInfoService.updateGoodsStauts(goods);
					for (int i = 0; i < list.size(); i++) {
						//用户商品上下架Regis缓存
						if(goods.getIsOnSale().equals("1")){
							//上架
							redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, list.get(i).getGoodsId()+"");
						}
					}
				}else{
					//直接活动开启
					actionInfo.setStatus(1);//商品上架
					actionInfo.setExecuteStatus(0);//可进行定时器扫描
					actionInfoService.updateStatus(actionInfo);//修改活动状态
					actionInfo = get(actionInfo.getActionId()+"");
				}
			}
			//活动关闭
			if(actionInfo.getStatus()==2){
				actionInfo.setExecuteStatus(0);//可进行定时器扫描
				actionInfoService.updateStatus(actionInfo);//修改活动状态
				
				List<Goods> list=actionInfoService.ActionGoodslist(actionInfo.getActionId());
				
				Goods goods=new Goods();
				goods.setActionId(actionInfo.getActionId());
				goods.setIsOnSale("0");
				actionInfoService.updateGoodsStauts(goods);
				for (int i = 0; i < list.size(); i++) {
					//用户商品上下架Regis缓存
					if(goods.getIsOnSale().equals("0")){
						//下架
						redisClientTemplate.hset(GOOD_UNSHELVE_KEY, list.get(i).getGoodsId()+"", "0");
					}
				}
				
			}
			addMessage(redirectAttributes, "操作成功");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "开启关闭活动", e);
			addMessage(redirectAttributes, "操作失败！");
			logger.error("开启关闭：" + e.getMessage());
		}

		return "redirect:" + adminPath + "/ec/action/list";
	}

	
	
	/**
	 * 移除活动商品
	 * @param actionInfo
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "numByGoodsId")
	public int numByGoodsId(String goodsId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		int num=0;
		try {
			
			num=actionInfoService.numByGoodsId(goodsId);
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "添加活动商品", e);
			logger.error("方法：save，添加活动商品：" + e.getMessage());
			
		}
		return num;

	}
	
	
	
	
	/**
	 * 保存抢购商品
	 * @param actionInfo
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveGoods")
	public String saveGoods(ActionInfo actionInfo,String actionId,String actionType, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			if(actionInfo.getGoodsId().length()>0){
				String[] idarry = actionInfo.getGoodsId().split(",");
				for (int i = 0; i < idarry.length; i++) {
					Goods goods=new Goods();
					goods.setActionId(Integer.parseInt(actionId));
					goods.setGoodsId(Integer.parseInt(idarry[i]));
					goods.setActionType(actionType);
					actionInfoService.updateActionId(goods);
				}
			
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "添加活动商品", e);
			logger.error("方法：save，添加活动商品：" + e.getMessage());
			return "error";
		}

		return "success";

	}
	
	/**
	 * 移除活动商品
	 * @param actionInfo
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "dellGoods")
	public String dellGoods(String goodsId,String actionId,String actionType, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			
			Goods goods=new Goods();
			goods.setActionId(Integer.parseInt(actionId));
			goods.setGoodsId(Integer.parseInt(goodsId));
			goods.setActionType(actionType);
			actionInfoService.updateActionId(goods);
				
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "添加活动商品", e);
			logger.error("方法：save，添加活动商品：" + e.getMessage());
			return "error";
		}

		return "success";

	}

}
