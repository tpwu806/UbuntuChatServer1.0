package ucs.model;

import java.util.HashMap;

import ucs.view.ServerJFrame;

public class HashMapServer {
private static HashMap hm=new HashMap<String, ServerJFrame>();
	
	//����
	public static void addServerJFrame(String server,ServerJFrame sjf)
	{
		hm.put(server,sjf);
	}
	//����
		public static void delServerJFrame(String server)
		{
			hm.remove(server);
		}
	//ȡ��
	public static ServerJFrame getServerJFrame(String server)
	{
		return (ServerJFrame)hm.get(server);
	}
	
}

