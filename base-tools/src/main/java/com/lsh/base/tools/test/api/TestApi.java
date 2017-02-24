package com.lsh.base.tools.test.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.net.HttpClientUtils;
import com.lsh.base.common.utils.ClassLoaderUtils;
import com.lsh.base.common.utils.EncodeUtils;
import com.lsh.base.common.utils.ObjUtils;
import com.lsh.base.common.utils.StrUtils;
import com.lsh.base.tools.model.api.Api;
import com.lsh.base.tools.model.api.ApiData;
import com.lsh.base.tools.model.api.BodyParam;
import com.lsh.base.tools.model.api.HeadParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TestApi {

    private static final Logger logger = LoggerFactory.getLogger(TestApi.class);

    public void testApi(String input, String output) {
        StringBuffer rs = new StringBuffer();
        try {
            Api api = null;
            logger.info("输入文件：{}", input);
            input = ClassLoaderUtils.getPath(input);
            logger.info("输入文件真实地址：{}", input);
            if (input.endsWith(".xml")) {
                api = parseXml(input);
            } else if (input.endsWith(".json")) {
                api = parseJson(input);
            }
            if (api == null) {
                rs.append("api is null.");
                return;
            }
            if (StringUtils.isBlank(api.getUrl())) {
                rs.append("url is null.");
                return;
            }
            List<ApiData> dataList = api.getData();
            if (dataList == null || dataList.isEmpty()) {
                rs.append("data is empty");
                return;
            }
            for (ApiData data : dataList) {
                testApiData(api, data, rs);
            }
        } finally {
            try {
                int index1 = input.lastIndexOf("/");
                int index2 = input.lastIndexOf(".");
                String name = StringUtils.substring(input, index1 + 1, index2);
                File outFile = new File(output, name + ".txt");
                logger.info("输出文件：{}", outFile.getAbsolutePath());
                FileUtils.writeStringToFile(outFile, rs.toString());
            } catch (IOException e) {
                logger.error("输出文件异常：", e);
            }
        }
    }

    public Api parseJson(String input) {
        try {
            Api api = JsonUtils.getJsonMapper().getMapper().readValue(new File(input), Api.class);
            return api;
        } catch (Exception e) {
            logger.error("解析输入文件异常：", e);
        }
        return null;
    }

    public Api parseXml(String input) {
        try {
            File file = new File(input);
            Api api = new Api();
            SAXReader sr = new SAXReader();
            Document doc = sr.read(file);
            Element rootElement = doc.getRootElement();
            Element url = rootElement.element("url");
            if (url != null) {
                api.setUrl(url.getTextTrim());
                api.setName(url.attributeValue("name"));
                api.setEncrypt(ObjUtils.toBoolean(url.attributeValue("encrypt"), false));
            }
            Element dataAry = rootElement.element("dataAry");
            if (dataAry == null) {
                return api;
            }
            List<Element> dataList = dataAry.elements("data");
            if (dataList == null || dataList.isEmpty()) {
                return api;
            }
            List<ApiData> apiDataList = Lists.newArrayList();
            for (Element data : dataList) {
                ApiData apiData = new ApiData();
                apiData.setDesc(data.attributeValue("desc"));
                apiData.setMethod(data.attributeValue("method"));
                apiData.setEffective(ObjUtils.toBoolean(data.attributeValue("effective"), true));
                List<HeadParam> headParamList = Lists.newArrayList();
                Element headAry = data.element("headAry");
                if (headAry != null) {
                    List<Element> headList = headAry.elements("head");
                    if (headList != null) {
                        for (Element head : headList) {
                            HeadParam headParam = new HeadParam();
                            headParam.setKey(head.attributeValue("key"));
                            headParam.setValue(head.attributeValue("value"));
                            headParamList.add(headParam);
                        }
                    }
                }
                apiData.setHead(headParamList);
                List<BodyParam> bodyParamList = Lists.newArrayList();
                Element bodyAry = data.element("bodyAry");
                if (bodyAry != null) {
                    List<Element> bodyList = bodyAry.elements("body");
                    if (bodyList != null) {
                        for (Element body : bodyList) {
                            BodyParam bodyParam = new BodyParam();
                            bodyParam.setType(ObjUtils.toInteger(body.attributeValue("type", "1")));
                            bodyParam.setKey(body.attributeValue("key"));
                            bodyParam.setValue(body.attributeValue("value"));
                            bodyParamList.add(bodyParam);
                        }
                    }
                }
                apiData.setBody(bodyParamList);
                Element bodystr = data.element("bodystr");
                if (bodystr != null) {
                    apiData.setBodystr(bodystr.getTextTrim());
                }
                apiDataList.add(apiData);
            }
            api.setData(apiDataList);
            return api;
        } catch (Exception e) {
            logger.error("解析输入文件异常：", e);
        }
        return null;
    }

    public void testApiData(Api api, ApiData apiData, StringBuffer rs) {
        String desc = ObjUtils.ifNull(apiData.getDesc(), "");
        String method = apiData.getMethod();
        if (!apiData.isEffective()) {
            logger.info("=====忽略请求:{}={}=====", api.getName(), desc);
            return;
        }
        logger.info("=====开始请求:{}={}=====", api.getName(), desc);
        // 组织头参数
        Map<String, String> headMap = Maps.newHashMap();
        List<HeadParam> headList = apiData.getHead();
        if (headList != null && !headList.isEmpty()) {
            for (HeadParam head : headList) {
                if (StringUtils.isBlank(head.getKey()) || StringUtils.isBlank(head.getValue())) {
                    continue;
                }
                headMap.put(head.getKey(), head.getValue());
            }
        }
        // 如果要求加密，自动计算sign，加到头参数里
        if (api.isEncrypt()) {
            headMap.put("sign", getEncrypt(apiData.getBody()));
        }
        // 组织参数
        Map<String, Object> paramMap1 = Maps.newHashMap();
        Map<String, String> paramMap2 = Maps.newHashMap();
        Map<String, String> paramMap3 = Maps.newHashMap();
        boolean formdata = false; // 是否表单（含文件）请求，默认为否，当参数中有类型为文件时改为是
        List<BodyParam> bodyList = apiData.getBody();
        if (bodyList != null && !bodyList.isEmpty()) {
            for (BodyParam param : bodyList) {
                if (StringUtils.isBlank(param.getKey()) || StringUtils.isBlank(param.getValue())) {
                    continue;
                }
                //文件类型
                if (param.getType() == 2) {
                    formdata = true;
                    paramMap1.put(param.getKey(), new File(param.getValue()));
                } else {
                    paramMap1.put(param.getKey(), StrUtils.toISO88591(param.getValue()));
                    paramMap2.put(param.getKey(), StrUtils.toISO88591(param.getValue()));
                    paramMap3.put(param.getKey(), EncodeUtils.urlEncode(param.getValue()));
                }
            }
        }

        int timeout = 600000;
        String charset = "UTF-8";
        String httpResult = null;
        if ("get".equalsIgnoreCase(method)) {
            // get请求
            StringBuilder url = new StringBuilder(api.getUrl());
            Iterator<Map.Entry<String, String>> iter = paramMap3.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (StringUtils.contains(url, "?")) {
                    url.append("&").append(entry.getKey());
                    url.append("=");
                    url.append(entry.getValue());
                } else {
                    url.append("?").append(entry.getKey());
                    url.append("=");
                    url.append(entry.getValue());
                }
            }
            httpResult = HttpClientUtils.get(url.toString(), timeout, charset, headMap);
        } else if ("post".equalsIgnoreCase(method)) {
            String bodyStr = apiData.getBodystr();
            if (StringUtils.isBlank(bodyStr)) {
                if (formdata) {
                    httpResult = HttpClientUtils.postMultipartForm(api.getUrl(), paramMap1, headMap);
                } else {
                    httpResult = HttpClientUtils.post(api.getUrl(), paramMap2, timeout, charset, headMap);
                }
            } else {
                // 表体post请求
                httpResult = HttpClientUtils.postBody(api.getUrl(), bodyStr, timeout, charset, headMap);
            }
        } else {
            rs.append(desc).append("method非法（只能为get或post）。");
            return;
        }
        if (httpResult == null) {
            rs.append("==========开始请求[").append(desc).append("]，请求失败。==========\n");
            rs.append("==========结束请求[").append(desc).append("]==========\n\n");
            return;
        }
        rs.append("==========开始请求[").append(desc).append("]，请求成功：==========\n");
        rs.append(JsonUtils.formatJson(httpResult));
        rs.append("\n==========结束请求[").append(desc).append("]==========\n\n");
        logger.info("=====结束请求:{}={}=====", api.getName(), desc);
    }

    public String getEncrypt(List<BodyParam> paramList) {
        List<String> strList = new ArrayList<String>();
        if (paramList != null && !paramList.isEmpty()) {
            for (BodyParam param : paramList) {
                if (StringUtils.isBlank(param.getKey())
                        || StringUtils.isBlank(param.getValue())) {
                    continue;
                }
                if (param.getType() == 2) {
                    continue;
                }
                strList.add(param.getKey() + "=" + param.getValue());
            }
        }
        Collections.sort(strList);
        StringBuilder builder = new StringBuilder();
        for (String param : strList) {
            builder.append(param);
        }
        builder.append("UJMpkYFiq4YDMLkEXgqYUltbfWCb7p67");
        String strBeforeEncode = builder.toString();
        logger.info("=====编码前为：{}=====", strBeforeEncode);
        String strAfterEncode = EncodeUtils.urlEncode(strBeforeEncode);
        logger.info("=====编码后为：{}=====", strAfterEncode);
        String sign = EncodeUtils.md5(strAfterEncode);
        logger.info("=====sign为：{}=====", sign);
        return sign;
    }

}
