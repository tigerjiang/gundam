package org.booster.gundam.bean;

import org.booster.gundam.GundamApplication;
import org.booster.sdk.util.CommonTools;

public class GundamInfo {
    private static GundamInfo instance;
    
    private String resourceFileSavePath="";
    
    private long maxStorageSize = 100 * 1024 * 1024;
    /**
     * 构造方法，私有化以确保单例
     * @param 
     */
    private GundamInfo() {
    }

    /**
     * 获取实例，确保单例
     * @param
     * @return
     */
    public static GundamInfo getInstance() {
        if (instance == null) {
            synchronized (GundamInfo.class) {
                if (instance == null) {
                    instance = new GundamInfo();
                }
            }
        } else {
        }
        return instance;
    }

    public String getResourceFileSavePath() {
        if(CommonTools.isEmpty(resourceFileSavePath)){
            resourceFileSavePath = GundamApplication.mApp.getFilesDir().getAbsolutePath();
        }
        return resourceFileSavePath;
    }

    public void setResourceFileSavePath(String resourceFileSavePath) {
        this.resourceFileSavePath = resourceFileSavePath;
    }

    public long getMaxStorageSize() {
        return maxStorageSize;
    }

    public void setMaxStorageSize(long maxStorageSize) {
        this.maxStorageSize = maxStorageSize;
    }
    
    
}
