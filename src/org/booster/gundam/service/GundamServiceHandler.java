package org.booster.gundam.service;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.aidl.ResourceInfo;
import org.booster.gundam.bean.DownloadInfo;
import org.booster.gundam.dao.GundamDaoHandler;
import org.booster.gundam.util.AndroidUtil;
import org.booster.gundam.util.GundamConst;
import org.booster.sdk.bean.ReportDataReply;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.util.CommonTools;
import org.booster.sdk.util.FileUtil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.preference.PreferenceManager;



public class GundamServiceHandler {
    /**
     * Application的实例
     */
    private GundamApplication application;
    /**
     * 自引用
     */
    private static GundamServiceHandler instance;
    /**
     * Dao实例
     */
    private GundamDaoHandler dao;
    
    
    

    /**
     * Service构造方法，私有化以确保单例
     * @param 
     */
    private GundamServiceHandler(Application application) {
        this.application =(GundamApplication)application;
    }

    /**
     * 获取Service的服务实例，确保单例
     * @param application
     * @return
     */
    public static GundamServiceHandler getInstance(Application application) {
        if (instance == null) {
            synchronized (GundamServiceHandler.class) {
                if (instance == null) {
                    instance = new GundamServiceHandler(application);
                    instance.dao =
                        GundamDaoHandler
                            .getInstance();
                    
                }
            }
        } else {
            instance.application = (GundamApplication)application;
        }
        if (instance.dao == null) {
            instance.dao =
                GundamDaoHandler.getInstance();
        }
        return instance;
    }

    /**
     * 获取广告资源信息
     * @param boardId: 广告码
     * @param adType：广告类型
     * @param packageName：调用的包名
     * @return
     */
     public synchronized ResourceInfo getADResourceInfo(int boardId,int adType,String packageName){
       
        String key = GundamConst.ADCACHE_PREFIX+String.valueOf(boardId);
        GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_DOING);
        ResourceInfo resourceInfo = CacheManager.getInstance().getResourceInfo(key);
        if(resourceInfo == null){
            resourceInfo = dao.getADInfo(boardId,adType);
            if (resourceInfo ==null){
                GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_NO);
                return null;
            }
            
            ResourceInfo localeResourceInfo = dao.getLocalResourceInfo(key);
            
          //TODO 比较新获取广告信息与SP中储存的上一次的广告信息，若一致，则直接返回上一次的信息，不再下载。
            //TODO 若不一致，则开启下载流程，同时更新SP中的存储信息，下载过程中，该广告信息处于不可用状态，直到下载完成后，置为可用。
            if(resourceInfo.equals(localeResourceInfo) && (new File(resourceInfo.getResourceTempPath()).exists()) ){
                HiLog.d("ad : "+boardId+" not change after last update not need to download");
                CacheManager.getInstance().putResourceInfo(key, localeResourceInfo);
                //目标文件不存在
                if(!new File(resourceInfo.getResourcePath()).exists()){
                    postExecuteResource(key);
                }
                return localeResourceInfo;
            }else{
                //开启下载任务
                ResourceDownloadManager downloadManager = ResourceDownloadManager.getInstance(application);
                DownloadInfo downloadInfo = new DownloadInfo();
                downloadInfo.setResourceURL(resourceInfo.getResourceURL());
                //downloadInfo.setResourceTempPath(resourceInfo.getResourceTempPath());
//                downloadInfo.setResourcePath(resourceInfo.getResourcePath());
                downloadInfo.setResourcePath(resourceInfo.getResourceTempPath());
                downloadInfo.setKey(key);
                downloadManager.sendDownloadRequest(downloadInfo);
                
                dao.saveResourceInfo(key, resourceInfo);
                
                CacheManager.getInstance().putResourceInfo(key, resourceInfo);
                
                if(!resourceInfo.equals(localeResourceInfo)){
                    CacheManager.getInstance().putResourceInfo(key+"_old", localeResourceInfo);
                }
            }
            
            
            
            
        }
        return resourceInfo;
        
    }
    
    
    
    
    
    
     public synchronized ResourceInfo getLauncherResourceInfo(String launcherVersion,String packageName){
        String key = GundamConst.LAUNCHER_PREFIX+GundamConst.RESOURCECODE_LAUNCHER;
        GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_DOING);
        ResourceInfo resourceInfo = CacheManager.getInstance().getResourceInfo(key);
        String version = launcherVersion;
        if(CommonTools.isEmpty(version)){
            version = getLauncherVersionInfo();
        }
        if(resourceInfo == null){
            resourceInfo = dao.getLauncherInfo(version);
            if (resourceInfo ==null){
                GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_NO);
                return null;
            }
            ResourceInfo localeResourceInfo = dao.getLocalResourceInfo(key);
            
          //TODO 比较新获取Launcher资源信息与SP中储存的上一次的launcher资源信息，若一致，则直接返回上一次的信息，不再下载。
            //TODO 若不一致，则开启下载流程，同时更新SP中的存储信息，下载过程中，该资源信息处于不可用状态，直到下载完成后，置为可用。
            if(resourceInfo.equals(localeResourceInfo) && (new File(resourceInfo.getResourceTempPath()).exists()) ){
                HiLog.d("launcher : "+version+" not change after last update not need to download");
                CacheManager.getInstance().putResourceInfo(key, localeResourceInfo);
                //目标文件不存在
                if(!new File(resourceInfo.getResourcePath()).exists()){
                    postExecuteResource(key);
                }
                return localeResourceInfo;
            }else{
                
                //开启下载任务
                ResourceDownloadManager downloadManager = ResourceDownloadManager.getInstance(application);
                DownloadInfo downloadInfo = new DownloadInfo();
                downloadInfo.setResourceURL(resourceInfo.getResourceURL());
                //downloadInfo.setResourceTempPath(resourceInfo.getResourceTempPath());
//                downloadInfo.setResourcePath(resourceInfo.getResourcePath());
                downloadInfo.setResourcePath(resourceInfo.getResourceTempPath());
                downloadInfo.setKey(key);
                downloadManager.sendDownloadRequest(downloadInfo);
                
                dao.saveResourceInfo(key, resourceInfo);
                
                CacheManager.getInstance().putResourceInfo(key, resourceInfo);
                if(!resourceInfo.equals(localeResourceInfo)){
                    CacheManager.getInstance().putResourceInfo(key+"_old", localeResourceInfo);
                }
                
            }
            
            
            
            
        }
        return resourceInfo;
        
    }
    
     /**
      * 获得Launcher的版本信息
      * @return
      */
    private String getLauncherVersionInfo() {
        String version = "1.0";
        PackageInfo packageInfo = AndroidUtil.getPackageInfo(application, GundamConst.LAUNCHERAPP_PACKAGENAME);
        if(packageInfo!=null){
            HiLog.d("launcher version name is : "+packageInfo.versionName);
            version =  packageInfo.versionName;
        }
        return version;
    }

    public ResourceInfo getResourceInfo(int resourceCode,int resourceType, String packageName) {
        ResourceInfo ri = new ResourceInfo();
        ri.setResourceId(CommonTools.getUTCTime());
        ri.setResourceCode("merry");
        ri.setResourcePath("filepath.....");
        ri.setResourceType(1);
        HiLog.d("go into getResourceContent  "+ packageName);
        HiLog.d("resource Id is : "+ri.getResourceId());
        HiLog.d("resource Code is : "+ri.getResourceCode());
        HiLog.d("resource Path is : "+ri.getResourcePath());
        HiLog.d("resource type is : "+ri.getResourceType());
        return ri;
    }
    
    public ReportDataReply reportErrorLog(String businessType,String errorInfo,String url,String description){
        return dao.reportErrorLog(businessType, errorInfo, url, description);
    }

    /**
     * 资源文件下载完成后的处理，更新sp数据，更新cache，可能的将资源文件拷贝到目标位置等
     * @param key
     */
    public void postExecuteResource(String key) {
        ResourceInfo resourceInfo = CacheManager.getInstance().getResourceInfo(key);
        if(resourceInfo == null){
            return;
        }
        resourceInfo.setEnabled(1);
        CacheManager.getInstance().putResourceInfo(key, resourceInfo);
        dao.saveResourceInfo(key, resourceInfo);
        //删除旧的文件，要删除的旧文件仅限于/data/data/org.booster.gundam/files目录中的原始下载文件
        ResourceInfo oldResourceInfo = CacheManager.getInstance().getResourceInfo(key+"_old");
        if(oldResourceInfo != null && !CommonTools.isEmpty(oldResourceInfo.getResourceTempPath())){
            File oldResourceFile = new File(oldResourceInfo.getResourceTempPath());
            if(oldResourceFile.exists()){
                if(oldResourceFile.delete()){
                    HiLog.d("to delete file : "+key+oldResourceInfo.getResourceTempPath());
                    CacheManager.getInstance().removeResourceInfo(key+"_old");
                }
            }
           
        }
        
        //如果下载的文件所在目录与资源文件要求的目录不一致，则执行拷贝动作。资源文件和XML文件都拷贝
        if(!resourceInfo.getResourcePath().equals(resourceInfo.getResourceTempPath())){
            String newPath = resourceInfo.getResourcePath().substring(0,resourceInfo.getResourcePath().lastIndexOf("/"));
            String newFileName = resourceInfo.getResourcePath().substring(resourceInfo.getResourcePath().lastIndexOf("/")+1);
//            FileUtil.moveFile(resourceInfo.getResourceTempPath(), newPath, newFileName);
            FileUtil.copyFile(resourceInfo.getResourceTempPath(), newPath, newFileName);
            String resourceDetailFileName="";
            String adFilePath = "";
            if(key.equals(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_BOOTUPAD)){
                resourceDetailFileName=GundamConst.BOOTUPAD_DETAILS;
                adFilePath = GundamConst.BOOTUPAD_PATH;
            }else if(key.equals(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_VOLUMEAD)){
                resourceDetailFileName=GundamConst.VOLUMEAD_DETAILS;
                adFilePath = GundamConst.VOLUMEAD_PATH;
            } else if(key.startsWith(GundamConst.LAUNCHER_PREFIX)){
                resourceDetailFileName=GundamConst.LAUCHER_DETAILS;
                
            }
            if(!CommonTools.isEmpty(resourceDetailFileName)){
                try {
                    FileUtil.saveContentToFile(resourceDetailFileName,resourceInfo.getResourceDetails());
                    FileUtil.changeMode(adFilePath, "chmod 777");
                    GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_DONE);
                    return;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_NO);
                    return;
                }
            }else{
                GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_NO);
                return;
            }
        }else{
            GundamApplication.mApp.getResults().put(key,GundamConst.OPSTATUS_DONE);
        }
    }

    /**
     * 任务调度方法，执行所有Gundam需要执行的业务动作
     */
    public void work() {
       /**
        * 遍历所有任务，若任务尚未开始，则执行任务
        */
       for(Entry<String,String> entry:GundamApplication.mApp.getResults().entrySet()){
           String key = entry.getKey();
           String value = entry.getValue();
            if(value.equals(GundamConst.OPSTATUS_NO)){
                if(key.equals(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_BOOTUPAD)){
                    getADResourceInfo(GundamConst.RESOURCECODE_BOOTUPAD, GundamConst.RESOURCETYPE_PIC, null);
                }else if(key.equals(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_VOLUMEAD)){
                    getADResourceInfo(GundamConst.RESOURCECODE_VOLUMEAD, GundamConst.RESOURCETYPE_PIC, null);
                }else if(key.equals(GundamConst.LAUNCHER_PREFIX+GundamConst.RESOURCECODE_LAUNCHER)){
                    getLauncherResourceInfo("",null);
                }
            }
        }
    }

    /**
     * 初始化
     * 
     */
    public void init() {
        HiLog.d("to init GundamServiceHandler....");
        /**
         * 对操作结果集合进行初始化,目前有三种： 开机广告、音量条广告、Launcher图片
         */
       HashMap<String,String> results = new HashMap<String,String>();
       results.put(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_BOOTUPAD,GundamConst.OPSTATUS_NO);
       results.put(GundamConst.ADCACHE_PREFIX+GundamConst.RESOURCECODE_VOLUMEAD,GundamConst.OPSTATUS_NO);
       results.put(GundamConst.LAUNCHER_PREFIX+GundamConst.RESOURCECODE_LAUNCHER,GundamConst.OPSTATUS_NO);
       GundamApplication.mApp.setResults(results);
        
    }

}
