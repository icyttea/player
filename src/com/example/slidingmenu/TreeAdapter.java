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
    private List<Node> all = new ArrayList<Node>();//展示
    private List<Node> cache = new ArrayList<Node>();//缓存
    private TreeAdapter tree = this;
    boolean hasCheckBox;
    private int expandIcon = -1;//展开图标
    private int collapseIcon = -1;//收缩图标
    /**
	 * 构造方法
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
	 * 把一个节点上的所有的内容都挂上去
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
	 * 设置展开收缩图标
	 * @param expandIcon
	 * @param collapseIcon
	 *
	 */
	public void setCollapseAndExpandIcon(int expandIcon,int collapseIcon){
		this.collapseIcon = collapseIcon;
		this.expandIcon = expandIcon;
	}

	/**
	 * 控制展开缩放某节点
	 * @param location
	 *
	 */
	public void ExpandOrCollapse(int location){
		Node n = all.get(location);//获得当前视图需要处理的节点 
		if(n!=null)//排除传入参数错误异常
		{
			if(!n.isLeaf()){
				Log.i("leaf", "isParent");
				n.setExplaned(!n.isExplaned());// 由于该方法是用来控制展开和收缩的，所以取反即可
				filterNode();//遍历一下，将所有上级节点展开的节点重新挂上去
				this.notifyDataSetChanged();//刷新视图
			}
			else if(n.isLeaf()){
				Log.i("leaf", "isleaf");
				mSlidingMenu.toggle();
//				点击叶节点，用VLC播放器打开固定地址				
				LibVLC mLibVLC = null;
				try {
					mLibVLC = VLCInstance.getLibVlcInstance();
				} catch (LibVlcException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				mLibVLC.playMRL("http://192.168.9.158:9200/SIPServer/34020000001310000001_192.168.9.150_30000.sdp");
				mLibVLC.playMRL("rtsp://admin:12345@10.46.4.16/h264/ch1/main/av_stream");
				
//				//接入服务器，发送取消命令
//				String nameSpace = "http://sipserviceconsumer.monitor.videomonitor.direction/";
//				String methodName = "stopRealtimeStreamByDeviceId";
////				String EndPointness = "http://192.168.9.158:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				String EndPointness = "http://10.46.4.12:9100/VideoMonitor/services/ClientVodServicePublish?wsdl";
//				StopRealTimePlayerAsyncTask stp = new StopRealTimePlayerAsyncTask(con);
//				stp.execute(nameSpace, methodName, EndPointness);
//				
//				//接入服务器，请求流				
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
	 * 设置展开等级
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
	/* 清理all,从缓存中将所有父节点不为收缩状态的都挂上去*/
	public void filterNode(){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(!n.isParentCollapsed()||n.isRoot())//凡是父节点不收缩或者不是根节点的都挂上去
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
			
			//叶节点不显示展开收缩图标
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
			//设置是否显示头像图标
			if(n.getIcon()!=-1){
				vi.icon.setImageResource(n.getIcon());
				vi.icon.setVisibility(View.VISIBLE);
			}
			else{
				vi.icon.setVisibility(View.GONE);
			}
			//显示文本
			vi.tv.setText(n.getName());
			// 控制缩进
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
