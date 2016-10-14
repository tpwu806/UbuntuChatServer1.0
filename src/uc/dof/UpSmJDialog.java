
package uc.dof;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import uc.dal.db.TableModel;

import javax.swing.JDialog;

import java.awt.event.*;
//��ӽ���

public class UpSmJDialog extends JDialog implements ActionListener{

	JPanel p1,p2,p3;
	JLabel jl1,jl2,jl3;
	JTextField jtf1,jtf2,jtf3;
	JButton jb1,jb2;
	public UpSmJDialog(ServerJFrame sm,String title,boolean model,TableModel tm,int rowNum)
	{
		super();
		p1=new JPanel(new GridLayout(4,1));
		jl1=new JLabel("name");
		jl2=new JLabel("pwd");
		jl3=new JLabel("power");

		
		p1.add(jl1);
		p1.add(jl2);
		p1.add(jl3);
		
		
		p2=new JPanel(new GridLayout(4,1));
		jtf1=new JTextField();
		jtf1.setText(tm.getValueAt(rowNum, 0)+"");
		jtf1.setEditable(false);
		//System.out.println(em.getValueAt(rowNum, 0)+"");
		jtf2=new JTextField();
		jtf2.setText( tm.getValueAt(rowNum, 1)+"");
		jtf3=new JTextField();
		jtf3.setText( tm.getValueAt(rowNum, 2)+"");
		
		p2.add(jtf1);
		p2.add(jtf2);
		p2.add(jtf3);
	

		p3=new JPanel(new FlowLayout(FlowLayout.CENTER));
		jb1=new JButton("�޸�");
		//jb1.setFont(MyTools.f4);
		jb1.addActionListener(this);
		jb2=new JButton("ȡ��");
		//jb2.setFont(MyTools.f4);
		jb2.addActionListener(this);
		p3.add(jb1);
		p3.add(jb2);
		this.setLayout(new BorderLayout());
		this.add(p1,"West");
		this.add(p2,"Center");
		this.add(p3,"South");
		

		this.setSize(400,250);

		this.setTitle(title);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO �Զ����ɵķ������
		if(arg0.getSource()==jb1)
		{
			String sql="update User set name=?,pwd=? ,power=? where name=?";
			String []params={jtf1.getText(),jtf2.getText(),jtf3.getText(),jtf1.getText()};
			TableModel em=new TableModel();
			if(!em.UpdateModel(sql, params))
			{
				JOptionPane.showMessageDialog(null, "�޸�ʧ�ܣ���������ȷ��������!");
			}
			JOptionPane.showMessageDialog(null, "��ϲ���޸ĳɹ���");
			this.dispose();
		}
		else if(arg0.getSource()==jb2)
		{
			this.dispose();
		}
	}
	
	
	
	
}

