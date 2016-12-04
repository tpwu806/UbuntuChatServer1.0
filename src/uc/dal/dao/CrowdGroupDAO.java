package uc.dal.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uc.common.CrowdGroupModel;
import uc.common.GroupModel;

public class CrowdGroupDAO extends BaseDAO {

	/**
	 * @Description:组织群分组信息，包括分组内的群信息、群内的好友信息
	 * @auther: wutp 2016年12月4日
	 * @param uid
	 * @return
	 * @return ArrayList<CrowdGroupModel>
	 */
	public static ArrayList<CrowdGroupModel> getCrowdGroupList(String uid) {
		ArrayList<CrowdGroupModel> lists = getCrowdGroups(uid);
		if(lists != null && lists.size()>0){
			for(CrowdGroupModel cgm : lists){
				ArrayList<GroupModel> groupList = GroupInfoDAO.getGroupByCgidAndUid(cgm.getCid(),uid);
				cgm.setGroupList(groupList);
			}
		}			
		return lists;
	}
	
	/**
	 * @Description:获取不重复的群分组
	 * @auther: wutp 2016年12月4日
	 * @param uid
	 * @return
	 * @return ArrayList<CrowdGroupModel>
	 */
	public static ArrayList<CrowdGroupModel> getCrowdGroups(String uid) {
		ArrayList<CrowdGroupModel> lists = null;
		CrowdGroupModel cgm = null;
		String sql = "select DISTINCT CID,CNAME from crowdgroup where UID = ?";
		Object[] params = new Object[1];
		params[0] = uid;
		List<Object> list = new CrowdGroupDAO().getResultList(sql, params);
		if(list != null && list.size()>0){
			lists = new ArrayList<>();
			for(Object o : list){				
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) o;
				cgm = new CrowdGroupModel();
				cgm.setCid(map.get("CID").toString());
				cgm.setCname(map.get("CNAME").toString());
				lists.add(cgm);
			}
		}
		return lists;
	}

}
