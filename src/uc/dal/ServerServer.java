package uc.dal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import uc.common.MessageBean;
import uc.common.MessageModel;
import uc.common.MessageType;
import uc.dal.sevice.UcService;
import uc.dof.ServerJFrame;
import uc.pub.tool.DataTool;

/**
 * @Description: 服务器后台
 * @author wutp 2016年10月13日
 * @version 1.0
 */
public class ServerServer implements Runnable{
	
	private static ServerSocket ss;	
	private Socket socket;
	private ServerJFrame AppWindow;
	public static ConcurrentHashMap<String, ClientThread> signinThreads;// 登录客户集合
	private ClientThread clientThread;
	
	volatile boolean running = true;

	static {
		try {
			ss = new ServerSocket(8520);
			signinThreads = new ConcurrentHashMap<String, ClientThread>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerServer(ServerJFrame AppWindow) {
		this.AppWindow = AppWindow;
		initUserStatus();
	}

	// 返回当前在线的人的线程
	@Deprecated
	private static String getAllOnLineUserid() {
		// 使用迭代器完成
		Iterator<String> it = signinThreads.keySet().iterator();
		String res = "";
		while (it.hasNext()) {
			res += it.next().toString() + " ";
		}
		return res;
	}

	/**
	 * @Description:服务器关闭监听
	 * @auther: wutp 2016年10月15日
	 * @return void
	 */
	public void closeServer() {
		this.running=false;
	}

	/**
	 * @Description:向所有在线用户发送广播
	 * @auther: wutp 2016年10月15日
	 * @param str
	 * @return void
	 */
	public void broadcast(String str) {
		MessageBean serverBean = new MessageBean();
		serverBean.setType(MessageType.SERVER_BROADCAST);
		MessageModel message = new MessageModel();
		message.setSender("系统管理员");
		message.setTime(DataTool.getTimer());
		message.setInfo(str);	
		serverBean.setObject(message);
		Collection<ClientThread> clients = ServerServer.signinThreads.values();
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

	// 强制下线
	public void kickAway(String id) {
		
		ServerServer.signinThreads.get(id).closeSockt();
		ServerServer.signinThreads.get(id).running=false;
		ServerServer.signinThreads.remove(id);
		broadcast(id + " " + " 被服务器管理员强制下线了");
		this.AppWindow.delList(id);
	}
	
	/**
	 * @Description:初始化用户在线状态
	 * @auther: wutp 2016年10月24日
	 * @return void
	 */
	private void initUserStatus(){
		UcService UcService = new UcService();
		try {
			UcService.initUserStatus2();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (running) {
				socket = ss.accept();
				clientThread = new ClientThread(socket,AppWindow);
				Thread t = new Thread(clientThread);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
