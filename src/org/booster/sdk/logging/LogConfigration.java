package org.booster.sdk.logging;

/**
 * @Description: 日志配置类，其中定义了日志工具实现类，日志打印级别等配置
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:53:13
 */
public class LogConfigration {

    /**
     * 要配置的日志级别
     */
    private int logLevel;
    /**
     * 日志打印接口的实现类
     */
    private String loggerName;
    /**
     * 日志打印深度
     */
    private int logInvokeDepth = 1;
    
    /**
     * 日志文件绝对路径，只有当设置为记录日志到文件时才有效
     */
    private String logFilePath = "";

    private OnChangeListener mOnChangeListener = null;
    
    
    

    /**
     * 获得日志打印级别
     * @return
     */
    public int getLogLevel() {
        return logLevel;
    }

    /**
     * 设置日志打印级别
     * @param logLevel
     */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange();
        }
    }

    /**
     * @return the logger
     */
    public String getLoggerName() {
        return loggerName;
    }

    /**
     * @param logger the logger to set
     */
    public void setLoggerName(String logger) {
        this.loggerName = logger;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange();
        }
    }

    public LogConfigration() {
        this.logLevel = Logger.LOGLEVEL_DEBUG;
        this.loggerName = Logger.LOGGERIMPL_ANDROID;
        this.logInvokeDepth = 1;
    }

    /**
     * 获得日志打印深度
     * @return
     */
    public int getLogInvokeDepth() {
        return logInvokeDepth;
    }

    /**
     * 设置日志打印深度，一般情况不需更改
     * @param logInvokeDepth
     */
    public void setLogInvokeDepth(int logInvokeDepth) {
        this.logInvokeDepth = logInvokeDepth;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange();
        }
    }

    
    
    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
        if (mOnChangeListener != null) {
            mOnChangeListener.onChange();
        }
    }



    /**
     * @Description: 日志配置改变事件监听接口，当日志配置改变时（打印级别、深度等)，将触发其中的onChange()方法
     * @author Merry.Zhao
     * @date 2014-3-13 下午6:57:33
     */
    public interface OnChangeListener {
        void onChange();
    }

    /**
     * 设置日志配置改变事件监听接口实现
     * @param mOnChangeListener
     */
    public void setOnChangeListener(OnChangeListener mOnChangeListener) {
        this.mOnChangeListener = mOnChangeListener;
    }
}
