package com.training.modules.sys.dao;

import java.util.List;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.Userinfocontent;

/**
 * 用户图片信息
 * @author water
 *
 */

@MyBatisDao
public interface UserinfocontentDao extends CrudDao<Userinfocontent> {
	/**
	 * 循环插入图片list
	 * @param list
	 * @return
	 */
	public int insertPiclive(List<Userinfocontent> list);
	/**
	 * 删除图片信息
	 * @param userid
	 * @return
	 */
	
	public int deletPicByuser(String userid);
	
	/**
	 * 根据用户id查询用户的生活照集合信息
	 * @param id
	 * @return
	 */
	public List<String> findByUserId(String id);

}
