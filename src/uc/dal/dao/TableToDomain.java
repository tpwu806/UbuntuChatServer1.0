package uc.dal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import uc.common.domain.Friends;
import uc.common.domain.GroupInfo;
import uc.common.domain.FriendGroup;
import uc.common.domain.UserInfo;

public class TableToDomain {
	
	/**
	 * @Description:
	 * @auther: wutp 2016年11月6日
	 * @param rs
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<GroupInfo> resultSetGroupTable(ResultSet rs) {
		Set<GroupInfo> Set = new HashSet<>();
		GroupInfo g = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				g = new GroupInfo();
				g.setGid(rs.getString("GID"));
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

	/**
	 * @Description:
	 * @auther: wutp 2016年11月6日
	 * @param rs
	 * @return
	 * @return Set<UserInfo>
	 */
	public static Set<UserInfo> resultSetUserInfos(ResultSet rs) {
		Set<UserInfo> set = new HashSet<>();
		UserInfo g = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				g = new UserInfo();
				g.setUid(rs.getString("UID"));
				g.setNickname(rs.getString("NICKNAME"));
				g.setStatus(rs.getString("STATUS"));
				
				set.add(g);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return set;
	}
	
	/**
	 * @Description:
	 * @auther: wutp 2016年11月6日
	 * @param rs
	 * @return
	 * @return UserInfo
	 */
	public static UserInfo resultSetUserInfo(ResultSet rs) {
		UserInfo u = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				u = new UserInfo();
				u.setUid(rs.getString("UID"));
				u.setNickname(rs.getString("NICKNAME"));
				u.setStatus(rs.getString("STATUS"));
					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return u;
	}

	/**
	 * @Description:
	 * @auther: wutp 2016年11月6日
	 * @param rs
	 * @return
	 * @return Set<SubGroup>
	 */
	public static Set<FriendGroup> resultSetSubGroup(ResultSet rs) {
		Set<FriendGroup> Set = new HashSet<>();
		FriendGroup s = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				s = new FriendGroup();
				s.setSid(rs.getString("SID"));
				s.setSname(rs.getString("SNAME").trim());
				s.setUid(rs.getString("UID"));
				Set.add(s);		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
		//System.out.println("list:" + list.toString());
		return Set;
	}

	/**
	 * @Description:
	 * @auther: wutp 2016年11月27日
	 * @param rs
	 * @return
	 * @return Set<Friends>
	 */
	public static Set<Friends> resultSetToFriends(ResultSet rs) {
		Set<Friends> Set = new HashSet<>();
		Friends friend = null;
		if (rs == null)
			return null;
		try {
			while (rs.next()) {
				friend = new Friends();
				friend.setFdate(rs.getTimestamp("FDATE"));
				friend.setRemarks(rs.getString("REMARKS"));
				friend.setFid(rs.getString("FID"));
				friend.setSign(rs.getString("SIGN"));
				friend.setPhotoid(rs.getString("PHOTOID"));
				friend.setNickname(rs.getString("NICKNAME"));
				friend.setStatus(rs.getString("STATUS"));
				
				Set.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Set;
	}
}
