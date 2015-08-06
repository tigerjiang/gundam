package org.booster.sdk.bean;

public class ReportDataReply extends GundamDataReply {

    private static final long serialVersionUID = 1L;

    String operateTime = "";
    String businessType = "";
    public String getOperateTime() {
        return operateTime;
    }
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    
}
