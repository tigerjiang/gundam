package org.booster.sdk.bean;

public class LauncherDataReply extends GundamDataReply {

    private static final long serialVersionUID = 1L;
    String editTime = "";
    String resourceURL = "";

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

}
