package com.riguz.j2b.controller;

import com.jfinal.aop.Clear;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.log.Logger;
import com.riguz.j2b.ajax.AjaxKit;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.service.SecurityService;

public class IndexController extends JsonController {
    private static Logger logger          = Logger.getLogger(IndexController.class.getName());
    SecurityService       securityService = new SecurityService(this);

    /**
     * 系统首页
     */
    public void index() {
        this.render("/pages/index.html");
    }

    /**
     * 登录页面
     */
    public void login() {
        this.render("/pages/login.html");
    }

    /**
     * 用户登录
     */
    public void doLogin() {
        logger.info("User trying to login...");
        try {
            if (this.securityService.doLoginAction()) {
                AjaxKit.createSuccessResponse(this);
                return;
            }
        }
        catch (Exception ex) {
        }
        AjaxKit.createErrorRespone(this, "登录失败");
    }

    /**
     * 用户登出
     */
    public void doLogout() {
        this.securityService.doLogoutAction();
        this.redirect("/login");
    }

    /**
     * 获取验证码，通过RandkeyValidator验证验证码是否正确
     */
    @Clear
    public void randpic() {
        CaptchaRender img = new CaptchaRender(DefaultSettings.RANDOM_PIC_KEY);
        this.render(img);
    }

}
