package com.lsh.base.tools.model.api;

import java.util.List;

public class ApiData {

    private String desc;
    private String method;
    private int formtype;
    private boolean effective = true;
    private List<HeadParam> head;
    private List<BodyParam> body;
    private String bodystr;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getFormtype() {
        return formtype;
    }

    public void setFormtype(int formtype) {
        this.formtype = formtype;
    }

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public List<HeadParam> getHead() {
        return head;
    }

    public void setHead(List<HeadParam> head) {
        this.head = head;
    }

    public List<BodyParam> getBody() {
        return body;
    }

    public void setBody(List<BodyParam> body) {
        this.body = body;
    }

    public String getBodystr() {
        return bodystr;
    }

    public void setBodystr(String bodystr) {
        this.bodystr = bodystr;
    }

}
