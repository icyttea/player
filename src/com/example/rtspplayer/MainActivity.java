package com.example.rtspplayer;

import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.VLCInstance;

import com.slidingmenu.lib.SlidingMenu;
import com.example.net.StartRealTimePlayerAsyncTask;
import com.example.net.StopRealTimePlayerAsyncTask;
import com.example.slidingmenu.SampleListFragment;

import android.support.v4.app.FragmentActivity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity implements Callback, IVideoPlayer {
	
	private static final String TAG = "LibVLC";
	
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	public 	LibVLC mLibVLC;
	public  StopRealTimePlayerAsyncTask stp;
	public  SlidingMenu mSlidingMenu;

	private int mVideoHeight;
	private int mVideoWidth;
	private int mVideoVisibleHeight;
	private int mVideoVisibleWidth;
	private int mSarNum;
	private int mSarDen;
    private int mSurfaceAlign;  
    private static final int SURFACE_SIZE = 3;              
    private static final int SURFACE_BEST_FIT = 0;    
    private static final int SURFACE_FIT_HORIZONTAL = 1;    
    private static final int SURFACE_FIT_VERTICAL = 2;    
    private static final int SURFACE_FILL = 3;    
    private static final int SURFACE_16_9 = 4;    
    private static final int SURFACE_4_3 = 5;    
    private static final int SURFACE_ORIGINAL = 6;    
    private int mCurrentSize = SURFACE_BEST_FIT; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Set surface view
		setContentView(R.layout.activity_main);
		mSurfaceView = (SurfaceView) findViewById(R.id.videoview);
		mSurfaceView.setKeepScreenOn(true);
		
		//Set LibVLC
		 try {
	            mLibVLC = VLCInstance.getLibVlcInstance();
	        } catch (LibVlcException e) {
	            Log.i(TAG, "LibVLC initialisation failed");
	            return;
	        }
		mLibVLC.eventVideoPlayerActivityCreated(true);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Set surface holder
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		mSurfaceHolder.addCallback(this);

		//Set SlidingMenu
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.menu_frame);
		
		//Add List Fragment to FrameLayout
		SampleListFragment menuFrame = new SampleListFragment(mSlidingMenu);
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.menu_frame, menuFrame).commit();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mLibVLC != null) {
			//mLibVLC.playMRL("rtsp://218.204.223.237:554/live/1/0547424F573B085C/gsfp90ef4k0a6iap.sdp");
			//mLibVLC.playMRL("rtsp://admin:12345@10.46.4.16/h264/ch1/main/av_stream");
		} 
	}
	
	@Override 
	protected void onPause(){
		super.onPause();
		
		if(mLibVLC!=null){
			mLibVLC.stop();
			mSurfaceView.setKeepScreenOn(false);
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(mLibVLC!=null){
			mLibVLC.eventVideoPlayerActivityCreated(false);
		}
//		stp = new StopRealTimePlayerAsyncTask(this.getApplicationContext());
//		stp.execute("http://sipserviceconsumer.monitor.videomonitor.direction/", 
//				"stopRealtimeStreamByDeviceId",
//				"http://192.168.9.158:9100/VideoMonitor/services/ClientVodServicePublish?wsdl");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mLibVLC != null){
			mSurfaceHolder = holder;
			mLibVLC.attachSurface(holder.getSurface(), this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mSurfaceHolder = holder;
		if(mLibVLC != null){
			mLibVLC.attachSurface(holder.getSurface(), this);
		}
		if(width>0){
			mVideoHeight = height;
			mVideoWidth = width;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mLibVLC != null){
			mLibVLC.detachSurface();
		}
	}
	
	private void changeSurfaceSize() {  
        // get screen size  
        int dw = getWindow().getDecorView().getWidth();  
        int dh = getWindow().getDecorView().getHeight();  

        // getWindow().getDecorView() doesn't always take orientation into account, we have to correct the values  
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;  
        if (dw > dh && isPortrait || dw < dh && !isPortrait) {  
            int d = dw;  
            dw = dh;  
            dh = d;  
        }  

        // sanity check  
        if (dw * dh == 0 || mVideoWidth * mVideoHeight == 0) {  
            Log.e(TAG, "Invalid surface size");  
            return;  
        }  

        // compute the aspect ratio  
        double ar, vw;  
        double density = (double)mSarNum / (double)mSarDen;  
        if (density == 1.0) {  
            /* No indication about the density, assuming 1:1 */  
            vw = mVideoWidth;  
            ar = (double)mVideoWidth / (double)mVideoHeight;  
        } else {  
            /* Use the specified aspect ratio */  
            vw = mVideoWidth * density;  
            ar = vw / mVideoHeight;  
        }  

        // compute the display aspect ratio  
        double dar = (double) dw / (double) dh;  

        switch (mCurrentSize) {  
            case SURFACE_BEST_FIT:  
                if (dar < ar)  
                    dh = (int) (dw / ar);  
                else  
                    dw = (int) (dh * ar);  
                break;  
            case SURFACE_FIT_HORIZONTAL:  
                dh = (int) (dw / ar);  
                break;  
            case SURFACE_FIT_VERTICAL:  
                dw = (int) (dh * ar);  
                break;  
            case SURFACE_FILL:  
                break;  
            case SURFACE_16_9:  
                ar = 16.0 / 9.0;  
                if (dar < ar)  
                    dh = (int) (dw / ar);  
                else  
                    dw = (int) (dh * ar);  
                break;  
            case SURFACE_4_3:  
                ar = 4.0 / 3.0;  
                if (dar < ar)  
                    dh = (int) (dw / ar);  
                else  
                    dw = (int) (dh * ar);  
                break;  
            case SURFACE_ORIGINAL:  
                dh = mVideoHeight;  
                dw = (int) vw;  
                break;  
        }  

        // align width on 16bytes  
        int alignedWidth = (mVideoWidth + mSurfaceAlign) & ~mSurfaceAlign;  

        // force surface buffer size  
        mSurfaceHolder.setFixedSize(alignedWidth, mVideoHeight);  

        // set display size  
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();  
        lp.width = dw * alignedWidth / mVideoWidth;  
        lp.height = dh;  
        mSurfaceView.setLayoutParams(lp);  
        mSurfaceView.invalidate();  
    }  
	
	@Override
	public void setSurfaceSize(int width, int height, int visible_width,
			int visible_height, int sar_num, int sar_den) {
		// TODO Auto-generated method stub
			mVideoHeight = height;
		    mVideoWidth = width;
		    mVideoVisibleHeight = visible_height;
		    mVideoVisibleWidth = visible_width;
		    mSarNum = sar_num;
		    mSarDen = sar_den;
		    mHandler.removeMessages(SURFACE_SIZE);
		    mHandler.sendEmptyMessage(SURFACE_SIZE);
	}
	
	private Handler mHandler = new Handler(){

	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case SURFACE_SIZE:
	                changeSurfaceSize();
	                break;
	            }
	        }
	  };
	  
}
