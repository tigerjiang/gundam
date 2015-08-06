package org.booster.sdk.http;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.booster.sdk.logging.HiLog;
import org.booster.sdk.util.Constants;





import android.text.TextUtils;

public class HttpHandler {
    private final static String TAG = "HttpHandler";
    
    private static DefaultHttpClient httpClient = CustomHttpClient
        .getHttpClient();
//    private static PublicKey publicKey = HiCloudKey.getPublicKey();

    public static String httpGetString(String url, String encode, boolean verifyFlag) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (TextUtils.isEmpty(encode)) {
            encode = Constants.ENCODE;
        }
        HiLog.d(TAG, "url is : " + url);
        HttpGet request = null;
        HttpResponse response = null;
        String result = null;
        try {
            request = new HttpGet(url);
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                // Log.d(TAG, "request error ... ");
                HiLog.d(TAG, "request error ... ");
                if (request != null) {
                    request.abort();
                }
                return "error : responsecode "+response.getStatusLine().getStatusCode()+" for url : "+url;
            }
            //test for report error log
           /* if(url.contains("launcher")){
                if (request != null) {
                    request.abort();
                }
                return "error : responsecode "+response.getStatusLine().getStatusCode()+" for url : "+url;
            }*/
            
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encode);
            }
        } catch (Exception e) {
            if (request != null) {
                request.abort();
            }
            HiLog.d(TAG,e.getMessage());
            return "error : "+e.getMessage();
        }

        return result;
    }

    public static String httpGetString(String url, String encode) {
        return httpGetString(url, encode, true);
    }

    public static String httpPostString(String url, String encode,
        Map<String, String> map) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (TextUtils.isEmpty(encode)) {
            encode = Constants.ENCODE;
        }
        
        // for print url log
        StringBuilder builder = new StringBuilder();
        builder.append(url).append("&");
        HttpPost request = null;
        HttpResponse response = null;
        String result = null;
        List<BasicNameValuePair> postParameters = new ArrayList<BasicNameValuePair>();
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                value = value == null ? "" : value;
                postParameters.add(new BasicNameValuePair(key, value));
                builder.append(key).append("=").append(value).append("&");
            }
        }
       
        HiLog.d(TAG, "url is : " + url + "  postParameter:" + builder.toString());
        
        try {
            request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                postParameters, encode);
            request.setEntity(formEntity);
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                // Log.d(TAG, "request error ... ");
                HiLog.d(TAG, "request error ... ");
                if (request != null) {
                    request.abort();
                }
                return "error : responsecode "+response.getStatusLine().getStatusCode()+" for url : "+url;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (request != null) {
                request.abort();
            }
            // Log.v(TAG, SDKUtil.getStackTrace(e));
            HiLog.e(TAG,e.getMessage());
            return "error : "+e.getMessage();
        }
        return result;
    }
    
   

}
