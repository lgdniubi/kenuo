/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.personnelfile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.personnelfile.entity.UserImages;

/**
 * 人事档案DAO接口
 * @author ouwei
 *  2016-12-1
 */
@MyBatisDao
public interface UserImagesDao extends CrudDao<UserImages> {
	
	public void saveUserImages(List<UserImages> userImages);

	public void deleteByUserId(@Param("userId")String id);

	public UserImages getImgObject(@Param("userId")String id, @Param("imgType")int i);
}

