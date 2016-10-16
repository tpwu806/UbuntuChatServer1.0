package uc.dal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import uc.dof.ServerJFrame;
import uc.pub.common.MessageBean;
import uc.pub.common.MessageType;

/**
 * @Description: ��������̨
 * @author wutp 2016��10��13��
 * @version 1.0
 */
public class ServerServer implements Runnable{
	
	private static ServerSocket ss;	
	private Socket socket;
	private ServerJFrame AppWindow;
	public static ConcurrentHashMap<String, ClientThread> signinThreads;// ��¼�ͻ�����
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
	}

	// ���ص�ǰ���ߵ��˵��߳�
	@Deprecated
	private static String getAllOnLineUserid() {
		// ʹ�õ��������
		Iterator<String> it = signinThreads.keySet().iterator();
		String res = "";
		while (it.hasNext()) {
			res += it.next().toString() + " ";
		}
		return res;
	}

	/**
	 * @Description:�������رռ���
	 * @auther: wutp 2016��10��15��
	 * @return void
	 */
	public void closeServer() {
		this.running=false;
	}

	/**
	 * @Description:�����������û����͹㲥
	 * @auther: wutp 2016��10��15��
	 * @param str
	 * @return void
	 */
	public void broadcast(String str) {
		MessageBean serverBean = new MessageBean();
		serverBean.setType(MessageType.SERVER_BROADCAST);
		serverBean.setInfo(str);		
		
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

	// ǿ������
	public void kickAway(String id) {
		
		ServerServer.signinThreads.get(id).closeSockt();
		ServerServer.signinThreads.get(id).running=false;
		ServerServer.signinThreads.remove(id);
		broadcast(id + " " + " ������������Աǿ��������");
		this.AppWindow.delList(id);
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
