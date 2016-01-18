package com.riguz.jb.web.ext.ajax.pagination;

import com.jfinal.plugin.activerecord.Page;

public interface IGridAdapter {
    public <T> void renderJsonGrid(Page<T> page);

    public PageParam getPageParam();
}
