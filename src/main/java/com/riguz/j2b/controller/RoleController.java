package com.riguz.j2b.controller;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.entity.Role;
import com.riguz.j2b.service.RoleService;

public class RoleController extends AbstractJsonController {
    RoleService roleService = new RoleService();

    public void index() {
        this.render("/pages/system/roles.html");
    }

    public void list() {
        int offset = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM, DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);
        Page<Role> list = this.roleService.getList(pageNumber, pageSize);
        this.renderJson(list);
    }

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

    public void update() {
        Role item = this.getModel(Role.class, "role");
        boolean result = this.roleService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    public void delete() {
        Role item = this.getModel(Role.class, "role");
        boolean result = this.roleService.delete(item);
        ResponseFactory.renderResult(this, result);
    }
}