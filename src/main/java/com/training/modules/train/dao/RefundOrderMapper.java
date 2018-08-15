/**
 * 
 */
package com.training.modules.train.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.training.common.persistence.CrudDao;
import com.training.common.persistence.annotation.MyBatisDao;
import com.training.modules.train.entity.ArrearageOfficeList;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.entity.Statement;

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
	/**
	 * 查询对账单
	 * @param order_id
	 * @return
	 */
	List<Statement> queryStatementOfRefund(@Param("office_id")String office_id,@Param("billmonth")String billmonth);
	/**
	 * 查询支付信息
	 * @param order_id
	 * @return
	 */
	public RefundOrder queryRefundOrderDetail(@Param("order_id")String order_id);
	/**
	 * 确认入账
	 * @param order_id
	 */
	public void makeSureInAccount(@Param("order_id")String order_id,@Param("status")String status);

	/**  
	* <p>Title: 将订单改为逾期状态</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年6月6日  
	* @version 3.0.0  
	*/  
	void updateOrderOverdueStatus();
}
