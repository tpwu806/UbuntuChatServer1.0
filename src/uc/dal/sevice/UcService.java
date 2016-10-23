package uc.dal.sevice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

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

}
