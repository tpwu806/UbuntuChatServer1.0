package ucs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySqlHelper {
	//������Ҫ�Ķ���
	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://127.0.0.1:3306/kilo";
	String user = "root";
	String pwd = "522300";
	int sum=0;
	//���캯������ʼ��ct
	public MySqlHelper()
	{
		try {
			//��������
			Class.forName(driver);
			//�õ�����
			con=DriverManager.getConnection(url,user,pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//[]params��ͨ��?��ֵ��ʽ���Է�ֹ©��ע�뷽ʽ����֤��ȫ��
	//��¼��֤ ���ر�
	public ResultSet queryExecute(String sql,String []params)
	{
		try {
			ps=con.prepareStatement(sql);
			//��sql�Ĳ�����ֵ
			for(int i=0;i<params.length;i++)
			{
				ps.setString(i+1, params[i]);
			}
			//ִ�в�ѯ
			rs=ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		//���ؽ����
		return rs;
	}
	//�鿴�ж��ټ�¼
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
		//���ؽ����
		return sum;
	}
	//��ɾ��
	public boolean updateExecete(String sql,String []params)
	{
		boolean b=true;
		try {
			ps=con.prepareStatement(sql);
			//��sql�Ĳ�����ֵ
			for(int i=0;i<params.length;i++)
			{
				ps.setString(i+1, params[i]);
			}
			//ִ�в�ѯ
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
		//���ؽ����
		return b;
		
	}
	//�ر���Դ�ķ���
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

