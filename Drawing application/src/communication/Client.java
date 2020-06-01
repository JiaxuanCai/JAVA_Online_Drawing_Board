package communication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import app.OpenGLApp;
import graph.Graphic;

public class Client //客户端服务代码
{
	private static Socket socket = null; //使用Socket定义客户端
	private static OutputStream os = null; //输出流
	private static InputStream is = null; //输入流
	private static boolean StateFlag; //状态的标识符
	
	public static void StartClient()//客户端的服务启动
	{
		OpenGLApp.State.setText("寻找服务器...");		 //初始状态即为寻找服务器
		try
		{
			StateFlag = true; //先看能不能连上
			OpenGLApp.ServerIP = OpenGLApp.GoalIP.getText(); 
			socket = new Socket(OpenGLApp.ServerIP,6666);//建立连接
		}
		catch (UnknownHostException e) //服务器未知错误
		{
			OpenGLApp.State.setText("未知服务器!");
			StateFlag = false;
		}
		catch (IOException e) //无可见服务器错误
		{
			OpenGLApp.State.setText("未找到服务器!");
			StateFlag = false;
		}
		if(StateFlag) //如果确实已经连上
		{
			OpenGLApp.State.setText("已连接!"); //状态设为已经连上
			OpenGLApp.IsConnect = true; //同上
			OpenGLApp.TypeFlag = false; //同上
			while(true)
			{
				ClientRecive(); //反复接受
			}
		}
	}
	
	public static void ClientRecive() //客户端的接收代码
	{
    	byte[] b = new byte[102400];
    	int n = 0;
		try
		{
			is = socket.getInputStream(); //输出流
		}
		catch (IOException e) //处理错误
		{
			OpenGLApp.State.setText("与服务器断开连接!");
		}
		try
		{
			n = is.read(b);
		}
		catch (IOException e) //处理错误
		{
			OpenGLApp.State.setText("与服务器断开连接!");
		}

		try
			{
	            ByteArrayInputStream bin = new ByteArrayInputStream(b); //从Thread里面得到的数据
	            Graphic.image = ImageIO.read(bin);
	            Graphic.isResive = true;
	            OpenGLApp.place.repaint();
			}
		catch (IOException e)
			{
				OpenGLApp.State.setText("与服务器断开连接!");
			}
		
	}
	
	public static void ClientImageSend(BufferedImage image)//客户端传输图像的方法
	{
		try
		{
			os = socket.getOutputStream(); //获得对应的输出流
		}
		catch (IOException e) //错误的处理
		{
			OpenGLApp.State.setText("与服务器断开连接!");
		}
    	try
    	{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out); //写出图片给服务器端
            byte[] b = out.toByteArray();
            os.write(b);
    	}
    	catch (IOException e)
		{
    		OpenGLApp.State.setText("与服务器断开连接!");//错误的处理
		}
	}
	
}
