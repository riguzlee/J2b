package cn.julytech.lepao.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.controller.AbstractJsonController;
import com.riguz.j2b.validator.IdValidator;

import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.service.WeixinUserService;

public class UserController extends AbstractJsonController {
    WeixinUserService userService = new WeixinUserService();

    public void index() {
        this.render("/pages/lepao/users.html");
    }

    public void list() {
        int offset = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM, DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);
        Page<WeixinUser> list = this.userService.getList(pageNumber, pageSize);
        this.renderJson(list);
    }

    @Before(IdValidator.class)
    public void get() {
        String id = this.getPara();
        WeixinUser user = this.userService.get(id);
        ResponseFactory.renderModel(this, id, user);
    }
}
