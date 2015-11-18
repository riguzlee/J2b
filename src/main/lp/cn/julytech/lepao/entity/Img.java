package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class Img extends Entity<Img> {

    public static final Img dao = new Img();

    @Override
    public String getTableName() {
        return "img";
    }

    @Override
    public String getPrimaryKeyName() {
        return "IMG_ID";
    }

    @Override
    public boolean autoId() {
        return false;
    }

}
