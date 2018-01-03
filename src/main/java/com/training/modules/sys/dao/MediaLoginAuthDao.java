package com.training.modules.sys.dao;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.sys.entity.MediaLoginAuth;

/**
 * 自媒体权限dao
 * @author 土豆  2017.11.8
 *
 */

@MyBatisDao
public interface MediaLoginAuthDao extends CrudDao<MediaLoginAuth>{

	/**
	 * 根据妃子校用户id查询自媒体信息
	 * @param id
	 * @return
	 */
	MediaLoginAuth findMediaLoginAuthByUserId(String userId);

	/**
	 * 更新用户的自媒体权限
	 * @param mediaLoginAuth
	 */
	void saveMediaLoginAuth(MediaLoginAuth mediaLoginAuth);
	

}
