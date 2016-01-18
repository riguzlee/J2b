package com.riguz.jb.web.controller;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.riguz.jb.config.Constants;
import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.core.User;
import com.riguz.jb.util.mail.MailKit;
import com.riguz.jb.web.ext.ajax.ResponseFactory;
import com.riguz.jb.web.ext.ajax.pagination.impl.JqGridAdapter;
import com.riguz.jb.web.ext.menu.MenuFactory;
import com.riguz.jb.web.ext.menu.MenuItem;
import com.riguz.jb.web.service.RandomPictureService;
import com.riguz.jb.web.service.SecurityService;
import com.riguz.jb.web.validator.RandkeyValidator;

public class AnonController extends AbstractJsonController {
    public AnonController() {
        super();
        this.dataGridAdapter = new JqGridAdapter(this);
    }

    private static Logger logger          = LoggerFactory.getLogger(AnonController.class.getName());
    SecurityService       securityService = new SecurityService();
    RandomPictureService  randPicService  = new RandomPictureService();

    final Prop            config          = PropKit.use("jfinal.properties");

    public void login() {
        this.render("/html/pages/index/login.html");
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
            String roleIdent = role.getIdent();
            List<MenuItem> menu = MenuFactory.getRoleMenu(roleIdent);
            // TODO:support muti roles

            this.setSessionAttr(Constants.SESSION_USERNAME, userName);
            this.setSessionAttr(Constants.SESSION_MENU, menu);
            this.setSessionAttr(Constants.SESSION_ROLE, role);
            ResponseFactory.createSuccessResponse(this);

        }
        else {
            ResponseFactory.createErrorRespone(this, this.securityService.getErrorMsg());
        }
    }

    public void doLogout() {
        this.securityService.doLogoutAction();
        this.setSessionAttr(Constants.SESSION_USER_OBJECT, null);
        this.setSessionAttr(Constants.SESSION_USERNAME, null);
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
        String subject = this.config.get("email.reset.subject", "密码重置");
        String body = MessageFormat.format(this.config.get("email.reset.body", "验证码为：{0}"), code);
        MailKit.sendMail(email, subject, body);
        logger.info("Mail(Reset) sent to (" + email + "):" + body);

        ResponseFactory.createSuccessResponse(this);

    }

    public void reset() {
        this.render("/html/pages/index/reset.html");
    }

    @Before({ RandkeyValidator.class })
    public void doReset() {
        String token = this.getPara("token");
        String newPasswd = this.getPara("new-password");
        logger.info("Reset passwd：", token);
        if (this.securityService.resetPasswd(token, newPasswd)) {
            this.render("/html/pages/index/login.html");
        }
        else
            ResponseFactory.createErrorRespone(this, "系统错误，重置密码失败");
    }

    @Clear
    public void randpic() {
        render(this.randPicService.getRandomPictureRender());
    }

}
