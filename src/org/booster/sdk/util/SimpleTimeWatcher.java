package org.booster.sdk.util;

import org.booster.sdk.logging.HiLog;



/**
 * @Description: 一个代码片段执行耗时统计工具类，提供简单的执行时长统计功能
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:34:20
 */
public class SimpleTimeWatcher {

    private long startPoint;
    private long endPoint;
    private String tag;
    private boolean quiet = false;
    private final static String TAG = "timer";

    public SimpleTimeWatcher()
    {

    }

    /**
     * 带参构造
     * @param tagname 统计别名
     */
    public SimpleTimeWatcher(String tagname)
    {
        tag = tagname;
    }

    /**
     * 触发开始统计的方法，该方法必需被调用在end()方法之前
     */
    public void start()
    {
        startPoint = System.currentTimeMillis();
        HiLog.d(TAG, tag + " start...." + startPoint);
    }

    /**
     * 触发结束统计的方法，该方法必需被调用在start()方法之后
     */
    public void end()
    {
        endPoint = System.currentTimeMillis();
    }

    /**
     * 重置统计状态，当结束了一次统计之后，再开始下一次统计之前，需要调用该方法
     */
    public void reset()
    {
        startPoint = 0;
        endPoint = 0;
    }

    /**
     * 打印统计报告，并重置，如果设置了沉默选项，则不打印任何信息
     */
    public void report()
    {
        if (quiet)
            return;
        HiLog.d(TAG, tag + " Cost(millisecond) : " + (endPoint - startPoint));
        reset();
    }

    /**
     * 获得本次代码片段执行耗时的统计结果，该方法必需在report()方法之前调用。
     * @return
     */
    public long result()
    {
        return endPoint - startPoint;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * 设置沉默选项，该方法调用后，调用report()将不会打印任何统计结果
     * @param quiet
     */
    public void quiet()
    {
        setQuiet(true);
    }

    /**
     * 设置活跃选项，该方法调用后，调用report()将会打印统计结果
     * @param quiet
     */
    public void active()
    {
        setQuiet(false);
    }
}
