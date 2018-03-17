package com.training.modules.ec.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.common.utils.StringUtils;
import com.training.modules.ec.dao.GoodsCardDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.dao.GoodsSpecPriceDao;
import com.training.modules.ec.dao.GoodsStatisticsDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsCard;
import com.training.modules.ec.entity.GoodsImages;
import com.training.modules.ec.entity.GoodsSpecImage;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.ec.entity.GoodsStatisticsCountData;
import com.training.modules.ec.utils.GoodsUtil;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.utils.ParametersFactory;

import net.sf.json.JSONObject;

/**
 * 卡项-Service层
 * 
 * @author 土豆
 * @version 2017-7-26
 */
@Service
@Transactional(readOnly = false)
public class GoodsCardService extends CrudService<GoodsCardDao, GoodsCard> {

	
	public static final String GOOD_UNSHELVE_KEY = "GOOD_UNSHELVE_KEY"; // 商品下架
	public static final String GOODSSTORE = "GOODSSTORE_";	// 商品总库存
	public static final String SPECPRICE = "SPECPRICE_";	// 商品对应的规格
	public static final String GOODS_SPECPRICE_HASH = "GOODS_SPECPRICE_HASH";	//所有商品id集合
	
	@Autowired
	private GoodsCardDao goodsCardDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private GoodsStatisticsDao goodsStatisticsDao;
	@Autowired
	private GoodsSpecPriceDao goodsSpecPriceDao;
	@Autowired
	private GoodsSpecItemService goodsSpecItemService;
	
	/**
	 * 分页展示所有信息
	 * 
	 * @param page
	 * @param trainLessonss
	 * @return
	 */
	public Page<GoodsCard> findGoodsCard(Page<GoodsCard> page, GoodsCard goodsCard) {
		goodsCard.setPage(page);
		page.setList(dao.findList(goodsCard));
		return page;
	}
	
	/**
	 * 根据商品ID查询价格
	 * @param goodsCard
	 * @return
	 */
	public GoodsCard findByGoodsCard(GoodsCard goodsCard) {
		return goodsCardDao.findByGoodsCard(goodsCard);
	}
	/**
	 * 根据套卡ID查询对于的商品
	 * @param goods
	 * @return
	 */
	/*public List<Goods> findGoodsList(Goods goods) {
		return goodsCardDao.findGoodsList(goods);
	}*/

	/**
	 * 保存卡项信息
	 * @param goods
	 * @param request
	 */
	public void saveGoods(Goods goods, HttpServletRequest request) {
		// 商品套卡详情转换
		goods.setGoodsContent(HtmlUtils.htmlUnescape(goods.getGoodsContent()));
		
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
		goodsDao.insert(goods);
		
		//自媒体每天美耶商品信息同步
		JSONObject jsonObject = new JSONObject();
		String updateMtmyGoodInfo = ParametersFactory.getMtmyParamValues("mtmy_updateMtmyGoodInfo");	
		logger.info("##### web接口路径:"+updateMtmyGoodInfo);	         
		String parpm = "{\"goodsId\":\""+goods.getGoodsId()+"\",\"goodsName\":\""+goods.getGoodsName()+"\",\"marketPrice\":\""+goods.getMarketPrice()+"\",\"shopPrice\":\""+goods.getShopPrice()+"\",\"advancePrice\":\""+goods.getAdvancePrice()+"\",\"goodsRemark\":\""+goods.getGoodsRemark()+"\",\"actionType\":\""+goods.getActionType()+"\",\"isReal\":\""+goods.getIsReal()+"\",\"integral\":\""+goods.getIntegral()+"\",\"originalImg\":\""+goods.getOriginalImg()+"\",\"isOpen\":\""+goods.getGoodsIsOpen()+"\",\"franchiseeId\":\""+goods.getFranchiseeId()+"\"}";
		String url=updateMtmyGoodInfo;
		String result = WebUtils.postMediaObject(parpm, url);
		jsonObject = JSONObject.fromObject(result);
		logger.info("##### web接口返回数据：code:"+jsonObject.get("code")+",msg:"+jsonObject.get("msg")+",data:"+jsonObject.get("data"));
		
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
		
		String[] goodsImages = request.getParameterValues("goodsImages");
		
		if (goods.getGoodsId() > 0) {

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
		
		//先判断是套卡还是通用卡
		if(goods.getIsReal().equals("2")){
	
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
			//保存卡项子项表 begin
			GoodsCard goodsCard = new GoodsCard();
			
			goodsCard.setCardId(goods.getGoodsId());						//卡项ID
			
			List<Integer> goodsIds = goods.getGoodsIds();					//商品ID 集合
			List<Integer> goodsNums = goods.getGoodsNums();					//次（个）数集合
			List<Double> marketPrices = goods.getMarketPrices();			//市场单价集合
			List<Double> prices = goods.getPrices();						//优惠价集合
			List<Double> totalMarketPrices = goods.getTotalMarketPrices();	//市场价合计集合
			List<Double> totalPrices = goods.getTotalPrices();				//优惠价合计集合
			for (Integer i = 0; i < goodsIds.size(); i++) {
				Integer goodsId = goodsIds.get(i);					//商品id
				Integer goodsNum = goodsNums.get(i);				//次（个）数
				Double marketPrice = marketPrices.get(i);			//市场价
				Double price = prices.get(i);						//优惠价
				Double totalMarketPrice = totalMarketPrices.get(i); //市场价合计
				Double totalPrice = totalPrices.get(i);				//优惠价合计
				//通过商品id查询商品的商品名称,商品原始图,服务时长（虚拟商品）,是否为实物（0：实物；1：虚拟；）,
				Goods newgoods = goodsDao.findGoodsBygoodsId(goodsId);
				
				goodsCard.setGoodsId(newgoods.getGoodsId());
				goodsCard.setGoodsName(newgoods.getGoodsName());
				goodsCard.setOriginalImg(newgoods.getOriginalImg());
				goodsCard.setGoodsNum(goodsNum);
				goodsCard.setServiceMin(newgoods.getServiceMin());
				goodsCard.setIsReal(newgoods.getIsReal());
				goodsCard.setMarketPrice(marketPrice);
				goodsCard.setPrice(price);
				goodsCard.setTotalMarketPrice(totalMarketPrice);
				goodsCard.setTotalPrice(totalPrice);
				
				goodsCardDao.insert(goodsCard);//保存卡项
			}
			//保存卡项子项表 end 
			
			//添加套卡,默认规格
			//保存默认规格 begin    
			GoodsSpecPrice gsp = new GoodsSpecPrice();
			gsp.setGoodsId(goodId+"");
			gsp.setSpecKey("0");
			gsp.setSpecKeyName("默认规格");
			gsp.setSpecKeyValue("默认规格");
			gsp.setCostPrice(goods.getCostPrice());
			gsp.setMarketPrice(goods.getMarketPrice());
			gsp.setPrice(goods.getShopPrice());
			gsp.setStoreCount(0);
			gsp.setExpiringDate(999);
			gsp.setDelFlag("0");
			
			goodsSpecPriceDao.savespec(gsp);
			//保存默认规格 end 
			
			// 新增商品时同时新增到统计表中	
			addGoodsStatisticsCountData(goodId);
	
		}else if(goods.getIsReal().equals("3")){
			
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
			//保存卡项子项表 begin
			GoodsCard goodsCard = new GoodsCard();
			
			goodsCard.setCardId(goods.getGoodsId());						//卡项ID
			
			List<Integer> goodsIds = goods.getGoodsIds();					//商品ID 集合
			List<Integer> goodsNums = goods.getGoodsNums();					//数量
			for (Integer i = 0; i < goodsIds.size(); i++) {
				Integer goodsId = goodsIds.get(i);					//商品id
				Integer goodsNum = goodsNums.get(i);				//数量
				//通过商品id查询商品的商品名称,商品原始图,服务时长（虚拟商品）,是否为实物（0：实物；1：虚拟；）,
				Goods newgoods = goodsDao.findGoodsBygoodsId(goodsId);
				
				goodsCard.setGoodsId(newgoods.getGoodsId());
				goodsCard.setGoodsName(newgoods.getGoodsName());
				goodsCard.setOriginalImg(newgoods.getOriginalImg());
				goodsCard.setGoodsNum(goodsNum);
				goodsCard.setServiceMin(newgoods.getServiceMin());
				goodsCard.setIsReal(newgoods.getIsReal());
				goodsCard.setMarketPrice(0);
				goodsCard.setPrice(0);
				goodsCard.setTotalMarketPrice(0);
				goodsCard.setTotalPrice(0);
				
				goodsCardDao.insert(goodsCard);//保存卡项
			}
			//保存卡项子项表 end 
		}
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
	/**
	 * 先删除之前的商品规格项内容
	 * 
	 * @param goods
	 */
	public void deletespec(Goods goods) {
		goodsDao.deletespec(goods);
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
	 * 保存商品规格图片
	 * 
	 * @param goods
	 */
	public void savespecimg(Goods goods) {
		goodsDao.deletespecimg(goods);// 先删除之前保存的商品规格图片
		goodsDao.savespecimg(goods);// 再添加新的商品规格图片
	}
	
	/**
	 * 查询卡项的子项信息
	 * @param cardId
	 * @return
	 */
	public List<GoodsCard> selectSonsByCardId(int cardId){
		return goodsCardDao.selectSonsByCardId(cardId);
	}
	
	
}
