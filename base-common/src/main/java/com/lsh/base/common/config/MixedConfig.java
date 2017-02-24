package com.lsh.base.common.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MixedConfig implements ItfConfig {

    private static final Logger logger = LoggerFactory.getLogger(MixedConfig.class);

    private static final String PREFIX_FILE = "file:";
    private static final String PREFIX_CLASSPATH = "classpath:";

    private ItfConfig[] configs;

    public MixedConfig(String[] configPropertiesFiles) {
        init(configPropertiesFiles);
    }

    private void init(String[] configPropertiesFiles) {
        if (configPropertiesFiles == null || configPropertiesFiles.length == 0) {
            return;
        }
        configs = new ItfConfig[configPropertiesFiles.length];
        for (int i = 0; i < configs.length; i++) {
            logger.info("loading properties file: {}", configPropertiesFiles[i]);
            if (configPropertiesFiles[i].startsWith(PREFIX_FILE)) {
                configs[i] = new FilePropertiesConfig(StringUtils.substringAfter(configPropertiesFiles[i], PREFIX_FILE));
            } else if (configPropertiesFiles[i].startsWith(PREFIX_CLASSPATH)) {
                configs[i] = new ClassPathPropertiesConfig(StringUtils.substringAfter(configPropertiesFiles[i], PREFIX_CLASSPATH));
            }
        }
    }

    public String getString(String key) {
        for (ItfConfig configuration : configs) {
            if (configuration.containsKey(key)) {
                return configuration.getString(key);
            }
        }
        return StringUtils.EMPTY;
    }

    public String[] getStringArray(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return cfg.getStringArray(key);
            }
        }
        return null;
    }

    public int getInt(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return cfg.getInt(key);
            }
        }
        return -1;
    }

    public long getLong(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return cfg.getLong(key);
            }
        }
        return -1L;
    }

    public float getFloat(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return cfg.getFloat(key);
            }
        }
        return -1F;
    }

    public double getDouble(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return cfg.getDouble(key);
            }
        }
        return 0;
    }

    public boolean containsKey(String key) {
        for (ItfConfig cfg : configs) {
            if (cfg.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

}
