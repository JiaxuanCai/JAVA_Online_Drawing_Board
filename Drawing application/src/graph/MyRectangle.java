package graph;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MyRectangle extends Shape{ //��ʼ��ʱ�����ƺ�ʹ�õ���һ����Rectangle��ͻ�ˣ����Ը������е�����
	public int x,y,width,height;
	public boolean isFilled;
//	public boolean isSelected;
	public int ps;
	
	public MyRectangle(boolean f, int parax, int paray, int paraw, int parah) //����
	{
		isFilled=f;
		x=parax;
		y=paray;
		width=paraw;
		height=parah;
	}
	
	public void draw(Graphics2D bg) //����Graphic2D��drawRect
	{
		if(!isFilled)
			bg.drawRect(x,y,width,height);
		else
			bg.fillRect(x,y,width,height);
	}

	@Override
	public void selected(MyRectangle field) { //ȷ�����ĸ��㱻ѡ����
		if(x>field.x && x<field.x+field.width && y>field.y && y<field.y+field.height)
		{
			isSelected=true;
			ps=1;
		}
		else if (x+width>field.x && x+width<field.x+field.width && y+height>field.y && y+height<field.y+field.height)
		{
			isSelected=true;
			ps=2;
		}
		else if (x>field.x && x<field.x+field.width && y+height>field.y && y+height<field.y+field.height)
		{
			isSelected=true;
			ps=3;
		}
		else if (x+width>field.x && x+width<field.x+field.width && y+height>field.y && y+height<field.y+field.height)
		{
			isSelected=true;
			ps=4;
		}
		else
			isSelected=false;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("������: "+x+"   "+y+"   "+width+"   "+height);
	}

	@Override
	public int CN() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int x() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int y() {
		// TODO Auto-generated method stub
		return 0;
	}
}
