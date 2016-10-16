package uc.dal.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class DbHelper {
	//������Ҫ�Ķ���
	Connection con=null;
	PreparedStatement ps=null;
	ResultSet rs=null;
	
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://192.168.0.111:3306/chat";
	String user = "root";
	String pwd = "123456";
	int sum=0;
	//���캯������ʼ��ct
	public DbHelper()
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
	
	/**
	 * @Description: ��ʾ��
	 * @auther: wutp 2016��10��16��
	 * @param sql
	 * @param params
	 * @return
	 * @return Vector[]
	 */
	public Vector[] query(String sql, String[] params) {
		// ��ʼ��
		Vector[] data = new Vector[2];
		Vector<String> colums = new Vector<String>();
		Vector<Vector> rows = new Vector<Vector>();
		//Vector[2] = new Vector[2];
		// this.colums.add("Ա����");
		// this.colums.add("����");
		// this.colums.add("�Ա�");
		// this.colums.add("ְλ");
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

