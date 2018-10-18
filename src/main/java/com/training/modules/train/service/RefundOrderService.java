/**
 * 
 */
package com.training.modules.train.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.Thread.RefundThread;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.RefundOrderMapper;
import com.training.modules.train.entity.ArrearageOfficeList;
import com.training.modules.train.entity.RefundOrder;
import com.training.modules.train.entity.RefundOrderExport;
import com.training.modules.train.entity.RefundOrderLog;
import com.training.modules.train.entity.Statement;


/**  
* <p>Title: RefundOrderService.java</p>  
* <p>Copyright（C）2018 by FengFeng</p>  
* @author fengfeng  
* @date 2018年5月14日  
* @version 3.0.0 
*/

@Service
@Transactional(readOnly = false)
public class RefundOrderService extends CrudService<RefundOrderMapper, RefundOrder> {

	@Autowired
	private RefundOrderMapper refundOrderMapper;

	/**  
	* <p>Title: 获取欠款机构</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年5月14日  
	* @version 3.0.0  
	 * @param format 
	 * @param formats 
	*/  
	public List<ArrearageOfficeList> queryarrearageoffice(String format, String formats) {
		return refundOrderMapper.queryarrearageoffice(format,formats);
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
	
	/**
	 * 查询对账单
	 * @param order_id
	 * @return
	 */
	public List<Statement> queryStatementOfRefund(String orderId){
		return this.refundOrderMapper.queryStatementOfRefund(orderId);
	}
	/**
	 * 查询支付信息
	 * @param order_id
	 * @return
	 */
	public RefundOrder queryRefundOrderDetail(String order_id){
		return this.refundOrderMapper.queryRefundOrderDetail(order_id);
	}
	/**
	 * 确认入账
	 * @param order_id
	 */
	public void makeSureInAccount(String order_id,String office_id,double amount,String status,String remarks){
		//修改订单状态为已入账
		this.refundOrderMapper.makeSureInAccount(order_id,status,remarks);
		this.refundOrderMapper.updateStatementStatus(order_id, Integer.parseInt(status));
		if("3".equals(status)){System.out.println("调用远程接口还款：office_id:"+office_id+",order_id:"+order_id+",amount:"+amount);
			new Thread(new RefundThread(office_id, order_id, amount)).start();
		}
		//记录日志
		RefundOrderLog log = new RefundOrderLog();
		log.setCreateBy(UserUtils.getUser().getId());
		log.setOrderId(order_id);
		if("4".equals(status))
			log.setDescription("【取消账单】");
		else
			log.setDescription("【确认入账】");
		this.refundOrderMapper.insertRefundOrderLog(log);
			
	}

	/**  
	* <p>Title: 将订单改为逾期状态</p>  
	* <p>Copyright（C）2018 by FengFeng</p>   
	* @author fengfeng  
	* @date 2018年6月6日  
	* @version 3.0.0  
	*/  
	public void updateOrderOverdueStatus() {
		this.refundOrderMapper.updateOrderOverdueStatus();
	}
	/**
	 * 查询账单日志
	 * @param order_id
	 * @return
	 */
	public List<RefundOrderLog> queryRefundOrderLogList(String order_id){
		return this.refundOrderMapper.queryRefundOrderLogList(order_id);
	}

	public Page<RefundOrderExport> findExportPage(Page<RefundOrderExport> page, RefundOrderExport refundOrder) {
		// 设置分页参数
		refundOrder.setPage(page);
		// 执行分页查询
		page.setList(refundOrderMapper.findExportList(refundOrder));
		return page;
	}
}
