package org.booster.sdk.bean;

import java.io.Serializable;

public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 3754320750309448604L;
    private String status;// 0 表示成功，1表示失败
    private ErrorInfo errorInfo;
    private String signatureVerified; // 0表示签名验证成功，1表示签名验证失败

    /**
     * 0表示返回成功，1表示失败
     * @return
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    /**
     * 结果签名校验是否成功标志位，0表示成功，1表示失败
     * @return
     */
    public String getSignatureVerified() {
        return signatureVerified;
    }

    public void setSignatureVerified(String signatureVerified) {
        this.signatureVerified = signatureVerified;
    }
}
