package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class WeixinUser extends Entity<WeixinUser> {
    public static final WeixinUser dao = new WeixinUser();

    @Override
    public String getTableName() {
        return "usr";
    }

    @Override
    public String getPrimaryKeyName() {
        return "usr_id";
    }

}
