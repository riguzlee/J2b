package com.riguz.j2b.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.log.Logger;
import com.riguz.j2b.ajax.ResponseFactory;
import com.riguz.j2b.config.ConfigManager;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.menu.MenuFactory;
import com.riguz.j2b.model.bean.MenuItem;
import com.riguz.j2b.model.entity.Role;
import com.riguz.j2b.model.entity.User;
import com.riguz.j2b.service.RandomPictureService;
import com.riguz.j2b.service.SecurityService;
import com.riguz.j2b.util.MailKit;
import com.riguz.j2b.validator.RandkeyValidator;

public class IndexController extends AbstractJsonController {
    private static Logger logger          = Logger.getLogger(IndexController.class.getName());
    SecurityService       securityService = new SecurityService();
    RandomPictureService  randPicService  = new RandomPictureService();

    public void index() {
        this.render("/pages/index/index.html");
    }

    public void login() {
        this.render("/pages/index/login.html");
    }

    @Before(RandkeyValidator.class)
    public void doLogin() {
        String userName = this.getPara("user");
        String passwd = this.getPara("password");
        boolean remember = this.getParaToBoolean("remember", false);
        if (this.securityService.doLoginAction(userName, passwd, remember)) {
            User u = SecurityService.getLoginUser();
            List<Role> roles = u.getRoles();
            if (roles == null || roles.size() == 0) {
                ResponseFactory.createErrorRespone(this, "用户未设置角色，请联系管理员");
                return;
            }
            Role role = roles.get(0);
            String roleIdent = role.getStr("ident");
            List<MenuItem> menu = MenuFactory.getRoleMenu(roleIdent);
            // TODO:support muti roles
            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put("roleMenu", menu);
            vars.put("loginUser", u.get("login_name"));
            BeetlRenderFactory.groupTemplate.setSharedVars(vars);
            this.setSessionAttr(DefaultSettings.SESSION_USERNAME, userName);
            ResponseFactory.createSuccessResponse(this);
        }
        else {
            ResponseFactory.createErrorRespone(this, this.securityService.getErrorMsg());
        }
    }

    public void doLogout() {
        this.securityService.doLogoutAction();
        this.setSessionAttr(DefaultSettings.SESSION_USER_OBJECT, null);
        this.setSessionAttr(DefaultSettings.SESSION_USERNAME, null);
        this.redirect("/login");
    }

    public void sendReset() {
        String randomStr = this.getPara("rand");
        String email = this.getPara("email");
        logger.info("User trying to reset his password:" + email + "/" + randomStr);
        User user = User.dao.findByEmail(email);
        if (user == null) {
            ResponseFactory.createErrorRespone(this, "系统中未找到此用户：" + email);
            return;
        }
        // 生成一个随机字符串存储到用户的activate_code中
        String code = this.securityService.createActivateCode(user);
        if (code == null) {
            ResponseFactory.createErrorRespone(this, "系统内部错误，请稍后再试");
            return;
        }
        // 发送重置密码邮件给用户
        String subject = ConfigManager.getConfig("email.reset.subject", "密码重置");
        String body = MessageFormat.format(ConfigManager.getConfig("email.reset.body", "验证码为：{0}"), code);
        MailKit.sendMail(email, subject, body);
        logger.info("Mail(Reset) sent to (" + email + "):" + body);

        ResponseFactory.createSuccessResponse(this);

    }

    public void reset() {
        this.render("/pages/index/reset.html");
    }

    @Before({ RandkeyValidator.class })
    public void doReset() {
        String token = this.getPara("token");
        String newPasswd = this.getPara("new-password");
        logger.info("Reset passwd：" + token);
        if (this.securityService.resetPasswd(token, newPasswd)) {
            ResponseFactory.createSuccessResponse(this);
        }
        else
            ResponseFactory.createErrorRespone(this, "系统错误，重置密码失败");
    }

    @Clear
    public void randpic() {
        render(this.randPicService.getRandomPictureRender());
    }

}
