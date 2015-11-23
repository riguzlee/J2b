package cn.julytech.lepao.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.controller.AbstractJsonController;
import com.riguz.j2b.validator.IdValidator;

import cn.julytech.lepao.entity.Img;
import cn.julytech.lepao.service.ImgService;

public class ShareController extends AbstractJsonController {
    ImgService imgService = new ImgService();

    public void index() {
        this.render("/pages/lepao/shares.html");
    }

    public void list() {
        int offset = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_NUM_PARAM, 0);
        int pageSize = this.getParaToInt(DefaultSettings.DEFAULT_PAGE_SIZE_PARAM, DefaultSettings.DEFAULT_PAGE_SIZE);
        int pageNumber = this.getPageNumber(pageSize, offset);

        Page<Img> list = this.imgService.getList(pageNumber, pageSize);
        this.renderJson(list);
    }

    @Before(IdValidator.class)
    public void pass() {
        boolean result = this.imgService.pass(this.getPara(), true);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void reject() {
        boolean result = this.imgService.pass(this.getPara(), false);
        ResponseFactory.renderResult(this, result);
    }

    @Before(IdValidator.class)
    public void delete() {
        boolean result = this.imgService.delete(Img.dao, this.getPara());
        ResponseFactory.renderResult(this, result);
    }

}
