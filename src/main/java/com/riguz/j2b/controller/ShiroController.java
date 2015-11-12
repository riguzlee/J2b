package com.riguz.j2b.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.entity.Shiro;
import com.riguz.j2b.service.ShiroService;
import com.riguz.j2b.validator.IdValidator;

public class ShiroController extends AbstractJsonController {
    ShiroService shiroService = new ShiroService();

    public void index() {
        this.render("/pages/system/urls.html");
    }

    public void list() {
        int offset = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM, DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);
        Page<Shiro> list = this.shiroService.getList(pageNumber, pageSize);
        this.renderJson(list);
    }

    @Before(IdValidator.class)
    public void get() {
        String id = this.getPara();
        Shiro role = this.shiroService.get(id);
        ResponseFactory.renderModel(this, id, role);
    }

    public void add() {
        Shiro item = this.getModel(Shiro.class, "url");
        boolean result = this.shiroService.save(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void update() {
        Shiro item = this.getModel(Shiro.class, "url");
        boolean result = this.shiroService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void delete() {
        boolean result = this.shiroService.delete(Shiro.dao, this.getPara());
        ResponseFactory.renderResult(this, result);
    }
}
