package org.booster.gundam.activity;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.R;
import org.booster.gundam.service.GundamServiceHandler;
import org.booster.gundam.util.GundamConst;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.util.SingleThreadPool;
import org.booster.sdk.util.Task;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HiLog.d("merry","log ok !!!");
        HiLog.d("log done !!!");
        button1 = (Button)this.findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
               SingleThreadPool.addTask(new Task(){

                @Override
                public void work() {
                    GundamServiceHandler.getInstance(GundamApplication.mApp).getADResourceInfo(GundamConst.RESOURCECODE_BOOTUPAD, GundamConst.RESOURCETYPE_PIC, null);
                    GundamServiceHandler.getInstance(GundamApplication.mApp).getADResourceInfo(GundamConst.RESOURCECODE_VOLUMEAD, GundamConst.RESOURCETYPE_PIC, null);
                    GundamServiceHandler.getInstance(GundamApplication.mApp).getLauncherResourceInfo("",null);
                    
                }
                   
               });
                
            }
            
        });
    }
}
