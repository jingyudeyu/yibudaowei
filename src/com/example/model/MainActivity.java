package com.example.model;




import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pimg.Pimgdes;

import com.view.RoundImageDrawable;

import communication.ClientThread;
import communication.ImgThread;
import location.Getlocation;
import login.LoginActivity;
import title_bar_Activity.TitlebarActivity;
import userFunction.FindCarWei;
import userFunction.GateInOut;
import userFunction.GateInOut1;
import userFunction.ParkInformation;
import userFunction.PayMoney;
import wifiAdapter.WifiAutoConnectManager;
import wifiAdapter.WifiMain;
import wifiAdapter.WifiAutoConnectManager.WifiCipherType;
import admin.Account_information;
import admin.Account_manage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends TitlebarActivity {
	ImageView image_park_information;
	ImageView image_gate_in_out;
    ImageView imagepay_money;
    ImageView imagefind_car;
   // ImageView image_find_carwei;
    
    WifiManager wifiManager;
	WifiAutoConnectManager wac;//
	Bitmap bitmap;
    Button back;
    Button other;
	WifiInfo wifiInfo ;
    Handler handler;
    ClientThread clientThread;// �����������ͨ�ŵ����߳�
    ImgThread imgthread;
    String ipText;
    Handler imghandler;
	String pl=null;
	static SharedPreferences sp ;
	static String name_number;
	static String wifiname;


private static final String URL = "http://avatar.csdn.net/3/2/4/2_ameyume.jpg";  
	
View weather_re;
	ImageView img;
	TextView textview1;
	TextView wrong;
	private String cityname;
	private TextView weatherDespText;    //������ʾ����������Ϣ  
	private TextView temp1Text;          //������ʾ�������  
	private TextView temp2Text;          //������ʾ�������  
	private TextView currentDateText;    //������ʾ��ǰ����  
	private ImageView imgday;
	private ImageView imgnight;
	TextView temp1_1;
	TextView current_date2;
	
	
	
    public void handlerMsg(){
        handler=new Handler()
             {
             	@Override
      			public void handleMessage(Message msg)
      			{
      				// �����Ϣ���������߳�
      				if (msg.what == 0x123)
      				{// ��ȡ������
      					
     					if(msg.obj.toString()!=null&&msg.obj.toString().contains("pl"))
      					{
      						String[] PL=msg.obj.toString().split(";");
      						pl=PL[1];
      						 Intent intent=new Intent(MainActivity.this, ParkInformation.class);
      						intent.putExtra("ParkList",pl);
      						 startActivity(intent);	
      					}
     					
     					
     					else if(msg.obj.toString()!=null&&msg.obj.toString().contains("kong")){
     						String[] Pinfo=msg.obj.toString().split(";");
     						String pinfo=Pinfo[1];
  
     						Intent intent=new Intent(MainActivity.this, GateInOut.class);
       						intent.putExtra("pinfo",pinfo);
       					//	intent.putExtra("wifiname", wifiname);
						//	intent.putExtra("wifiq", wifiInfo.getRssi()+"");
       					//	Toast toast1=Toast.makeText(getApplicationContext(), pinfo+"", Toast.LENGTH_SHORT);			
       					// toast1.show();
       						 startActivity(intent);	
     					}
	
      				}/*else if(msg.what==0x246){
      					if(msg.getData()!=null){
      						Bundle bd=msg.getData();
      						byte[] data=bd.getByteArray("imgpp");
      						Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
      						
      						Intent intent=new Intent(MainActivity.this,Pimgdes.class);  
							ByteArrayOutputStream baos=new ByteArrayOutputStream();  
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
							byte [] bitmapByte =baos.toByteArray();  
							intent.putExtra("bitmap", bitmapByte);  
							startActivity(intent);  
      					}
      				}*/
      			}
      		};    
      	
            clientThread = new ClientThread(handler);
           	new Thread(clientThread).start();  	// �ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������          
       } 
   public void ggHandler(){
       imghandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==0x246){
  					if(msg.getData()!=null){
  						Bundle bd=msg.getData();
  						byte[] data=bd.getByteArray("imgpp");
  						Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
  						System.err.println(bitmap);
  					   Intent intent=new Intent(MainActivity.this,Pimgdes.class);  
						ByteArrayOutputStream baos=new ByteArrayOutputStream();  
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
						byte [] bitmapByte =baos.toByteArray();  
						intent.putExtra("bitmap", bitmapByte);  
						startActivity(intent);  
  					}
  				}
			}
        	
        };
       
        imgthread=new ImgThread(imghandler);
        new Thread(imgthread).start();
   }
  
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     
     
      if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        init();   
     // openWifi();                
       this.handlerMsg(); 
      //  this.ggHandler();
        image_park_information.setOnClickListener(new OnClickListener() {				          	   
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub							
				try {
				    Message msg = new Message();
					msg.what = 0x345;
					msg.obj = "parkinglist";
					clientThread.revHandler.sendMessage(msg);
			    } 
	    	   catch (Exception e)
	    	   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }		
		
				
			}
		});

    	image_gate_in_out.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				
				sp = getSharedPreferences("userInfo", 0);
				  //ȡ���ֻ�������
				 name_number = sp.getString("USER_NAME",null); 
				
				//String wifiname="";
				String wifipwd="";
				
				String nowwifi=getConnectWifiSsid().replaceAll("\"", "");
				
				WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE); 
				 wifiInfo = wifiManager.getConnectionInfo();		
				 System.out.println(nowwifi);
				 
				 try
				{
					 wifiname=getWifiName();
					 wifipwd="xiaoqiao2";
					 
					 if(wifiname==null){
								Toast toast = Toast.makeText(MainActivity.this, "δ�ҵ���ȷwifi", Toast.LENGTH_SHORT);
								toast.setGravity(Gravity.CENTER, 0, 0);
								toast.show();
					 }else{
						 
					 
					
					if(wifiname.equals(nowwifi))
					{
						System.out.println("������ͬ����������");
						boolean WifiIsConnect;
						WifiIsConnect=isWifiConnect();
						if(WifiIsConnect==false){
							Toast toast = Toast.makeText(MainActivity.this, "wifiû������", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							
						}else{
							//Toast toast = Toast.makeText(MainActivity.this, "Wifi �Ѿ�����", Toast.LENGTH_SHORT);
							//toast.setGravity(Gravity.CENTER, 0, 0);
							//toast.show();
							if(wifiInfo.getRssi()>-50){
								/*Intent intent=new Intent(MainActivity.this, GateInOut1.class);
								intent.putExtra("wifiname", wifiname);
								intent.putExtra("wifiq", wifiInfo.getRssi()+"");
								startActivity(intent);*/
							
								 Message msg = new Message();
									msg.what = 0x345;  
									msg.obj = "kong;"+wifiname+";"+name_number;
									clientThread.revHandler.sendMessage(msg);
							                  }else{
								Toast toast1 = Toast.makeText(MainActivity.this, "Wifi ǿ��ֵ������������ǰ��ʻ", Toast.LENGTH_SHORT);
								toast1.setGravity(Gravity.CENTER, 0, 0);
								toast1.show();
							}
							
							
						}
					}else{
						wac.connect(wifiname, wifipwd,wifipwd.equals("")?WifiCipherType.WIFICIPHER_NOPASS:WifiCipherType.WIFICIPHER_WPA);
				
						boolean WifiIsConnect;
						WifiIsConnect=isWifiConnect();
						if(WifiIsConnect==false){
							Toast toast = Toast.makeText(MainActivity.this, "Wifi is NOT connected yet", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}else{
							Toast toast = Toast.makeText(MainActivity.this, "Wifi is connected yet", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							if(wifiInfo.getRssi()>-50){
								Intent intent=new Intent(MainActivity.this, GateInOut1.class);
								intent.putExtra("wifiname", wifiname);
								intent.putExtra("wifiq", wifiInfo.getRssi()+"");
								//startActivity(intent);
							}
							
							
						}
					}
					 }
					
				} 
				catch(Exception e)
				{
				  e.printStackTrace();
				}
				
				
				
				
				/*	//��ȡ����ʱ��
				SimpleDateFormat formatter = new SimpleDateFormat ("yyyy:MM:dd HH:mm:ss ");
				Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
				String str = formatter.format(curDate);
				
				System.out.println(str);
				//textView1.setText(str);
				
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();		
				
				Toast toast1=Toast.makeText(getApplicationContext(), wifiInfo.getRssi()+"", Toast.LENGTH_SHORT);			
				 toast1.show();	
		
				   Message msg = new Message();
					msg.what = 0x345;  
					msg.obj = "kong;"+wifiname+";"+name_number;
					clientThread.revHandler.sendMessage(msg);
				
				 if(wifiInfo.getRssi()>-50)
						{																
						 try 
						 {
							    Message msg = new Message();
								msg.what = 0x345;
								msg.obj = "kong;"+wifiname;
								clientThread.revHandler.sendMessage(msg);
						    } 
				    	   catch (Exception e)
				    	   {
							// TODO Auto-generated catch block
							e.printStackTrace();
				    	   }
				}*/
				
			}
			
		});
        
       imagefind_car.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 	
 				Intent intent=new Intent(MainActivity.this,FindCarWei.class);
     			startActivity(intent);	

 			}
 		});     
  
        addListener();  
   	}   
    //���ؽ�Ҫ���ӵ�wifi��
    private String getWifiName(){
    	 WifiManager wifiMg = (WifiManager)getSystemService(WIFI_SERVICE);  
         
         List<ScanResult> list = wifiMg.getScanResults();
         String wifiname=null;
    	if(list!=null){
    		System.out.println("wifi������ "+list.size());
    		for (ScanResult scanResult : list) {
				System.out.println(scanResult.SSID+" "+scanResult.level);
				if(scanResult.level>-100){
					if(scanResult.SSID.contains("god")){
						wifiname=scanResult.SSID;
					}
				}
			}
    	}

    	return wifiname;
    	
    	
    	
    }
    
    
    //���ص�ǰ���ӵ�wifi��
    private String getConnectWifiSsid(){  
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
        Log.d("wifiInfo", wifiInfo.toString());  
        Log.d("SSID",wifiInfo.getSSID());  
        return wifiInfo.getSSID();  
 }  
    
  
 //�ж��Ƿ�ɹ�����wifi
    
    public boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
     }
 
    public void init()
	{	
    	
    	
    	image_park_information=(ImageView) findViewById(R.id.image_park_information);   
    	image_gate_in_out=(ImageView) findViewById(R.id.image_gate_in_out);
    	imagepay_money=(ImageView) findViewById(R.id.image_pay_money);   
    	imagefind_car=(ImageView)findViewById(R.id.image_find_car);
    	
    	wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wac = new WifiAutoConnectManager(wifiManager);
    	
    	Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.parking_info);  
    	Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.in_out); 
    	Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.payment_money1); 
    	Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.find_car_navigation); 
    	
    	image_park_information.setImageDrawable(new RoundImageDrawable(bitmap1));
    	image_gate_in_out.setImageDrawable(new RoundImageDrawable(bitmap2));
    	imagepay_money.setImageDrawable(new RoundImageDrawable(bitmap3));
    	imagefind_car.setImageDrawable(new RoundImageDrawable(bitmap4));
    		
    	back=(Button) findViewById(R.id.button_back);    
        other=(Button)findViewById(R.id.button_other);
        back.setVisibility(View.GONE);   
        
        
        
        weather_re=(View)findViewById(R.id.weather_re);
        weather_re.getBackground().setAlpha(150);//������͸��
        
        wrong=(TextView)findViewById(R.id.wrong);
	//	Bitmap bitmap11 = BitmapFactory.decodeResource(getResources(),R.drawable.pd); //
         current_date2=(TextView)findViewById(R.id.current_date2);
		textview1=(TextView) findViewById(R.id.weather);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);  
	    temp1Text = (TextView) findViewById(R.id.temp1);  
	    temp2Text = (TextView) findViewById(R.id.temp2);  
	    currentDateText = (TextView) findViewById(R.id.current_date);  
	    imgday=(ImageView) findViewById(R.id.imageday);
	    imgnight=(ImageView) findViewById(R.id.imagenight);
	    temp1_1=(TextView) findViewById(R.id.temp1_1);  
	    
	    if(NetIsConnect(MainActivity.this)){
            if(this.GpsIsOpen()){
            	//Toast.makeText(this, "GPSģ������", Toast.LENGTH_SHORT).show();  
            	
	  String  addJson=this.GetLocation();
	
	JSONObject adjson;
	try {
		adjson = new JSONObject(addJson);
		JSONObject resultJson= adjson.getJSONObject("result"); 
		String address=resultJson.getString("formatted_address");
		System.out.println(address);
		JSONObject detail=resultJson.getJSONObject("addressComponent");
		String city=detail.getString("city");
		String district=detail.getString("district");
		String province=detail.getString("province");
		String street=detail.getString("street");
		String streetnum=detail.getString("street_number");
		cityname=district;
		textview1.setText(district);//---------------------------------------------
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String ccode=null;
	try{
 ccode=java.net.URLEncoder.encode(cityname);
	}catch(Exception e){
		e.printStackTrace();
	}
	if(ccode!=null){
	String weatherUrl="http://api.map.baidu.com/telematics/v3/weather?location="+ccode+"&output=json&ak=MPDgj92wUYvRmyaUdQs1XwCf";
	
	String weatherJson=queryStringForGet(weatherUrl);
	System.out.println(weatherJson);
	 
	JSONObject jsonweaObject;
	JSONObject jobweather = null;

	try {
		jsonweaObject = new JSONObject(weatherJson);
		JSONArray jarrywea=jsonweaObject.getJSONArray("results") ;  
		JSONObject jsondatawea=jarrywea.getJSONObject(0);   
		JSONArray jarrywee=jsondatawea.getJSONArray("weather_data");  
		 jobweather=jarrywee.getJSONObject(0);  
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	/*
	 * ��ʾ������Ϣ�ڽ�����
	 */
	try {
		//current_date2.setText(jsondatawea.getString("results"));//---------------------------------------------
	//	currentDateText.setText(jobweather.getString("date"));
		 ArrayList<String> Pinfo=new ArrayList<String>();
		   Pinfo=SplitS(jobweather.getString("date"));
		   try{
			   currentDateText.setText(Pinfo.get(0));
			   current_date2.setText(Pinfo.get(1).replace("ʵʱ��", " ").replace(")", " "));
			   }catch(Exception e){
				   e.printStackTrace();
			   }
		   
		weatherDespText.setText(jobweather.getString("weather"));
  //��ȡͼƬ������ʾ
        
        try {  
            byte[] data = getImage(jobweather.getString("dayPictureUrl"));  
            if(data!=null){  
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
                imgday.setImageBitmap(bitmap);// display image  
                
            }else{  
                Toast.makeText(MainActivity.this, "Image error!", 1).show();  
            }  
        } catch (Exception e) {  
          //  Toast.makeText(MainActivity.this,"Newwork error!", 1).show();  
          //  e.printStackTrace();  

        }  
//��ȡͼƬ������ʾ
        
        try {  
            byte[] data = getImage(jobweather.getString("nightPictureUrl"));  
            if(data!=null){  
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap  
                imgnight.setImageBitmap(bitmap);// display image  
            }else{  
              //  Toast.makeText(MainActivity.this, "Image error!", 1).show();  
            	System.out.println("null");
            }  
        } catch (Exception e) {  
           // Toast.makeText(MainActivity.this,"Newwork error!", 1).show();  
           // e.printStackTrace(); 
        	System.out.println("null");
        }  
        temp1Text.setText(jobweather.getString("temperature"));
        temp2Text.setText(jobweather.getString("wind"));     
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	/*	Intent intent=getIntent();  
    if(intent !=null)  
    {  
        byte [] bis=intent.getByteArrayExtra("bitmap");  
        Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);  
        img.setImageBitmap(bitmap);  
    }  */
            }
        }else{
        	temp1_1.setVisibility(View.GONE);
        	wrong.setText("�뿪��GPS��");
        	
        	//Toast.makeText(this, "�뿪��GPS��", Toast.LENGTH_SHORT).show();  
        }
        }else{
        	temp1_1.setVisibility(View.GONE);
        	wrong.setText("�뿪�����ݣ�");
        	//Toast.makeText(this, "�뿪�����ݣ�", Toast.LENGTH_SHORT).show();  
        }
}
    public static String readData(InputStream inSream, String charsetName) throws Exception{  
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = -1;  
		while( (len = inSream.read(buffer)) != -1 ){  
		outStream.write(buffer, 0, len);  
		}  
		byte[] data = outStream.toByteArray();  
		outStream.close();  
		inSream.close();  
		return new String(data, charsetName);  
		}
	
	 /** 
     * �����ѯ 
     */  
    public static String queryStringForGet(String url) {  
        HttpGet request = new HttpGet(url);  
        String result = null;  
        try {  
            HttpResponse response = new DefaultHttpClient().execute(request);  
            if (response.getStatusLine().getStatusCode() == 200) {  
                result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);  
                return result;  
            }  
        } catch (ClientProtocolException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            result = "�����쳣��";  
            return result;  
        } catch (ParseException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            result = "�����쳣��";  
            return result;  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return result;  
    }  

    /** 
     * Get image from newwork 
     * @param path The path of image 
     * @return 
     * @throws Exception 
     */  
    public static byte[] getImage(String path) throws Exception{  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        InputStream inStream = conn.getInputStream();  
        if(conn.getResponseCode()==200){  
            return readStream(inStream);  
        }  
        return null;  
    }  
  
    /** 
     * Get data from stream 
     * @param inStream 
     * @return 
     * @throws Exception 
     */  
    public static byte[] readStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1){  
            outStream.write(buffer, 0, len);  
        }  
        outStream.close();  
        inStream.close();  
        return outStream.toByteArray();  
    }  
    
    /*
     * �ж������Ƿ��
     */
    
    public boolean NetIsConnect(Activity activity){
    	Context context = activity.getApplicationContext();
        // ��ȡ�ֻ��������ӹ�����󣨰�����wi-fi,net�����ӵĹ���
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // ��ȡNetworkInfo����
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===״̬===" + networkInfo[i].getState());
                    System.out.println(i + "===����===" + networkInfo[i].getTypeName());
                    // �жϵ�ǰ����״̬�Ƿ�Ϊ����״̬
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /*
     *�ж�gps�Ƿ��
     */
    
    public  Boolean GpsIsOpen(){
		 LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
		 if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {  
			 return true;
		 }else{
			 return false;
		 }
    }
    private  String GetLocation(){
    	String cityName="";
    	 // ��ȡλ�ù������  
        LocationManager locationManager;  
        String serviceName = Context.LOCATION_SERVICE;  
        locationManager = (LocationManager) this.getSystemService(serviceName);  
        // ���ҵ�������Ϣ  
        Criteria criteria = new Criteria();  
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // �߾���  
        criteria.setAltitudeRequired(false);  
        criteria.setBearingRequired(false);  
        criteria.setCostAllowed(true);  
        criteria.setPowerRequirement(Criteria.POWER_HIGH); // �͹���  
 
        String provider = locationManager.getBestProvider(criteria, true); // ��ȡGPS��Ϣ  
      
        Location location = locationManager.getLastKnownLocation(provider); // ͨ��GPS��ȡλ��  
       if(location==null){
    	   location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
       
       System.out.println("����λ��"+location);
       
       }
       if(location!=null){
    	   String latitude=location.getLatitude()+"";
           String longitude=location.getLongitude()+"";
           Getlocation gl=new Getlocation();
          cityName=gl.GetAddr(latitude, longitude);
       }
        // ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N��  
        locationManager.requestLocationUpdates(provider, 100 * 1000, 500,  
                locationListener); 
       return cityName;
    }
    /**  
     * ��λ�ı�ʱ���������е���  
     */    
    private final static LocationListener locationListener = new LocationListener() {    
      
        public void onLocationChanged(Location location) {    
        }    
    
        public void onProviderDisabled(String provider) {    
        }    
    
        public void onProviderEnabled(String provider) {    
        }    
    
        public void onStatusChanged(String provider, int status, Bundle extras) {    
        }    
    };    
    
    
    
    
    
    
	    
	    
        
     
        
     

    private void addListener()
    {	  
    	
 
    	
    	imagepay_money.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				Intent intent=new Intent(MainActivity.this,PayMoney.class);
     			startActivity(intent);				
 			}
 		});        
    	
    	

    }
    
    
 
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
           exitTime = System.currentTimeMillis();
       } 
        else 
        {
           finish();
    	
           System.exit(0);
           android.os.Process.killProcess(android.os.Process.myPid());    
       }
    }
    
    @Override
    	public boolean onKeyDown(int keyCode, KeyEvent event)
    	{ 
    	 if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
    	 { 
    	 
    	 onBackPressed();
    	  return false; 
    	 } 
    	 return false; 
    	}
    
    public static ArrayList<String> SplitS(String str){
		String[] ss= str.split("\\(");
		ArrayList<String>  list=new ArrayList<String>();
		 for (String string : ss) {
			list.add(string);
		}
		 return list;
	 }
	
}
