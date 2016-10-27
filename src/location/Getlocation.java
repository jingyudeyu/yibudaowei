package location;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Getlocation {

	public Getlocation() {
		// TODO Auto-generated constructor stub
	}
	  public static Document getDoc(String BootURL) { // ��ȡbox
			Document document = null;
			int ii = 3;
			String userAgent = "Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0";
			// System.out.println(BootURL);
			while (document == null && ii != 0) {
				ii--;
				try {
					document = Jsoup.connect(BootURL).userAgent(userAgent)
							.timeout(30000).get();
				} catch (HttpStatusException htp) {
					// htp.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("=========��ȡ�ɹ�========");
			return document;
		}

	  /**  
	     * ͨ����γ�Ȼ�ȡ��ַ��Ϣ����һ�ַ���  
	     * @param latitude  
	     * @param longitude  
	     * @return ������  
	     */    
	    public String GetAddr(String latitude, String longitude) {      

	        /*  
	         * Ҳ������http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s������������������Ӣ�ĵ�ַ  
	         * ��Կ�������дһ��key=abc  
	         * output=csv,Ҳ������xml��json������ʹ��csv���ص��������෽�����      
	         */    
	      /*  String url = String.format(      
	            "http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",      
	            latitude, longitude);*/      
	        String addrUrl=String.format("http://api.map.baidu.com/geocoder?output=json&location=%s,%s&key=C66C0501D0280744759A6957C42543AE38F5D540", latitude,longitude);
	        System.err.println(addrUrl);
	      String addrJson=null;
	       Document doc=getDoc(addrUrl);
	    	addrJson=doc.select("body").html().replaceAll("&quot;", "\"");
	    System.out.println(addrJson);
	           return addrJson;      
	    }    
	
	  
}
