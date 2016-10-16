package uc.dal.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * @Title: DbHelper.java
 * @Description: ͳһ��ѯ�� ��Ԥ���룬ͨ��?��ֵ��ʽ���Է�ֹ©��ע�뷽ʽ����֤��ȫ�ԡ�
 * @author wutp
 * @version 1.0
 * @time 2016-8-17����4:37:39
 */
public class DbUtils {
	private static Connection conn=null;
	private static PreparedStatement ps=null;
	private static ResultSet rs=null;   
	private static Statement stmt = null;
	
	private DbUtils(){
	
	}	
	/**
	 * @Description: 
	 * @param sql
	 * @return PreparedStatement
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,String sql) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			System.out.println("ִ��sql: " + sql + "************************");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			ConnectionUtil.BackPreparedStatement(conn,ps, null);			
		}
		return ps;
	}

	/**
	 * @Description:
	 * @auther: wutongpeng 2016��9��25�� 
	 * @return
	 * @throws SQLException: Statement
	 */
	public static Statement getStatement(Connection conn) throws SQLException{
		try {
			stmt=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			ConnectionUtil.BackPreparedStatement(conn,ps, null);			
		}
		return stmt;
	}
	
	/**
	 * @Description:���ر�
	 * @param sql
	 * @param params	
	 * @return ResultSet
	 * @throws SQLException 
	 */
	public static ResultSet getResultSet(Connection conn,String sql,String []params) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)			
				ps.setString(i+1, params[i]);			
			rs=ps.executeQuery();
			System.out.println("ִ��sql: " + sql + "************************");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
	}
	
	/**
	 * @Description:��ʾ��
	 * @auther: wutp 2016��10��17��
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 * @return Vector[]
	 */
	public static Vector[] query(Connection conn,String sql, String[] params) 
			throws SQLException{
		// ��ʼ��
		Vector[] data = new Vector[2];
		Vector<String> colums = new Vector<String>();
		Vector<Vector> rows = new Vector<Vector>();
		// Vector[2] = new Vector[2];
		// this.colums.add("Ա����");
		// this.colums.add("����");
		// this.colums.add("�Ա�");
		// this.colums.add("ְλ");			
		try {
			ResultSet rs = getResultSet(conn,sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				colums.add(rsmd.getColumnName(i + 1));
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
			data[0] = colums;
			data[1] = rows;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return data;
	}

	/**
	 * @Description:�鿴�ж��ټ�¼
	 * @param sql
	 * @return
	 * @return int
	 * @throws SQLException 
	 */
	public static int getExecuteCount(String sql,String []params) throws SQLException{
		int sum=0;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			rs=ps.executeQuery();
			System.out.println("ִ��sql: " + sql + "************************");
			if(rs.next())
				sum=rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return sum;
	}
	
	/**
	 * @Description:��������
	 * @param sql
	 * @return
	 * @return String
	 * @throws SQLException 
	 */
	@Deprecated
	public static String getMaxCount(String sql,String value) throws SQLException
	{
		String max="0";
		try {
			ps=conn.prepareStatement(sql);			
			rs=ps.executeQuery();
			System.out.println("ִ��sql: " + sql + "************************");
			if(rs.next())
				max=rs.getString(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return max;
	}


	/**
	 * @Description:�����Ƿ��ָ����Ϣ
	 * @param sql
	 * @param params
	 * @return String
	 * @throws SQLException 
	 */
	public static boolean executeQuery(Connection conn,String sql,String []params) throws SQLException
	{		
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);			
			for(int i=0;i<params.length;i++){
				System.out.println("����Ϊ��" + params[i]);
 				ps.setString(i+1, params[i]);
 			}
			rs=ps.executeQuery();
			if(rs.next() && rs.getInt(1) >= 1)
				confInfo = true;			 					
		} catch (Exception e) {
			e.printStackTrace();
			throw e;		
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);		
		}
		return confInfo;		
	}


	/**
	 * @Description:��ɾ��
	 * @param sql
	 * @param params
	 * @return boolean
	 * @throws SQLException 
	 */
	public static boolean execute(String sql,String []params) throws SQLException
	{
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			if(ps.executeUpdate()==1)
				confInfo = true;
			System.out.println("ִ��sql: " + sql + "************************");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, null);
		}
		return confInfo;		
	}
	@Deprecated
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException {	
		try {
			if(stmt == null && rs == null)
				ConnectionUtil.BackPreparedStatement(conn);
			else if(rs == null)
				ConnectionUtil.BackPreparedStatement(conn,stmt);
			else
				ConnectionUtil.BackPreparedStatement(conn,stmt,rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
