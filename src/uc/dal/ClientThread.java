package uc.dal;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uc.common.MessageBean;
import uc.common.MessageType;
import uc.common.UserModel;
import uc.common.UserInfoModel;
import uc.common.domain.GroupInfo;
import uc.common.domain.ResultObject;
import uc.common.domain.UserInfo;
import uc.dal.sevice.TableModel;
import uc.dal.sevice.UcService;
import uc.dof.ServerJFrame;

/**
 * @Description: 连接客户端的线程
 * @author wutp 2016年10月13日
 * @version 1.0
 */
public class ClientThread implements Runnable {

	public Socket clientsocket;

	private MessageBean bean;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ServerJFrame AppWindow;
	private UcService ucService ; 
	
	public ClientThread(Socket socket, ServerJFrame AppWindow) {
		this.clientsocket = socket;
		this.AppWindow = AppWindow;
		ucService = new UcService();
	}

	volatile boolean running = true;

	@Override
	public void run() {
		try {			
			while (running) {
				ois = new ObjectInputStream(clientsocket.getInputStream());								
				bean = (MessageBean) ois.readObject();
				System.out.println("ServerSocket Type: "+bean.getType());
				switch (bean.getType()) {
				// 登录
				case MessageType.SIGN_IN: {
					ActionSignIn(bean);
					break;
				}
				// 注册
				case MessageType.SIGN_UP: {
					//ActionSignUp(bean);					
					break;
				}
				// 下线	
				case MessageType.SIGN_OUT: { 														
					//ActionSignOut(bean);
					//return;
				}
				// 群聊天
				case MessageType.GROUP_CHAT: { 
					//ActionGroupChat(bean);
					break;
				}
				// 请求接受文件
				case MessageType.FILE_REQUESTION: { 
					//ActionFileRequestion(bean);
					break;
				}
				// 确定接收文件
				case MessageType.FILE_RECEIVE: { 
					//ActionFileReceive(bean);					
					break;
				}
				// 确定接收文件
				case MessageType.FILE_RECEIVE_OK: {
					//ActionFileReceiveOk(bean);					
					break;
				}
				// 一对一聊天
				case MessageType.SINGLETON_CHAT: { 					
					//ActionSingletonChat(bean);					
					break;
				}
				// 更新群好友
				case MessageType.GET_GROUP_FRIEND_LIST: {
					//ActionUpdateGroupFriends(bean);
					break;
				}
				// 更新群好友
				case MessageType.INIT_FRIEND_LIST: {
					ActionInitFriendList(bean);
					break;
				}
				default: {
					break;
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			closeSockt();
		}
	}
	
	private void ActionInitFriendList(MessageBean bean2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @return void
	 * @throws IOException 
	 */
	/*private void ActionSignIn(MessageBean bean) throws IOException{
		MessageBean mbean = new MessageBean();
		System.out.println(bean.getType() + ":" + bean.getUser().getUc() + bean.getUser().getPwd());
		
		
		//UserInfo su = ucService.checkUser(bean.getUser());
		UserInfo su = null;
		if (su!=null && !"1".equals(su.getStatus())) {
			System.out.println("sql验证成功");
			bean.setName(su.getNickname());
			//发送登录成功消息，更新后台在线列表
			mbean.setType(MessageType.SIGN_IN_SUCCESS);
			mbean.setUser(su);
			
			//初始化主界面信息
			Set<GroupTable> groupTables= UcService.getGroupTableByNickName(bean.getName().trim());
			//mbean = new MessageBean();
			//mbean.setType(MessageType.INIT_FRIEND_LIST);
			mbean.setGroups((HashSet<GroupTable>)groupTables);
			Set<UserInfo> fUsers= (Set<UserInfo>) UcService.getAllFriendsUserInfoByNickName(bean.getName().trim());
			mbean.setUsers((HashSet<UserInfo>)fUsers);
			
			sendSingletonMessage(mbean);

			// 告诉其他人我上线了
			Set<UserInfo> allUsers = new HashSet<>();
			allUsers.addAll(fUsers);
			for(GroupTable g : groupTables){
				HashSet<UserInfo> groupFriends= (HashSet<UserInfo>) 
						ucService.getGroupUserInfoByGname(g.getGname().trim());
				allUsers.addAll(groupFriends);
			}
			allUsers.remove(bean.getName());
			updateFriendsList(allUsers,bean.getName());
			
			ServerServer.signinThreads.put(bean.getName(), this);
			AppWindow.AddList(bean.getName());
		} else {
			mbean = new MessageBean();
			mbean.setType(MessageType.SIGN_IN_FALSE);
			if("1".equals(su.getStatus())){
				mbean.setErrorMessage("已经登陆，不允许重复登录！");
			}
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mbean);
			oos.flush();

			System.out.println("sql验证失败");
		}
	}*/
	
	/**
	 * @Description:
	 * @auther: wutp 2016年11月5日
	 * @return void
	 * @throws IOException 
	 * @throws SQLException 
	 */
	private void ActionSignIn(MessageBean bean) 
			throws IOException, SQLException{
		MessageBean mbean = new MessageBean();		
		UserModel userModel = (UserModel) bean.getObject();
		System.out.println(bean.getType() + ":" 
				+ userModel.toString()+"-"+userModel.getPassword());
		ResultObject RO = ucService.checkUser(userModel);
		
		if(RO.ErrorCode == 1){
			System.out.println("sql验证成功");
			//发送登录成功消息，更新后台在线列表
			mbean.setType(MessageType.SIGN_IN_SUCCESS);
					
			UserInfoModel userinfo = UcService.verificationUser(userModel);
			mbean.setObject(userinfo);
			sendSingletonMessage(mbean);
			
			ServerServer.signinThreads.put(userModel.toString(), this);
			AppWindow.AddList(userinfo.getUserModel().getNickName());				
		}else if(RO.ErrorCode == 0){			
			mbean = new MessageBean();
			mbean.setType(MessageType.SIGN_IN_FALSE);			
			mbean.setErrorMessage("服务器异常！");			
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mbean);
			oos.flush();		
		} else {
			mbean = new MessageBean();
			mbean.setType(MessageType.SIGN_IN_FALSE);			
			mbean.setErrorMessage(RO.ErrorString);			
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mbean);
			oos.flush();
		}
	}
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @param bean
	 * @throws IOException
	 * @return void
	 */
	/*private void ActionSignUp(MessageBean bean) throws IOException{
		MessageBean mbean = new MessageBean();
		System.out.println(bean.getType() + bean.getName() + bean.getPwd());
		String sql = "insert into User values(?,?,?)";
		String name = bean.getName().trim();
		String pwd = bean.getPwd().trim();
		String power = "user";
		String[] params = { name, pwd, power };
		System.out.printf(name, pwd, power);
		TableModel tm = new TableModel();

		if (!tm.UpdateModel(sql, params)) {
			mbean.setType(MessageType.SIGN_UP_SUCCESS);
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mbean);
			oos.flush();

			System.out.println("sql注册成功");
		} else {
			mbean.setType(MessageType.SIGN_UP_FALSE);
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mbean);
			oos.flush();
			System.out.println("sql注册成功");
		}
	}

	*//**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @param bean
	 * @throws IOException
	 * @return void
	 *//*
	private void ActionSignOut(MessageBean bean) throws IOException{
		// 告诉其他人我下线了
		MessageBean mbean = new MessageBean();
		mbean.setType(MessageType.SERVER_BROADCAST);
		mbean.setName(bean.getName());
		mbean.setInfo(bean.getTimer() + "  " + bean.getName() + "下线了");
		AppWindow.delList(bean.getName());
		
		ServerServer.signinThreads.remove(bean.getName());
		closeSockt();
		this.running=false;
	}
	
	private void ActionGroupChat(MessageBean bean) throws IOException{
		// 创建服务器的catbean，并发送给客户端
		MessageBean mbean = new MessageBean();
		mbean.setType(MessageType.GROUP_CHAT);		
		mbean.setClients(bean.getClients());
		mbean.setInfo(bean.getInfo());
		mbean.setName(bean.getName());
		mbean.setGroupName(bean.getGroupName());
		mbean.setTimer(bean.getTimer());
		// 向选中的客户发送数据
		sendMessage(mbean);
	}
	*//**
	 * @Description:一对一聊天
	 * @auther: wutp 2016年10月16日
	 * @param serverBean
	 * @return void
	 *//*
	private void ActionSingletonChat(MessageBean bean)throws IOException{
		ObjectOutputStream oos;
		MessageBean serverBean;
		try {		
		Set<String> onlines = ServerServer.signinThreads.keySet();
		if(onlines.contains(bean.getFriendName())){
			Socket c = ServerServer.signinThreads.get(bean.getFriendName()).clientsocket;	
			
			serverBean = new MessageBean();
			serverBean.setType(MessageType.SINGLETON_CHAT);
			serverBean.setName(bean.getName());
			serverBean.setFriendName(bean.getFriendName());
			serverBean.setTimer(bean.getTimer());
			serverBean.setInfo(bean.getInfo());
			
			oos = new ObjectOutputStream(c.getOutputStream());
			oos.writeObject(serverBean);
			oos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	*//**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @param bean
	 * @throws IOException
	 * @return void
	 *//*
	private void ActionFileRequestion(MessageBean bean)throws IOException{
		// 创建服务器的catbean，并发送给客户端
		MessageBean mbean = new MessageBean();
		String info = bean.getTimer() + "  " + bean.getName() + "向你传送文件,是否需要接受";

		mbean.setType(MessageType.FILE_REQUESTION);
		mbean.setClients(bean.getClients()); // 这是发送的目的地
		mbean.setFileName(bean.getFileName()); // 文件名称
		mbean.setSize(bean.getSize()); // 文件大小
		mbean.setInfo(info);
		mbean.setName(bean.getName()); // 来源
		mbean.setTimer(bean.getTimer());
		// 向选中的客户发送数据
		sendMessage(mbean);
	}
	*//**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @param bean
	 * @throws IOException
	 * @return void
	 *//*
	private void ActionFileReceive(MessageBean bean)throws IOException{
		MessageBean mbean = new MessageBean();
		mbean.setType(MessageType.FILE_RECEIVE);
		mbean.setClients(bean.getClients()); // 文件来源
		mbean.setTo(bean.getTo()); // 文件目的地
		mbean.setFileName(bean.getFileName()); // 文件名称
		mbean.setIp(bean.getIp());
		mbean.setPort(bean.getPort());
		mbean.setName(bean.getName()); // 接收的客户名称
		mbean.setTimer(bean.getTimer());
		// 通知文件来源的客户，对方确定接收文件
		sendMessage(mbean);
	}
	*//**
	 * @Description:
	 * @auther: wutp 2016年10月22日
	 * @param bean
	 * @throws IOException
	 * @return void
	 *//*
	private void ActionFileReceiveOk(MessageBean bean)throws IOException{
		MessageBean mbean = new MessageBean();

		mbean.setType(MessageType.FILE_RECEIVE_OK);
		mbean.setClients(bean.getClients()); // 文件来源\
		mbean.setTo(bean.getTo()); // 文件目的地
		mbean.setFileName(bean.getFileName());
		mbean.setInfo(bean.getInfo());
		mbean.setName(bean.getName());// 接收的客户名称
		mbean.setTimer(bean.getTimer());
		sendMessage(mbean);

	}

	private void ActionUpdateGroupFriends(MessageBean bean) {
		HashSet<UserInfo> groupFriends= (HashSet<UserInfo>) ucService.getGroupUserInfoByGname(bean.getGroupName().trim());
		MessageBean mbean = new MessageBean();
		mbean.setType(MessageType.GET_GROUP_FRIEND_LIST);
		mbean.setGroupName(bean.getGroupName());
		mbean.setUsers(groupFriends);
		
		sendSingletonMessage(mbean);
		
	}

	*//**
	 * @Description:更新好友列表
	 * @auther: wutp 2016年10月15日
	 * @return void
	 *//*
	private void updateFriendsList(Set<UserInfo> allUsers,String name){
		//首先取得所有的在线用户
		HashSet<String> hs = new HashSet<>();
		for(UserInfo u : allUsers){
			hs.add(u.getNickname());
		}
		hs.remove(name);
		MessageBean serverBean = new MessageBean();
		serverBean.setType(MessageType.SERVER_UPDATE_FRIENDS);
		serverBean.setClients(hs);
		serverBean.setName(name);
		//serverBean.setUsers((HashSet<UserInfo>)allUsers);
		
		sendMessage(serverBean);
	}

	*//**
	 * @Description:// 向选中的用户发送数据
	 * @auther: wutp 2016年10月15日
	 * @param serverBean
	 * @return void
	 *//*
	private void sendMessage(MessageBean serverBean) {
		// 首先取得所有的在线用户
		Set<String> onlines = ServerServer.signinThreads.keySet();
		Iterator<String> it = onlines.iterator();
		// 选中客户
		HashSet<String> clients = serverBean.getClients();

		while (it.hasNext()) {
			// 选中客户
			String onlineClientName = it.next();
			// 选中的客户中若是在线的，就发送serverbean
			if (clients.contains(onlineClientName)) {
				Socket c = ServerServer.signinThreads.get(onlineClientName).clientsocket;
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	*//**
	 * @Description:向所有的用户发送信息
	 * @auther: wutp 2016年10月14日
	 * @param serverBean
	 * @return void
	 *//*
	@Deprecated
	private void sendOtherAll(MessageBean serverBean) {
		//首先取得所有的在线用户
		Set<String> signins = ServerServer.signinThreads.keySet();
		signins.remove(serverBean.getName());
		if(!signins.isEmpty()){
			Iterator<String> it = signins.iterator();

			while (it.hasNext()) {
				// 选中客户
				String onlineClientName = it.next();

				Socket c = ServerServer.signinThreads.get(onlineClientName).clientsocket;
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}*/
	
	/**
	 * @Description:// 向选中的用户发送数据
	 * @auther: wutp 2016年10月15日
	 * @param serverBean
	 * @return void
	 */
	private void sendSingletonMessage(MessageBean mBean) {
		try {
			oos = new ObjectOutputStream(clientsocket.getOutputStream());
			oos.writeObject(mBean);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @Description:关闭Socket
	 * @auther: wutp 2016年10月14日
	 * @return void
	 */
	public void closeSockt() {
		if (oos != null) {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (ois != null) {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (clientsocket != null) {
			try {
				clientsocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
