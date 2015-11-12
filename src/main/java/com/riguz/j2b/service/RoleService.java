package com.riguz.j2b.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.model.entity.Role;

public class RoleService extends CurdService<Role> {

    public Page<Role> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `ROLE_ID` AS `ID`, `IDENT`, `NAME`, `REMARK`, `ROLE_STATUS`";
        String where = "FROM `ROLE` WHERE THRU_DATE IS NULL ";
        return this.getList(Role.dao, pageNumber, pageSize, select, where, args);
    }

    public Role get(String id) {
        return this.get(Role.dao, id, "ROLE_ID", "NAME", "IDENT", "ROLE_STATUS", "REMARK");
    }

}
