 package communication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ClientThread implements Runnable
{
	private Socket s;	
	private Handler handler;// ������UI�̷߳�����Ϣ��Handler����	
	public Handler revHandler;// �������UI�̵߳���Ϣ��Handler����	
	BufferedReader br = null;// ���߳��������Socket����Ӧ��������
	OutputStream os = null;
	BufferedWriter bw=null;
	public ClientThread(Handler handler)//�ͻ����̳߳�ʼ��
	{
		this.handler = handler;
	}
	
	
	
	public void run()
	{
		try
		{
			s = new Socket("192.168.155.1", 8144);
			s.setSoTimeout(0);
			br = new BufferedReader(new InputStreamReader(s.getInputStream(),"gbk"));
			os = s.getOutputStream();
		//	bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"gbk"));
		
			new Thread()
			{// ����һ�����߳�����ȡ��������Ӧ������
				@Override
				public void run()
				{
					String content = null;
					// ���϶�ȡSocket�������е�����
					try
					{
						while ((content = br.readLine()) != null)
						{
							// ÿ���������Է�����������֮�󣬷�����Ϣ֪ͨ����
							// ������ʾ������
							System.out.println(br.readLine());
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							
							handler.sendMessage(msg);
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				
				
			}.start();
			// Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			// ����revHandler����
			revHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{		// ����UI�߳����û����������

					if (msg.what == 0x345)
					{	// ���û����ı��������������д������
						try
						{
							//bw.write(msg.obj.toString());
							os.write((msg.obj.toString() + "\r\n").getBytes("gbk"));
						//	bw.flush();
						}
						catch (Exception e)
						{
							System.out.println("��������");
							try {
							      s.close();
								s = new Socket("192.168.191.1", 8132);
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								System.out.println("io����");
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
						try {
							os.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					   
					   
					   
					   
				}
			};
			// ����Looper
			Looper.loop();
		}
		catch (SocketTimeoutException e1)
		{
			System.out.println("�������ӳ�ʱ����");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

