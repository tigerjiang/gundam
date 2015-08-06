package org.booster.gundam.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.booster.gundam.bean.DownloadInfo;
import org.booster.gundam.service.ResourceDownloadManager;
import org.booster.sdk.http.CustomHttpClient;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.util.FileUtil;






public class HttpDownloader implements Runnable {

	private final String TAG = "HttpDownloader";
	private final int MAX_BUFFER_SIZE = 4096*2;
	private final int HTTP_RESPONSE_CODE_OK = 200;
	private final int HTTP_RESPONSE_CONTINUE_CODE_OK = 206;
    
	
	
	private final int DOWNLOADING = 0;
	private final int EXIST = 1;
	private final int COMPLETED = 2;
	private final int ERROR = 4;
	private static int count = 0;
	
	private int id;
	private volatile DownloadInfo downloadInfo;
	private ResourceDownloadManager downloadManager;
//	private int status;

	public HttpDownloader(DownloadInfo downloadInfo, ResourceDownloadManager downloadManager){
		id = count++;	
		this.downloadInfo = new DownloadInfo(downloadInfo);
		this.downloadManager = downloadManager;
		HiLog.d("HttpDownloader" + id + " downloadInfo::" + this.downloadInfo + ", " + downloadInfo );
	}
	
	public void run() {
        long downloadedSize = 0;
        URL httpUrl = null;
        File resourceFile = null;
        File tmpResourceFile = null;
        File rootFile = null;
        RandomAccessFile file = null;
        InputStream inputStream = null;
//        HttpURLConnection httpConnection = null;
        HttpResponse response = null;  
        DefaultHttpClient client = null;
        HttpGet request=null;
        HttpEntity entity=null;
        
        HiLog.d("httpdownloader" + id + ":" + " to download resource file " + downloadInfo.getResourcePath());
        if(downloadInfo.getResourcePath().length() == 0){
            HiLog.d("invalid path");
            changeStatus(ERROR,"no resource path in locale",downloadInfo.getResourceURL());
            return;
        }
        rootFile = new File(GundamConst.BASIC_FILE_DIR);
        if(!rootFile.exists()){
        	  System.out.print("dir doesn't exists");
        	  rootFile.mkdir();
        }else{
        	 System.out.print("dir doesn't exists");
        }
		if (!rootFile.isDirectory()) {
			System.out.print("file is not dir");
			rootFile.delete();
			rootFile.mkdir();
		}
        resourceFile = new File(downloadInfo.getResourcePath());
//      if(resourceFile.exists() || tmpResourceFile.exists()){
        if(resourceFile.exists()){
            /*需要下载的资源文件己存在*/
//            httpConnection.disconnect();
           /* if(request!=null){
                request.abort();
                request=null;
            }*/
            changeStatus(EXIST,"resource file exist",downloadInfo.getResourceURL());
           /* file.close();
            file = null;*/
            return;
        }
        
        
        client = CustomHttpClient.getMultiHttpClient();
        HiLog.d("httpdownloader" + id + ":" + " resource url is " + downloadInfo.getResourceURL());
        try{
            httpUrl = new URL(downloadInfo.getResourceURL());
        }catch(MalformedURLException e){
            HiLog.d("invalid url");
            changeStatus(ERROR,e.getMessage(),downloadInfo.getResourceURL());
            
            return;
        }
        
        try{
            file = new RandomAccessFile(downloadInfo.getResourcePath() + ".tmp", "rw");
            if(file!=null){
                downloadedSize = file.length();
            }
            
            
            //connect to http server
            HiLog.d("httpdownloader" + id + ":" + "to connect http server.");
            request = new HttpGet(httpUrl.toURI()); 
          //设置下载的数据位置XX字节到XX字节   
            Header header_size = new BasicHeader("Range", "bytes=" + downloadedSize + "-"  
                );  
            request.addHeader(header_size);  
            HiLog.d("before response the download url is："+request.getURI().toString());
            response = client.execute(request);  
            
            HiLog.d("after response the download url is："+request.getURI().toString());
            Header[] headers=response.getAllHeaders();
            
            for(int i=0;i<headers.length;i++){
               
               
                HiLog.d(headers[i].getName()+" "+ headers[i].getValue());

            }
            
             //获取响应码
            int responseCode = response.getStatusLine().getStatusCode(); 
            
            
            
            /*httpConnection = (HttpURLConnection)httpUrl.openConnection();
            httpConnection.setRequestProperty("Range", "bytes=" + downloadedSize + "-");
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();*/
            if(responseCode != HTTP_RESPONSE_CODE_OK && responseCode != HTTP_RESPONSE_CONTINUE_CODE_OK){
//                httpConnection.disconnect();
                if(request!=null){
                    request.abort();
                    request=null;
                }
                changeStatus(ERROR,"response code error as : "+responseCode,downloadInfo.getResourceURL());
                HiLog.d("failed to connect http server.");
                file.close();
                file = null;
                return;
            }
            entity = response.getEntity();
            long contentSize = entity.getContentLength();
//            int contentSize = httpConnection.getContentLength();
            if(contentSize < 1){
//                httpConnection.disconnect();
                if(request!=null){
                    request.abort();
                    request=null;
                }
                changeStatus(ERROR,"server file error content size is 0",downloadInfo.getResourceURL());
                HiLog.e("httpdownloader" + id + ":" + "the size of " + downloadInfo.getResourcePath() + " is 0.");
                file.close();
                file = null;
                return;
            }
            
            
           

            
            //read from http server into temporary resource file
            
            tmpResourceFile= new File(downloadInfo.getResourcePath() + ".tmp");
            file.seek(downloadedSize);
            inputStream = entity.getContent();
            if(inputStream == null){
                if(request!=null){
                    request.abort();
                    request=null;
                }
                if(entity!=null)
                {
                    entity.consumeContent();
                    entity=null;
                }
            }
            HiLog.d("httpdownloader" + id + ":" + "  write resource into " + downloadInfo.getResourcePath()+".tmp file.");
            int readSize = 0;
            byte buffer[] = new byte[MAX_BUFFER_SIZE];
            do{
                if(downloadedSize>=contentSize)
                    break;
                readSize = inputStream.read(buffer);
                file.write(buffer, 0, readSize);
                downloadedSize += readSize;
//                HiLog.d("downloaded size is :>>>>>>   "+downloadedSize);
            }while(true);
            /*while((readSize = inputStream.read(buffer)) != -1){             
                file.write(buffer, 0, readSize);
                downloadedSize += readSize;
                HiLog.d("downloaded size is ::::   "+downloadedSize);
            }*/
           
            HiLog.d("download finished !!!!!!!");
            file.close();
            file = null;
            try{
                if(request!=null){
                request.abort();
                request=null;
            }
            
            if(entity!=null)
            {
                entity.consumeContent();
                entity=null;
            }
            if (inputStream != null) {
                
                inputStream.close();
                inputStream = null;
            }
            }catch(Exception ex){
//                ex.printStackTrace();
                HiLog.w("error in close connection resources : "+ex.getMessage());
            }
            
            
            //rename temporary resource file to resource file
            if(downloadedSize != contentSize || !tmpResourceFile.renameTo(resourceFile)){
                tmpResourceFile.delete();
                if(downloadedSize != contentSize){
                    changeStatus(ERROR,"downloaded file size is not correct downloaded size is : "+downloadedSize+" , origin content size is : "+contentSize,downloadInfo.getResourceURL());
                }else{
                    changeStatus(ERROR,"rename tmp downloaded file error ! for : "+tmpResourceFile,downloadInfo.getResourceURL());
                }
                HiLog.d("httpdownloader" + id + ":" + "file size is " 
                        + downloadedSize + " Failed to rename " + downloadInfo.getResourcePath() 
                        + ".tmp to " + downloadInfo.getResourcePath());
            }else{
                FileUtil.changeMode(downloadInfo.getResourcePath(),"chmod 777");
                changeStatus(COMPLETED,null,null);
                HiLog.d("httpdownloader" + id + ":" + "success to download resource files " + downloadInfo.getResourcePath());
            }
        }catch(Exception e){
            HiLog.e("error in downloadfile is : "+e.getMessage());
            e.printStackTrace();
            
            if(file != null){
                try{
                    file.close();
                    file = null;
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
            
            if(tmpResourceFile != null && tmpResourceFile.exists()){
                tmpResourceFile.deleteOnExit();
                tmpResourceFile = null;
            }
            
            changeStatus(ERROR,"error in download file . ",downloadInfo.getResourceURL());
            
            
            
        }finally{
            //disconnect from http server
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                inputStream = null;
            }
            
            if(request != null){
                request.abort();
                request = null;
            }
            if(entity != null){
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                entity = null;
            }
        }
        
       
        
        
        
        return;
    }
	
	/*
	public void run1() {
		long downloadedSize = 0;
//		int status = DOWNLOADING;
		URL httpUrl = null;
		File resourceFile = null;
		File tmpResourceFile = null;
		RandomAccessFile file = null;
		InputStream inputStream = null;
		HttpURLConnection httpConnection = null;
		
		HiLog.d("httpdownloader" + id + ":" + " to download resource file " + downloadInfo.getResourcePath());
		if(downloadInfo.getResourcePath().length() == 0){
			HiLog.d("invalid path");
			changeStatus(ERROR);
			
			return;
		}
		
		HiLog.d("httpdownloader" + id + ":" + " resource url is " + downloadInfo.getResourceURL());
		try{
			httpUrl = new URL(downloadInfo.getResourceURL());
		}catch(MalformedURLException e){
			HiLog.d("invalid url");
			changeStatus(ERROR);
			
			return;
		}
		
		try{
		    file = new RandomAccessFile(downloadInfo.getResourcePath() + ".tmp", "rw");
		    if(file!=null){
		        downloadedSize = file.length();
		    }
		    
		    
			//connect to http server
			HiLog.d("httpdownloader" + id + ":" + "to connect http server.");
			httpConnection = (HttpURLConnection)httpUrl.openConnection();
			httpConnection.setRequestProperty("Range", "bytes=" + downloadedSize + "-2141865");
			httpConnection.connect();
			int responseCode = httpConnection.getResponseCode();
			if(responseCode != HTTP_RESPONSE_CODE_OK && responseCode != HTTP_RESPONSE_CONTINUE_CODE_OK){
				httpConnection.disconnect();
				changeStatus(ERROR);
				HiLog.d("failed to connect http server.");
				file.close();
				file = null;
				return;
			}
			
			int contentSize = httpConnection.getContentLength();
			if(contentSize < 1){
				httpConnection.disconnect();
				changeStatus(ERROR);
				HiLog.e("httpdownloader" + id + ":" + "the size of " + downloadInfo.getResourcePath() + " is 0.");
				file.close();
                file = null;
				return;
			}
			
			resourceFile = new File(downloadInfo.getResourcePath());
			tmpResourceFile = new File(downloadInfo.getResourcePath() + ".tmp");
//			if(resourceFile.exists() || tmpResourceFile.exists()){
			if(resourceFile.exists()){
//				需要下载的资源文件己存在
				httpConnection.disconnect();
				changeStatus(EXIST);
				file.close();
                file = null;
				return;
			}
			
			//read from http server into temporary resource file
			
			
			file.seek(downloadedSize);
			inputStream = httpConnection.getInputStream();
			
			HiLog.d("httpdownloader" + id + ":" + "  write resource into " + downloadInfo.getResourcePath()+".tmp file.");
			int readSize = 0;
			byte buffer[] = new byte[MAX_BUFFER_SIZE];
			while((readSize = inputStream.read(buffer)) != -1){				
				file.write(buffer, 0, readSize);
				downloadedSize += readSize;
				//HiLog.d("downloaded size is ::::   "+downloadedSize);
			}
			HiLog.d("download finished !!!!!!!");
			file.close();
			file = null;
			//rename temporary resource file to resource file
			if(downloadedSize != contentSize || !tmpResourceFile.renameTo(resourceFile)){
				tmpResourceFile.delete();
				changeStatus(ERROR);
				HiLog.d("httpdownloader" + id + ":" + "file size is " 
						+ downloadedSize + " Failed to rename " + downloadInfo.getResourcePath() 
						+ ".tmp to " + downloadInfo.getResourcePath());
			}else{
				FileUtil.changeMode(downloadInfo.getResourcePath(),"chmod 755");
				changeStatus(COMPLETED);
				HiLog.d("httpdownloader" + id + ":" + "success to download resource files " + downloadInfo.getResourcePath());
			}
		}catch(Exception e){
		    HiLog.e("error in downloadfile is : "+e.getMessage());
			e.printStackTrace();
			
			if(file != null){
				try{
					file.close();
					file = null;
				}catch(Exception exception){
					exception.printStackTrace();
				}
			}
			
			if(tmpResourceFile != null && tmpResourceFile.exists()){
				tmpResourceFile.deleteOnExit();
				tmpResourceFile = null;
			}
			
			changeStatus(ERROR);
		}finally{
			//disconnect from http server
			if(inputStream != null){
				try{
					inputStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				inputStream = null;
			}
			
			if(httpConnection != null){
				httpConnection.disconnect();
				httpConnection = null;
			}
		}
		
		
		
		
		
		return;
	}
	*/
	private void changeStatus(int status,String errorMessage,String errorUrl){	
	    HiLog.d("status changed ..download .....:   "+status);
		if(status == COMPLETED){
			downloadManager.sendDownloadSuccessMsg(downloadInfo);
			//just for test
			//downloadInfo.setErrorInfo("test error");
			//downloadManager.sendDownloadFailMsg(downloadInfo);
			//just for test done !
		}else if(status == ERROR){
		    downloadInfo.setErrorInfo(errorMessage);
			downloadManager.sendDownloadFailMsg(downloadInfo);
		}else if(status == EXIST){
			downloadManager.sendFileExistMsg(downloadInfo);
		}		
	}

}
