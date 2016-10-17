package uc.pub.common;

/**
 * @Description: MessageType
 * @author wutp 2016��10��14��
 * @version 1.0
 */
public class MessageType {

	/**
	 * ��¼
	 */
	public static final int SIGN_IN = 100;
	/**
	 * ��½�ɹ�
	 */
	public static final int SIGN_IN_SUCCESS = 101;
	/**
	 * ��½ʧ�� �������
	 */
	public static final int SIGN_IN_FALSE = 102;
	// *******************************************************************
	/**
	 * ����
	 */
	public static final int SIGN_OUT = 200;
	/**
	 * ��������
	 */
	public static final int SIGN_OUT_NOTICE = 201;

	// *******************************************************************
	/**
	 * ע��
	 */
	public static final int SIGN_UP = 301;
	/**
	 * ע��ɹ�
	 */
	public static final int SIGN_UP_SUCCESS = 302;
	/**
	 * ע��ʧ��
	 */
	public static final int SIGN_UP_FALSE = 303;
	// *******************************************************************
	/**
	 * ��������ļ�
	 */
	public static final int FILE_REQUESTION = 402;

	/**
	 * ȷ�������ļ�
	 */
	public static final int FILE_RECEIVE = 403;

	/**
	 * 
	 */
	public static final int FILE_RECEIVE_OK = 404;
	// *******************************************************************
	/**
	 * ����
	 */
	public static final int CLIENT_CHAR = 500;
	/**
	 * һ��һ����
	 */
	public static final int SINGLETON_CHAR = 510;
	/**
	 * �������㲥
	 */
	public static final int SERVER_BROADCAST = 520;
	/**
	 * Ⱥ����
	 */
	public static final int GROUP_CHAR = 530;
	// *******************************************************************
	/**
	 * �������ߺ���
	 */
	public static final int SERVER_UPDATE_FRIENDS = 600;
	/**
	 * ����Ⱥ
	 */
	public static final int UPDATE_GROUP = 610;
	/**
	 * ��������Ⱥ����
	 */
	public static final int UPDATE_GROUP_FRIENDS = 620;
	

}
