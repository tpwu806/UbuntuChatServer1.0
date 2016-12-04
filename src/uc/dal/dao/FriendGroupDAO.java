package uc.dal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import uc.common.domain.Friends;
import uc.common.domain.GroupInfo;
import uc.common.domain.FriendGroup;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

public class FriendGroupDAO extends BaseDAO{

	/**
	 * @Description:根据uc获取所有好友分组信息
	 * @auther: wutp 2016年11月6日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<FriendGroup> getSubGroupByUc(String uc) {
		Set<FriendGroup> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM friendgroup WHERE UID = ?";
			String[] params = {uc};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetSubGroup(rs);
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
	 * @Description:根据用户昵称获取所有分组信息
	 * @auther: wutp 2016年10月23日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<Friends> getFriendsOfSubGroupByUcAndSunGroupId(String uc,String fgid) {
		Set<Friends> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = " SELECT f.FDATE,f.REMARKS,f.FID,u.SIGN,u.PICTURE,u.NICKNAME,u.status";
			sql += " FROM friendinfo f, userinfo u";
			sql += " WHERE f.`FID` = u.`UID` AND f.`UID` = ? AND f.`FGID` = ?";
			String[] params = {uc,fgid};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetToFriends(rs);
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
