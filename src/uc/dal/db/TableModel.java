package uc.dal.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;



	public class TableModel extends AbstractTableModel{
		public Vector<String> colums;
		public Vector<Vector> rows;
		
		public boolean UpdateModel(String sql,String []params)
		{ 
			DBUtils hp=new DBUtils();
			return hp.updateExecete(sql,params);
		}
		public int getNum(String sql)
		{
			DBUtils hp=new DBUtils();
			int sum=hp.queryExecute(sql);
			return sum;
		}
		//显示表
		public void query(String sql,String[]params)
		{
			//初始化
			
			colums=new Vector<String>();
			rows=new Vector<Vector>();
//			this.colums.add("员工号");
//			this.colums.add("姓名");
//			this.colums.add("性别");
//			this.colums.add("职位");
			DBUtils hp=new DBUtils();
			ResultSet rs=hp.queryExecute(sql, params);
			try {
				ResultSetMetaData rsmd=rs.getMetaData();
				for(int i=0;i<rsmd.getColumnCount();i++)
				{
					this.colums.add(rsmd.getColumnName(i+1));	
				}	
				while(rs.next())
				{
					Vector<String> temp=new Vector<String>();
					for(int i=0;i<rsmd.getColumnCount();i++)
					{
						temp.add(rs.getString(i+1));
					}
					rows.add(temp);
//					temp.add(rs.getString(1));
//					temp.add(rs.getString(2));
//					temp.add(rs.getString(3));
//					temp.add(rs.getString(4));	
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			finally
			{
				hp.close();
			}
		}
		


		@Override
		public int getColumnCount() {
			// TODO 自动生成的方法存根
			return this.colums.size();
		}

		@Override
		public int getRowCount() {
			// TODO 自动生成的方法存根
			return this.rows.size();
		}

		@Override
		public String getColumnName(int arg0) {
			// TODO 自动生成的方法存根
			return this.colums.get(arg0).toString();
		}
		@Override
		public Object getValueAt(int arg0, int arg1) {
			// TODO 自动生成的方法存根
			return ((Vector)rows.get(arg0)).get(arg1);
		}

}
