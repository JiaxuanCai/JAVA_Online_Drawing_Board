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
	public static boolean IsConnect = false; //是否连接
	public static boolean TypeFlag;  //确定端口的类型是服务器还是客户端
	public static boolean Dash = false;  //是否修改
	public static boolean Full = false;  //是否填充颜色
	public static String AllData = "";  //所有信息
	public static String ServerIP;  //服务器IP
	public static float PenType = 1.0f;  //画笔粗细
	public static int DrawType = 0;  //画图类型
	public static Color color = null;  //颜色
	public static InetAddress myip = null;	//本机IP
	private JTextField MyIP; // 本机IP
	public static JTextField GoalIP; //目标服务器IP
	public static JTextField State;  //连接状态
	
	public OpenGLApp() throws UnknownHostException
	{
//__________________________________________________________________________________________________//
		//使用Java swing 进行界面布局 尽量使得界面简洁美观实用
		
		this.setTitle("基于Java Swing和套接字网络绘图板的远程课堂教学白板_Cai Jiaxuan");
		color = new Color(0,0,0);//
		place.setFocusable(true);
		place.requestFocus();
		place.getViewport().setBackground(Color.WHITE);
		
		JToggleButton shape1=new JToggleButton(new ImageIcon("src/icons/cur.png"));
		JToggleButton shape2=new JToggleButton(new ImageIcon("src/icons/line.png"));
		JToggleButton shape3=new JToggleButton(new ImageIcon("src/icons/rec.png"));
		JToggleButton shape4=new JToggleButton(new ImageIcon("src/icons/oval.png")); //选择图形类型的按钮们
		JToggleButton shape5=new JToggleButton(new ImageIcon("src/icons/tri.png"));
		JToggleButton shape6=new JToggleButton(new ImageIcon("src/icons/sel.png"));

		JToggleButton[] bs= {shape1,shape2,shape3,shape4,shape5,shape6};
		
		ButtonGroup btng= new ButtonGroup(); //使用按钮组管理如上的按钮
		btng.add(shape1);
		btng.add(shape2);
		btng.add(shape3);
		btng.add(shape4);
		btng.add(shape5);
		btng.add(shape6);

		JToggleButton modify=new JToggleButton(new ImageIcon("src/icons/modi.png"));
		JToggleButton fill=new JToggleButton(new ImageIcon("src/icons/fill.png"));
		JButton delete=new JButton(new ImageIcon("src/icons/del.png")); //图标们 声明：图标来源阿里巴巴开源的iconfront图标库
		JButton colorChoose=new JButton(new ImageIcon("src/icons/color.png"));
		
		JSpinner spinner = new JSpinner();		 //设置画笔粗细的框框
		spinner.setModel(new SpinnerNumberModel(new Float(1), new Float(1), new Float(99), new Float(1))); //形式的设置
		
		MyIP = new JTextField();
		MyIP.setColumns(10);
		MyIP.setEditable(false); //IP地址们的框框
		myip = InetAddress.getLocalHost();
		MyIP.setText(myip.getHostName() + ":" + myip.getHostAddress());
		
		GoalIP = new JTextField("请输入服务器地址");
		GoalIP.setColumns(14);
		GoalIP.setText("");
		
		State = new JTextField();
		State.setText("未连接");
		State.setColumns(10);
		State.setEditable(false);
		
		JButton JoinIn = new JButton("加入连接"); //加入链接的按钮
		JButton BuildConnect = new JButton("建立连接"); //建立连接的俺就		
		JLabel lblip = new JLabel("本机IP");
		JLabel pii=new JLabel("请输入服务器IP地址");

		JPanel p1=new JPanel();
		JPanel zhanwei=new JPanel();
		zhanwei.setPreferredSize(new Dimension(0,100));
		JPanel p2=new JPanel();
		p2.setPreferredSize(new Dimension(170,8000));
		p2.add(lblip);
		p2.add(MyIP);
		p2.add(zhanwei); // 布局 右边的框框
		p2.add(pii);
		p2.add(GoalIP);
		p2.add(JoinIn);
		p2.add(BuildConnect);
		p2.add(State);
		
		JLabel cuxi = new JLabel("线条粗细");

		p1.setLayout(new FlowLayout());
		p1.add(cuxi);
		p1.add(spinner);
		p1.add(shape1);
		p1.add(shape2);
		p1.add(shape3);
		p1.add(shape4);
		p1.add(shape5); //布局 下面的框框
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
		
		ActionListener listener = new ActionListener() //下面一排前六个按钮的监听器 用来监听选择的图形类型
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for(int i=0;i<6;i++)
				{
					if(bs[i].isSelected()) //判断哪个按钮被选中了
						DrawType=i;
				}
			}			
		};
		for(int i=0;i<6;i++)
		{
			bs[i].addActionListener(listener); //对于前6个图标，全部都要加这个监听器，否则图标没用
		}
		
		ActionListener colorlistener = new ActionListener() //选择颜色的监听器
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFrame f=new JFrame("选择颜色"); //歆的窗口 用于弹出颜色选择器
				f.setVisible(false); //不然会多弹出一个没用的空窗口
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        f.setBounds(100,100,400,200);
				JColorChooser cc = new JColorChooser(); //颜色选择器
				color = cc.showDialog(f, "选取颜色", Color.black);
			}			
		};
		colorChoose.addActionListener(colorlistener);//按钮得加上这个监听器

		spinner.addChangeListener(new ChangeListener()//改变画笔粗细的监听器
		{
			public void stateChanged(ChangeEvent e)
			{
				JSpinner spin = (JSpinner)e.getSource();
				PenType = (float) spin.getValue();
			}
		});

		modify.addItemListener(new ItemListener() //修改图形的监听器
		{
			public void itemStateChanged(ItemEvent e)
			{
				JToggleButton bu = (JToggleButton)e.getSource(); //得到是否选择
				Dash = bu.isSelected();
			}
		});

		fill.addItemListener(new ItemListener() //是否填充的监听器
		{
			public void itemStateChanged(ItemEvent e)
			{
				JToggleButton bu = (JToggleButton)e.getSource();
				Full = bu.isSelected();
			}
		});

		BuildConnect.addActionListener(new ActionListener() //建立连接按钮的监听器
		{
			public void actionPerformed(final ActionEvent e)
			{
				Thread ServerStart = new Thread() //新建线程
				{
					public void run()
					{
						JButton button = (JButton)e.getSource();
						try
						{
							Server.StartServer(); //启动服务器
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
		
		delete.addActionListener(new ActionListener() //删除已选中图形的监听器
		{
			public void actionPerformed(final ActionEvent e)
			{
				place.delete(); //直接调用delete方法就好，很方便
			}
		});
		

		JoinIn.addActionListener(new ActionListener() //加入链接的监听器
		{
			public void actionPerformed(final ActionEvent e)
			{
				Thread ClientStart = new Thread() //同样新建线程
				{
					public void run()
					{
						JButton button = (JButton)e.getSource();
						ServerIP = GoalIP.getText(); //得到服务器地址
						Client.StartClient();//启动服务器
					}					
				};
				ClientStart.start();//启动客户端
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
