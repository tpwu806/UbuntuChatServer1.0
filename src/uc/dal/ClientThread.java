package uc.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uc.dal.db.TableModel;
import uc.dal.db.UserService;
import uc.dof.ServerJFrame;
import uc.pub.common.MessageBean;
import uc.pub.common.MessageType;

/**
 * @Description: ���ӿͻ��˵��߳�
 * @author wutp 2016��10��13��
 * @version 1.0
 */
public class ClientThread implements Runnable {

	public Socket clientsocket;
	private MessageBean bean;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ServerJFrame AppWindow;

	public ClientThread(Socket socket, ServerJFrame AppWindow) {
		this.clientsocket = socket;
		this.AppWindow = AppWindow;
	}

	volatile boolean running = true;

	@Override
	public void run() {
		try {
			MessageBean mbean;
			while (running) {				
				ois = new ObjectInputStream(clientsocket.getInputStream());								
				bean = (MessageBean) ois.readObject();

				// ����catbean�У�type������һ������
				switch (bean.getType()) {
				// ��¼
				case MessageType.CLIENT_SIGN_IN: {
					System.out.println(bean.getType() + ":" + bean.getName() + bean.getPwd());
					mbean = new MessageBean();
					UserService user = new UserService();
					String power = new String(user.checkUser(bean.getName().trim(), bean.getPwd()).trim());
					if (power.equals("root") || power.equals("user")) {
						System.out.println("sql��֤�ɹ�");
						mbean.setType(MessageType.SERVER_SIGN_IN_SUCCESS);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();

						ServerServer.signinThreads.put(bean.getName(), this);
						AppWindow.AddList(bean.getName());
						// ������������������
						/*MessageBean serverBean = new MessageBean();
						serverBean.setType(MessageType.SERVER_SIGN_IN_NOTICE);
						serverBean.setName(bean.getName());
						serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "������");

						sendOtherAll(serverBean);*/
						updateFriendsList(bean.getName());

					} else {
						bean = new MessageBean();
						bean.setType(MessageType.SERVER_SIGN_IN_FALSE);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();

						System.out.println("sql��֤ʧ��");
					}
					break;
				}
				// ע��
				case MessageType.CLIENT_SIGN_UP: {
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
						mbean.setType(MessageType.SERVER_SIGN_UP_SUCCESS);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();

						System.out.println("sqlע��ɹ�");
					} else {
						mbean.setType(MessageType.SERVER_SIGN_UP_FALSE);
						oos = new ObjectOutputStream(clientsocket.getOutputStream());
						oos.writeObject(mbean);
						oos.flush();
						System.out.println("sqlע��ɹ�");
					}
					break;
				}

				case MessageType.CLIENT_SIGN_OUT: { // ����															
					// ������������������
					MessageBean serverBean = new MessageBean();
					serverBean.setType(MessageType.SERVER_SIGN_IN_NOTICE);
					serverBean.setName(bean.getName());
					serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "������");
					sendOtherAll(serverBean);
					AppWindow.delList(bean.getName());
					
					ServerServer.signinThreads.remove(bean.getName());
					closeSockt();
					this.running=false;
					//return;
				}
				/*case MessageType.CLIENT_CHAR: { // ����

					// ������������������
					MessageBean serverBean = new MessageBean();
					serverBean.setType(MessageType.SERVER_SIGN_IN_NOTICE);
					serverBean.setName(bean.getName());
					serverBean.setInfo(bean.getTimer() + "  " + bean.getName() + "������");

					sendOtherAll(serverBean);
					updateFriendsList(bean.getName());
				}*/
				case MessageType.CLIENT_CHAR: { // ����
					System.out.println("JJJJJJJJJJJJJJJ");
					// ������������catbean�������͸��ͻ���
					MessageBean serverBean = new MessageBean();
					serverBean.setType(MessageType.CLIENT_CHAR);
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
					MessageBean serverBean = new MessageBean();
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
					MessageBean serverBean = new MessageBean();

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
					MessageBean serverBean = new MessageBean();

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

	/**
	 * @Description:���º����б�
	 * @auther: wutp 2016��10��15��
	 * @return void
	 */
	private void updateFriendsList(String name){
		//����ȡ�����е������û�
		HashSet<String> hs = new HashSet<>();
		
		hs.addAll(ServerServer.signinThreads.keySet());
		System.out.println("%%%%%%%%%%%%%%"+hs.size()+hs.toString());
		MessageBean serverBean = new MessageBean();
		serverBean.setType(MessageType.SERVER_UPDATE_FRIENDS);
		serverBean.setClients(hs);
		
		sendMessage(serverBean);
	}

	/**
	 * @Description:// ��ѡ�е��û���������
	 * @auther: wutp 2016��10��15��
	 * @param serverBean
	 * @return void
	 */
	private void sendMessage(MessageBean serverBean) {
		// ����ȡ�����е������û�
		Set<String> onlines = ServerServer.signinThreads.keySet();
		Iterator<String> it = onlines.iterator();
		// ѡ�пͻ�
		HashSet<String> clients = serverBean.getClients();

		while (it.hasNext()) {
			// ѡ�пͻ�
			String onlineClientName = it.next();
			// ѡ�еĿͻ����������ߵģ��ͷ���serverbean
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
	 * @Description:�����е��û�������Ϣ
	 * @auther: wutp 2016��10��14��
	 * @param serverBean
	 * @return void
	 */
	private void sendOtherAll(MessageBean serverBean) {
		//����ȡ�����е������û�
		Set<String> signins = ServerServer.signinThreads.keySet();
		signins.remove(serverBean.getName());
		Iterator<String> it = signins.iterator();

		while (it.hasNext()) {
			// ѡ�пͻ�
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
	
	/**
	 * @Description:�ر�Socket
	 * @auther: wutp 2016��10��14��
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
