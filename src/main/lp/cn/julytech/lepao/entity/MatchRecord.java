package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class MatchRecord extends Entity {

    @Override
    public String getTableName() {
        return "match_record";
    }

    @Override
    public String getPrimaryKeyName() {
        return "usr_id";
    }

}
