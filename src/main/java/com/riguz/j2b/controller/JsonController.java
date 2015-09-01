package com.riguz.j2b.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.riguz.j2b.model.DataGrid;
import com.riguz.j2b.util.RecordKit;

/**
 * IE不支持contentType为application/json 可以通过修改contentType为text/html
 * 这里让所有controller都重载自此类
 * 
 * @author solever
 *
 */
public class JsonController extends Controller {
    @SuppressWarnings("unchecked")
    @Override
    public void renderJson(Object data) {
        // 重载分页数据JSON封装
        if (data instanceof Page) {
            DataGrid<Record> grid = RecordKit.recordsToDataGrid((Page<Record>) data);
            super.render(new JsonRender(grid).forIE());
        }
        else {
            super.render(new JsonRender(data).forIE());
        }

    }

    @Override
    public void renderJson(String data) {
        super.render(new JsonRender(data).forIE());
    }

    @Override
    public void renderJson(String[] data) {
        super.render(new JsonRender(data).forIE());
    }

    @Override
    public void renderJson(String key, Object value) {
        super.render(new JsonRender(key, value).forIE());
    }
}
