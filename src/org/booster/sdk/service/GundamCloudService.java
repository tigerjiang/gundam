package org.booster.sdk.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.booster.gundam.GundamApplication;
import org.booster.sdk.bean.ADDataListReply;
import org.booster.sdk.bean.GundamDataReply;
import org.booster.sdk.bean.LauncherDataReply;
import org.booster.sdk.bean.ReportDataReply;
import org.booster.sdk.http.HttpHandler;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.parser.ADContentParser;
import org.booster.sdk.parser.GundamDataParser;
import org.booster.sdk.parser.LauncherContentParser;
import org.booster.sdk.parser.ReportDataParser;
import org.booster.sdk.util.CommonTools;
import org.booster.sdk.util.Constants;



public class GundamCloudService {
    private StringBuilder defaultParameter;
    private StringBuffer prefixUrl;
    private static GundamCloudService service;

    /**
     * 获取应用商店服务实例
     * @param info HiSDKInfo，其中含有访问Server端所必需的配置信息
     * @return 应用商店服务实例
     */
    public static GundamCloudService getInstance() {
        // 双重锁模式实现单例模式
        if (service == null) {
            synchronized (GundamCloudService.class) {
                if (service == null) {
                    service = new GundamCloudService();
                }
            }
        } else {

        }
        return service;
    }

    private GundamCloudService() {
        init();
    }

    private void init() {
        prefixUrl = new StringBuffer();
        defaultParameter = new StringBuilder();
        prefixUrl.append("/").append(Constants.DEFAULTPREFIX);

    }

    /**
     * URL组装方法
     * @param protocalType 协议类型，目前支持Http和Https
     * @param action URL中的action
     * @param map 包含有参数
     * @return 组装后的URL
     */
    protected String assembleUrl(String protocalType, String action, Map<String, String> map) {
        if (CommonTools.isEmpty(action)) {
            return "";
        }
        StringBuffer url = new StringBuffer(protocalType);
        if (protocalType.equalsIgnoreCase(Constants.PROTOCAL_HTTP)) {
            url.append(Constants.DOMAINNAME);
        } else {
            url.append(Constants.DOMAINNAME);
        }
        url.append(prefixUrl).append("?")
            .append(Constants.ACTION).append("=").append(action);
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = CommonTools.toUTF_8(entry.getValue());
                url.append("&").append(key).append("=").append(value);
            }
        }

        url.append(defaultParameter);
        return url.toString();
    }

    protected GundamDataReply execute(GundamDataParser parser, String data) {
        parser.setData(data);
        parser.setEncode(Constants.ENCODE);
        parser.preParse();
        parser.parse();
        return parser.getResult();
    }

    public ADDataListReply getADContentInfo(HashMap<String, String> params) {
        HiLog.d("getADContentInfo");
        String url =assembleUrl(Constants.PROTOCAL_HTTP, Constants.ACTION_AD, params);
        String xmlData =
            HttpHandler.httpGetString(url,Constants.ENCODE);
        //请求业务接口异常时上报日志
        if(!CommonTools.isEmpty(xmlData) && xmlData.startsWith("error")){
            HashMap<String,String> errorLogParams = new HashMap<String,String>();
            errorLogParams.put("a","index");
            errorLogParams.put("mac",CommonTools.getMacAddress(GundamApplication.mApp));
            errorLogParams.put("type",Constants.BUSINESSTYPE_AD);
            errorLogParams.put("content",xmlData);
            errorLogParams.put("url",url);
            errorLogParams.put("memo","");
            reportErrorLog(errorLogParams);
        }
        ADDataListReply reply = null;
        GundamDataReply result = execute(new ADContentParser(), xmlData);
        if (result != null) {
            reply = (ADDataListReply) result;
        }
        return reply;
    }

    public LauncherDataReply getLauncherContentInfo(HashMap<String, String> params) {
        HiLog.d("getLauncherContentInfo");
        String url = assembleUrl(Constants.PROTOCAL_HTTP, Constants.ACTION_LAUNCHER, params);
        String xmlData = HttpHandler.httpGetString(url,Constants.ENCODE);
        //请求业务接口异常时上报日志
        if(!CommonTools.isEmpty(xmlData) && xmlData.startsWith("error")){
            HashMap<String,String> errorLogParams = new HashMap<String,String>();
            errorLogParams.put("a","index");
            errorLogParams.put("mac",CommonTools.getMacAddress(GundamApplication.mApp));
            errorLogParams.put("type",Constants.BUSINESSTYPE_LAUNCHER);
            errorLogParams.put("content",xmlData);
            errorLogParams.put("url",url);
            errorLogParams.put("memo","");
            reportErrorLog(errorLogParams);
        }
        LauncherDataReply reply = null;
        GundamDataReply result = execute(new LauncherContentParser(), xmlData);
        if (result != null) {
            reply = (LauncherDataReply) result;
        }
        return reply;
    }

    /**
     * 错误日志上报接口
     * @param params
     * @return
     */
    public ReportDataReply reportErrorLog(HashMap<String, String> params) {
        HiLog.d("reportLog");
        String xmlData =
            HttpHandler.httpPostString(
                assembleUrl(Constants.PROTOCAL_HTTP, Constants.ACTION_REPORTERRORLOG, null),
                Constants.ENCODE, params);
        ReportDataReply reply = null;
        GundamDataReply result = execute(new ReportDataParser(), xmlData);
        if (result != null) {
            reply = (ReportDataReply) result;
        }
        HiLog.d(reply.getOriginalData());
        return reply;
    }

}
