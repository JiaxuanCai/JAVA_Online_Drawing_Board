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

public class Client //�ͻ��˷������
{
	private static Socket socket = null; //ʹ��Socket����ͻ���
	private static OutputStream os = null; //�����
	private static InputStream is = null; //������
	private static boolean StateFlag; //״̬�ı�ʶ��
	
	public static void StartClient()//�ͻ��˵ķ�������
	{
		OpenGLApp.State.setText("Ѱ�ҷ�����...");		 //��ʼ״̬��ΪѰ�ҷ�����
		try
		{
			StateFlag = true; //�ȿ��ܲ�������
			OpenGLApp.ServerIP = OpenGLApp.GoalIP.getText(); 
			socket = new Socket(OpenGLApp.ServerIP,6666);//��������
		}
		catch (UnknownHostException e) //������δ֪����
		{
			OpenGLApp.State.setText("δ֪������!");
			StateFlag = false;
		}
		catch (IOException e) //�޿ɼ�����������
		{
			OpenGLApp.State.setText("δ�ҵ�������!");
			StateFlag = false;
		}
		if(StateFlag) //���ȷʵ�Ѿ�����
		{
			OpenGLApp.State.setText("������!"); //״̬��Ϊ�Ѿ�����
			OpenGLApp.IsConnect = true; //ͬ��
			OpenGLApp.TypeFlag = false; //ͬ��
			while(true)
			{
				ClientRecive(); //��������
			}
		}
	}
	
	public static void ClientRecive() //�ͻ��˵Ľ��մ���
	{
    	byte[] b = new byte[102400];
    	int n = 0;
		try
		{
			is = socket.getInputStream(); //�����
		}
		catch (IOException e) //��������
		{
			OpenGLApp.State.setText("��������Ͽ�����!");
		}
		try
		{
			n = is.read(b);
		}
		catch (IOException e) //��������
		{
			OpenGLApp.State.setText("��������Ͽ�����!");
		}

		try
			{
	            ByteArrayInputStream bin = new ByteArrayInputStream(b); //��Thread����õ�������
	            Graphic.image = ImageIO.read(bin);
	            Graphic.isResive = true;
	            OpenGLApp.place.repaint();
			}
		catch (IOException e)
			{
				OpenGLApp.State.setText("��������Ͽ�����!");
			}
		
	}
	
	public static void ClientImageSend(BufferedImage image)//�ͻ��˴���ͼ��ķ���
	{
		try
		{
			os = socket.getOutputStream(); //��ö�Ӧ�������
		}
		catch (IOException e) //����Ĵ���
		{
			OpenGLApp.State.setText("��������Ͽ�����!");
		}
    	try
    	{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out); //д��ͼƬ����������
            byte[] b = out.toByteArray();
            os.write(b);
    	}
    	catch (IOException e)
		{
    		OpenGLApp.State.setText("��������Ͽ�����!");//����Ĵ���
		}
	}
	
}