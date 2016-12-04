package uc.dal.dao;

import java.sql.Connection;
import java.util.List;

import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

public class BaseDAO {
	Connection conn = null;
	public BaseDAO(){
		conn = ConnectionUtil.getConnection();
	}
	public List<Object> getResultList(String sql, Object[] params){
		List<Object> list = null;
		try {
			list = DbUtils.excuteQuery(conn, sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;		
	}
}
