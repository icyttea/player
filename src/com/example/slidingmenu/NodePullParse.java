package com.example.slidingmenu;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.example.rtspplayer.R;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class NodePullParse {
	
    public static ArrayList<NodeResource> ParseXml(XmlPullParser parser){
        ArrayList<NodeResource> DeviceArray = new ArrayList<NodeResource>();
        
        try {
            //��ʼ�����¼�
            int eventType = parser.getEventType();

            //�����¼����������ĵ�������һֱ����
            while (eventType != XmlPullParser.END_DOCUMENT) {                
                //��Ϊ������һ�Ѿ�̬�������������������switch
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        // �����κβ��������ʼ������
                        break;

                    case XmlPullParser.START_TAG:
                        // ����XML�ڵ�����
                        // ��ȡ��ǰ��ǩ����
                        String tagName = parser.getName();

                        if(tagName.equals("device")){
                        	String parentId = parser.getAttributeValue(0);
                        	String curId = parser.getAttributeValue(1);
                        	String name = parser.getAttributeValue(2);
                        	String cameraId = parser.getAttributeValue(3);
//                            provinceTemp = new Node();
//                            provinceTemp.setProvinceId(Integer.parseInt(parser.getAttributeValue(0)));
//                            provinceTemp.setProvinceName(parser.getAttributeValue(1));
                        	NodeResource deviceTemp = new NodeResource(parentId, curId, name, cameraId,R.drawable.icon_department);
                            DeviceArray.add(deviceTemp);                            
                        }else if(tagName.equals("site")||tagName.equals("company")||tagName.equals("city")||tagName.equals("province")){
                          	String parentId = parser.getAttributeValue(0);
                        	String curId = parser.getAttributeValue(1);
                        	String name = parser.getAttributeValue(2);
                        	String cameraId = "-1";
                        	NodeResource deviceTemp = new NodeResource(parentId, curId, name, cameraId,R.drawable.icon_department);
                            DeviceArray.add(deviceTemp);  
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        // ���ڵ���ɣ����������������µ�����
                        break;
                    case XmlPullParser.END_DOCUMENT:

                        break;
                }

                // ��������next����������һ���¼������˵Ľ���ͳ���ѭ��#_#
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return DeviceArray;
    }
    

}
