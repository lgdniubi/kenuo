package com.training.modules.ec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.dao.SubjectDao;
import com.training.modules.ec.entity.Goods;
import com.training.modules.ec.entity.Subject;

/**
 * 主题图Service
 * @author 小叶  2017年2月17日
 *
 */
@Service
@Transactional(readOnly = false)
public class SubjectService extends CrudService<SubjectDao,Subject>{
	
	@Autowired
	private SubjectDao subjectDao;
	
	/**
	 * 分页查询出所有的专题图
	 */
	public Page<Subject> findPage(Page<Subject> page,Subject subject){
		subject.setPage(page);
		page.setList(dao.findAllList(subject));
		return page;
	}
	
	/**
	 * 插入主题图
	 */
	public void insertSubject(Subject subject){
		subjectDao.insertSubject(subject);
	}
	
	/**
	 * 更新主题图
	 */
	public void updateSubject(Subject subject){
		subjectDao.updateSubject(subject);
	}
	
	/**
	 * 根据subId获取相应的主题图
	 * @param subject
	 * @return
	 */
	public Subject getSubject(int subId){
		return subjectDao.getSubject(subId);
	}
	
	/**
	 * 逻辑删除主题图
	 * @param subId
	 */
	public void delSubject(int subId){
		subjectDao.delSubject(subId);
	}
	
	/**
	 * 分页查询出主题图对应的商品
	 */
	public Page<Goods> findGoodsPage(Page<Goods> page,Goods goods){
		goods.setPage(page);
		page.setList(dao.findGoodsList(goods));
		return page;
	}
	
	/**
	 * 保存主题图对应的商品 
	 * @param subId
	 * @param goodsId
	 */
	public void saveSubjectGoods(int subId,String goodsIds){
		if(!"".equals(goodsIds) && goodsIds != null){
			subjectDao.delAllGoods(subId);
			String[] goodsIdArr = goodsIds.split(",");
			for(String goodsId:goodsIdArr){
				subjectDao.insertGoods(subId, Integer.valueOf(goodsId));
			}
		}
	}
	
	/**
	 * 删除主题图对应的商品 
	 * @param subId
	 * @param goodsId
	 */
	public void delGoods(int subId,int goodsId){
		subjectDao.delGoods(subId, goodsId);
	}
}
