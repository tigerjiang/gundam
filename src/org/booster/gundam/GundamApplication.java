package org.booster.gundam;

import java.io.File;
import java.util.HashMap;

import org.booster.gundam.bean.GundamInfo;
import org.booster.gundam.broadcast.NetChangeReceiver;
import org.booster.gundam.service.GundamServiceHandler;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.logging.LogConfigration;
import org.booster.sdk.logging.Logger;
import org.booster.sdk.util.FileUtil;




import android.app.Application;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;

public class GundamApplication extends Application {
    /**
     * 自引用
     */
    public static GundamApplication mApp;
    /**
     * 记录每项操作的结果
     * */
    private volatile HashMap<String,String> results;
    
    private volatile boolean networkFlag = false;// 网络连通标志
    private NetChangeReceiver mNetChage = new NetChangeReceiver();//网络监听
    
    @Override
    public void onCreate() {
        super.onCreate();
       
        LogConfigration logConfig = new LogConfigration();
        logConfig.setLogLevel(Logger.LOGLEVEL_ALL);
        logConfig.setLoggerName(Logger.LOGGERIMPL_ANDROID);
//        logConfig.setLoggerName(Logger.LOGGERIMPL_FILE);
//        logConfig.setLogFilePath(this.getFilesDir().getAbsolutePath()+"/booster.log");
        HiLog.setLogConfig(logConfig);
        HiLog.d("ok GundamApplication created!!!");
        mApp = this;
        registerBroadcastReceiver();
        checkDirs();
        GundamServiceHandler.getInstance(mApp).init();
     }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }
    
    /**
     * 检查Gundam所需要的路径是否已创建，若无，则需要创建
     */
    private void checkDirs(){
        String fileSavePath = GundamInfo.getInstance().getResourceFileSavePath();
        File fileSaveDir = new File(fileSavePath);
        if(!fileSaveDir.exists()){
            fileSaveDir.mkdirs();
            FileUtil.changeMode(fileSavePath,"chmod -R 777 ");
        }
    }
    
    private void registerBroadcastReceiver(){
      //注册网络状态变更监听
        IntentFilter mNetFilter = new IntentFilter();
        mNetFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetChage, mNetFilter);
    }
    
    private void unRegisterBroadcastReceiver(){
        unregisterReceiver(mNetChage);
    }

    public boolean isNetworkFlag() {
        return networkFlag;
    }

    public void setNetworkFlag(boolean networkFlag) {
        this.networkFlag = networkFlag;
    }

    public HashMap<String, String> getResults() {
        return results;
    }

    public void setResults(HashMap<String, String> results) {
        this.results = results;
    }

    @Override
    public void onTerminate() {
        unRegisterBroadcastReceiver();
        super.onTerminate();
    }

}
