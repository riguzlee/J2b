package com.riguz.j2b.model.bean;

/**
 * AJAX通用请求
 * 
 * @author solever
 *
 */
public class JsonResponse {
    String error; // 错误代码
    Object data;  // 返回数据，当错误代码不为0时返回错误描述；否则根据需要返回数据

    public JsonResponse() {
        this.error = "0";
        this.data = "";
    }

    public JsonResponse(String errorCode, String detail) {
        this.error = errorCode;
        this.data = detail;
    }

    public JsonResponse(Object data) {
        this.error = "0";
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
