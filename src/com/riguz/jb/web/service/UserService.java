package com.riguz.jb.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.core.User;
import com.riguz.jb.model.core.UserToRole;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.sqlinxml.SqlKit;

public class UserService extends CurdService<User> {
    private static Logger logger     = LoggerFactory.getLogger(UserService.class.getName());
    RoleService           roleSerive = new RoleService();

    public Page<User> getList(int pageNumber, int pageSize, Argument... args) {
        Page<User> users = this.getList(User.dao, pageNumber, pageSize, SqlKit.sql("system.userListSelect"),
                SqlKit.sql("system.userListWhere"), args);
        return users;
    }

    public User get(String id) {
        return this.get(User.dao, SqlKit.sql("core.getUserById"), id);
    }

	@SuppressWarnings("rawtypes")
	@Override
	public boolean save(Model item) {
        User user = (User) item;
		String passwd = SecurityService.encrypt("12345678");
        user.setPassword(passwd);
        user.setAccountStatus(User.ACCOUNT_STATUS.WAITING.toString());
        user.setEmailStatus(User.EMAIL_STATUS.WAITING.toString());
        user.setFailTotal(0);
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
        final List<UserToRole> refs = new ArrayList<UserToRole>();
        final List<UserToRole> newRefs = new ArrayList<UserToRole>();

        for (String u : users) {
            List<UserToRole> userRefs = UserToRole.dao.find(SqlKit.sql("core.getUserToRoles"), u);
            refs.addAll(userRefs);
            if (hasRole) {
                UserToRole rt = new UserToRole();
                rt.setUserId(u);
                rt.setRoleId(role.getRoleId());
                rt.setUserToRoleId(IdentityService.getNewId("USER_TO_ROLE"));
                newRefs.add(rt);
            }
        }
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                // 删除原有关联
                for (UserToRole r : refs) {
                    if (!r.delete()) {
                        logger.error("Falied to delete user_to_role:", r.getUserToRoleId());
                        return false;
                    }
                }
                for (UserToRole r : newRefs) {
                    if (!r.save()) {
                        logger.error("Falied to save user_to_role:", r.getUserToRoleId());
                        return false;
                    }
                }
                return true;
            }
        });
        return succeed;
    }
}
