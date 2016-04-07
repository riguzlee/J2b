package com.riguz.jb.web.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.ext.arg.Argument;
import com.riguz.jb.model.ext.arg.Argument.QUERY_TYPE;
import com.riguz.jb.web.ext.ajax.ResponseFactory;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;
import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;
import com.riguz.jb.web.service.IdentityService;
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
        String paramSearch = this.getPara("search");
        String sidx = this.getPara("sidx", "NAME");
        String sord = this.getPara("sord", "asc");
        Argument orderArg = new Argument(sidx, QUERY_TYPE.ORDERBY, sord);
        
        Page<Role> list = this.roleService.getList(param, new Argument("NAME", QUERY_TYPE.LIKE, paramSearch), orderArg);
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
        String id = IdentityService.getNewId("ROLE");
        item.setRoleId(id);
        boolean result = this.roleService.save(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void update() {
        Role item = this.getModel(Role.class, "role");
        boolean result = this.roleService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    public void delete() {
        boolean result = this.roleService.delete(Role.dao, this.getPara("ids"));
        ResponseFactory.renderResult(this, result);
    }
}
