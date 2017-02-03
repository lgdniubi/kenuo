package com.training.modules.ec.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsBeauticianFee;
import com.training.modules.ec.service.GoodsBeauticianFeeService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 商品服务费基础配置
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/beautician")
public class GoodsBeauticianFeeController extends BaseController {

	@Autowired
	private GoodsBeauticianFeeService goodsBeauticianFeeService;

	@ModelAttribute
	public GoodsBeauticianFee get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return goodsBeauticianFeeService.get(id);
		} else {
			return new GoodsBeauticianFee();
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
	@RequiresPermissions("ec:beautician:view")
	@RequestMapping(value = { "list", "" })
	public String list(GoodsBeauticianFee goodsBeauticianFee, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<GoodsBeauticianFee> page = goodsBeauticianFeeService.findBeautician(new Page<GoodsBeauticianFee>(request, response), goodsBeauticianFee);
		model.addAttribute("page", page);
		return "modules/ec/goodsBeauticianList";
	}

	/**
	 * 创建活动，修改
	 * 
	 * @param goodsBeauticianFee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:beautician:view", "ec:beautician:add", "ec:beautician:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request, GoodsBeauticianFee goodsBeauticianFee, Model model) {
		try {
			
			model.addAttribute("goodsBeauticianFee",goodsBeauticianFee);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "创建商品服务费", e);
			logger.error("创建商品服务费页面：" + e.getMessage());
		}

		return "modules/ec/createGoodsBeautician";
	}
	

	
	
	/**
	 * 保存数据
	 * 
	 * @param activity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveCreate")
	public String saveCreate(GoodsBeauticianFee goodsBeauticianFee, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			int num=goodsBeauticianFeeService.selectByGoodsIdNum(goodsBeauticianFee.getGoodsId());
			if(num>0){
				addMessage(redirectAttributes, "该商品服务费已经配置！");
			}else{
				goodsBeauticianFeeService.insertBeaut(goodsBeauticianFee);
				addMessage(redirectAttributes, "保存成功！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存商品服务费异常", e);
			logger.error("方法：save，商品服务费：" + e.getMessage());
			addMessage(redirectAttributes, "保存失败");
		}

		return "redirect:" + adminPath + "/ec/beautician/list";

	}
	
	
	/**
	 * 创建活动，修改
	 * 
	 * @param goodsBeauticianFee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value = { "ec:beautician:view", "ec:beautician:add", "ec:beautician:edit" }, logical = Logical.OR)
	@RequestMapping(value = "editform")
	public String editform(HttpServletRequest request, GoodsBeauticianFee goodsBeauticianFee, Model model) {
		try {
			List<GoodsBeauticianFee> list=goodsBeauticianFeeService.selectBygoodsId(goodsBeauticianFee.getGoodsId());
			if(list.size()>0){
				goodsBeauticianFee.setGoodsName(list.get(1).getGoodsName());
				goodsBeauticianFee.setGoodsNo(list.get(1).getGoodsNo());
				goodsBeauticianFee.setBasisFee(list.get(1).getBasisFee());
				goodsBeauticianFee.setRemarks(list.get(1).getRemarks());
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getType()==1){
						goodsBeauticianFee.setPrimary(list.get(i).getPostFee());
					}else if(list.get(i).getType()==2){
						goodsBeauticianFee.setMiddle(list.get(i).getPostFee());
					}else if(list.get(i).getType()==3){
						goodsBeauticianFee.setHigh(list.get(i).getPostFee());
					}else if(list.get(i).getType()==4){
						goodsBeauticianFee.setInternship(list.get(i).getPostFee());
					}else if(list.get(i).getType()==5){
						goodsBeauticianFee.setStore(list.get(i).getPostFee());
					}else{
						goodsBeauticianFee.setPrther(list.get(i).getPostFee());
					}
				}
			}
			model.addAttribute("goodsBeauticianFee",goodsBeauticianFee);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "修改商品服务费页", e);
			logger.error("修改商品服务费页：" + e.getMessage());
		}

		return "modules/ec/editGoodsBeautician";
	}
	
	

	/**
	 * 保存数据
	 * 
	 * @param activity
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(GoodsBeauticianFee goodsBeauticianFee, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	
		try {
			goodsBeauticianFeeService.update(goodsBeauticianFee);
			addMessage(redirectAttributes, "保存成功！");
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存商品服务费异常", e);
			logger.error("方法：save，商品服务费：" + e.getMessage());
			addMessage(redirectAttributes, "保存失败");
		}

		return "redirect:" + adminPath + "/ec/beautician/list";

	}

	
	

}
