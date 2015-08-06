package org.booster.gundam.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.bean.DownloadInfo;
import org.booster.gundam.bean.GundamInfo;
import org.booster.gundam.dao.GundamDaoHandler;
import org.booster.gundam.util.GundamConst;
import org.booster.gundam.util.HttpDownloader;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.util.CommonTools;
import org.booster.sdk.util.Constants;
import org.booster.sdk.util.SingleThreadPool;
import org.booster.sdk.util.Task;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;


public class ResourceDownloadManager{
	private static final int ONE_HOUR_TIME = 1 * 60 * 60;
	private static final int MAX_DOWNLOAD_THREADS = 4;
	
	private static final int DOWNLOAD_REQUEST_MSG = 1;
	private static final int DOWNLOAD_SUCCESS_MSG = 2;
	private static final int DOWNLOAD_FAIL_MSG = 3;
	private static final int FILE_EXIST_MSG = 4;
	
	private static final long RESERVED_STORAGE_SPACE = 6 * 1024 * 1024; 
	
	private static final boolean DEBUG = true;
	
	private static final String TAG = "ResourceDownloadManager";
	
	private int downloadThreadCount = 0;	//正在执行的下载线程数
	private int maxDownloadThreadSize = 0;	//同时可执行的最大下载线程数
	private long storageSpaceUsed = 0;		//己下载资源文件占用的存储空间大小
	private boolean enableReclaim = true;	//允许回收存储空间标志
	private GundamDaoHandler dao;
	private HandlerThread handlerThread;
	private ResourceDownloadHandler handler;
	private ExecutorService downloadThreadPool;		//资源下载线程池
	private List<DownloadInfo> downloadTaskList;	//资源下载请求列表
	private CacheManager adCacheManager;
	private static ResourceDownloadManager downloadManager;
//	private GundamInfo gundamInfo;
	private ResourceDownloadManager(GundamApplication application){
		downloadTaskList = new LinkedList<DownloadInfo>();
		maxDownloadThreadSize = MAX_DOWNLOAD_THREADS;
//		gundamInfo = GundamInfo.getInstance();
		dao = GundamDaoHandler.getInstance();
//		adCacheManager = CacheManager.getInstance(application);
		
		File rootDir = new File(GundamInfo.getInstance().getResourceFileSavePath());
		if(rootDir.exists()){
			storageSpaceUsed = getFileSize(rootDir);
		}
		
		handlerThread = new HandlerThread("ResourceDownloadManager");
		handlerThread.start();
		handler = new ResourceDownloadHandler(handlerThread.getLooper());

		downloadThreadPool = Executors.newFixedThreadPool(4);
	}
	
	public synchronized static ResourceDownloadManager getInstance(GundamApplication application)
	{
		if(downloadManager == null){
			downloadManager = new ResourceDownloadManager(application);
		}
		
		return downloadManager;
	}
	
	/**
	 * 请求资源下载管理器下载资源文件
	 * @param downloadInfo
	 */
	public synchronized void sendDownloadRequest(DownloadInfo downloadInfo){
		Message message = handler.obtainMessage();
		message.what = DOWNLOAD_REQUEST_MSG;
		message.obj = (Object)new DownloadInfo(downloadInfo);
		handler.sendMessage(message);
		HiLog.d("send a Request to download " + downloadInfo.getResourcePath());
	}
	
	/**
	 * 通知资源下载管理器资源文件下载成功
	 * @param downloadInfo
	 */
	public synchronized void sendDownloadSuccessMsg(DownloadInfo downloadInfo){
		Message message = handler.obtainMessage();
		message.what = DOWNLOAD_SUCCESS_MSG;
		message.obj = (Object)new DownloadInfo(downloadInfo);
		handler.sendMessage(message);
		HiLog.d("success to send a DownloadSuccess msg.");
	}
	
	/**
	 * 通知资源下载管理器资源文件下载失败
	 * @param downloadInfo
	 */
	public synchronized void sendDownloadFailMsg(DownloadInfo downloadInfo){
		Message message = handler.obtainMessage();
		message.what = DOWNLOAD_FAIL_MSG;
		message.obj = (Object)new DownloadInfo(downloadInfo);
		handler.sendMessage(message);
		HiLog.d("success to send a DownloadFail msg.");
	}
	
	/**
	 * 通知资源下载管理器资源文件己经存在
	 * @param downloadInfo
	 */
	public synchronized void sendFileExistMsg(DownloadInfo downloadInfo){
		Message message = handler.obtainMessage();
		message.what = FILE_EXIST_MSG;
		message.obj = (Object)new DownloadInfo(downloadInfo);
		handler.sendMessage(message);
		HiLog.d("success to send a FileExist msg.");
	}
	
	class ResourceDownloadHandler extends Handler
	{
		public ResourceDownloadHandler(Looper looper){
			super(looper);
		}
		
		public void handleMessage(Message msg){
			switch(msg.what){
			case DOWNLOAD_REQUEST_MSG:
				handleDownloadRequest((DownloadInfo)msg.obj);
				
				break;
			case DOWNLOAD_SUCCESS_MSG:
				handleDownloadSuccess((DownloadInfo)msg.obj);
				
				break;
			case DOWNLOAD_FAIL_MSG:
			case FILE_EXIST_MSG:
				handleDownloadFail((DownloadInfo)msg.obj);
				
				break;
			}
		}
	}
	
	/**
	 * 获取某个目录/文件的大小
	 * @param file
	 * @return
	 */
	private long getFileSize(File file)
	{
		long size = 0;
				
		if(file.isDirectory()){
			File files[] = file.listFiles();
			if(files != null){
				for(int i = 0; i < files.length; i++){
					size += getFileSize(files[i]);			
				}
			}
		}else{
			size += file.length();
		}
		
		return size;
	}
	
	/**
	 * 删除无引用的资源文件
	 * @param file
	 * @param resourceFiles
	 */
	private void deleteResourceFileExpired(File file, List<String> resourceFiles){	
		if(file.isDirectory()){
			File files[] = file.listFiles();
			if(files != null){
				for(int i = 0; i < files.length; i++){
					deleteResourceFileExpired(files[i], resourceFiles);
				}
			}
		}else{
			if(!resourceFiles.contains(file.getAbsolutePath())){
				long length = file.length();
				if(file.delete()){
					storageSpaceUsed -= length;
				}
			}
		}
		
		return;
	}
	
	/**
	 * 资源文件下载成功处理函数
	 * @param downloadInfo
	 */
	private void handleDownloadSuccess(DownloadInfo downloadInfo){
		File file = new File(downloadInfo.getResourcePath());
		if(file.exists()){
			storageSpaceUsed += file.length();
		}

		if(downloadThreadCount > 0){
			downloadThreadCount--;
		}else{
			Log.e(TAG, "flow exception.");
		}
		
		if(downloadTaskList.size() > 0){
			startDownloader();
		}
		GundamServiceHandler.getInstance(GundamApplication.mApp).postExecuteResource(downloadInfo.getKey());
		return;
	}
	
	/**
	 * 资源文件下载失败处理函数
	 * @param downloadInfo
	 */
	private void handleDownloadFail(final DownloadInfo downloadInfo)
	{
		if(downloadThreadCount > 0){
			downloadThreadCount--;
		}else{
			Log.e(TAG, "flow exception.");
		}
		
		if(downloadTaskList.size() > 0){
			startDownloader();
		}
		GundamApplication.mApp.getResults().put(downloadInfo.getKey(),GundamConst.OPSTATUS_NO);
		//如果错误信息不为空，则上报错误信息
		if(!CommonTools.isEmpty(downloadInfo.getErrorInfo())){
		    SingleThreadPool.addTask(new Task(){

                @Override
                public void work() {
                    String businessType = Constants.BUSINESSTYPE_AD;
                    if(!CommonTools.isEmpty(downloadInfo.getKey())&& downloadInfo.getKey().startsWith(GundamConst.LAUNCHER_PREFIX)){
                        businessType = Constants.BUSINESSTYPE_LAUNCHER;
                    }
                   GundamServiceHandler.getInstance(GundamApplication.mApp).reportErrorLog(businessType,downloadInfo.getErrorInfo(),downloadInfo.getResourceURL(),null);
                    
                }
		        
		    });
		}
		
		
		return;
	}
	
	/**
	 * 资源下载请求处理函数
	 * @param downloadInfo
	 */
	private void handleDownloadRequest(DownloadInfo downloadInfo){
		DownloadInfo tmp;
		Iterator<DownloadInfo> taskIterator = downloadTaskList.iterator();
		
		while(taskIterator.hasNext()){
			tmp = taskIterator.next();
			if(tmp.getResourcePath().equals(downloadInfo.getResourcePath())){
				HiLog.d("task list contains " + downloadInfo.getResourcePath());
				return;
			}
		}
		
		
		downloadTaskList.add(new DownloadInfo(downloadInfo));
		
		if(downloadThreadCount >= maxDownloadThreadSize){
			HiLog.d("download thread count is " + downloadThreadCount);
			return;
		}
		
		startDownloader();
		
		return;
	}
	
	/**
	 * 启动资源下载线程
	 */
	private void startDownloader(){
		StatFs statFs = new StatFs(GundamInfo.getInstance().getResourceFileSavePath());
		
		do{
			long availableStorageSpace = (long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize();
			
			HiLog.d("flashMax:" + GundamInfo.getInstance().getMaxStorageSize());
			HiLog.d("storageSpaceUsed:" + storageSpaceUsed);
			HiLog.d("availableStorageSpace:" + availableStorageSpace);
			
			if(storageSpaceUsed < GundamInfo.getInstance().getMaxStorageSize() && 
					availableStorageSpace > RESERVED_STORAGE_SPACE){
				HiLog.d("task count is " + downloadTaskList.size());
				downloadThreadPool.execute(new HttpDownloader(downloadTaskList.remove(0), downloadManager));
				downloadThreadCount++;
				HiLog.i(TAG, "success to create a http downloader.");
				return;
			}
			
		}while(reclaimStorageSpace());
		
		downloadTaskList.clear();
		
		return;
	}
	
	/**
	 * 删除无引用的资源文件，回收存储空间
	 * @return
	 */
	private boolean reclaimStorageSpace(){	
	    return true;
	    /*
		String adCode;
		String packageName;
		ADCodeInfo adCodeInfo;
		AppADCodeInfo appAdCodeInfo;
		List<AppADCodeInfo> appAdCodeExpired;
		List<ADCodeInfo> adCodeInfosUnregister;
		Iterator<AppADCodeInfo> appAdCodeIterator;
		Iterator<ADCodeInfo> adCodeIterator;
		ConcurrentHashMap<String, AppADCodeInfo> appADCodeMap;
		
		if(!enableReclaim){
			return false;
		}
		
		HiLog.d("clear expired AppAdCodeInfo");
		appAdCodeExpired = dao.getExpiredAppADCodeInfo(adServiceInfo);
		appAdCodeIterator = appAdCodeExpired.iterator();
		while(appAdCodeIterator.hasNext()){
			appAdCodeInfo = appAdCodeIterator.next();
			adCode = appAdCodeInfo.getAdCode();
			packageName = appAdCodeInfo.getPackageName();
			adCodeInfo = adCacheManager.getAdCodeInfo(adCode);
			if(adCodeInfo == null){
			    HiLog.e(TAG, "no AD code info.");
				dao.deleteAppADCodeInfo(adCode, packageName);
				continue;
			}
			
			adCodeInfo.getLock();
			
			appADCodeMap = adCodeInfo.getAppAdCodeInfos();
			if(appADCodeMap.size() > 1){
				dao.deleteAppADCodeInfo(adCode, packageName);
				appADCodeMap.remove(packageName);
			}else{
				dao.deleteAllADCodeInfo(adCode);
				adCacheManager.removeAdCodeInfo(adCode);
			}
			
			adCodeInfo.putLock();
			HiLog.d("clear expired " + adCode + " register info.");
		}
		
	
		List<String> resourceFiles = dao.getAllResourcePath();
		if(resourceFiles.size() > 0){
			File rootDir = new File(adServiceInfo.getAdFileSavePath());
			
			deleteResourceFileExpired(rootDir, resourceFiles);
		}
		
		StatFs statFs = new StatFs(adServiceInfo.getAdFileSavePath());
		long availableStorageSpace = (long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize();
		if(storageSpaceUsed > adServiceInfo.getMaxStorageSize() || 
				availableStorageSpace < RESERVED_STORAGE_SPACE){
			if(DEBUG)
				Log.i(TAG, "storage space is available.");
			
			enableReclaim = false;
			
			return false;
		}
		
		return true;
		*/
	}

}
