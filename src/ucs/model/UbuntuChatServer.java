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
	//强制下线
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
				// 不停的从客户端接收信息
				while (true) {
					// 读取从客户端接收到的catbean信息
					ois = new ObjectInputStream(client.getInputStream());
					bean = (ChatBean)ois.readObject();
					
					// 分析catbean中，type是那样一种类型
					switch (bean.getType()) {
					//登录
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
						
							System.out.println("sql验证成功");
						}else{
							bean=new ChatBean();
							bean.setType(111);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();
							
							
							System.out.println("sql验证成功");
						}
						break;
					}
					//注册
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
						
							System.out.println("sql注册成功");
						}else{
							bean=new ChatBean();
							bean.setType(121);
							oos = new ObjectOutputStream(
								client.getOutputStream());
							oos.writeObject(bean);
							oos.flush();						
							System.out.println("sql注册成功");
						}
						break;
					}
					// 上下线更新
					case 0: { // 上线
						// 记录上线客户的用户名和端口在clientbean中
						ClientBean cbean = new ClientBean();
						cbean.setName(bean.getName());
						cbean.setSocket(client);
						// 添加在线用户
						onlines.put(bean.getName(), cbean);
						// 创建服务器的catbean，并发送给客户端
						ChatBean serverBean = new ChatBean();
						serverBean.setType(0);
						serverBean.setInfo(bean.getTimer() + "  "
								+ bean.getName() + "上线了");
						// 通知所有客户有人上线
						HashSet<String> set = new HashSet<String>();
						// 客户昵称
						set.addAll(onlines.keySet());
						serverBean.setClients(set);
						sendAll(serverBean);
					    
						jf.AddList(bean.getName());
						
						//ServerJFrame sjf=HashMapServer.getServerJFrame("server");
						//sjf.AddList(bean.getName());
						break;
					}
					case -1: { // 下线
						// 创建服务器的catbean，并发送给客户端
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
						// 向剩下的在线用户发送有人离开的通知
						ChatBean serverBean2 = new ChatBean();
						serverBean2.setInfo(bean.getTimer() + "  "
								+ bean.getName() + " " + " 下线了");
						serverBean2.setType(0);
						HashSet<String> set = new HashSet<String>();
						set.addAll(onlines.keySet());
						serverBean2.setClients(set);
						ServerJFrame sjf=HashMapServer.getServerJFrame("server");
						sjf.DelList(bean.getName());
						sendAll(serverBean2);
						return;
					}
					case 1: { // 聊天
						
//						 创建服务器的catbean，并发送给客户端
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
						String info = bean.getTimer() + "  " + bean.getName()
								+ "向你传送文件,是否需要接受";

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				close();
			}
		}

		// 向选中的用户发送数据
		private void sendMessage(ChatBean serverBean) {
			// 首先取得所有的values
			Set<String> cbs = onlines.keySet();
			Iterator<String> it = cbs.iterator();
			// 选中客户
			HashSet<String> clients = serverBean.getClients();
			while (it.hasNext()) {
				// 在线客户
				String client = it.next();
				// 选中的客户中若是在线的，就发送serverbean
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

		// 向所有的用户发送数据
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
			// 向剩下的在线用户发送有人离开的通知
			ChatBean serverBean2 = new ChatBean();
			serverBean2.setInfo(id + " " + " 被强制下线了");
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
