package com.atlantis.zeus.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
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
import org.apache.http.conn.HttpClientConnectionManager;
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
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
public class HttpClientUtils {
    /**
     * 默认的编码方式
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    /**
     * 默认的图片下载存储地址
     */
    private static final String DEFAULT_IMAGE_PATH = "/home/appops/images/";
    private static final String HTTPS_PREFIX = "https";
    public static final String HTTP_PREFIX = "http";
    /**
     * 默认的connect timeout 10s
     */
    private static final int DEFAULT_CONNECT_TIMEOUT = 1000 * 10;
    /**
     * 默认的read timeout 10s 从连接池获取连接的超时时间
     */
    private static final int DEFAULT_READ_TIMEOUT = 5000;
    /**
     * 默认的socket timeout 10s
     */
    private static final int DEFAULT_SOCKET_TIMEOUT = 1000 * 10;
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

    private enum BODY_TYPE {
        PARAM, JSON
    }

    // 初始化连接管理池
    static {
        PoolingHttpClientConnectionManager cm = buildConnectionManager();
        // socket 配置
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(DEFAULT_SOCKET_TIMEOUT).build();
        cm.setDefaultSocketConfig(socketConfig);
        // 最大连接数
        cm.setMaxTotal(200);
        // 同路由的并发数, 默认值2，限制数量实际使用 DefaultMaxPerRoute并非MaxTotal，设置过小无法支持大并发
        cm.setDefaultMaxPerRoute(100);
        // CLOSE_WAIT，50ms内没有被使用时验证有效
        cm.setValidateAfterInactivity(50);
        // 清理过期连接和空闲连接
        IdleConnectionMonitorThread idleConnectionMonitorThread = new IdleConnectionMonitorThread(cm);
        idleConnectionMonitorThread.setName("HttpClient-idle");
        idleConnectionMonitorThread.start();
        // HTTP conn 配置
        ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setCharset(DEFAULT_CHARSET).build();
        cm.setDefaultConnectionConfig(defaultConnectionConfig);

        httpClient = HttpClients.custom().setConnectionManager(cm).build();
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
     * 创建一个连接池管理器
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

    /**
     * 监视器线程：驱除空闲连接
     */
    public static class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        /**
         * 用于控制是否中断 监视器线程
         */
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            log.debug("Inside IdleConnectionMonitorThread");
            this.connMgr = connMgr;
            log.debug("Connection manager assigned");
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        // 每 50ms 清理一次
                        wait(50);
                        // 关闭过期连接
                        connMgr.closeExpiredConnections();
                        // 关闭指定时间内，所有空闲连接
                        connMgr.closeIdleConnections(1000, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
                log.debug("Monitor Thread interrupted!");
            }
        }

        /**
         * 优雅关闭
         */
        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
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

    public static String execute(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url,
                                 Map<String, String> params) throws Exception {
        return execute(method, socketTimeout, connectTimeout, readTimeout, url, params, null);
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
     * @param headers 请求的 header
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
                .setConnectionRequestTimeout(readTimeout).setProxy(null).build();
        request.setConfig(config);
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                request.setHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        CloseableHttpResponse response = execute(request, url);
        return parseResponse(null, response, url, checkStatusCode);
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

    /**
     * 请求超时记录报警
     *
     * @param e
     */
    public static void connectExceptionAlarm(HttpHostConnectException e) {
        long curr = System.currentTimeMillis();
        HttpHost host = e.getHost();
        if (hostAliamMap.containsKey(host)) {
            if (curr - hostAliamMap.get(host) < TIME) {
                return;
            }
        }
        hostAliamMap.put(host, curr);
        log.debug("http connection error " + host.toHostString(), e);
    }

    public static HttpRequestBase getHttpRequest(HttpMethod method, String url, Map<String, String> params) throws Exception {
        HttpRequestBase request;
        if (method == HttpMethod.POST) {
            HttpPost httpPost = new HttpPost(url);
            if (!CollectionUtils.isEmpty(params)) {
                // 添加请求参数
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
                log.error("http error, url=" + url + ",resp =" + EntityUtils.toString(entity, DEFAULT_CHARSET)
                        + ", statusCode =" + status.getStatusCode());
                return EntityUtils.toString(entity, DEFAULT_CHARSET);
            } else {
                return EntityUtils.toString(entity, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            log.warn(String.format("http exception occured, url: %s, errMsg: %s", url, e.getMessage()));
            throw e;
        } finally {
            if (Objects.nonNull(client)) {
                client.close();
            }
            response.close();
        }
    }

    /**
     * 解析响应cookie
     *
     * @param response
     * @return
     */
    private static List<HttpCookie> parseCookie(CloseableHttpResponse response) {
        // cookie 处理
        List<HttpCookie> cookies = new ArrayList<>();
        Header[] arr = response.getHeaders("set-cookie");
        for (Header header : arr) {
            cookies.addAll(HttpCookie.parse(header.getValue()));
        }

        return cookies;
    }

    /**
     * 获取输入流
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static InputStream getStream(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_READ_TIMEOUT).build();
        get.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(get);
        return response.getEntity().getContent();
    }

    /**
     * 下载图片到指定位置
     *
     * @param url imgUrl
     * @return
     */
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
        RequestConfig config = RequestConfig.custom().setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
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


    /**
     * 判断一个 url 是否存在
     * @param URLName
     * @return
     */
    public static boolean exists(String URLName) {
        return exists(URLName, "GET") || isExists(URLName);
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

    public static boolean isExists(String url) {
        return isExists(HttpMethod.GET, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, url, null);
    }

    public static boolean isExists(HttpMethod method, int socketTimeout, int connectTimeout, int readTimeout, String url,
                                   Map<String, String> params) {
        HttpRequestBase request;
        CloseableHttpResponse response = null;
        try {
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



    public static CloseableHttpClient getHttpClient(Charset charset) {
        ConnectionConfig config = ConnectionConfig.custom().setCharset(charset).build();
        return HttpClients.custom().setDefaultConnectionConfig(config).build();
    }

    /**
     * 带有重试机制的HttpClient （3次）
     *
     * @param charset
     * @return
     */
    public static CloseableHttpClient getHttpClientWithRetry(Charset charset) {
        ConnectionConfig config;
        if (charset != null) {
            config = ConnectionConfig.custom().setCharset(charset).build();
        } else {
            config = ConnectionConfig.custom().build();
        }
        return HttpClients.custom().setDefaultConnectionConfig(config)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))
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

        return parseResponse(httpclient, response, url);
    }

    /**
     * 不用连接池的HTTP 请求获取输入流
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static InputStream getStreamNoPool(String url) throws Exception {
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

    public static InputStream getStreamNoPool(String url, Map<String,String> params, Map<String, String> headers)
            throws Exception {
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

}