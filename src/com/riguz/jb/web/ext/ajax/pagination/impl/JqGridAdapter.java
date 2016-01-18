package com.riguz.jb.web.ext.ajax.pagination.impl;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.config.Constants;
import com.riguz.jb.web.ext.ajax.pagination.IGridAdapter;
import com.riguz.jb.web.ext.ajax.pagination.JqGridData;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;

/**
 * examples can be found here:
 * <link>http://www.guriddo.net/demo/guriddojs/</link>
 * @author solee
 *
 */
public class JqGridAdapter implements IGridAdapter {
    static final String PARAM_PAGENUMBER = "page";
    static final String PARAM_PAGESIZE   = "rows";

    Controller          controller;
    public JqGridAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public <T> void renderJsonGrid(Page<T> page) {
        JqGridData<T> grid = new JqGridData<T>();
        grid.setRows(page.getList());
        grid.setPage(page.getPageNumber());
        grid.setRecords(page.getTotalRow());
        grid.setRows(page.getList());
        grid.setTotal(page.getTotalPage());
        controller.renderJson(grid);
    }

    @Override
    public PageParam getPageParam() {
        int pageNumber = controller.getParaToInt(PARAM_PAGENUMBER, 1);
        int pageSize = controller.getParaToInt(PARAM_PAGESIZE, Constants.DEFAULT_PAGE_SIZE);
        return new PageParam(pageNumber, pageSize);
    }

}
