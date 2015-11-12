package com.riguz.j2b.model.bean;

public class Argument {
    public enum QUERY_TYPE {
        EQUAL, LIKE, NOT_EQUAL, LESS_THEN, GREATER_THEN, LESS_EQUAL, GREATER_EQUAL;

    }

    String     fieldName;
    QUERY_TYPE queryType;
    Object     param;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public QUERY_TYPE getQueryType() {
        return queryType;
    }

    public void setQueryType(QUERY_TYPE queryType) {
        this.queryType = queryType;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

}
