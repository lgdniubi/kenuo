package com.training.modules.train.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.training.common.mapper.JsonMapper;
import com.training.common.persistence.Page;
import com.training.common.service.CrudService;
import com.training.modules.ec.utils.WebUtils;
import com.training.modules.sys.dao.FranchiseeDao;
import com.training.modules.sys.entity.Area;
import com.training.modules.sys.entity.Franchisee;
import com.training.modules.sys.entity.Office;
import com.training.modules.sys.entity.User;
import com.training.modules.sys.service.FranchiseeService;
import com.training.modules.sys.service.OfficeService;
import com.training.modules.sys.utils.ParametersFactory;
import com.training.modules.sys.utils.UserUtils;
import com.training.modules.train.dao.UserCheckDao;
import com.training.modules.train.entity.ModelFranchisee;
import com.training.modules.train.entity.TrainCategorys;
import com.training.modules.train.entity.TrainLessons;
import com.training.modules.train.entity.UserCheck;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *  用户审核Service
 * @author 
 * @version 2018年3月14日
 */
@Service
@Transactional(readOnly = false)
public class UserCheckService extends CrudService<UserCheckDao,UserCheck> {

	@Autowired
	private UserCheckDao userCheckDao;
	@Autowired
	private FranchiseeDao franchiseeDao;
	@Autowired
	private FranchiseeService franchiseeService;
	@Autowired
	private OfficeService officeService;
	
	/**
	 * 保存用户审核的结果
	 * @param userCheck
	 */
	public void saveModel(UserCheck userCheck) {
		if (StringUtils.isNotEmpty(userCheck.getId())) {
			userCheckDao.editUserCheck(userCheck);
		}
	}

	/**
	 * 根据id查询单条版本记录
	 * @param trainModel
	 * @return
	 */
	public UserCheck getUserCheck(UserCheck userCheck) {
		return userCheckDao.getUserCheck(userCheck);
	}

	/**
	 * 分页查询审核信息记录
	 * @param page
	 * @param userCheck
	 * @return
	 */
	public Page<UserCheck> findList(Page<UserCheck> page, UserCheck userCheck) {
		// 设置分页参数
		userCheck.setPage(page);
		// 执行分页查询
		page.setList(userCheckDao.findList(userCheck));
		return page;
	}

	/**
	 * 保存修改的权限设置信息
	 * 保存手艺人的权益信息。
	 * @param modelFranchisee
	 */
	public void saveModelFranchisee(ModelFranchisee modelFranchisee) {
		save(modelFranchisee);
		//更改用户表type为手艺人类型
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			userCheckDao.updateUserType("syr",modelFranchisee.getUserid(),null);
		}
	}

	/**
	 * 保存企业和手艺人的权益信息
	 * @param modelFranchisee
	 */
	private void save(ModelFranchisee modelFranchisee) {
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			modelFranchisee.preInsert();
			userCheckDao.saveModelFranchisee(modelFranchisee);
		}else{
			modelFranchisee.preUpdate();
			userCheckDao.editModelFranchisee(modelFranchisee);
		}
	}

	/**
	 * 根据userID查询权限设置信息
	 * @param userid
	 * @return
	 */
	public ModelFranchisee getModelFranchiseeByUserid(String userid) {
		return userCheckDao.getModelFranchiseeByUserid(userid);
	}

	/**
	 * 保存企业的权益信息。
	 * 保存修改企业的权限设置信息
	 * @param modelFranchisee
	 * 在授权企业的时候去把train_franchisee_info(企业审核信息)中的企业信息入库到sys_franchisee和sys_office中，然后去授权在去更改sys_user表中此用户的归属商家机构等信息
	 */
	public void saveQYModelFranchisee(ModelFranchisee modelFranchisee) {
		if (StringUtils.isEmpty(modelFranchisee.getId())) {
			UserCheck userCheck = new UserCheck();
			userCheck.setId(modelFranchisee.getApplyid());
			userCheck.setUserid(modelFranchisee.getUserid());
			UserCheck find = getUserCheck(userCheck);
			//向商家和机构插入一条信息
			String franchid = saveFranchiseeAndOffice(find);
			
			modelFranchisee.setFranchiseeid(franchid);
			userCheckDao.updateUserType("qy",modelFranchisee.getUserid(),franchid);
			modelFranchisee.setUserid("0");
		}
		
		save(modelFranchisee);
		//更改用户表type为企业类型
	}

	private String saveFranchiseeAndOffice(UserCheck find) {
		find.preInsert();
		userCheckDao.saveFranchisee(find);
		String id = find.getId();
		saveOffice(id,find);
		return id ;
	}

	
	private void saveOffice(String id,UserCheck find) {
		//保存组织机构信息，给组织机构添加一条父类数据
		Office office = new Office();
		office.setId(id);//id
		Office parent = new Office();
		parent.setId("1");
		office.setParent(parent);
		office.setParentIds("0,"+parent.getId()+",");
		office.setName(find.getCompanyName());
		office.setArea(new Area("1111"));
//		office.setArea(franchisee.getArea());
		office.setSort(10);
		office.setCode(find.getCharterCard());
		office.setType("1");
		office.setGrade("2");
		office.setUseable("1");
		
		office.setCreateDate(new Date());
		office.setUpdateDate(new Date());
		office.setDelFlag("0");
//		office.setIconUrl(franchisee.getIconUrl());
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			office.setUpdateBy(user);
			office.setCreateBy(user);
		}
		officeService.franchiseeSaveOffice(office);//保存组织结构表
	}

	/**
	 * 获取企业的权益信息
	 * @param userid
	 * @return
	 */
	public ModelFranchisee getQYModelFranchiseeByUserid(String userid) {
		return userCheckDao.getQYModelFranchiseeByUserid(userid);
	}

	/**
	 * 审核成功后推送消息
	 * @param userCheck
	 * @return
	 */
	public String pushMsg(UserCheck userCheck) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add("35be8ac9632c9475ac67f9be3c340665");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cid_list", jsonArray);
		jsonObj.put("push_type", 1);
		Map<String, Object> map = new HashMap<String, Object>();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		map.put("content", "审核已经过了");
//		map.put("notify_id", "35be8ac9632c9475ac67f9be3c3");
		map.put("notify_type", 2);
		map.put("push_time", dateStr);
		map.put("title", "企业审核");
		jsonObj.put("content", map);
		
		String json =  this.push(jsonObj);
		return json;
	}
	/*
	 * 推送消息具体方法
	 */
	private String push(JSONObject jsonObj){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		//  用于加密
		String sign = WebUtils.MD5("train"+StringEscapeUtils.unescapeJava(jsonObj.toString())+"train");
		String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+jsonObj+"'}";
		
		HttpEntity<String> entity = new HttpEntity<String>(paramter,headers);
		
//		String push_url = ParametersFactory.getMtmyParamValues("push_url");
		String push_url = "http://10.10.8.22:8801/appServer/pushMsg/pushGT.do";
		
		String json = restTemplate.postForObject(push_url, entity, String.class);
		System.out.println("pushjsonStr == "+jsonObj);
		System.out.println(json);
		return json;
	}
	public static void main(String[] args) {
		UserCheck userCheck = new UserCheck();
		new UserCheckService().pushMsg(userCheck);
//		pppssssmmm();
	}
	
	private static void pppssssmmm() {
		JSONArray jsonArray = new JSONArray();
		jsonArray.add("35be8ac9632c9475ac67f9be3c340665");
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("cid_list", jsonArray);
		jsonObj.put("push_type", 1);
		Map<String, Object> map = new HashMap<String, Object>();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		map.put("content", "dalkdjadsa");
		map.put("notify_id", "35be8ac9632c9475ac67f9be3c3");
		map.put("notify_type", 2);
		map.put("push_time", dateStr);
		map.put("title", "地推透传");
		jsonObj.put("content", map);
		//jsonObj.put("notify_type", 2);
		//jsonObj.put("title", "title");
//		jsonObj.put("notify_type", 1);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		
		//  用于加密
		String sign = WebUtils.MD5("train"+StringEscapeUtils.unescapeJava(jsonObj.toString())+"train");
		String paramter = "{'sign':'"+sign+"' , 'jsonStr':'train"+jsonObj+"'}";
		
		HttpEntity<String> entity = new HttpEntity<String>(paramter,headers);
		
		String push_url = "http://10.10.8.22:8801/appServer/pushMsg/pushGT.do";
		
		String json = restTemplate.postForObject(push_url, entity, String.class);
		System.out.println("pushjsonStr == "+jsonObj);
		System.out.println(json);
		
	}
	
	/*String start = "";
	String end = "";
	if(map.get("start_time") != null)
		start = ((Timestamp) map.get("start_time")).toString();
	if(map.get("end_time") != null)
		end = ((Timestamp) map.get("end_time")).toString();
	if(StringUtils.isNotBlank(start))
		map.put("start_time", start.substring(0, start.lastIndexOf(".")));
	if(StringUtils.isNotBlank(end))
		map.put("end_time", end.substring(0, end.lastIndexOf(".")));
	map.put("push_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	
	String type = (String) map.get("notify_type");
	if("4".equals(type)){
		TrainLessons l = this.trainLessonsDao.get(((String)map.get("content")).trim());
		
		map.put("name", l.getName());
		map.put("pic", l.getCoverPic());
	}else if("5".equals(type)){
		TrainCategorys tc = this.trainCategorysDao.get(((String)map.get("content")).trim());
		map.put("name", tc.getName());
		map.put("pic",tc.getCoverPic());
	}*/
}
