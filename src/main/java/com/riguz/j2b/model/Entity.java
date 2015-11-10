package com.riguz.j2b.model;

import java.util.Date;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("rawtypes")
public class Entity<M extends Model> extends Model<M> {

    @Override
    public boolean update() {
        this.set("LAST_UPDATED_DATE", new Date());
        return super.update();
    }

    @Override
    public boolean save() {
        Date now = new Date();
        this.set("FROM_DATE", now);
        this.set("CREATED_DATE", now);
        return super.save();
    }

    @Override
    public boolean delete() {
        this.set("THUR_DATE", new Date());
        return this.update();
    }
}
