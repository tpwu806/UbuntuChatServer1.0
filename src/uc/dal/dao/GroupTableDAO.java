package uc.dal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uc.pub.common.GroupTable;

public class GroupTableDAO {
	
	public static List<GroupTable> resultSetGroupTable(ResultSet rs) {
		List<GroupTable> list = new ArrayList<>();
		GroupTable g = null;
		if (rs == null)
			return Collections.emptyList();
		try {
			while (rs.next()) {
				g = new GroupTable();
				g.setGno(rs.getInt("GNO"));
				g.setGname(rs.getString("GNAME").trim());
				g.setGdate(rs.getTimestamp("GDATE"));
				list.add(g);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		System.out.println("list:" + list.toString());
		return list;
	}
}
