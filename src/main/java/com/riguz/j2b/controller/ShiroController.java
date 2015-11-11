package com.riguz.j2b.controller;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.entity.Shiro;
import com.riguz.j2b.service.ShiroService;

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
}
