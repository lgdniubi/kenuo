package com.training.modules.train.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.common.Thread.ClearTokenThread;
import com.training.common.service.CrudService;
import com.training.modules.quartz.service.RedisClientTemplate;
import com.training.modules.sys.dao.UserDao;
import com.training.modules.sys.entity.User;
import com.training.modules.train.dao.TrainMenuDao;
import com.training.modules.train.dao.TrainModelDao;
import com.training.modules.train.entity.FzxRole;
import com.training.modules.train.entity.MediaRole;
import com.training.modules.train.entity.PCMenu;
import com.training.modules.train.entity.PcRole;
import com.training.modules.train.entity.TrainModel;

/**
 * 版本管理Service
 * @author 
 * @version 2018年3月14日
 */
@Service
@Transactional(readOnly = false)
public class TrainModelService extends CrudService<TrainModelDao,TrainModel> {

	@Autowired
	private TrainModelDao trainModelDao;
	@Autowired
	private TrainMenuDao trainMenuDao;
	@Autowired
	private PcRoleService pcRoleService;
	@Autowired
	private FzxRoleService fzxRoleService;
	@Autowired
	private MediaRoleService mediaRoleService;
	@Autowired
	private UserDao userDao;

	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	public static final String PC_FZX_CACHE = "pc_fzx_cache_";
	/**
	 * 根据版本英文名称查询是否存在
	 * @param modEname
	 * @return
	 */
	public int findByModEname(String modEname) {
		return dao.findByModEname(modEname);
	}
	
	/**
	 * 保存版本，如果新增就保存，修改就更新
	 * @param trainModel
	 */
	public void saveModel(TrainModel trainModel) {
		if (StringUtils.isEmpty(trainModel.getId())) {
			this.save(trainModel);
		}else{
			trainModel.preUpdate();
			trainModelDao.editTrainModel(trainModel);
		}
		
	}

	/**
	 * 根据id查询单条版本记录
	 * @param trainModel
	 * @return
	 */
	public TrainModel getTrainModel(TrainModel trainModel) {
		return trainModelDao.getTrainModel(trainModel);
	}

	/**
	 * 查找所有PC端的菜单
	 */
	public List<PCMenu> findAllpcMenu() {
		return trainMenuDao.findAllList(new PCMenu());
	}

	/**
	 * 查找该版本下的菜单
	 * @param trainModel
	 */
	public TrainModel findmodpcMenu(TrainModel trainModel) {
		return dao.findmodpcMenu(trainModel);
	}

	/**
	 * 保存pc版本菜单
	 * @param trainModel
	 */
	public void saveModpcMenu(TrainModel trainModel,String oldMenuIds) {
		TrainModel newModel = new TrainModel();
		dao.deleteModpcMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModpcMenu(newModel);
	        }
	        //插入pc_role超级管理员角色，并赋予该角色菜单
	        insertPCRoleAndMenu(trainModel);
		}
		//把新增加的菜单给各个超管
		changeSuperMenu(trainModel.getId(),oldMenuIds,trainModel.getMenuIds(),1);
	}

	//插入pc_role超级管理员角色，并赋予该角色菜单
	private void insertPCRoleAndMenu(TrainModel trainModel) {
		//根据版本id和ename=sjgly查找超级管理员角色
		String id = trainModel.getId();
		PcRole pcRole = pcRoleService.getPcRoleByModAndEname(id);
		if(pcRole == null){
			pcRole = new PcRole();
			pcRole.setName("超级管理员");
			pcRole.setEname("sjgly");
			pcRole.setGrade("100");
			pcRole.setRoleRange("0");
			pcRole.setModeid(Integer.valueOf(id));
			pcRole.setRemarks("版本设置权限创建的超级管理员");;
			pcRoleService.savepcRole(pcRole);
		}
		pcRole.setMenuIds(trainModel.getMenuIds());
		pcRoleService.saveRoleMenu(pcRole);
	}

	/**
	 * 查找该版本下的fzx菜单
	 * @param trainModel
	 */
	public TrainModel findmodfzxMenu(TrainModel trainModel) {
		return dao.findmodfzxMenu(trainModel);
	}

	/**
	 * 保存fzx版本菜单
	 * @param trainModel
	 */
	public void saveModfzxMenu(TrainModel trainModel,String oldMenuIds) {
		//把新增加的菜单给各个超管
		changeSuperMenu(trainModel.getId(),oldMenuIds,trainModel.getMenuIds(),2);
		TrainModel newModel = new TrainModel();
		dao.deleteModfzxMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModfzxMenu(newModel);
	        }
	        //插入fzx_role超级管理员角色，并赋予该角色菜单
	        insertFzxRoleAndMenu(trainModel);
		}
	}
	
	//清除使用该版本商家超管token
	private void clearToken(int mode_id){
		List<String> uids = this.userDao.findSuperManageUid(mode_id);
		if(uids != null && uids.size() > 0){
			for(String uid : uids){
				this.redisClientTemplate.del("UTOKEN_"+uid);
			}
		}
	}
	//插入fzx_role超级管理员角色，并赋予该角色菜单
	private void insertFzxRoleAndMenu(TrainModel trainModel) {
		//根据版本id和ename=sjgly查找超级管理员角色
		String modid = trainModel.getId();
		TrainModel model = dao.getTrainModel(trainModel);
		if("syrmf".equals(model.getModEname())){
			creatRole("手艺人免费管理员","syrmf",modid,"版本设置权限创建手艺人免费" ,trainModel.getMenuIds());
		}else if ("syrsf".equals(model.getModEname())){
			creatRole("手艺人收费管理员","syrsf",modid,"版本设置权限创建手艺人收费",trainModel.getMenuIds());
		}else if ("dy".equals(model.getModEname())){
			 creatRole("登云管理员","dy",modid,"版本设置权限创建登云",trainModel.getMenuIds());
		}else if ("pthy".equals(model.getModEname())){
			creatRole("普通会员管理员","pthy",modid,"版本设置权限创建普通会员",trainModel.getMenuIds());
		}else{
			creatRole("超级管理员","sjgly",modid,"版本设置权限创建超级",trainModel.getMenuIds());
		}
	}
/*.
 * ("ptyh".equals(model.getModEname())){
			fzxRole = fzxRoleService.getFzxRoleByModAndEname(modid,"ptyh");
			creatRole("普通用户管理员","ptyh",intModId,"版本设置权限创建普通用户",fzxRole);
		}else
 * */
	private FzxRole creatRole(String name, String enname, String modid, String remarks,String menuIds) {
		Integer intModId = Integer.valueOf(modid);
		FzxRole fzxRole = fzxRoleService.getFzxRoleByModAndEname(modid,enname);
		if(fzxRole == null){
			fzxRole = new FzxRole();
			fzxRole.setName(name);
			fzxRole.setEnname(enname);
			fzxRole.setModeid(intModId);
			fzxRole.setRemarks(remarks);
			fzxRole.setRoleGrade(100);
			fzxRoleService.saveFzxRole(fzxRole);
		}
		fzxRole.setMenuIds(menuIds);
		fzxRoleService.saveRoleMenu(fzxRole);
		return fzxRole;
	}

	/**
	 * 查找该版本下的自媒体菜单
	 * @param trainModel
	 */
	public TrainModel findmodMediaMenu(TrainModel trainModel) {
		return  dao.findmodMediaMenu(trainModel);
	}
	/**
	 * 保存自媒体版本菜单
	 * @param trainModel
	 */
	public void saveModMediaMenu(TrainModel trainModel,String oldMenuIds) {
		TrainModel newModel = new TrainModel();
		dao.deleteModMediaMenu(trainModel);
		if(!trainModel.getMenuIds().isEmpty()){
	        String[] ids = trainModel.getMenuIds().split(",");
	        for (int i = 0; i < ids.length; i++) {
	        	newModel.setId(trainModel.getId());
	        	newModel.setMenuId(Integer.valueOf(ids[i]));
	            dao.insertModMediaMenu(newModel);
	        }
	        //插入meida_role超级管理员角色，并赋予该角色菜单
	        insertMeidaRoleAndMenu(trainModel);
	        
		}
		//把新增加的菜单给各个超管
		changeSuperMenu(trainModel.getId(),oldMenuIds,trainModel.getMenuIds(),3);
	}
	//插入meida_role超级管理员角色，并赋予该角色菜单
	private void insertMeidaRoleAndMenu(TrainModel trainModel) {
		//根据版本id和ename=sjgly查找超级管理员角色
		String modid = trainModel.getId();
		MediaRole meidaRole = mediaRoleService.getMediaRoleByModAndEname(modid);
		if(meidaRole == null){
			meidaRole = new MediaRole();
			meidaRole.setName("超级管理员");
			meidaRole.setModeid(Integer.valueOf(modid));
			meidaRole.setRemarks("版本设置权限创建的超级管理员");;
			meidaRole.setType("2");
//			meidaRole.setPublicto("ab");
			mediaRoleService.savemediaRole(meidaRole);
			//将版本是登云的超管角色赋予平台admin
			setSuperMedia(meidaRole);
		}
		meidaRole.setMenuIds(trainModel.getMenuIds());
		mediaRoleService.saveRoleMenu(meidaRole);
	}
	/**
	 * 将版本是登云的超管角色赋予平台admin
	 * @param meidaRole
	 */
	private void setSuperMedia(MediaRole meidaRole) {
		User user = new User();
		user.setLoginName("admin");
		User loginName = userDao.getByLoginName(user);
		mediaRoleService.insertUserRole(loginName.getId(), meidaRole.getRoleId());
	}

	/**
	 * 查询登云的版本--1个
	 * @param trainModel
	 * @return
	 * @Description:
	 */
	public List<TrainModel> findDYModelList() {
		return dao.findDYModelList("dy");
	}

	public List<TrainModel> findModelListByType() {
		return dao.findDYModelList("qy");
	}
	/**
	 * 当版本菜单变化的时候同步给各个超管菜单
	 * @param oldMenuIds
	 * @param newMenuIds
	 * @param string 
	 */
	private void changeSuperMenu(String modId,String oldMenuIds, String newMenuIds, int sc) {
		String[] omenuid = oldMenuIds.split(",");
		//找出旧的有新的没有的菜单id，需要删除
		List<Integer> ls1 = findDifMenuId(newMenuIds, omenuid);
		if(ls1.size()>0){
			deleteOldMenuId(modId,ls1,sc);
		}
		String[] nmenuid = newMenuIds.split(",");
		//找出旧的没有新的有的菜单id，需要增加
		List<Integer> ls2 = findDifMenuId(oldMenuIds, nmenuid);
		if(ls2.size()>0){
			addNewMenuIdForSuper(modId,ls2,sc);
		}
		
	}
	/**
	 * 给此版本的所有超管新增菜单
	 * @param modId
	 * @param ls1
	 * @param sc
	 */
	private void addNewMenuIdForSuper(String modId, List<Integer> ls1, int sc) {
		switch (sc) {
		case 1://PC端菜单增加
			List<Integer> roleids = pcRoleService.findpcRoleByModId(modId);
			if(roleids !=null && roleids.size()>0){
				for (Integer oldid : ls1) {
					pcRoleService.insertUserRoleForRoleId(oldid,roleids);
				}
				List<String> list = userDao.findupdateUser(roleids);
				for (String userId : list) {
					redisClientTemplate.set(PC_FZX_CACHE + userId, userId);
				}
			}
			break;
		case 2://fzx端菜单增加
			List<Integer> fzxRoleids = fzxRoleService.findFzxRoleByModId(modId);
			if(fzxRoleids !=null && fzxRoleids.size()>0){
				for (Integer oldid : ls1) {
					fzxRoleService.insertUserRoleForRoleId(oldid,fzxRoleids);
				}
			}
			//清除使用该版本商家超管token
			clearToken(Integer.parseInt(modId));
			break;
		case 3://自媒体PC端菜单增加
			List<Integer> mdRoleids = mediaRoleService.findMediaRoleByModId(modId);
			if(mdRoleids !=null && mdRoleids.size()>0){
				for (Integer oldid : ls1) {
					mediaRoleService.insertUserRoleForRoleId(oldid,mdRoleids);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 删除旧的菜单，不勾选的菜单
	 * @param modId 
	 * @param ls1
	 * @param sc
	 */
	private void deleteOldMenuId(String modId, List<Integer> ls1, int sc) {
		switch (sc) {
		case 1://PC端删除
			for (Integer oldid : ls1) {
				pcRoleService.deleteRoleMenuForRoleId(oldid,Integer.valueOf(modId));
			}
			break;
		case 2://fzx端删除
			List<String> uids = this.userDao.findUidByMenu(Integer.valueOf(modId),ls1);
			//删除菜单查找使用这菜单的所有用户清除token
			if(uids != null && uids.size() > 0){
				ClearTokenThread thread = new ClearTokenThread(uids);
				new Thread(thread).start();
			}
			for (Integer oldid : ls1) {
				fzxRoleService.deleteRoleMenuForRoleId(oldid,Integer.valueOf(modId));
			}
			break;
		case 3://自媒体PC端删除
			for (Integer oldid : ls1) {
				mediaRoleService.deleteRoleMenuForRoleId(oldid,Integer.valueOf(modId));
			}
			break;
		default:
			break;
		}
		
	}

	/**
	 * 查找出不同的菜单
	 * @param newMenuIds
	 * @param omenuid
	 * @return
	 */
	private List<Integer> findDifMenuId(String newMenuIds, String[] omenuid) {
		List<Integer> ls = new ArrayList<>();
		if(StringUtils.isBlank(newMenuIds)){
			for (int i = 0; i < omenuid.length; i++) {
				ls.add(Integer.valueOf(omenuid[i]));
			}
		}else{
			List<String> list = Arrays.asList(newMenuIds.split(","));
			for (int i = 0; i < omenuid.length; i++) {
				if(!list.contains(omenuid[i])){
					ls.add(Integer.valueOf(omenuid[i]));
				}
			}
		}
		return ls;
	}
}
