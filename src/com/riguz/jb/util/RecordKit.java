package com.riguz.jb.util;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.jb.web.ext.ajax.pagination.BootstrapTableDataGrid;

/**
 * Record工具类
 * 
 * @author solever
 *
 */
public class RecordKit {
    /**
     * 将Record转化为List<String>
     * 
     * @param items
     *            查询结果
     * @param fieldName
     *            需要获取的字段名
     * @return 查询结果中的某字段值列表
     */
    public static List<String> recordsToStringList(List<Record> items, String fieldName) {
        List<String> result = new ArrayList<String>();
        for (Record item : items) {
            String url = item.getStr(fieldName);
            result.add(url);
        }
        return result;
    }

    /**
     * 适配分页查询结果到JSON返回值对象
     * @param <T>
     * 
     * @param page
     *            查询到的某一页
     * @return 一个DataGrid对象。返回给页面用
     */
    public static <T> BootstrapTableDataGrid<T> recordsToDataGrid(Page<T> page) {
        BootstrapTableDataGrid<T> grid = new BootstrapTableDataGrid<T>();
        grid.page = page.getPageNumber();
        grid.total = page.getTotalRow();
        grid.rows = page.getList();
        return grid;
    }
}
