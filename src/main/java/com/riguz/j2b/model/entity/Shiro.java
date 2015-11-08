package com.riguz.j2b.model.entity;

import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

public class Shiro extends Model<Shiro> {
    private static final long serialVersionUID = -7676027466630170568L;
    private static Logger     logger           = Logger.getLogger(Shiro.class.getName());
    public static final Shiro dao              = new Shiro();

    public Shiro() {
        super();
    }

    public List<Shiro> getAllFilters() {
        return Shiro.dao.find("select * from SHIRO");
    }
}
