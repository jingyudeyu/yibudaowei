package communication;

import login.LoginActivity;
import admin.Account_information;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class HandlerUnit extends Activity {

	Handler  handler;
	ClientThread clientThread;// �����������ͨ�ŵ����߳�
	 public   Handler handlerMsg(){
	        handler=new Handler()
	             {
	       
	             	@Override
	      			public void handleMessage(Message msg)
	      			{
	      				// �����Ϣ���������߳�
	      				if (msg.what == 0x123)
	      				{// ��ȡ���������ص�����					
	      					//�ַ������� 	        	
	      					if(msg.obj.equals("true"))
	      					{
	      						Toast toast=Toast.makeText(getApplicationContext(), "��ӭ��¼", Toast.LENGTH_SHORT);			
	      						toast.show();			
	      					}
	      					else if(msg.obj.toString().equals("false"))
	      					{
	      						Toast toast=Toast.makeText(getApplicationContext(), "�û������������", Toast.LENGTH_SHORT);			
	      						toast.show();											
	      					}	      	 				
	      				}							
	      			}
	      		};  
	      		return handler;
	            /*clientThread = new ClientThread(handler);
	           	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������          
*/	       } 
}
