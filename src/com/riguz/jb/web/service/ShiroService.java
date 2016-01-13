package com.riguz.jb.web.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Filter;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;

public class ShiroService extends CurdService<Filter> {

    public Page<Filter> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `FILTER_ID` AS `ID`,`NAME`, `URL`, `RULES`, `REMARK`, `ORDER`";
        String where = "FROM `FILTER` WHERE THRU_DATE IS NULL ";
        return this.getList(Filter.dao, pageNumber, pageSize, select, where, args);
    }

    public Filter get(String id) {
        return this.get(Filter.dao, SqlKit.sql("core.findFilterById"), id);
    }
}
