package ucs.model;

import java.net.Socket;
import java.util.HashMap;

public class HashMapSocket {
private static HashMap hms=new HashMap<String, Socket>();
	
	//����
	public static void addSocket(String name,Socket s)
	{
		hms.put(name,s);
		
	}
	//��ȥ
	public static void delSocket(String name)
	{	
		hms.remove(name);
	}
	//ȡ��
	public static Socket getSocket(String name)
	{
		return (Socket)hms.get(name);
	}
	
}
