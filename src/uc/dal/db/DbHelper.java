package uc.dal.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class DbHelper {
	//定义需要的对象
	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://192.168.0.111:3306/chat";
	String user = "root";
	String pwd = "123456";
	int sum=0;
	//构造函数，初始化ct
	public DbHelper()
	{
		try {
			//加载驱动
			Class.forName(driver);
			//得到连接
			con=DriverManager.getConnection(url,user,pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//[]params，通过?赋值方式可以防止漏洞注入方式，保证安全性
	//登录验证 返回表
	public ResultSet queryExecute(String sql,String []params)
	{
		try {
			ps=con.prepareStatement(sql);
			//对sql的参数赋值
			for(int i=0;i<params.length;i++)
			{
				ps.setString(i+1, params[i]);
			}
			//执行查询
			rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回结果集
		return rs;
	}
	//查看有多少记录
	public int queryExecute(String sql)
	{
		try {
			ps=con.prepareStatement(sql);
			
			rs=ps.executeQuery();
			if(rs.next())
			{
				sum=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		//返回结果集
		return sum;
	}
	//增删改
	public boolean updateExecete(String sql,String []params)
	{
		boolean b=true;
		try {
			ps=con.prepareStatement(sql);
			//对sql的参数赋值
			for(int i=0;i<params.length;i++)
			{
				ps.setString(i+1, params[i]);
			}
			//执行查询
			if(ps.executeUpdate()!=1)
			{
				b=false;
			}
		} catch (Exception e) {
			b=false;
			e.printStackTrace();
			// TODO: handle exception
		}
		finally
		{
			this.close();
		}
		//返回结果集
		return b;
		
	}
	
	/**
	 * @Description: 显示表
	 * @auther: wutp 2016年10月16日
	 * @param sql
	 * @param params
	 * @return
	 * @return Vector[]
	 */
	public Vector[] query(String sql, String[] params) {
		// 初始化
		Vector[] data = new Vector[2];
		Vector<String> colums = new Vector<String>();
		Vector<Vector> rows = new Vector<Vector>();
		//Vector[2] = new Vector[2];
		// this.colums.add("员工号");
		// this.colums.add("姓名");
		// this.colums.add("性别");
		// this.colums.add("职位");
		DbHelper hp = new DbHelper();
		ResultSet rs = hp.queryExecute(sql, params);
		try {
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
			data[0]=colums;
			data[1]=rows;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hp.close();
		}
		
		
		return data;
	}
	
	//关闭资源的方法
	public void close()
	{
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(con!=null) con.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}

