package com.riguz.jb.web.ext.ajax.pagination;

import java.util.ArrayList;
import java.util.List;

public class JqGridData<T> {
    int            page = 1;
    int            total   = 0;
    int            records = 0;
    List<T> rows    = new ArrayList<T>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
