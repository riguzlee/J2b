package com.riguz.jb.model.ext.arg;

import java.util.ArrayList;
import java.util.List;

public class SqlAndParam {
    String       sql;
    List<Object> params;

    public SqlAndParam() {
        this.sql = "";
        this.params = new ArrayList<Object>();
    }

    public SqlAndParam(String sql, List<Object> params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
