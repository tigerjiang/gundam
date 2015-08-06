package org.booster.sdk.bean;

public class ADDataReply extends GundamDataReply {
    private static final long serialVersionUID = 1L;
    String editTime = "";
    int adType;
    String adCode="";
    String adResourceURL="";
    public String getEditTime() {
        return editTime;
    }
    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }
    public int getAdType() {
        return adType;
    }
    public void setAdType(int adType) {
        this.adType = adType;
    }
    public String getAdCode() {
        return adCode;
    }
    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }
    public String getAdResourceURL() {
        return adResourceURL;
    }
    public void setAdResourceURL(String adResourceURL) {
        this.adResourceURL = adResourceURL;
    }
    
}
