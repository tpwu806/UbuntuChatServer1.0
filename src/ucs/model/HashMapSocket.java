package ucs.model;

import java.net.Socket;
import java.util.HashMap;

public class HashMapSocket {
private static HashMap hms=new HashMap<String, Socket>();
	
	//加入
	public static void addSocket(String name,Socket s)
	{
		hms.put(name,s);
		
	}
	//除去
	public static void delSocket(String name)
	{	
		hms.remove(name);
	}
	//取出
	public static Socket getSocket(String name)
	{
		return (Socket)hms.get(name);
	}
	
}
