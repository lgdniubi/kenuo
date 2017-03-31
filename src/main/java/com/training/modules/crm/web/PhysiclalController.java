package com.training.modules.crm.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.CrmDict;
import com.training.modules.crm.entity.ShapeFile;
import com.training.modules.crm.entity.SkinFile;
import com.training.modules.crm.entity.UserDetail;
import com.training.modules.crm.service.CrmDictService;
import com.training.modules.crm.service.ShapeFileService;
import com.training.modules.crm.service.SkinFileService;
import com.training.modules.crm.service.UserDetailService;
import com.training.modules.crm.utils.StringHandel;
import com.training.modules.sys.utils.BugLogUtils;

/**
 * kenuo 产品使用记录相关
 * 
 * @author：sharp @date：2017年3月7日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/physical")
public class PhysiclalController extends BaseController {

	@Autowired
	private ShapeFileService shapeService;
	@Autowired
	private CrmDictService crmDictService;
	@Autowired
	private SkinFileService skinFileService;

	@Autowired
	private UserDetailService userDetailService;
	
	/**
	 * 默认返回用户信息
	 * @param 
	 * @return UserDetail
	 */
	@ModelAttribute
	public UserDetail  get(@RequestParam(required = false) String userId) {
		if (StringUtils.isNotBlank(userId)) {
			UserDetail detail=  userDetailService.getUserNickname(userId); 
		    return detail;
		} else {
			return new UserDetail();
		}
	}
	/**
	 * @param
	 * @return String
	 * @description
	 */
	@RequestMapping(value = "skin")
	public String getPhysiclalStatus(String userId, HttpServletRequest request, HttpServletResponse response,Model model) {
		try {
			//if ("".equals(userId) || userId == null) {
			if (null == userId || userId.trim().length() <= 0) {
				return null;
			} else {
				SkinFile file = skinFileService.get(userId);
				// 获取所有skin条目
				List<CrmDict> dictList = crmDictService.getSkinFile();
				List<String> typeList = new ArrayList<>();
				Map<String, Object> multiValueMap = new HashMap<String, Object>();
				// 如果skin对象为空的话，直接返回不用设值，而且反射会报NPX
				if (file != null) {
					// 循环skinFile实体类的属性和值
					Class cls = file.getClass();
					Field[] fields = cls.getDeclaredFields();
					for (int i = 0; i < fields.length; i++) {
						Field f = fields[i];
						f.setAccessible(true);
						// skinFile每个属性和value放进map
						multiValueMap.put(f.getName(), StringHandel.getValue(f.get(file)));
						logger.debug("属性名:" + f.getName() + " 属性值:" + StringHandel.getValue(f.get(file)));
					}
					// 将skinFile的字段设到每个crmDict中
					for (CrmDict crmDict : dictList) {
						String type = crmDict.getType();
						logger.debug("type" + type);
						// 如果在skinFile中有此字段，给它设置multiValue
						if (multiValueMap.containsKey(type)) {
							logger.debug("contains" + type);
							crmDict.setMultiValue(multiValueMap.get(type));
						}
						if (!typeList.contains(type)) {
							typeList.add(type);
						}
					}
				}
				model.addAttribute("dictList", dictList);
				model.addAttribute("typeList", typeList);
				model.addAttribute("userId", userId);
				model.addAttribute("skinFile",file );
			}
		} catch (Exception e) {
			e.printStackTrace();
			BugLogUtils.saveBugLog(request, "查询用户皮肤状态", e);
			logger.error("用户详情：" + e.getMessage());
		}
		return "modules/crm/physiclalStatus";
	}

	/**
	 * @param 一个皮肤档案的实体类
	 * @return String
	 * @description
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "saveSkinFile")
	public String saveSkinFile(SkinFile skinFile, HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			String userId = skinFile.getUserId();
			if (null!=userId && userId.trim().length()>0) {
				SkinFile file = skinFileService.get(userId);
				if (null != file) {
					Integer result = skinFileService.update(skinFile);
					if (result == 1) {
						addMessage(redirectAttributes, "保存/修改  皮肤档案'" + "'成功");
					}
				} else {
					skinFileService.save(skinFile);
					addMessage(redirectAttributes, "保存/修改  皮肤档案'" + "'成功");
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			logger.debug(e.getLocalizedMessage());
			addMessage(redirectAttributes, "保存/修改  皮肤档案'" + "'失败");
			e.printStackTrace();
		}
		return "redirect:" + adminPath + "/crm/physical/skin?userId="+skinFile.getUserId();
	}

	/**
	 * 返回形体档案
	 * @param
	 * @return String
	 * @description
	 */
	@RequestMapping(value = "shape")
	public String userShape(String userId, HttpServletRequest request, HttpServletResponse response, Model model) {

		if (null == userId ||userId.trim().length()<=0) {
			model.addAttribute("userId", userId);
		} else {
			ShapeFile shapeFile;
			try {
				shapeFile = shapeService.get(userId);
				model.addAttribute("shapeFile", shapeFile);
			} catch (Exception e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
			model.addAttribute("userId", userId);
		}
		return "modules/crm/userShape";
	}
	
	/**
	 * 保存形体档案
	 * @param 形体档案
	 * @return redirect
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "saveShapeFile")
	public String saveShapeFile(ShapeFile shapeFile, HttpServletRequest request, HttpServletResponse response, Model model,
			RedirectAttributes redirectAttributes) {
		
		String userId = shapeFile.getUserId();
		try {
			if (null!=userId && userId.trim().length()>0) {
				ShapeFile exist = shapeService.get(userId);
				if (null != exist) {
					Integer result = shapeService.update(shapeFile);
					if (result == 1) {
						addMessage(redirectAttributes, "保存/修改  形体档案'" + "'成功");
					}
				} else {
					shapeService.save(shapeFile);
					addMessage(redirectAttributes, "保存/修改  形体档案'" + "'成功");
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			logger.debug(e.getLocalizedMessage());
			addMessage(redirectAttributes, "保存/修改  皮肤档案'" + "'失败");
			e.printStackTrace();
		}
		return "redirect:" + adminPath + "/crm/physical/shape?userId="+userId;
	}
}
