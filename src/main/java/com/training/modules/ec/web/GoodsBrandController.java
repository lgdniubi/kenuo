package com.training.modules.ec.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsBrand;
import com.training.modules.ec.service.GoodsBrandService;

/**
 * 商品品牌-Controller层
 * @author kele
 * @version 2016-6-17
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsbrand")
public class GoodsBrandController extends BaseController{

	@Autowired
	private GoodsBrandService goodsBrandService;
	
	/**
	 * 分页查询商品品牌
	 * @param goodsBrand
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsbrand:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(GoodsBrand goodsBrand,Model model, HttpServletRequest request, HttpServletResponse response){
		Page<GoodsBrand> page = goodsBrandService.find(new Page<GoodsBrand>(request, response), goodsBrand);
		model.addAttribute("page", page);
		return "modules/ec/goodsBrandList";
	}
	
	/**
	 * 商品分类 查看、修改、添加
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsbrand:view","ec:goodsbrand:add","ec:goodsbrand:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsBrand goodsBrand,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsBrand.getId())){
			goodsBrand = goodsBrandService.get(goodsBrand);
		}else{
			//id为null或者"" 时，则为添加下级菜单时，code自增
			List<GoodsBrand> l = goodsBrandService.findAllList(goodsBrand);
			if(l.size()==0){
				goodsBrand.setSort(goodsBrand.getSort());
			}else{
				goodsBrand.setSort(l.get(l.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("goodsBrand", goodsBrand);
		return "modules/ec/goodsBrandForm";
	}
	
	/**
	 * 保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsbrand:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsBrand goodsBrand, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		
		try {
			goodsBrandService.save(goodsBrand);
			addMessage(redirectAttributes, "修改商品品牌'" + goodsBrand.getName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodsbrand/list";
	}
	
	/**
	 * 删除
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsbrand:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsBrand goodsBrand, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		goodsBrandService.delete(goodsBrand);;
		addMessage(redirectAttributes, "删除商品分类信息成功");
		return "redirect:" + adminPath + "/ec/goodsbrand/list";
	}
	
	/**
	 * 商品品牌是否推荐
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = {"updateishot"})
	public @ResponseBody Map<String, String> updateishot(HttpServletRequest request){
		
		//商品品牌是否推荐
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String isHot = request.getParameter("ISHOT");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(isHot)){
				GoodsBrand goodsBrand = new GoodsBrand();
				goodsBrand.setId(id);
				goodsBrand.setIsHot(Integer.parseInt(isHot));
				goodsBrandService.updateIsHot(goodsBrand);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISHOT", isHot);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("商品品牌是否推荐 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	
}
