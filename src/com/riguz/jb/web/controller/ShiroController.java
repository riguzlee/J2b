package com.riguz.jb.web.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.jb.model.core.Filter;
import com.riguz.jb.web.ext.ajax.ResponseFactory;
import com.riguz.jb.web.ext.ajax.pagination.PageParam;
import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;
import com.riguz.jb.web.service.ShiroService;
import com.riguz.jb.web.validator.IdValidator;

public class ShiroController extends AbstractJsonController {
    ShiroService shiroService = new ShiroService();

    public ShiroController() {
        super();
        this.dataGridAdapter = new JqGridAdapter(this);
    }

    public void index() {
        this.render("/html/pages/system/urls.html");
    }

    public void list() {
        PageParam param = this.dataGridAdapter.getPageParam();
        Page<Filter> list = this.shiroService.getList(param);
        this.renderJson(list);
    }

    @Before(IdValidator.class)
    public void get() {
        String id = this.getPara();
        Filter role = this.shiroService.get(id);
        ResponseFactory.renderModel(this, id, role);
    }

    public void add() {
        Filter item = this.getModel(Filter.class, "url");
        boolean result = this.shiroService.save(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void update() {
        Filter item = this.getModel(Filter.class, "url");
        boolean result = this.shiroService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void delete() {
        boolean result = this.shiroService.delete(Filter.dao, this.getPara());
        ResponseFactory.renderResult(this, result);
    }
}
