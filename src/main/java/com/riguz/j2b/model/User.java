package com.riguz.j2b.model;

import java.util.List;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> {
    private static final long serialVersionUID = -7192602281278366184L;
    private static Logger     logger           = Logger.getLogger(User.class.getName());
    public static final User  dao              = new User();

    public User() {
        super();
    }

    public User findByLoginName(String loginName) {
        return User.dao.findFirst("select * from sys_user where login_name = ?", loginName);
    }

    public List<Role> getRoles() {
        String query = "select * from sys_role where id in (select role_id from sys_role_user where user_id=?)";
        return Role.dao.find(query, this.get("id"));
    }
}
