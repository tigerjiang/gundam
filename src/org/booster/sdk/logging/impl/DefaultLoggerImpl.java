/**
 * 
 */
package org.booster.sdk.logging.impl;

import org.booster.sdk.logging.Logger;


/**
 * 
 * @Description: 实现了Logger接口的类，用于输出日志信息
 * @author Merry.Zhao
 * @date 2014-3-13 下午7:02:03
 */
public class DefaultLoggerImpl extends Logger {

    @Override
    protected synchronized void verbose(String tag, String text) {
        System.out.println("[Verbose] " + tag + " " + text);

    }

    @Override
    protected synchronized void debug(String tag, String text) {
        System.out.println("[Debug] " + tag + " " + text);

    }

    @Override
    protected synchronized void info(String tag, String text) {
        System.out.println("[Info] " + tag + " " + text);

    }

    @Override
    protected synchronized void warn(String tag, String text) {
        System.out.println("[Warn] " + tag + " " + text);

    }

    @Override
    protected synchronized void error(String tag, String text) {
        System.out.println("[Error] " + tag + " " + text);

    }

    @Override
    protected synchronized void fetal(String tag, String text) {
        System.out.println("[Fetal] " + tag + " " + text);

    }

}
