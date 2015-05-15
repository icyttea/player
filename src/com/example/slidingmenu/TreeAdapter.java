package com.example.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.VLCInstance;

import com.example.net.StartRealTimePlayerAsyncTask;
import com.example.net.StopRealTimePlayerAsyncTask;
import com.example.rtspplayer.MainActivity;
import com.example.rtspplayer.R;
import com.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeAdapter extends BaseAdapter{
	private Context con;
    private LayoutInflater lif;
    private SlidingMenu mSlidingMenu;
    private Bundle mBundle;
    private List<Node> all = new ArrayList<Node>();//չʾ
    private List<Node> cache = new ArrayList<Node>();//����
    private TreeAdapter tree = this;
    boolean hasCheckBox;
    private int expandIcon = -1;//չ��ͼ��
    private int collapseIcon = -1;//����ͼ��
    /**
	 * ���췽��
	 */
	public TreeAdapter(Context context,List<Node>rootNodes,SlidingMenu mMenu){
		this.con = context;
		this.lif = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mSlidingMenu = mMenu;
		for(int i=0;i<rootNodes.size();i++){
			addNode(rootNodes.get(i));
		}
	}
	/**
	 * ��һ���ڵ��ϵ����е����ݶ�����ȥ
	 * @param node
	 *
	 */
	public void addNode(Node node){
		all.add(node);
		cache.add(node);
		if(node.isLeaf())return;
		for(int i = 0;i<node.getChildrens().size();i++){
			addNode(node.getChildrens().get(i));
		}
	}
	/**
	 * ����չ������ͼ��
	 * @param expandIcon
	 * @param collapseIcon
	 *
	 */
	public void setCollapseAndExpandIcon(int expandIcon,int collapseIcon){
		this.collapseIcon = collapseIcon;
		this.expandIcon = expandIcon;
	}

	/**
	 * ����չ������ĳ�ڵ�
	 * @param location
	 *
	 */
	public void ExpandOrCollapse(int location){
		Node n = all.get(location);//��õ�ǰ��ͼ��Ҫ����Ľڵ� 
		if(n!=null)//�ų�������������쳣
		{
			if(!n.isLeaf()){
				Log.i("leaf", "isParent");
				n.setExplaned(!n.isExplaned());// ���ڸ÷�������������չ���������ģ�����ȡ������
				filterNode();//����һ�£��������ϼ��ڵ�չ���Ľڵ����¹���ȥ
				this.notifyDataSetChanged();//ˢ����ͼ
			}
			else if(n.isLeaf()){
				Log.i("leaf", "isleaf");
				mSlidingMenu.toggle();
//				���Ҷ�ڵ㣬��VLC�������򿪹̶���ַ				
				LibVLC mLibVLC = null;
				try {
					mLibVLC = VLCInstance.getLibVlcInstance();
				} catch (LibVlcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				mLibVLC.playMRL("http://192.168.9.158:9200/SIPServer/34020000001310000001_192.168.9.150_30000.sdp");
				mLibVLC.playMRL("rtsp://admin:12345@10.46.4.16/h264/ch1/main/av_stream");
				
//				//���������������ȡ������
//				String nameSpace = "http://sipserviceconsumer.monitor.videomonitor.direction/";
//				String methodName = "stopRealtimeStreamByDeviceId";
////				String EndPointness = "http://192.168.9.158:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				String EndPointness = "http://10.46.4.12:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				StopRealTimePlayerAsyncTask stp = new StopRealTimePlayerAsyncTask(con);
//				stp.execute(nameSpace, methodName, EndPointness);
//				
//				//�����������������				
//				nameSpace = "http://sipserviceconsumer.monitor.videomonitor.direction/";
//				methodName = "startRealtimeStream";
////				EndPointness = "http://192.168.9.158:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				EndPointness = "http://10.46.4.12:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				StartRealTimePlayerAsyncTask rtsp = new StartRealTimePlayerAsyncTask(con);
//				rtsp.execute(nameSpace, methodName, EndPointness);
			}
		}
		
	}
	/**
	 * ����չ���ȼ�
	 * @param level
	 *
	 */
	public void setExpandLevel(int level){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(n.getLevel()<=level){
			 if(n.getLevel()<level)
				 n.setExplaned(true);
			 else
				 n.setExplaned(false);
			 all.add(n);
			}
		}
		
	}
	/* ����all,�ӻ����н����и��ڵ㲻Ϊ����״̬�Ķ�����ȥ*/
	public void filterNode(){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(!n.isParentCollapsed()||n.isRoot())//���Ǹ��ڵ㲻�������߲��Ǹ��ڵ�Ķ�����ȥ
				all.add(n);
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return all.get(location);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int location, View view, ViewGroup viewgroup) {

		ViewItem vi = null;
		if(view == null){
			view = lif.inflate(R.layout.list_item, null);
			vi = new ViewItem();
			vi.flagIcon = (ImageView)view.findViewById(R.id.ivec);
			vi.tv = (TextView)view.findViewById(R.id.itemvalue);
			vi.icon =(ImageView)view.findViewById(R.id.ivicon);
			view.setTag(vi);
		}
		else{
			vi = (ViewItem)view.getTag();
			if(vi ==null)
				System.out.println();
		}
		Node n = all.get(location);
		if(n!=null){
			if(vi==null)
				System.out.println();    
			
			//Ҷ�ڵ㲻��ʾչ������ͼ��
			if(n.isLeaf()){
				vi.flagIcon.setVisibility(View.GONE);
			}
			else{
				vi.flagIcon.setVisibility(View.VISIBLE);
				if(n.isExplaned()){
					if(expandIcon!=-1){
						vi.flagIcon.setImageResource(expandIcon);
					}
				}
				else{
					if(collapseIcon!=-1){
						vi.flagIcon.setImageResource(collapseIcon);
					}
				}
			}
			//�����Ƿ���ʾͷ��ͼ��
			if(n.getIcon()!=-1){
				vi.icon.setImageResource(n.getIcon());
				vi.icon.setVisibility(View.VISIBLE);
			}
			else{
				vi.icon.setVisibility(View.GONE);
			}
			//��ʾ�ı�
			vi.tv.setText(n.getName());
			// ��������
			view.setPadding(30*n.getLevel(), 3,3, 3);
		}
		return view;
	}
    public class ViewItem{
    	private ImageView icon;
    	private ImageView flagIcon;
    	private TextView tv;
    }
}
