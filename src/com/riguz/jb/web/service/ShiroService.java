package com.riguz.jb.web.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.platform.model.bean.Argument;
import com.riguz.j2b.platform.service.CurdService;
import com.riguz.j2b.system.entity.Filter;

public class ShiroService extends CurdService<Filter> {

    public Page<Filter> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `FILTER_ID` AS `ID`,`NAME`, `URL`, `RULES`, `REMARK`, `ORDER`";
        String where = "FROM `FILTER` WHERE THRU_DATE IS NULL ";
        return this.getList(Filter.dao, pageNumber, pageSize, select, where, args);
    }

    public Filter get(String id) {
        return this.get(Filter.dao, id, "FILTER_ID", "NAME", "URL", "RULES", "REMARK", "ORDER");
    }
}
