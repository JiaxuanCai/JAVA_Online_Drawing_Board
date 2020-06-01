package graph;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Myline extends Shape{
	public int aX,aY,bX,bY;
//	public boolean isSelected;
	public int ps;
	
	public Myline(int ax, int ay, int bx, int by)//构造
	{
		aX=ax;
		aY=ay;
		bX=bx;
		bY=by;
	}
	
	public void draw(Graphics2D bg) //调用Graphic2D的drawLine方法
	{
		bg.drawLine(aX,aY,bX,bY);
	}

	@Override
	public void selected(MyRectangle field) {///确定被选的点
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
		System.out.println("直线: "+aX+"   "+aY+"   "+bX+"   "+bY);
	}

	@Override
	public int CN() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int x() {//根据选中的点不同，返回不同的点的坐标值
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
