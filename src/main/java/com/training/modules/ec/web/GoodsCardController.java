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
	
	
	/**
	 * 商品 查看、修改、添加
	 * @param goods
	 * @param model
	 * @param request
	 * @return
	 */
	/*@RequiresPermissions(value={"ec:goods:view","ec:goods:add","ec:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = {"form"})
	public String form(Goods goods,Model model,HttpServletRequest request){
		
		String opflag = request.getParameter("opflag");
		String id = request.getParameter("id");
		String isReal = request.getParameter("isReal");//判断是否为卡项
		logger.info("#####修改标识[opflag]:"+opflag+"  [id]:"+id);
		model.addAttribute("opflag", opflag);
		try {
			if(StringUtils.isNotEmpty(isReal)){
				if(isReal.equals("2")){//套卡
					
					return "modules/ec/goodsFormTao";
				}else if(isReal.equals("3")){//通用卡
					
				}
			}
			//查询商品类型
			List<GoodsType> goodsTypeList = goodsTypeService.findAllList(new GoodsType());
			model.addAttribute("goodsTypeList", goodsTypeList);
			
			//查询商品品牌
			List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
			model.addAttribute("goodsBrandList", goodsBrandList);
			
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
					//商品规格
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
	*/
}
