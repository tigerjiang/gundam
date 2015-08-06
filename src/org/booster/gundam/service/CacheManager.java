package org.booster.gundam.service;

import java.util.concurrent.ConcurrentHashMap;

import org.booster.gundam.GundamApplication;
import org.booster.gundam.aidl.ResourceInfo;
import org.booster.sdk.logging.HiLog;

public class CacheManager {
	private ConcurrentHashMap<String, ResourceInfo> cache;
	private static CacheManager cacheManager;

	private CacheManager(){
		cache = new ConcurrentHashMap<String, ResourceInfo>();
	}
	
	public synchronized static CacheManager getInstance(){
		if(cacheManager == null){
			cacheManager = new CacheManager();
		}
		
		return cacheManager;
	}
	
	/**
	 * 根据资源类型从缓存管理器获取信息
	 * @param resourceType
	 * @return
	 */
	public ResourceInfo getResourceInfo(String key){	
	    if(cache.containsKey(key)){
		    HiLog.d("cache size is : "+cache.size());
			return cache.get(key);
		}
		return null;
	}
	
	/**
	 * 从缓存管理器删除资源信息
	 * @param resourceType
	 * @return
	 */
	public ResourceInfo removeResourceInfo(String key){
		return cache.remove(key);
	}
	
	/**
	 * 往缓存管理器添加资源信息
	 * @param key
	 * @param value
	 * @return
	 */
	public ResourceInfo putResourceInfo(String key, ResourceInfo value){
	    if(value!=null){
	        return cache.put(key, value);
	    }
	    return null;
		
	}
}
