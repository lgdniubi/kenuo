package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.ec.dao.EquipmentLabelDao;
import com.training.modules.ec.dao.GoodsAttributeDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.dao.GoodsSpecPriceDao;
import com.training.modules.ec.dao.GoodsStatisticsDao;
import com.training.modules.ec.entity.Effect;
import com.training.modules.ec.entity.EquipmentLabel;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsAttribute;
import com.training.modules.ec.entity.GoodsAttributeMappings;
import com.training.modules.ec.entity.GoodsEquipmentLabel;
import com.training.modules.ec.entity.GoodsImages;
import com.training.modules.ec.entity.GoodsSkill;
import com.training.modules.ec.entity.GoodsSpecImage;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.GoodsStatisticsCountData;
import com.training.modules.ec.utils.GoodsUtil;
import com.training.modules.quartz.entity.GoodsCollect;
import com.training.modules.quartz.entity.StoreVo;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.SkillDao;
import com.training.modules.sys.entity.Skill;

import net.sf.json.JSONObject;

/**
 * 商品属性-Service层
 * 
 * @author kele
 * @version 2016-6-20
 */
@Service
@Transactional(readOnly = false)
public class GoodsService extends CrudService<GoodsDao, Goods> {

	public static final String GOOD_UNSHELVE_KEY = "GOOD_UNSHELVE_KEY"; // 商品下架
	public static final String GOODSSTORE = "GOODSSTORE_";	// 商品总库存
	public static final String SPECPRICE = "SPECPRICE_";	// 商品对应的规格
	public static final String GOODS_SPECPRICE_HASH = "GOODS_SPECPRICE_HASH";	//所有商品id集合
	
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsSpecPriceDao goodsSpecPriceDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private GoodsSpecItemService goodsSpecItemService;
	@Autowired
	private GoodsAttributeDao goodsAttributeDao;
	@Autowired
	private SkillDao skillDao;
	@Autowired
	private EquipmentLabelDao equipmentLabelDao;
	@Autowired
	private GoodsStatisticsDao goodsStatisticsDao;

	/**
	 * 分页展示所有信息
	 * 
	 * @param page
	 * @param trainLessonss
	 * @return
	 */
	public Page<Goods> find(Page<Goods> page, Goods goods) {
		goods.setPage(page);
		page.setList(dao.findList(goods));
		return page;
	}

	/**
	 * 商品-通用信息-保存
	 * 
	 * @param goods
	 * @return
	 */
	public void saveGoods(Goods goods, HttpServletRequest request) {
		// 商品详情转换
		goods.setGoodsContent(HtmlUtils.htmlUnescape(goods.getGoodsContent()));
		goods.setGoodsName(HtmlUtils.htmlUnescape(goods.getGoodsName()));
		goods.setGoodsShortName(HtmlUtils.htmlUnescape(goods.getGoodsShortName()));
		goods.setGoodsTags(HtmlUtils.htmlUnescape(goods.getGoodsTags()));
		
		if (0 == goods.getGoodsId()) {
			// 添加
			goods.setAttrType((goods.getAttrType() == null || "".equals(goods.getAttrType())) ? null : goods.getAttrType());
			goods.setSpecType((goods.getSpecType() == null || "".equals(goods.getSpecType())) ? null : goods.getSpecType());
			goods.setIsRecommend(goods.getIsRecommend() == null ? "0" : goods.getIsRecommend());
			goods.setIsHot(goods.getIsHot() == null ? "0" : goods.getIsHot());
			goods.setIsAppshow(goods.getIsAppshow() == null ? "0" : goods.getIsAppshow());
			goods.setIsOnSale(goods.getIsOnSale() == null ? "0" : goods.getIsOnSale());
			goods.setIsFreeShipping(goods.getIsFreeShipping() == null ? "0" : goods.getIsFreeShipping());
			goods.setIsNew(goods.getIsNew() == null ? "0" : goods.getIsNew());

			// 保存
			dao.insert(goods);
			int goodId = goods.getGoodsId();
			if (0 == goodId) {
				try {
					throw new Exception("保存失败");
				} catch (Exception e) {

				}
			}

			// 用户商品上下架Regis缓存
			if (goods.getIsOnSale().equals("0")) {
				// 下架
				redisClientTemplate.hset(GOOD_UNSHELVE_KEY, goodId + "", "0");
			} else if (goods.getIsOnSale().equals("1")) {
				// 上架
				redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, goodId + "");
			}

			// 保存护理流程图
			String[] goodsNurseImages = request.getParameterValues("goodsNurseImages");
			String[] goodsNurseImagesText = request.getParameterValues("goodsNurseImagesText");
			if (0 != goodId) {
				if (null != goodsNurseImages && goodsNurseImages.length > 0) {
					List<GoodsImages> giList = new ArrayList<GoodsImages>();
					for (int i = 0; i < goodsNurseImages.length; i++) {
						GoodsImages goodsImg = new GoodsImages();
						goodsImg.setGoodsId(String.valueOf(goodId));
						goodsImg.setImageUrl(goodsNurseImages[i]);
						goodsImg.setImageDesc((null != goodsNurseImagesText[i] && !"".equals(goodsNurseImagesText[i])? goodsNurseImagesText[i] : ""));// 商品描述
						goodsImg.setImageType(1);// 图片类型 0 是商品图 1 护理流程图
						goodsImg.setSort(i + 1 + "0");// 10、20
						giList.add(goodsImg);
					}
					goods.setGoodsImagesList(giList);
					goods.setImageType(1);// 商品图片类型[0:是商品图;1:护理流程图;]
					savegoodsimages(goods);
				} else {
					// 当没有图片时，删除原有的图片
					goods.setImageType(1);// 商品图片类型[0:是商品图;1:护理流程图;]
					deletegoodsimages(goods);
				}
			}

			String[] goodsImages = request.getParameterValues("goodsImages");

			if (goods.getGoodsId() > 0) {
				// goods.setGoodsId(Integer.parseInt(goodId));
				// goods = goodsService.get(goods);

				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {
					if (null != goodsImages && goodsImages.length > 0) {
						List<GoodsImages> giList = new ArrayList<GoodsImages>();
						for (int i = 0; i < goodsImages.length; i++) {
							GoodsImages goodsImg = new GoodsImages();
							goodsImg.setGoodsId(goods.getGoodsId() + "");
							goodsImg.setImageUrl(goodsImages[i]);
							goodsImg.setImageDesc("");// 商品描述
							goodsImg.setImageType(0);// 图片类型 0 是商品图 1 护理流程图
							goodsImg.setSort(i + 1 + "0");// 10、20
							giList.add(goodsImg);
						}
						goods.setGoodsImagesList(giList);
						goods.setImageType(0);// 商品图片类型[0:是商品图;1:护理流程图;]
						savegoodsimages(goods);
					} else {
						// 当商品相册为空时，删除以前全部数据
						goods.setImageType(0);// 商品图片类型[0:是商品图;1:护理流程图;]
						deletegoodsimages(goods);
					}

				}
			}

			// String goodsId = request.getParameter("goodsid");
			String specTypeId = request.getParameter("specTypeId");
			String spec_arr = request.getParameter("specarr");

			if (!StringUtils.isEmpty(specTypeId) && !StringUtils.isEmpty(spec_arr)) {

				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {

					if ("-1".equals(specTypeId) || null == spec_arr || "{}".equals(spec_arr)) {
						// 删除
						deletespec(goods);
						goods.setSpecType("0");
						updategoodstype(goods);

					} else {
						List<String[]> list = new ArrayList<String[]>();// 保存规格的规格项数组
						JSONObject json = JSONObject.fromObject(spec_arr);
						Iterator<?> iterator = json.keys();
						while (iterator.hasNext()) {
							String key = (String) iterator.next();
							// 保存所需选择的规格项的数组
							String[] value = (String[]) json.getString(key).replace("[", "").replace("]", "").split(",");
							list.add(value);
						}

						List<String> specItemList = new ArrayList<String>();// 获取所有排列组合值
						// 判断规格项有值时，才进行查询
						if (list.size() > 0) {
							// 规格项数组，排列组合
							String str = "";
							GoodsUtil.goodsSpecItem(list, list.get(0), str, specItemList, goodsSpecItemService);
						}

						// 判断随机排列活的组合值
						if (specItemList.size() > 0) {
							List<GoodsSpecPrice> goodsSpecPricesList = new ArrayList<GoodsSpecPrice>();
							List<GoodsSpecImage> goodsSpecImagesList = new ArrayList<GoodsSpecImage>();
							// int storeCount=0; //库存
							// 遍历组合值，拼接form表单的值，并循环获取
							for (int i = 0; i < specItemList.size(); i++) {
								GoodsSpecPrice gsp = new GoodsSpecPrice();
								gsp.setGoodsId(goods.getGoodsId() + "");// 商品id
								gsp.setSpecKey(specItemList.get(i));// 规格键
								gsp.setSpecKeyName(request.getParameter("item[" + specItemList.get(i) + "][spec_name]"));// 规格名称
								gsp.setSpecKeyValue(request.getParameter("item[" + specItemList.get(i) + "][key_name]"));// 规格键名中文
								gsp.setPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][price]")));// 优惠价格
								gsp.setMarketPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][market_price]")));// 市场价格
								gsp.setCostPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][cost_price]")));// 成本价格
								gsp.setCargoPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][cargo_price]")));// 成本价格
								gsp.setPurchasePrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][purchase_price]")));// 成本价格
								gsp.setStoreCount(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][store_count]")));// 库存数量
								gsp.setBarCode(request.getParameter("item[" + specItemList.get(i) + "][bar_code]"));// 商品条形码
								gsp.setGoodsNo(request.getParameter("item[" + specItemList.get(i) + "][goods_No]"));// 商品编码
								gsp.setSuplierGoodsNo(request.getParameter("item[" + specItemList.get(i) + "][suplier_goods_no]"));// 商品编码
								gsp.setGoodsWeight(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][goods_weight]")));// 商品重量
								gsp.setServiceTimes(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][service_times]")));// 服务次数
								gsp.setExpiringDate(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][expiring_date]")));// 截止日期（月）
								// 保存到list中
								// 规格库存统一使用补仓功能 -kele 2016年10月9日
								// storeCount=storeCount+Integer.parseInt(request.getParameter("item["+specItemList.get(i)+"][store_count]"));
								goodsSpecPricesList.add(gsp);
							}

							List<String> items = GoodsUtil.getitemsvalue(list);

							for (int j = 0; j < items.size(); j++) {
								// 保存到商品规格图片
								GoodsSpecImage goodsSpecImage = new GoodsSpecImage();
								String gsivalue = request.getParameter("item_img[" + items.get(j) + "]");
								if (null != gsivalue && !"".equals(gsivalue)) {
									goodsSpecImage.setGoodsId(goods.getGoodsId() + "");
									goodsSpecImage.setSpecItemId(Integer.parseInt(items.get(j)));
									goodsSpecImage.setSrc(gsivalue);
									// 保存到list中
									goodsSpecImagesList.add(goodsSpecImage);
								}
							}

							goods.setGoodsSpecPricesList(goodsSpecPricesList);// 保存到商品实体bean-商品规格价格list
							goods.setGoodsSpecImagesList(goodsSpecImagesList);// 保存到商品实体bean-商品规格图片list

							// 规格库存统一使用补仓功能 -kele 2016年10月9日
							// Goods goods2=new Goods();
							// goods2.setGoodsId(Integer.parseInt(goodsId));
							// goods2.setStoreCount(storeCount);
							// goodsService.updateStorCount(goods2);

							// 商品规格【价格】list有数据才进行保存操作
							if (goodsSpecPricesList.size() > 0) {
								// 添加商品排列组合规格项数据（先删除、后添加）
								savespec(goods);
								// 修改商品规格项类型
								goods.setSpecType(specTypeId);
								updategoodstype(goods);
							}

							// 商品规格【图片】list有数据才进行保存操作
							if (goodsSpecImagesList.size() > 0) {
								savespecimg(goods);
							}
						}
					}

				}
			}

			String attrTypeId = request.getParameter("attrTypeId");

			if (!StringUtils.isEmpty(goods.getGoodsId() + "") && !StringUtils.isEmpty(attrTypeId)) {
				// goods.setGoodsId(Integer.parseInt(goodsId));
				// goods = goodsService.get(goods);

				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {

					if ("-1".equals(attrTypeId)) {
						// 删除商品属性数据
						deleteattribute(goods);
						// 修改商品规格项类型
						goods.setAttrType("0");
						updategoodstype(goods);

					} else {
						List<GoodsAttributeMappings> goodsAttributeMappingsList = new ArrayList<GoodsAttributeMappings>();// 商品的所有属性

						// 商品属性
						GoodsAttribute goodsAttribute = new GoodsAttribute();
						goodsAttribute.setTypeId(attrTypeId);
						List<GoodsAttribute> galist = goodsAttributeDao.findList(goodsAttribute);
						if (galist.size() > 0) {
							for (int i = 0; i < galist.size(); i++) {
								goodsAttribute = galist.get(i);
								GoodsAttributeMappings gam = new GoodsAttributeMappings();
								gam.setGoodsId(goods.getGoodsId() + "");
								gam.setAttrId(Integer.parseInt(goodsAttribute.getId()));
								gam.setAttrValue(request.getParameter("attr_" + goodsAttribute.getId()));
								goodsAttributeMappingsList.add(gam);
							}
							goods.setGoodsAttributeMappingsList(goodsAttributeMappingsList);
						}

						// 商品属性列表值大于0
						if (goodsAttributeMappingsList.size() > 0) {
							// 添加商品属性数据（先删除、后添加）
							saveattribute(goods);
							// 修改商品规格项类型
							goods.setAttrType(attrTypeId);
							updategoodstype(goods);
						}
					}

				}
			}

			// 插入商品技能标签
			if (!"".equals(goods.getSkill().getId())) {
				List<GoodsSkill> list = SpeArrSkillList(goods); // 获取拼接的技能标签list
				if (list.size() > 0) {
					skillDao.insertGoodsSkill(list); // 插入技能标签表
				}
			}

			// 插入设备标签
			if (!"".equals(goods.getEquipmentLabel().getId())) {
				List<GoodsEquipmentLabel> list = SpeArrGoodsEquipmentLabelList(goods); // 获取拼接的设备标签list
				if (list.size() > 0) {
					equipmentLabelDao.insertGoodsEquipmentLabel(list); // 插入设备标签表
				}
			}
			// 新增商品时同时新增到统计表中	
			addGoodsStatisticsCountData(goodId);
		} else {
			// 修改
			goods.setAttrType((goods.getAttrType() == null || "".equals(goods.getAttrType())) ? null : goods.getAttrType());
			goods.setSpecType((goods.getSpecType() == null || "".equals(goods.getSpecType())) ? null : goods.getSpecType());
			goods.setIsRecommend(goods.getIsRecommend() == null ? "0" : goods.getIsRecommend());
			goods.setIsHot(goods.getIsHot() == null ? "0" : goods.getIsHot());
			goods.setIsAppshow(goods.getIsAppshow() == null ? "0" : goods.getIsAppshow());
			goods.setIsOnSale(goods.getIsOnSale() == null ? "0" : goods.getIsOnSale());
			goods.setIsFreeShipping(goods.getIsFreeShipping() == null ? "0" : goods.getIsFreeShipping());
			goods.setIsNew(goods.getIsNew() == null ? "0" : goods.getIsNew());

			updateGoods(goods);

			// 用户商品上下架Regis缓存
			if (goods.getIsOnSale().equals("0")) {
				// 下架
				redisClientTemplate.hset(GOOD_UNSHELVE_KEY, goods.getGoodsId() + "", "0");
			} else if (goods.getIsOnSale().equals("1")) {
				// 上架
				redisClientTemplate.hdel(GOOD_UNSHELVE_KEY, goods.getGoodsId() + "");
			}

			// 保存护理流程图
			String[] goodsNurseImages = request.getParameterValues("goodsNurseImages");
			String[] goodsNurseImagesText = request.getParameterValues("goodsNurseImagesText");
			if (0 != goods.getGoodsId()) {
				if (null != goodsNurseImages && goodsNurseImages.length > 0) {
					List<GoodsImages> giList = new ArrayList<GoodsImages>();
					for (int i = 0; i < goodsNurseImages.length; i++) {
						GoodsImages goodsImg = new GoodsImages();
						goodsImg.setGoodsId(String.valueOf(goods.getGoodsId()));
						goodsImg.setImageUrl(goodsNurseImages[i]);
						goodsImg.setImageDesc((null != goodsNurseImagesText[i] && !"".equals(goodsNurseImagesText[i])? goodsNurseImagesText[i] : ""));// 商品描述
						goodsImg.setImageType(1);// 图片类型 0 是商品图 1 护理流程图
						goodsImg.setSort(i + 1 + "0");// 10、20
						giList.add(goodsImg);
					}
					goods.setGoodsImagesList(giList);
					goods.setImageType(1);// 商品图片类型[0:是商品图;1:护理流程图;]
					savegoodsimages(goods);
				} else {
					// 当没有图片时，删除原有的图片
					goods.setImageType(1);// 商品图片类型[0:是商品图;1:护理流程图;]
					deletegoodsimages(goods);
				}
			}

			String[] goodsImages = request.getParameterValues("goodsImages");

			if (goods.getGoodsId() > 0) {
				// goods.setGoodsId(Integer.parseInt(goodId));
				// goods = goodsService.get(goods);

				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {
					if (null != goodsImages && goodsImages.length > 0) {
						List<GoodsImages> giList = new ArrayList<GoodsImages>();
						for (int i = 0; i < goodsImages.length; i++) {
							GoodsImages goodsImg = new GoodsImages();
							goodsImg.setGoodsId(goods.getGoodsId() + "");
							goodsImg.setImageUrl(goodsImages[i]);
							goodsImg.setImageDesc("");// 商品描述
							goodsImg.setImageType(0);// 图片类型 0 是商品图 1 护理流程图
							goodsImg.setSort(i + 1 + "0");// 10、20
							giList.add(goodsImg);
						}
						goods.setGoodsImagesList(giList);
						goods.setImageType(0);// 商品图片类型[0:是商品图;1:护理流程图;]
						savegoodsimages(goods);
					} else {
						// 当商品相册为空时，删除以前全部数据
						goods.setImageType(0);// 商品图片类型[0:是商品图;1:护理流程图;]
						deletegoodsimages(goods);
					}

				}
			}

			// Goods goodsnew = get(goods);
			// int goodsNum = orderGoodsDao.numByGoodsId(goods.getGoodsId() +
			// "");

			// 从后台查询商品规格数据
			List<GoodsSpecPrice> gspList = findGoodsSpecPrice(goods);

			String specTypeId = request.getParameter("specTypeId");
			String spec_arr = request.getParameter("specarr");
			if (!StringUtils.isEmpty(specTypeId) && !StringUtils.isEmpty(spec_arr)) {
				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {

					if ("-1".equals(specTypeId) || null == spec_arr || "{}".equals(spec_arr)) {
						// 删除
						deletespec(goods);
						goods.setSpecType("0");
						updategoodstype(goods);

					} else {
						List<String[]> list = new ArrayList<String[]>();// 保存规格的规格项数组
						JSONObject json = JSONObject.fromObject(spec_arr);
						Iterator<?> iterator = json.keys();
						while (iterator.hasNext()) {
							String key = (String) iterator.next();
							// 保存所需选择的规格项的数组
							String[] value = (String[]) json.getString(key).replace("[", "").replace("]", "").split(",");
							list.add(value);
						}

						List<String> specItemList = new ArrayList<String>();// 获取所有排列组合值
						// 判断规格项有值时，才进行查询
						if (list.size() > 0) {
							// 规格项数组，排列组合
							String str = "";
							GoodsUtil.goodsSpecItem(list, list.get(0), str, specItemList, goodsSpecItemService);
						}
						int totalNum = 0;
						// 判断随机排列活的组合值
						if (specItemList.size() > 0) {
							List<GoodsSpecPrice> goodsSpecPricesList = new ArrayList<GoodsSpecPrice>();
							List<GoodsSpecImage> goodsSpecImagesList = new ArrayList<GoodsSpecImage>();
							// 创建两个集合,判断商品规格分类是否改变(取SpecKey进行比对),当改变就修改,否则就不修改
							List<String> goodsSpecPricesList1 = new ArrayList<String>();
							List<String> goodsSpecPricesList2 = new ArrayList<String>();

							for (GoodsSpecPrice gsp : gspList) {
								goodsSpecPricesList1.add(gsp.getSpecKey());
							}

							// int storeCount=0; //库存
							// 遍历组合值，拼接form表单的值，并循环获取
							for (int i = 0; i < specItemList.size(); i++) {
								GoodsSpecPrice gsp = new GoodsSpecPrice();
								gsp.setGoodsId(goods.getGoodsId() + "");// 商品id
								gsp.setSpecKey(specItemList.get(i));// 规格键
								gsp.setSpecKeyName(request.getParameter("item[" + specItemList.get(i) + "][spec_name]"));// 规格名称
								gsp.setSpecKeyValue(request.getParameter("item[" + specItemList.get(i) + "][key_name]"));// 规格键名中文
								gsp.setPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][price]")));// 优惠价格
								gsp.setMarketPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][market_price]")));// 市场价格
								gsp.setCostPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][cost_price]")));// 成本价格
								gsp.setCargoPrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][cargo_price]")));// 成本价格
								gsp.setPurchasePrice(Double.valueOf(request.getParameter("item[" + specItemList.get(i) + "][purchase_price]")));// 成本价格
								gsp.setStoreCount(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][store_count]")));// 库存数量
								gsp.setBarCode(request.getParameter("item[" + specItemList.get(i) + "][bar_code]"));// 商品条形码
								gsp.setGoodsNo(request.getParameter("item[" + specItemList.get(i) + "][goods_No]"));// 商品编码
								gsp.setSuplierGoodsNo(request.getParameter("item[" + specItemList.get(i) + "][suplier_goods_no]"));// 商品编码
								gsp.setGoodsWeight(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][goods_weight]")));// 商品重量
								gsp.setServiceTimes(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][service_times]")));// 服务次数
								gsp.setExpiringDate(Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][expiring_date]")));// 截止日期（月）
								// 保存到list中
								// 规格库存统一使用补仓功能 -kele 2016年10月9日
								// storeCount=storeCount+Integer.parseInt(request.getParameter("item["+specItemList.get(i)+"][store_count]"));
								totalNum = totalNum + Integer.parseInt(request.getParameter("item[" + specItemList.get(i) + "][store_count]"));
								goodsSpecPricesList.add(gsp);
								goodsSpecPricesList2.add(specItemList.get(i));
							}
							//判断商品规格是否更改,更改就修改,如果没有,则直接跳过
							boolean flag = (goodsSpecPricesList1.size() == goodsSpecPricesList2.size()) && goodsSpecPricesList1.containsAll(goodsSpecPricesList2);
							if (!flag) {
								List<String> items = GoodsUtil.getitemsvalue(list);

								for (int j = 0; j < items.size(); j++) {
									// 保存到商品规格图片
									GoodsSpecImage goodsSpecImage = new GoodsSpecImage();
									String gsivalue = request.getParameter("item_img[" + items.get(j) + "]");
									if (null != gsivalue && !"".equals(gsivalue)) {
										goodsSpecImage.setGoodsId(goods.getGoodsId() + "");
										goodsSpecImage.setSpecItemId(Integer.parseInt(items.get(j)));
										goodsSpecImage.setSrc(gsivalue);
										// 保存到list中
										goodsSpecImagesList.add(goodsSpecImage);
									}
								}

								goods.setGoodsSpecPricesList(goodsSpecPricesList);// 保存到商品实体bean-商品规格价格list
								goods.setGoodsSpecImagesList(goodsSpecImagesList);// 保存到商品实体bean-商品规格图片list

								// 规格库存统一使用补仓功能 -kele 2016年10月9日
								// Goods goods2=new Goods();
								// goods2.setGoodsId(Integer.parseInt(goodsId));
								// goods2.setStoreCount(storeCount);
								// goodsService.updateStorCount(goods2);

								// 商品规格【价格】list有数据才进行保存操作
								if (goodsSpecPricesList.size() > 0) {
									// 添加商品排列组合规格项数据（先删除、后添加）
									savespec(goods);
									// 修改商品规格项类型
									goods.setSpecType(specTypeId);
									updategoodstype(goods);
									updateGoodsSpecCache(goods.getGoodsId(),goodsSpecPricesList1);	// 更新缓存
									Goods g = new Goods();
									g.setGoodsId(goods.getGoodsId());
									g.setTotalStore(0);
									g.setStoreCount(0);
									goodsDao.updateStorCount(g);					// 修改商品总库存、剩余库存为0 
								}

								// 商品规格【图片】list有数据才进行保存操作
								if (goodsSpecImagesList.size() > 0) {
									savespecimg(goods);
								}

							}

						}
					}

				}
			}

			String attrTypeId = request.getParameter("attrTypeId");
			if (!StringUtils.isEmpty(goods.getGoodsId() + "") && !StringUtils.isEmpty(attrTypeId)) {
				// goods.setGoodsId(Integer.parseInt(goodsId));
				// goods = goodsService.get(goods);

				// 获取商品名称，判断是否根据商品ID查询到商品信息
				if (!StringUtils.isEmpty(goods.getGoodsName())) {

					if ("-1".equals(attrTypeId)) {
						// 删除商品属性数据
						deleteattribute(goods);
						// 修改商品规格项类型
						goods.setAttrType("0");
						updategoodstype(goods);

					} else {
						List<GoodsAttributeMappings> goodsAttributeMappingsList = new ArrayList<GoodsAttributeMappings>();// 商品的所有属性

						// 商品属性
						GoodsAttribute goodsAttribute = new GoodsAttribute();
						goodsAttribute.setTypeId(attrTypeId);
						List<GoodsAttribute> galist = goodsAttributeDao.findList(goodsAttribute);
						if (galist.size() > 0) {
							for (int i = 0; i < galist.size(); i++) {
								goodsAttribute = galist.get(i);
								GoodsAttributeMappings gam = new GoodsAttributeMappings();
								gam.setGoodsId(goods.getGoodsId() + "");
								gam.setAttrId(Integer.parseInt(goodsAttribute.getId()));
								gam.setAttrValue(request.getParameter("attr_" + goodsAttribute.getId()));
								goodsAttributeMappingsList.add(gam);
							}
							goods.setGoodsAttributeMappingsList(goodsAttributeMappingsList);
						}

						// 商品属性列表值大于0
						if (goodsAttributeMappingsList.size() > 0) {
							// 添加商品属性数据（先删除、后添加）
							saveattribute(goods);
							// 修改商品规格项类型
							goods.setAttrType(attrTypeId);
						}
					}

				}
			}
			// 更新商品技能标签
			List<GoodsSkill> skillList = SpeArrSkillList(goods); // 获得技能标签list
			skillDao.deleteGoodsSkill(goods.getGoodsId());
			if (skillList.size() > 0) {
				skillDao.insertGoodsSkill(skillList);
			}

			// 更新商品设备标签
			List<GoodsEquipmentLabel> goodsEquipmentLabelList = SpeArrGoodsEquipmentLabelList(goods); // 获得设备标签list
			equipmentLabelDao.deleteGoodsEquipmentLabel(goods.getGoodsId());
			if (goodsEquipmentLabelList.size() > 0) {
				equipmentLabelDao.insertGoodsEquipmentLabel(goodsEquipmentLabelList);
			}
		}
	}

	/**
	 * 复制商品
	 * 
	 * @param goods
	 * @return
	 */
	public void saveCoypGoods(Goods goods) {
		// 根据商品ID，查询商品信息
		// goods.setGoodsId(goods.getId());
		goods = get(goods);
		// 查询商品相册、商品护理流程 begin
		goods.setGoodsImagesList(findGoodsImages(goods));
		// 根据商品ID，查询所有规格价格信息
		List<GoodsSpecPrice> gspList = findGoodsSpecPrice(goods);
		// 根据商品id，查询出商品规格图片
		List<GoodsSpecImage> goodsSpecImagesList = findspecimgbyid(goods);
		// 根据商品ID，查询所有属性值内容
		List<GoodsAttributeMappings> gamList = findGoodsAttributeMappings(goods);

		if (goods != null) {
			int num = dao.selectByMaxSort();
			goods.setGoodsId(0);
			goods.setActionType("0");
			goods.setActionId(0);
			goods.setSort(num + 1);
			goods.setStoreCount(0);
			// 保存
			dao.insert(goods);
			int goodId = goods.getGoodsId();
			if (0 == goodId) {
				try {
					throw new Exception("保存失败");
				} catch (Exception e) {

				}
			}
			// 新增商品时同时新增到统计表中	
			addGoodsStatisticsCountData(goodId);
		}
		if (goods.getGoodsImagesList().size() > 0) {
			List<GoodsImages> imglist = goods.getGoodsImagesList();
			List<GoodsImages> list = new ArrayList<GoodsImages>();
			for (int i = 0; i < imglist.size(); i++) {
				GoodsImages goodsImg = new GoodsImages();
				goodsImg.setGoodsId(goods.getGoodsId() + "");
				goodsImg.setImageUrl(imglist.get(i).getImageUrl());
				goodsImg.setImageDesc(imglist.get(i).getImageDesc());// 商品描述
				goodsImg.setImageType(imglist.get(i).getImageType());// 图片类型 0是商品图 ， 1 护理流程图
				goodsImg.setSort(imglist.get(i).getSort());// 10、20
				list.add(goodsImg);
			}
			goods.setGoodsImagesList(list);
			savegoodsimages(goods);
		}
		if (gspList.size() > 0) {
			List<GoodsSpecPrice> goodspriceList = new ArrayList<GoodsSpecPrice>();
			for (GoodsSpecPrice goodsSpecPrice : gspList) {
				GoodsSpecPrice goodsSpec = goodsSpecPrice;
				goodsSpec.setGoodsId(goods.getGoodsId() + "");
				goodsSpec.setStoreCount(0);
				goodspriceList.add(goodsSpec);
			}
			goods.setGoodsSpecPricesList(goodspriceList); // 保存到商品实体bean-商品规格价格list

			// 添加商品排列组合规格项数据（先删除、后添加）
			savespec(goods);
			// 修改商品规格项类型
			// goods.setSpecType(specTypeId);
			updategoodstype(goods);

		}
		if (goodsSpecImagesList.size() > 0) {
			List<GoodsSpecImage> goodsimgelist = new ArrayList<GoodsSpecImage>();
			// 商品规格【图片】list有数据才进行保存操作
			for (GoodsSpecImage goodsSpecImage : goodsSpecImagesList) {
				GoodsSpecImage goodsImage = goodsSpecImage;
				goodsImage.setGoodsId(goods.getGoodsId() + "");
				goodsimgelist.add(goodsImage);
			}
			goods.setGoodsSpecImagesList(goodsimgelist);// 保存到商品实体bean-商品规格图片list
			savespecimg(goods);
		}
		if (gamList.size() > 0) {
			List<GoodsAttributeMappings> goodsAttributeList = new ArrayList<GoodsAttributeMappings>();
			for (GoodsAttributeMappings goodsAttributeMappings : gamList) {
				GoodsAttributeMappings goodsAttribut = goodsAttributeMappings;
				goodsAttribut.setGoodsId(goods.getGoodsId() + "");
				goodsAttributeList.add(goodsAttribut);
			}
			goods.setGoodsAttributeMappingsList(goodsAttributeList);
			saveattribute(goods);
		}
	}

	/**
	 * 商品-通用信息-修改
	 * 
	 * @param goods
	 * @return
	 */
	public int updateGoods(Goods goods) {
		int result = dao.update(goods);
		if (1 == result) {
			return 1;
		} else {
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 更新库存
	 * 
	 * @param goods
	 * @return
	 */
	public int updateStorCount(Goods goods) {
		int num = dao.updateStorCount(goods);
		if (1 == num) {
			return 1;
		} else {
			try {
				throw new Exception("添加库存失败");
			} catch (Exception e) {

			}
		}
		return num;
	}

	/**
	 * 修改商品类型（规格与属性的类型）
	 * 
	 * @param goods
	 * @return
	 */
	public int updategoodstype(Goods goods) {
		int result = goodsDao.updategoodstype(goods);
		if (1 == result) {
			return 1;
		} else {
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 商品-是否显示（上架、推荐、新品、热卖、app显示）
	 * 
	 * @param goods
	 * @return
	 */
	public int updateisyesno(Goods goods) {
		int result = goodsDao.updateisyesno(goods);
		if (1 == result) {
			return 1;
		} else {
			try {
				throw new Exception("修改失败");
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 根据商品ID，查询商品图片 0-商品相册；1-商品护理流程
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsImages> findGoodsImages(Goods goods) {
		return goodsDao.findGoodsImages(goods);
	}

	/**
	 * 根据商品ID，查询商品规格价格项
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsSpecPrice> findGoodsSpecPrice(Goods goods) {
		return goodsDao.findGoodsSpecPrice(goods);
	}

	/**
	 * 根据商品ID，查询商品规格价格项
	 * 
	 * @param goods
	 * @return
	 */
	public GoodsSpecPrice findByGoodsSpecPrice(GoodsSpecPrice goodsSpecPrice) {
		return goodsDao.findByGoodsSpecPrice(goodsSpecPrice);
	}

	/**
	 * 根据商品规格specKey，修改商品规格价格项
	 * 
	 * @param goodsSpecPrice
	 * @return
	 */
	public int updateByGoodsSpecPrice(GoodsSpecPrice goodsSpecPrice) {
		int result = goodsDao.updateByGoodsSpecPrice(goodsSpecPrice);
		if (1 == result) {
			return 1;
		} else {
			try {
				throw new Exception("根据商品ID，修改商品规格价格项：失败");
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 根据商品ID，查询商品属性
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsAttributeMappings> findGoodsAttributeMappings(Goods goods) {
		return goodsDao.findGoodsAttributeMappings(goods);
	}

	/**
	 * 查询所有功效
	 * 
	 * @return
	 */
	public List<Effect> findEffect(Effect effect) {
		return dao.findEffect(effect);
	}

	/**
	 * 分页查询功效
	 * 
	 * @param page
	 * @param effect
	 * @return
	 */
	public Page<Effect> findEffectPage(Page<Effect> page, Effect effect) {
		effect.setPage(page);
		page.setList(dao.findEffect(effect));
		return page;
	}

	/**
	 * 保存功效
	 * 
	 * @param effect
	 */
	public void saveEffect(Effect effect) {
		dao.saveEffect(effect);
	}

	/**
	 * 删除功效
	 * 
	 * @param effect
	 */
	public void deleteEffect(Effect effect) {
		dao.deleteEffect(effect);
	}

	/**
	 * 修改功效
	 * 
	 * @param effect
	 */
	public void updateEffect(Effect effect) {
		dao.updateEffect(effect);
	}

	/**
	 * 保存商品相册
	 * 
	 * @param goods
	 * @return
	 */
	public void savegoodsimages(Goods goods) {
		goodsDao.deletegoodsimages(goods);// 先删除之前的商品相册项内容
		goodsDao.savegoodsimages(goods);// 再添加新的商品相册内容
	}

	/**
	 * 删除商品相册项内容
	 * 
	 * @param goods
	 */
	public void deletegoodsimages(Goods goods) {
		goodsDao.deletegoodsimages(goods);// 先删除之前的商品相册项内容
	}

	/**
	 * 保存商品规格项
	 * 
	 * @param goods
	 * @return
	 */
	public void savespec(Goods goods) {
		goodsDao.updatespec(goods);// 先逻辑删除之前的商品规格项内容
		goodsDao.savespec(goods);// 再添加新的商品规格项内容
	}

	/**
	 * 先删除之前的商品规格项内容
	 * 
	 * @param goods
	 */
	public void deletespec(Goods goods) {
		goodsDao.deletespec(goods);
	}

	/**
	 * 保存商品属性
	 * 
	 * @param goods
	 */
	public void saveattribute(Goods goods) {
		goodsDao.deleteattribute(goods);// 先删除之前保存的商品属性
		goodsDao.saveattribute(goods);// 再添加新的商品属性
	}

	/**
	 * 先删除之前保存的商品属性
	 * 
	 * @param goods
	 */
	public void deleteattribute(Goods goods) {
		goodsDao.deleteattribute(goods);
	}

	/**
	 * 保存商品规格图片
	 * 
	 * @param goods
	 */
	public void savespecimg(Goods goods) {
		goodsDao.deletespecimg(goods);// 先删除之前保存的商品规格图片
		goodsDao.savespecimg(goods);// 再添加新的商品规格图片
	}

	/**
	 * 根据商品ID，查询商品规格图片
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsSpecImage> findspecimgbyid(Goods goods) {
		return goodsDao.findspecimgbyid(goods);
	}

	/**
	 * 根据分类查询商品
	 * 
	 * @param goodsCategory
	 * @return
	 */
	public List<Goods> list(Goods goods) {
		return goodsDao.goodslist(goods);
	}

	public int modifyStoreCount(Map<String, Object> map) {
		return this.goodsDao.modifyStoreCount(map);
	}

	public int modifySpecStoreCount(Map<String, Object> map) {
		return goodsSpecPriceDao.modifySpecStoreCount(map);
	}

	public List<StoreVo> queryStoreCount() {
		return this.goodsDao.queryStoreCount();
	}

	public List<GoodsSpecPrice> querySpecsPrices() {
		return goodsSpecPriceDao.querySpecsPrices();
	}

	public List<GoodsCollect> queryAllGoodsCollect() {
		return this.goodsDao.queryAllGoodsCollect();
	}

	public List<Integer> queryAllUnshelve() {
		return this.goodsDao.queryAllUnshelve();
	}

	/**
	 * 定时器执行商品上下架
	 * 
	 * @param goods
	 * @return
	 */
	public int updateGoodsStauts(Goods goods) {
		return goodsDao.updateGoodsStauts(goods);
	}

	/**
	 * 根据多分类查询数据
	 * 
	 * @param cateId
	 * @return
	 */
	public List<Goods> findByCateList(String cateId) {
		return goodsDao.findByCateList(cateId);
	}

	/**
	 * 技能标签拼接
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsSkill> SpeArrSkillList(Goods goods) {
		List<GoodsSkill> list = new ArrayList<GoodsSkill>();
		if (goods.getSkill().getId() != null) {
			String spec = goods.getSkill().getId();
			if (!"".equals(spec) && spec != null) {
				String[] arry = spec.split(",");
				for (int i = 0; i < arry.length; i++) {
					GoodsSkill usspe = new GoodsSkill();
					usspe.setGoodsId(goods.getGoodsId());
					usspe.setSkillId(Integer.valueOf(arry[i]));
					list.add(usspe);
				}
			}

		}
		return list;
	}

	/**
	 * 商品设备标签拼接
	 * 
	 * @param goods
	 * @return
	 */
	public List<GoodsEquipmentLabel> SpeArrGoodsEquipmentLabelList(Goods goods) {
		List<GoodsEquipmentLabel> list = new ArrayList<GoodsEquipmentLabel>();
		if (goods.getEquipmentLabel().getId() != null) {
			String spec = goods.getEquipmentLabel().getId();
			if (!"".equals(spec) && spec != null) {
				String[] arry = spec.split(",");
				for (int i = 0; i < arry.length; i++) {
					GoodsEquipmentLabel usspe = new GoodsEquipmentLabel();
					usspe.setGoodsId(goods.getGoodsId());
					usspe.setLabelId(Integer.valueOf(arry[i]));
					list.add(usspe);
				}
			}

		}
		return list;
	}

	/**
	 * 根据商品id获取该商品的技能标签
	 * 
	 * @param goodsId
	 * @return
	 */
	public Skill getSkillByGoodsId(int goodsId) {
		String id = "";
		String name = "";
		Skill skill = new Skill();
		List<Skill> list = skillDao.findSkillListByGoodsId(goodsId);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				id = id + list.get(i).getSkillId() + ",";
				name = name + list.get(i).getName() + ",";
			}
			id = id.substring(0, id.lastIndexOf(","));
			name = name.substring(0, name.lastIndexOf(","));
			skill.setId(id);
			skill.setName(name);
		}
		return skill;
	}

	/**
	 * 根据商品id获取该商品的设备标签
	 * 
	 * @param goodsId
	 * @return
	 */
	public EquipmentLabel getEquipmentLabelByGoodsId(int goodsId) {
		String id = "";
		String name = "";
		EquipmentLabel equipmentLabel = new EquipmentLabel();
		List<EquipmentLabel> list = equipmentLabelDao.findEquipmentLabelListByGoodsId(goodsId);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				id = id + list.get(i).getEquipmentLabelId() + ",";
				name = name + list.get(i).getName() + ",";
			}
			id = id.substring(0, id.lastIndexOf(","));
			name = name.substring(0, name.lastIndexOf(","));
			equipmentLabel.setId(id);
			equipmentLabel.setName(name);
		}
		return equipmentLabel;
	}
	public void updateGoodsSpecCache(int goodsId,List<String> goodsSpecPricesList){
		redisClientTemplate.set(GOODSSTORE+goodsId, "0");
		for (int i = 0; i < goodsSpecPricesList.size(); i++) {
			redisClientTemplate.srem(GOODS_SPECPRICE_HASH, goodsId+"#"+goodsSpecPricesList.get(i));
			redisClientTemplate.del(SPECPRICE+goodsId+"#"+goodsSpecPricesList.get(i));
		}
	}
	// 新增商品时同时新增到统计表中	
	// 1、新增商品时调用	2、复制商品时调用
	public void addGoodsStatisticsCountData(int goodId){
		GoodsStatisticsCountData goodsStatisticsCountData = new GoodsStatisticsCountData();
		goodsStatisticsCountData.setGoodsId(String.valueOf(goodId));
		goodsStatisticsCountData.setEvaluationScore((float)5);
		goodsStatisticsCountData.setEvaluationCount(0);
		goodsStatisticsCountData.setBuyCount(0);
		goodsStatisticsDao.addGoodsStatisticsCountData(goodsStatisticsCountData);
	}
	
}
