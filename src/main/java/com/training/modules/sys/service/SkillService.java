package com.training.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.dao.SkillDao;
import com.training.modules.sys.entity.Skill;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.UserUtils;

/**
 * 技能标签Service
 * @author 小叶  2016.12.27
 *
 */
@Service
@Transactional(readOnly = false)
public class SkillService extends CrudService<SkillDao, Skill>{
	@Autowired
	private SkillDao skillDao;
	
	/**
	 * 查询出所有的技能标签(只查找显示的)
	 */
	public List<Skill> findAllList(){
		return skillDao.findAllList();
	}
	
	/**
	 * 查询出所有的技能标签(无论显示还是隐藏都查出来)
	 */
	public List<Skill> newFindAllList(){
		return skillDao.newFindAllList();
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param skill
	 * @return
	 */
	public Page<Skill> findList(Page<Skill> page, Skill skill) {
		// 设置分页参数
		skill.setPage(page);
		// 执行分页查询
		page.setList(skillDao.findList(skill));
		return page;
	}
	
	/**
	 * 新增编辑技能标签
	 * @param skill
	 */
	public void saveSkill(Skill skill){
		User user = UserUtils.getUser();
		if(skill.getSkillId() != 0){
			skill.setUpdateBy(user);
			skill.setFranchiseeId(user.getCompany().getId());
			skillDao.updateSkill(skill);
		}else{
			skill.setCreateBy(user);
			skill.setFranchiseeId(user.getCompany().getId());
			skillDao.insertSkill(skill);
		}
	}
	
	/**
	 * 逻辑删除技能标签
	 * @param skillId
	 * @return
	 */
	public void deleteSkill(Skill skill){
		skillDao.deleteSkill(skill);
	}
	
	/**
	 * 修改技能标签是否显示
	 */
	public void updateIsShow(Skill skill){
		skillDao.updateIsShow(skill);
	}
	
	/**
	 * 验证技能标签对应的商品是否仍有上架的
	 * @param skillId
	 * @return
	 */
	public int selectGoodsisOnSale(int skillId){
		return skillDao.selectGoodsisOnSale(skillId);
	}
	
	/**
	 * 验证技能标签名称
	 * @param name
	 * @return
	 */
	public int getByName(String name){
		return skillDao.getByName(name);
	}

	/**
	 * 根据id判断可否删除
	 * @param skill
	 * @return
	 * @Description:
	 */
	public boolean validDel(Skill skill) {
		int count = skillDao.validDel(skill);
		return count == 0;
	}
}
