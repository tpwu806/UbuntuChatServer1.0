package uc.dal.sevice;

import java.sql.ResultSet;

import uc.dal.db.DbHelper;

public class UserService {

	public boolean checkUser(String uid, String p) {
		boolean result = false;
		int power = 0;
		DbHelper hp = null;
		try {
			String sql = "select 1 from User where  nickname=? and pwd=?";
			String[] params = { uid, p };
			hp = new DbHelper();
			ResultSet rs = hp.queryExecute(sql, params);
			if (rs.next())
				power = rs.getInt(1);
			if(power==1)
				result=true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hp.close();
		}
		return result;
	}

}
