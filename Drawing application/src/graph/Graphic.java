package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JScrollPane;

import app.OpenGLApp;
import communication.Client;
import communication.SocketThread;

/*
 * Graphic类
 * 
 * 完成包括增、删、改、画的图形画画的操作
 * 
 */


public class Graphic extends JScrollPane implements MouseListener , MouseMotionListener
{
	public List<Shape> shapes; //用于存放图形对象的List
	public MyRectangle chooseField; //用于存放选择框
	public static boolean isResive = false; //接收控制
	public boolean flag; //标志
	private BasicStroke stroke = null;  //画笔样式
	public static BufferedImage image = null; //存放图形的文件
	private int oldMouseX=-100,oldMouseY=-100,nowMouseX=-100,nowMouseY=-100,X,Y; //四个坐标，用来获得鼠标的绘画区域
	public Graphic()
	{
		this.addMouseListener(this); //监听鼠标
		this.addMouseMotionListener(this); //监听鼠标移动
		stroke = new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f); //画笔粗细等值的默认值
		image = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB); //初始化image
		image.getGraphics().fillRect(0, 0, 2000, 2000); //清空白板
		shapes = new LinkedList<Shape>(); //初始化shapes List
		chooseField = new MyRectangle(false,0,0,0,0); //初始化选择框
	}
	
	public void paint(Graphics g) //重载paint方法，画出目前正在画的中间轨迹，以及最终得到的画画结果，实时更新。
	{
		super.paint(g); //调用父类的paint方法
		Graphics2D g2d = (Graphics2D) g; //画布
		if(isResive) //没有收到接收控制
		{
			g2d.drawImage(image, 0, 0, this);
			isResive = false;
		}
		else
		{
			Graphics2D bg = image.createGraphics(); //存放最终结果的画布
			SetStroke(); //实时更新画笔的格式（粗细 之类的）
			bg.setStroke(stroke); 
			bg.setColor(OpenGLApp.color); //实时更新画笔颜色
			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//网上提供的抗锯齿方法
			g2d.drawImage(image, 0, 0, this);
			
			if(OpenGLApp.DrawType!=0)//如果不是签名模式（自由画笔），每次画画之前先清空画板
			{
				bg.setColor(new Color(255,255,255));
				bg.fillRect(0, 0, 2000, 2000);
				bg.setColor(OpenGLApp.color);
			}
			
			if(OpenGLApp.DrawType==0)//自由画笔 最终
			{
				bg.drawLine(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
			}
			
			switch(OpenGLApp.DrawType)//任何图形 中间过程图像
			{
			case 0://自由画图
				g2d.drawLine(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
				break;
			case 1://直线
				g2d.drawLine(oldMouseX,oldMouseY,X,Y);
				break;
			case 2://矩形
				if(!OpenGLApp.Full)
					g2d.drawRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				else
					g2d.fillRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				break;
			case 3://椭圆
				if(!OpenGLApp.Full)
					g2d.drawOval(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				else
					g2d.fillOval(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				break;
			case 4://三角形
				//注释掉的是选择 
				{
//					if(!OpenGLApp.Dash)
//						g2d.drawRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				int[] x= {Math.min(oldMouseX,X),Math.min(oldMouseX,X)+Math.abs(oldMouseX-X),Math.min(oldMouseX,X)+Math.abs(oldMouseX-X)};
				int[] y= {Math.min(oldMouseY,Y),Math.min(oldMouseY,Y)+Math.abs(oldMouseY-Y),Math.min(oldMouseY,Y)-Math.abs(oldMouseY-Y)};
				if(!OpenGLApp.Full)
					g2d.drawPolygon(x,y,3);
				else
					g2d.fillPolygon(x,y,3);			
				}
				break;
			case 5://选择框
				{
					change(g2d);
					if(!OpenGLApp.Dash)
						g2d.drawRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				}
			}
			
			for(int i=0;i<shapes.size();i++) //列表内的元素 挨个画在画板上
			{
				if(OpenGLApp.DrawType!=5)
					shapes.get(i).draw(bg);//如果不是选择模式 不画选择框
				else 
				{
//						System.out.println(shapes.get(i).isSelected);
					if(shapes.get(i).isSelected==false)
						shapes.get(i).draw(bg);
					else if(shapes.get(i).isSelected==true)
					{
						bg.setColor(new Color(176,196,222)); //被选中的图像，变为灰色，以进行表示
						shapes.get(i).draw(bg);
						bg.setColor(OpenGLApp.color);
					}
				}
			}
			
			if(OpenGLApp.DrawType==5)//如果是选择模式，把最终的选择框画在画板上
			{
				BasicStroke tstroke = new BasicStroke(0.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f,new float[]{5f,5f},10f);
				bg.setStroke(tstroke);
				bg.setColor(new Color(105,105,105));//画个灰色的虚线框 好看一点
				if(!OpenGLApp.Dash)
					chooseField.draw(bg);
				bg.setColor(OpenGLApp.color);
				bg.setStroke(stroke);
			}
			
			if(flag == false)//如果不是接受
			{
				g2d.drawImage(image, 0, 0, this);
				if(OpenGLApp.IsConnect)
				{
					if(!OpenGLApp.TypeFlag)
					{
						Client.ClientImageSend(image);
					}
					else
					{
						SocketThread.ServerImageSend(image);
					}
				}
			}
		}
	}
	
	public void addshape()//加入shape列表
	{
		switch(OpenGLApp.DrawType)//根据 选择的图形类型 把图形对象加入List中
		{
		case 1: //直线
			{
				Myline tline=new Myline(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
				shapes.add(tline);
			}
			break;
		case 2: //矩形
			if(!OpenGLApp.Full)
			{
				MyRectangle trec = new MyRectangle (false,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY));
				shapes.add(trec);
			}
			else
			{
				MyRectangle trec = new MyRectangle (true,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY));
				shapes.add(trec);
			}
			break;
		case 3: //椭圆
			if(!OpenGLApp.Full)
			{
				MyOval toval= new MyOval(false,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY)); 
				shapes.add(toval);				
			}
			else
			{
				MyOval toval= new MyOval(true,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY)); 
				shapes.add(toval);	
			}
			break;
		case 4://三角形
			//注释掉的是选择框成员的更新
//			{
//				MyRectangle trec = new MyRectangle (false,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY));
//				chooseField=trec;
				
//			}
			if(!OpenGLApp.Full)
			{
				MyTriangle ttri= new MyTriangle(false,
						Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),
						Math.min(oldMouseX,nowMouseX)+Math.abs(oldMouseX-nowMouseX),Math.min(oldMouseY,nowMouseY)+Math.abs(oldMouseY-nowMouseY),
						Math.min(oldMouseX,nowMouseX)+Math.abs(oldMouseX-nowMouseX),Math.min(oldMouseY,nowMouseY)-Math.abs(oldMouseY-nowMouseY)); 
				shapes.add(ttri);				
			}
			else
			{
				MyTriangle ttri= new MyTriangle(true,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),
						Math.min(oldMouseX,nowMouseX)+Math.abs(oldMouseX-nowMouseX),Math.min(oldMouseY,nowMouseY)+Math.abs(oldMouseY-nowMouseY),
						Math.min(oldMouseX,nowMouseX)+Math.abs(oldMouseX-nowMouseX),Math.min(oldMouseY,nowMouseY)-Math.abs(oldMouseY-nowMouseY)); 
				shapes.add(ttri);	
			}
			break;
		case 5://选择框的更新
			{
				MyRectangle trec = new MyRectangle (false,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY));
				chooseField=trec;
			}
		}
	}
	
	public void selectShape() //选择模式中，选择图形
	{
		if(OpenGLApp.DrawType!=5) //如果不是选择模式，把所有的图形都标为未选中，符合我们的设计逻辑
		{
			for(int i=0;i<shapes.size();i++)
			{
				shapes.get(i).isSelected=false;
			}
		}
		else //否则 对于每个图形，都要判断是不是被选中
		{
			for(int i=0;i<shapes.size();i++)
			{
				shapes.get(i).selected(chooseField);//把选择框传入每一个图形对象
					System.out.println("选择框的范围"+chooseField.x+"  "+chooseField.y+"  "+chooseField.width+"  "+chooseField.height);
					shapes.get(i).print();//调试用 输出到命令行给我看
					System.out.println(shapes.get(i).isSelected);
			}
		}
	}
	
	public void delete() //删除按钮调用的方法
	{
		if(OpenGLApp.DrawType==5)//只有选择了才能被删 否则没门
		{
			for(int i=0;i<shapes.size();i++)
			{
				if(shapes.get(i).isSelected==true)
					shapes.remove(shapes.get(i)); //被选中了，并且按了删除按钮，就会把对象从List里面移除掉 这样子，下一次画图的时候就不会画进来了
			}
		}
		repaint();
	}
	
	public void change(Graphics2D g) //修改选中的图形
	{
		if(OpenGLApp.DrawType==5 && OpenGLApp.Dash) //必须既是选择模式又是选择了修改才能进行下列操作
		{
			for(int i=0;i<shapes.size();i++)//遍历所有图形
			{
				if(shapes.get(i).isSelected==true)//如果被选中
				{
					if(shapes.get(i).CN()==1)//如果是直线
					{
						g.drawLine(shapes.get(i).x(), shapes.get(i).y(), X, Y);
						Myline l= (Myline) shapes.get(i);
						if(l.ps==1)
						{
							l.aX= X;
							l.aY= Y;
						}
						else if(l.ps==2)
						{
							l.bX= X;
							l.bY= Y;
						}
					}
					
					if(shapes.get(i).CN()==2)//矩形
					{
						MyRectangle r = (MyRectangle) shapes.get(i);
						g.drawRect(r.x,r.y,r.x+X-r.width,r.y+Y-r.height);
						r.width=r.x-r.width+X;
						r.height=r.y-r.height+Y;
					}

					if(shapes.get(i).CN()==3)//圆形
					{
						MyOval o= (MyOval) shapes.get(i);
						g.drawOval(o.x, o.y, o.x+X-o.width, o.y+Y-o.height);
						o.width=o.x-o.width+X;
						o.height=o.y-o.height+Y;
					}
					
					if(shapes.get(i).CN()==4)//三角形
					{
						MyTriangle t = (MyTriangle) shapes.get(i);
						if(t.ps==1)
						{
							int[] x = {X,t.bx,t.cx};
							int[] y = {Y,t.by,t.cy};
							g.drawPolygon(x, y, 3);
							t.ax=X;
							t.ay=Y;
						}
						else if(t.ps==2)
						{
							int[] x = {t.ax,X,t.cx};
							int[] y = {t.ay,Y,t.cy};
							g.drawPolygon(x, y, 3);
							t.bx=X;
							t.by=Y;
						}
						else
						{
							int[] x = {t.ax,t.bx,X};
							int[] y = {t.ay,t.by,Y};
							g.drawPolygon(x, y, 3);
							t.cx=X;
							t.cy=Y;
						}
					}
					
				}
				repaint(); //实时更新
			}
		}
	}
	
	private void SetStroke() //实时更新画笔
	{
		stroke = new BasicStroke(OpenGLApp.PenType,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f); //画笔粗细
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) //按下鼠标的操作
	{
		oldMouseX = nowMouseX = e.getX(); //更新坐标
		oldMouseY = nowMouseY = e.getY();
		flag = true;//flag变为真 
	}

	@Override
	public void mouseReleased(MouseEvent e) //鼠标松掉
	{
		nowMouseX = e.getX();
		nowMouseY = e.getY();
		flag = false;
		addshape(); //必须实时调用
		selectShape(); //同上
		repaint(); //同上
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseDragged(MouseEvent e) //拖动鼠标的操作
	{
		if(OpenGLApp.DrawType == 0)
		{
			oldMouseX = nowMouseX;
			oldMouseY = nowMouseY;
			nowMouseX = e.getX();
			nowMouseY = e.getY();
		}
		X = e.getX();
		Y = e.getY();
		repaint(); //这行代码实现了拖动的过程中实时画出图像
	} 

	@Override
	public void mouseMoved(MouseEvent e)
	{
		
	}


}
