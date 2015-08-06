package org.booster.gundam.broadcast;

import java.util.HashMap;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.service.GundamServiceHandler;
import org.booster.sdk.logging.HiLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class NetChangeReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = con.getActiveNetworkInfo();
		HiLog.i(intent.getAction());
        
		if(networkInfo == null || !networkInfo.isConnected()){
			HiLog.d("network is disconnect ");
			//设置全局网络连通标志为否
			GundamApplication.mApp.setNetworkFlag(false);
		}else if(networkInfo.isConnected()){
			HiLog.d("network is connect ");
			//设置全局网络连通标志为是，调用自升级接口	
			GundamApplication.mApp.setNetworkFlag(true);
			
			new Thread() {
                public void run() {
                    GundamServiceHandler.getInstance(GundamApplication.mApp).work();
                }
            }.start();
		}
	}

}
