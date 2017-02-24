package com.lsh.base.common.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Properties;
import java.util.regex.Matcher;

public abstract class AbsPropertiesConfig implements ItfConfig {

    protected static final Properties EMPTY_PROPERTIES = new Properties();

    protected Properties properties;

    public String getString(String key) {
        return getAndProcessValue(key);
    }

    private String getAndProcessValue(String key) {
        String _value = properties.getProperty(key);
        if (StringUtils.isBlank(_value)) {
            return StringUtils.EMPTY;
        }
        StringBuffer result = new StringBuffer();
        Matcher matcher = ItfConfig.VALUE_RESOLVER_PATTERN.matcher(_value);
        String resolveKey = null;
        String matchStr = null;
        while (matcher.find()) {
            matchStr = matcher.group();
            resolveKey = org.apache.commons.lang3.StringUtils.substringBetween(matchStr, "${", "}");
            matcher.appendReplacement(result, properties.getProperty(resolveKey));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public String[] getStringArray(String key) {
        String value = getAndProcessValue(key);
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    public int getInt(String key) {
        return NumberUtils.toInt(getAndProcessValue(key), -1);
    }

    public long getLong(String key) {
        return NumberUtils.toLong(getAndProcessValue(key), -1L);
    }

    public float getFloat(String key) {
        return NumberUtils.toFloat(getAndProcessValue(key), -1F);
    }

    public double getDouble(String key) {
        return NumberUtils.toDouble(getAndProcessValue(key), -1D);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

}
