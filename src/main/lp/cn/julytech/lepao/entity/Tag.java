package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class Tag extends Entity {

    @Override
    public String getTableName() {
        return "tag";
    }

    @Override
    public String getPrimaryKeyName() {
        return "TAG_ID";
    }

    @Override
    public boolean autoId() {
        return true;
    }
}
