package cn.julytech.lepao.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.service.CurdService;

import cn.julytech.lepao.entity.WeixinUser;

public class WeixinUserService extends CurdService<WeixinUser> {
    public WeixinUser getUsrByOpenId(String openId) {
        return WeixinUser.dao.findFirst("select * from usr where open_id=?", openId);
    }

    public Page<WeixinUser> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT USR_ID, REAL_NAME, GENDER, OPEN_ID, HOBBY, LEPAO_NUMBER, AGE, COMPANY";
        String where = "FROM USR";
        return this.getList(WeixinUser.dao, pageNumber, pageSize, select, where, args);
    }

    public WeixinUser get(String id) {
        return this.get(WeixinUser.dao, id, "USR_ID", "REAL_NAME", "GENDER", "OPEN_ID", "HOBBY", "LEPAO_NUMBER", "AGE", "COMPANY");
    }
}
