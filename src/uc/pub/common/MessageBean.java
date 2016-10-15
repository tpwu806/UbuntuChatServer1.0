package uc.pub.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class MessageBean implements Serializable {

	private String name;//��¼��
	private String pwd;//����
	private int type; // 1˽�� 0�����߸��� -1�������� 2�������ļ� 3.ȷ�������ļ�
	
	private HashSet<String> clients; // ���ѡ�еĿͻ�
	private HashSet<String> to;	
	
	private String info;//����
	private String timer;//ʱ��	
	private String fileName;//�ļ�����
	private String ip;//ip��ַ
	private int size;//��С
	private int port;//�˿ں�	
		

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashSet<String> getTo() {
		return to;
	}

	public void setTo(HashSet<String> to) {
		this.to = to;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public HashSet<String> getClients() {
		return clients;
	}

	public void setClients(HashSet<String> clients) {
		this.clients = clients;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
