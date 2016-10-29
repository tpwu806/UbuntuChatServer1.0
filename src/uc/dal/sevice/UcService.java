package uc.dal.sevice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import uc.dal.dao.UcDAO;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;
import uc.pub.common.MessageType;
import uc.pub.common.domain.GroupTable;
import uc.pub.common.domain.UserInfo;

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
	public String checkUser(String nickName, String pwd) {
		String result = "登录失败！";
		int power = -1;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "select STATUS from USERINFO where  nickname=? and pwd=?";
			String[] params = { nickName, pwd };
			rs = DbUtils.getResultSet2(conn,sql, params);
			if (rs.next())
				power = rs.getInt(1);
			if(power == 1)
				result = "已经登陆，不允许重复登录！";
			else if(power == 0)
				result = MessageType.SIGN_IN_SUCCESS;
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
	public static Set<GroupTable> getGroupTable(String name) {
		Set<GroupTable> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT gt.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE u.`NICKNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {name};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  UcDAO.resultSetGroupTable(rs);
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
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @return void
	 */
	public void initUserStatus(){
		Set<UserInfo> users = getAllUser();
		UpdateUser(users);
	}
	/**
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @param users
	 * @return void
	 */
	private void UpdateUser(Set<UserInfo> users){
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
	private Set<UserInfo> getAllUser() {
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE 1 = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {"1"};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  UcDAO.resultSetUserInfos(rs);
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
	 * @Description:
	 * @auther: wutp 2016年10月24日
	 * @param gname
	 * @return
	 * @return List<UserInfo>
	 */
	public Set<UserInfo> getGroupFriends(String gname) {
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE gt.`GNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {gname};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  UcDAO.resultSetUserInfos(rs);
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
	 * @Description:
	 * @auther: wutp 2016年10月28日
	 * @param name
	 * @return
	 * @return Set<UserInfo>
	 */
	public static Set<UserInfo> getUserInfos(String name) {
		UserInfo u = getUserInfo(name);
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, friends f ";
			sql += "WHERE f.`UC` = ? ";
			sql += "AND u.`UC` = f.`FUC`;";
			String[] params = {String.valueOf(u.getUc())};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  UcDAO.resultSetUserInfos(rs);
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
	 * @Description:
	 * @auther: wutp 2016年10月28日
	 * @param name
	 * @return
	 * @return Set<UserInfo>
	 */
	private static UserInfo getUserInfo(String name) {
		UserInfo u = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM userinfo WHERE `NICKNAME` = ? ";			
			String[] params = {name};
			rs = DbUtils.getResultSet2(conn,sql,params);
			u =  UcDAO.resultSetUserInfo(rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return u;
	}

	public static void main(String[] args) {
		Set<UserInfo> set = getUserInfos("system1");
		for(UserInfo u : set){
			System.out.println(u.getUc());
		}
	}

}
