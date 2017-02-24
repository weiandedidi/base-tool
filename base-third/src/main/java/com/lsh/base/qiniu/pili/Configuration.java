// CHECKSTYLE:OFF

package com.lsh.base.qiniu.pili;

import com.lsh.base.qiniu.pili.common.Config;
import com.lsh.base.qiniu.pili.common.Utils;

public final class Configuration {
    boolean USE_HTTPS = Config.DEFAULT_USE_HTTPS;
    String API_HOST = Config.DEFAULT_API_HOST;
    String API_VERSION = Config.DEFAULT_API_VERSION;

    private Configuration() {
    }

    public static Configuration getInstance() {
        return ConfigurationHolder.instance;
    }

    public void setAPIHost(String apiHost) {
        if (!Utils.isArgNotEmpty(apiHost)) {
            throw new IllegalArgumentException("Illegal API Host:" + apiHost);
        }
        API_HOST = apiHost;
    }

    public void setAPIVersion(String apiVersion) {
        if (!Utils.isArgNotEmpty(apiVersion)) {
            throw new IllegalArgumentException("Illegal API Version:" + apiVersion);
        }
        API_VERSION = apiVersion;
    }

    private static class ConfigurationHolder {
        public final static Configuration instance = new Configuration();
    }

//    public void setHttpsEnabled(boolean enabled) {
//        USE_HTTPS = enabled;
//    }
}

// CHECKSTYLE:ON
