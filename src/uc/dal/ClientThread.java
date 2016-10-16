package uc.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uc.dal.sevice.TableModel;
import uc.dal.sevice.UserService;
import uc.dof.ServerJFrame;
import uc.pub.common.MessageBean;
import uc.pub.common.MessageType;

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
	private UserService userService ; 

	public ClientThread(Socket socket, ServerJFrame AppWindow) {
		this.clientsocket = socket;
		this.AppWindow = AppWindow;
		userService = new UserService();
	}

	volatile boolean running = true;

	@Override
	public void run() {
		try {
			MessageBean mbean;
			while (running) {
				ois = new ObjectInputStream(clientsocket.getInputStream());								
				bean = (MessageBean) ois.readObject();

				// 分析catbean中，type是那样一种类型
				switch (bean.getType()) {
				// 登录
				case MessageType.SIGN_IN: {
					System.out.println(bean.getType() + ":" + bean.getName() + bean.getPwd());
					mbean = new MessageBean();
					
					if (userService.checkUser(bean.getName().trim(), bean.getPwd().trim())) {
						System.out.println("sql验证成功");
						mbean.setType(MessageType.SIGN_IN_SUCCESS);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();

						ServerServer.signinThreads.put(bean.getName(), this);
						AppWindow.AddList(bean.getName());
						// 告诉其他人我上线了
						/*MessageBean serverBean = new MessageBean();
						serverBean.setType(MessageType.SERVER_SIGN_IN_NOTICE);
						serverBean.setName(bean.getName());
						serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "上线了");

						sendOtherAll(serverBean);*/
						updateFriendsList(bean.getName());

					} else {
						bean = new MessageBean();
						bean.setType(MessageType.SIGN_IN_FALSE);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();

						System.out.println("sql验证失败");
					}
					break;
				}
				// 注册
				case MessageType.SIGN_UP: {
					mbean = new MessageBean();
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
					break;
				}

				case MessageType.SIGN_OUT: { // 下线															
					// 告诉其他人我下线了
					MessageBean serverBean = new MessageBean();
					serverBean.setType(MessageType.SERVER_BROADCAST);
					serverBean.setName(bean.getName());
					serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "下线了");
					//sendOtherAll(serverBean);
					AppWindow.delList(bean.getName());
					
					ServerServer.signinThreads.remove(bean.getName());
					closeSockt();
					this.running=false;
					//return;
				}				
				case MessageType.CLIENT_CHAR: { // 聊天
					// 创建服务器的catbean，并发送给客户端
					MessageBean serverBean = new MessageBean();
					serverBean.setType(MessageType.CLIENT_CHAR);
					serverBean.setClients(bean.getClients());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());
					serverBean.setTimer(bean.getTimer());
					// 向选中的客户发送数据
					sendMessage(serverBean);
					break;
				}
				case MessageType.FILE_REQUESTION: { // 请求接受文件
					// 创建服务器的catbean，并发送给客户端
					MessageBean serverBean = new MessageBean();
					String info = bean.getTimer() + "  " + bean.getName() + "向你传送文件,是否需要接受";

					serverBean.setType(MessageType.FILE_REQUESTION);
					serverBean.setClients(bean.getClients()); // 这是发送的目的地
					serverBean.setFileName(bean.getFileName()); // 文件名称
					serverBean.setSize(bean.getSize()); // 文件大小
					serverBean.setInfo(info);
					serverBean.setName(bean.getName()); // 来源
					serverBean.setTimer(bean.getTimer());
					// 向选中的客户发送数据
					sendMessage(serverBean);

					break;
				}
				case MessageType.FILE_RECEIVE: { // 确定接收文件
					MessageBean serverBean = new MessageBean();

					serverBean.setType(MessageType.FILE_RECEIVE);
					serverBean.setClients(bean.getClients()); // 文件来源
					serverBean.setTo(bean.getTo()); // 文件目的地
					serverBean.setFileName(bean.getFileName()); // 文件名称
					serverBean.setIp(bean.getIp());
					serverBean.setPort(bean.getPort());
					serverBean.setName(bean.getName()); // 接收的客户名称
					serverBean.setTimer(bean.getTimer());
					// 通知文件来源的客户，对方确定接收文件
					sendMessage(serverBean);
					break;
				}
				case MessageType.FILE_RECEIVE_OK: {
					MessageBean serverBean = new MessageBean();

					serverBean.setType(MessageType.FILE_RECEIVE_OK);
					serverBean.setClients(bean.getClients()); // 文件来源\
					serverBean.setTo(bean.getTo()); // 文件目的地
					serverBean.setFileName(bean.getFileName());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());// 接收的客户名称
					serverBean.setTimer(bean.getTimer());
					sendMessage(serverBean);

					break;
				}
				case MessageType.SINGLETON_CHAR: { 					
					sendSingleton(bean);					
					break;
				}
				default: {
					break;
				}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeSockt();
		}
	}

	/**
	 * @Description:更新好友列表
	 * @auther: wutp 2016年10月15日
	 * @return void
	 */
	private void updateFriendsList(String name){
		//首先取得所有的在线用户
		HashSet<String> hs = new HashSet<>();		
		hs.addAll(ServerServer.signinThreads.keySet());
		MessageBean serverBean = new MessageBean();
		serverBean.setType(MessageType.SERVER_UPDATE_FRIENDS);
		serverBean.setClients(hs);
		
		sendMessage(serverBean);
	}

	/**
	 * @Description:// 向选中的用户发送数据
	 * @auther: wutp 2016年10月15日
	 * @param serverBean
	 * @return void
	 */
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
	
	/**
	 * @Description:一对一聊天
	 * @auther: wutp 2016年10月16日
	 * @param serverBean
	 * @return void
	 */
	private void sendSingleton(MessageBean bean){
		ObjectOutputStream oos;
		MessageBean serverBean;
		try {		
		Set<String> onlines = ServerServer.signinThreads.keySet();
		if(onlines.contains(bean.getFriendName())){
			Socket c = ServerServer.signinThreads.get(bean.getFriendName()).clientsocket;	
			
			serverBean = new MessageBean();
			serverBean.setType(MessageType.SINGLETON_CHAR);
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

	/**
	 * @Description:向所有的用户发送信息
	 * @auther: wutp 2016年10月14日
	 * @param serverBean
	 * @return void
	 */
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
