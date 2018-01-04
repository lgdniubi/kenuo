package com.training.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.service.CrudService;
import com.training.modules.sys.dao.MediaLoginAuthDao;
import com.training.modules.sys.entity.MediaLoginAuth;

/**
 * 自媒体权限表Service
 * @author 土豆  2017.11.8
 *
 */
@Service
@Transactional(readOnly = false)
public class MediaLoginAuthService extends CrudService<MediaLoginAuthDao, MediaLoginAuth>{

	@Autowired
	private MediaLoginAuthDao mediaLoginAuthDao;

	/**
	 * 根据妃子校用户id查询自媒体信息
	 * @param id
	 * @return
	 */
	public MediaLoginAuth findMediaLoginAuthByUserId(String userId) {
		return mediaLoginAuthDao.findMediaLoginAuthByUserId(userId);
	}

	/**
	 * 更新用户的自媒体权限
	 * @param mediaLoginAuth
	 */
	public void saveMediaLoginAuth(MediaLoginAuth mediaLoginAuth) {
		mediaLoginAuthDao.saveMediaLoginAuth(mediaLoginAuth);
	}
	
	
}
