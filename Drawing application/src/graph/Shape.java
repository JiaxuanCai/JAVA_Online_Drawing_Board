package graph;

import java.awt.Graphics2D;

public abstract class Shape {
	public boolean isSelected = false; //（用于选择模式中）标志这个图形是否被选中
	public abstract void draw(Graphics2D bg); //图形的draw方法，用于在画板上画出当前shape子类对象
	public abstract void selected(MyRectangle field); //（用于选择模式中）判断这个图形是否被选中
	public abstract void print(); //调试时用的工具函数
	public abstract int CN(); //返回图形是哪个子类
	public abstract int x(); //坐标
	public abstract int y(); //坐标
}
