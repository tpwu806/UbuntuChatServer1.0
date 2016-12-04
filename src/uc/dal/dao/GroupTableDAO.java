package uc.dal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import uc.common.domain.GroupInfo;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

public class GroupTableDAO {

	/**
	 * @Description:根据用户昵称获取所有群信息
	 * @auther: wutp 2016年10月23日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<GroupInfo> getGroupTableByNickName(String nickname) {
		Set<GroupInfo> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT gt.* FROM userinfo u, usergroup ug,grouptable gt ";
			sql += "WHERE u.`NICKNAME` = ? ";
			sql += "AND u.`UC` = ug.`UC`";
			sql += "AND ug.`GNO` = gt.`GNO`;";
			String[] params = {nickname};
			rs = DbUtils.getResultSet2(conn,sql,params);
			set =  TableToDomain.resultSetGroupTable(rs);
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
