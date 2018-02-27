package com.training.modules.ec.entity;

import com.training.common.persistence.DataEntity;

/**
 * 热门主题中间表实体类
 * @author xiaoye  2017年9月13日
 *
 */
public class ThemeMapping extends DataEntity<ThemeMapping>{

	private static final long serialVersionUID = 1L;
	
	private int themeMappingId;      //主键ID
	private int themeId;             //主题ID
	private int type;                //类型(0:文章，1:商品)
	private int someId;              //文章/商品ID
	
	private Goods goods;            //热门主题对应的商品
	private ArticleRepository articleRepository;     //热门主题对应的文章
	
	private String newGoodsId;              //商品id，用来查询用的
	private String newGoodsName;              //商品id，用来查询用的
	private String newGoodsCategoryId;      //商品分类id，用来查询用的
	private String newGoodsCategoryName;    //商品分类名称，回显用的

	public int getThemeMappingId() {
		return themeMappingId;
	}

	public void setThemeMappingId(int themeMappingId) {
		this.themeMappingId = themeMappingId;
	}

	public int getThemeId() {
		return themeId;
	}

	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSomeId() {
		return someId;
	}

	public void setSomeId(int someId) {
		this.someId = someId;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getNewGoodsId() {
		return newGoodsId;
	}

	public void setNewGoodsId(String newGoodsId) {
		this.newGoodsId = newGoodsId;
	}

	public String getNewGoodsName() {
		return newGoodsName;
	}

	public void setNewGoodsName(String newGoodsName) {
		this.newGoodsName = newGoodsName;
	}

	public String getNewGoodsCategoryId() {
		return newGoodsCategoryId;
	}

	public void setNewGoodsCategoryId(String newGoodsCategoryId) {
		this.newGoodsCategoryId = newGoodsCategoryId;
	}

	public String getNewGoodsCategoryName() {
		return newGoodsCategoryName;
	}

	public void setNewGoodsCategoryName(String newGoodsCategoryName) {
		this.newGoodsCategoryName = newGoodsCategoryName;
	}

	public ArticleRepository getArticleRepository() {
		return articleRepository;
	}

	public void setArticleRepository(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}
	
	
}
