/**
 * 
 */
package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.modules.train.dao.AuthenticationMapper;
import com.training.modules.train.entity.AuthenticationBean;

/**  
* <p>Title: AuthenticationService.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月24日  
* @version 3.0.0 
*/

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationMapper authenticationMapper;

	/**  
	* <p>Title:查询企业认证过期授权 </p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	public List<AuthenticationBean> querypastdueauthentication() {
		return authenticationMapper.querypastdueauthentication();
	}

	/**  
	* <p>Title: 将认证授权状态改成已过去</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	public int updateauthenticationstatus(int id) {
		return authenticationMapper.updateauthenticationstatus(id);
	}

	/**  
	* <p>Title: 将合同状态改成已失效</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	public int updateprotocolstatus(int franchisee_id) {
		return authenticationMapper.updateprotocolstatus(franchisee_id);
	}

	/**  
	* <p>Title: 修改pc菜单改为禁用</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月25日  
	* @version 3.0.0  
	*/  
	public int updatepcmenustatus(int franchisee_id) {
		return authenticationMapper.updatepcmenustatus(franchisee_id);
		
	}
	
	
}
