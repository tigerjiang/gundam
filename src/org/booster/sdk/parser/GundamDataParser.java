/**
 * 
 */
package org.booster.sdk.parser;

import java.util.List;

import org.booster.sdk.bean.ErrorInfo;
import org.booster.sdk.bean.GundamDataReply;
import org.booster.sdk.util.CommonTools;
import org.booster.sdk.util.Constants;



/**
 * @author merry
 */
public abstract class GundamDataParser {

    protected String data = ""; // 要解析的数据
    protected GundamDataReply result; // 解析后返回的数据
    protected String encode = Constants.ENCODE; // 默认的编码方式
    protected ErrorInfo errorInfo = null;// 错误信息对象
    protected String temp = "";
    protected boolean endTag = false;
    protected boolean startElementFlag = false;
    protected  String rootTag = "root";
    protected  String errorCodeTag = "error_code";
    protected final String errorNameTag = "reason";
    
    /**
     * 设置要解析的数据</br>
     * @param data 要解析的EPG数据</br>
     */
    public void setData(String data) {
        this.data = data;
        this.decryptData();

    }

    /**
     * 获取当前编码设置</br>
     * @return 当前的编码</br>
     */
    public String getEncode() {
        return encode;
    }

    /**
     * 设置编码</br>
     * @param encode 要设置的编码</br>
     */
    public void setEncode(String encode) {
        this.encode = encode;
    }

    /**
     * 带参构造方法</br>
     * @param data 要解析的数据</br>
     */
    protected GundamDataParser(String data) {
        this.data = data;
        this.decryptData();
    }

    /**
     * 构造方法</br>
     */
    protected GundamDataParser() {
    }

    /**
     * 预解析数据</br> 在解析系统端下发的数据之前触发</br> 该方法在每个解析方法中都会被调用到</br>
     */
    public void preParse() {

    }

    /**
     * 解析数据</br>
     */
    public abstract void parse();

    /**
     * 返回解析后的数据</br>
     * @return 含有解析后的数据的对象</br>
     */
    public GundamDataReply getResult() {
        if (!CommonTools.isEmpty(data)) {
            if (errorInfo != null) {
                result.setStatus(Constants.RESULT_FAIL);
                result.setErrorInfo(errorInfo);
            } else {
                result.setStatus(Constants.RESULT_SUCCESS);
            }
            result.setOriginalData(data);
        } else {
            result.setStatus(Constants.RESULT_FAIL);
        }
        return result;

    }

    /**
     * 内部方法，对要解析的数据进行解密</br> 通常情况下，需要对要解析的数据先进行解密再进行解析</br>
     */
    protected void decryptData() {

    }
}
