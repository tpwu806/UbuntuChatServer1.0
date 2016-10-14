package uc.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uc.dal.db.TableModel;
import uc.dal.db.UserService;
import uc.dof.ServerJFrame;
import uc.pub.common.ChatBean;

/**
 * @Description: 服务器后台
 * @author wutp 2016年10月13日
 * @version 1.0
 */
public class UbuntuChatServer {
	private static ServerSocket ss;
	// public static HashMap<String, ClientBean> onlines;//客户端信息
	public static HashMap<String, ClientThread> onlineThreads;// 客户端信息
	private Socket socket;
	private ServerJFrame AppWindow;
	private ClientThread clientThread;

	static {
		try {
			ss = new ServerSocket(8520);
			// onlines = new HashMap<String, ClientBean>();
			onlineThreads = new HashMap<String, ClientThread>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UbuntuChatServer(ServerJFrame AppWindow) {
		this.AppWindow = AppWindow;
	}

	// 返回当前在线的人的线程
	private static String getAllOnLineUserid() {
		// 使用迭代器完成
		Iterator<String> it = onlineThreads.keySet().iterator();
		String res = "";
		while (it.hasNext()) {
			res += it.next().toString() + " ";
		}
		return res;
	}

	// 服务器开启监听
	public void startServer() {
		try {
			while (true) {
				socket = ss.accept();
				clientThread = new ClientThread(socket,AppWindow);
				clientThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 服务器关闭监听
	public void closeServer() {
		// clientThread.stop();
	}

	// 发送广播
	public void broadcast(String str) {
		ChatBean serverBean = new ChatBean();
		serverBean.setType(1);
		serverBean.setInfo(str);
		HashSet<String> set = new HashSet<String>();
		// set.addAll(onlines.keySet());
		set.addAll(onlineThreads.keySet());

		serverBean.setClients(set);
		//clientThread.sendAll(serverBean);
	}

	// 强制下线
	public void kickAway(String id) {
		//clientThread.DownLine(id);
		this.AppWindow.delList(id);
	}

	

	

}
