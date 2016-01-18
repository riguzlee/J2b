package com.riguz.jb.web.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Filter;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;

public class ShiroService extends CurdService<Filter> {

    public Page<Filter> getList(PageParam pageParam, Argument... args) {
        return this.getList(Role.dao,
                pageParam.getPageNumber(),
                pageParam.getPageSize(),
                SqlKit.sql("core.filterListSelect"),
                SqlKit.sql("core.filterListWhere"), args);
    }

    public Filter get(String id) {
        return this.get(Filter.dao, SqlKit.sql("core.findFilterById"), id);
    }
}
