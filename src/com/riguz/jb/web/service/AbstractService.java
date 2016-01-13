package com.riguz.jb.web.service;

public class AbstractService {
    protected String  errorMsg = "";

    protected boolean hasError = false;

    protected void setErrorMsg(String errMsg) {
        this.hasError = true;
        this.errorMsg = errMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
