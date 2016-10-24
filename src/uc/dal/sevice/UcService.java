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
	public boolean checkUser(String nickName, String pwd) {
		boolean result = false;
		int power = 0;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "select 1 from USERINFO where  nickname=? and pwd=?";
			String[] params = { nickName, pwd };
			rs = DbUtils.getResultSet2(conn,sql, params);
			if (rs.next())
				power = rs.getInt(1);
			if(power==1)
				result=true;
			changeSatus(nickName,1);
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
	 * @auther: wutp 2016年10月24日
	 * @param nickName
	 * @param status
	 * @return void
	 */
	private void changeSatus(String nickName,int status){
		String[] params = new String[2];
		params[0] = String.valueOf(status);
		params[1] = nickName;

		Connection conn = ConnectionUtil.getConnection();
		String sql = "UPDATE USERINFO SET STATUS = ? WHERE NICKNAME = ?";
		try {
			DbUtils.execute(conn,sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @return void
	 */
	public void initUserStatus(){
		List<UserInfo> users = getAllUser();
		UpdateUser(users);
	}
	/**
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @param users
	 * @return void
	 */
	private void UpdateUser(List<UserInfo> users){
		for(UserInfo u: users){
			Integer uc = u.getUc();
			Connection conn = ConnectionUtil.getConnection();
			String sql = "UPDATE USERINFO SET STATUS = 0 WHERE UC = ?";
			String[] params = {String.valueOf(uc)};
			try {
				DbUtils.execute(conn,sql,params);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @return
	 * @return List<UserInfo>
	 */
	private List<UserInfo> getAllUser() {
		List<UserInfo> list = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE 1 = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {"1"};
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
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @param gname
	 * @return
	 * @return List<UserInfo>
	 */
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
