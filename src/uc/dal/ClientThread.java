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
 * @Description: ���ӿͻ��˵��߳�
 * @author wutp 2016��10��13��
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

				// ����catbean�У�type������һ������
				switch (bean.getType()) {
				// ��¼
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

						System.out.println("sql��֤�ɹ�");
					} else {
						bean = new ChatBean();
						bean.setType(111);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();

						System.out.println("sql��֤�ɹ�");
					}
					break;
				}
				// ע��
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

						System.out.println("sqlע��ɹ�");
					} else {
						bean = new ChatBean();
						bean.setType(121);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(bean);
						oos.flush();
						System.out.println("sqlע��ɹ�");
					}
					break;
				}

				/**
				 * ���߰��߳�
				 */
				case 0: { // ���� //��¼���߿ͻ����û����Ͷ˿���clientbean��
					//ClientBean cbean = new ClientBean();
					//cbean.setName(bean.getName());
					//cbean.setSocket(clientsocket);
					// ��������û� 
					UbuntuChatServer.onlineThreads.put(bean.getName(), this); //
					// ������������catbean�������͸��ͻ���
					ChatBean serverBean = new ChatBean();
					serverBean.setType(0);
					serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "������"); // ֪ͨ���пͻ���������
																							// HashSet<String>
					HashSet<String> set = new HashSet<String>(); // �ͻ��ǳ�
					set.addAll(UbuntuChatServer.onlineThreads.keySet());
					serverBean.setClients(set);
					sendAll(serverBean);
					AppWindow.AddList(bean.getName());
					break;
				}

				case -1: { // ����
					// ������������catbean�������͸��ͻ���
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
					// ��ʣ�µ������û����������뿪��֪ͨ
					ChatBean serverBean2 = new ChatBean();
					serverBean2.setInfo(bean.getTimer() + "  " + bean.getName() + " " + " ������");
					serverBean2.setType(0);
					HashSet<String> set = new HashSet<String>();
					set.addAll(UbuntuChatServer.onlineThreads.keySet());
					serverBean2.setClients(set);

					AppWindow.delList(bean.getName());
					sendAll(serverBean2);
					return;
				}
				case 1: { // ����

					// ������������catbean�������͸��ͻ���
					ChatBean serverBean = new ChatBean();
					serverBean.setType(1);
					serverBean.setClients(bean.getClients());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());
					serverBean.setTimer(bean.getTimer());
					// ��ѡ�еĿͻ���������
					sendMessage(serverBean);
					break;
				}
				case 2: { // ��������ļ�
					// ������������catbean�������͸��ͻ���
					ChatBean serverBean = new ChatBean();
					String info = bean.getTimer() + "  " + bean.getName() + "���㴫���ļ�,�Ƿ���Ҫ����";

					serverBean.setType(2);
					serverBean.setClients(bean.getClients()); // ���Ƿ��͵�Ŀ�ĵ�
					serverBean.setFileName(bean.getFileName()); // �ļ�����
					serverBean.setSize(bean.getSize()); // �ļ���С
					serverBean.setInfo(info);
					serverBean.setName(bean.getName()); // ��Դ
					serverBean.setTimer(bean.getTimer());
					// ��ѡ�еĿͻ���������
					sendMessage(serverBean);

					break;
				}
				case 3: { // ȷ�������ļ�
					ChatBean serverBean = new ChatBean();

					serverBean.setType(3);
					serverBean.setClients(bean.getClients()); // �ļ���Դ
					serverBean.setTo(bean.getTo()); // �ļ�Ŀ�ĵ�
					serverBean.setFileName(bean.getFileName()); // �ļ�����
					serverBean.setIp(bean.getIp());
					serverBean.setPort(bean.getPort());
					serverBean.setName(bean.getName()); // ���յĿͻ�����
					serverBean.setTimer(bean.getTimer());
					// ֪ͨ�ļ���Դ�Ŀͻ����Է�ȷ�������ļ�
					sendMessage(serverBean);
					break;
				}
				case 4: {
					ChatBean serverBean = new ChatBean();

					serverBean.setType(4);
					serverBean.setClients(bean.getClients()); // �ļ���Դ\
					serverBean.setTo(bean.getTo()); // �ļ�Ŀ�ĵ�
					serverBean.setFileName(bean.getFileName());
					serverBean.setInfo(bean.getInfo());
					serverBean.setName(bean.getName());// ���յĿͻ�����
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

	// ��ѡ�е��û���������
	private void sendMessage(ChatBean serverBean) {
		// ����ȡ�����е�values
		Set<String> cbs = UbuntuChatServer.onlineThreads.keySet();
		Iterator<String> it = cbs.iterator();
		// ѡ�пͻ�
		HashSet<String> clients = serverBean.getClients();
		while (it.hasNext()) {
			// ���߿ͻ�
			String clientname = it.next();
			// ѡ�еĿͻ����������ߵģ��ͷ���serverbean
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

	// �����е��û���������
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
		// ��ʣ�µ������û����������뿪��֪ͨ
		ChatBean serverBean2 = new ChatBean();
		serverBean2.setInfo(id + " " + " ��ǿ��������");
		serverBean2.setType(0);
		HashSet<String> set = new HashSet<String>();
		set.addAll(UbuntuChatServer.onlineThreads.keySet());
		serverBean2.setClients(set);
		// ServerJFrame sjf=HashMapServer.getServerJFrame("server");
		AppWindow.delList(id);
		sendAll(serverBean2);
		// return;
	}

	// �ر�Socket
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
