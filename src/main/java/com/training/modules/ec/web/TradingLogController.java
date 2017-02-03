package com.training.modules.ec.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.ec.entity.TradingLog;
import com.training.modules.ec.service.TradingLogService;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * 账户明细管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/ec/tradinglog")
public class TradingLogController extends BaseController {

	@Autowired
	private TradingLogService tradingLogService;

	// @ModelAttribute
	// public Coupon get(@RequestParam(required = false) String id) {
	// if (StringUtils.isNotBlank(id)) {
	// return couponService.get(id);
	// } else {
	// return new CouponUser();
	// }
	// }

	/**
	 * 明细分页查询 条件查询
	 * 
	 * @param orders
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("ec:trading:view")
	@RequestMapping(value = { "tradinglist", "" })
	public String tradinglist(TradingLog tradingLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			Page<TradingLog> page = tradingLogService.findTradingLog(new Page<TradingLog>(request, response), tradingLog);
			model.addAttribute("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "账户明细页面", e);
			logger.error("账户明细页面：" + e.getMessage());
		}
		
		return "modules/ec/tradingList";
	}

}
