package com.lsh.base.common.config;

import java.util.regex.Pattern;

public interface ItfConfig {

    Pattern VALUE_RESOLVER_PATTERN = Pattern.compile("\\$\\{\\w+\\}", Pattern.CASE_INSENSITIVE);

    String getString(String key);

    String[] getStringArray(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    boolean containsKey(String key);

}
