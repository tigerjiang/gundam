package org.booster.gundam.broadcast;



import org.booster.gundam.GundamApplication;
import org.booster.gundam.service.GundamServiceHandler;
import org.booster.gundam.util.GundamConst;
import org.booster.sdk.logging.HiLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		HiLog.d("received boot complete.");
		HiLog.i("BootCompleteReceiver", "received boot complete.");
		/*GundamServiceHandler.getInstance(GundamApplication.mApp).getADResourceInfo(GundamConst.RESOURCECODE_BOOTUPAD, GundamConst.RESOURCETYPE_PIC, null);
        GundamServiceHandler.getInstance(GundamApplication.mApp).getADResourceInfo(GundamConst.RESOURCECODE_VOLUMEAD, GundamConst.RESOURCETYPE_PIC, null);
        GundamServiceHandler.getInstance(GundamApplication.mApp).getLauncherResourceInfo("1.0",null);*/
//		Intent mIntent = new Intent();
//		mIntent.setAction("com.hisense.service.ad.service.ADServiceHandler");//adservicehandler 启动服务消息
//		context.startService(mIntent);
//		
//		Intent startIntent = new Intent(context,ADServiceApplication.class);
//		startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(startIntent);
	}

}
