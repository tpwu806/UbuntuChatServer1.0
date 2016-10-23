package uc.dal.sevice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import uc.dal.db.ConnectionUtil;
import uc.dal.db.DbUtils;

/**
 * @Description: 
 * @author wutp 2016年10月16日
 * @version 1.0
 */
public class TableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<String> colums;
	public Vector<Vector> rows;

	public boolean UpdateModel(String sql, String[] params) {
		boolean result = false;
		Connection con = ConnectionUtil.getConnection();
		try {
			result = DbUtils.executeQuery(con,sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getNum(String sql) {
		int sum = 0;
		String params[] = {"1"}; 
		sql += "and 1 = ? ";
		Connection con = ConnectionUtil.getConnection();
		try {
			sum = DbUtils.getExecuteCount(con,sql,params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum;
	}

	// 显示表
	public void query(String sql, String[] params) {
		// 初始化

		colums = new Vector<String>();
		rows = new Vector<Vector>();
		// this.colums.add("员工号");
		// this.colums.add("姓名");
		// this.colums.add("性别");
		// this.colums.add("职位");
		Connection con = ConnectionUtil.getConnection();
		ResultSet rs = null;
		try {
			rs = DbUtils.getResultSet2(con,sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				this.colums.add(rsmd.getColumnName(i + 1));
			}
			while (rs.next()) {
				Vector<String> temp = new Vector<String>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					temp.add(rs.getString(i + 1));
				}
				rows.add(temp);
				// temp.add(rs.getString(1));
				// temp.add(rs.getString(2));
				// temp.add(rs.getString(3));
				// temp.add(rs.getString(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(con,null, rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getColumnCount() {
		return this.colums.size();
	}

	@Override
	public int getRowCount() {
		return this.rows.size();
	}

	@Override
	public String getColumnName(int arg0) {
		return this.colums.get(arg0).toString();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {	
		return rows.get(arg0).get(arg1);
	}

}
