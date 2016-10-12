package ucs.model;

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













import uc.common.ChatBean;
import uc.common.ClientBean;
import ucs.db.TableModel;
import ucs.db.UserModel;
import ucs.view.ServerJFrame;

public class UbuntuChatServer {
	private static ServerSocket ss;
	public static HashMap<String, ClientBean> onlines;
	private Socket client;
	private ServerJFrame jf;
	private ClientThread clientThread;
	static {
		try {
			ss = new ServerSocket(8520);
			onlines = new HashMap<String, ClientBean>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UbuntuChatServer(ServerJFrame jf) {
		this.jf=jf;
	}

	public static HashMap<String, ClientBean> getOnlines() {
		return onlines;
	}

	public static void setOnlines(HashMap<String, ClientBean> onlines) {
		UbuntuChatServer.onlines = onlines;
	}


	

	public static void main(String[] args) {
		//new UbuntuChatServer().start();
		
	}
	
	public void start() {
		try {	
			while (true) {
			    client = ss.accept();
			    clientThread=new ClientThread(client,jf);
			    clientThread.start();
		    
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		clientThread.stop();
	}
	public void sendall(String str){
		ChatBean serverBean = new ChatBean();
		serverBean.setType(1);
		serverBean.setInfo(str);
		HashSet<String> set = new HashSet<String>();
		set.addAll(onlines.keySet());
		
		serverBean.setClients(set);
		clientThread.sendAll(serverBean);	
	}
	//ǿ������
	public void DownLine(String id){
		//onlines.remove(id);
		clientThread.DownLine(id);
	}

	
	class ClientThread extends Thread {
		private Socket client;
		private ChatBean bean;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private ServerJFrame jf;

		public ClientThread(Socket client,ServerJFrame jf) {
			this.client = client;
			this.jf=jf;
		}

		@Override
		public void run() {
			try {
				// ��ͣ�Ĵӿͻ��˽�����Ϣ
				while (true) {
					// ��ȡ�ӿͻ��˽��յ���catbean��Ϣ
					ois = new ObjectInputStream(client.getInputStream());
					bean = (ChatBean)ois.readObject();
					
					// ����catbean�У�type������һ������
					switch (bean.getType()) {
					//��¼
					case 11: {
						System.out.println(bean.getType()+":"+bean.getName()+bean.getPwd());
						UserModel user=new UserModel();
						String power=new String(user.checkUser(bean.getName().trim(), bean.getPwd()).trim());
						if(power.equals("root")){
							bean=new ChatBean();
							bean.setType(110);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();
						
							System.out.println("sql��֤�ɹ�");
						}else{
							bean=new ChatBean();
							bean.setType(111);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();
							
							
							System.out.println("sql��֤�ɹ�");
						}
						break;
					}
					//ע��
                    case 12: {
						
						System.out.println(bean.getType()+bean.getName()+bean.getPwd());
						String sql="insert into User values(?,?,?)";
						String name=bean.getName().trim();
						String pwd =bean.getPwd().trim();
						String power="user";
						String []params={name,pwd,power};
						System.out.printf(name,pwd,power);
						TableModel tm=new TableModel();
						//String power=new String(user.UpdateModel(bean.getName().trim(), bean.getPwd()).trim());
						if(!tm.UpdateModel(sql, params)){
							bean=new ChatBean();
							bean.setType(120);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();
						
							System.out.println("sqlע��ɹ�");
						}else{
							bean=new ChatBean();
							bean.setType(121);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();						
							System.out.println("sqlע��ɹ�");
						}
						break;
					}
					// �����߸���
					case 0: { // ����
						// ��¼���߿ͻ����û����Ͷ˿���clientbean��
						ClientBean cbean = new ClientBean();
						cbean.setName(bean.getName());
						cbean.setSocket(client);
						// ��������û�
						onlines.put(bean.getName(), cbean);
						// ������������catbean�������͸��ͻ���
						ChatBean serverBean = new ChatBean();
						serverBean.setType(0);
						serverBean.setInfo(bean.getTimer() + "  "
								+ bean.getName() + "������");
						// ֪ͨ���пͻ���������
						HashSet<String> set = new HashSet<String>();
						// �ͻ��ǳ�
						set.addAll(onlines.keySet());
						serverBean.setClients(set);
						sendAll(serverBean);
					    
						jf.AddList(bean.getName());
						
						//ServerJFrame sjf=HashMapServer.getServerJFrame("server");
						//sjf.AddList(bean.getName());
						break;
					}
					case -1: { // ����
						// ������������catbean�������͸��ͻ���
						ChatBean serverBean = new ChatBean();
						serverBean.setType(-1);

						try {
							oos = new ObjectOutputStream(
									client.getOutputStream());
							oos.writeObject(serverBean);
							oos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						onlines.remove(bean.getName());
						// ��ʣ�µ������û����������뿪��֪ͨ
						ChatBean serverBean2 = new ChatBean();
						serverBean2.setInfo(bean.getTimer() + "  "
								+ bean.getName() + " " + " ������");
						serverBean2.setType(0);
						HashSet<String> set = new HashSet<String>();
						set.addAll(onlines.keySet());
						serverBean2.setClients(set);
						ServerJFrame sjf=HashMapServer.getServerJFrame("server");
						sjf.DelList(bean.getName());
						sendAll(serverBean2);
						return;
					}
					case 1: { // ����
						
//						 ������������catbean�������͸��ͻ���
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
						String info = bean.getTimer() + "  " + bean.getName()
								+ "���㴫���ļ�,�Ƿ���Ҫ����";

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close();
			}
		}

		// ��ѡ�е��û���������
		private void sendMessage(ChatBean serverBean) {
			// ����ȡ�����е�values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// ѡ�пͻ�
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// ���߿ͻ�
				String client = it.next();
				// ѡ�еĿͻ����������ߵģ��ͷ���serverbean
				if (clients.contains(client)) {
					Socket c = onlines.get(client).getSocket();
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
		public void sendAll(ChatBean serverBean) {
			Collection<ClientBean> clients = onlines.values();
			Iterator<ClientBean> it = clients.iterator();
			ObjectOutputStream oos;
			while (it.hasNext()) {
				Socket c = it.next().getSocket();
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
		public void DownLine(String id){
	
			onlines.remove(id);
			System.out.println(id);
			// ��ʣ�µ������û����������뿪��֪ͨ
			ChatBean serverBean2 = new ChatBean();
			serverBean2.setInfo(id + " " + " ��ǿ��������");
			serverBean2.setType(0);
			HashSet<String> set = new HashSet<String>();
			set.addAll(onlines.keySet());
			serverBean2.setClients(set);
			ServerJFrame sjf=HashMapServer.getServerJFrame("server");
			sjf.DelList(id);
			sendAll(serverBean2);
			//return;
		}
		private void close() {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	}
	
}
