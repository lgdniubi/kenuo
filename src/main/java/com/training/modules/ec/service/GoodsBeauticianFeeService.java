package com.training.modules.ec.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.TreeService;
import com.training.modules.ec.dao.GoodsBeauticianFeeDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.GoodsBeauticianFee;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;
/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class GoodsBeauticianFeeService extends TreeService<GoodsBeauticianFeeDao,GoodsBeauticianFee> {
	
	@Autowired
	private GoodsBeauticianFeeDao goodsBeauticianFeeDao;
	@Autowired
	private GoodsDao goodsDao;

	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<GoodsBeauticianFee> findBeautician(Page<GoodsBeauticianFee> page, GoodsBeauticianFee goodsBeauticianFee) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		goodsBeauticianFee.setPage(page);
		// 执行分页查询
		page.setList(goodsBeauticianFeeDao.findList(goodsBeauticianFee));
		return page;
	}
	
	/**
	 * 保存数据
	 * @param goodsBeauticianFee
	 * @return
	 */
	public void insertBeaut(GoodsBeauticianFee goodsBeauticianFee){
		int j=6;   //目前只有 初中高  储备 实习 项目师 6个类型
		Date createDate=new Date();
		User user=UserUtils.getUser();
		Goods goods = goodsDao.getgoods(goodsBeauticianFee.getGoodsId()+"");
		for (int i = 0; i < j; i++) {
			GoodsBeauticianFee gbf=new GoodsBeauticianFee();
			gbf.setGoodsId(goodsBeauticianFee.getGoodsId());
			gbf.setGoodsNo(goods.getGoodsSn());
			gbf.setBasisFee(goodsBeauticianFee.getBasisFee());
			if(i==0){			//0 是初级
				gbf.setType(1);
				gbf.setPostFee(goodsBeauticianFee.getPrimary());
			}else if(i==1){		//1 中级
				gbf.setType(2);
				gbf.setPostFee(goodsBeauticianFee.getMiddle());
			}else if(i==2){		//2 高级
				gbf.setType(3);
				gbf.setPostFee(goodsBeauticianFee.getHigh());
			}else if(i==3){		//3 实习
				gbf.setType(4);
				gbf.setPostFee(goodsBeauticianFee.getInternship());
			}else if(i==4){		//4 储备
				gbf.setType(5); 
				gbf.setPostFee(goodsBeauticianFee.getStore());
			}else{				//5 项目师
				gbf.setType(6); 
				gbf.setPostFee(goodsBeauticianFee.getPrther());
			}
			gbf.setCreateBy(user);
			gbf.setCreateDate(createDate);
			gbf.setUpdateBy(user);
			gbf.setUpdateDate(createDate);
			gbf.setRemarks(goodsBeauticianFee.getRemarks());
			goodsBeauticianFeeDao.insertBeaut(gbf);
		}
		 
	}
	
	/**
	 * 根据商品id 查询信息 
	 * @param goodsId
	 * @return
	 */
	public List<GoodsBeauticianFee> selectBygoodsId(int goodsId){
		return goodsBeauticianFeeDao.selectBygoodsId(goodsId);
	}
	
	/**
	 * 根据商品id 查询记录
	 * @param goodsId
	 * @return
	 */
	public int selectByGoodsIdNum(int goodsId){
		return goodsBeauticianFeeDao.selectByGoodsIdNum(goodsId);
	}
	
	
	
	/**
	 * 保存数据
	 * @param goodsBeauticianFee
	 * @return
	 */
	public void update(GoodsBeauticianFee goodsBeauticianFee){
		int j=6;   //目前只有 初中高  储备 实习 项目师 6个类型
		Date createDate=new Date();
		User user=UserUtils.getUser();
		for (int i = 0; i < j; i++) {
			GoodsBeauticianFee gbf=new GoodsBeauticianFee();
			gbf.setGoodsId(goodsBeauticianFee.getGoodsId());
			gbf.setBasisFee(goodsBeauticianFee.getBasisFee());
			if(i==0){			//0 是初级
				gbf.setType(1);
				gbf.setPostFee(goodsBeauticianFee.getPrimary());
			}else if(i==1){		//1 中级
				gbf.setType(2);
				gbf.setPostFee(goodsBeauticianFee.getMiddle());
			}else if(i==2){		//2 高级
				gbf.setType(3);
				gbf.setPostFee(goodsBeauticianFee.getHigh());
			}else if(i==3){		//3 实习
				gbf.setType(4);
				gbf.setPostFee(goodsBeauticianFee.getInternship());
			}else if(i==4){		//4 储备
				gbf.setType(5); 
				gbf.setPostFee(goodsBeauticianFee.getStore());
			}else{				//5 项目师
				gbf.setType(6); 
				gbf.setPostFee(goodsBeauticianFee.getPrther());
			}
			gbf.setUpdateBy(user);
			gbf.setUpdateDate(createDate);
			gbf.setRemarks(goodsBeauticianFee.getRemarks());
			goodsBeauticianFeeDao.updateBeautician(gbf);
		}
		 
	}
	
}
