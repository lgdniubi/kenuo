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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.service.GoodsCategoryService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 商品分类-Controller层
 * @author kele
 * @version 2016-6-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goodscategory")
public class GoodsCategoryController extends BaseController{

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	
	/**
	 * 查询所有商品分类
	 * @param goodsCategory
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"list"})
	public String list(GoodsCategory goodsCategory,Model model){
		GoodsCategory parent = new GoodsCategory();
		parent.setId("0");
		goodsCategory.setParent(parent);
		model.addAttribute("list", goodsCategoryService.findAllList(goodsCategory));
		return "modules/ec/goodsCategoryList";
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
		/*GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setCategoryId(id);*/
		List<GoodsCategory> list = goodsCategoryService.findListbyPID(id);
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
	@RequiresPermissions(value={"ec:goodscategory:view","ec:goodscategory:add","ec:goodscategory:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(GoodsCategory goodsCategory,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		logger.info("#####修改标识[opflag]:"+opflag);
		model.addAttribute("opflag", opflag);
		
		//当时id不为空与不为""时,查看
		if(!StringUtils.isEmpty(goodsCategory.getId())){
			goodsCategory = goodsCategoryService.get(goodsCategory);
		}else{
			//添加或者修改
			if(null != opflag && "ADDPARENT".equals(opflag)){
				goodsCategory.getParent().setId("0");
				goodsCategory.getParent().setCode("1");
				goodsCategory.setLevel("1");//等级一级
			}else{
				goodsCategory.setParent(goodsCategoryService.get(goodsCategory.getParent().getId()));
			}
			
			//id为null或者"" 时，则为添加下级菜单时，code自增
			List<GoodsCategory> l = goodsCategoryService.findListbyPID(goodsCategory.getParentId());
			long size = 0;
			if(l.size()==0){
				size=0;
				goodsCategory.setCode(goodsCategory.getParent().getCode()+StringUtils.leftPad(String.valueOf(size > 0 ? size : 1), 4, "0"));
				goodsCategory.setSort(goodsCategory.getSort());
			}else{
				for (int i = 0; i < l.size(); i++) {
					GoodsCategory e = l.get(i);
					size=Long.valueOf(e.getCode())+1;
				}
				goodsCategory.setCode(String.valueOf(size));
				goodsCategory.setSort(l.get(l.size()-1).getSort() + 30);
			}
		}
		model.addAttribute("goodsCategory", goodsCategory);
		return "modules/ec/goodsCategoryForm";
	}
	
	/**
	 * 保存/修改
	 * @param franchisee
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodscategory:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(GoodsCategory goodsCategory, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		
		try {
			//保存/修改
			if (goodsCategory.getIsNewRecord()){
				//进行保存
				goodsCategory.setParentIds(goodsCategory.getParentIds()+goodsCategory.getParent().getId()+",");
				goodsCategoryService.insertGoodsCategory(goodsCategory);
				
				addMessage(redirectAttributes, "保存机构'" + goodsCategory.getName() + "'成功");
			}else{
				//修改商家信息表
				goodsCategoryService.updateGoodsCategory(goodsCategory);
				addMessage(redirectAttributes, "修改机构'" + goodsCategory.getName() + "'成功");
			}
			
		} catch (Exception e) {
			logger.error("保存/修改 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goodscategory/list";
	}
	
	/**
	 * 删除
	 * @param goodsCategory
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodscategory:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(GoodsCategory goodsCategory, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		goodsCategory = goodsCategoryService.get(goodsCategory);
		
		goodsCategoryService.delete(goodsCategory);;
		addMessage(redirectAttributes, "删除商品分类信息成功");
		return "redirect:" + adminPath + "/ec/goodscategory/list";
	}
	
	/**
	 * 商品分类是否显示
	 * @param goodsCategory
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodscategory:updateisyesno"},logical=Logical.OR)
	@RequestMapping(value = {"updateisyesno"})
	public @ResponseBody Map<String, String> updateisyesno(HttpServletRequest request){
		//商品品牌是否显示
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String fromid = request.getParameter("FROMID");
			String isyesno = request.getParameter("ISYESNO");
			
			//校验参数不能为空
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(fromid) && !StringUtils.isEmpty(isyesno)){
				if("ISHOT".equals(fromid)){
					//是否推荐
					GoodsCategory goodsCategory = new GoodsCategory();
					goodsCategory.setId(id);
					goodsCategory.setIsHot(isyesno);
					goodsCategoryService.updateIsHot(goodsCategory);
				}else if("ISSHOW".equals(fromid)){
					//是否显示
					GoodsCategory goodsCategory = new GoodsCategory();
					goodsCategory.setId(id);
					goodsCategory.setIsShow(isyesno);
					goodsCategoryService.updateIsShow(goodsCategory);
				}
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISYESNO", isyesno);
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
	
	/**
	 * 校验父类，是否有需要修改的子类（是否显示、是否推荐）
	 * 有，则无法修改
	 * 无，则修改所有子类内容
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodscategory:verifyparent"},logical=Logical.OR)
	@RequestMapping(value = {"verifyparent"})
	public @ResponseBody Map<String, String> verifyparent(HttpServletRequest request){ 
		//商品品牌是否显示
		Map<String, String> jsonMap = new HashMap<String, String>();
		//先空着，后期添加此功能
		
		return jsonMap;
	}
	
	/**
	 * 根据商品父类id找到其子类商品分类
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goodscategory:querycatid"},logical=Logical.OR)
	@RequestMapping(value = {"querycatid"})
	public @ResponseBody Map<String, List<GoodsCategory>> querycatid(HttpServletRequest request){
		Map<String, List<GoodsCategory>> jsonMap = new HashMap<String, List<GoodsCategory>>();
		String id = request.getParameter("ID");
		logger.info("#####[id]:"+id);
		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setId(id);
		List<GoodsCategory> catList = goodsCategoryService.queryCategory(goodsCategory);
		jsonMap.put("catlist", catList);
		return jsonMap;
	}
	
	/**
	 * 获取商品分类JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（占时没用）
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) String positionType,@RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setPositionType(positionType);
		List<GoodsCategory> list = goodsCategoryService.findAllList(goodsCategory);
		for (int i=0; i<list.size(); i++){
			GoodsCategory e = list.get(i);
			if ((StringUtils.isBlank(extId) || 
					(extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
	/**
	 * 获取商品一级分类JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（占时没用）
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "catelist")
	public List<Map<String, Object>> catelist(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<GoodsCategory> list = goodsCategoryService.catelist();
		for (int i=0; i<list.size(); i++){
			GoodsCategory e = list.get(i);
			if ((StringUtils.isBlank(extId) || 
					(extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 根据一级查询二级分类JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（占时没用）
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "catetwolist")
	public List<Map<String, Object>> catetwolist(@RequestParam(required=false) String extId,String onCate, @RequestParam(required=false) String type,
			@RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<GoodsCategory> list = goodsCategoryService.catetwolist(onCate);
		for (int i=0; i<list.size(); i++){
			GoodsCategory e = list.get(i);
			if ((StringUtils.isBlank(extId) || 
					(extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	
}
