   package login;

import com.example.model.MainActivity;
import com.example.model.R;

import communication.ClientThread;
import pre_register.pre_registerActivity;
import title_bar_Activity.TitlebarActivity;
import admin.Account_information;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends  TitlebarActivity   {
	Handler  handler;
	EditText Account; //�û��˺�
	EditText Password;//�û�����
	Button   Login;   //��¼��ť
	Button   Register;//ע�ᰴť
	//TextView Show;    //�����ı�����ʾע����˺�
	
	TextView t_title;//����������
	Button imageButton;//�˻�����ť

			
	ClientThread clientThread;// �����������ͨ�ŵ����߳�
	
	SharedPreferences sp;//��¼״̬
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
       super.onCreate(savedInstanceState);
       setContentView(R.layout.login);          
       initView();  //��ʼ������ؼ�           
       t_title.setText("��¼����");
       imageButton.setVisibility(View.GONE);
       this.handlerMsg();

       addListener(); //��Ӽ�����Ӧ�¼�
    } 
      
      public void handlerMsg(){
        handler=new Handler()
             {	
             	@Override
      			public void handleMessage(Message msg)
      			{
      				// �����Ϣ���������߳�
      				if (msg.what == 0x123)
      				{// ��ȡ������					    				
      					if(msg.obj.equals("true"))
      					{
      						Toast toast=Toast.makeText(getApplicationContext(), "��ӭ��¼", Toast.LENGTH_SHORT);			
      						toast.show();
      						Intent intent=new Intent(LoginActivity.this,MainActivity.class);
      						startActivity(intent);	
      						finish();
      					}
      					else if(msg.obj.toString().equals("false"))
      					{
      						Toast toast=Toast.makeText(getApplicationContext(), "�û������������", Toast.LENGTH_SHORT);			
      						toast.show();											
      					}
      	 				
      				}							
      			}
      		};    
            clientThread = new ClientThread(handler);
           	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������          
       } 

	private void initView() {
		// TODO Auto-generated method stub
    
	    sp = this.getSharedPreferences("userInfo", 0); //���ʵ������ 
		
		Account=(EditText) findViewById(R.id.account);
		Password=(EditText) findViewById(R.id.password);
		Login=(Button) findViewById(R.id.login);
		Register=(Button) findViewById(R.id.register);
	 //   Show=(TextView) findViewById(R.id.show);     
	    t_title=(TextView) findViewById(R.id.text_title);	    
	    imageButton = (Button) findViewById(R.id.button_other); 
	    if(sp.getBoolean("ISCHECK", false))  //��������
        {  
	    Account.setText(sp.getString("USER_NAME", ""));  //
        Password.setText(sp.getString("PASSWORD", ""));// 
        }
        if(sp.getBoolean("AUTO_ISCHECK", false))  //�Զ���¼
        {  
           
              //��ת����  
              Intent intent = new Intent(LoginActivity.this,MainActivity.class);  
              startActivity(intent);	
              finish();
        } 
	}
	
	 
	private void addListener() {
		// TODO Auto-generated method stub
		          
		 Login.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					
				     String name = Account.getText().toString();//��ȡ�û���
				     String password =Password.getText().toString();//��ȡ�û�����
				     
				     Editor editor = sp.edit();  
                     editor.putString("USER_NAME", name);//�û��� �� SharedPreferences��sp������
                     editor.putString("PASSWORD",password);//���� �� SharedPreferences��sp������
                     editor.commit();   
                     sp.edit().putBoolean("ISCHECK", true).commit();  //��������
                     sp.edit().putBoolean("AUTO_ISCHECK", true).commit(); //�Զ���½
				    
				     if (name.equals("") || password.equals("")) 
				      {
				    	 Toast toast1=Toast.makeText(getApplicationContext(), "�û��������벻��Ϊ��", Toast.LENGTH_SHORT);			
	 				     toast1.show();	
				      }//if				      
				
				     else
				     {//����������͵�¼����request,���û����������װ�ɣ�ĳ�����ͣ�,���͸����������ȴ�����������������Ӧ����
				    	 try {
							    Message msg = new Message();
								msg.what = 0x345;
								msg.obj = "login;"+Account.getText().toString()+";"+Password.getText().toString();
								clientThread.revHandler.sendMessage(msg);
						    } 
				    	   catch (Exception e)
				    	   {
							// TODO Auto-generated catch block
							e.printStackTrace();
						   }				    					    	
				     }
				   }//onClick
				});//LOGIN_OnClickListener
		 
			Register.setOnClickListener(new OnClickListener()
			{		
				@Override
				public void onClick(View v) {
					
					Intent intent=new Intent(LoginActivity.this, pre_registerActivity.class);
					startActivity(intent);
				}
			});	//Register_OnClickListener		
		   }  
	
}

 


