/**
 * 
 */
package org.booster.sdk.logging;

/**
 * @Description: 日志工厂类，可以产生一个具体的日志工具实例
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:50:34
 */
public class LogFactory {

    private static Logger logger;

    private static LogConfigration logConfig;

    /**
     * 根据日志配置获得日志工具实例
     * @param config
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Logger getLogger(LogConfigration config) {
        if (logger == null) {
            synchronized (LogFactory.class) {
                if (logger == null) {
                    try {
                        Class c = Class.forName(config.getLoggerName());
                        logger = (Logger) c.newInstance();
                        logger.setLogLevel(config.getLogLevel());
                        logger.setInvokeDepth(config.getLogInvokeDepth());
                        logger.setLogFilePath(config.getLogFilePath());
                        logConfig = config;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return logger;
    }

    /**
     * 获得默认的日志工具实例
     * @return
     */
    public static Logger getLogger() {
        if (logConfig == null) {
            logConfig = new LogConfigration();
        }
        return getLogger(logConfig);
    }

    /**
     * 初始化，日志配置
     * @param config
     */
    public static void init(LogConfigration config) {
        logConfig = config;
    }

}
