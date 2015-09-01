package com.riguz.j2b.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataGrid<T> implements Serializable {

    public int     total     = 0;
    public int     page;
    public String  sortName  = "";
    public String  sortOrder = "";
    public List<T> rows      = new ArrayList<T>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
