package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class Img extends Entity {

    @Override
    public String getTableName() {
        return "img";
    }

    @Override
    public String getPrimaryKeyName() {
        return "img_id";
    }

}