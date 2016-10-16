package uc.dof;

import java.awt.*;

import javax.swing.*;

import uc.dal.sevice.TableModel;

import java.awt.event.*;

/**
 * @Description: 添加界面
 * @author wutp 2016年10月16日
 * @version 1.0
 */
public class AddSmJDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel p1,p2,p3;
	JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7,jl8,jl9,jl10,jl11,jl12,jl13;
	JTextField jtf1,jtf2,jtf3,jtf4,jtf5,jtf6,jtf7,jtf8,jtf9,jtf10,jtf11,jtf12,jtf13;
	JButton jb1,jb2;
	
	public AddSmJDialog(ServerJFrame sm,String title,boolean model)
	{
		//super(empInfo,title,model);
		p1=new JPanel(new GridLayout(14,1));
		jl1=new JLabel("uc");
		jl2=new JLabel("pwd");
		jl3=new JLabel("sign");
		jl4=new JLabel("photoid");
		jl5=new JLabel("nickname");
		jl6=new JLabel("sex");
		jl7=new JLabel("birthday");
		jl8=new JLabel("constellation");
		jl9=new JLabel("bloodtype");
		jl10=new JLabel("diploma");
		jl11=new JLabel("telephone");
		jl12=new JLabel("email");
		jl13=new JLabel("address");

		
		p1.add(jl1);
		p1.add(jl2);
		p1.add(jl3);
		p1.add(jl4);
		p1.add(jl5);
		p1.add(jl6);
		p1.add(jl7);
		p1.add(jl8);
		p1.add(jl9);
		p1.add(jl10);
		p1.add(jl11);
		p1.add(jl12);
		p1.add(jl13);
		
		p2=new JPanel(new GridLayout(14,1));
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		jtf5=new JTextField();
		jtf6=new JTextField();
		jtf7=new JTextField();
		jtf8=new JTextField();
		jtf9=new JTextField();
		jtf10=new JTextField();
		jtf11=new JTextField();
		jtf12=new JTextField();
		jtf13=new JTextField();
		
		p2.add(jtf1);
		p2.add(jtf2);
		p2.add(jtf3);
		p2.add(jtf4);
		p2.add(jtf5);
		p2.add(jtf6);
		p2.add(jtf7);
		p2.add(jtf8);
		p2.add(jtf9);
		p2.add(jtf10);
		p2.add(jtf11);
		p2.add(jtf12);
		p2.add(jtf13);
		
		p3=new JPanel(new FlowLayout(FlowLayout.CENTER));
		jb1=new JButton("添加");
	//	jb1.setFont(MyTools.f4);
		jb1.addActionListener(this);
		jb2=new JButton("取消");
	//	jb2.setFont(MyTools.f4);
		jb2.addActionListener(this);
		p3.add(jb1);
		p3.add(jb2);
		this.setLayout(new BorderLayout());
		this.add(p1,"West");
		this.add(p2,"Center");
		this.add(p3,"South");
		

		this.setSize(400,500);

		this.setTitle("添加用户");
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO 自动生成的方法存根
		if(arg0.getSource()==jb1){
			String sql="insert into USERINFO(uc,pwd,sign,photoid,nickname,sex,birthday,constellatton,constellation,bloodtype,"
					+ "telephone,email,address) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String []params={jtf1.getText(),jtf2.getText(),jtf3.getText(),jtf4.getText(),jtf5.getText(),jtf6.getText(),
					jtf7.getText(),jtf8.getText(),jtf9.getText(),jtf10.getText(),jtf11.getText(),jtf12.getText(),jtf13.getText()};
			TableModel tm=new TableModel();
			if(!tm.UpdateModel(sql, params))
			{
				JOptionPane.showMessageDialog(null, "添加失败，请输入正确数据类型！");
			}
			
			this.dispose();
		}
		else if(arg0.getSource()==jb2)
		{
			this.dispose();
		}
	}
	
	
	
	
}

