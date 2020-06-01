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
 * Graphic��
 * 
 * ��ɰ�������ɾ���ġ�����ͼ�λ����Ĳ���
 * 
 */


public class Graphic extends JScrollPane implements MouseListener , MouseMotionListener
{
	public List<Shape> shapes; //���ڴ��ͼ�ζ����List
	public MyRectangle chooseField; //���ڴ��ѡ���
	public static boolean isResive = false; //���տ���
	public boolean flag; //��־
	private BasicStroke stroke = null;  //������ʽ
	public static BufferedImage image = null; //���ͼ�ε��ļ�
	private int oldMouseX=-100,oldMouseY=-100,nowMouseX=-100,nowMouseY=-100,X,Y; //�ĸ����꣬����������Ļ滭����
	public Graphic()
	{
		this.addMouseListener(this); //�������
		this.addMouseMotionListener(this); //��������ƶ�
		stroke = new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f); //���ʴ�ϸ��ֵ��Ĭ��ֵ
		image = new BufferedImage(2000, 2000, BufferedImage.TYPE_INT_RGB); //��ʼ��image
		image.getGraphics().fillRect(0, 0, 2000, 2000); //��հװ�
		shapes = new LinkedList<Shape>(); //��ʼ��shapes List
		chooseField = new MyRectangle(false,0,0,0,0); //��ʼ��ѡ���
	}
	
	public void paint(Graphics g) //����paint����������Ŀǰ���ڻ����м�켣���Լ����յõ��Ļ��������ʵʱ���¡�
	{
		super.paint(g); //���ø����paint����
		Graphics2D g2d = (Graphics2D) g; //����
		if(isResive) //û���յ����տ���
		{
			g2d.drawImage(image, 0, 0, this);
			isResive = false;
		}
		else
		{
			Graphics2D bg = image.createGraphics(); //������ս���Ļ���
			SetStroke(); //ʵʱ���»��ʵĸ�ʽ����ϸ ֮��ģ�
			bg.setStroke(stroke); 
			bg.setColor(OpenGLApp.color); //ʵʱ���»�����ɫ
			bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//�����ṩ�Ŀ���ݷ���
			g2d.drawImage(image, 0, 0, this);
			
			if(OpenGLApp.DrawType!=0)//�������ǩ��ģʽ�����ɻ��ʣ���ÿ�λ���֮ǰ����ջ���
			{
				bg.setColor(new Color(255,255,255));
				bg.fillRect(0, 0, 2000, 2000);
				bg.setColor(OpenGLApp.color);
			}
			
			if(OpenGLApp.DrawType==0)//���ɻ��� ����
			{
				bg.drawLine(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
			}
			
			switch(OpenGLApp.DrawType)//�κ�ͼ�� �м����ͼ��
			{
			case 0://���ɻ�ͼ
				g2d.drawLine(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
				break;
			case 1://ֱ��
				g2d.drawLine(oldMouseX,oldMouseY,X,Y);
				break;
			case 2://����
				if(!OpenGLApp.Full)
					g2d.drawRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				else
					g2d.fillRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				break;
			case 3://��Բ
				if(!OpenGLApp.Full)
					g2d.drawOval(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				else
					g2d.fillOval(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				break;
			case 4://������
				//ע�͵�����ѡ�� 
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
			case 5://ѡ���
				{
					change(g2d);
					if(!OpenGLApp.Dash)
						g2d.drawRect(Math.min(oldMouseX,X),Math.min(oldMouseY,Y),Math.abs(oldMouseX-X),Math.abs(oldMouseY-Y));
				}
			}
			
			for(int i=0;i<shapes.size();i++) //�б��ڵ�Ԫ�� �������ڻ�����
			{
				if(OpenGLApp.DrawType!=5)
					shapes.get(i).draw(bg);//�������ѡ��ģʽ ����ѡ���
				else 
				{
//						System.out.println(shapes.get(i).isSelected);
					if(shapes.get(i).isSelected==false)
						shapes.get(i).draw(bg);
					else if(shapes.get(i).isSelected==true)
					{
						bg.setColor(new Color(176,196,222)); //��ѡ�е�ͼ�񣬱�Ϊ��ɫ���Խ��б�ʾ
						shapes.get(i).draw(bg);
						bg.setColor(OpenGLApp.color);
					}
				}
			}
			
			if(OpenGLApp.DrawType==5)//�����ѡ��ģʽ�������յ�ѡ����ڻ�����
			{
				BasicStroke tstroke = new BasicStroke(0.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f,new float[]{5f,5f},10f);
				bg.setStroke(tstroke);
				bg.setColor(new Color(105,105,105));//������ɫ�����߿� �ÿ�һ��
				if(!OpenGLApp.Dash)
					chooseField.draw(bg);
				bg.setColor(OpenGLApp.color);
				bg.setStroke(stroke);
			}
			
			if(flag == false)//������ǽ���
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
	
	public void addshape()//����shape�б�
	{
		switch(OpenGLApp.DrawType)//���� ѡ���ͼ������ ��ͼ�ζ������List��
		{
		case 1: //ֱ��
			{
				Myline tline=new Myline(oldMouseX,oldMouseY,nowMouseX,nowMouseY);
				shapes.add(tline);
			}
			break;
		case 2: //����
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
		case 3: //��Բ
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
		case 4://������
			//ע�͵�����ѡ����Ա�ĸ���
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
		case 5://ѡ���ĸ���
			{
				MyRectangle trec = new MyRectangle (false,Math.min(oldMouseX,nowMouseX),Math.min(oldMouseY,nowMouseY),Math.abs(oldMouseX-nowMouseX),Math.abs(oldMouseY-nowMouseY));
				chooseField=trec;
			}
		}
	}
	
	public void selectShape() //ѡ��ģʽ�У�ѡ��ͼ��
	{
		if(OpenGLApp.DrawType!=5) //�������ѡ��ģʽ�������е�ͼ�ζ���Ϊδѡ�У��������ǵ�����߼�
		{
			for(int i=0;i<shapes.size();i++)
			{
				shapes.get(i).isSelected=false;
			}
		}
		else //���� ����ÿ��ͼ�Σ���Ҫ�ж��ǲ��Ǳ�ѡ��
		{
			for(int i=0;i<shapes.size();i++)
			{
				shapes.get(i).selected(chooseField);//��ѡ�����ÿһ��ͼ�ζ���
					System.out.println("ѡ���ķ�Χ"+chooseField.x+"  "+chooseField.y+"  "+chooseField.width+"  "+chooseField.height);
					shapes.get(i).print();//������ ����������и��ҿ�
					System.out.println(shapes.get(i).isSelected);
			}
		}
	}
	
	public void delete() //ɾ����ť���õķ���
	{
		if(OpenGLApp.DrawType==5)//ֻ��ѡ���˲��ܱ�ɾ ����û��
		{
			for(int i=0;i<shapes.size();i++)
			{
				if(shapes.get(i).isSelected==true)
					shapes.remove(shapes.get(i)); //��ѡ���ˣ����Ұ���ɾ����ť���ͻ�Ѷ����List�����Ƴ��� �����ӣ���һ�λ�ͼ��ʱ��Ͳ��ử������
			}
		}
		repaint();
	}
	
	public void change(Graphics2D g) //�޸�ѡ�е�ͼ��
	{
		if(OpenGLApp.DrawType==5 && OpenGLApp.Dash) //�������ѡ��ģʽ����ѡ�����޸Ĳ��ܽ������в���
		{
			for(int i=0;i<shapes.size();i++)//��������ͼ��
			{
				if(shapes.get(i).isSelected==true)//�����ѡ��
				{
					if(shapes.get(i).CN()==1)//�����ֱ��
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
					
					if(shapes.get(i).CN()==2)//����
					{
						MyRectangle r = (MyRectangle) shapes.get(i);
						g.drawRect(r.x,r.y,r.x+X-r.width,r.y+Y-r.height);
						r.width=r.x-r.width+X;
						r.height=r.y-r.height+Y;
					}

					if(shapes.get(i).CN()==3)//Բ��
					{
						MyOval o= (MyOval) shapes.get(i);
						g.drawOval(o.x, o.y, o.x+X-o.width, o.y+Y-o.height);
						o.width=o.x-o.width+X;
						o.height=o.y-o.height+Y;
					}
					
					if(shapes.get(i).CN()==4)//������
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
				repaint(); //ʵʱ����
			}
		}
	}
	
	private void SetStroke() //ʵʱ���»���
	{
		stroke = new BasicStroke(OpenGLApp.PenType,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f); //���ʴ�ϸ
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e) //�������Ĳ���
	{
		oldMouseX = nowMouseX = e.getX(); //��������
		oldMouseY = nowMouseY = e.getY();
		flag = true;//flag��Ϊ�� 
	}

	@Override
	public void mouseReleased(MouseEvent e) //����ɵ�
	{
		nowMouseX = e.getX();
		nowMouseY = e.getY();
		flag = false;
		addshape(); //����ʵʱ����
		selectShape(); //ͬ��
		repaint(); //ͬ��
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseDragged(MouseEvent e) //�϶����Ĳ���
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
		repaint(); //���д���ʵ�����϶��Ĺ�����ʵʱ����ͼ��
	} 

	@Override
	public void mouseMoved(MouseEvent e)
	{
		
	}


}