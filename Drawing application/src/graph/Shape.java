package graph;

import java.awt.Graphics2D;

public abstract class Shape {
	public boolean isSelected = false; //������ѡ��ģʽ�У���־���ͼ���Ƿ�ѡ��
	public abstract void draw(Graphics2D bg); //ͼ�ε�draw�����������ڻ����ϻ�����ǰshape�������
	public abstract void selected(MyRectangle field); //������ѡ��ģʽ�У��ж����ͼ���Ƿ�ѡ��
	public abstract void print(); //����ʱ�õĹ��ߺ���
	public abstract int CN(); //����ͼ�����ĸ�����
	public abstract int x(); //����
	public abstract int y(); //����
}