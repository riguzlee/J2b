package com.riguz.j2b.model;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

public class Role extends Model<Role> {
    private static final long serialVersionUID = -7756555304698572L;
    private static Logger     logger           = Logger.getLogger(Role.class.getName());
    public static final Role  dao              = new Role();

    public Role() {
        super();
    }

}
