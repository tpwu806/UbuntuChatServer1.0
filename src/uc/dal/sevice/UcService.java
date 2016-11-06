package uc.dal.sevice;

import java.util.Set;

import uc.common.domain.Friends;
import uc.common.domain.GroupTable;
import uc.common.domain.SubGroup;
import uc.common.domain.UserInfo;
import uc.common.dto.UserInformation;
import uc.dal.dao.GroupTableDAO;
import uc.dal.dao.SubGroupDAO;
import uc.dal.dao.UserInfoDAO;

/**
 * @Description: 
 * @author wutp 2016年10月23日
 * @version 1.0
 */
public class UcService {

	/**
	 * @Description:登录验证
	 * @auther: wutp 2016年10月23日
	 * @param user
	 * @return
	 * @return UserInfo
	 */
	public UserInfo checkUser(UserInfo user) {
		UserInfo suser = null;
		suser = UserInfoDAO.getUserInfoByUcAndPwd(
				user.getUc().toString().trim(), user.getPwd().trim());
		if(user!=null&& "0".equals(user.getStatus().trim()))
			UserInfoDAO.UpdateSatusByNickNameAndStatus(
					suser.getNickname().trim(),1);
		return suser;
	}
	
	

	/**
	 * @Description:系统启动时，将所有用户状态设为离线
	 * @auther: wutp 2016年10月24日
	 * @return void
	 */
	public void initUserStatus(){
		Set<UserInfo> users = UserInfoDAO.getAllUser();
		for(UserInfo u: users){
			Integer uc = u.getUc();
			int status = 0;
			UserInfoDAO.UpdateSatusByUcAndStatus(uc,status);
		}
	}
	
	/**
	 * @Description:根据用户昵称获取所有群信息
	 * @auther: wutp 2016年10月23日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static Set<GroupTable> getGroupTableByNickName(String nickname) {
		return GroupTableDAO.getGroupTableByNickName(nickname);
	}



	/**
	 * @Description:根据群名称获取群用户信息
	 * @auther: wutp 2016年10月24日
	 * @param gname
	 * @return
	 * @return List<UserInfo>
	 */
	public Set<UserInfo> getGroupUserInfoByGname(String gname) {
	
		return UserInfoDAO.getGroupUserInfoByGname(gname);
	}

	/**
	 * @Description:根据用户名称获取好友信息
	 * @auther: wutp 2016年10月28日
	 * @param name
	 * @return
	 * @return Set<UserInfo>
	 */
	public static Set<UserInfo> getAllFriendsUserInfoByNickName(String name) {
		UserInfo u = UserInfoDAO.getUserInfoByNickName(name);
		Set<UserInfo> set = null;
		set = UserInfoDAO.getAllFriendsUserInfoByUc(u.getUc());
		return set;
	}
	
	
	
	/**
	 * @Description:根据用户昵称获取所有好友分组信息
	 * @auther: wutp 2016年11月6日
	 * @param name
	 * @return
	 * @return Set<GroupTable>
	 */
	public static UserInformation getUserInformationByNickName(String nickname) {
		UserInformation userInformation = null;
		UserInfo u = UserInfoDAO.getUserInfoByNickName(nickname);
		Set<SubGroup> subGroups = SubGroupDAO.getSubGroupByUc(u.getUc());
		return userInformation;
	}

	public static void main(String[] args) {
		Set<UserInfo> set = getAllFriendsUserInfoByNickName("system1");
		for(UserInfo u : set){
			System.out.println(u.getUc());
		}
	}

}
