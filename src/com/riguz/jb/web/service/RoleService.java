package com.riguz.jb.web.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;

public class RoleService extends CurdService<Role> {

    public Page<Role> getList(PageParam pageParam, Argument... args) {
        return this.getList(Role.dao,
                pageParam.getPageNumber(),
                pageParam.getPageSize(),
                SqlKit.sql("core.roleListSelect"),
                SqlKit.sql("core.roleListWhere"), args);
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
