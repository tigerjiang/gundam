package org.booster.sdk.logging;

import org.booster.sdk.logging.LogConfigration.OnChangeListener;


/**
 * 
 * @Description:日志打印类，通过日志配置完成日志打印相关功能
 * @author Merry.Zhao
 * @date 2014-3-13 下午7:00:18
 */
public class HiLog {

    private static Logger log = null;
    private static LogConfigration config = new LogConfigration();
    // 为了保证抽象类中单例初始化所使用的对象
    private static Object lock = new Object();

    public static void setLogConfig(LogConfigration logConfig) {
        config.setLogLevel(logConfig.getLogLevel());
        config.setLoggerName(logConfig.getLoggerName());
        config.setLogFilePath(logConfig.getLogFilePath());
        init();
    }

    private static void init() {
        if (log == null) {
            synchronized (lock) {
                if (log == null) {
                    config.setLogInvokeDepth(2);
                    log = LogFactory.getLogger(config);
                    config.setOnChangeListener(new OnChangeListener() {

                        @Override
                        public void onChange() {
                            log.setLogLevel(config.getLogLevel());
                            log.setInvokeDepth(config.getLogInvokeDepth());
                            log.setLogFilePath(config.getLogFilePath());

                        }

                    });

                }
            }
        }
    }

    public static LogConfigration getConfig() {
        return config;
    }

    public static void v(String str, int depth) {
        init();
        log.v(str, depth);
    }

    public static void v(String tag, String str, int depth) {
        init();
        log.v(tag, str, depth);
    }

    public static void v(String str) {
        init();
        log.v(str);
    }

    public static void v(String tag, String str) {
        init();
        log.v(tag, str);
    }

    public static void d(String str, int depth) {
        init();
        log.d(str, depth);
    }

    public static void d(String tag, String str, int depth) {
        init();
        log.d(tag, str, depth);
    }

    public static void d(String str) {
        init();
        log.d(str);
    }

    public static void d(String tag, String str) {
        init();
        log.d(tag, str);
    }

    public static void i(String str, int depth) {
        init();
        log.i(str, depth);
    }

    public static void i(String tag, String str, int depth) {
        init();
        log.i(tag, str, depth);
    }

    public static void i(String str) {
        init();
        log.i(str);
    }

    public static void i(String tag, String str) {
        init();
        log.i(tag, str);
    }

    public static void w(String str, int depth) {
        init();
        log.w(str, depth);
    }

    public static void w(String tag, String str, int depth) {
        init();
        log.w(tag, str, depth);
    }

    public static void w(String str) {
        init();
        log.w(str);
    }

    public static void w(String tag, String str) {
        init();
        log.w(tag, str);
    }

    public static void e(String str, int depth) {
        init();
        log.e(str, depth);
    }

    public static void e(String tag, String str, int depth) {
        init();
        log.e(tag, str, depth);
    }

    public static void e(String str) {
        init();
        log.e(str);
    }

    public static void e(String tag, String str) {
        init();
        log.e(tag, str);
    }

    public static void f(String str, int depth) {
        init();
        log.f(str, depth);
    }

    public static void f(String tag, String str, int depth) {
        init();
        log.f(tag, str, depth);
    }

    public static void f(String str) {
        init();
        log.f(str);
    }

    public static void f(String tag, String str) {
        init();
        log.f(tag, str);
    }

    public static void println(String str, int depth) {
        init();
        log.println(str, depth);
    }

    public static void println(String str) {
        init();
        log.println(str);
    }
}
