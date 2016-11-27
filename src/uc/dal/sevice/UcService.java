package uc.dal.sevice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;

import uc.common.FriendItemModel;
import uc.common.GroupModel;
import uc.common.User;
import uc.common.UserInformation;
import uc.common.domain.Friends;
import uc.common.domain.GroupTable;
import uc.common.domain.ResultObject;
import uc.common.domain.SubGroup;
import uc.common.domain.UserInfo;
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
	public ResultObject checkUser(UserInfo user) {
		ResultObject PO = new ResultObject();
		UserInfo suser = null;	
		suser = UserInfoDAO.getUserInfoByUc(user.getUc().toString().trim());
		if(suser == null){
			return PO = new ResultObject(null, -1, "查无此账号，请先注册！");
		}
		suser = UserInfoDAO.getUserInfoByUcAndPwd(
				user.getUc().toString().trim(), user.getPwd().trim());		
		if(suser == null){
			return PO = new ResultObject(null, -1, "密码错误，请输入正确密码！");
		}
		if( "0".equals(suser.getStatus().trim())){
			PO = new ResultObject(suser, 1, "");
			UserInfoDAO.UpdateSatusByNickNameAndStatus(
					suser.getNickname().trim(),1);			
		}else{
			PO = new ResultObject(null, -1, "已经登陆，不允许重复登陆");			
		}
		return PO;
	}
	
	

	/**
	 * @Description:系统启动时，将所有用户状态设为离线
	 * @auther: wutp 2016年10月24日
	 * @return void
	 */
	@Deprecated
	public void initUserStatus(){
		Set<UserInfo> users = UserInfoDAO.getAllUser();
		for(UserInfo u: users){
			Integer uc = u.getUc();
			int status = 0;
			UserInfoDAO.UpdateSatusByUcAndStatus(uc,status);
		}
	}
	
	/**
	 * @Description:系统启动时，将所有用户状态设为离线
	 * @auther: wutp 2016年11月27日
	 * @return void
	 */
	public void initUserStatus2(){
		UserInfoDAO.UpdateSatus();
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
	@Deprecated
	public static UserInformation getUserInformationByNickName(String nickname) {
		UserInformation userInformation = null;
		UserInfo u = UserInfoDAO.getUserInfoByNickName(nickname);
		Set<SubGroup> subGroups = SubGroupDAO.getSubGroupByUc(u.getUc());
		return userInformation;
	}

	/**
	 * @Description:验证用户
	 * @auther: wutp 2016年11月27日
	 * @param user
	 * @param nickName 
	 * @return
	 * @throws SQLException
	 * @return UserInformation
	 */
	public static UserInformation verificationUser(User user, String nickName) throws SQLException {
		UserInformation userInformation = null ;
		// 当前程序所在路径
		String route = System.getProperty("user.dir") + "/";
		// 取得账户信息
		String uc = user.toString();
		Set<SubGroup> SubGroups = null;							
		//继续查询该用户的分组			
		SubGroups = SubGroupDAO.getSubGroupByUc(Integer.valueOf(uc));
		ArrayList<GroupModel> groupModels = new ArrayList<GroupModel>();

		if(SubGroups != null){
			for(SubGroup subGroup : SubGroups) {
				// 取得分组名并构建
				String groupName = subGroup.getSname();
				GroupModel group = new GroupModel(groupName);
				group.setSno(subGroup.getSno());
				group.setSdate(subGroup.getSdate());
				group.setUc(subGroup.getUc());
				//继续查询该分组下的好友
				Set<Friends> friends = SubGroupDAO.getFriendsOfSubGroupByUcAndSunGroupId(uc, group.getSno().toString().trim());
				if(friends != null && friends.size() > 0){
					for (Friends friend : friends) {
						String headFile = route + friend.getPhotoid();
						byte[] head = createByte(new File(headFile));
						boolean hasUpdate = false;
						switch (friend.getStatus()) {
						case "0":
							hasUpdate = false;
						case "1":
							hasUpdate = true;
						}
						FriendItemModel friendModel = new FriendItemModel(
								head, 
								friend.getRemarks(), 
								friend.getNickname(),
								hasUpdate, 
								friend.getSign(), 
								friend.getFuc().toString(),
								friend.getStatus());
									
						group.add(friendModel);
					}
				}
				groupModels.add(group);
			}
		}
		userInformation = new UserInformation(user, nickName,groupModels);
		return userInformation;	
	}
	// 将图像转换为字节数组好用于传输
	public static byte[] createByte(File s) {
		BufferedImage bu;
		try {
			bu = ImageIO.read(s);
			ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
			try {
				ImageIO.write(bu, "png", imageStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return imageStream.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Set<UserInfo> set = getAllFriendsUserInfoByNickName("system1");
		for(UserInfo u : set){
			System.out.println(u.getUc());
		}
	}

}
