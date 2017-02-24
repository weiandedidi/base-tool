package com.lsh.base.common.net;

import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.RandomUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    public static final int DEFAULT_TIMEOUT = 6000;
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String get(String url) {
        return get(url, DEFAULT_TIMEOUT, DEFAULT_CHARSET, null);
    }

    public static String get(String url, int timeout) {
        return get(url, timeout, DEFAULT_CHARSET, null);
    }

    public static String get(String url, Map<String, String> headMap) {
        return get(url, DEFAULT_TIMEOUT, DEFAULT_CHARSET, headMap);
    }

    public static String get(String url, int timeout, String charset, Map<String, String> headMap) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            // 超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).build();
            httpGet.setConfig(requestConfig);
            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpGet);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, ((end - start)));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, charset) : null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
        return null;
    }

    public static byte[] getByte(String url) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpGet);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, ((end - start)));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toByteArray(entity) : null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
        return null;
    }

    public static String post(String url, Map<String, String> params) {
        return post(url, params, DEFAULT_TIMEOUT, DEFAULT_CHARSET, null);
    }

    public static String post(String url, Map<String, String> params, int timeout) {
        return post(url, params, timeout, DEFAULT_CHARSET, null);
    }

    public static String post(String url, Map<String, String> params, Map<String, String> headMap) {
        return post(url, params, DEFAULT_TIMEOUT, DEFAULT_CHARSET, headMap);
    }

    public static String post(String url, Map<String, String> params, int timeout, String charset, Map<String, String> headMap) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            // 超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).build();
            httpPost.setConfig(requestConfig);
            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 参数设置
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpPost);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, (end - start));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, charset) : null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
        return null;
    }


    public static String httpsPost(String url,Map<String,String> map,String charset, Map<String, String> headMap){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            if(headMap != null && !headMap.isEmpty()) {
                Iterator start = headMap.entrySet().iterator();

                while (start.hasNext()) {
                    Map.Entry i$ = (Map.Entry) start.next();
                    httpPost.addHeader((String) i$.getKey(), (String) i$.getValue());
                }
            }
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 包含文件类型的POST提交,参数支持File,InputStream,byte[]和String
     * 第三个参数支持远程文件，先将远程文件下载
     *
     * @param url
     * @param paramMap
     * @param urlParamMap
     * @return
     */
    public static String postMultipartForm(String url,
                                           Map<String, Object> paramMap,
                                           Map<String, String> urlParamMap) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            // 参数设置
            if (paramMap != null && !paramMap.isEmpty()) {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof File) {
                        builder.addBinaryBody(entry.getKey(), (File) value);
                    } else if (value instanceof InputStream) {
                        builder.addBinaryBody(entry.getKey(), (InputStream) value);
                    } else if (value instanceof byte[]) {
                        builder.addBinaryBody(entry.getKey(), (byte[]) value);
                    } else {
                        builder.addTextBody(entry.getKey(), ObjUtils.toString(entry.getValue()));
                    }
                }
            }
            if (urlParamMap != null && !urlParamMap.isEmpty()) {
                for (Map.Entry<String, String> entry : urlParamMap.entrySet()) {
                    builder.addBinaryBody(entry.getKey(), getByte(entry.getValue()));
                }
            }
            httpPost.setEntity(builder.build());
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpPost);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, (end - start));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, DEFAULT_CHARSET) : null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
        return null;
    }

    public static String postBody(String url, String requestBody) {
        return postBody(url, requestBody, DEFAULT_TIMEOUT, DEFAULT_CHARSET, null);
    }

    public static String postBody(String url, String requestBody, int timeout) {
        return postBody(url, requestBody, timeout, DEFAULT_CHARSET, null);
    }

    public static String postBody(String url, String requestBody, int timeout, String charset, Map<String, String> headMap) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            // 超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).build();
            httpPost.setConfig(requestConfig);
            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 请求体设置
            logger.info("requestBody：{}", requestBody);
            if (StringUtils.isNotBlank(requestBody)) {
                httpPost.setEntity(new StringEntity(requestBody, charset));
            }
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpPost);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, ((end - start)));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity, charset) : null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
        return null;
    }

    public static void download(String url, String filePath) {
        download(url, filePath, null);
    }

    public static void download(String url, String filePath, Map<String, String> headMap) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            // head设置
            if (headMap != null && !headMap.isEmpty()) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            httpclient = HttpClients.createDefault();
            long start = System.currentTimeMillis();
            response = httpclient.execute(httpGet);
            long end = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.info("请求url：{}，结果状态：{}，耗时：{}毫秒。", url, status, ((end - start)));
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    File targetFile = new File(filePath);
                    File tmpFile = new File(targetFile.getAbsolutePath() + "." + RandomUtils.uuid2());
                    FileUtils.copyInputStreamToFile(entity.getContent(), tmpFile);
                    tmpFile.renameTo(targetFile);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
            org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpclient);
        }
    }

}
