package uc.dal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import uc.common.domain.UserInfo;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

public class UserInfoDAO {
	
	/**
	 * @Description:根据昵称获取用户信息
	 * @param name
	 * @return
	 * @return Set<UserInfo>
	 */
	public static UserInfo getUserInfoByNickName(String name) {
		UserInfo u = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM userinfo WHERE `NICKNAME` = ? ";			
			String[] params = {name};
			rs = DbUtils.getResultSet2(conn,sql,params);
			u =  TableToDomain.resultSetUserInfo(rs);
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
	
	/**
	 * @Description:
	 * @auther: wutp 2016年11月27日
	 * @param uc
	 * @return
	 * @return UserInfo
	 */
	public static UserInfo getUserInfoByUc(String uc) {
		UserInfo u = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM userinfo WHERE UID = ? ";			
			String[] params = {uc};
			rs = DbUtils.getResultSet2(conn,sql,params);
			if(rs.next())
				u = new UserInfo();
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

	/**
	 * @Description:根据昵称获取用户信息
	 * @param name
	 * @return
	 * @return Set<UserInfo>
	 */
	public static UserInfo getUserInfoByUcAndPwd(String uc,String pwd) {
		UserInfo u = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "select * from USERINFO where  UID = ? and PWD = ?";
			String[] params = {uc,pwd};
			rs = DbUtils.getResultSet2(conn,sql,params);
			u =  TableToDomain.resultSetUserInfo(rs);
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
	
	/**
	 * @Description:登陆成功后，改变登录状态
	 * @auther: wutp 2016年10月24日
	 * @param nickName
	 * @param status
	 * @return void
	 */
	public static void UpdateSatusByNickNameAndStatus(String nickName,int status){
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
	 * @Description:改变登录状态
	 * @auther: wutp 2016年11月06日
	 * @param uc
	 * @param status
	 * @return void
	 */
	public static void UpdateSatusByUcAndStatus(String uc,String status){
		String[] params = new String[2];
		params[0] = status;
		params[1] = uc;

		Connection conn = ConnectionUtil.getConnection();
		String sql = "UPDATE USERINFO SET STATUS = ? WHERE UID = ?";
		try {
			DbUtils.execute(conn,sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description:初始化登录状态
	 * @auther: wutp 2016年11月27日
	 * @return void
	 */
	public static void UpdateSatus() {
		String[] params = new String[1];
		params[0] = "0";

		Connection conn = ConnectionUtil.getConnection();
		String sql = "UPDATE USERINFO SET STATUS = ? ";
		try {
			DbUtils.execute(conn,sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @Description:得到所有用户 
	 * @auther: wutp 2016年10月24日
	 * @return
	 * @return List<UserInfo>
	 */
	public static Set<UserInfo> getAllUser() {
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE 1 = ? ";
			sql += "AND u.`UID` = ug.`UID`";
			sql += "AND ug.`UID` = gt.`GID`;";
			String[] params = {"1"};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetUserInfos(rs);
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
	 * @Description:根据用户名称获取好友信息
	 * @auther: wutp 2016年10月28日
	 * @param uc
	 * @return
	 * @return Set<UserInfo>
	 */
	public static Set<UserInfo> getAllFriendsUserInfoByUc(String uc) {
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, friends f ";
			sql += "WHERE f.`UID` = ? ";
			sql += "AND u.`UID` = f.`FID`;";
			String[] params = {uc};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetUserInfos(rs);
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
	 * @Description:根据群名称获取群用户信息
	 * @auther: wutp 2016年10月24日
	 * @param gname
	 * @return
	 * @return List<UserInfo>
	 */
	public static Set<UserInfo> getGroupUserInfoByGname(String gname) {
		Set<UserInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT u.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE gt.`GNAME` = ? ";
			sql += "AND u.`UID` = ug.`UID`";
			sql += "AND ug.`GID` = gt.`GID`;";
			String[] params = {gname};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetUserInfos(rs);
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
