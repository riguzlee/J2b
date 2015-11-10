package com.riguz.j2b.model.entity;

import java.util.List;

import com.jfinal.log.Logger;
import com.riguz.j2b.model.Entity;

public class User extends Entity<User> {
    private static final long serialVersionUID = -7192602281278366184L;
    private static Logger     logger           = Logger.getLogger(User.class.getName());
    public static final User  dao              = new User();

    public User() {
        super();
    }

    public enum ACCOUNT_STATUS {
        NORMAL(0), LOCKED(-1), BLOCKED(1);
        int status = 0;

        ACCOUNT_STATUS(int t) {
            this.status = t;
        }

        public int getValue() {
            return this.status;
        }
    }

    public enum EMAIL_STATUS {
        DEFAULT(-1), VALIDATED(0);
        int status = 0;

        EMAIL_STATUS(int t) {
            this.status = t;
        }

        public int getValue() {
            return this.status;
        }
    }

    public List<Role> getRoles() {
        String sql = "SELECT * FROM `ROLE` WHERE `ROLE_ID` IN (SELECT `ROLE_ID` FROM `USER_TO_ROLE` WHERE `USER_ID`=?)";
        return Role.dao.find(sql, this.get("USER_ID"));
    }

    public User findByLoginName(String loginName) {
        String sql = "SELECT * FROM `USER` WHERE `THRU_DATE` IS NULL AND LOGIN_NAME=?";
        return User.dao.findFirst(sql, loginName);
    }

    public User findByEmail(String email) {
        return User.dao.findFirst("SELECT * FROM `USER` WHERE THRU_DATE IS NULL AND EMAIL = ?", email);
    }
}
