package graph;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MyOval extends Shape{
	public int x,y,width,height;
	public boolean isFilled;
//	public boolean isSelected;
	
	public MyOval(boolean f, int parax, int paray, int paraw, int parah) //����
	{
		isFilled=f;
		x=parax;
		y=paray;
		width=paraw;
		height=parah;
	}
	
	public void draw(Graphics2D bg) //����Graphic2D��drawOval����
	{
		if(!isFilled)
			bg.drawOval(x,y,width,height);
		else
			bg.fillOval(x,y,width,height);
	}

	@Override
	public void selected(MyRectangle field) {
		if(x>field.x && x<field.x+field.width && y>field.y && y<field.y+field.height)
			isSelected=true;
		else
			isSelected=false;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("��Բ: "+x+"   "+y);
	}

	@Override
	public int CN() {
		// TODO Auto-generated method stub
		return 3;
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