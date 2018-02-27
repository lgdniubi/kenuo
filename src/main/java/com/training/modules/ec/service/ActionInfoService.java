package com.training.modules.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.ActionInfoDao;
import com.training.modules.ec.dao.GoodsDao;
import com.training.modules.ec.dao.OrderGoodsDao;
import com.training.modules.ec.entity.ActionInfo;
import com.training.modules.ec.entity.Goods;
/**
 * 活动
 * @author yangyang
 *
 */
@Service
@Transactional(readOnly = false)
public class ActionInfoService extends CrudService<ActionInfoDao,ActionInfo> {
	
	@Autowired
	private ActionInfoDao actionInfoDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	
	
	/**
	 * 分页查询
	 * @param page
	 * @param 分页查询
	 * @return
	 */
	public Page<ActionInfo> findAction(Page<ActionInfo> page, ActionInfo actionInfo) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		//redEnvelope.getSqlMap().put("dsf", dataScopeFilter(redEnvelope.getCurrentUser(), "o", "a"));
		// 设置分页参数
		actionInfo.setPage(page);
		// 执行分页查询
		page.setList(actionInfoDao.findList(actionInfo));
		return page;
	}
	
	public List<ActionInfo> actionList(){
		return actionInfoDao.findAllList();
	}
	
	/**
	 * 保存数据
	 * @param actionInfo
	 * @return
	 */
	public int insertAction(ActionInfo actionInfo){
		return actionInfoDao.insertAction(actionInfo);
	}

	/**
	 * 开启，关闭状态
	 * @param actionInfo
	 * @return
	 */
	public int updateStatus(ActionInfo actionInfo){
		return actionInfoDao.updateStatus(actionInfo);
	}
	
	/**
	 *商品添加活动 
	 * @param goods
	 * @return
	 */
	public int updateActionId(Goods goods){
		return goodsDao.updateActionId(goods);
	}
	
	
	public List<Goods> ActionGoodslist(int actionId){
		return goodsDao.ActionGoodslist(actionId);
	}
	/**
	 * 更新数据
	 * @param actionInfo
	 * @return
	 */
	public int update(ActionInfo actionInfo){
		return actionInfoDao.update(actionInfo);
	}
	/**
	 * 改变开启，关闭商品上下架
	 * @param goods
	 * @return
	 */
	public int updateGoodsStauts(Goods goods){
		return goodsDao.updateGoodsStauts(goods);
	}
	/**
	 * 查询商品是否有购买记录
	 * @param goodsId
	 * @return
	 */
	public int numByGoodsId(String goodsId){
		return orderGoodsDao.numByGoodsId(goodsId);
	}
	/**
	 *定时器 开启状态 所有开启时间到了的数据
	 * @return
	 */
	
	public List<ActionInfo> selectActionStartTime(){
		return actionInfoDao.selectActionStarttime();
	}
	
	/**
	 *定时器 开启关闭状态 所有关闭时间到了的数据
	 * @return
	 */
	
	public List<ActionInfo> selectActionCloseTime(){
		return actionInfoDao.selectActionCloseTime();
	}
	
	/**
	 * 更新定时器状态
	 * @return
	 */
	public int updateExecuteStatus(ActionInfo actionInfo){
		return actionInfoDao.updateExecuteStatus(actionInfo);
	}
	
}
