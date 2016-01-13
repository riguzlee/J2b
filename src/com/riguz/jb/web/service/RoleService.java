package com.riguz.jb.web.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;

public class RoleService extends CurdService<Role> {

    public Page<Role> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `ROLE_ID` AS `ID`, `IDENT`, `NAME`, `REMARK`, `ROLE_STATUS`";
        String where = "FROM `ROLE` WHERE THRU_DATE IS NULL ";
        return this.getList(Role.dao, pageNumber, pageSize, select, where, args);
    }

    public Role get(String id) {
        return this.get(Role.dao, SqlKit.sql("core.getRoleById"), id);
    }

    public List<Role> getAllRoles() {
        return Role.dao.find(SqlKit.sql("core.getAllRoles"));
    }

    public Role getRoleByIdent(String ident) {
        return Role.dao.findFirst(SqlKit.sql("core.getRoleByIdent"), ident);
    }
}
