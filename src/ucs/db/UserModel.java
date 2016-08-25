package ucs.db;

import java.sql.ResultSet;


public class UserModel {
	
	public String checkUser(String uid,String p)
	{
		String power = null;
		MySqlHelper hp=null;
		try{
		String sql="select power from User where  name=? and pwd=?";
		String []params={uid,p};
		hp=new MySqlHelper();
		ResultSet rs=hp.queryExecute(sql, params);
		if(rs.next())
		{
			power=rs.getString(1);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			hp.close();
		}
		return power;
	}
	
}



