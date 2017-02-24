package com.lsh.base.tools.model.api;

public class BodyParam {

    private int type;
    private String key;
    private String value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BodyParam{" +
                "type=" + type +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
