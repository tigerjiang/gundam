package org.booster.gundam.bean;

/**
 * 资源信息
 */
public class DownloadInfo {
	
	private String resourceURL;// 资源链接
	private String resourcePath;// 资源的本地路径
 	private String resourceMD5;// 资源的MD5
 	//这里主要是满足一些资源文件是要固定存储在某个地方，而这个地方不一定就是资源下载的临时路径，比如启动图片、音量条图片等，所以要区分开
    private String resourceTempPath;//资源的临时本地路径
    private String key;
    private String errorInfo;
    
	public DownloadInfo() {
		resourceURL = new String();
		resourcePath = new String();
		resourceMD5 = new String();
		resourceTempPath = new String();
		key = new String();
		errorInfo = new String();
	}
	
	public DownloadInfo(DownloadInfo downloadInfo)
	{
		resourceURL = downloadInfo.getResourceURL();
		resourcePath = downloadInfo.getResourcePath();
		resourceMD5 = downloadInfo.getResourceMD5();
		resourceTempPath = downloadInfo.getResourceTempPath();
		key = downloadInfo.getKey();
		errorInfo = downloadInfo.getErrorInfo();
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getResourceMD5() {
		return resourceMD5;
	}

	public void setResourceMD5(String resourceMD5) {
		this.resourceMD5 = resourceMD5;
	}

    public String getResourceTempPath() {
        return resourceTempPath;
    }

    public void setResourceTempPath(String resourceTempPath) {
        this.resourceTempPath = resourceTempPath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

}
