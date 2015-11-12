package com.riguz.j2b.model.entity;

import java.util.List;

import com.jfinal.log.Logger;
import com.riguz.j2b.model.Entity;

public class Shiro extends Entity<Shiro> {
    private static final long serialVersionUID = -7676027466630170568L;
    private static Logger     logger           = Logger.getLogger(Shiro.class.getName());
    public static final Shiro dao              = new Shiro();

    public Shiro() {
        super();
    }

    public List<Shiro> getAllFilters() {
        return Shiro.dao.find("select * from SHIRO");
    }

    @Override
    public String getTableName() {
        return "SHIRO";
    }
}
