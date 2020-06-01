package communication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;

import app.OpenGLApp;
import graph.Graphic;

public class SocketThread extends Thread //����Ĵ���·��
{
	private static Socket socket = null;
	private static OutputStream os = null;
	private static InputStream is = null;
    public SocketThread(Socket socket) //�߳�����
    {
    	this.socket = socket;
    	start(); //�����߳�
    }     
    public void run() //���н���
    {
    	while(true)
    	{
			ServerRecive();
    	}
    }
    
    public void ServerRecive() //�������˽�������
	{
		byte[] b = new byte[102400]; 
		int n = 0;
		try
		{
			is = socket.getInputStream();
		}
		catch (IOException e)//�쳣�Ĳ���
		{
			OpenGLApp.State.setText("��ͻ��˶Ͽ�����!");
		}
		try
		{
			n = is.read(b);
		}
		catch (IOException e)
		{
			OpenGLApp.State.setText("��ͻ��˶Ͽ�����!");
		}
		try//����ͼ��Ĵ���
			{
	            ByteArrayInputStream bin = new ByteArrayInputStream(b);
	            Graphic.image = ImageIO.read(bin);
	            Graphic.isResive = true;
	            OpenGLApp.place.repaint();
			}
		catch (IOException e)
			{
				OpenGLApp.State.setText("��ͻ��˶Ͽ�����!");
			}
		
	}
    
    public static void ServerImageSend(BufferedImage image) //��������ͼ����
	{
		try
		{
			os = socket.getOutputStream();//������Ĳ���
		}
		catch (IOException e)
		{
			OpenGLApp.State.setText("��ͻ��˶Ͽ�����!");
		}
    	try
    	{
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //����ͼ��
            ImageIO.write(image, "png", out);
            byte[] b = out.toByteArray();
            os.write(b);
    	}
    	catch (IOException e)
		{
    		OpenGLApp.State.setText("��ͻ��˶Ͽ�����!");
		}
	}
    
}