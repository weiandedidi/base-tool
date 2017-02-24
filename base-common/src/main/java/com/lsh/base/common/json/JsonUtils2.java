package com.lsh.base.common.json;

import com.lsh.base.common.utils.DateUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils2 {

    /**
     * 正常
     */
    public static final Integer SUCCESS = 1;
    /**
     * 参数无效
     */
    public static final Integer PARAM_INVALID = 2;
    /**
     * 系统异常
     */
    public static final Integer EXCEPTION = 3;
    /**
     * 业务异常
     */
    public static final Integer BIZ_EXCEPTION = 4;
    /**
     * 超时异常
     */
    public static final Integer TOKEN_EXCEPTION = 5;
    /**
     * 其他异常
     */
    public static final Integer OTHER_EXCEPTION = 6;


    private static JsonMapper jsonMapper = new JsonMapper();

    public static JsonMapper getJsonMapper() {
        return jsonMapper;
    }

    public static String obj2Json(Object obj) {
        return jsonMapper.toJson(obj);
    }

    public static <T> T json2Obj(String json, Class<T> clazz) {
        return jsonMapper.fromJson(json, clazz);
    }

    public static String formatJson(String json) {
        return jsonMapper.formatJson(json);
    }

    public static String SUCCESS() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", SUCCESS);
        head.put("msg", "success.");
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String SUCCESS(Object data) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", SUCCESS);
        head.put("msg", "success.");
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        map.put("body", data);
        return obj2Json(map);
    }

    public static String PARAMETER_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", PARAM_INVALID);
        head.put("msg", "parameter invalid.");
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String PARAMETER_ERROR(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", PARAM_INVALID);
        head.put("msg", message);
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }
    
    public static String EXCEPTION_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", EXCEPTION);
        head.put("msg", "system exception.");
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String EXCEPTION_ERROR(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", EXCEPTION);
        head.put("msg", message);
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String TOKEN_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", TOKEN_EXCEPTION);
        head.put("msg", "system exception.");
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String TOKEN_ERROR(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", TOKEN_EXCEPTION);
        head.put("msg", message);
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String BIZ_ERROR(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", BIZ_EXCEPTION);
        head.put("msg", message);
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }


    public static String OTHER_EXCEPTION(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", OTHER_EXCEPTION);
        head.put("msg", message);
        head.put("timestamp", new Date().getTime()/1000);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }
}
