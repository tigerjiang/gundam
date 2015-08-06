package org.booster.sdk.logging.impl;



import org.booster.sdk.logging.Logger;

import android.util.Log;

/**
 * 
 * @Description: 日志打印工具接口的Android实现
 * @author Merry.Zhao
 * @date 2014-3-13 下午7:02:23
 */
public class AndroidLoggerImpl extends Logger {

    
    
    @Override
    protected void verbose(String tag, String text) {
	Log.v(tag,text);
    }

    @Override
    protected void debug(String tag, String text) {
	Log.d(tag,text);

    }

    @Override
    protected void info(String tag, String text) {
	Log.i(tag,text);

    }

    @Override
    protected void warn(String tag, String text) {
	Log.w(tag,text);

    }

    @Override
    protected void error(String tag, String text) {
	Log.e(tag,text);

    }

    @Override
    protected void fetal(String tag, String text) {
	Log.e(tag,text);

    }

}
