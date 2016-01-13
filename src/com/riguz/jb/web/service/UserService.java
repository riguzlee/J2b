package com.riguz.jb.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.j2b.platform.jfinal.plugin.sqlinxml.SqlKit;
import com.riguz.j2b.platform.model.bean.Argument;
import com.riguz.j2b.platform.service.CurdService;
import com.riguz.j2b.platform.service.IdentityService;
import com.riguz.j2b.system.entity.Role;
import com.riguz.j2b.system.entity.User;

public class UserService extends CurdService<User> {
    private static Logger logger     = Logger.getLogger(UserService.class.getName());
    RoleService           roleSerive = new RoleService();

    public Page<User> getList(int pageNumber, int pageSize, Argument... args) {
        Page<User> users = this.getList(User.dao, pageNumber, pageSize, SqlKit.sql("system.userListSelect"),
                SqlKit.sql("system.userListWhere"), args);
        return users;
    }

    public User get(String id) {
        return this.get(User.dao, id, "USER_ID", "LOGIN_NAME", "REAL_NAME", "EMAIL", "ACCOUNT_STATUS", "REMARK");
    }

	@SuppressWarnings("rawtypes")
	@Override
	public boolean save(Model item) {
		String passwd = SecurityService.encrypt("12345678");
		item.set("PASSWORD", passwd);
		item.set("ACCOUNT_STATUS", User.ACCOUNT_STATUS.WAITING);
		item.set("EMAIL_STATUS", User.EMAIL_STATUS.WAITING);
		item.set("FAIL_TOTAL", 0);
		return super.save(item);
	}

    public boolean grant(final List<String> users, String roleIdent) {
        boolean hasRole = true;
        if (Strings.isNullOrEmpty(roleIdent))
            hasRole = false;

        Role role = this.roleSerive.getRoleByIdent(roleIdent);
        if (hasRole && role == null) {
            logger.error("Role not found:" + roleIdent);
            return false;
        }
        final List<Record> refs = new ArrayList<Record>();
        final List<Record> newRefs = new ArrayList<Record>();

        for (String u : users) {
            List<Record> userRefs = Db.use("j2b").find(SqlKit.sql("system.getUserToRoles"), u);
            refs.addAll(userRefs);
            if (hasRole) {
                Record rt = new Record();
                rt.set("USER_ID", u);
                rt.set("ROLE_ID", role.getPrimaryKey());
                rt.set("USER_TO_ROLE_ID", IdentityService.getNewId("USER_TO_ROLE"));
                newRefs.add(rt);
            }
        }
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                boolean success = true;
                // 删除原有关联
                for (Record r : refs) {
                    if (!Db.use("j2b").delete("USER_TO_ROLE", "USER_TO_ROLE_ID", r)) {
                        logger.error("Falied to delete user_to_role" + r.getStr("USER_TO_ROLE_ID"));
                        return false;
                    }
                }
                for (Record r : newRefs) {
                    if (!Db.use("j2b").save("USER_TO_ROLE", "USER_TO_ROLE_ID", r)) {
                        logger.error("Falied to save user_to_role" + r.getStr("USER_TO_ROLE_ID"));
                        return false;
                    }
                }
                return true;
            }
        });
        return succeed;
    }
}
