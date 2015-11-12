package com.riguz.j2b.controller;

import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.entity.Role;
import com.riguz.j2b.model.entity.User;
import com.riguz.j2b.service.UserService;

public class UserController extends AbstractJsonController {
    UserService userService = new UserService();

    public void index() {
        this.render("/pages/system/users.html");
    }

    public void list() {
        int offset = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM, DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);
        Page<User> list = this.userService.getList(pageNumber, pageSize);
        this.renderJson(list);
    }

    public void get() {
        String id = this.getPara();
        User user = this.userService.get(id);
        ResponseFactory.renderModel(this, id, user);
    }

    public void add() {
        User item = this.getModel(User.class, "user");
        boolean result = this.userService.save(item);
        ResponseFactory.renderResult(this, result);
    }

    public void update() {
        User item = this.getModel(User.class, "user");
        boolean result = this.userService.update(item);
        ResponseFactory.renderResult(this, result);
    }

    public void delete() {
        User item = this.getModel(User.class, "user");
        boolean result = this.userService.delete(item);
        ResponseFactory.renderResult(this, result);
    }
}
