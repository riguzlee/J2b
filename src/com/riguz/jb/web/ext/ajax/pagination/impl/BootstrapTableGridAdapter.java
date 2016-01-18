package com.riguz.jb.web.ext.ajax.pagination.impl;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.config.Constants;
import com.riguz.jb.util.RecordKit;
import com.riguz.jb.web.ext.ajax.pagination.BootstrapTableDataGrid;
import com.riguz.jb.web.ext.ajax.pagination.IGridAdapter;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;

public class BootstrapTableGridAdapter implements IGridAdapter {
    static final String PARAM_OFFSET = "offset";
    static final String PARAM_PAGESIZE = "limit";
    Controller          controller;

    public BootstrapTableGridAdapter(Controller controller) {
        this.controller = controller;
    }
    @Override
    public <T> void renderJsonGrid(Page<T> page) {
        BootstrapTableDataGrid<T> grid = RecordKit.recordsToDataGrid(page);
        controller.renderJson(grid);
    }

    @Override
    public PageParam getPageParam() {
        int offset = controller.getParaToInt(PARAM_OFFSET, 0);
        int pageSize = controller.getParaToInt(PARAM_PAGESIZE, Constants.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);
        return new PageParam(pageNumber, pageSize);
    }

    int getPageNumber(int limit, int offset) {
        if (limit == 0)
            return 1;
        return offset / limit + 1;
    }
}
