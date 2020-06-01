package graph;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Myline extends Shape{
	public int aX,aY,bX,bY;
//	public boolean isSelected;
	public int ps;
	
	public Myline(int ax, int ay, int bx, int by)//����
	{
		aX=ax;
		aY=ay;
		bX=bx;
		bY=by;
	}
	
	public void draw(Graphics2D bg) //����Graphic2D��drawLine����
	{
		bg.drawLine(aX,aY,bX,bY);
	}

	@Override
	public void selected(MyRectangle field) {///ȷ����ѡ�ĵ�
		if(aX>field.x && aX<field.x+field.width && aY>field.y && aY<field.y+field.height)
		{
				isSelected=true;
				ps=1;
		}
		else if(bX>field.x && bX<field.x+field.width && bY>field.y && bY<field.y+field.height)
		{
				isSelected=true;
				ps=2;
		}
		else
			isSelected=false;
	}

	 
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("ֱ��: "+aX+"   "+aY+"   "+bX+"   "+bY);
	}

	@Override
	public int CN() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int x() {//����ѡ�еĵ㲻ͬ�����ز�ͬ�ĵ������ֵ
		// TODO Auto-generated method stub
		if(ps==1) return bX;
		else return aY;
	}

	@Override
	public int y() {
		// TODO Auto-generated method stub
		if(ps==1) return bY;
		else return aY;
	}
}