package org.booster.sdk.bean;

import java.io.Serializable;

public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = -8298461589532954871L;
    private String errorName = "";
    private String errorCode = "";

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
