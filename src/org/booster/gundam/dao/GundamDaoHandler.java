package org.booster.gundam.dao;

import java.util.HashMap;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.aidl.ResourceInfo;
import org.booster.gundam.util.GundamConst;
import org.booster.sdk.bean.ADDataListReply;
import org.booster.sdk.bean.ADDataReply;
import org.booster.sdk.bean.LauncherDataReply;
import org.booster.sdk.bean.ReportDataReply;
import org.booster.sdk.service.GundamCloudService;
import org.booster.sdk.util.CommonTools;

import com.google.gson.Gson;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 * @author Merry.Zhao
 *
 */
public class GundamDaoHandler {
    /**
     * 自引用
     */
    private static GundamDaoHandler instance;
    private GundamCloudService cloudService;
    

    /**
     * Dao构造方法，私有化以确保单例
     * @param 
     */
    private GundamDaoHandler() {
        cloudService = GundamCloudService.getInstance();
    }

    /**
     * 获取Dao的服务实例，确保单例
     * @param 
     * @return
     */
    public static GundamDaoHandler getInstance() {
        if (instance == null) {
            synchronized (GundamDaoHandler.class) {
                if (instance == null) {
                    instance = new GundamDaoHandler();
                }
            }
        } else {
        }
       return instance;
    }

    public ResourceInfo getADInfo(int boardId, int adType) {
       //TODO 访问服务器获取广告信息。
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("a","index");
        params.put("mac",CommonTools.getMacAddress(GundamApplication.mApp));
        params.put("board_id",String.valueOf(boardId));
        params.put("type",String.valueOf(adType));
        
       ADDataListReply adList = cloudService.getADContentInfo(params);
       if(adList.getList().size()>0){
           ADDataReply reply = adList.getList().get(CommonTools.getRandomNumber(adList.getList().size()));
           ResourceInfo resourceInfo = new ResourceInfo();
           resourceInfo.setResourceCode(reply.getAdCode());
           resourceInfo.setResourceType(reply.getAdType());
           resourceInfo.setResourceURL(reply.getAdResourceURL());
           resourceInfo.setResourceDetails(adList.getOriginalData());
           
           resourceInfo.setResourceTempPath(GundamApplication.mApp.getFilesDir().getAbsolutePath()+resourceInfo.getResourceURL().substring(resourceInfo.getResourceURL().lastIndexOf("/")));
           
           //根据广告类型设置不同的广告下载路径和最终存取路径
           //已经约定   开机启动时广告服务主动请求服务器端，若有多个广告，全部下载，并且随机选择一个广告，将其资源，连同
           //服务器返回的XML数据保存成xml文件，转存到指定的目录，不同的广告，指定的目录不同。
           switch (boardId){
               case GundamConst.RESOURCECODE_BOOTUPAD:
                   resourceInfo.setResourcePath(GundamConst.BOOTUPAD_PATH);
                    break;
               case GundamConst.RESOURCECODE_VOLUMEAD:
                   resourceInfo.setResourcePath(GundamConst.VOLUMEAD_PATH);
                   break;
               default:
                   resourceInfo.setResourcePath(resourceInfo.getResourceTempPath());
                   break;
           }
           return resourceInfo;
       }
        
        return null;
    }
    
    /**
     * 获取Launcher资源信息
     * @param version
     * @return
     */
    public ResourceInfo getLauncherInfo(String version) {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("a","index");
        params.put("mac",CommonTools.getMacAddress(GundamApplication.mApp));
        params.put("version",String.valueOf(version));
        
        
       LauncherDataReply reply = cloudService.getLauncherContentInfo(params);
       if(reply !=null && !CommonTools.isEmpty(reply.getResourceURL())){
           
           ResourceInfo resourceInfo = new ResourceInfo();
           resourceInfo.setResourceURL(reply.getResourceURL());
           resourceInfo.setResourceDetails(reply.getOriginalData());
           resourceInfo.setResourceVersion(version);
           resourceInfo.setResourceTempPath(GundamApplication.mApp.getFilesDir().getAbsolutePath()+resourceInfo.getResourceURL().substring(resourceInfo.getResourceURL().lastIndexOf("/")));
           
           //根据资源类型设置不同的广告下载路径和最终存取路径
           //已经约定   开机启动时服务主动请求服务器端，将其资源，连同
           //服务器返回的XML数据保存成xml文件，转存到指定的目录，不同的资源，指定的目录不同。
           
           resourceInfo.setResourcePath(GundamConst.LAUCHERRESOURCE_PATH);
           return resourceInfo;
       }
        
        return null;
    }
    
    
    /**
     *  从本地获取资源信息，本地资源信息保存在sharedpreference中
     * @param key
     * @return
     */
    public ResourceInfo getLocalResourceInfo(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GundamApplication.mApp);
        String s = prefs.getString(key,null);
        if(!CommonTools.isEmpty(s)){
            Gson gson = new Gson();
            ResourceInfo resourceInfo = gson.fromJson(s,ResourceInfo.class);
            return resourceInfo;
        }
        return null;
    }
    /**
     * 保存资源信息，本地资源信息保存在sharedpreference中s
     * @param key
     * @param value
     * @return
     */
    public boolean saveResourceInfo(String key,ResourceInfo resourceInfo){
        if(resourceInfo == null){
            return false;
        }
        Gson gson = new Gson();
        String value = gson.toJson(resourceInfo);
        if(!CommonTools.isEmpty(value)){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GundamApplication.mApp);
            Editor editor = prefs.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public ReportDataReply reportErrorLog(String businessType,String errorInfo,String url,String description){
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("a","index");
        params.put("mac",CommonTools.getMacAddress(GundamApplication.mApp));
        params.put("type",businessType);
        params.put("content",errorInfo);
        params.put("url",url);
        params.put("memo",description);
        ReportDataReply reply = cloudService.reportErrorLog(params);
        return reply;
    }
}
