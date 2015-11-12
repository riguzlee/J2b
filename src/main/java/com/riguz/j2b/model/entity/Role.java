package com.riguz.j2b.model.entity;

import com.jfinal.log.Logger;
import com.riguz.j2b.model.Entity;

public class Role extends Entity<Role> {
    private static final long serialVersionUID = -7756555304698572L;
    private static Logger     logger           = Logger.getLogger(Role.class.getName());
    public static final Role  dao              = new Role();

    public Role() {
        super();
    }

    @Override
    public String getTableName() {
        return "ROLE";
    }

}
