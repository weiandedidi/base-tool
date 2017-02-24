package com.lsh.base.tools.model.api;

import java.util.List;

public class Api {

    private String name;
    private String url;
    private boolean encrypt;
    private List<ApiData> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public List<ApiData> getData() {
        return data;
    }

    public void setData(List<ApiData> data) {
        this.data = data;
    }

}
