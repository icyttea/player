package com.example.net;

import android.os.AsyncTask;
import android.util.Log;

public class NetControlAsyncTask extends AsyncTask<String, Integer, String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if (result.equals("success")) {
			//mMediaPlayer.playMRL(result);
		}
		super.onPostExecute(result);
	}
}
