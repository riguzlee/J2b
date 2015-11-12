package com.riguz.j2b.service;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.model.entity.User;

public class UserService extends CurdService<User> {

    public Page<User> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `USER_ID` AS `ID`, `LOGIN_NAME`, `EMAIL`, `REMARK`, `ACCOUNT_STATUS`";
        String where = "FROM `USER` WHERE THRU_DATE IS NULL ";
        return this.getList(User.dao, pageNumber, pageSize, select, where, args);
    }

    public User get(String id) {
        return this.get(User.dao, id, "USER_ID", "LOGIN_NAME", "EMAIL", "ACCOUNT_STATUS", "REMARK");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean save(Model item) {
        String passwd = SecurityService.encrypt("12345678");
        item.set("PASSWORD", passwd);
        item.set("ACCOUNT_STATUS", User.ACCOUNT_STATUS.BLOCKED.getValue());
        item.set("EMAIL_STATUS", User.EMAIL_STATUS.DEFAULT.getValue());
        item.set("FAIL_TOTAL", 0);
        return super.save(item);
    }
}
