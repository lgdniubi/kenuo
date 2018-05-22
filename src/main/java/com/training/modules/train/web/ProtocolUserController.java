/**
 * Copyright &copy; 2016 kenuodanting
 */
package com.training.modules.train.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.web.BaseController;
import com.training.modules.ec.entity.ArticleRepository;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.ProtocolModel;
import com.training.modules.train.entity.ProtocolType;
import com.training.modules.train.entity.ProtocolUser;
import com.training.modules.train.service.ProtocolModelService;

/**
 * 用户--协议表--供应链
 * @author: jingfeng
 * @date 2018年5月15日下午2:40:37
 */
@Controller
@RequestMapping(value = "${adminPath}/train/protocol")
public class ProtocolUserController extends BaseController {

	@Autowired
	private ProtocolModelService protocolModelService;
	
	
	
	/**
	 * 查询某一个类型的协议列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "list")
	public String modelList( Model model) {
		List<ProtocolUser> protocolList = protocolModelService.findProtocolList();
		model.addAttribute("protocolList", protocolList);
		model.addAttribute("protocol", new ProtocolUser());
		return "modules/train/protocolUserList";
	}
	
	
}

 