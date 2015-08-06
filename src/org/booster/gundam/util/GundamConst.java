package org.booster.gundam.util;

public class GundamConst {
    /**
     * 资源代码：开机广告
     */
    public static final int RESOURCECODE_BOOTUPAD= 1;
    /**
     * 资源代码：音量广告
     */
    public static final int RESOURCECODE_VOLUMEAD = 2;
    /**
     * 资源代码：应用启动页面广告
     */
    public static final int RESOURCECODE_APPSTARTUPAD = 3;
    /**
     * 资源代码：屏保广告
     */
    public static final int RESOURCECODE_SCREENOFFAD = 4;
    /**
     * 资源代码：屏显广告
     */
    public static final int RESOURCECODE_SCREENONAD = 5;
    /**
     * 资源代码：launcher图片
     */
    public static final int RESOURCECODE_LAUNCHER = 888888;
    /**
     * 资源代码：整机升级包
     */
    public static final int RESOURCECODE_ROM = 999999;
    
    
    /**
     * 资源类型：图片
     */
    public static final int RESOURCETYPE_PIC = 1;
    /**
     * 资源类型：视频
     */
    public static final int RESOURCETYPE_VIDEO = 2;
    
    /**
     * cache中广告资源key前缀
     */
    public static final String ADCACHE_PREFIX = "ad_";
    
    public static final String LAUNCHER_PREFIX="launcher_";
   
    public static final String BOOTUPAD_PATH="/data/local/misc/third_party_bootanimation.zip";
    
    public static final String BOOTUPAD_DETAILS="/data/local/misc/third_party_bootanimation.xml";
    
    public static final String VOLUMEAD_PATH="/data/local/misc/volume_ad";
    
    public static final String VOLUMEAD_DETAILS="/data/local/misc/volume_ad.xml";
    
    public static final String LAUCHERRESOURCE_PATH="/data/local/misc/launcher.gz";
    
    public static final String BASIC_FILE_DIR  = "/data/local/misc/";
    public static final String LAUCHER_DETAILS="/data/local/misc/launcher.xml";
 
    /**
     * 操作状态：未进行
     */
    public static final String OPSTATUS_NO = "0";
    /**
     * 操作状态：进行中
     */
    public static final String OPSTATUS_DOING = "1";
    /**
     * 操作状态：完成
     */
    public static final String OPSTATUS_DONE = "2";
    
    /**
     * Launcher应用的包名
     */
    public static final String LAUNCHERAPP_PACKAGENAME = "com.android.launcher";
//    public static final String LAUNCHERAPP_PACKAGENAME = "org.booster.gundam";
}
