package com.training.modules.personnelfile.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.personnelfile.entity.PersonnelFile;
import com.training.modules.personnelfile.service.PersonnelFileService;
import com.training.modules.sys.utils.BugLogUtils;


@Controller
@RequestMapping(value = "${adminPath}/personnelfile/user")
public class PersonnelFileController extends BaseController {
	@Autowired
	private PersonnelFileService personnelFileService;
	
	@ModelAttribute("personnelFile")
	public PersonnelFile get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return personnelFileService.get(id);
		}else{
			return new PersonnelFile();
		}
	}
	
	/**
	 * 查询人事档案列表
	 * @param personnelFile
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "list", "" })
	public String list(PersonnelFile personnelFile, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			Page<PersonnelFile> page = personnelFileService.findPersonnelFileAll(new Page<PersonnelFile>(request, response), personnelFile);
			model.addAttribute("page", page);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "查询人事档案列表错误", e);
			logger.error("查询人事档案列表错误:"+e.getMessage());
		}
		return "modules/personnelfile/personnelfileList";
	}
	
	/**
	 * 新增人员需要回带一些数据
	 * @param personnelFile
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:personnelfile:add")
	@RequestMapping(value = "form")
	public String form(PersonnelFile personnelFile, Model model,HttpServletRequest request) {
		try {
			PersonnelFile findPortionInfo = personnelFileService.findPortionInfo(personnelFile);
			model.addAttribute("personnelFile", findPortionInfo);
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "新增人员页面回带数据错误", e);
			logger.error("新增人员页面回带数据错误:"+e.getMessage());
		}
		return "modules/personnelfile/personnelfileForm";
	}
	
	/**
	 * 保存人事档案
	 * @param personnelFile
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "save")
	public String save(PersonnelFile personnelFile, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		try {
			personnelFileService.savePersonnelFile(personnelFile, request);
			addMessage(redirectAttributes, "保存人事档案成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "保存人事档案失败！");
			BugLogUtils.saveBugLog(request, "保存人事档案错误", e);
			logger.error("保存人事档案错误:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/personnelfile/user/list";
	}
	
	/**
	 * 获得人事档案所有信息
	 * @param personnelFile
	 * @return
	 */
	@RequiresPermissions(value = {"sys:personnelfile:edit","sys:personnelfile:view"})
	@RequestMapping(value = "getPersonnelFileBefor")
	public String getPersonnelFileBefor(PersonnelFile personnelFile, Model model, HttpServletRequest request){
		String url = "";
		try {
			String type = request.getParameter("type");
			PersonnelFile  _personnelFile = personnelFileService.getPersonnelFileBefor(personnelFile);
			model.addAttribute("personnelFile", _personnelFile);
			if("1".equals(type)){
				url = "modules/personnelfile/personnelfileEdit";
			}else{
				url = "modules/personnelfile/personnelfileView";
			}
		} catch (Exception e) {
			BugLogUtils.saveBugLog(request, "获取人事档案添加或修改页面错误", e);
			logger.error("获取人事档案添加或修改页面错误:"+e.getMessage());
		}
		return url;
	}
	/**
	 * 修改人事档案的方法
	 * @return
	 */
	@RequestMapping(value = "updatePersonnelFile")
	public String updatePersonnelFile(PersonnelFile personnelFile, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
		try {
			personnelFileService.updatePersonnelFile(personnelFile);
			addMessage(redirectAttributes, "修改人事档案成功！");
		} catch (Exception e) {
			addMessage(redirectAttributes, "修改人事档案失败！");
			BugLogUtils.saveBugLog(request, "修改人事档案错误", e);
			logger.error("修改人事档案错误:"+e.getMessage());
		}
		return "redirect:" + adminPath + "/personnelfile/user/list";
	}
}

