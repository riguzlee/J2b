package com.riguz.jb.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.web.ext.ajax.pagination.IGridAdapter;

/**
 * IE不支持contentType为application/json 可以通过修改contentType为text/html
 * 这里让所有controller都重载自此类
 * 
 * @author solever
 *
 */
public abstract class AbstractJsonController extends Controller {
    IGridAdapter dataGridAdapter = null;

    public AbstractJsonController() {
    }

    protected void renderPage(Page<?> page) {
        this.dataGridAdapter.renderJsonGrid(page);
    }
}
