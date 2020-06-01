package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import app.OpenGLApp;

public class Server //�������˵ķ������
{
	private static ServerSocket serverSocket = null; //ʹ��ServerSocket���������
	public static Socket socket = null; //client ��socket
	
	public static void StartServer() throws IOException
	{
		serverSocket = new ServerSocket(6666);//ʹ��һ�����Կ��еĶ˿� �������� �˿���Ҫ����һ����������ȽϿ죨����
		OpenGLApp.State.setText("���ڽ�������...");
		while(true)
		{
			socket = serverSocket.accept();//�������
			OpenGLApp.State.setText("������!");
			OpenGLApp.IsConnect = true;
			OpenGLApp.TypeFlag = true;
			new SocketThread(socket); //�½�socket
		}
	}

}