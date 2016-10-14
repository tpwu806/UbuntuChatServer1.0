package uc.dof;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uc.dal.UbuntuChatServer;
import uc.dal.db.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: ��̨����
 * @author wutp 2016��10��13��
 * @version 1.0
 */
public class ServerJFrame extends JFrame implements ActionListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ��������
	private JPanel jp1, jp2, jp3, jp4, jp5, jp6;
	private JTabbedPane jtp;
	private JScrollPane jsp, rightPanel, leftPanel;
	private JSplitPane centerSplit;

	private JButton jb1, jb2, jb3, jb4, jb5, jb6, btn_send;
	private JTable jtable;
	private JTextField jtf, jtf_port, jtf_max, jtf_message;
	private Timer t;
	private JLabel showTime, jl1, jl2, jl3;
	
	public JList<String> userList;
	public DefaultListModel<String> listModel;
	public JTextArea contentArea;

	private UbuntuChatServer ubuntuchatServer;
	private TableModel tm = null;

	public ServerJFrame() {
		init();
	}

	private void init() {
		// �ϱ�
		jp1 = new JPanel(new GridLayout(1, 10));
		// jp1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp1.setBorder(new TitledBorder("������Ϣ"));
		jl2 = new JLabel("��������:");
		jtf_max = new JTextField("30");
		jl3 = new JLabel("�˿�:");
		jtf_port = new JTextField("8520");
		jb1 = new JButton("����");
		jb1.addActionListener(this);
		jb2 = new JButton("�ر�");
		jb2.setEnabled(false);
		jb2.addActionListener(this);

		jp1.add(jl2);
		jp1.add(jtf_max);
		jp1.add(jl3);
		jp1.add(jtf_port);
		jp1.add(jb1);
		jp1.add(jb2);

		// �м�
		listModel = new DefaultListModel<String>();
		userList = new JList<String>(listModel);
		userList.addListSelectionListener(this);
		// listModel.addMouseListener(this);
		leftPanel = new JScrollPane(userList);
		leftPanel.setBorder(new TitledBorder("�����û�"));
		contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setLineWrap(true);
		contentArea.setForeground(Color.blue);
		jp4 = new JPanel(new BorderLayout());
		jp4.setBorder(new TitledBorder("д��Ϣ"));
		jtf_message = new JTextField();
		btn_send = new JButton("����");
		btn_send.addActionListener(this);
		jp4.add(jtf_message, "Center");
		jp4.add(btn_send, "East");
		jp6 = new JPanel(new BorderLayout());
		jp6.add(contentArea, "Center");
		jp6.add(jp4, "South");
		rightPanel = new JScrollPane(jp6);
		rightPanel.setBorder(new TitledBorder("ϵͳ��־"));
		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		centerSplit.setDividerLocation(100);

		// �ڶ���ѡ�
		jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jp2.setBorder(new TitledBorder("������"));
		jl1 = new JLabel("�������û�����");
		jtf = new JTextField(10);
		jb3 = new JButton("���");
		jb3.addActionListener(this);
		jb4 = new JButton("ɾ��");
		jb4.addActionListener(this);
		jb5 = new JButton("�޸�");
		jb5.addActionListener(this);
		jb6 = new JButton("��ѯ");
		jb6.addActionListener(this);
		jp2.add(jl1);
		jp2.add(jtf);
		jp2.add(jb6);
		jp2.add(jb4);
		jp2.add(jb5);
		jp2.add(jb3);

		String[] params = { "1" };
		String sql = "select * from User where 1=?";
		tm = new TableModel();
		tm.query(sql, params);
		jtable = new JTable(tm);
		jsp = new JScrollPane(jtable);
		jp3 = new JPanel(new BorderLayout());
		jp3.add(jp2, BorderLayout.NORTH);
		jp3.add(jsp, BorderLayout.CENTER);
		jtp = new JTabbedPane();
		jtp.add("ע���û�", jp3);
		jtp.add("�����û�", centerSplit);

		// �±�
		jp5 = new JPanel(new BorderLayout());
		t = new Timer(1000, this);
		showTime = new JLabel();
		jp5.add(showTime);

		jp5.setBackground(Color.GRAY);
		t.start();

		this.setLayout(new BorderLayout());
		this.add(jp1, "North");
		this.add(jtp, "Center");
		this.add(jp5, "South");
		this.setVisible(true);
		this.setSize(800, 500);
		// this.setIconImage(titleIcon);
		this.setTitle("UbuntuChatServer");
	}

	/**
	 * @Description:
	 * @auther: wutp 2016��10��13��
	 * @return void
	 */
	private void startServer(){
		ubuntuchatServer = new UbuntuChatServer(this);
		ubuntuchatServer.startServer();
	}

	// �رշ�����
	private void stopServer() {
		//sts.stop();
		ubuntuchatServer.closeServer();
	}

	//�㲥
	private void broadcast(String str) {
		this.ubuntuchatServer.broadcast(str);
	}

	/**
	 * @Description:ǿ������
	 * @auther: wutp 2016��10��13��
	 * @param id
	 * @return void
	 */
	private void kickAway(String id) {
		this.ubuntuchatServer.kickAway(id);
	}
	
	// ��������û�
	public void AddList(String res) {
		// onlines=(HashMap<String, ClientBean>)ubuntuchatServer.getOnlines();
		// Iterator it=onlines.keySet().iterator();
		// String res="";
		// while(it.hasNext())
		// {
		// res=it.next().toString()+" ";
		// System.out.println(res);

		// }
		// this.listModel.removeAllElements();
		this.listModel.addElement(res);
		this.userList.setModel(this.listModel);
		this.contentArea.append(res + "������" + "\r\n");

		// this.listModel.addListDataListener(res);
	}

	// ȥ�������û�
	public void delList(String res) {
		this.listModel.removeElement(res);
		this.userList.setModel(this.listModel);
		this.contentArea.append(res + "������" + "\r\n");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		//Calendar.getInstance().getTime().toLocaleString()//����
		this.showTime.setText("��ǰʱ�䣺" + time + "   ");
		// UPList();
		if (arg0.getSource() == jb1) { // ����������
			contentArea.append("���������������ѿ���" + "\r\n");
			System.out.println("����");

			//sts = new ServerThreadStart(this);
			//sts.start();
			//����������
			startServer();
			jb1.setEnabled(false);
			jtf_max.setEnabled(false);
			jtf_port.setEnabled(false);
			jb2.setEnabled(true);
		} else if (arg0.getSource() == jb2) { // �رշ�����

			contentArea.setText("���������������ѹر�" + "\r\n");
			System.out.println("�ر�");
			jb2.setEnabled(false);
			jb1.setEnabled(true);
			jtf_max.setEnabled(true);
			jtf_port.setEnabled(true);

			stopServer();
		} else if (arg0.getSource() == jb3) { // ���
			if (true) {
				new AddSmJDialog(this, "���", true);
			}
			String[] params = { "1" };
			String sql = "select * from User where 1=?";
			tm = new TableModel();
			tm.query(sql, params);
			jtable.setModel(tm);

		} else if (arg0.getSource().equals(jb4)) { // ɾ��
			int rowNum = this.jtable.getSelectedRow();

			if (rowNum == -1) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ�У�");
			} else {
				String name = (String) this.jtable.getValueAt(rowNum, 0);
				String sql = "delete from User where name=?";
				String[] params = { name };
				tm = new TableModel();
				;
				tm.UpdateModel(sql, params);
				JOptionPane.showMessageDialog(null, "��ϲ��ɾ���ɹ���");
			}
			String[] params = { "1" };
			String sql = "su\\elect * from User where 1=?";
			tm = new TableModel();
			tm.query(sql, params);
			jtable.setModel(tm);
		} else if (arg0.getSource() == jb5) { // �޸�
			int rowNum = this.jtable.getSelectedRow();
			if (rowNum == -1) {
				JOptionPane.showMessageDialog(this, "��ѡ��һ�У�");
			} else {
				new UpSmJDialog(this, "�޸�", true, tm, rowNum);
			}
			String[] params = { "1" };
			String sql = "select * from User where 1=?";
			tm = new TableModel();
			tm.query(sql, params);
			jtable.setModel(tm);
		} else if (arg0.getSource().equals(jb6)) { // ��ѯ
			if (jtf.getText().trim().equals("")) {
				String[] params = { "1" };
				String sql = "select * from User where 1=?";
				tm = new TableModel();
				tm.query(sql, params);
				jtable.setModel(tm);
			} else {
				String params[] = { jtf.getText().trim() };
				String sql = "select * from User where name=?";
				tm = new TableModel();
				tm.query(sql, params);
				jtable.setModel(tm);
			}
		} else if (arg0.getSource().equals(btn_send)) {

			String str = ("��������" + jtf_message.getText().trim());
			broadcast(str);
			contentArea.append(str + "\r\n");
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		// if(getValueIsAdjusting()=ture)
		String id = (String) ((JList<?>) e.getSource()).getSelectedValue();
		// String id=obj.toString();
		contentArea.append("��ѡ����" + id + "\r\n");
		System.out.println("��ѡ����" + id + "\r\n");
		kickAway(id);

	}

	public static void main(String[] args) {
		new ServerJFrame();
	}

}
