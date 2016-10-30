package uc.dal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import uc.common.domain.GroupTable;
import uc.common.domain.UserInfo;

public class UcDAO {
	
	public static Set<GroupTable> resultSetGroupTable(ResultSet rs) {
		Set<GroupTable> Set = new HashSet<>();
		GroupTable g = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				g = new GroupTable();
				g.setGno(rs.getInt("GNO"));
				g.setGname(rs.getString("GNAME").trim());
				g.setGdate(rs.getTimestamp("GDATE"));
				Set.add(g);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return Set;
	}

	public static Set<UserInfo> resultSetUserInfos(ResultSet rs) {
		Set<UserInfo> set = new HashSet<>();
		UserInfo g = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				g = new UserInfo();
				g.setUc(rs.getInt("UC"));
				g.setNickName(rs.getString("NICKNAME"));
				g.setStatus(rs.getString("STATUS"));
				
				set.add(g);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return set;
	}
	
	public static UserInfo resultSetUserInfo(ResultSet rs) {
		UserInfo u = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				u = new UserInfo();
				u.setUc(rs.getInt("UC"));
				u.setNickName(rs.getString("NICKNAME"));
				u.setStatus(rs.getString("STATUS"));
					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return u;
	}
}
