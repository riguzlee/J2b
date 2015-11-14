package cn.julytech.lepao.controller;

import com.google.common.base.Strings;
import com.riguz.j2b.controller.AbstractJsonController;

import cn.julytech.lepao.entity.WeixinUser;
import cn.julytech.lepao.service.WeixinUserService;

public class LepaoController extends AbstractJsonController {
    WeixinUserService usrService = new WeixinUserService();

    public void home() {
        String code = this.getPara("code");
        String openId = this.getPara("open_id");
        if (Strings.isNullOrEmpty(code) && Strings.isNullOrEmpty(openId)) {
            this.renderText("无法获取用户授权。请在微信中打开此网页");
            return;
        }
        // FIXME:
        this.setAttr("id", openId);
        WeixinUser usr = this.getCurrentUser();
        if (usr == null) {
            this.license();
            return;
        }
        this.render("/pages/lepao/home.html");
    }

    public void register() {
        this.render("/pages/lepao/register.html");
    }

    public void sign() {

    }

    public void share() {

    }

    public void match() {
        this.render("/pages/lepao/match.html");
    }

    public void zone() {
        this.render("/pages/lepao/zone.html");
    }

    public void portrait() {
        this.render("/pages/lepao/portrait.html");
    }

    public void license() {
        this.keepPara();
        this.render("/pages/lepao/license.html");
    }

    public void doAcceptLicense() {
        this.register();
    }

    private WeixinUser getCurrentUser() {
        String id = this.getPara("id");
        if (Strings.isNullOrEmpty(id))
            return null;
        return this.usrService.getUsrByOpenId(id);
    }
}
