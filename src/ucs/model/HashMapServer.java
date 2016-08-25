package ucs.model;

import java.util.HashMap;

import ucs.view.ServerJFrame;

public class HashMapServer {
private static HashMap hm=new HashMap<String, ServerJFrame>();
	
	//加入
	public static void addServerJFrame(String server,ServerJFrame sjf)
	{
		hm.put(server,sjf);
	}
	//加入
		public static void delServerJFrame(String server)
		{
			hm.remove(server);
		}
	//取出
	public static ServerJFrame getServerJFrame(String server)
	{
		return (ServerJFrame)hm.get(server);
	}
	
}

