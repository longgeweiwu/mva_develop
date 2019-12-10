package com.itcc.mva.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author whoami
 */

public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 连接池
     */
    private static PoolingHttpClientConnectionManager connManager;
    /**
     * 出错返回结果
     */
    private static final String RESULT = null;


    /**
     * get请求
     *
     * @param url
     * @param param
     * @return
     */
    public static String get(String url, Map<String, Object> param) {
        StringBuilder builder = new StringBuilder();
        try {
            StringBuilder params = new StringBuilder();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                params.append(entry.getKey());
                params.append("=");
                params.append(entry.getValue().toString());
                params.append("&");
            }
            if (params.length() > 0) {
                params.deleteCharAt(params.lastIndexOf("&"));
            }
            URL restServiceURL = new URL(url + (params.length() > 0 ? "?" + params.toString() : ""));
            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : "
                        + httpConnection.getResponseCode());
            }
            InputStream inStrm = httpConnection.getInputStream();
            byte[] b = new byte[1024];
            int length = -1;
            while ((length = inStrm.read(b)) != -1) {
                builder.append(new String(b, 0, length));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    public HttpUtil() {

    }

    /**
     * @param url      地址
     * @param headers  headers
     * @param str      jsonString
     * @param params
     * @param timeOut
     * @param isStream
     * @return
     */
    public static String httpPost(String url, Map<String, Object> headers, String str, Map<String, Object> params, Integer timeOut, boolean isStream) {

        // 创建post请求
        HttpPost httpPost = new HttpPost(url);

        // 添加请求头信息
        if (null != headers) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }

        // 添加请求参数信息
        if (null != params) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), ENCODING));
            } catch (Exception e) {
                e.getMessage();
            }
        }

        if (null != str) {
            httpPost.setEntity(new StringEntity(str, "utf-8"));
        }
        return getResult(httpPost, timeOut, isStream, null, null);

    }


    private static CloseableHttpClient getHttpClient(Integer timeOut, String ip, String port) {
        // 配置请求参数
        RequestConfig requestConfig = null;
        if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(port)) {
            HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut).
                            setConnectTimeout(timeOut).
                            setSocketTimeout(timeOut).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeOut).
                            setConnectTimeout(timeOut).
                            setSocketTimeout(timeOut).
                            build();
        }

        // 配置超时回调机制
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 2) {// 如果已经重试了2次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .build();

        return httpClient;
    }


    public static String getResult(HttpRequestBase httpRequest, Integer timeOut, boolean isStream, String ip, String port) {
        // 响应结果
        StringBuilder sb = null;

        CloseableHttpResponse response = null;

        try {
            // 获取连接客户端
            CloseableHttpClient httpClient = getHttpClient(timeOut, ip, port);
            // 发起请求
            response = httpClient.execute(httpRequest);

            int respCode = response.getStatusLine().getStatusCode();
            // 如果是重定向
            if (302 == respCode) {
                String locationUrl = response.getLastHeader("Location").getValue();
                return getResult(new HttpPost(locationUrl), timeOut, isStream, ip, port);
            }
//            if (401 == respCode) {
//                sb = new StringBuilder();
//                sb.append(response.getLastHeader("WWW-Authenticate").getValue());
//            }
            // 正确响应
            if (200 == respCode || 401 == respCode) {
                // 获得响应实体
                HttpEntity entity = response.getEntity();
                sb = new StringBuilder();

                // 如果是以流的形式获取
                if (isStream) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), ENCODING));
                    String len = "";
                    while ((len = br.readLine()) != null) {
                        sb.append(len);
                    }
                } else {
                    sb.append(EntityUtils.toString(entity, ENCODING));
                    if (sb.length() < 1) {
                        sb.append("-1");
                    }
                }

            }
        } catch (ConnectionPoolTimeoutException e) {
            log.info("从连接池获取连接超时!!!  " + e.toString());
        } catch (SocketTimeoutException e) {
            log.info("响应超时 " + e.toString());
        } catch (ConnectTimeoutException e) {
            log.info("请求超时  " + e.toString());
        } catch (ClientProtocolException e) {
            log.info("http协议错误  " + e.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("不支持的字符编码  " + e.toString());
        } catch (UnsupportedOperationException e) {
            log.info("不支持的请求操作  " + e.toString());
        } catch (ParseException e) {
            log.info("解析错误  " + e.toString());
        } catch (IOException e) {
            log.info("IO错误  " + e.toString());
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.info("关闭响应连接出错  " + e.toString());
                }
            }

        }

        return sb == null ? RESULT : ("".equals(sb.toString().trim()) ? "-1" : sb.toString());

    }


    /**
     * Map转换成NameValuePair List集合
     *
     * @param params map
     * @return NameValuePair List集合
     */
    public static List<NameValuePair> covertParams2NVPS(Map<String, Object> params) {

        List<NameValuePair> paramList = new LinkedList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return paramList;

    }


    /**
     * @ param  : requestUrl
     * @ param  : RequestMethod
     * @ Return : java.lang.String
     * @ Author : lanbvan
     * @ Desc   : TODO 无参请求
     * @ Date   : 2019/6/18 8:36
     */

    public static String httpRequest(String requestUrl, String RequestMethod) {

        HttpURLConnection connection = null;
        BufferedReader br = null;
        String result = null;
        String temp = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            // 打开和URL之间的连接
            connection = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod(RequestMethod);//GET  or  POST
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
            }
            result = sbf.toString();
            connection.disconnect();
        } catch (ConnectionPoolTimeoutException e) {
            log.info("从连接池获取连接超时!!!  " + e.toString());
        } catch (SocketTimeoutException e) {
            log.info("响应超时 " + e.toString());
        } catch (ConnectTimeoutException e) {
            log.info("请求超时  " + e.toString());
        } catch (ClientProtocolException e) {
            log.info("http协议错误  " + e.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("不支持的字符编码  " + e.toString());
        } catch (UnsupportedOperationException e) {
            log.info("不支持的请求操作  " + e.toString());
        } catch (ParseException e) {
            log.info("解析错误  " + e.toString());
        } catch (IOException e) {
            log.info("IO错误  " + e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                log.info("关闭连接失败  " + e.toString());
            }
        }
        return result;
    }

    /**
     * @ param  : requestUrl
     * @ param  : query
     * @ Return : java.lang.String
     * @ Author : lanbvan
     * @ Desc   : TODO 带json参数请求方式
     * @ Date   : 2019/6/18 8:37
     */

    public static String httpRequestJson(String requestUrl, String query) {
        HttpURLConnection connection = null;
        PrintWriter os = null;
        BufferedReader br = null;
        String result = null;
        String temp = null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            // 发送POST请求必须设置如下两行
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            //设置通用的请求属性,注意在传送json数据时， Content-Type的值
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();

            // 获取URLConnection对象对应的输出流
            os = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            os.print(query);
            // flush输出流的缓冲
            os.flush();
            // 定义BufferedReader输入流来读取URL的响应
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
            }
            result = sbf.toString();

            connection.disconnect();
        } catch (ConnectionPoolTimeoutException e) {
            log.info("从连接池获取连接超时!!!  " + e.toString());
        } catch (SocketTimeoutException e) {
            log.info("响应超时 " + e.toString());
        } catch (ConnectTimeoutException e) {
            log.info("请求超时  " + e.toString());
        } catch (ClientProtocolException e) {
            log.info("http协议错误  " + e.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("不支持的字符编码  " + e.toString());
        } catch (UnsupportedOperationException e) {
            log.info("不支持的请求操作  " + e.toString());
        } catch (ParseException e) {
            log.info("解析错误  " + e.toString());
        } catch (IOException e) {
            log.info("IO错误  " + e.toString());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                log.info("关闭连接失败  " + e.toString());
            }

        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);


            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     *      * 将Map<String, String>转换为name=value&name=value形式的字符串
     *      * @param map
     *      * @return
     *      
     */
    public static String getNamValStr(Map<String, String> map) {
        StringBuilder str = new StringBuilder();
        Set<String> set = map.keySet();
        for (String key : set) {
            str.append(key).append("=").append(map.get(key).toString()).append("&");
        }
        return str.toString().substring(0, str.lastIndexOf("&"));
    }

    public static void main(String args[]) {
        List lit = new ArrayList<String>();
        lit.add("asd");
        Map<String, Object> param = new HashMap<>();
        param.put("operationName", "StartContactCenterSession");
        param.put("isForceLogin", "1");
        param.put("channels", lit);
        String namValStr =JSON.toJSONString(param);
        String  s= httpRequestJson("http://172.21.49.47:80" + "/api/v1/me", namValStr);
        System.out.println(namValStr);
        System.out.println(s);
    }
}
