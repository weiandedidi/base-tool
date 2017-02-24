package com.lsh.base.common.json;

import com.lsh.base.common.config.PropertyUtils;
import com.lsh.base.common.utils.DateUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils {

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
        head.put("message", "success.");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String SUCCESS(Object data) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", SUCCESS);
        head.put("message", "success.");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        map.put("body", data);
        return obj2Json(map);
    }

    public static String PARAMETER_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", PARAM_INVALID);
        head.put("message", "parameter invalid.");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String PARAMETER_ERROR(String code) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :PARAM_INVALID);
        head.put("message", null != PropertyUtils.getString(code) ? PropertyUtils.getString(code)  :"prameter error");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String PARAMETER_ERROR(String code,String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status",null != code && !"".equals(code) ? code  :PARAM_INVALID );
        head.put("message", null != PropertyUtils.getString(code) ? PropertyUtils.getString(code)  :"prameter error");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String FAIL(String code) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :EXCEPTION);
        head.put("message", null != PropertyUtils.getString(code) ? PropertyUtils.getString(code)  :"fail");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String FAIL(String code ,String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :EXCEPTION);
        head.put("message", message);
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String EXCEPTION_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", EXCEPTION);
        head.put("message", "system exception.");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String EXCEPTION_ERROR(String code) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :EXCEPTION);
        head.put("message", null != PropertyUtils.getString(code) ? PropertyUtils.getString(code)  :"exception error");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }


    public static String EXCEPTION_ERROR(String code,String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :EXCEPTION);
        head.put("message", message);
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String TOKEN_ERROR() {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", TOKEN_EXCEPTION);
        head.put("message", "system exception.");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String TOKEN_ERROR(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", TOKEN_EXCEPTION);
        head.put("message", message);
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String BIZ_ERROR(String code) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :BIZ_EXCEPTION);
        head.put("message", null != PropertyUtils.getString(code) ? PropertyUtils.getString(code)  :"biz error");
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }

    public static String BIZ_ERROR(String code,String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", null != code && !"".equals(code) ? code  :BIZ_EXCEPTION);
        head.put("message", message);
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }


    public static String OTHER_EXCEPTION(String message) {
        Map<String, Object> head = new LinkedHashMap<String, Object>();
        head.put("status", OTHER_EXCEPTION);
        head.put("message", message);
        head.put("timestamp", DateUtils.FORMAT_TIME_NO_BAR.format(new Date()));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("head",head);
        return obj2Json(map);
    }
}
