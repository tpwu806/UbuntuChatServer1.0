package uc.dal.sevice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import uc.dal.dao.UcDAO;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;
import uc.pub.common.GroupTable;
import uc.pub.common.UserInfo;

/**
 * @Description: 
 * @author wutp 2016年10月23日
 * @version 1.0
 */
public class UcService {

	/**
	 * @Description:
	 * @auther: wutp 2016年10月23日
	 * @param uid
	 * @param p
	 * @return
	 * @return boolean
	 */
	public boolean checkUser(String uid, String p) {
		boolean result = false;
		int power = 0;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "select 1 from USERINFO where  nickname=? and pwd=?";
			String[] params = { uid, p };
			rs = DbUtils.getResultSet2(conn,sql, params);
			if (rs.next())
				power = rs.getInt(1);
			if(power==1)
				result=true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @Description:
	 * @auther: wutp 2016年10月23日
	 * @param name
	 * @return void
	 */
	public static List<GroupTable> getGroupTable(String name) {
		List<GroupTable> list = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT gt.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE u.`NICKNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {name};
			rs = DbUtils.getResultSet2(conn,sql,params);
			list =  UcDAO.resultSetGroupTable(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<UserInfo> getGroupFriends(String gname) {
		List<UserInfo> list = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE gt.`GNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {gname};
			rs = DbUtils.getResultSet2(conn,sql,params);
			list =  UcDAO.resultSetUserInfo(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void main(String[] args){
		System.out.println("JJJJJJJJJJJJJJ");
		//getGroupTable("system1");
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		String uc = null;
		try {
			String sql = "select UC from USERINFO where  nickname=? ";
			String[] params = {"system1"};
			rs = DbUtils.getResultSet2(conn,sql, params);
			if (rs.next())
				uc = rs.getString(1);
			System.out.println(uc);
			
			sql = "select * from USERGROUP where UC = ?";
			params = new String[1];
			params[0] = uc;
			rs = DbUtils.getResultSet2(conn,sql, params);
			DbUtils.resultSetToList(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
