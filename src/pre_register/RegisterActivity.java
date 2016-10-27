 package pre_register;

import login.LoginActivity;
import title_bar_Activity.TitlebarActivity;

import com.example.model.MainActivity;
import com.example.model.R;

import communication.ClientThread;
import admin.Account_information;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends TitlebarActivity implements OnClickListener {
	Button button_car_number;
	Button button_my_register;
	CheckBox ck_agreen;
	ListView lv=null;
	static String phone1;
	 String carnum;
	EditText user_name;
	EditText password1;
	EditText password2;
	EditText car_number_shuzi;
	TextView t_title;//����������
	Button imageButton;//ImageButton
	Button buttonback;
	
	ClientThread clientThread;// �����������ͨ�ŵ����߳�
	Handler  handler;
	private ButtonOnClick buttonOnClick = new ButtonOnClick(1);
	private String[] provinces = new String[]{ "ԥ", "��", "��", "³", "��", "��","��","��","��","��","��","��","��",
			"��","��","��","��","��","��","��","��","��","��","��","��","��","��","��"};
		
	/*public void handlerMsg(){
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
      						Toast toast=Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT);			
      						toast.show();
      						Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
      						startActivity(intent);		
      					}
      					else if(msg.obj.toString().equals("false"))
      					{
      						Toast toast=Toast.makeText(getApplicationContext(), "ע��ʧ�ܣ������ֻ�����ע�ᣩ", Toast.LENGTH_SHORT);			
      						toast.show();											
      					}
      	 				
      				}							
      			}
      		};    
            clientThread = new ClientThread(handler);
           	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������          
       } */
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
      
        init();
        t_title.setText("ע�����");
        imageButton.setVisibility(View.GONE);
        
        handler=new Handler()
        {	
        	@Override
 			public void handleMessage(Message msg)
 			{
 				// �����Ϣ���������߳�
 				if (msg.what == 0x123)
 				{// ��ȡ������					    				
 					if(msg.obj.equals("registertrue"))
 					{
 						Toast toast=Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT);			
 						toast.show();
 						Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
 						startActivity(intent);		
 					}
 					else if(msg.obj.toString().equals("registerfalse"))
 					{
 						Toast toast=Toast.makeText(getApplicationContext(), "ע��ʧ�ܣ������ֻ�����ע�ᣩ", Toast.LENGTH_SHORT);			
 						toast.show();											
 					}
 	 				
 				}							
 			}
 		};    
       clientThread = new ClientThread(handler);
      	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������
button_my_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				carnum=button_car_number.getText().toString()+car_number_shuzi.getText().toString();
				if(password1.getText().toString().equals(password2.getText().toString())){
				 try {
					    Message msg = new Message();
						msg.what = 0x345;
						msg.obj = "register;"+phone1+";"+password1.getText().toString()+";"+user_name.getText().toString()+";"+carnum;
						clientThread.revHandler.sendMessage(msg);
				    } 
		    	   catch (Exception e)
		    	   {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }	
				}
				else
				 {
			    	 Toast toast1=Toast.makeText(getApplicationContext(), "���벻һ��", Toast.LENGTH_SHORT);			
				     toast1.show();	
			      }
			
				
			}
		});
        
        
        addListener();	
 }  
	
	public void init()
	  {
		user_name=(EditText)findViewById(R.id.user_name);
		password1=(EditText)findViewById(R.id.password1);
		password2=(EditText)findViewById(R.id.password2);
		car_number_shuzi=(EditText)findViewById(R.id.car_number_shuzi);
		   button_car_number=(Button)findViewById(R.id.car_number);//���ƺŵĺ���λ
	       button_my_register=(Button)findViewById(R.id.my_register);//ע��
	       ck_agreen=(CheckBox)findViewById(R.id.ck_agreen);//Э��checkbox
	       
	       t_title=(TextView) findViewById(R.id.text_title);
	       imageButton = (Button) findViewById(R.id.button_other); 
	       buttonback=(Button)findViewById(R.id.button_back);
	       Intent intent = getIntent();  
		     phone1 = intent.getStringExtra("phone");
		         
	       
	  }
	
	
	public void addListener()
	{
		buttonback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		button_car_number.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSingleChoiceDialog();
				carnum=button_car_number.getText().toString()+car_number_shuzi.getText().toString();
			}
		});
		/*button_my_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				carnum=button_car_number.getText().toString()+car_number_shuzi.getText().toString();
				if(password1.getText().toString().equals(password2.getText().toString())){
				 try {
					    Message msg = new Message();
						msg.what = 0x345;
						msg.obj = "register;"+phone1+";"+password1.getText().toString()+";"+user_name.getText().toString()+";"+carnum;
						clientThread.revHandler.sendMessage(msg);
				    } 
		    	   catch (Exception e)
		    	   {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }	
				}
				else
				 {
			    	 Toast toast1=Toast.makeText(getApplicationContext(), "���벻һ��", Toast.LENGTH_SHORT);			
				     toast1.show();	
			      }
			
				
			}
		});*/
		      
	}
	private void showSingleChoiceDialog()
	{
		new AlertDialog.Builder(this).setTitle("ѡ��ʡ��").setSingleChoiceItems(
				provinces, 1, buttonOnClick).setPositiveButton("ȷ��",buttonOnClick).setNegativeButton("ȡ��", buttonOnClick).show();
		
	}
	private class ButtonOnClick implements DialogInterface.OnClickListener
	{
		private int index;

		public ButtonOnClick(int index)
		{
			this.index = index;
		}
		@Override
		public void onClick(DialogInterface dialog, int whichButton)
		{
			if (whichButton >= 0)
			{
				index = whichButton;					
			}
			else
			{
				if (whichButton == DialogInterface.BUTTON_POSITIVE)
				{
					   button_car_number=(Button)findViewById(R.id.car_number);
					   button_car_number.setText(provinces[index]);
					
				}
				else if (whichButton == DialogInterface.BUTTON_NEGATIVE)
				{
					
				}
			}
			
		}
   }		
}
