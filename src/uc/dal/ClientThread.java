package uc.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uc.dal.db.TableModel;
import uc.dal.db.UserService;
import uc.dof.ServerJFrame;
import uc.pub.common.ChatBean;

/**
 * @Description: 连接客户端的线程
 * @author wutp 2016年10月13日
 * @version 1.0
 */
public class ClientThread extends Thread {
	public Socket clientsocket;
	private ChatBean bean;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ServerJFrame AppWindow;
	public ClientThread(Socket socket,ServerJFrame AppWindow) {
		this.clientsocket = socket;
		this.AppWindow=AppWindow;
	}
	
	volatile boolean running = true;

	@Override
	public void run() {
		try {
			while (running) {
				ois = new ObjectInputStream(clientsocket.getInputStream());
				bean = (ChatBean) ois.readObject();

				// 分析catbean中，type是那样一种类型
				switch (bean.getType()) {
				// 登录
				case 11: {
					System.out.println(bean.getType() + ":" + bean.getName() + bean.getPwd());
					UserService user = new UserService();
					String power = new String(user.checkUser(bean.getName().trim(), bean.getPwd()).trim());
					if (power.equals("root")) {
						bean = new ChatBean();
						bean.setType(110);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();

						System.out.println("sql验证成功");
					} else {
						bean = new ChatBean();
						bean.setType(111);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();

						System.out.println("sql验证成功");
					}
					break;
				}
				// 注册
				case 12: {

					System.out.println(bean.getType() + bean.getName() + bean.getPwd());
					String sql = "insert into User values(?,?,?)";
					String name = bean.getName().trim();
					String pwd = bean.getPwd().trim();
					String power = "user";
					String[] params = { name, pwd, power };
					System.out.printf(name, pwd, power);
					TableModel tm = new TableModel();
					// String power=new
					// String(user.UpdateModel(bean.getName().trim(),
					// bean.getPwd()).trim());
					if (!tm.UpdateModel(sql, params)) {
						bean = new ChatBean();
						bean.setType(120);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();

						System.out.println("sql注册成功");
					} else {
						bean = new ChatBean();
						bean.setType(121);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();
						System.out.println("sql注册成功");
					}
					break;
				}

				/**
				 * 上线把线程
				 */
				case 0: { // 上线 //记录上线客户的用户名和端口在clientbean中
					//ClientBean cbean = new ClientBean();
					//cbean.setName(bean.getName());
					//cbean.setSocket(clientsocket);
					// 添加在线用户 
					UbuntuChatServer.onlineThreads.put(bean.getName(), this); //
					// 创建服务器的catbean，并发送给客户端
					ChatBean serverBean = new ChatBean();
					serverBean.setType(0);
					serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "上线了"); // 通知所有客户有人上线
																							// HashSet<String>
					HashSet<String> set = new HashSet<String>(); // 客户昵称
					set.addAll(UbuntuChatServer.onlineThreads.keySet());
					serverBean.setClients(set);
					sendAll(serverBean);
					AppWindow.AddList(bean.getName());
					break;
				}

				case -1: { // 下线
					// 创建服务器的catbean，并发送给客户端
					ChatBean serverBean = new ChatBean();
					serverBean.setType(-1);

					try {
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(serverBean);
						oos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}

					// onlines.remove(bean.getName());
					UbuntuChatServer.onlineThreads.remove(bean.getName());
					// 向剩下的在线用户发送有人离开的通知
					ChatBean serverBean2 = new ChatBean();
					serverBean2.setInfo(bean.getTimer() + "  " + bean.getName() + " " + " 下线了");
					serverBean2.setType(0);
					HashSet<String> set = new HashSet<String>();
					set.addAll(UbuntuChatServer.onlineThreads.keySet());
					serverBean2.setClients(set);

					AppWindow.delList(bean.getName());
					sendAll(serverBean2);
					return;
				}
				case 1: { // 聊天

					// 创建服务器的catbean，并发送给客户端
					ChatBean serverBean = new ChatBean();
					serverBean.setType(1);
					serverBean.setClients(bean.getClients());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());
					serverBean.setTimer(bean.getTimer());
					// 向选中的客户发送数据
					sendMessage(serverBean);
					break;
				}
				case 2: { // 请求接受文件
					// 创建服务器的catbean，并发送给客户端
					ChatBean serverBean = new ChatBean();
					String info = bean.getTimer() + "  " + bean.getName() + "向你传送文件,是否需要接受";

					serverBean.setType(2);
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
				case 3: { // 确定接收文件
					ChatBean serverBean = new ChatBean();

					serverBean.setType(3);
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
				case 4: {
					ChatBean serverBean = new ChatBean();

					serverBean.setType(4);
					serverBean.setClients(bean.getClients()); // 文件来源\
					serverBean.setTo(bean.getTo()); // 文件目的地
					serverBean.setFileName(bean.getFileName());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());// 接收的客户名称
					serverBean.setTimer(bean.getTimer());
					sendMessage(serverBean);

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

	// 向选中的用户发送数据
	private void sendMessage(ChatBean serverBean) {
		// 首先取得所有的values
		Set<String> cbs = UbuntuChatServer.onlineThreads.keySet();
		Iterator<String> it = cbs.iterator();
		// 选中客户
		HashSet<String> clients = serverBean.getClients();
		while (it.hasNext()) {
			// 在线客户
			String clientname = it.next();
			// 选中的客户中若是在线的，就发送serverbean
			if (clients.contains(clientname)) {
				Socket c = UbuntuChatServer.onlineThreads.get(clientsocket).clientsocket;
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(c.getOutputStream());
					oos.writeObject(serverBean);
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	// 向所有的用户发送数据
	private void sendAll(ChatBean serverBean) {
		Collection<ClientThread> clients = UbuntuChatServer.onlineThreads.values();
		Iterator<ClientThread> it = clients.iterator();
		ObjectOutputStream oos;
		while (it.hasNext()) {
			Socket c = it.next().clientsocket;
			try {
				oos = new ObjectOutputStream(c.getOutputStream());
				oos.writeObject(serverBean);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void DownLine(String id) {

		UbuntuChatServer.onlineThreads.remove(id);
		System.out.println(id);
		// 向剩下的在线用户发送有人离开的通知
		ChatBean serverBean2 = new ChatBean();
		serverBean2.setInfo(id + " " + " 被强制下线了");
		serverBean2.setType(0);
		HashSet<String> set = new HashSet<String>();
		set.addAll(UbuntuChatServer.onlineThreads.keySet());
		serverBean2.setClients(set);
		// ServerJFrame sjf=HashMapServer.getServerJFrame("server");
		AppWindow.delList(id);
		sendAll(serverBean2);
		// return;
	}

	// 关闭Socket
	private void closeSockt() {
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
