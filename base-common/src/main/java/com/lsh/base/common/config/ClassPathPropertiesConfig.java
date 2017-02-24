package com.lsh.base.common.config;


import com.lsh.base.common.utils.ClassLoaderUtils;

public class ClassPathPropertiesConfig extends AbsPropertiesConfig {

    public ClassPathPropertiesConfig(String propertiesFile) {
        this.properties = ClassLoaderUtils.getProperties(propertiesFile);
    }

}
