package com.riguz.jb.web.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.core.User;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;

public class RoleService extends CurdService<Role> {

    public Page<Role> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `ROLE_ID` AS `ID`, `IDENT`, `NAME`, `REMARK`, `ROLE_STATUS`";
        String where = "FROM `ROLE` WHERE THRU_DATE IS NULL ";
        return this.getList(Role.dao, pageNumber, pageSize, select, where, args);
    }

    public Role get(String id) {
        return this.get(Role.dao, id, "ROLE_ID", "NAME", "IDENT", "ROLE_STATUS", "REMARK");
    }

    public List<Role> getAllRoles() {
        return Role.dao.find(SqlKit.sql("system.getAllRoles"));
    }

    public Role getRoleByIdent(String ident) {
        return Role.dao.findFirst(SqlKit.sql("system.getRoleByIdent"), ident);
    }

    public List<User> getUserByIdent(String ident) {
        String sql = "SELECT * FROM user_view WHERE IDENT=? AND (THRU_DATE IS NULL OR  THRU_DATE<NOW()) ";
        return User.dao.find(sql, ident);
    }
}
