package graph;

import java.awt.Graphics2D;

public class MyTriangle extends Shape{
	public int ax,ay,bx,by,cx,cy;
	public boolean isFilled;
	public int ps;
	
	public MyTriangle(boolean f, int ax_ , int ay_ , int bx_ , int by_ , int cx_ , int cy_) //构造
	{
		isFilled=f;
		ax=ax_;
		ay=ay_;
		bx=bx_;
		by=by_;
		cx=cx_;
		cy=cy_;
	}
	
	@Override
	public void draw(Graphics2D bg) { //调用Graphic2D的drawPolygon方法
		// TODO Auto-generated method stub
		int[] x = {ax,bx,cx};
		int[] y = {ay,by,cy};
		if(!isFilled)
			bg.drawPolygon(x, y, 3);
		else
			bg.fillPolygon(x, y, 3);
	}

	@Override
	public void selected(MyRectangle field) {//判断是哪个点被选了
		// TODO Auto-generated method stub
		if(ax>field.x && ax<field.x+field.width && ay>field.y && ay<field.y+field.height)
		{
				isSelected=true;
				ps=1;
		}
		else if(bx>field.x && bx<field.x+field.width && by>field.y && by<field.y+field.height)
		{
				isSelected=true;
				ps=2;
		}
		else if(cx>field.x && cx<field.x+field.width && cy>field.y && cy<field.y+field.height)
		{
				isSelected=true;
				ps=3;
		}
		else
			isSelected=false;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int CN() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int x() {
		// TODO Auto-generated method stub
		if(ps==1) return ax;
		else if(ps==2) return bx;
		else return cx;
	}

	@Override
	public int y() {
		// TODO Auto-generated method stub
		if(ps==1) return ay;
		else if(ps==2) return by;
		else return cy;
	}

}
