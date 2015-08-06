package org.booster.gundam.aidl;


import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ResourceInfo implements Parcelable{
	private long resourceId;	//资源ID
	private String resourceCode; //资源编码
	private int resourceType; //资源·类型
	private String resourcePath; //资源链接的本地路径
	private String resourceDetails;//资源详细信息
    private String resourceVersion;//资源版本
    private String resourceTempPath;//资源下载临时路径
	private int enabled;//是否可用
	private String resourceURL;//资源下载URL
	


	
	public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getResourceDetails() {
        return resourceDetails;
    }

    public void setResourceDetails(String resourceDetails) {
        this.resourceDetails = resourceDetails;
    }

    public String getResourceTempPath() {
        return resourceTempPath;
    }

    public void setResourceTempPath(String resourceTempPath) {
        this.resourceTempPath = resourceTempPath;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public ResourceInfo(){
	    resourceId = 0;
	    resourceType = 0;
	    resourceCode = new String();
	    resourcePath = new String();
	    resourceDetails = new String();
	    resourceVersion = new String();
	    resourceTempPath = new String();
	    enabled = 0;
	    resourceURL = new String();
	    
	}
	
	private ResourceInfo(Parcel pr){
	    resourceId = pr.readLong();
	    resourceCode = pr.readString();
	    resourcePath = pr.readString();
		resourceType = pr.readInt();
		resourceDetails = pr.readString();
		resourceVersion = pr.readString();
		resourceTempPath = pr.readString();
		enabled = pr.readInt();
		resourceURL = pr.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(resourceId);
		dest.writeString(resourceCode);
		dest.writeString(resourcePath);
		dest.writeInt(resourceType);
		dest.writeString(resourceDetails);
		dest.writeString(resourceVersion);
		dest.writeString(resourceTempPath);
		dest.writeInt(enabled);
		dest.writeString(resourceURL);
        
	}
	
	public static final Parcelable.Creator<ResourceInfo> CREATOR = new Parcelable.Creator<ResourceInfo>() {
		public ResourceInfo createFromParcel(Parcel source) {
			return new ResourceInfo(source);
		}

		public ResourceInfo[] newArray(int size) {
			return new ResourceInfo[size];
		}
	};





    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        ResourceInfo b =(ResourceInfo)o;
        if(!this.resourceCode.equals(b.getResourceCode())){
            return false;
        }else if(this.resourceType!=b.getResourceType()){
            return false;
        }else if(!this.resourceTempPath.equals(b.getResourceTempPath())){
            return false;
        }else if(!this.resourcePath.equals(b.getResourcePath())){
            return false;
        }else if(!this.resourceVersion.equals(b.getResourceVersion())){
            return false;
        }else if(!this.resourceURL.equals(b.getResourceURL())){
            return false;
        }
        return true;
    }

}
