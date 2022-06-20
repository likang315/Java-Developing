package com.atlantis.zeus.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * HTTPClient
 *
 * @author likang02@corp.netease.com
 * @date 2021-11-25 15:40
 */
@Slf4j
public class HTTPClientUtil {
    /**
     * 默认的编码方式
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");
    private static final String RUBBISH_IMAGE_PATH = "/home/appops/rubbishimages/";
    private static final String DEFAULT_IMAGE_PATH = "/home/appops/videoimages/";
    private static final String HTTPS_PREFIX = "https";
    public static final String HTTP_PREFIX = "http";
    /**
     * 默认的connect timeout 10s
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    /**
     * 默认的read timeout 10s 从连接池获取连接的超时时间
     */
    private static final int DEFAULT_READ_TIMEOUT = 5000;
    /**
     * 默认的socket timeout 10s
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    /**
     * 10分钟报一次警
     */
    private static final long TIME = 600000L;

    private static final String JSON_CONTENT_TYPE = "application/json";

    /**
     * 未初始化的httpclient
     */
    private static CloseableHttpClient httpClient;
    private static Map<HttpHost, Long> hostAliamMap = new HashMap<>();

    private static String userAgent = " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36";

    private enum BODY_TYPE {
        PARAM, JSON
    }

    // 初始化连接管理池
    static {
        PoolingHttpClientConnectionManager cm = buildConnectionManager();
        // socket 配置
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_SOCKET_TIMEOUT).build();
        cm.setDefaultSocketConfig(socketConfig);
        // 连接的空闲关闭
        cm.closeIdleConnections(30, TimeUnit.MINUTES);
        cm.setMaxTotal(200);
        // 同路由的并发数 , 默认值2 . 限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)
        cm.setDefaultMaxPerRoute(100);
        // HTTP conn 配置
        ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build();
        cm.setDefaultConnectionConfig(defaultConnectionConfig);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static CloseableHttpClient getSslHttpClient(Charset charset) {
        try {
            ConnectionConfig config = ConnectionConfig.custom().setCharset(charset).build();
            return HttpClients.custom().setDefaultConnectionConfig(config).setSSLSocketFactory(buildSSLConnectionFactory()).build();
        } catch (Exception e) {
            log.warn("getSslHttpClient failed", e);
        }
        return null;
    }

    public static CloseableHttpClient getSslHttpClientWithRetryHandler(Charset charset) {
        try {
            ConnectionConfig config;
            if (charset != null) {
                config = ConnectionConfig.custom().setCharset(charset).build();
            } else {
                config = ConnectionConfig.custom().build();
            }
            return HttpClients.custom().setDefaultConnectionConfig(config).setSSLSocketFactory(buildSSLConnectionFactory())
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false)).build();
        } catch (Exception e) {
            log.error("getSslClient", e);
        }
        return null;
    }

    public static String get(String url) throws Exception {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.GET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.GET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, params, headers);
    }

    public static String getLong(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.GET, 2 * DEFAULT_SOCKET_TIMEOUT, 2 * DEFAULT_CONNECT_TIMEOUT, 3 * DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String getLong(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.GET, 2 * DEFAULT_SOCKET_TIMEOUT, 2 * DEFAULT_CONNECT_TIMEOUT, 3 * DEFAULT_READ_TIMEOUT, url, params,
                headers);
    }

    public static String getVeryLong(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.GET, 5 * DEFAULT_SOCKET_TIMEOUT, 5 * DEFAULT_CONNECT_TIMEOUT, 30 * DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String post(String url) throws Exception {
        return post(url, null);
    }

    public static String post(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, params, headers);
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String post(String url, Map<String, String> params, int connectTimeout) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT, connectTimeout, DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String postIgnoreStatusCode(String url, Map<String, String> params, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, params, headers,
                BODY_TYPE.PARAM, false);
    }

    public static String postLong(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.POST, 4 * DEFAULT_SOCKET_TIMEOUT, 2 * DEFAULT_CONNECT_TIMEOUT, 2 * DEFAULT_READ_TIMEOUT, url, params);
    }

    public static String postShort(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT / 3, DEFAULT_CONNECT_TIMEOUT / 3, DEFAULT_READ_TIMEOUT / 3, url, params);
    }

    public static String postVeryShort(String url, Map<String, String> params) throws Exception {
        return execute(HttpMethod.POST, 200, 200, 200, url, params);
    }

    public static String execute(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url,
                                 Map<String, String> params) throws Exception {
        return execute(method, socketTimeout, connectTimeout, readTimeout, url, params, null);
    }

    /**
     * POST json
     *
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public static String postJson(String url, String json, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, json, headers,
                BODY_TYPE.JSON);
    }

    public static String postJsonLong(String url, String json, Map<String, String> headers) throws Exception {
        return execute(HttpMethod.POST, DEFAULT_SOCKET_TIMEOUT * 3, DEFAULT_CONNECT_TIMEOUT * 3, DEFAULT_READ_TIMEOUT * 3, url, json, headers,
                BODY_TYPE.JSON);
    }

    /**
     * 用连接池的http请求.
     *
     * @param method {@link HttpMethod}
     * @param socketTimeout socket连接超时时间
     * @param connectTimeout http连接超时时间
     * @param readTimeout http读取超时时间
     * @param url 请求的url地址
     * @param params 请求参数 string形式的键值对
     * @return 响应的内容体
     * @throws Exception 异常
     */
    public static String execute(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url,
                                 Map<String, String> params, Map<String, String> headers) throws Exception {
        return execute(method, socketTimeout, connectTimeout, readTimeout, url, params, headers, BODY_TYPE.PARAM);
    }

    private static String execute(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url, Object body,
                                  Map<String, String> headers, BODY_TYPE bodyType) throws Exception {
        return execute(method, socketTimeout, connectTimeout, readTimeout, url, body, headers, bodyType, true);
    }

    private static String execute(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url, Object body,
                                  Map<String, String> headers, BODY_TYPE bodyType, boolean checkStatusCode) throws Exception {
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(url)) {
            throw new Exception("httpclient execute url is empty");
        }
        HttpRequestBase request = null;
        switch (bodyType) {
            case JSON:
                if (body instanceof String) {
                    request = getHttpRequest(method, url, (String) body);
                }
                break;
            default:
                request = getHttpRequest(method, url, (Map<String, String>) body);
        }
        if (request == null) {
            throw new Exception("httpclient illegal body type");
        }
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(readTimeout).build();
        request.setConfig(config);
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                request.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        CloseableHttpResponse response = execute(request, url);
        long end = System.currentTimeMillis();
        if (end - start > 500) {
            log.info(String.format("httpclient execute,url=%1$s,time=%2$d", url, (end - start)));
        }
        return parseResponse(null, response, url, checkStatusCode);
    }

    public static String parseResponse(CloseableHttpClient client, CloseableHttpResponse response, String url)
            throws Exception {
        return parseResponse(client, response, url, true);
    }

    private static String parseResponse(CloseableHttpClient client, CloseableHttpResponse response, String url,
                                        boolean checkStatusCode) throws Exception {
        StatusLine status = response.getStatusLine();
        try {
            HttpEntity entity = response.getEntity();
            if (checkStatusCode && status.getStatusCode() != 200 && status.getStatusCode() != 201) {
                log.error("http error,url=" + url + ",resp =" + EntityUtils.toString(entity, DEFAULT_CHARSET) + ", statusCode =" + status.getStatusCode());
                return null;
            } else {
                return EntityUtils.toString(entity, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            log.warn(String.format("http exception occured, url: %s, errMsg: %s", url, e.getMessage()));
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
            response.close();
        }
    }

    public static CloseableHttpResponse execute(HttpRequestBase request, String url) throws Exception {
        CloseableHttpResponse response;
        CloseableHttpClient hc = httpClient;
        try {
            response = hc.execute(request);
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException) {
                HttpHostConnectException httpHostConnectException = (HttpHostConnectException) e;
                connectExceptionAlarm(httpHostConnectException);
            } else {
                log.warn(String.format("http exception occured, url: %s, errMsg: %s", url, e.getMessage()));
            }
            if (request != null) {
                // 出错时释放连接
                log.info("error in executing http request, will release connection. url: " + url);
                request.releaseConnection();
            }
            throw e;
        }
        return response;
    }

    public static boolean isExists(String url) {
        return isExists(HttpMethod.GET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, null);
    }

    public static boolean isExists(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url,
                                   Map<String, String> params) {
        HttpRequestBase request;
        CloseableHttpResponse response = null;
        try {
            long start = System.currentTimeMillis();
            if (StringUtils.isEmpty(url)) {
                return false;
            }
            request = getHttpRequest(method, url, params);
            RequestConfig config =
                    RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
                            .setConnectionRequestTimeout(readTimeout).build();
            request.setConfig(config);
            request.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
            CloseableHttpClient hc = httpClient;
            response = hc.execute(request);
            long end = System.currentTimeMillis();
            if (end - start > 500) {
                log.info(String.format("httpclient execute,url=%1$s,time=%2$d", url, (end - start)));
            }
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (status.getStatusCode() == 200 || status.getStatusCode() == 201) {
                return true;
            } else {
                log.error("http error,url=" + url + ",resp =" + EntityUtils.toString(entity, DEFAULT_CHARSET));
                return false;
            }
        } catch (Exception e) {
            log.warn(String.format("http exception occured, url: %s, errMsg: %s", url, e.getMessage()));
            return false;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                log.warn("close resource failed", ex);
            }
        }
    }

    public static HttpRequestBase getHttpRequest(HttpMethod method, String url, Map<String, String> params) throws Exception {
        HttpRequestBase request;
        if (method == HttpMethod.POST) {
            HttpPost httpPost = new HttpPost(url);
            if (!CollectionUtils.isEmpty(params)) { // 添加请求参数
                List<NameValuePair> list = new ArrayList<>(params.size());
                for (Map.Entry<String, String> ent : params.entrySet()) {
                    if (StringUtils.isBlank(ent.getKey())) {
                        try {
                            httpPost.setEntity(new StringEntity(ent.getValue(), "utf-8"));
                        } catch (Exception e) {
                            log.warn("excute, httpPost.setEntity failed caused by " + e.getMessage());
                        }
                    } else {
                        list.add(new BasicNameValuePair(ent.getKey(), ent.getValue()));
                    }
                }
                if (!list.isEmpty()) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, DEFAULT_CHARSET);
                    httpPost.setEntity(entity);
                }
            }
            request = httpPost;
        } else {
            request = new HttpGet(appendParams(url, params));
        }
        return request;
    }

    private static HttpRequestBase getHttpRequest(HttpMethod method, String url, String body) {
        HttpRequestBase request;
        StringEntity entity = new StringEntity(body, DEFAULT_CHARSET);
        entity.setContentType(JSON_CONTENT_TYPE);
        switch (method) {
            case PUT:
                HttpPut httpPut = new HttpPut(url);
                httpPut.setEntity(entity);
                request = httpPut;
                break;
            default:
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(entity);
                request = httpPost;
        }
        return request;
    }

    public static boolean exists(String URLName, String method) {
        HttpURLConnection con = null;
        try {
            // 设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(true);
            // 到 URL 所引用的远程对象的连接
            con = (HttpURLConnection) new URL(URLName).openConnection();
            /* 设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。 */
            if ("post".equalsIgnoreCase(method)) {
                // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
                con.setDoOutput(true);
                // 设置连接输入流为true
                con.setDoInput(true);
            }
            // 从 HTTP 响应消息获取状态码
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            log.error("exists url error" + URLName, e);
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static boolean exists(String URLName) {
        return exists(URLName, "GET") || isExists(URLName);
    }

    static String appendParams(String url, Map<String, String> params) throws Exception {
        return appendParams(null, url, params);
    }

    static String appendParams(String charset, String url, Map<String, String> params) throws Exception {
        if (CollectionUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        for (Map.Entry<String, String> ent : params.entrySet()) {
            if (StringUtils.isNotBlank(ent.getValue())) {
                if (sb.indexOf("?") == -1) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                if (StringUtils.isEmpty(charset)) {
                    charset = DEFAULT_CHARSET.name();
                }
                sb.append(ent.getKey()).append("=").append(URLEncoder.encode(ent.getValue(), charset));
            }
        }
        return sb.toString();
    }

    public static void connectExceptionAlarm(HttpHostConnectException e) {
        long curr = System.currentTimeMillis();
        HttpHost host = e.getHost();
        if (hostAliamMap.containsKey(host)) {
            if (curr - hostAliamMap.get(host) < TIME) {
                return;
            }
        }
        hostAliamMap.put(host, curr);
        log.error("http connection error " + host.toHostString(), e);
    }

    public static CloseableHttpClient getHttpClient(Charset charset) {
        ConnectionConfig config = ConnectionConfig.custom().setCharset(charset).build();
        return HttpClients.custom().setDefaultConnectionConfig(config).build();
    }

    public static CloseableHttpClient getHttpClientWithRetry(Charset charset) {
        ConnectionConfig config;
        if (charset != null) {
            config = ConnectionConfig.custom().setCharset(charset).build();
        } else {
            config = ConnectionConfig.custom().build();
        }
        return HttpClients.custom().setDefaultConnectionConfig(config).setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))// 默认重试三次
                .build();
    }

    public static CloseableHttpClient getSslHttpClientWithRetry(Charset charset) {
        try {
            ConnectionConfig config = ConnectionConfig.custom().setCharset(charset).build();
            return HttpClients.custom().setDefaultConnectionConfig(config).setSSLSocketFactory(buildSSLConnectionFactory()).
                    setRetryHandler(new DefaultHttpRequestRetryHandler(3, false)).build();
        } catch (Exception e) {
            log.warn("getSslHttpClient failed", e);
        }
        return null;
    }

    /**
     * 不用连接池的http请求post. http读取超时时间
     *
     * @param url 请求的url地址
     * @param params 请求参数 string形式的键值对
     * @return 响应的内容体
     * @throws Exception 异常
     */
    public static String postNoPool(Charset charset, String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient;
        if (url.startsWith(HTTPS_PREFIX)) {
            httpclient = getSslHttpClient(charset);
        } else {
            httpclient = getHttpClient(charset);
        }
        HttpPost httpPost = new HttpPost(url);
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> nvps = params.entrySet().stream().map(ent -> new BasicNameValuePair(ent.getKey(), ent.getValue()))
                    .collect(Collectors.toList());
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, charset);
            httpPost.setEntity(entity);
        }
        CloseableHttpResponse response = null;
        if (httpclient != null) {
            response = httpclient.execute(httpPost);
        }
        return parseResponse(httpclient, response, url);
    }

    /**
     * 不用连接池的http请求get
     *
     * @param charset
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String getNoPool(Charset charset, String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient;
        if (url.startsWith(HTTPS_PREFIX)) {
            httpclient = getSslHttpClient(charset);
        } else {
            httpclient = getHttpClient(charset);
        }
        HttpGet httpPost = new HttpGet(appendParams(charset.name(), url, params));
        CloseableHttpResponse response = null;
        if (httpclient != null) {
            response = httpclient.execute(httpPost);
        }
        if (response == null) {
            return null;
        }
        try {
            if (200 == response.getStatusLine().getStatusCode() || 201 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, charset);
            } else {
                log.error(String.format("httpget,url=%1$s,status=%2$d,msg=%3$s", appendParams(url, params), response.getStatusLine()
                        .getStatusCode(), response.getStatusLine().getReasonPhrase()));
            }
        } catch (Exception e) {
            log.warn(String.format("failed to http get without pool, url: %s, params: %s", url, params), e);
        } finally {
            response.close();
            httpclient.close();
        }
        return null;
    }

    public static String getURL(String url) throws Exception {
        HttpGet httpget=new HttpGet(url);
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,**/*//*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
        BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build());
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.custom().setConnectionManager(cm).build();
            httpclient= HttpClients.custom().setUserAgent(userAgent).build();
            response = httpClient.execute(httpget);
            if (200 == response.getStatusLine().getStatusCode() || 201 == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, DEFAULT_CHARSET);
            } else {
                String format = String.format("httppost,status=%1$d,msg=%2$s", response.getStatusLine().getStatusCode(), response.getStatusLine()
                        .getReasonPhrase());
                if(404 == response.getStatusLine().getStatusCode()){
                    return "404";
                }
                log.error(format);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
            cm.shutdown();
            cm.close();
        }
        return null;
    }

    public static InputStream getStream(String url) throws Exception {
        BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build());
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet get = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        get.setConfig(config);
        CloseableHttpResponse response = httpclient.execute(get);
        return response.getEntity().getContent();
    }

    public static InputStream getDefaultStream(String url) throws Exception {
        BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build());
        HttpGet get = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        get.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(get);
        return response.getEntity().getContent();
    }

    public static InputStream getStream(String url, Map<String,String> params, Map<String, String> headers) throws Exception {
        BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
        cm.setConnectionConfig(ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build());
        CloseableHttpClient myHttpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpGet get = new HttpGet(appendParams(url, params));
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        get.setConfig(config);
        if(headers != null){
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                get.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        CloseableHttpResponse response = myHttpClient.execute(get);
        return response.getEntity().getContent();
    }

    public static String getLocalFileForImg(String imgUrl, String localFileName, int timeoutRate) throws Exception {
        HttpGet get;
        try {
            get = new HttpGet(imgUrl);
        } catch (Exception ex1) {
            URL urlObj = new URL(imgUrl);
            URI uri = new URI(urlObj.getProtocol(), urlObj.getHost(), urlObj.getPath(), urlObj.getQuery(), null);
            get = new HttpGet(uri);
        }
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient;
        if (imgUrl.startsWith(HTTPS_PREFIX)) {
            httpclient = getSslHttpClientWithRetryHandler(DEFAULT_CHARSET);
        } else {
            httpclient = getHttpClientWithRetry(DEFAULT_CHARSET);
        }
        InputStream input = null;
        FileOutputStream output = null;
        RequestConfig config;
        try {
            config = RequestConfig.custom().setSocketTimeout(timeoutRate * DEFAULT_SOCKET_TIMEOUT)
                    .setConnectTimeout(timeoutRate * DEFAULT_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(timeoutRate * DEFAULT_READ_TIMEOUT).build();
            get.setConfig(config);
            if (httpclient != null) {
                response = httpclient.execute(get);
            }
            if (response != null && (response.getStatusLine().getStatusCode() == 200 || response.getStatusLine().getStatusCode() == 201)) {
                input = response.getEntity().getContent();
                byte[] imgBuffer = new byte[4096];
                if (input != null) {
                    int len;
                    int off = 0;
                    output = new FileOutputStream(localFileName);
                    IOUtils.copy(input, output);
                    while ((len = input.read(imgBuffer)) != -1) {
                        output.write(imgBuffer, off, len);
                    }
                }
                return localFileName;
            } else {
                log.warn("getLocalFileForImg failed:" + (response == null ? null : response.getStatusLine().getStatusCode()) + ":" + imgUrl);
            }
            return null;
        } catch (SSLHandshakeException ex) {
            log.warn("getLocalFileForImg " + ex.getMessage() + ", imgUrl:" + imgUrl);
        } catch (SocketTimeoutException ex) {
            log.warn("getLocalFileForImg Time out:" + imgUrl, ex);
        } catch (Exception ex) {
            log.error("getLocalFileForImg failed:" + imgUrl, ex);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return null;
    }

    /**
     * post 数据到指定地址，并获取返回结果. Multipart
     *
     * @throws IOException
     */
    public static String postContentBySsl(String url, String paraStr, String encoding) throws IOException {
        CloseableHttpClient httpClient = getSslHttpClient(Charset.forName(encoding));
        // 重试
        // 超时
        RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).build();
        String res = null;
        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(config);
        HttpEntity reqEntity = new StringEntity(paraStr, encoding);
        httppost.setEntity(reqEntity);
        httppost.setHeader("Content-Type", " application/x-www-form-urlencoded");
        CloseableHttpResponse response = null;
        if (httpClient != null) {
            response = httpClient.execute(httppost);
        }
        if (response == null) {
            return null;
        }
        try {
            res = EntityUtils.toString(response.getEntity(), encoding);
        } finally {
            response.close();
            httpClient.close();
        }
        return res;
    }

    public static File getImage(String url) {
        File path = new File(DEFAULT_IMAGE_PATH);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }
        if (StringUtils.isEmpty(url) || !url.trim().startsWith("http")) {
            return null;
        }
        HttpGet httpGet = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        httpGet.setConfig(config);
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            log.error("Get image failed. url:" + url, e);
            return null;
        }
        if (response == null) {
            log.error("Get image response is null. url:" + url);
            return null;
        }
        FileOutputStream outputStream = null;
        try {
            HttpEntity entity = response.getEntity();
            BufferedInputStream inputStream = new BufferedInputStream(entity.getContent());
            String imageName = url.substring(url.lastIndexOf("/"));
            if (imageName.contains("?")) {
                imageName = imageName.substring(0, imageName.indexOf("?"));
            }
            File file = new File(DEFAULT_IMAGE_PATH + imageName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            return file;
        } catch (Exception e) {
            log.error("Get image error. url:" + url, e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                response.getEntity().getContent().close();
            } catch (Exception ex) {
                log.warn("close resource failed", ex);
            }
        }
    }

    public static File getImageRubbishPic(String picUrl) {
        File path = new File(RUBBISH_IMAGE_PATH);
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }
        if (StringUtils.isEmpty(picUrl) || !picUrl.trim().startsWith("http")) {
            return null;
        }
        HttpGet httpGet = new HttpGet(picUrl);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        httpGet.setConfig(config);
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            log.error("Get rubbishimage failed. url:" + picUrl, e);
            return null;
        }
        if (response == null) {
            log.error("Get rubbishimage response is null. url:" + picUrl);
            return null;
        }
        FileOutputStream outputStream = null;
        try {
            HttpEntity entity = response.getEntity();
            BufferedInputStream inputStream = new BufferedInputStream(entity.getContent());
            String imageName = picUrl.substring(picUrl.lastIndexOf("/"));
            if (imageName.contains("?")) {
                imageName = imageName.substring(0, imageName.indexOf("?"));
            }
            File file = new File(RUBBISH_IMAGE_PATH + imageName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            return file;
        } catch (Exception e) {
            log.error("Get image error. url:" + picUrl, e);
            return null;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                response.getEntity().getContent().close();
            } catch (Exception ex) {
                log.warn("close resource failed", ex);
            }
        }
    }

    public static Map<String, String> getCookieHeader(List<String[]> keyValues) {
        Map<String, String> headers = new HashMap<>();
        StringBuilder cookieValueBuilder = new StringBuilder();
        for (String[] kv : keyValues) {
            if (kv == null || kv.length != 2) {
                throw new IllegalArgumentException(String.format("illegal key value format: %s", kv));
            }
            cookieValueBuilder.append(kv[0]).append('=').append(kv[1]).append(';');
        }
        headers.put("Cookie", cookieValueBuilder.toString());
        return headers;
    }

    public static String postWithRawBody(String url, String body) throws Exception{
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(body, DEFAULT_CHARSET);
        httpPost.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).
                setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).
                setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException) {
                HttpHostConnectException hostConnectException = (HttpHostConnectException) e;
                log.error(hostConnectException.getHost().toHostString(), e);
            }
            throw e;
        }
        StatusLine statusLine = response.getStatusLine();
        String responseEntity = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        if (statusLine.getStatusCode() != 200) {
            log.error("http status error, url = " + url + ", status code = " + statusLine.getStatusCode() +", resp = " +
                    responseEntity);
            return null;
        }
        return responseEntity;
    }

    /**
     * 重试3次 GET
     * @param charset
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String getWithRetry(Charset charset, String url, Map<String, String> params) throws Exception{
        HttpGet httpGet = new HttpGet(appendParams(charset.name(), url, params));
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).
                setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).
                setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = null;
            if (url.startsWith(HTTPS_PREFIX)) {
                httpClient = getSslHttpClientWithRetry(charset);
            } else {
                httpClient = getHttpClientWithRetry(charset);
            }
            if (httpClient != null) {
                response = httpClient.execute(httpGet);
            }
            StatusLine statusLine = response.getStatusLine();
            String responeEntity = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            if (statusLine.getStatusCode() != 200) {
                log.error("get retry http status error, url = " + url + ", status code = " + statusLine.getStatusCode() +
                        ", resp = " + responeEntity);
                return null;
            }
            return responeEntity;
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException) {
                HttpHostConnectException hostConnectException = (HttpHostConnectException) e;
                log.error(hostConnectException.getHost().toHostString(), e);
            }
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }

    }

    /**
     * 解决SSL CertificateException异常 而写
     * httpclient 设置为信任所有证书
     *
     * @return 返回SSL socket工厂
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLConnectionSocketFactory buildSSLConnectionFactory() throws KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    }

    /**
     * 解决连接池ssl验证异常的问题
     *
     * @return PoolingHttpClientConnectionManager
     */
    private static PoolingHttpClientConnectionManager buildConnectionManager() {
        SSLConnectionSocketFactory reuseStrategy = null;
        try {
            reuseStrategy = buildSSLConnectionFactory();
        } catch (Exception e) {
            log.error("create ssl SSLConnectionSocketFactory error", e);
        }
        if (Objects.isNull(reuseStrategy)) {
            return new PoolingHttpClientConnectionManager();
        } else {
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP_PREFIX, PlainConnectionSocketFactory.getSocketFactory())
                    .register(HTTPS_PREFIX, reuseStrategy).build();
            return new PoolingHttpClientConnectionManager(registry);
        }
    }

}