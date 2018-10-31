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
import com.training.modules.train.entity.RefundOrderExport;
import com.training.modules.train.entity.RefundOrderLog;
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
	List<Statement> queryStatementOfRefund(@Param("orderId")String orderId);
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
	public void makeSureInAccount(@Param("order_id")String order_id,@Param("status")String status,@Param("remarks")String remarks);

	/**  
	* <p>Title: 将订单改为逾期状态</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年6月6日  
	* @version 3.0.0  
	*/  
	void updateOrderOverdueStatus();
	
	
	int updateStatementStatus(@Param("order_id")String order_id,@Param("status")int status);
	/**
	 * 插入账单日志
	 * @param log
	 * @return
	 */
	int insertRefundOrderLog(RefundOrderLog log);
	/**
	 * 查询账单日志列表
	 * @param order_id
	 * @return
	 */
	public List<RefundOrderLog> queryRefundOrderLogList(@Param("order_id")String order_id);

	/**
	 * 查询导出
	 * @param refundOrder
	 * @return
	 */
	public List<RefundOrderExport> findExportList(RefundOrderExport refundOrder);

	public List<RefundOrder> auditAll(String[] idArray);

	/**
	 * 查凭证
	 * @param order_id
	 * @return
	 */
	public List<String> findProofList(String id);

	public void refundOrderTimeout(@Param("timeout")String timeout);

	public void refundOrderTimeoutUpdateOfficeState(@Param("timeout")String timeout);
}
