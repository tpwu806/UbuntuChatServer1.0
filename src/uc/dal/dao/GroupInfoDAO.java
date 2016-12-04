package uc.dal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uc.common.CrowdGroupModel;
import uc.common.FriendItemModel;
import uc.common.GroupModel;
import uc.common.domain.GroupInfo;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;
import uc.pub.tool.ImagesFunction;

public class GroupInfoDAO extends BaseDAO{

	/**
	 * @Description:根据用户昵称获取所有群信息
	 * @auther: wutp 2016年10月23日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	@Deprecated
	public static ArrayList<GroupInfo> getGroupTableByNickName(String nickname) {
		ArrayList<GroupInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT gt.* FROM userinfo u, usergroup ug,groupinfo gt ";
			sql += "WHERE u.`NICKNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {nickname};
			rs = DbUtils.getResultSet2(conn,sql,params);
			//set =  TableToDomain.resultSetGroupTable(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return set;
	}
	/**
	 * @Description:根据gid获取群信息以及群好友信息
	 * @auther: wutp 2016年12月4日
	 * @param gid
	 * @return
	 * @return ArrayList<GroupModel>
	 */
	public static ArrayList<GroupModel> getGroupByGid(String gid) {
		ArrayList<GroupModel> glist = null;
		
		return null;
	}
	/**
	 * @Description:根据cgid、uid获取群信息以及群好友信息
	 * @auther: wutp 2016年12月4日
	 * @param cid
	 * @param uid
	 * @return
	 * @return ArrayList<GroupModel>
	 */
	public static ArrayList<GroupModel> getGroupByCgidAndUid(String cid, String uid) {
		ArrayList<GroupModel> glist = null;
		GroupModel gm;
		String sql = "SELECT g.* FROM groupinfo g,crowdgroup c "
				+ " WHERE c.CID = ? AND c.UID = ? AND c.GID = g.GID";
		Object[] params = new Object[2];
		params[0] = cid;
		params[1] = uid;
		List<Object> list = new CrowdGroupDAO().getResultList(sql, params);
		if(list != null && list.size()>0){
			glist = new ArrayList<>(); 
			for(Object o : list){
				GroupModel g = new GroupModel();
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) o;
				g.setGid(map.get("GID").toString());
				g.setGroupName(map.get("GNAME").toString());
				g.setGdate(map.get("GNAME"));
				String picture = map.get("PICTURE").toString();
				if(picture != null && !"".equals(picture)){
					g.setPicture(ImagesFunction.getImage(picture));
				}				
				g.setPower(map.get("POWER").toString());
				ArrayList<FriendItemModel> friends = UserInfoDAO.getUsersByGname(g.getGid());
				if(friends != null && friends.size() > 0){
					g.setFriends(friends);
				}
				glist.add(g);				
			}
		}
		return glist;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
