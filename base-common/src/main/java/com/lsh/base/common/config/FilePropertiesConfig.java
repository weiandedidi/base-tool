package com.lsh.base.common.config;

import com.lsh.base.common.utils.ClassLoaderUtils;

public class FilePropertiesConfig extends AbsPropertiesConfig {

    public FilePropertiesConfig(String propertiesFile) {
        this.properties = ClassLoaderUtils.getProperties(propertiesFile);
    }

}
