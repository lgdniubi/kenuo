package com.training.modules.ec.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.training.common.persistence.Page;
import com.training.common.utils.ObjectUtils;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.ActionInfo;
import com.training.modules.ec.entity.Effect;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsAttribute;
import com.training.modules.ec.entity.GoodsAttributeMappings;
import com.training.modules.ec.entity.GoodsBrand;
import com.training.modules.ec.entity.GoodsCard;
import com.training.modules.ec.entity.GoodsCategory;
import com.training.modules.ec.entity.GoodsPosition;
import com.training.modules.ec.entity.GoodsSpec;
import com.training.modules.ec.entity.GoodsSpecImage;
import com.training.modules.ec.entity.GoodsSpecItem;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.GoodsType;
import com.training.modules.ec.service.ActionInfoService;
import com.training.modules.ec.service.GoodsAttributeService;
import com.training.modules.ec.service.GoodsBrandService;
import com.training.modules.ec.service.GoodsCardService;
import com.training.modules.ec.service.GoodsCategoryService;
import com.training.modules.ec.service.GoodsPositionService;
import com.training.modules.ec.service.GoodsService;
import com.training.modules.ec.service.GoodsSpecItemService;
import com.training.modules.ec.service.GoodsSpecService;
import com.training.modules.ec.service.GoodsTypeService;
import com.training.modules.ec.service.OrderGoodsService;
import com.training.modules.ec.utils.GoodsUtil;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.quartz.tasks.utils.RedisConfig;
import com.training.modules.quartz.utils.RedisLock;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;

import net.sf.json.JSONObject;

/**
 * 商品-Controller层
 * @author kele
 * @version 2016-6-22
 */
@Controller
@RequestMapping(value = "${adminPath}/ec/goods")
public class GoodsController extends BaseController{

	public static final String GOOD_DETAIL_KEY = "GOODS_DETAIL_"; // 商品详情app
	public static final String GOOD_DETAIL_WAP_KEY = "GOODS_DETAIL_WAP_"; // 商品详情wap
	public static final String GOODS_COMM_WAP_KEY = "GOODS_COMM_WAP_1_"; // 商品评论wap
	public static final String GOODS_RECOMMEND_KEY = "GOODS_RECOMMEND_"; // 商品推荐wap
	public static final String GOODS_AD_WAP_KEY = "GOODS_AD_WAP_"; // 商品副标题wap
	public static final String GOOD_UNSHELVE_KEY = "GOOD_UNSHELVE_KEY"; //商品下架
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsTypeService goodsTypeService;
	@Autowired
	private GoodsBrandService goodsBrandService;
	@Autowired
	private GoodsAttributeService goodsAttributeService;
	@Autowired
	private GoodsSpecService goodsSpecService;
	@Autowired
	private GoodsSpecItemService goodsSpecItemService;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private ActionInfoService actionInfoService;
	@Autowired
	private OrderGoodsService orderGoodsService;
	@Autowired
	private GoodsCardService goodsCardService;
	@Autowired
	private GoodsPositionService goodsPositionService;
	
	/**
	 * 分页查询商品属性
	 * @param goodsBrand
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:list"},logical=Logical.OR)
	@RequestMapping(value = {"list"})
	public String list(Goods goods,Model model, HttpServletRequest request, HttpServletResponse response){
		if(!"".equals(goods.getNewRatio()) && goods.getNewRatio() != null){
			String[] result = goods.getNewRatio().split("-");
			goods.setMinRatio(Double.valueOf(result[0]));
			goods.setMaxRatio(Double.valueOf(result[1]));
		}
		
		//查询商品品牌
		List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
		model.addAttribute("goodsBrandList", goodsBrandList);
		
		//商品规格列表
		Page<Goods> page = goodsService.find(new Page<Goods>(request, response), goods);
		model.addAttribute("page", page);
		model.addAttribute("goods", goods);
		
		//活动列表
		List<ActionInfo> actionList=actionInfoService.actionList();
		model.addAttribute("actionList", actionList);
		//查询商品分类
		GoodsCategory goodsCategory = new GoodsCategory();
		goodsCategory.setId(goods.getGoodsCategoryId());
		goodsCategory = goodsCategoryService.get(goodsCategory);
		model.addAttribute("goodsCategory", goodsCategory);
		
		return "modules/ec/goodsList";
	}
	
	/**
	 * 商品 查看、修改、添加
	 * @param goods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:view","ec:goods:add","ec:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(Goods goods,GoodsCard goodsCard,Model model,HttpServletRequest request, HttpServletResponse response){
		
		String opflag = request.getParameter("opflag");
		String id = request.getParameter("id");
		logger.info("#####修改标识[opflag]:"+opflag+"  [id]:"+id);
		model.addAttribute("opflag", opflag);
		try {
			//查询商品类型
			List<GoodsType> goodsTypeList = goodsTypeService.findAllList(new GoodsType());
			model.addAttribute("goodsTypeList", goodsTypeList);
			
			//查询商品品牌
			List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
			model.addAttribute("goodsBrandList", goodsBrandList);
			
			//查询项目部位
			List<GoodsPosition> goodsPositionList = goodsPositionService.findList(new GoodsPosition());
			model.addAttribute("goodsPositionList", goodsPositionList);
			
			
			if (null == id || "".equals(id)){
				//获取商品sort排序
				List<Goods> l = goodsService.findList(goods);
				if(l.size()==0){
					goods.setSort(goods.getSort());
				}else{
					goods.setSort(l.get(0).getSort() + 10);
				}
				
				//给设置默认赋值
				goods.setIsRecommend("0");
				goods.setIsHot("0");
				goods.setIsAppshow("0");
				goods.setIsOnSale("0");
				goods.setIsFreeShipping("1");
				goods.setIsNew("0");
				
				//添加套卡或者通用卡的路径
				if(StringUtils.isNotEmpty(opflag)){
					if(opflag.equals("ADDSUIT")){//套卡
						return "modules/ec/goodsFormSuit";
					}else if(opflag.equals("ADDCOMMON")){//通用卡
						return "modules/ec/goodsFormCommon";
					}
				}
				
			}else{
				//修改
				//根据商品ID，查询商品信息
				goods.setGoodsId(Integer.parseInt(id));
				goods = goodsService.get(goods);
				goods.setGoodsContent(HtmlUtils.htmlEscape(goods.getGoodsContent()));
				int goodsNum=orderGoodsService.numByGoodsId(goods.getGoodsId()+"");
				goods.setGoodsNum(goodsNum);
				//查询商品相册、商品护理流程 begin
				goods.setGoodsImagesList(goodsService.findGoodsImages(goods));
				//查询商品相册、商品护理流程 end
				
				
				//商品规格 begin
				//根据商品ID，查询所有规格价格信息
				List<GoodsSpecPrice> gspList = goodsService.findGoodsSpecPrice(goods);
				List<String> gspList2 = new ArrayList<String>();
				if(gspList.size() > 0){
					for (int i = 0; i < gspList.size(); i++) {
						GoodsSpecPrice gsp = gspList.get(i);
						String[] value = gsp.getSpecKey().split("_");
						for (int j = 0; j < value.length; j++) {
							gspList2.add(value[j]);
						}
					}
				}
				logger.debug("#####[gspList2]去重前:"+gspList2.toString());
				
				if(gspList2.size() > 0){
					//进行去重
					gspList2 = GoodsUtil.removeDuplicateWithOrder(gspList2);
					logger.debug("#####[gspList2]去重后:"+gspList2.toString());
					
					//根据商品id，查询出商品规格图片
					List<GoodsSpecImage> goodsSpecImagesList = goodsService.findspecimgbyid(goods);
					
					Map<String, List<String>> specitemmaps = new LinkedHashMap<String, List<String>>();//保存规格的规格项数组
					
					StringBuffer tablecontent = new StringBuffer();
					//商品规格 begin
					GoodsSpec gs = new GoodsSpec();
					gs.setTypeId(Integer.parseInt(goods.getSpecType()));
					List<GoodsSpec> gslist = goodsSpecService.findList(gs);
					if(gslist.size() > 0){
						for (int i = 0; i < gslist.size(); i++) {
							gs = gslist.get(i);
							tablecontent.append("<tr>");
							tablecontent.append("<td>"+gs.getName()+"：</td>");
							tablecontent.append("<td>");
							List<GoodsSpecItem> gsilist = gs.getSpecItemList();
							if(gsilist.size() > 0){
								List<String> itesList = new ArrayList<String>();
								for (int j = 0; j < gsilist.size(); j++) {
									GoodsSpecItem gsi = gsilist.get(j);
									
									if(gspList2.contains(String.valueOf(gsi.getSpecItemId()))){
										tablecontent.append("<span class='btn btn-success' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'><input type='checkbox' name='"+gsi.getSpecItemId()+"'>"+gsi.getItem()+"</span>");
										
										GoodsSpecImage goodsSpecImage = GoodsUtil.getSpecImgObject(String.valueOf(gsi.getSpecItemId()), goodsSpecImagesList);
										//若商品规格图片不为null时，则回写商品规格图片
										if(null != goodsSpecImage){
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" > ");
											tablecontent.append("<img src=\""+goodsSpecImage.getSrc()+"\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\""+goodsSpecImage.getSrc()+"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}else{
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" >");
											tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}
										
										itesList.add(String.valueOf(gsi.getSpecItemId()));
									}else{
										tablecontent.append("<span class='btn btn-default' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'><input type='checkbox' name='"+gsi.getSpecItemId()+"'>"+gsi.getItem()+"</span>");
										tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" >");
										tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
										tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
										tablecontent.append("</span>");
									}
								}
								specitemmaps.put(gs.getId(), itesList);
							}
							tablecontent.append("</td>");
							tablecontent.append("</tr>");
						}
					}
					//提交到页面展示
					model.addAttribute("goodsSpectablecontent", tablecontent.toString());
					
					//除去map中value为空的数组
					specitemmaps = GoodsUtil.removeMapNull(specitemmaps);
					
					if(specitemmaps.size() > 0 ){
					
						//根据选择的规格项，生成table内容
						tablecontent = new StringBuffer();
						tablecontent.append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\">");//开始
						tablecontent.append("<tr>");
						
						List<String[]> list = new ArrayList<String[]>();//保存规格的规格项数组
						
						for(Entry<String, List<String>> entry: specitemmaps.entrySet()) {
							String key = entry.getKey();
							GoodsSpec goodsSpec = new GoodsSpec();
							goodsSpec.setId(key);
							goodsSpec = goodsSpecService.get(goodsSpec);
							tablecontent.append("<td><b>"+goodsSpec.getName()+"</b></td>");
							
							List<String> itemlists = entry.getValue();
							String[] str = new String[itemlists.size()];
							for (int i = 0; i < itemlists.size(); i++) {
								str[i] = itemlists.get(i);
							}
							list.add(str);
						}
						
						tablecontent.append("<td><b>优惠价</b></td>");
						tablecontent.append("<td><b>市场价</b></td>");
						tablecontent.append("<td><b>系统价</b></td>");
						tablecontent.append("<td><b>报货价</b></td>");
						tablecontent.append("<td><b>采购价</b></td>");
						tablecontent.append("<td><b>库存</b></td>");
						tablecontent.append("<td><b>条码</b></td>");
						tablecontent.append("<td><b>商品编码</b></td>");
						tablecontent.append("<td><b>供应商商品编码</b></td>");
						tablecontent.append("<td><b>商品重量（克）</b></td>");
						tablecontent.append("<td><b>服务次数</b></td>");
						tablecontent.append("<td><b>截止时间（月）</b></td>");
						tablecontent.append("</tr>");
						
						//判断规格项有值时，才进行查询
						if(list.size() >0){
							//规格项数组，排列组合
							String str = "";
							GoodsUtil.updategoodsSpecItem(list, list.get(0), str,gspList,tablecontent,goodsSpecItemService);
						}
						tablecontent.append("</table>");//结尾
						//提交到页面展示
						model.addAttribute("goodsSpecItemtablecontent", tablecontent.toString());
					}
					
				}
				//商品规格 end
				
				//商品属性 begin
				//根据商品ID，查询所有属性值内容
				List<GoodsAttributeMappings> gamList = goodsService.findGoodsAttributeMappings(goods);
				//只有存在属性值
				if(gamList.size() > 0){
					//商品属性
					GoodsAttribute goodsAttribute = new GoodsAttribute();
					goodsAttribute.setTypeId(goods.getAttrType());
					List<GoodsAttribute> galist = goodsAttributeService.findList(goodsAttribute);
					if(galist.size() > 0){
						StringBuffer tablecontent = new StringBuffer();
						for (int i = 0; i < galist.size(); i++) {
							goodsAttribute = galist.get(i);
							tablecontent.append("<tr class='attr_"+goodsAttribute.getId()+"'>");
							tablecontent.append("<td>"+goodsAttribute.getAttrName()+"</td>");
							tablecontent.append("<td>");
							
							GoodsAttributeMappings gam = GoodsUtil.getAttributeObject(goodsAttribute.getAttrId(), gamList);
							
							if (null == gam) {
								//0 手工录入 1从列表中选择 2多行文本框
								if(0 == goodsAttribute.getAttrInputType()){
									tablecontent.append("<input type='text' size='40' value='"+goodsAttribute.getAttrValues()+"' name='attr_"+goodsAttribute.getId()+"'/>");
								}else if(1 == goodsAttribute.getAttrInputType()){
									String[] attrvalues = goodsAttribute.getAttrValues().split("\r\n");
									tablecontent.append("<select name='attr_"+goodsAttribute.getId()+"'>");
									for (int j = 0; j < attrvalues.length; j++) {
										tablecontent.append("<option value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
									}
									tablecontent.append("</select>");
								}
							}else{
								//0 手工录入 1从列表中选择 2多行文本框
								if(0 == goodsAttribute.getAttrInputType()){
									tablecontent.append("<input type='text' size='40' value='"+gam.getAttrValue()+"' name='attr_"+goodsAttribute.getId()+"'/>");
								}else if(1 == goodsAttribute.getAttrInputType()){
									String[] attrvalues = goodsAttribute.getAttrValues().split("\r\n");
									tablecontent.append("<select name='attr_"+goodsAttribute.getId()+"'>");
									for (int j = 0; j < attrvalues.length; j++) {
										if(gam.getAttrValue().equals(attrvalues[j])){
											tablecontent.append("<option selected=\"selected\" value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
										}else{
											tablecontent.append("<option value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
										}
										
									}
									tablecontent.append("</select>");
								}
							}
							
							tablecontent.append("</td>");
							tablecontent.append("</tr>");
						}
						//提交到页面展示
						model.addAttribute("goodsAttributecontent", tablecontent.toString());
					}
				}
				//商品属性 end
			}
			
			//查看商品的技能标签
			if(goods.getSkill() == null || String.valueOf(goods.getSkill().getSkillId()) == null){
				goods.setSkill(goodsService.getSkillByGoodsId(goods.getGoodsId()));
			}
			
			//查看商品的特殊设备
			if(goods.getEquipmentLabel() == null || String.valueOf(goods.getEquipmentLabel().getEquipmentLabelId()) == null){
				goods.setEquipmentLabel(goodsService.getEquipmentLabelByGoodsId(goods.getGoodsId()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "保存/修改 页面出现异常!", e);
			logger.error("保存/修改 页面出现异常，异常信息为："+e.getMessage());
		}
		

		model.addAttribute("goods", goods);
		return "modules/ec/goodsForm";
	}
	/**
	 * 商品 查看、修改、添加
	 * @param goods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:view","ec:goods:add","ec:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = {"formCard"})
	public String formCard(Goods goods,GoodsCard goodsCard,Model model,HttpServletRequest request, HttpServletResponse response){
		
		String id = request.getParameter("id");
		
		//查询商品类型
		List<GoodsType> goodsTypeList = goodsTypeService.findAllList(new GoodsType());
		model.addAttribute("goodsTypeList", goodsTypeList);
		
		//查询商品品牌
		List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
		model.addAttribute("goodsBrandList", goodsBrandList);
		try {
			//根据商品ID，查询商品信息
			goods.setGoodsId(Integer.parseInt(id));
			goods = goodsService.get(goods);
			goods.setGoodsContent(HtmlUtils.htmlEscape(goods.getGoodsContent()));
			int goodsNum=orderGoodsService.numByGoodsId(goods.getGoodsId()+"");
			goods.setGoodsNum(goodsNum);
			//查询商品相册、商品护理流程 begin
			goods.setGoodsImagesList(goodsService.findGoodsImages(goods));
			//查询商品相册、商品护理流程 end
			
			//通用卡存在商品规格 begin
			//根据商品ID，查询所有规格价格信息
			if(goods.getIsReal().equals("3")){
				List<GoodsSpecPrice> gspList = goodsService.findGoodsSpecPrice(goods);
				List<String> gspList2 = new ArrayList<String>();
				if(gspList.size() > 0){
					for (int i = 0; i < gspList.size(); i++) {
						GoodsSpecPrice gsp = gspList.get(i);
						String[] value = gsp.getSpecKey().split("_");
						for (int j = 0; j < value.length; j++) {
							gspList2.add(value[j]);
						}
					}
				}
				logger.debug("#####[gspList2]去重前:"+gspList2.toString());
				
				if(gspList2.size() > 0){
					//进行去重
					gspList2 = GoodsUtil.removeDuplicateWithOrder(gspList2);
					logger.debug("#####[gspList2]去重后:"+gspList2.toString());
					
					//根据商品id，查询出商品规格图片
					List<GoodsSpecImage> goodsSpecImagesList = goodsService.findspecimgbyid(goods);
					
					Map<String, List<String>> specitemmaps = new LinkedHashMap<String, List<String>>();//保存规格的规格项数组
					
					StringBuffer tablecontent = new StringBuffer();
					//商品规格 begin
					GoodsSpec gs = new GoodsSpec();
					gs.setTypeId(Integer.parseInt(goods.getSpecType()));
					List<GoodsSpec> gslist = goodsSpecService.findList(gs);
					if(gslist.size() > 0){
						for (int i = 0; i < gslist.size(); i++) {
							gs = gslist.get(i);
							tablecontent.append("<tr>");
							tablecontent.append("<td>"+gs.getName()+"：</td>");
							tablecontent.append("<td>");
							List<GoodsSpecItem> gsilist = gs.getSpecItemList();
							if(gsilist.size() > 0){
								List<String> itesList = new ArrayList<String>();
								for (int j = 0; j < gsilist.size(); j++) {
									GoodsSpecItem gsi = gsilist.get(j);
									
									if(gspList2.contains(String.valueOf(gsi.getSpecItemId()))){
										tablecontent.append("<span class='btn btn-success' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'><input type='checkbox' name='"+gsi.getSpecItemId()+"'>"+gsi.getItem()+"</span>");
										
										GoodsSpecImage goodsSpecImage = GoodsUtil.getSpecImgObject(String.valueOf(gsi.getSpecItemId()), goodsSpecImagesList);
										//若商品规格图片不为null时，则回写商品规格图片
										if(null != goodsSpecImage){
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" > ");
											tablecontent.append("<img src=\""+goodsSpecImage.getSrc()+"\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\""+goodsSpecImage.getSrc()+"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}else{
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" >");
											tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}
										
										itesList.add(String.valueOf(gsi.getSpecItemId()));
									}else{
										tablecontent.append("<span class='btn btn-default' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'><input type='checkbox' name='"+gsi.getSpecItemId()+"'>"+gsi.getItem()+"</span>");
										tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" >");
										tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
										tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
										tablecontent.append("</span>");
									}
								}
								specitemmaps.put(gs.getId(), itesList);
							}
							tablecontent.append("</td>");
							tablecontent.append("</tr>");
						}
					}
					//提交到页面展示
					model.addAttribute("goodsSpectablecontent", tablecontent.toString());
					
					//除去map中value为空的数组
					specitemmaps = GoodsUtil.removeMapNull(specitemmaps);
					
					if(specitemmaps.size() > 0 ){
						
						//根据选择的规格项，生成table内容
						tablecontent = new StringBuffer();
						tablecontent.append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\">");//开始
						tablecontent.append("<tr>");
						
						List<String[]> list = new ArrayList<String[]>();//保存规格的规格项数组
						
						for(Entry<String, List<String>> entry: specitemmaps.entrySet()) {
							String key = entry.getKey();
							GoodsSpec goodsSpec = new GoodsSpec();
							goodsSpec.setId(key);
							goodsSpec = goodsSpecService.get(goodsSpec);
							tablecontent.append("<td><b>"+goodsSpec.getName()+"</b></td>");
							
							List<String> itemlists = entry.getValue();
							String[] str = new String[itemlists.size()];
							for (int i = 0; i < itemlists.size(); i++) {
								str[i] = itemlists.get(i);
							}
							list.add(str);
						}
						
						tablecontent.append("<td><b>优惠价</b></td>");
						tablecontent.append("<td><b>市场价</b></td>");
						tablecontent.append("<td><b>系统价</b></td>");
						tablecontent.append("<td><b>报货价</b></td>");
						tablecontent.append("<td><b>采购价</b></td>");
						tablecontent.append("<td><b>库存</b></td>");
						tablecontent.append("<td><b>条码</b></td>");
						tablecontent.append("<td><b>商品编码</b></td>");
						tablecontent.append("<td><b>供应商商品编码</b></td>");
						tablecontent.append("<td><b>商品重量（克）</b></td>");
						tablecontent.append("<td><b>服务次数</b></td>");
						tablecontent.append("<td><b>截止时间（月）</b></td>");
						tablecontent.append("</tr>");
						
						//判断规格项有值时，才进行查询
						if(list.size() >0){
							//规格项数组，排列组合
							String str = "";
							GoodsUtil.updategoodsSpecItem(list, list.get(0), str,gspList,tablecontent,goodsSpecItemService);
						}
						tablecontent.append("</table>");//结尾
						//提交到页面展示
						model.addAttribute("goodsSpecItemtablecontent", tablecontent.toString());
					}
					
				}
			}
			//商品规格 end
			
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "查看套卡和通用卡  页面出现异常!", e);
			logger.error("查看套卡和通用卡   页面出现异常，异常信息为："+e.getMessage());
		}
		model.addAttribute("goods", goods);
		return "modules/ec/goodsFormView";
	}
	
	/**
	 * 保存/修改 - 商品通用信息
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:save"},logical=Logical.OR)
	@RequestMapping(value = {"save"})
	public String save(Goods goods, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			goodsService.saveGoods(goods,request);
			addMessage(redirectAttributes, "保存/修改 商品'" + goods.getGoodsName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 商品通用信息 出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存/修改 商品通用信息 出现异常", e);
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list?actionId="+goods.getActionId();
	}
	/**
	 * 保存/修改 - 商品通用信息
	 * @param goods
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:save"},logical=Logical.OR)
	@RequestMapping(value = {"saveCard"})
	public String saveCard(Goods goods, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			goodsService.saveGoodsCard(goods,request);
			addMessage(redirectAttributes, "保存/修改 商品'" + goods.getGoodsName() + "'成功");
		} catch (Exception e) {
			logger.error("保存/修改 商品通用信息 出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "保存/修改 商品通用信息 出现异常", e);
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list?actionId="+goods.getActionId();
	}
	
	
	/**
	 * 补仓-填写页面
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:fromspecstocks"},logical=Logical.OR)
	@RequestMapping(value = {"fromspecstocks"})
	public String fromspecstocks(Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		String goodsId = request.getParameter("id");
		String actionId=request.getParameter("actionId");
		if(null != goodsId && !"".equals(goodsId.trim())){
			Goods goods = new Goods();
			goods.setGoodsId(Integer.parseInt(goodsId));
			goods = goodsService.get(goods);
			goods.setActionId(Integer.parseInt(actionId));
			//获取商品名称，判断是否根据商品ID查询到商品信息
			if(!StringUtils.isEmpty(goods.getGoodsName())){
				//根据商品ID，查询所有规格价格信息
				List<GoodsSpecPrice> gspList = goodsService.findGoodsSpecPrice(goods);
				model.addAttribute("gspList", gspList);
				model.addAttribute("goods", goods);
				
				return "modules/ec/goodsSpecStocks";
			}else{
				addMessage(redirectAttributes, "商品规格:保存/修改失败,未查询到商品信息");
			}
		}else{
			addMessage(redirectAttributes, "商品补仓:保存/修改失败,必要参数为空，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	/**
	 * 补仓-保存规格库存
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:savespecstocks"},logical=Logical.OR)
	@RequestMapping(value = {"savespecstocks"})
	public String savepecstocks(Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		String goodsId = request.getParameter("goodsId");
		String actionId=request.getParameter("actionId");
		if(null != goodsId && !"".equals(goodsId.trim())){
			Goods goods = new Goods();
			goods.setGoodsId(Integer.parseInt(goodsId));
			goods = goodsService.get(goods);
			
			//获取商品名称，判断是否根据商品ID查询到商品信息
			if(!StringUtils.isEmpty(goods.getGoodsName())){
				//根据商品ID，查询所有规格价格信息
				List<GoodsSpecPrice> gspList = goodsService.findGoodsSpecPrice(goods);
				List<GoodsSpecPrice> goodsSpecPricesList = new ArrayList<GoodsSpecPrice>();
				
//				String connector = ParametersFactory.getMtmyParamValues("mtmy_connector");
//				String weburl = ParametersFactory.getMtmyParamValues("mtmy_incrstore_url");
//				
//				logger.info("##### web接口版本号："+connector+",路径:"+weburl);
				
				int addcount = 0;//规格信息库存数
				int count = 0;//添加库存总数：所有规格累加
				
				//调用补仓的接口，try处理，若接口失败，直接跳转回页面
				try {
					for (int i = 0; i < gspList.size(); i++) {
						GoodsSpecPrice gsp = gspList.get(i);
						int storeCount = Integer.parseInt(request.getParameter("item["+gsp.getSpecKey()+"][store_count]"));// 库存数量
						
						addcount = addcount + storeCount;//规格新增库存累计
						
						//当新库存与老库存不相同，才进行接口
						if(storeCount != 0){
							//调用接口，更新缓存中的库存
//							String parpm = "{\"goods_id\":"+goodsId+",\"spec_key\":\""+gsp.getSpecKey()+"\",\"count\":"+storeCount+"}";
//							logger.info("#####接口入参："+parpm);
//							String url=weburl;
//							String result = WebUtils.postObject(parpm, url);
//							JSONObject jsonObject = JSONObject.fromObject(result);
//							logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg"));
							redisClientTemplate.sadd(RedisConfig.GOODS_IDS_HASH, goodsId);
							if(StringUtils.isBlank(redisClientTemplate.get(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId))){
								redisClientTemplate.sadd(RedisConfig.GOODS_IDS_HASH, goodsId);
								redisClientTemplate.set(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,String.valueOf(storeCount));
								redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH,goodsId+"#"+gsp.getSpecKey());
								redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+gsp.getSpecKey(),String.valueOf(storeCount));
							}else{
								boolean str = redisClientTemplate.exists(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+gsp.getSpecKey());
								if(str){
									RedisLock redisLock = new RedisLock(redisClientTemplate, RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+gsp.getSpecKey());
									redisLock.lock();
									redisClientTemplate.incrBy(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+gsp.getSpecKey(), storeCount);
									redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,storeCount);
									redisLock.unlock();
								}else{
									redisClientTemplate.incrBy(RedisConfig.GOODS_STORECOUNT_PREFIX+goodsId,storeCount);
									redisClientTemplate.sadd(RedisConfig.GOODS_SPECPRICE_HASH,goodsId+"#"+gsp.getSpecKey());
									redisClientTemplate.set(RedisConfig.GOODS_SPECPRICE_PREFIX+goodsId+"#"+gsp.getSpecKey(),String.valueOf(storeCount));
								}
							}
						}
							
						//保存新的库存
						int storeCountEnd = gsp.getStoreCount()+storeCount;
						gsp.setStoreCount(storeCountEnd<0?0:storeCountEnd);
						goodsSpecPricesList.add(gsp);
						//累计商品总库存
						count = count + storeCountEnd;
					}
				} catch (Exception e) {
					logger.info("#####补仓调用接口出现异常，异常信息为："+e.getMessage());
					addMessage(redirectAttributes, "商品补仓:保存/修改-调用接口失败,请与管理员联系");
					return "redirect:" + adminPath + "/ec/goods/list";
				}
				
				goods.setGoodsSpecPricesList(goodsSpecPricesList);//保存到商品实体bean-商品规格价格list
				//商品规格【价格】list有数据才进行保存操作
				if(goodsSpecPricesList.size() > 0){
					//添加商品排列组合规格项数据（先删除、后添加）
					goodsService.savespec(goods);
					
					//修改商品规格项类型
					goods.setTotalStore(goods.getTotalStore()+addcount);//总库存数
					goods.setStoreCount(count);//剩余库存数
					goodsService.updateStorCount(goods);
					addMessage(redirectAttributes, "商品补仓:成功");
				}
			}else{
				addMessage(redirectAttributes, "商品规格:保存/修改失败,未查询到商品信息");
			}
		}else{
			addMessage(redirectAttributes, "商品补仓:保存/修改失败,必要参数为空，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list?actionId="+actionId;
	}
	
	
	/**
	 * 保存/修改 - 商品属性
	 * @param goods
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:saveattribute"},logical=Logical.OR)
	@RequestMapping(value = {"saveattribute"})
	public String saveattribute(Goods goods, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			String goodsId = request.getParameter("goodsid");
			String attrTypeId = request.getParameter("attrTypeId");
			
			if(!StringUtils.isEmpty(goodsId) && !StringUtils.isEmpty(attrTypeId)){
				goods.setGoodsId(Integer.parseInt(goodsId));
				goods = goodsService.get(goods);
				
				//获取商品名称，判断是否根据商品ID查询到商品信息
				if(!StringUtils.isEmpty(goods.getGoodsName())){
					
					if("-1".equals(attrTypeId)){
						//删除商品属性数据
						goodsService.deleteattribute(goods);
						//修改商品规格项类型
						goods.setAttrType("0");
						goodsService.updategoodstype(goods);
						addMessage(redirectAttributes, "保存/修改 '" + goods.getGoodsName() + "-商品属性'成功");
					}else{
						List<GoodsAttributeMappings> goodsAttributeMappingsList = new ArrayList<GoodsAttributeMappings>();//商品的所有属性
						
						//商品属性
						GoodsAttribute goodsAttribute = new GoodsAttribute();
						goodsAttribute.setTypeId(attrTypeId);
						List<GoodsAttribute> galist = goodsAttributeService.findList(goodsAttribute);
						if(galist.size() > 0){
							for (int i = 0; i < galist.size(); i++) {
								goodsAttribute = galist.get(i);
								GoodsAttributeMappings gam = new GoodsAttributeMappings();
								gam.setGoodsId(goodsId);
								gam.setAttrId(Integer.parseInt(goodsAttribute.getId()));
								gam.setAttrValue(request.getParameter("attr_"+goodsAttribute.getId()));
								goodsAttributeMappingsList.add(gam);
							}
							goods.setGoodsAttributeMappingsList(goodsAttributeMappingsList);
						}
						
						//商品属性列表值大于0
						if(goodsAttributeMappingsList.size() > 0){
							//添加商品属性数据（先删除、后添加）
							goodsService.saveattribute(goods);
							//修改商品规格项类型
							goods.setAttrType(attrTypeId);
							goodsService.updategoodstype(goods);
							
							addMessage(redirectAttributes, "保存/修改 '" + goods.getGoodsName() + "-商品属性'成功");
						}
					}
					
				}else{
					addMessage(redirectAttributes, "商品属性:保存/修改失败,未查询到商品信息");
				}
			}else{
				addMessage(redirectAttributes, "商品属性:保存/修改失败,必要参数为空，请与管理员联系");
			}
			
		} catch (Exception e) {
			logger.error("保存/修改 商品属性 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "商品属性:程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	
	
	/**
	 * 商品-是否显示（上架、推荐、新品、热卖、app显示）
	 * @param goodsBrand
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:updateisyesno"},logical=Logical.OR)
	@RequestMapping(value = {"updateisyesno"})
	public @ResponseBody Map<String, String> updateisyesno(HttpServletRequest request){
		//商品属性-是否检索
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String id = request.getParameter("ID");
			String fromid = request.getParameter("FROMID");
			String isyesno = request.getParameter("ISYESNO");
			if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(fromid) && !StringUtils.isEmpty(isyesno)){
				Goods goods = new Goods();
				goods.setGoodsId(Integer.parseInt(id));
				
				if("ISONSALE".equals(fromid)){
					//上架
					goods.setIsOnSale(isyesno);
					
					//用户商品上下架Regis缓存
					if(isyesno.equals("0")){
						//下架
						redisClientTemplate.hset(GOOD_UNSHELVE_KEY, goods.getGoodsId()+"", "0");
					}else if(isyesno.equals("1")){
						//上架
						redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, goods.getGoodsId()+"");
					}
					
				}else if("ISRECOMMEND".equals(fromid)){
					//推荐
					goods.setIsRecommend(isyesno);
				}else if("ISNEW".equals(fromid)){
					//新品
					goods.setIsNew(isyesno);
				}else if("ISHOT".equals(fromid)){
					//热卖
					goods.setIsHot(isyesno);
				}else if("ISAPPSHOW".equals(fromid)){
					//app显示
					goods.setIsAppshow(isyesno);
				}
				goodsService.updateisyesno(goods);
				jsonMap.put("STATUS", "OK");
				jsonMap.put("ISYESNO", isyesno);
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "修改失败,必要参数为空");
			}
		} catch (Exception e) {
			logger.error("商品属性-是否检索 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "修改失败,出现异常");
		}
		return jsonMap;
	}
	/**
	 * 批量上下架
	 * @param ids
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:updateIsOnSale"},logical=Logical.OR)
	@RequestMapping(value = {"updateIsOnSale"})
	public String updateIsOnSale(String ids,String flag,HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			String idArray[] =ids.split(",");
			for(String id : idArray){
				if(!StringUtils.isEmpty(id)){
					Goods goods = new Goods();
					goods.setGoodsId(Integer.parseInt(id));
					if("1".equals(flag)){		
						//上架
						goods.setIsOnSale("1");
						redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, goods.getGoodsId()+"");
						addMessage(redirectAttributes, "批量上架成功");
					}else if("2".equals(flag)){
						//下架
						goods.setIsOnSale("0");
						redisClientTemplate.hset(GOOD_UNSHELVE_KEY, goods.getGoodsId()+"", "0");
						addMessage(redirectAttributes, "批量下架成功");
					}else if("3".equals(flag)){
						//是推荐
						goods.setIsRecommend("1");
						addMessage(redirectAttributes, "批量修改是推荐成功");
					}else if("4".equals(flag)){
						//否推介
						goods.setIsRecommend("0");
						addMessage(redirectAttributes, "批量修改否推荐成功");
					}else{
						addMessage(redirectAttributes, "批量上下架、修改推荐出现异常，请与管理员联系");
						return "redirect:" + adminPath + "/ec/goods/list";
					}
					goodsService.updateisyesno(goods);
				}
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "批量上下架、修改推荐", e);
			logger.error("批量上下架、修改推荐错误信息:"+e.getMessage());
			addMessage(redirectAttributes, "批量上下架、修改推荐出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	/**
	 * 商品删除
	 * @param goodsattribute
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:del"},logical=Logical.OR)
	@RequestMapping(value = {"delete"})
	public String delete(Goods goods, Model model,HttpServletRequest request, RedirectAttributes redirectAttributes){
		//删除
		String id = request.getParameter("id");
		logger.info("#####删除商品id："+id);
		if(null != id && !"".equals(id)){
			goods.setGoodsId(Integer.parseInt(id));
			goodsService.delete(goods);
			
			//自媒体每天美耶商品信息同步
			JSONObject jsonObject = new JSONObject();
			String updateMtmyGoodInfo = ParametersFactory.getMtmyParamValues("mtmy_updateMtmyGoodInfo");	
			logger.info("##### web接口路径:"+updateMtmyGoodInfo);	         
			String parpm = "{\"goodsId\":\""+goods.getGoodsId()+"\",\"delFlag\":\"2\"}";
			String url=updateMtmyGoodInfo;
			String result = WebUtils.postMediaObject(parpm, url);
			jsonObject = JSONObject.fromObject(result);
			logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
			
			addMessage(redirectAttributes, "删除商品信息成功");
		}else{
			addMessage(redirectAttributes, "删除失败，必要参数为空，请与系统管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	
	/**
	 * 根据商品类型ID查询其对应的商品规格/属性
	 * specTypeId	商品规格
	 * attrTypeId	商品属性
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:getattrcontent"},logical=Logical.OR)
	@RequestMapping(value = {"getattrcontent"})
	public @ResponseBody Map<String, String> getattrcontent(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String typeName = request.getParameter("TYPENAME");//规格、类型
			String typeValue = request.getParameter("TYPEVALUE");//商品类型值
			String goodsid = request.getParameter("GOODSID");//商品ID
			
			logger.info("#####[typeName]:"+typeName+" [typeValue]:"+typeValue+"  [goodsid]:"+goodsid);
			
			if(!StringUtils.isEmpty(typeName) && !StringUtils.isEmpty(typeValue)){
				StringBuffer tablecontent = new StringBuffer();
				if("SPECTYPEID".equals(typeName)){
					
					//根据商品ID，获取商品规格项价格值
					List<String> gspList2 = new ArrayList<String>();
					//根据商品ID，查询所有规格价格信息
					List<GoodsSpecPrice> gspList = new ArrayList<GoodsSpecPrice>();
					//根据商品ID，获取商品规格项图片
					List<GoodsSpecImage> goodsSpecImagesList = new ArrayList<GoodsSpecImage>();
					if(null != goodsid && !"".equals(goodsid) && !"0".equals(goodsid) && !"undefined".equals(goodsid)){
						Goods goods = new Goods();
						goods.setGoodsId(Integer.parseInt(goodsid));
						//根据商品id，查询出商品规格图片
						goodsSpecImagesList = goodsService.findspecimgbyid(goods);
						
						//查询所有规格价格信息
						gspList = goodsService.findGoodsSpecPrice(goods);
						if(gspList.size() > 0){
							for (int i = 0; i < gspList.size(); i++) {
								GoodsSpecPrice gsp = gspList.get(i);
								String[] value = gsp.getSpecKey().split("_");
								for (int j = 0; j < value.length; j++) {
									gspList2.add(value[j]);
								}
							}
						}
					}
					
					logger.debug("#####[gspList2]去重前:"+gspList2.toString());
					//进行去重
					gspList2 = GoodsUtil.removeDuplicateWithOrder(gspList2);
					logger.debug("#####[gspList2]去重后:"+gspList2.toString());
					
					//商品规格
					GoodsSpec gs = new GoodsSpec();
					gs.setTypeId(Integer.parseInt(typeValue));
					List<GoodsSpec> gslist = goodsSpecService.findList(gs);
					if(gslist.size() > 0){
						for (int i = 0; i < gslist.size(); i++) {
							gs = gslist.get(i);
							tablecontent.append("<tr>");
							tablecontent.append("<td>"+gs.getName()+"：</td>");
							List<GoodsSpecItem> gsilist = gs.getSpecItemList();
							if(gsilist.size() > 0){
								tablecontent.append("<td>");
								for (int j = 0; j < gsilist.size(); j++) {
									GoodsSpecItem gsi = gsilist.get(j);
									if(gspList2.contains(String.valueOf(gsi.getSpecItemId()))){
										GoodsSpecImage goodsSpecImage = GoodsUtil.getSpecImgObject(String.valueOf(gsi.getSpecItemId()), goodsSpecImagesList);
										tablecontent.append("<span class='btn btn-success' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'>");
										tablecontent.append(""+gsi.getItem()+"");
										tablecontent.append("</span>");
										//若商品规格图片不为null时，则回写商品规格图片
										if(null != goodsSpecImage){
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\" style=\"margin-right: 8px;\" > ");
											tablecontent.append("<img src=\""+goodsSpecImage.getSrc()+"\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\""+goodsSpecImage.getSrc()+"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}else{
											tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\"  style=\"margin-right: 8px;\" > ");
											tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
											tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
											tablecontent.append("</span>");
										}
									}else{
										tablecontent.append("<span class='btn btn-default' data-spec_id='"+gs.getId()+"' data-item_id='"+gsi.getSpecItemId()+"'>");
										tablecontent.append(""+gsi.getItem()+"");
										tablecontent.append("</span>");
										tablecontent.append("<span id=\"item_img_"+gsi.getSpecItemId()+"\"  style=\"margin-right: 8px;\" > ");
										tablecontent.append("<img src=\"/kenuo/static/ec/images/add-button.jpg\" style=\"width: 35px;height: 35px;\" onclick=\"specitemupload('"+gsi.getSpecItemId()+"')\">");
										tablecontent.append("<input type=\"hidden\" value=\"\" name=\"item_img["+gsi.getSpecItemId()+"]\">");
										tablecontent.append("</span>");
									}
								}
								tablecontent.append("</td>");
							}
							tablecontent.append("</tr>");
						}
						
						//获取商品的规格项保存的值 Begin
						//当商品ID有效时，获取商品规格项的内容
						if(null != goodsid && !"".equals(goodsid) && !"0".equals(goodsid) && !"undefined".equals(goodsid)){
							if(gspList2.size() > 0){
								Map<String, List<String>> specitemmaps = new HashMap<String, List<String>>();//保存规格的规格项数组
								StringBuffer specItemcontent = new StringBuffer();
								//商品规格
								if(gslist.size() > 0){
									for (int i = 0; i < gslist.size(); i++) {
										gs = gslist.get(i);
										List<GoodsSpecItem> gsilist = gs.getSpecItemList();
										if(gsilist.size() > 0){
											List<String> itesList = new ArrayList<String>();
											for (int j = 0; j < gsilist.size(); j++) {
												GoodsSpecItem gsi = gsilist.get(j);
												if(gspList2.contains(String.valueOf(gsi.getSpecItemId()))){
													itesList.add(String.valueOf(gsi.getSpecItemId()));
												}
											}
											specitemmaps.put(gs.getId(), itesList);
										}
									}
								}
								
								//出去map中value为空的数组
								specitemmaps = GoodsUtil.removeMapNull(specitemmaps);
								if(specitemmaps.size() > 0 ){
									//根据选择的规格项，生成table内容
									specItemcontent = new StringBuffer();
									specItemcontent.append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\">");//开始
									specItemcontent.append("<tr>");
									
									List<String[]> list = new ArrayList<String[]>();//保存规格的规格项数组
									
									for(Entry<String, List<String>> entry: specitemmaps.entrySet()) {
										String key = entry.getKey();
										GoodsSpec goodsSpec = new GoodsSpec();
										goodsSpec.setId(key);
										goodsSpec = goodsSpecService.get(goodsSpec);
										specItemcontent.append("<td><b>"+goodsSpec.getName()+"</b></td>");
										
										List<String> itemlists = entry.getValue();
										String[] str = new String[itemlists.size()];
										for (int i = 0; i < itemlists.size(); i++) {
											str[i] = itemlists.get(i);
										}
										list.add(str);
									}
									
									specItemcontent.append("<td><b>优惠价</b></td>");
									specItemcontent.append("<td><b>市场价</b></td>");
									specItemcontent.append("<td><b>系统价</b></td>");
									specItemcontent.append("<td><b>报货价</b></td>");
									specItemcontent.append("<td><b>采购价</b></td>");
									specItemcontent.append("<td><b>库存</b></td>");
									specItemcontent.append("<td><b>条码</b></td>");
									specItemcontent.append("<td><b>商品编码</b></td>");
									specItemcontent.append("<td><b>供应商商品编码</b></td>");
									specItemcontent.append("<td><b>商品重量（克）</b></td>");
									specItemcontent.append("<td><b>服务次数</b></td>");
									specItemcontent.append("<td><b>截止时间（月）</b></td>");
									specItemcontent.append("</tr>");
									
									//判断规格项有值时，才进行查询
									if(list.size() >0){
										//规格项数组，排列组合
										String str = "";
										GoodsUtil.updategoodsSpecItem(list, list.get(0), str,gspList,specItemcontent,goodsSpecItemService);
									}
									specItemcontent.append("</table>");//结尾
									//提交到页面展示
									jsonMap.put("SPECITEMCONTENT", specItemcontent.toString());
								}
							}
						}
						//获取商品的规格项保存的值 End
					}
					
				}else if("ATTRTYPEID".equals(typeName)){
					//商品属性
					
					List<GoodsAttributeMappings> gamList = new ArrayList<GoodsAttributeMappings>();
					//当商品ID有效时，查询所有属性值内容
					if(null != goodsid && !"".equals(goodsid) && !"0".equals(goodsid) && !"undefined".equals(goodsid)){
						Goods goods = new Goods();
						goods.setGoodsId(Integer.parseInt(goodsid));
						gamList = goodsService.findGoodsAttributeMappings(goods);
					}
					
					GoodsAttribute goodsAttribute = new GoodsAttribute();
					goodsAttribute.setTypeId(typeValue);
					List<GoodsAttribute> galist = goodsAttributeService.findList(goodsAttribute);
					if(galist.size() > 0){
						for (int i = 0; i < galist.size(); i++) {
							goodsAttribute = galist.get(i);
							tablecontent.append("<tr class='attr_"+goodsAttribute.getId()+"'>");
							tablecontent.append("<td>"+goodsAttribute.getAttrName()+"</td>");
							tablecontent.append("<td>");
							
							GoodsAttributeMappings gam = GoodsUtil.getAttributeObject(goodsAttribute.getAttrId(), gamList);
							
							if (null == gam) {
								//0 手工录入 1从列表中选择 2多行文本框
								if(0 == goodsAttribute.getAttrInputType()){
									tablecontent.append("<input type='text' size='40' value='"+goodsAttribute.getAttrValues()+"' name='attr_"+goodsAttribute.getId()+"'/>");
								}else if(1 == goodsAttribute.getAttrInputType()){
									String[] attrvalues = goodsAttribute.getAttrValues().split("\r\n");
									tablecontent.append("<select name='attr_"+goodsAttribute.getId()+"'>");
									for (int j = 0; j < attrvalues.length; j++) {
										tablecontent.append("<option value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
									}
									tablecontent.append("</select>");
								}
							}else{
								//0 手工录入 1从列表中选择 2多行文本框
								if(0 == goodsAttribute.getAttrInputType()){
									tablecontent.append("<input type='text' size='40' value='"+gam.getAttrValue()+"' name='attr_"+goodsAttribute.getId()+"'/>");
								}else if(1 == goodsAttribute.getAttrInputType()){
									String[] attrvalues = goodsAttribute.getAttrValues().split("\r\n");
									tablecontent.append("<select name='attr_"+goodsAttribute.getId()+"'>");
									for (int j = 0; j < attrvalues.length; j++) {
										if(gam.getAttrValue().equals(attrvalues[j])){
											tablecontent.append("<option selected=\"selected\" value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
										}else{
											tablecontent.append("<option value='"+attrvalues[j]+"'>"+attrvalues[j]+"</option>");
										}
										
									}
									tablecontent.append("</select>");
								}
							}
							
							tablecontent.append("</td>");
							tablecontent.append("</tr>");
						}
					}
					
				}
				jsonMap.put("STATUS", "OK");
				jsonMap.put("TABLECONTENT", tablecontent.toString());
				
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "查询失败,必要参数为空");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据商品类型ID查询其对应的商品规格/属性 出现异常，异常信息为!", e);
			logger.error("根据商品类型ID查询其对应的商品规格/属性 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "查询失败,出现异常");
		}
		return jsonMap;		
	}
	
	/**
	 * 根据选择的商品规格项动态生成规格项表格
	 * specTypeId	商品规格
	 * attrTypeId	商品属性
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:getspeccontent"},logical=Logical.OR)
	@RequestMapping(value = {"getspeccontent"})
	public @ResponseBody Map<String, String> getspeccontent(HttpServletRequest request){
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			String spec_arr = request.getParameter("SPECARR");
			logger.info("#####[spec_arr]:"+spec_arr);
			if(!StringUtils.isEmpty(spec_arr) && spec_arr.length() >0){
				//根据选择的规格项，生成table内容
				StringBuffer tablecontent = new StringBuffer();
				tablecontent.append("<table class=\"table table-bordered\" id=\"goods_spec_input_table\">");//开始
				tablecontent.append("<tr>");
				
				List<String[]> list = new ArrayList<String[]>();//保存规格的规格项数组
				JSONObject json = JSONObject.fromObject(spec_arr);
				Iterator<?> iterator = json.keys();
				while(iterator.hasNext()){
					String key = (String) iterator.next();
					GoodsSpec goodsSpec = new GoodsSpec();
					goodsSpec.setId(key);
					goodsSpec = goodsSpecService.get(goodsSpec);
					tablecontent.append("<td><b>"+goodsSpec.getName()+"</b></td>");
					
					//保存所需选择的规格项的数组
					String[] value = (String[]) json.getString(key).replace("[", "").replace("]", "").split(",");
			        list.add(value);
				}
				logger.debug("#####[list]:"+list.toString());
				
				tablecontent.append("<td><b>优惠价</b></td>");
				tablecontent.append("<td><b>市场价</b></td>");
				tablecontent.append("<td><b>系统价</b></td>");
				tablecontent.append("<td><b>报货价</b></td>");
				tablecontent.append("<td><b>采购价</b></td>");
				tablecontent.append("<td><b>库存</b></td>");
				tablecontent.append("<td><b>条码</b></td>");
				tablecontent.append("<td><b>商品编码</b></td>");
				tablecontent.append("<td><b>供应商商品编码</b></td>");
				tablecontent.append("<td><b>商品重量（克）</b></td>");
				tablecontent.append("<td><b>服务次数</b></td>");
				tablecontent.append("<td><b>截止时间（月）</b></td>");
				tablecontent.append("</tr>");
				
				//判断规格项有值时，才进行查询
				if(list.size() >0){
					//规格项数组，排列组合
					String str = "";
					GoodsUtil.updategoodsSpecItem(list, list.get(0), str,null,tablecontent,goodsSpecItemService);
				}
				
				tablecontent.append("</table>");//结尾
				
				jsonMap.put("STATUS", "OK");
				jsonMap.put("TABLECONTENT", tablecontent.toString());
				
			}else{
				jsonMap.put("STATUS", "ERROR");
				jsonMap.put("MESSAGE", "查询失败,必要参数为空");
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "根据商品类型ID查询其对应的商品规格/属性 出现异常，异常信息为!", e);
			logger.error("根据商品类型ID查询其对应的商品规格/属性 出现异常，异常信息为："+e.getMessage());
			jsonMap.put("STATUS", "ERROR");
			jsonMap.put("MESSAGE", "查询失败,出现异常");
		}
		return jsonMap;	
	}
	/**
	 * 查询所有功效
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Effect effect=new Effect();
		List<Effect> list = goodsService.findEffect(effect);
		for (int i=0; i<list.size(); i++){
			Effect e = list.get(i);
			
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getEfId());
//			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 分页查询所有功效
	 * @param effect
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findEffectPage")
	public String findEffectPage(Effect effect,HttpServletRequest request, HttpServletResponse response,Model model){
		Page<Effect> page=goodsService.findEffectPage(new Page<Effect>(request, response), effect);
		model.addAttribute("page", page);
		return "modules/ec/effectList";
	}
	/**
	 * 保存功效
	 * @param effect
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveEffect")
	public String saveEffect(Effect effect,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		if(effect.getEfId()==0){
			goodsService.saveEffect(effect);
			addMessage(redirectAttributes, "添加功效成功");
		}else{
			goodsService.updateEffect(effect);
			addMessage(redirectAttributes, "修改功效成功");
		}
		return "redirect:" + adminPath + "/ec/goods/findEffectPage";
	}
	/**
	 * 删除功效
	 * @param effect
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deleteEffect")
	public String deleteEffect(Effect effect,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		goodsService.deleteEffect(effect);
		addMessage(redirectAttributes, "删除功效成功");
		return "redirect:" + adminPath + "/ec/goods/findEffectPage";
	}
	/**
	 * 批量删除功效
	 * @param effect
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "deleteAllEffect")
	public String deleteAllEffect(Effect effect,String ids,HttpServletRequest request, HttpServletResponse response,Model model,RedirectAttributes redirectAttributes){
		String idArray[] =ids.split(",");
		for(String id : idArray){
			effect.setEfId(Integer.parseInt(id));
			goodsService.deleteEffect(effect);
		}
		addMessage(redirectAttributes, "批量删除功效成功");
		return "redirect:" + adminPath + "/ec/goods/findEffectPage";
	}
	
	/**
	 * 查询分类下的商品
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeGoodsData")
	public List<Map<String, Object>> treeGoodsData(@RequestParam(required=false) String extId,String franchiseeId,String goodsCategory,String actionType,String goodsName,String actionId,String goodsId,String isReal,String isOnSale,String isAppshow,String type,String isOpen,HttpServletResponse response) {
		// 注： type属于临时方案，目前仅用于下单时查询商品  type=1表示下单时下单需区分用户商家
		if(type != null && !"".equals(type)){
			String companyId = UserUtils.getUser().getCompany().getId();
			if(!"1".equals(companyId)){
				franchiseeId = companyId;
			}
		}
		List<Map<String, Object>> mapList = Lists.newArrayList();
		if("".equals(goodsId) || goodsId == null){
			goodsId = "0";
		}
		Goods goods=new Goods();
		goods.setGoodsCategoryId(goodsCategory);
		goods.setGoodsName(goodsName);
		goods.setActionType(actionType);
		goods.setFranchiseeId(franchiseeId);
		goods.setIsReal(isReal);
		goods.setIsOnSale(isOnSale);
		goods.setIsAppshow(isAppshow);
		goods.setGoodsId(Integer.valueOf(goodsId));
		if(actionId!=null){
			goods.setActionId(Integer.parseInt(actionId));
		}
		
		if(!"".equals(isOpen) && isOpen != null){
			if("0,".equals(isOpen)){                //若活动或者主题是公开的，则商品就是公开的
				goods.setOpenFlag("0");
			}else{
				String[] franchiseeIds = isOpen.split(",");
				if(franchiseeIds.length == 1){      //若活动或者主题不是公开的且只有一个商家
					goods.setFranchiseeId(franchiseeIds[0]);
				}else if(franchiseeIds.length >= 2){  //若活动或者主题不是公开的且多个商家
					goods.setOpenFlag("0");
				}
			}
		}
		
		List<Goods> list = goodsService.list(goods);
		for (int i=0; i<list.size(); i++){
			Goods e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getGoodsId());
			map.put("name", e.getGoodsName());
			mapList.add(map);
		}
 		return mapList;
	}
	
	/**
	 * 查询ALL商品
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "treeAllData")
	public List<Map<String, Object>> treeAllData(@RequestParam(required=false) String extId,String goodsCategory,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Goods> list = goodsService.findList(null);
		for (int i=0; i<list.size(); i++){
			Goods e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getGoodsId());
			map.put("name", e.getGoodsName());
			mapList.add(map);
		}
		return mapList;
	}
	
	
	/**
	 * 复制商品
	 * @param goods
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:view","ec:goods:add","ec:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = {"copyGood"})
	public String copyGood(Goods goods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			goodsService.saveCoypGoods(goods);	
			addMessage(redirectAttributes, "复制商品'" + goods.getGoodsName() + "'成功");
		} catch (Exception e) {
			logger.error("商品复制 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	
			
	}
	
	/**
	 * 商品规格价格list
	 * @param goods
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:goodsBySpecList"},logical=Logical.OR)
	@RequestMapping(value = {"goodsBySpecList"})
	public String goodsBySpecList(Goods goods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			List<GoodsSpecPrice> goodsspecpricelist = goodsService.findGoodsSpecPrice(goods);
			model.addAttribute("goodsspecpricelist", goodsspecpricelist);
			return "modules/ec/goodsBySpecList";
		} catch (Exception e) {
			logger.error("商品规格价格list 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "查看商品规格价格出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	}
	
	/**
	 * 卡项商品规格价格list
	 * @param goods
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:goodsBySpecList"},logical=Logical.OR)
	@RequestMapping(value = {"cardGoodsBySpecList"})
	public String cardGoodsBySpecList(Goods goods,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		String suitCardSons = "";
		try {
			List<GoodsSpecPrice> goodsspecpricelist = goodsService.findGoodsSpecPrice(goods);         //通用卡的规格信息
			List<GoodsCard> goodsCardSons = goodsCardService.selectSonsByCardId(goods.getGoodsId());  //套卡的子项信息
			if("2".equals(goods.getIsReal())){       //套卡商品
				suitCardSons = suitCardSons +
						"<thead>"+
							"<tr>"+
								"<th style='text-align: center;'>子项名称</th>"+
								"<th style='text-align: center;'>次(个)数</th>"+
								"<th style='text-align: center;'>市场价</th>"+
								"<th style='text-align: center;'>优惠价</th>"+
								"<th style='text-align: center;'>市场价合计</th>"+
								"<th style='text-align: center;'>优惠价合计</th>"+
							"</tr>"+
						"</thead>"+
						"<tbody>";
				if(goodsCardSons.size() > 0){
					for(GoodsCard goodsCard:goodsCardSons){
						suitCardSons = suitCardSons +
								"<tr style='text-align: center;'>"+
									"<td>"+goodsCard.getGoodsName()+"</td>"+
									"<td>"+goodsCard.getGoodsNum()+"</td>"+
									"<td>"+goodsCard.getMarketPrice()+"</td>"+
									"<td>"+goodsCard.getPrice()+"</td>"+
									"<td>"+goodsCard.getTotalMarketPrice()+"</td>"+
									"<td>"+goodsCard.getTotalPrice()+"</td>"+
								"</tr>";
					}
				}
				suitCardSons = suitCardSons +"</tbody>";
			}else if("3".equals(goods.getIsReal())){   //通用卡商品
				suitCardSons = suitCardSons +
						"<thead>"+
							"<tr>"+
								"<th style='text-align: center;'>规格ID</th>"+
								"<th style='text-align: center;'>规格名称</th>"+
								"<th style='text-align: center;'>市场价</th>"+
								"<th style='text-align: center;'>优惠价</th>"+
							"</tr>"+
						"</thead>"+
						"<tbody>";
				if(goodsspecpricelist.size() > 0){
					for(GoodsSpecPrice goodsSpecPrice:goodsspecpricelist){
						suitCardSons = suitCardSons +
								"<tr style='text-align: center;'>"+
									"<td>"+goodsSpecPrice.getSpecKey()+"</td>"+
									"<td>"+goodsSpecPrice.getSpecKeyValue()+"</td>"+
									"<td>"+goodsSpecPrice.getMarketPrice()+"</td>"+
									"<td>"+goodsSpecPrice.getPrice()+"</td>"+
								"</tr>";
					}
				}
				suitCardSons = suitCardSons +"</tbody>";
			}
		} catch (Exception e) {
			logger.error("卡项商品规格价格list 出现异常，异常信息为："+e.getMessage());
			addMessage(redirectAttributes, "查看卡项商品规格价格出现异常，请与管理员联系");
		}
		model.addAttribute("suitCardSons", suitCardSons);
		return "modules/ec/cardGoodsBySpecList";
	}
	
	/**
	 * 商品规格价格Form
	 * @param goods
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:goodsBySpecForm"},logical=Logical.OR)
	@RequestMapping(value = {"goodsBySpecForm"})
	public String goodsBySpecForm(GoodsSpecPrice goodsSpecPrice,Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){
		goodsSpecPrice = goodsService.findByGoodsSpecPrice(goodsSpecPrice);
		model.addAttribute("goodsSpecPrice", goodsSpecPrice);
		return "modules/ec/goodsBySpecForm";
	}
	
	/**
	 * 根据商品规格specKey，修改商品规格价格项
	 * @param goodsSpecPrice
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */ 
	@ResponseBody
	@RequestMapping(value = {"goodsBySpecSave"})
	public String goodsBySpecSave(GoodsSpecPrice goodsSpecPrice){
		try {
			int result = goodsService.updateByGoodsSpecPrice(goodsSpecPrice);
			if(result == 1){
				return "success";
			}
		} catch (Exception e) {
			logger.error("商品规格价格list 出现异常，异常信息为："+e.getMessage());
		}
		return "error";
	}
	/**
	 * 刷新商品详情缓存
	 * @param goods
	 * @return
	 */
	@RequiresPermissions(value={"ec:goods:refreshRedis"},logical=Logical.OR)
	@RequestMapping(value = {"refreshRedis"})
	public String refreshRedis(Goods goods,HttpServletRequest request,RedirectAttributes redirectAttributes){
		try {
			redisClientTemplate.del(ObjectUtils.serialize(GOOD_DETAIL_KEY+goods.getGoodsId()));
			redisClientTemplate.del(ObjectUtils.serialize(GOOD_DETAIL_WAP_KEY+goods.getGoodsId()));
			redisClientTemplate.del(ObjectUtils.serialize(GOODS_COMM_WAP_KEY + goods.getGoodsId()));
			redisClientTemplate.del(ObjectUtils.serialize(GOODS_RECOMMEND_KEY + goods.getGoodsId()));
			redisClientTemplate.del(ObjectUtils.serialize(GOODS_AD_WAP_KEY + goods.getGoodsId()));
			addMessage(redirectAttributes, "刷新商品(详情)成功");
		} catch (Exception e) {
			logger.error("刷新商品详情缓存出现异常，异常信息为："+e.getMessage());
			BugLogUtils.saveBugLog(request, "刷新商品详情缓存", e);
			addMessage(redirectAttributes, "程序出现异常，请与管理员联系");
		}
		return "redirect:" + adminPath + "/ec/goods/list";
	};
	/**
	 * 添加 修改 套卡子项对应的商品
	 * @param goods
	 * @param request
	 * @param model
	 * @param flag
	 * @return
	 */
	@RequestMapping(value="GoodsCardForm")
	public String GoodsCardForm(Goods goods,HttpServletRequest request,Model model,String flag){
		try{
			String isReal = request.getParameter("isReal");
			if(isReal != null && !"".equals(isReal)){
				//套卡内的项目或商品不可跨商家，同一商家内可以跨品牌
				model.addAttribute("franchiseeId", goods.getFranchiseeId());//所选商家
				model.addAttribute("isReal", Integer.valueOf(isReal));
			}
		}catch(Exception e){
			BugLogUtils.saveBugLog(request, "跳转增加主题图对应的商品页面", e);
			logger.error("跳转增加主题图页面对应的商品出错信息：" + e.getMessage());
		}
		return "modules/ec/GoodsCardForm";
	}
	
	/**
	 * 根据分类，关键字，商品id查询商品
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryGoods")
	public List<Map<String, Object>> queryGoods(String goodsCategory,String goodsName,String goodsIds,String cityIds,HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Integer> goodsIdsList = new ArrayList<Integer>();
		List<String> cityIdsList = new ArrayList<String>();
		if(!"".equals(goodsIds) && goodsIds != null){
			String[] newGoodsIds = goodsIds.split(",");
			for(String newGoodsId:newGoodsIds){
				goodsIdsList.add(Integer.valueOf(newGoodsId));
			}
		}
		
		if(!"".equals(cityIds) && cityIds != null){
			String[] newCityIds = cityIds.split(","); 
			for(String newCityId:newCityIds){
				cityIdsList.add(newCityId);
			}
		}
		
		Goods goods=new Goods();
		goods.setGoodsCategoryId(goodsCategory);
		goods.setGoodsName(goodsName);
		goods.setGoodsIds(goodsIdsList);
		goods.setCityIds(cityIdsList);
		
		List<Goods> list = goodsService.queryGoodsCanRatio(goods);
		for (int i=0; i<list.size(); i++){
			Goods e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getGoodsId());
			map.put("name", e.getGoodsName());
			mapList.add(map);
		}
 		return mapList;
	}
}
