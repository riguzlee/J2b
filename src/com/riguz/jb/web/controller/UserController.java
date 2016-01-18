package com.riguz.jb.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.core.User;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.arg.Argument.QUERY_TYPE;
import com.riguz.jb.web.ext.ajax.ResponseFactory;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;
import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;
import com.riguz.jb.web.service.RoleService;
import com.riguz.jb.web.service.UserService;
import com.riguz.jb.web.validator.IdValidator;
import com.riguz.jb.web.validator.UserValidator;

public class UserController extends AbstractJsonController {
	UserService userService = new UserService();
	RoleService roleService = new RoleService();

    public UserController() {
        super();
        this.dataGridAdapter = new JqGridAdapter(this);
    }

	public void index() {
		List<Role> roles = this.roleService.getAllRoles();
		this.setAttr("ROLES", roles);
        this.render("/html/pages/system/users.html");
	}

	public void list() {
        PageParam param = this.dataGridAdapter.getPageParam();

		String paramRole = this.getPara("s_role");
		String paramLoginName = this.getPara("s_login_name");
		String paramRealName = this.getPara("s_real_name");
        Page<User> list = this.userService.getList(param,
				new Argument("LOGIN_NAME", QUERY_TYPE.LIKE, paramLoginName),
				new Argument("REAL_NAME", QUERY_TYPE.LIKE, paramRealName),
				new Argument("IDENT", QUERY_TYPE.EQUAL, paramRole));
		this.renderJson(list);
	}

	@Before(IdValidator.class)
	public void get() {
		String id = this.getPara();
		User user = this.userService.get(id);
		ResponseFactory.renderModel(this, id, user);
	}

	@Before(UserValidator.class)
	public void add() {
		User item = this.getModel(User.class, "user");
		boolean result = this.userService.save(item);
		ResponseFactory.renderResult(this, result);
	}

	@Before({ IdValidator.class, UserValidator.class })
	public void update() {
		User item = this.getModel(User.class, "user");

		boolean result = this.userService.update(item);
		ResponseFactory.renderResult(this, result);
	}

	@Before(IdValidator.class)
	public void lock() {
		User item = User.dao.findById(this.getPara());
		item.set("ACCOUNT_STATUS", "L");
		boolean result = this.userService.update(item);
		ResponseFactory.renderResult(this, result);
	}

	@Before(IdValidator.class)
	public void unlock() {
		User item = User.dao.findById(this.getPara());
		item.set("ACCOUNT_STATUS", "N");
		boolean result = this.userService.update(item);
		ResponseFactory.renderResult(this, result);
	}

	@Before(IdValidator.class)
	public void delete() {
		boolean result = this.userService.delete(User.dao, this.getPara());
		ResponseFactory.renderResult(this, result);
	}

	public void grant() {
		String ids = this.getPara("user.USER_ID");
		String ident = this.getPara("user.ROLE_IDENT");
		String[] userIds = ids.split(",");
		List<String> idList = new ArrayList<String>();
		for (String i : userIds) {
			if (!Strings.isNullOrEmpty(i))
				idList.add(i);
		}
		boolean result = this.userService.grant(idList, ident);
		ResponseFactory.renderResult(this, result);
	}
}
