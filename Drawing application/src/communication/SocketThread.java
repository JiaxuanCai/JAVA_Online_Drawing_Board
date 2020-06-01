package communication;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import javax.imageio.ImageIO;

import app.OpenGLApp;
import graph.Graphic;

public class SocketThread extends Thread //虚拟的传输路径
{
	private static Socket socket = null;
	private static OutputStream os = null;
	private static InputStream is = null;
    public SocketThread(Socket socket) //线程启动
    {
    	this.socket = socket;
    	start(); //启动线程
    }     
    public void run() //运行进程
    {
    	while(true)
    	{
			ServerRecive();
    	}
    }
    
    public void ServerRecive() //服务器端接收数据
	{
		byte[] b = new byte[102400]; 
		int n = 0;
		try
		{
			is = socket.getInputStream();
		}
		catch (IOException e)//异常的捕获
		{
			OpenGLApp.State.setText("与客户端断开连接!");
		}
		try
		{
			n = is.read(b);
		}
		catch (IOException e)
		{
			OpenGLApp.State.setText("与客户端断开连接!");
		}
		try//尝试图像的传输
			{
	            ByteArrayInputStream bin = new ByteArrayInputStream(b);
	            Graphic.image = ImageIO.read(bin);
	            Graphic.isResive = true;
	            OpenGLApp.place.repaint();
			}
		catch (IOException e)
			{
				OpenGLApp.State.setText("与客户端断开连接!");
			}
		
	}
    
    public static void ServerImageSend(BufferedImage image) //服务器的图像传送
	{
		try
		{
			os = socket.getOutputStream();//输出流的操作
		}
		catch (IOException e)
		{
			OpenGLApp.State.setText("与客户端断开连接!");
		}
    	try
    	{
            ByteArrayOutputStream out = new ByteArrayOutputStream(); //传输图像
            ImageIO.write(image, "png", out);
            byte[] b = out.toByteArray();
            os.write(b);
    	}
    	catch (IOException e)
		{
    		OpenGLApp.State.setText("与客户端断开连接!");
		}
	}
    
}