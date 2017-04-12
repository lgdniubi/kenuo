package com.training.modules.crm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.training.common.persistence.Page;
import com.training.common.utils.StringUtils;
import com.training.common.web.BaseController;
import com.training.modules.crm.entity.Complain;
import com.training.modules.crm.service.AdviceService;
import com.training.modules.ec.entity.GoodsBrand;
import com.training.modules.ec.service.GoodsBrandService;
import com.training.modules.oa.entity.OaNotify;
import com.training.modules.oa.entity.OaNotifyRecord;
import com.training.modules.oa.service.OaNotifyService;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.UserUtils;

/**
 * @author：星星
 * @description：投诉咨询controller类 
 * 2017年3月10日
 */
@Controller
@RequestMapping(value = "${adminPath}/crm/store")
public class AdviceController extends BaseController {

	@Autowired
	private AdviceService adviceService;
	@Autowired
	private GoodsBrandService goodsBrandService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private OaNotifyService oaNotifyService;

	/**
	 * @author：星星
	 * @description：根据来电，查询是否会员 
	 * 2017年3月14日
	 */
	@ResponseBody
	@RequestMapping(value = "/member")
	public Complain member(Complain complain, HttpServletRequest request, HttpServletResponse response, Model model){
		String mobile = complain.getMobile();
		Complain complains = adviceService.selectMember(complain);
		if(null != complains){
			return complains;
		}else{
			List<Complain> complan = adviceService.selectSeek(complain);
			if(complan.size() <= 0){
				Complain compla = new Complain();
				compla.setStamp("1");
				return compla;
			}else{
				Complain comp = new Complain();
				comp.setMobile(mobile);
				return comp;
			}			
		}
	}

	/**
	 * @author：星星
	 * @description：店长首页 
	 * 2017年3月10日
	 */
	@RequiresPermissions("crm:store:home")
	@RequestMapping(value = "home")
	public String storeHome(Complain complain, HttpServletRequest request, Model model) {
		String id = UserUtils.getUser().getId();
		complain.setRedirectUserId(id);
		// 已处理的投诉咨询展示
		complain.setStatus(2);
		complain.setMember(1);
		List<Complain> complans = adviceService.selectStatus(complain);
		int counts = adviceService.getCount(complain);
		model.addAttribute("counts", counts);
		model.addAttribute("complans", complans);
		// 未处理的投诉咨询展示
		complain.setStatus(1);
		complain.setMember(1);
		List<Complain> com = adviceService.selectStatus(complain);
		int coun = adviceService.getCount(complain);
		model.addAttribute("coun", coun);
		model.addAttribute("com", com);
		// 未处理的快速来电展示
		complain.setStatus(1);
		complain.setMember(2);
		complain.setStamp("1");
		complain.setRedirectUserId("");
		List<Complain> complan = adviceService.selectStatus(complain);
		int count = adviceService.getCount(complain);
		model.addAttribute("count", count);
		model.addAttribute("complan", complan);
		return "modules/crm/storeHome";
	}
	
	/**
	 * @author：星星
	 * @description：分页查询、条件查询 
	 * 2017年3月10日
	 */
	@RequiresPermissions("crm:store:list")
	@RequestMapping(value = "/list")
	public String list(Complain complain, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		if(null != complain && complain.getStamp() != null && !complain.getStamp().equals("")){
			complain.setRedirectUserId("");
			model.addAttribute("stamp", complain.getStamp());
			model.addAttribute("mobile", complain.getMobile());
			
		}
/*		if(null != complain && complain.getTab() !=null && !complain.getTab().equals("")){
			complain.setMobile("");
			String id = UserUtils.getUser().getId();
			complain.setRedirectUserId(id);
		}*/
		else{
			complain.setMobile("");
			String id = UserUtils.getUser().getId();
			complain.setRedirectUserId(id);
		}
		// 查询所有投诉
		Page<Complain> page = adviceService.findPage(new Page<Complain>(request, response), complain);
		model.addAttribute("page", page);
		// 查询商品品牌
		List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
		model.addAttribute("goodsBrandList", goodsBrandList);
		return "modules/crm/questionList";
	}

	/**
	 * @author：星星
	 * @description：投诉咨询详情查询 
	 * 2017年3月10日
	 */
	@RequestMapping(value = "/detailed")
	public String detailed(@RequestParam("id") String id, Complain complan, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		if(complan != null && !complan.getStamp().equals("")){
			model.addAttribute("stamp", complan.getStamp());
		}	
		/*complain.setId(id);*/
		// 查询处理过程
		List<Complain> complains = adviceService.procedure(complan);
		model.addAttribute("complains", complains);
		// 查询投诉详情
		String userId = UserUtils.getUser().getId();
		complan.setRedirectUserId(userId);
		Complain complain = adviceService.detailed(complan);
		String name = UserUtils.getUser().getName();
		String no = UserUtils.getUser().getNo();
		complain.setHandlerID(no);
		complain.setHandler(name);
		if (complain != null && !complain.getConsumeShop().equals("")) {
			Office office = officeService.get(complain.getConsumeShop());
			complain.setOfficeId(office.getId());
			complain.setOfficeName(office.getName());
		}
		model.addAttribute("complain", complain);
		// 查询商品品牌
		List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
		model.addAttribute("goodsBrandList", goodsBrandList);
		return "modules/crm/storeFrom";
	}

	/**
	 * @author：星星
	 * @description：转新增投诉咨询页面 
	 * 2017年3月10日
	 */
	@RequestMapping(value = "from")
	public String storeHome(Model model, Complain complain, HttpServletRequest request, HttpServletResponse response) {
		if(complain !=null && complain.getMobile() !=null && !complain.getMobile().equals("")){
			Complain complains = adviceService.getUser(complain.getMobile());
			complain.setMobile(complains.getMobile());
			complain.setNickName(complains.getNickName());
			complain.setName(complains.getName());
		}
		String name = UserUtils.getUser().getName();
		String no = UserUtils.getUser().getNo();
		complain.setHandlerID(no);
		complain.setCreatBy(name);
		complain.setHandler(name);		
		model.addAttribute("complain", complain);
		// 查询商品品牌
		List<GoodsBrand> goodsBrandList = goodsBrandService.findAllList(new GoodsBrand());
		model.addAttribute("goodsBrandList", goodsBrandList);
		return "modules/crm/storeFrom";
	}

	/**
	 * 搜索转交人界面跳转
	 * @author：星星
	 * @return
	 */
	@RequestMapping(value = "/crmList")
	public String oaList(OaNotify oaNotify, Model model) {
		if (StringUtils.isNotBlank(oaNotify.getId())) {
			List<OaNotifyRecord> list = oaNotifyService.getUser(oaNotify);
			model.addAttribute("list", list);
		}
		return "modules/crm/redirectUserFrom";
	}

	/**
	 * @author：星星
	 * @description：查询转交人集合
	 * 2017年3月10日
	 */	
	@ResponseBody
	@RequestMapping(value = "oaList")
	public Map<String, List<User>> oaList(User user, HttpServletRequest request, HttpServletResponse response, Model model) {	
		Map<String,List<User>> jsonMap=new HashMap<String, List<User>>(); 
		List<User> list = adviceService.findUser(user);
		jsonMap.put("list",list);
		return jsonMap;
	}
	
	/**
	 * @author：星星
	 * @description：验证转交人
	 * 2017年3月10日
	 */
	@ResponseBody
	@RequestMapping(value = "getRedirectUserId")
	public int getRedirectUserId(Complain complain){
		if(complain !=null && complain.getId()!=null && complain.getId().equals("")){
			if(complain.getRedirectUserId()!= null && complain.getRedirectUserId().equals(UserUtils.getUser().getId())){
				return 1;				
			}else{
				return 0;
			}
		}else{
			List<Complain> complains = adviceService.getRedirectUserId(complain);
			int count = complains.size();
			return count;
		    }
		}	
	
	/**
	 * @author：星星
	 * @description：根据电话号码查询会员信息
	 * 2017年3月10日
	 */
	@ResponseBody
	@RequestMapping(value = "getUser")
	public Complain getUser(String mobile){
		Complain complain = adviceService.getUser(mobile);
		return complain;
	}

	/**
	 * @author：星星
	 * @description：保存投诉咨询 
	 * 2017年3月10日
	 */
	@RequestMapping(value = "save")
	public String storeFrom(Complain complain, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String redirectId = complain.getRedirectUserId();
		String id = UserUtils.getUser().getId();
		complain.setConsumeShop(complain.getOfficeId());
		complain.setCreatBy(id);
		complain.setHandler(id);
		//判断是不是首次保存问题
		if (complain.getId().equals("")) {
			Complain complains = adviceService.selectMemb(complain);
			if (complains == null) {
				complain.setMember(2);
			} else {
				complain.setMember(1);
			}
			//判断是否有处理过程
			if (complain.getHandResult() != null) {
				//判断是否转接
				if (complain.getRedirectUserId() != null && !complain.getRedirectUserId().equals("")) {
					complain.setRedirectUserId(id);
					complain.setChangeTimes(1);
					complain.setQuestionSource(2);
					complain.setSolveTimes(0);
					adviceService.saveQuestion(complain);
					adviceService.saveSolve(complain);
					complain.setRedirectUserId(redirectId);
					complain.setQuestionSource(3);
					complain.setSolveTimes(1);
					adviceService.saveSolve(complain);
				}else {
					complain.setRedirectUserId(id);
					complain.setChangeTimes(0);
					complain.setSolveTimes(1);
					complain.setQuestionSource(1);
					adviceService.saveQuestion(complain);
					adviceService.saveSolve(complain);			
				}
			} else {
				complain.setQuestionDegree(complain.getDegree());
				complain.setHandResult(complain.getStatus());
				complain.setRedirectUserId(id);
				complain.setChangeTimes(0);
				complain.setSolveTimes(0);
				complain.setQuestionSource(1);
				adviceService.saveQuestion(complain);
				adviceService.saveSolve(complain);
			}
			addMessage(redirectAttributes, complain.getName()+"的问题保存成功！");
			return "redirect:" + adminPath + "/crm/store/list?stamp="+complain.getStamp()+"&mobile="+complain.getMobile();
		} else {
			//判断有没有处理过程
			if (complain.getHandResult() != null) {
				//判断问题是否被处理
				if (complain.getHandResult() == 2) {
					adviceService.creatResult(complain);
				}
				Complain complains = adviceService.selectHandle(complain);
				complain.setSolveTimes(complains.getSolveTimes() + 1);
				//判断是否转交
				if (complain.getRedirectUserId()!=null && !complain.getRedirectUserId().equals("")) {
					Complain complainId = adviceService.selectId(complain);
					complainId.setQuestionSource(2);
					adviceService.creatHandle(complainId);
					complain.setRedirectUserId(redirectId);
					complain.setChangeTimes(complains.getChangeTimes() + 1);
					complain.setQuestionSource(3);	
				}else{
					complain.setQuestionSource(complains.getQuestionSource());//没转交的情况下，状态和上一个一致
					complain.setChangeTimes(complains.getChangeTimes());
				}
				adviceService.saveHandle(complain);
				addMessage(redirectAttributes, "处理过程保存成功");
				return "redirect:" + adminPath + "/crm/store/list";
			} else {
				addMessage(redirectAttributes, "没有保存处理过程");
				return "redirect:" + adminPath + "/crm/store/list";
			}
		}
	}
}
