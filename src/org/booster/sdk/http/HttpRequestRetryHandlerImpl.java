package org.booster.sdk.http;

import java.io.IOException;
import java.net.SocketException;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {

    @Override
    public boolean retryRequest(IOException exception, int executionCount,
        HttpContext context) {
        if (executionCount > 3) {
            return false;
        }

        if (exception instanceof NoHttpResponseException) {
            return true;
        } else if (exception instanceof SSLHandshakeException) {
            return false;
        } else if (exception instanceof ConnectTimeoutException) {
            return true;
        } else if (exception instanceof IOException) {
            return true;
        } else if (exception instanceof SocketException) {
            return true;
        }
        HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
        if (!idempotent) {
            return true;
        }
        return true;
    }

}
