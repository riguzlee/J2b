package com.riguz.jb.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;
import com.riguz.jb.util.RecordKit;
import com.riguz.jb.web.ext.ajax.DataGrid;
import com.riguz.jb.web.ext.ajax.ResponseFactory;

/**
 * IE不支持contentType为application/json 可以通过修改contentType为text/html
 * 这里让所有controller都重载自此类
 * 
 * @author solever
 *
 */
public abstract class AbstractJsonController extends Controller {
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

    /**
     * 返回非查询操作的JSON结果
     * 
     * @param success
     *            是否操作成功
     */
    public void doNoneQueryResponse(boolean success) {
        if (success)
            ResponseFactory.createSuccessResponse(this);
        else
            ResponseFactory.createErrorRespone(this, "操作失败！");
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

    public int getPageNumber(int limit, int offset) {
        if (limit == 0)
            return 1;
        return offset / limit + 1;
    }
}
