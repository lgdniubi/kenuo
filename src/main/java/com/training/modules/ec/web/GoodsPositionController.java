package com.training.modules.ec.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsPosition;
import com.training.modules.ec.service.GoodsPositionService;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 项目部位-Controller层
 * @author 土豆
 * @version 2017-10-9
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodsposition")
public class GoodsPositionController extends BaseController{

	@Autowired
	private GoodsPositionService goodsPositionService;
	
	/**
	 * 查询所有商品分类
	 * @param goodsCategory
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String list(GoodsPosition goodsPosition,Model model){
		GoodsPosition parent = new GoodsPosition();
		parent.setId("0");
		goodsPosition.setParent(parent);
		model.addAttribute("list", goodsPositionService.findAllList(goodsPosition));
		return "modules/ec/goodsPositionList";
	}
	
	/**
	 * 通过id加载子类
	 * @param id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public String getChildren(@RequestParam(required=false) String id, HttpServletResponse response) {
		GoodsPosition goodsPosition = new GoodsPosition();
		goodsPosition.setId(id);
		List<GoodsPosition> list = goodsPositionService.findListbyPID(id);
		//转为json格式
		JsonConfig jsonConfig = new JsonConfig();
	  	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	  	JSONArray json = JSONArray.fromObject(list, jsonConfig);
		return json.toString();
	}
	
	/**
	 * 商品分类 查看、修改、添加
	 * @param goodsCategory
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsposition:view","ec:goodsposition:add","ec:goodsposition:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsPosition goodsPosition,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsPosition.getId())){
			goodsPosition = goodsPositionService.get(goodsPosition);
		}else{
			//添加部位或者添加下级部位
			if(null != opflag && "ADDPARENT".equals(opflag)){
				goodsPosition.getParent().setId("0");
				goodsPosition.setLevel(1);//等级一级
			}else{
				GoodsPosition gp = goodsPositionService.get(goodsPosition.getParent().getId());
				goodsPosition.setParent(gp);
				int level = gp.getLevel()+1;
				goodsPosition.setLevel(level);
			}
		}
		model.addAttribute("goodsPosition", goodsPosition);
		return "modules/ec/goodsPositionForm";
	}
	
	/**
	 * 保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsposition:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsPosition goodsPosition, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		
		try {
			//保存/修改
			if (goodsPosition.getIsNewRecord()){
				//进行保存
				goodsPosition.setParentIds(goodsPosition.getParentIds()+goodsPosition.getParent().getId()+",");
				goodsPosition.setCreateBy(UserUtils.getUser());
				goodsPositionService.insertGoodsPosition(goodsPosition);
				
				addMessage(redirectAttributes, "保存机构'" + goodsPosition.getName() + "'成功");
			}else{
				//修改商家信息表
				goodsPosition.setUpdateBy(UserUtils.getUser());
				goodsPositionService.updateGoodsPosition(goodsPosition);
				addMessage(redirectAttributes, "修改机构'" + goodsPosition.getName() + "'成功");
			}
			
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodsposition/list";
	}
	
	/**
	 * 删除
	 * @param goodsCategory
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodsposition:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsPosition goodsPosition, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		goodsPosition = goodsPositionService.get(goodsPosition);
		
		goodsPositionService.delete(goodsPosition);
		addMessage(redirectAttributes, "删除商品分类信息成功");
		return "redirect:" + adminPath + "/ec/goodsposition/list";
	}
	
}
