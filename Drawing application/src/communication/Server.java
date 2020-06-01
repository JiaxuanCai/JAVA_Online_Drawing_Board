package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import app.OpenGLApp;

public class Server //服务器端的服务代码
{
	private static ServerSocket serverSocket = null; //使用ServerSocket定义服务器
	public static Socket socket = null; //client 的socket
	
	public static void StartServer() throws IOException
	{
		serverSocket = new ServerSocket(6666);//使用一个电脑空闲的端口 建立连接 端口名要霸气一点这样传输比较快（滑稽
		OpenGLApp.State.setText("正在建立连接...");
		while(true)
		{
			socket = serverSocket.accept();//获得连接
			OpenGLApp.State.setText("已连接!");
			OpenGLApp.IsConnect = true;
			OpenGLApp.TypeFlag = true;
			new SocketThread(socket); //新建socket
		}
	}

}
