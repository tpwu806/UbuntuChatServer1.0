package uc.dal.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description: 
 * @author wutp 2016年10月17日
 * @version 1.0
 */
public class ConnectionUtil {

	
	
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	static {
		Properties prop = new Properties();
    	InputStream in = Object.class.getResourceAsStream("Properties.properties");   	
    	try {    		
    		if(in == null)
    			throw new Exception("InputStream 读取失败！");
    		
    		prop.load(in);
    		driver = prop.getProperty(driver).trim();
    		url = prop.getProperty(url).trim();	
    		user = prop.getProperty(user).trim();
    		pwd = prop.getProperty(pwd).trim();    		
    		
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ConnectionUtil()throws Exception{
		
	}
	
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月17日
	 * @return
	 * @throws Exception
	 * @return Connection
	 */
	private static Connection getConnection()throws Exception{
		Connection con;	
		try {
			Class.forName(driver);			
			con=DriverManager.getConnection(url,user,pwd);
		} catch (Exception e) {			
			e.printStackTrace();
			throw e;
		}
		return con;
	}		
    
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月17日
	 * @param conn
	 * @param stmt
	 * @param rs
	 * @throws SQLException
	 * @return void
	 */
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Deprecated
	public static void BackPreparedStatement(Connection conn) throws SQLException{
		if(conn ==null)
			return;
		try {			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    @Deprecated
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt)
			throws SQLException {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}


