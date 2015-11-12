package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class User extends Entity {

    @Override
    public String getTableName() {
        return "ENTITY";
    }

    @Override
    public String getPrimaryKeyName() {
        return "USER_ID";
    }

}
