package com.example.net;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.VLCInstance;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class StopRealTimePlayerAsyncTask extends AsyncTask<String, Integer, String>{

	String TAG = "StopRealTimePlayerAsyncTask";
	Context mContext;
	
	public StopRealTimePlayerAsyncTask(Context context){
		this.mContext = context;
	}
	
	//Get Android Service IP
	private String intToIp(int i) {       
		 
		return (i & 0xFF ) + "." +       
				((i >> 8 ) & 0xFF) + "." +       
				((i >> 16 ) & 0xFF) + "." +       
				( i >> 24 & 0xFF) ;  
		}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		// ���������ռ�ͷ����õ�SoapObject����
		SoapObject soapObject = new SoapObject(params[0],params[1]);
		String soapAction = params[0]+params[1];
		Log.i(TAG, "soapAction = " + soapAction);
		
		//Get Android IP		
		WifiManager wifiManager = (WifiManager)mContext.getSystemService(mContext.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();    
		String ip = intToIp(ipAddress);   
		Log.i(TAG, "ip = "+ip);
		
		
//		// ���������WebService�ӿ���Ҫ�����������,���ﴫ��ʱҪע��,��ʱ����ط�����,�ڴ��������ʱ,Ҫ��wsdl�ļ��ϵķ����Ĳ�����,�����п��ܱ���
		//soapObject.addProperty("arg0", "34020000001310000001");
		soapObject.addProperty("arg0", "45010200051310000017");
		soapObject.addProperty("arg1", ip);
		soapObject.addProperty("arg2", 30000);
		
		//initalize httptransport service
		HttpTransportSE httpSE = new HttpTransportSE(params[2]);
		httpSE.debug = true;
		
		// ͨ��SOAP1.1Э��õ�envelop����,������Ϣ
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelop.bodyOut = soapObject;
		envelop.setOutputSoapObject(soapObject);
		
		// ��ʼ����Զ�̷���
		try {
			httpSE.call(null, envelop);
			
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return "XmlPullParserException";
		}
		
		return null;
	}

}
