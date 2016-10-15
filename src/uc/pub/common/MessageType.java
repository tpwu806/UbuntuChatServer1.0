
package uc.pub.common;

/**
 * @Description: MessageType
 * @author wutp 2016��10��14��
 * @version 1.0
 */
public class MessageType {

	/**************************************************************
	 *��������
	 *�ͻ������������Ϣ 1
	 *��½00       
	 *����01
	 *����02
	 *��������ļ�03
	 *ȷ����������ļ�04
	 *ע��05
	 *
	 *���������ؽ��2 
	 *��½�ɹ�100
	 *��½ʧ��101
	 */
	
	/**
	 *  ��¼
	 */
	public static final int CLIENT_SIGN_IN = 100;
	
	/**
	 *  ����
	 */
	public static final int CLIENT_SIGN_OUT = 110;
	
	/**
	 * ����
	 */
	public static final int CLIENT_CHAR = 120;
	
	/**
	 * ��������ļ�
	 */
	public static final int CLIENT_REQUESTION_RECEIVE = 130;
	
	/**
	 *  ȷ�������ļ�
	 */
	public static final int CLIENT_AGREE_RECEIVE = 140;
	
	/**
	 * 
	 */
	public static final int CLIENT_AGREE_RECEIVE_OK = 4;
	
	/**
	 *  ע��
	 */
	public static final int CLIENT_SIGN_UP = 150;
	
	/**
	 * ��½�ɹ�
	 */
	public static final int SERVER_SIGN_IN_SUCCESS = 200;
	/**
	 * ��½ʧ�� �������
	 */
	public static final int SERVER_SIGN_IN_FALSE = 201;
	
	/**
	 * ��½ʧ�� �Ѿ���½
	 */
	public static final int SERVER_SIGN_IN_FALSE2 = 202;	
	/**
	 * ���˵�½֪ͨ
	 */
	public static final int SERVER_SIGN_IN_NOTICE = 203;
	/**
	 * ������֪ͨ
	 */
	public static final int SERVER_NOTICE = 204;
	/**
	 *  �㱻ǿ������
	 */
	public static final int SERVER_SIGN_OUT = 210;
	/**
	 *  ��������
	 */
	public static final int SERVER_SIGN_OUT_NOTICE = 211;
	/**
	 * ע��ɹ�
	 */
	public static final int SERVER_SIGN_UP_SUCCESS = 250;
	/**
	 * ע��ʧ�� �Ѿ�ע��
	 */
	public static final int SERVER_SIGN_UP_FALSE = 251;
	/**
	 * ע��ʧ�� �Ѿ�ע��
	 */
	public static final int SERVER_UPDATE_FRIENDS = 251;


}
