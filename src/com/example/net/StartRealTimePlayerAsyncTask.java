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
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.rtspplayer.MainActivity;

public class StartRealTimePlayerAsyncTask extends AsyncTask<String, Integer, String> {

	String TAG = "StartRealTimePlayerAsyncTask";
	Context mContext;
	LibVLC mLibVLC;
	
	public StartRealTimePlayerAsyncTask(Context context){
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
		soapObject.addProperty("cameraCode", "45010200051310000017");
		soapObject.addProperty("receiveIp", ip);
		soapObject.addProperty("receivePort", 30000);
		
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
			
			// �õ�Զ�̷������ص�SOAP����
			SoapObject resultObj = (SoapObject) envelop.bodyIn;
			String result = resultObj.getProperty(0).toString();
			
			// �õ����������ص�����
			Log.i(TAG, result);
			
			if (result != null) {
				try {
					mLibVLC = VLCInstance.getLibVlcInstance();
				} catch (LibVlcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLibVLC.playMRL(result);
				Log.i(TAG, result.toString());
			}
			
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
