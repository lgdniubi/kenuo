/**
 * 
 */
package com.training.modules.train.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.modules.train.dao.RefundOrderMapper;
import com.training.modules.train.entity.ArrearageOfficeList;

/**  
* <p>Title: RefundOrderService.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/

@Service
public class RefundOrderService {

	@Autowired
	private RefundOrderMapper refundOrderMapper;

	/**  
	* <p>Title: 获取欠款机构</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	 * @param format 
	*/  
	public List<ArrearageOfficeList> queryarrearageoffice(String format) {
		return refundOrderMapper.queryarrearageoffice(format);
	}

	/**  
	* <p>Title:创建欠款订单 </p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	*/  
	public int addrefundOrder(List<ArrearageOfficeList> subList) {
		return refundOrderMapper.addrefundOrder(subList);
	}

	/**  
	* <p>Title: 冻结未还款的账户</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	*/  
	public void updateOfficeAccount() {
		refundOrderMapper.updateOfficeAccount();
	}
	
}
