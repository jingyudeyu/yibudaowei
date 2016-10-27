package userFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.model.MainActivity;
import com.example.model.R;

import communication.ClientThread;
import title_bar_Activity.TitlebarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GateInOut extends TitlebarActivity {

	TextView t_title;
	Button other;

	//TextView wifi_name;
//	TextView wifi_du;
	TextView welcome;
	TextView pa_name;//ͣ������
	TextView pa_left;//���пճ�λ
	
	
	
	TextView we_name;//�û���
	TextView pa_time2;//����ʱ��
	TextView pa_standard2;//�շѱ�׼		
	Button in;//��ť
	
	String pname = "";
	String pcri = "";
String intime="";

Handler handler;
ClientThread clientThread;// �����������ͨ�ŵ����߳�


static SharedPreferences sp ;
static String name_number;

String save_time;

public void handlerMsg(){
    handler=new Handler()
         {
         	@Override
  			public void handleMessage(Message msg)
  			{
  				// �����Ϣ���������߳�
  				if (msg.what == 0x123)
  				{// ��ȡ������
  					
 					if(msg.obj.toString()!=null&&msg.obj.toString().contains("getin"))
 					{
 						
 					
 						AlertDialog.Builder builder =new AlertDialog.Builder(GateInOut.this);
 						builder.setTitle(pname);
 						//builder.setAdapter(new ArrayAdapter<String>(this, resource, textViewResourceId), null);
 						builder.setMessage(name_number+"\n"+"̧�˽����ɹ���"+"\n"+"ʱ��:"+getNowtime());
 			
 	                 	builder.show();
 	                 
 	                 	Timer timer = new Timer();
 	                 	TimerTask task=new TimerTask() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								 Intent intent=new Intent(GateInOut.this, MainActivity.class);
		  						 startActivity(intent);	
		  						GateInOut.this.finish();
							}
						};
						timer.schedule(task, 1000 * 3);
 	                 	
 	             
 			}
 					
  					}
 					if(msg.obj.toString()!=null&&msg.obj.toString().contains("getout"))
 					{
 						//Toast toast1=Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT);			
      					// toast1.show();
  						
      					AlertDialog.Builder builder =new AlertDialog.Builder(GateInOut.this);
 						
      					builder.setTitle(pname);
 						builder.setMessage(name_number+"\n"+"̧�˳����ɹ���"+"\n"+"����:"+msg.obj.toString().replace("getout;", "")+"  Ԫ"+"\n"+"����ʱ�䣺"+getintime(save_time));
 	                 	builder.show();
 	                 	Timer timer = new Timer();
	                 	TimerTask task=new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							 Intent intent=new Intent(GateInOut.this, MainActivity.class);
	  						 startActivity(intent);	
	  						GateInOut.this.finish();
						}
					};
					timer.schedule(task, 1000 * 3);

  					}
  					
  				}							
  			
  		};    
        clientThread = new ClientThread(handler);
       	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������          
   } 



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_inout);

		init();

	}

	public void init() {
		t_title = (TextView) findViewById(R.id.text_title);
		other = (Button) findViewById(R.id.button_other);
		t_title.setText("��<����>��");
		other.setVisibility(View.GONE);

	//	wifi_name = (TextView) findViewById(R.id.wifi_name);
	//	wifi_du = (TextView) findViewById(R.id.wifi_du);
		we_name = (TextView) findViewById(R.id.we_name);
		pa_time2 = (TextView) findViewById(R.id.pa_time2);
		pa_standard2 = (TextView) findViewById(R.id.pa_standard2);///////////////////////
		
		
		
		
		welcome = (TextView) findViewById(R.id.welcome);
		pa_name = (TextView) findViewById(R.id.pa_name);
		pa_left = (TextView) findViewById(R.id.pa_left);
		in = (Button) findViewById(R.id.in);

		Intent intent = getIntent();
		String PI = intent.getStringExtra("pinfo");
	//	String WIFI = intent.getStringExtra("wifiname");
	//	String wifiQ = intent.getStringExtra("wifiq");
	//	wifi_name.setText(WIFI);
	//	wifi_du.setText(wifiQ);
		
		ArrayList<String> pinfo = new ArrayList<String>();
		pinfo = SplitString(PI);
		pa_name.setText(pinfo.get(0));
		pa_left.setText(pinfo.get(1)+"��");

		pname = pinfo.get(0);   //��ȡͣ������
		pcri = pinfo.get(2);    //��ȡͣ��������
		intime=pinfo.get(3);   //��ȡ����ʱ��
		
		
		sp = getSharedPreferences("userInfo", 0);
		//ȡ���ֻ�������
		 name_number = sp.getString("USER_NAME",null); 
		 
		 
		we_name.setText(name_number);
		pa_time2.setText(getintime(getNowtime()));
		pa_standard2.setText(pcri+"Ԫ/Сʱ");
		
		
		
		
	     this.handlerMsg();
		if (PI.contains("false")) {
			in.setText("����");

			in.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					
					try {
					    Message msg = new Message();
						msg.what = 0x345;
						msg.obj = "getin;"+name_number+";"+pname+";"+getNowtime();
						clientThread.revHandler.sendMessage(msg);
				    } 
		    	   catch (Exception e)
		    	   {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }		
					
					
				}
			});
		} else {
			in.setText("����");

			in.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub				
					///����ʱ���㷨
					double pay=CalPfees(intime, getNowtime(), pcri);
					save_time=getNowtime();
					try {
					    Message msg = new Message();
						msg.what = 0x345;
					//	msg.obj = "getout;"+name_number+";"+pname+";"+getNowtime()+";"+pay;
						msg.obj = "getout;"+name_number+";"+pname+";"+save_time+";"+pay;//----------------------
						clientThread.revHandler.sendMessage(msg);
				    } 
		    	   catch (Exception e)
		    	   {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }			
				}
			});
		}

	}

	public static ArrayList<String> SplitString(String str) {
		String[] ss = str.split("\\+");
		ArrayList<String> list = new ArrayList<String>();
		for (String string : ss) {
			list.add(string);
		}
		return list;
	}
	public static String getNowtime(){
		//��ȡ��ǰʱ��
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
		String str = formatter.format(curDate);
		return str;
	}
	/*
	 * ����ͣ������
	 * 
	 */
	public static  double  CalPfees(String inTime,String ouTime,String pcri2){
		
		double fees=0;
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				java.util.Date ot=df.parse(ouTime);
				java.util.Date it=df.parse(inTime);
				long l=ot.getTime()-it.getTime();
				
				  long day=l/(24*60*60*1000);
				   long hour=(l/(60*60*1000)-day*24);
				   long min=((l/(60*1000))-day*24*60-hour*60);
				   long s=(l/1000-day*24*60*60-hour*60*60-min*60);
				   
				   double p=Double.parseDouble(pcri2);
				   
				   System.out.println(""+day+"��"+hour+"Сʱ"+min+"��"+s+"��");
				   if(min<30&day==0&&hour==0){
					   fees=0;
				   }else{
					   fees=(day*24+hour+1)*p;
				   }
				  
				   
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return fees;
		
	}
	
	 public static String getintime(String in_time)  {
		 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 SimpleDateFormat df2=new SimpleDateFormat("MM"+"��"+"dd"+"��"+"HH:mm");		
		Date d=new Date();
		String str = null;
		try {
			str = df2.format(df.parse(in_time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return str;
		 
	
	 }
}