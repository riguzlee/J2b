package com.riguz.j2b.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.entity.Argument;
import com.riguz.j2b.model.entity.Role;

public class RoleService extends AbstractService {
    CurdService<Role> curdService = new CurdService<Role>();

    public Page<Role> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `ROLE_ID`, `IDENT`, `NAME`, `REMARK`, `ROLE_STATUS`";
        String where = "FROM `ROLE` WHERE THRU_DATE IS NULL ";
        return this.curdService.getList(Role.dao, pageNumber, pageSize, select, where, args);
    }
}
