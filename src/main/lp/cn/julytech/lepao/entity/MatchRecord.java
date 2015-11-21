package cn.julytech.lepao.entity;

import com.riguz.j2b.model.Entity;

public class MatchRecord extends Entity<MatchRecord> {
    public static final MatchRecord dao = new MatchRecord();

    @Override
    public String getTableName() {
        return "match_record";
    }

    @Override
    public String getPrimaryKeyName() {
        return "MATCH_ID";
    }

    @Override
    public boolean autoId() {
        return true;
    }

}
