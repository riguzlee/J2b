package com.riguz.jb.model.ext.arg;

public class Argument {
    public enum QUERY_TYPE {
        EQUAL("="), 
        LIKE("LIKE"),
        NOT_EQUAL("<>"),
        LESS_THEN("<"),
        GREATER_THEN(">"),
        LESS_EQUAL("<="),
        GREATER_EQUAL(">="),
        ORDERBY("orderby");

        final String queryType;

        private QUERY_TYPE(String queryType) {
            this.queryType = queryType;
        }
    }

    String     fieldName;
    QUERY_TYPE queryType;
    Object     param;

    public Argument(String filedName, QUERY_TYPE queryType, Object param) {
        this.fieldName = filedName;
        this.queryType = queryType;
        this.param = param;
        
        if(queryType == QUERY_TYPE.LIKE
                && param != null
                && param instanceof String){
            String likeStr = (String)param;
            if(!likeStr.startsWith("%"))
                likeStr = "%" + likeStr;
            if(!likeStr.endsWith("%"))
                likeStr = likeStr + "%";
            this.param = likeStr;
        }
    }

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
