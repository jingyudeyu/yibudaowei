package communication;

import android.app.Activity;
import android.os.Message;

public class SendMsgToServer extends Activity{

	ClientThread clientThread;// �����������ͨ�ŵ����߳�
	public void sendMsg(String str)
	{
		 Message msg = new Message();
		 msg.what = 0x345;
		 msg.obj=str;
	 	 clientThread.revHandler.sendMessage(msg);
	}
	
	   
    
}
