package com.lsh.base.common.config;

import com.lsh.base.common.utils.StrUtils;
import org.apache.commons.lang3.StringUtils;

public class PropertyUtils {

    private static final ItfConfig cfg = ConfigFactory.getConfig();

    private PropertyUtils() {
    }

    public static String getString(String key) {
        return cfg.getString(key);
    }
    public static String getMessage(String key,Object... args) {

        return StrUtils.formatString(cfg.getString(key),args);
    }

    public static String getString(String key, String defaultVal) {
        String value = cfg.getString(key);
        if (StringUtils.isBlank(value)) {
            return defaultVal;
        }
        return value;
    }

    public static String[] getStringArray(String key) {
        return cfg.getStringArray(key);
    }

    public static int getInt(String key) {
        return cfg.getInt(key);
    }

    public static int getInt(String key, int defaultVal) {
        int value = cfg.getInt(key);
        return value == -1 ? defaultVal : value;
    }

    public static long getLong(String key) {
        return cfg.getLong(key);
    }

    public static long getLong(String key,long defaultVal) {
        return 0== cfg.getLong(key) ?defaultVal: cfg.getLong(key);
    }

    public static double getDouble(String key, double defaultVal) {
        double value = cfg.getDouble(key);
        return value == 0 ? defaultVal : value;
    }

    public static void main(String[] args) {
        System.out.println(getString("test.test"));
    }

}
