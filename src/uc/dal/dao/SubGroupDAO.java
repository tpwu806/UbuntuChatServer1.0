package uc.dal.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import uc.common.domain.SubGroup;
import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

public class SubGroupDAO {

	/**
	 * @Description:根据uc获取所有好友分组信息
	 * @auther: wutp 2016年11月6日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<SubGroup> getSubGroupByUc(Integer uc) {
		Set<SubGroup> set = null;
		Connection conn = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM subgroup WHERE uc = ?";
			String[] params = {String.valueOf(uc)};
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
