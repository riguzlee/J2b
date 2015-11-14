package cn.julytech.lepao.controller;

import com.google.common.base.Strings;
import com.riguz.j2b.ajax.ResponseFactory;
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
        this.setAttr("open_id", openId);
        WeixinUser usr = this.getCurrentUser();
        if (usr == null) {
            this.redirect("/lepao/license?open_id=" + openId);
            return;
        }
        this.setAttr("user", usr);
        this.render("/pages/lepao/home.html");
    }

    public void register() {
        this.keepPara();
        this.render("/pages/lepao/register.html");
    }

    public void doRegister() {
        WeixinUser user = this.getModel(WeixinUser.class, "user");
        user.set("GENDER", "on".equals(this.getPara("GENDER")) ? 1 : 0);
        if (this.usrService.doRegister(user))
            ResponseFactory.createSuccessResponse(this);
        else {
            ResponseFactory.createErrorRespone(this, this.usrService.getErrorMsg());
        }
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
        this.redirect("/lepao/register?open_id=" + this.getPara("open_id"));
    }

    private WeixinUser getCurrentUser() {
        String id = this.getPara("open_id");
        if (Strings.isNullOrEmpty(id))
            return null;
        return this.usrService.getUsrByOpenId(id);
    }
}
