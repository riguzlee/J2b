package com.riguz.j2b.service;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.model.bean.Argument;
import com.riguz.j2b.model.entity.User;

public class UserService extends AbstractService {
    CurdService<User> curdService = new CurdService<User>();

    public Page<User> getList(int pageNumber, int pageSize, Argument... args) {
        String select = "SELECT `USER_ID` AS `ID`, `LOGIN_NAME`, `EMAIL`, `REMARK`, `ACCOUNT_STATUS`";
        String where = "FROM `USER` WHERE THRU_DATE IS NULL ";
        return this.curdService.getList(User.dao, pageNumber, pageSize, select, where, args);
    }
}
