package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeListener;

import communication.Client;
import communication.Server;
import communication.SocketThread;
import graph.Graphic;

import javax.swing.event.ChangeEvent;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OpenGLApp extends JFrame
{
	public static Graphic place = new Graphic();
	public static boolean IsConnect = false; //�Ƿ�����
	public static boolean TypeFlag;  //ȷ���˿ڵ������Ƿ��������ǿͻ���
	public static boolean Dash = false;  //�Ƿ��޸�
	public static boolean Full = false;  //�Ƿ������ɫ
	public static String AllData = "";  //������Ϣ
	public static String ServerIP;  //������IP
	public static float PenType = 1.0f;  //���ʴ�ϸ
	public static int DrawType = 0;  //��ͼ����
	public static Color color = null;  //��ɫ
	public static InetAddress myip = null;	//����IP
	private JTextField MyIP; // ����IP
	public static JTextField GoalIP; //Ŀ�������IP
	public static JTextField State;  //����״̬
	
	public OpenGLApp() throws UnknownHostException
	{
//__________________________________________________________________________________________________//
		//ʹ��Java swing ���н��沼�� ����ʹ�ý���������ʵ��
		
		this.setTitle("����Java Swing���׽��������ͼ���Զ�̿��ý�ѧ�װ�_Cai Jiaxuan");
		color = new Color(0,0,0);//
		place.setFocusable(true);
		place.requestFocus();
		place.getViewport().setBackground(Color.WHITE);
		
		JToggleButton shape1=new JToggleButton(new ImageIcon("src/icons/cur.png"));
		JToggleButton shape2=new JToggleButton(new ImageIcon("src/icons/line.png"));
		JToggleButton shape3=new JToggleButton(new ImageIcon("src/icons/rec.png"));
		JToggleButton shape4=new JToggleButton(new ImageIcon("src/icons/oval.png")); //ѡ��ͼ�����͵İ�ť��
		JToggleButton shape5=new JToggleButton(new ImageIcon("src/icons/tri.png"));
		JToggleButton shape6=new JToggleButton(new ImageIcon("src/icons/sel.png"));

		JToggleButton[] bs= {shape1,shape2,shape3,shape4,shape5,shape6};
		
		ButtonGroup btng= new ButtonGroup(); //ʹ�ð�ť��������ϵİ�ť
		btng.add(shape1);
		btng.add(shape2);
		btng.add(shape3);
		btng.add(shape4);
		btng.add(shape5);
		btng.add(shape6);

		JToggleButton modify=new JToggleButton(new ImageIcon("src/icons/modi.png"));
		JToggleButton fill=new JToggleButton(new ImageIcon("src/icons/fill.png"));
		JButton delete=new JButton(new ImageIcon("src/icons/del.png")); //ͼ���� ������ͼ����Դ����ͰͿ�Դ��iconfrontͼ���
		JButton colorChoose=new JButton(new ImageIcon("src/icons/color.png"));
		
		JSpinner spinner = new JSpinner();		 //���û��ʴ�ϸ�Ŀ��
		spinner.setModel(new SpinnerNumberModel(new Float(1), new Float(1), new Float(99), new Float(1))); //��ʽ������
		
		MyIP = new JTextField();
		MyIP.setColumns(10);
		MyIP.setEditable(false); //IP��ַ�ǵĿ��
		myip = InetAddress.getLocalHost();
		MyIP.setText(myip.getHostName() + ":" + myip.getHostAddress());
		
		GoalIP = new JTextField("�������������ַ");
		GoalIP.setColumns(14);
		GoalIP.setText("");
		
		State = new JTextField();
		State.setText("δ����");
		State.setColumns(10);
		State.setEditable(false);
		
		JButton JoinIn = new JButton("��������"); //�������ӵİ�ť
		JButton BuildConnect = new JButton("��������"); //�������ӵİ���		
		JLabel lblip = new JLabel("����IP");
		JLabel pii=new JLabel("�����������IP��ַ");

		JPanel p1=new JPanel();
		JPanel zhanwei=new JPanel();
		zhanwei.setPreferredSize(new Dimension(0,100));
		JPanel p2=new JPanel();
		p2.setPreferredSize(new Dimension(170,8000));
		p2.add(lblip);
		p2.add(MyIP);
		p2.add(zhanwei); // ���� �ұߵĿ��
		p2.add(pii);
		p2.add(GoalIP);
		p2.add(JoinIn);
		p2.add(BuildConnect);
		p2.add(State);
		
		JLabel cuxi = new JLabel("������ϸ");

		p1.setLayout(new FlowLayout());
		p1.add(cuxi);
		p1.add(spinner);
		p1.add(shape1);
		p1.add(shape2);
		p1.add(shape3);
		p1.add(shape4);
		p1.add(shape5); //���� ����Ŀ��
		p1.add(shape6);
		p1.add(modify);
		p1.add(fill);
		p1.add(delete);
		p1.add(colorChoose);
		p1.setPreferredSize(new Dimension(0, 50));
		this.add(p1,BorderLayout.SOUTH);
		this.add(place,BorderLayout.CENTER);
		this.add(p2,BorderLayout.EAST);
		
//_______________________________________________________________________________//
		
		ActionListener listener = new ActionListener() //����һ��ǰ������ť�ļ����� ��������ѡ���ͼ������
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i=0;i<6;i++)
				{
					if(bs[i].isSelected()) //�ж��ĸ���ť��ѡ����
						DrawType=i;
				}
			}			
		};
		for(int i=0;i<6;i++)
		{
			bs[i].addActionListener(listener); //����ǰ6��ͼ�꣬ȫ����Ҫ�����������������ͼ��û��
		}
		
		ActionListener colorlistener = new ActionListener() //ѡ����ɫ�ļ�����
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFrame f=new JFrame("ѡ����ɫ"); //짵Ĵ��� ���ڵ�����ɫѡ����
				f.setVisible(false); //��Ȼ��൯��һ��û�õĿմ���
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        f.setBounds(100,100,400,200);
				JColorChooser cc = new JColorChooser(); //��ɫѡ����
				color = cc.showDialog(f, "ѡȡ��ɫ", Color.black);
			}			
		};
		colorChoose.addActionListener(colorlistener);//��ť�ü������������

		spinner.addChangeListener(new ChangeListener()//�ı仭�ʴ�ϸ�ļ�����
		{
			public void stateChanged(ChangeEvent e)
			{
				JSpinner spin = (JSpinner)e.getSource();
				PenType = (float) spin.getValue();
			}
		});

		modify.addItemListener(new ItemListener() //�޸�ͼ�εļ�����
		{
			public void itemStateChanged(ItemEvent e)
			{
				JToggleButton bu = (JToggleButton)e.getSource(); //�õ��Ƿ�ѡ��
				Dash = bu.isSelected();
			}
		});

		fill.addItemListener(new ItemListener() //�Ƿ����ļ�����
		{
			public void itemStateChanged(ItemEvent e)
			{
				JToggleButton bu = (JToggleButton)e.getSource();
				Full = bu.isSelected();
			}
		});

		BuildConnect.addActionListener(new ActionListener() //�������Ӱ�ť�ļ�����
		{
			public void actionPerformed(final ActionEvent e)
			{
				Thread ServerStart = new Thread() //�½��߳�
				{
					public void run()
					{
						JButton button = (JButton)e.getSource();
						try
						{
							Server.StartServer(); //����������
						}
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}				
				};
				ServerStart.start();	
			}
		});
		
		delete.addActionListener(new ActionListener() //ɾ����ѡ��ͼ�εļ�����
		{
			public void actionPerformed(final ActionEvent e)
			{
				place.delete(); //ֱ�ӵ���delete�����ͺã��ܷ���
			}
		});
		

		JoinIn.addActionListener(new ActionListener() //�������ӵļ�����
		{
			public void actionPerformed(final ActionEvent e)
			{
				Thread ClientStart = new Thread() //ͬ���½��߳�
				{
					public void run()
					{
						JButton button = (JButton)e.getSource();
						ServerIP = GoalIP.getText(); //�õ���������ַ
						Client.StartClient();//����������
					}					
				};
				ClientStart.start();//�����ͻ���
			}
		});
	}

	public static void main(String[] args) throws UnknownHostException
	{
		OpenGLApp frame = new OpenGLApp();
		frame.setSize(900, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}