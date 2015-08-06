/**
 * 
 */
package org.booster.sdk.logging;

import org.booster.sdk.util.CommonTools;

/**
 * @Description Log接口，实现了该接口的子类可用于日志信息的输出</br> 子类的设计可以是单例，也可以是多例，视具体应用场景而定</br>
 * @author Merry.Zhao
 * @date 2014-3-13 下午6:49:01
 */
public abstract class Logger {
    // 定义日志级别
    /**
     * 允许所有级别的日志信息输出
     */
    public final static int LOGLEVEL_ALL = 0;
    /**
     * 重要性最低的级别
     */
    public final static int LOGLEVEL_VERBOSE = 1;
    /**
     * 调试级别
     */
    public final static int LOGLEVEL_DEBUG = 2;
    /**
     * 提示信息级别
     */
    public final static int LOGLEVEL_INFO = 3;
    /**
     * 警告级别
     */
    public final static int LOGLEVEL_WARN = 4;
    /**
     * 错误级别
     */
    public final static int LOGLEVEL_ERROR = 5;
    /**
     * 致命错误级别
     */
    public final static int LOGLEVEL_FETAL = 6;
    /**
     * 关闭所有级别的日志信息输出
     */
    public final static int LOGLEVEL_OFF = 7;

    /**
     * 定义当前的日志级别，如果需要完全关闭日志，可将此设置为LOGLEVEL_OFF</br> 默认设置为LOGLEVEL_WARN，即只打印警告、错误、致命错误日志
     */
    public int logLevel = LOGLEVEL_WARN;

    /**
     * 定义调用日志输出的深度，通常不需设置
     */
    public int invokeDepth = 1;

    /**
     * 日志文件路径,只有当日志需要保存到文件中时才生效
     */
    public String logFilePath = "";
    
    public static final String LOGGERIMPL_ANDROID = "org.booster.sdk.logging.impl.AndroidLoggerImpl";
    
    public static final String LOGGERIMPL_FILE = "org.booster.sdk.logging.impl.FileLoggerImpl";
    
    public static final String LOGGERIMPL_DEFAULT = "org.booster.sdk.logging.impl.DefaultLoggerImpl";
    
    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    /**
     * 设置日志输出级别</br>
     * @param logLevel ：期望的日志输出级别</br>
     */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * 定义调用日志输出的深度，即期望日志输出信息中包含了哪一层类的具体信息</br> 默认是1，即输出距离调用Log实现类最近的调用类信息</br> 通常不需设置，除非你对调用类信息有明确需求并且对调用层次的判断有把握</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public void setInvokeDepth(int depth) {
        this.invokeDepth = depth;
    }

    /**
     * 输出VERBOSE级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void v(String text, int depth) {
        if (logLevel <= LOGLEVEL_VERBOSE) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                verbose(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出VERBOSE级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 日志标记</br>
     * @param text:日志文本信息</br>
     * @param depth：日志记录深度</br>
     */
    public synchronized void v(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_VERBOSE) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                    .append(text);
                verbose(tag, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出VERBOSE级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth :
     */
    public synchronized void v(String text) {
        if (logLevel <= LOGLEVEL_VERBOSE) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                verbose(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出VERBOSE级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void v(String tag, String text) {
        if (logLevel <= LOGLEVEL_VERBOSE) {
            if (CommonTools.isEmpty(tag)) {
                v(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    verbose(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }

    }

    /**
     * VERBOSE级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void verbose(String tag, String text);

    /**
     * 输出DEBUG级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void d(String text, int depth) {
        if (logLevel <= LOGLEVEL_DEBUG) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                debug(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出DEBUG级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void d(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_DEBUG) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth + depth].getMethodName();
                    int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    debug(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * 输出DEBUG级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void d(String text) {
        if (logLevel <= LOGLEVEL_DEBUG) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                debug(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出DEBUG级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void d(String tag, String text) {
        if (logLevel <= LOGLEVEL_DEBUG) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    debug(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * DEBUG级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void debug(String tag, String text);

    /**
     * 输出INFO级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void i(String text, int depth) {
        if (logLevel <= LOGLEVEL_INFO) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();

                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                info(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出INFO级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void i(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_INFO) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth + depth].getMethodName();
                    int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    info(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * 输出INFO级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void i(String text) {
        if (logLevel <= LOGLEVEL_INFO) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                info(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出INFO级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void i(String tag, String text) {
        if (logLevel <= LOGLEVEL_INFO) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    info(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * INFO级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void info(String tag, String text);

    /**
     * 输出WARN级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void w(String text, int depth) {
        if (logLevel <= LOGLEVEL_WARN) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                warn(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出WARN级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void w(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_WARN) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth + depth].getMethodName();
                    int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    warn(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * 输出WARN级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void w(String text) {
        if (logLevel <= LOGLEVEL_WARN) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                warn(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出WARN级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void w(String tag, String text) {
        if (logLevel <= LOGLEVEL_WARN) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    warn(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * WARN级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void warn(String tag, String text);

    /**
     * 输出ERROR级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void e(String text, int depth) {
        if (logLevel <= LOGLEVEL_ERROR) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                error(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出ERROR级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void e(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_ERROR) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth + depth].getMethodName();
                    int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    error(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * 输出ERROR级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void e(String text) {
        if (logLevel <= LOGLEVEL_ERROR) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                error(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出ERROR级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void e(String tag, String text) {
        if (logLevel <= LOGLEVEL_ERROR) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    error(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * ERROR级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void error(String tag, String text);

    /**
     * 输出FETAL级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void f(String text, int depth) {
        if (logLevel <= LOGLEVEL_FETAL) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth + depth].getMethodName();
                int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                fetal(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出FETAL级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void f(String tag, String text, int depth) {
        if (logLevel <= LOGLEVEL_FETAL) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth + depth].getMethodName();
                    int lineNumber = stacks[invokeDepth + depth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    fetal(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * 输出FETAL级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void f(String text) {
        if (logLevel <= LOGLEVEL_FETAL) {
            try {
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                String className = stacks[invokeDepth].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1);
                String methodName = stacks[invokeDepth].getMethodName();
                int lineNumber = stacks[invokeDepth].getLineNumber();
                StringBuffer sb = new StringBuffer();
                sb.append(methodName).append(" ").append(lineNumber).append(" : ").append(text);
                fetal(className, sb.toString());
                sb = null;
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(text);
            }

        }
    }

    /**
     * 输出FETAL级别的日志信息</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    public synchronized void f(String tag, String text) {
        if (logLevel <= LOGLEVEL_FETAL) {
            if (CommonTools.isEmpty(tag)) {
                d(text);
            } else {
                try {
                    StackTraceElement[] stacks = new Throwable().getStackTrace();
                    String className = stacks[invokeDepth].getClassName();
                    className = className.substring(className.lastIndexOf(".") + 1);
                    String methodName = stacks[invokeDepth].getMethodName();
                    int lineNumber = stacks[invokeDepth].getLineNumber();
                    StringBuffer sb = new StringBuffer();
                    sb.append(className).append(" ").append(methodName).append(" ").append(lineNumber).append(" : ")
                        .append(text);
                    fetal(tag, sb.toString());
                    sb = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(text);
                }
            }

        }
    }

    /**
     * FETAL级别日志输出的具体实现方式</br>
     * @param tag : 该条日志信息的标志信息</br>
     * @param text : 日志信息</br>
     */
    protected abstract void fetal(String tag, String text);

    /**
     * 输出到控制台的日志打印方法</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     * @param depth ：调用日志输出类深度</br>
     */
    public synchronized void println(String text, int depth) {
        try {
            StackTraceElement[] stacks = new Throwable().getStackTrace();
            String className = stacks[invokeDepth + depth].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            String methodName = stacks[invokeDepth + depth].getMethodName();
            int lineNumber = stacks[invokeDepth + depth].getLineNumber();
            StringBuffer sb = new StringBuffer();
            sb.append(className).append("  ").append(methodName).append(" ").append(lineNumber).append(" : ")
                .append(text);
            System.out.println(sb.toString());
            sb = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(text);
        }
    }

    /**
     * 输出到控制台的日志打印方法</br> 日志信息格式为 "调用类名 调用方法名 调用行号 日志信息"</br> 其中调用类名、调用方法、调用行号的信息依赖于设置的日志输出深度</br>
     * @param text : 日志信息</br>
     */
    public synchronized void println(String text) {
        try {
            StackTraceElement[] stacks = new Throwable().getStackTrace();
            String className = stacks[invokeDepth].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
            String methodName = stacks[invokeDepth].getMethodName();
            int lineNumber = stacks[invokeDepth].getLineNumber();
            StringBuffer sb = new StringBuffer();
            sb.append(className).append("  ").append(methodName).append(" ").append(lineNumber).append(" : ")
                .append(text);
            System.out.println(sb.toString());
            sb = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(text);
        }
    }
}
