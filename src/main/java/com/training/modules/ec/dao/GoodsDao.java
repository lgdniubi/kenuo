package com.training.modules.ec.dao;

import java.util.List;
import java.util.Map;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.ec.entity.Effect;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsAttributeMappings;
import com.training.modules.ec.entity.GoodsImages;
import com.training.modules.ec.entity.GoodsSpecImage;
import com.training.modules.ec.entity.GoodsSpecPrice;
import com.training.modules.quartz.entity.GoodsCollect;
import com.training.modules.quartz.entity.StoreVo;

/**
 * 商品-DAO层
 * @author kele
 * @version 2016-6-22
 */
@MyBatisDao
public interface GoodsDao extends CrudDao<Goods>{

	/**
	 * 修改商品类型（规格与属性的类型）
	 * @param goods
	 * @return
	 */
	public int updategoodstype(Goods goods);
	
	/**
	 * 保存商品相册
	 * @param goods
	 */
	public void savegoodsimages(Goods goods);
	
	/**
	 * 删除商品相册
	 * @param goods
	 */
	public void deletegoodsimages(Goods goods);
	
	/**
	 * 保存商品规格项
	 * @param goods
	 */
	public void savespec(Goods goods);
	
	/**
	 * 删除商品规格
	 * @param goods
	 * @return
	 */
	public void deletespec(Goods goods);
	
	/**
	 * 逻辑删除商品规格
	 * @param goods
	 */
	public void updatespec(Goods goods);
	
	/**
	 * 保存商品属性
	 * @param goods
	 */
	public void saveattribute(Goods goods);
	
	/**
	 * 删除商品属性
	 * @param goods
	 * @return
	 */
	public void deleteattribute(Goods goods);
	
	/**
	 * 商品-是否显示（上架、推荐、新品、热卖、app显示）
	 * @param goods
	 * @return
	 */
	public int updateisyesno(Goods goods);
	
	/**
	 * 根据商品ID，查询商品图片
	 * 0-商品相册；1-商品护理流程
	 * @param goods
	 * @return
	 */
	public List<GoodsImages> findGoodsImages(Goods goods);
	
	/**
	 * 根据商品ID，查询商品规格价格项
	 * @param goods
	 * @return
	 */
	public List<GoodsSpecPrice> findGoodsSpecPrice(Goods goods);
	
	/**
	 * 根据商品规格specKey，查询商品规格价格
	 * @param goods
	 * @return
	 */
	public GoodsSpecPrice findByGoodsSpecPrice(GoodsSpecPrice goodsSpecPrice);
	
	/**
	 * 商品规格specKey，修改商品规格价格
	 * @param goodsspecprice
	 * @return
	 */
	public int updateByGoodsSpecPrice(GoodsSpecPrice goodsSpecPrice);
	
	/**
	 * 根据商品ID，查询商品属性
	 * @param goods
	 * @return
	 */
	public List<GoodsAttributeMappings> findGoodsAttributeMappings(Goods goods);
	
	
	/**
	 * 根据商品ID，查询商品规格图片
	 * @param goods
	 * @return
	 */
	public List<GoodsSpecImage> findspecimgbyid(Goods goods);
	
	/**
	 * 保存商品规格图片
	 * @param goods
	 */
	public void savespecimg(Goods goods);
	
	/**
	 * 删除商品规格图片
	 * @param goods
	 */
	public void deletespecimg(Goods goods);
	
	/**
	 * 查询所有功效
	 * @return
	 */
	public List<Effect> findEffect(Effect effect);
	/**
	 * 保存功效
	 * @param effect
	 */
	public void saveEffect(Effect effect);
	/**
	 * 删除功效
	 * @param effect
	 */
	public void deleteEffect(Effect effect);
	/**
	 * 修改功效
	 * @param effect
	 */
	public void updateEffect(Effect effect);
	/**
	 * 根据分类查询商品	
	 * @param id
	 * @return
	 */
	public List<Goods> goodslist(Goods goods);
	/**
	 * 根据id 获取商品信息
	 * @param id
	 * @return
	 */
	public Goods getgoods(String id);
	
	public int modifyStoreCount(Map<String, Object> map);
	
	public List<StoreVo> queryStoreCount();
	
	public List<GoodsCollect> queryAllGoodsCollect();
	/**
	 * 获取下架的商品id
	 * @return
	 */
	public List<Integer> queryAllUnshelve();
	/**
	 * 更新库存
	 * @param goods
	 * @return
	 */
	
	public int updateStorCount(Goods goods);
	
	/**
	 * 添加活动商品
	 * @param goods
	 * @return
	 */
	public int updateActionId(Goods goods);
	
	/**
	 * 查询活动下的所有商品
	 * @param actionId
	 * @return
	 */
	public List<Goods> ActionGoodslist(int actionId);
	/**
	 * 商品上下架
	 * @param goods
	 * @return
	 */
	public int updateGoodsStauts(Goods goods);
	
	/**
	 * 根据多分类查询数据
	 * @param cateId
	 * @return
	 */
	public List<Goods> findByCateList(String cateId);
	
	public int updateTotalstore(int goodsId);
	/**
	 * 排序最大值
	 * @return
	 */
	public int selectByMaxSort();
	
	/**
	 * 通过商品ID查询卡项中需要数据
	 * @param goodsId
	 */
	public Goods findGoodsBygoodsId(Integer goodsId);
	
	/**
	 * 根据goods_ids查询多个商品
	 * @param goodsIds
	 * @return
	 */
	public List<Goods> selectGoodsList(String goodsIds);
	
	/**
	 * 城市异价中根据条件查询能参加异价的商品
	 * @return
	 */
	public List<Goods> queryGoodsCanRatio(Goods goods);
}
