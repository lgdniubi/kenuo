package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.common.persistence.Page;
import com.training.common.web.BaseController;
import com.training.modules.sys.entity.TradingFZLog;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.service.TradingLogFZService;

/**
 * 账户明细管理
 * 
 * @author yangyang
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/train/tradinglog")
public class TradingLogFZController extends BaseController {

	@Autowired
	private TradingLogFZService tradingLogFZService;

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
	@RequiresPermissions("train:trading:view")
	@RequestMapping(value = { "tradinglist", "" })
	public String tradinglist(TradingFZLog tradingFZLog, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		try {
			Page<TradingFZLog> page = tradingLogFZService.findTradingLog(new Page<TradingFZLog>(request, response), tradingFZLog);
			model.addAttribute("page", page);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "妃子校账户明细页面", e);
			logger.error("妃子校账户明细页面：" + e.getMessage());
		}
		
		return "modules/train/tradingList";
	}

}
