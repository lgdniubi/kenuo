package com.training.modules.train.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.sys.utils.BugLogUtils;
import com.training.modules.train.entity.TrainLivePlayback;
import com.training.modules.train.service.TrainLivePlaybackService;

@Controller
@RequestMapping(value = "${adminPath}/train/playback")
public class TrainLivePlayBackController extends BaseController{
	
	@Autowired
	private TrainLivePlaybackService trainLivePlaybackService;
	

	
	@ModelAttribute
	public TrainLivePlayback get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return trainLivePlaybackService.get(id);
		} else {
			return new TrainLivePlayback();
		}
	}

	/**
	 * 分页查询 条件查询
	 * @param trainLiveAudit
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("train:playback:view")
	@RequestMapping(value = { "list"})
	public String list(TrainLivePlayback trainLivePlayback, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainLivePlayback> page = trainLivePlaybackService.findPlaybackList(new Page<TrainLivePlayback>(request, response), trainLivePlayback);
		model.addAttribute("page", page);
		return "modules/train/playbacklist";
	}
	
	/**
	 * 查看修改页面
	 * @param request
	 * @param trainLivePlayback
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "form")
	public String form(HttpServletRequest request,TrainLivePlayback trainLivePlayback, Model model) {
		try {
	
			model.addAttribute("trainLivePlayback",trainLivePlayback);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "回看查看修改页面", e);
			logger.error("回看查看修改页面：" + e.getMessage());
		}

		return "modules/train/playBackForm";
	}
	
    
	/**
	 * 显示隐藏回看
	 * @param request
	 * @param trainLivePlayback
	 * @return
	 */
	@RequestMapping(value = "pIsShow")
	public String pIsShow(HttpServletRequest request,TrainLivePlayback trainLivePlayback) {
		try {
			trainLivePlaybackService.updateIsShow(trainLivePlayback);
		} catch (Exception e) {
			// TODO: handle exception
			BugLogUtils.saveBugLog(request, "回看查看修改页面", e);
			logger.error("回看查看修改页面：" + e.getMessage());
		}

		return "redirect:" + adminPath + "/train/playback/list?repage";
	}
    
}
