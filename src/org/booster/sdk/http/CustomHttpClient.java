package org.booster.sdk.http;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.booster.sdk.logging.HiLog;






public class CustomHttpClient {
    private static DefaultHttpClient customHttpClient = null;
    private static ThreadSafeClientConnManager ccManager = null;
    private static HttpParams params = null;
    private static SchemeRegistry schemeRegistry = null;
    private final static int MAX_REDIRECTS=5;//最大重定向次数
    private CustomHttpClient() {
    }

    static {
        params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, false);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
            + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");

        // 最大连接数：200
        ConnManagerParams.setMaxTotalConnections(params, 200);
        // 单路由最大连接数：100
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(100);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

        ConnManagerParams.setTimeout(params, 1000);
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        HttpConnectionParams.setSoTimeout(params, 1000 * 20);

        schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getIgnoreCertificationSocketFactory(), 443));
    }

    private static ThreadSafeClientConnManager getConnectionManager() {
        if (ccManager == null) {
            synchronized (CustomHttpClient.class) {
                if (ccManager == null) {
                    ccManager = new ThreadSafeClientConnManager(params, schemeRegistry);
                }
            }
        }
        return ccManager;
    }

    public static DefaultHttpClient getHttpClient() {
        if (customHttpClient == null) {
            synchronized (CustomHttpClient.class) {
                if (customHttpClient == null) {
                    customHttpClient = new DefaultHttpClient(getConnectionManager(), params);
                    customHttpClient.setHttpRequestRetryHandler(new HttpRequestRetryHandlerImpl());
                }
            }
        }
        ccManager.closeExpiredConnections();
        return customHttpClient;
    }

    public static void shutDown() {
        if (ccManager != null) {
            ccManager.shutdown();
            ccManager = null;
            HiLog.d("CustomHttpClient : ", "close all connections ... ");
        }
    }
    
    /**
     * 获取一个可用的连接
     * @return
     */
    public static DefaultHttpClient getMultiHttpClient(){
    DefaultHttpClient client = new DefaultHttpClient(getConnectionManager(), params);
    
    client.setHttpRequestRetryHandler(new HttpRequestRetryHandlerImpl());
    setClientRedirectParams(client);
    HiLog.v("total clients in pool : "+getConnectionManager().getConnectionsInPool());
    ccManager.closeExpiredConnections();
    return client;
    }
    /**
     * 设置重定向处理的一些参数
     * @param defaultClient
     */
        private static void setClientRedirectParams(DefaultHttpClient defaultClient){
            defaultClient.getParams().setParameter("http.protocol.handle-redirects", true);
            defaultClient.getParams().setIntParameter("http.protocol.max-redirects", MAX_REDIRECTS);
            defaultClient.getParams().setParameter("http.protocol.allow-circular-redirects", false);
      
        }
}
