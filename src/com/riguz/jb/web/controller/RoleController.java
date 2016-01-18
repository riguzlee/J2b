package com.riguz.jb.web.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.web.ext.ajax.ResponseFactory;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;
import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;
import com.riguz.jb.web.service.RoleService;
import com.riguz.jb.web.validator.IdValidator;

public class RoleController extends AbstractJsonController {
    RoleService roleService = new RoleService();

    public RoleController() {
        super();
        this.dataGridAdapter = new JqGridAdapter(this);
    }

    public void index() {
        this.render("/html/pages/system/roles.html");
    }

    public void list() {
        PageParam param = this.dataGridAdapter.getPageParam();
        Page<Role> list = this.roleService.getList(param);
        this.renderPage(list);
    }

    @Before(IdValidator.class)
    public void get() {
        String id = this.getPara();
        Role role = this.roleService.get(id);
        ResponseFactory.renderModel(this, id, role);
    }

    public void add() {
        Role item = this.getModel(Role.class, "role");
        boolean result = this.roleService.save(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void update() {
        Role item = this.getModel(Role.class, "role");
        boolean result = this.roleService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void delete() {
        boolean result = this.roleService.delete(Role.dao, this.getPara());
        ResponseFactory.renderResult(this, result);
    }
}
