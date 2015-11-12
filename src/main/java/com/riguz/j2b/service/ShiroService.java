package com.riguz.j2b.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.model.entity.Shiro;

public class ShiroService extends CurdService<Shiro> {

    public Page<Shiro> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `SHIRO_ID` AS `ID`,`NAME`, `URL`, `FILTERS`, `REMARK`, `ORDER`";
        String where = "FROM `SHIRO` WHERE THRU_DATE IS NULL ";
        return this.getList(Shiro.dao, pageNumber, pageSize, select, where, args);
    }

    public Shiro get(String id) {
        return this.get(Shiro.dao, id, "SHIRO_ID", "NAME", "URL", "FILTERS", "REMARK", "ORDER");
    }
}
