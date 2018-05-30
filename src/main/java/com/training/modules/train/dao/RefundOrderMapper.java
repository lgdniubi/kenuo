/**
 * 
 */
package com.training.modules.train.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ArrearageOfficeList;
import com.training.modules.train.entity.RefundOrder;

/**  
* <p>Title: RefundOrderMapper.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/
@MyBatisDao
public interface RefundOrderMapper extends CrudDao<RefundOrder> {

	/**  
	* <p>Title: </p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	 * @param format 
	 * @param formats 
	*/  
	List<ArrearageOfficeList> queryarrearageoffice(@Param("format") String format,@Param("formats") String formats);

	/**  
	* <p>Title: </p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	*/  
	int addrefundOrder(@Param("subList") List<ArrearageOfficeList> subList);

	/**  
	* <p>Title: </p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	*/  
	void updateOfficeAccount();

	
}
