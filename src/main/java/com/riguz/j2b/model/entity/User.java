package com.riguz.j2b.model.entity;

import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> {
    private static final long  serialVersionUID = -7192602281278366184L;
    private static Logger      logger           = Logger.getLogger(User.class.getName());
    public static final User   dao              = new User();
    public static final String ME_TABLE_NAME    = "sys_user";

    public User() {
        super();
    }

    /**
     * 获取当前用户角色列表
     * 
     * @return
     */
    public List<Role> getRoles() {
        String query = "select * from `ROLE` where ROLE_ID in (select ROLE_ID from USER_TO_ROLE where USER_ID=?)";
        return Role.dao.find(query, this.get("USER_ID"));
    }

    public User findByLoginName(String loginName) {
        return User.dao.findFirst("select * from `USER` where THRU_DATE is null and LOGIN_NAME = ?", loginName);
    }

    public User findByEmail(String email) {
        return User.dao.findFirst("select * from `USER` where THRU_DATE is null and EMAIL = ?", email);
    }
}
