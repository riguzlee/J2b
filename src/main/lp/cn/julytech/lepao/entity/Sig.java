package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class Sig extends Entity<Sig> {

    public static final Sig dao = new Sig();

    @Override
    public String getTableName() {
        return "sig";
    }

    @Override
    public String getPrimaryKeyName() {
        return "SIG_ID";
    }

    @Override
    public boolean autoId() {
        return false;
    }
}
