package ucs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySqlHelper {
	//定义需要的对象
	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://127.0.0.1:3306/kilo";
	String user = "root";
	String pwd = "522300";
	int sum=0;
	//构造函数，初始化ct
	public MySqlHelper()
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
			// TODO: handle exception
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

