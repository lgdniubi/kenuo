/**
 * 
 */
package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.AuthenticationBean;

/**  
* <p>Title: AuthenticationMapper.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月24日  
* @version 3.0.0 
*/

@MyBatisDao
public interface AuthenticationMapper {

	/**  
	* <p>Title: 查询企业认证过期授权</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	List<AuthenticationBean> querypastdueauthentication();

	/**  
	* <p>Title: 将认证授权状态改成已过去</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	int updateauthenticationstatus(@Param("id") int id);

	/**  
	* <p>Title: 将合同状态改成已失效</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	int updateprotocolstatus(@Param("franchisee_id") int franchisee_id);

	/**  
	* <p>Title: 修改pc菜单改为禁用</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	int updatepcmenustatus(@Param("franchisee_id") int franchisee_id);

	/**  
	* <p>Title: 获取该商家下的用户</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月30日  
	* @version 3.0.0  
	*/  
	List<String> queryuserlist(@Param("franchisee_id") int franchisee_id);

	
}
