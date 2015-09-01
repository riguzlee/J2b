package com.riguz.j2b.service;

import java.util.UUID;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.riguz.j2b.config.Status;
import com.riguz.j2b.model.User;

public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class.getName());

    Controller controller; // 操作控制器

    public UserService(Controller c) {
        this.controller = c;
    }

    public boolean doAddAction() {
        User user = this.controller.getModel(User.class, "user");
        // 根据数据库表名生成一个新的ID
        long id = IdService.getInstance().getNextId("sys_user");
        user.set("id", id);
        user.set("status", Status.NOT_ENABLED.getStatus());
        // 默认密码为用户手机号码
        String tel = user.getStr("tel");
        String passwd = SecurityService.encrypt(tel);
        user.set("password", passwd);
        logger.info("Inserting sys_table:" + id);
        // 因为使用了Bcrypt，Salt就糊弄人了
        user.set("salt", UUID.randomUUID().toString());
        return user.save();
    }

    public boolean doUpdateAction() {
        User user = this.controller.getModel(User.class, "user");
        long id = this.controller.getParaToLong();
        User oldUser = User.dao.findById(id);
        if (oldUser == null) {
            logger.error("User not found:" + id);
            return false;
        }
        user.set("id", oldUser.get("id"));
        return user.update();
    }

    public User getUser(long id) {
        User user = User.dao.findFirst("select id, login_name, real_name, status, portrait, email, gender, tel, remark "
                + "from sys_user " + "where id=?", id);
        return user;
    }
}
