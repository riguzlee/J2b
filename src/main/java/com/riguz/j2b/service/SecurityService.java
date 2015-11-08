package com.riguz.j2b.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;

import com.google.common.base.Strings;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.bean.UrlFilter;
import com.riguz.j2b.model.entity.Shiro;
import com.riguz.j2b.model.entity.User;
import com.riguz.j2b.shiro.ShiroPlugin;
import com.riguz.j2b.util.EncryptUtil;

public class SecurityService {
    private static Logger logger = Logger.getLogger(SecurityService.class.getName());

    Controller            controller;

    public SecurityService(Controller c) {
        this.controller = c;
    }

    public boolean doLoginAction() {
        String userName = this.controller.getPara("user");
        String passwd = this.controller.getPara("password");
        logger.info("Login request:" + userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return true;
        }

        token.setRememberMe(true);
        String error = "";
        try {
            subject.login(token);
            User currentUser = User.dao.findByLoginName(userName);
            subject.getSession(true).setAttribute(DefaultSettings.SESSION_USER_OBJECT, currentUser);
            this.controller.setSessionAttr(DefaultSettings.SESSION_USERNAME, currentUser.get("login_name"));
            logger.info("User logged in:" + userName);
            return true;
        }
        catch (UnknownAccountException ue) {
            token.clear();
            error = "登录失败，您输入的账号不存在";
        }
        catch (IncorrectCredentialsException ie) {
            ie.printStackTrace();
            token.clear();
            error = "登录失败，密码不匹配";
        }
        catch (RuntimeException re) {
            re.printStackTrace();
            token.clear();
            error = "登录失败";
        }
        this.controller.setAttr("error", error);
        return false;
    }

    public boolean doLogoutAction() {
        try {

            Subject subject = SecurityUtils.getSubject();
            String userName = SecurityService.getLoginUser().getStr("login_name");
            subject.logout();
            this.controller.setSessionAttr(DefaultSettings.SESSION_USER_OBJECT, null);
            this.controller.setSessionAttr(DefaultSettings.SESSION_USERNAME, null);
            logger.info("User logout." + userName);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return null;
        Object attr = subject.getSession().getAttribute(DefaultSettings.SESSION_USER_OBJECT);
        return (User) attr;
    }

    public static boolean isPermitted(String url) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return false;
        return subject.isPermitted(url);
    }

    public static List<UrlFilter> getAllFilters() {
        List<Shiro> urls = Shiro.dao.getAllFilters();
        List<UrlFilter> filters = new ArrayList<UrlFilter>();
        for (Shiro u : urls) {
            String pattern = u.getStr("url");
            String filter = u.getStr("filters");
            if (Strings.isNullOrEmpty(pattern) || Strings.isNullOrEmpty(filter)) {
                logger.warn("Undefiened filter found");
                continue;
            }
            filters.add(new UrlFilter(pattern, filter));
        }
        return filters;
    }

    public static String encrypt(String plaintext) {
        int logRounds = PropKit.use("jfinal.properties").getInt("jbcrypt.log_rounds", 10);
        String hashed = BCrypt.hashpw(plaintext, BCrypt.gensalt(logRounds));
        return hashed;
    }

    public static boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    public static void updateFilters() {
        List<UrlFilter> filters = SecurityService.getAllFilters();
        ShiroPlugin.updateFilterChains(filters);
        logger.info("Shiro filters updated");
    }

    public String generateActiveCode(User user) {
        if (user == null)
            return null;
        UUID uuid = UUID.randomUUID();
        String activate_code = EncryptUtil.encrypt("SHA-1", uuid.toString());
        Date now = new Date();
        user.set("activate_code", activate_code);
        user.set("activate_time", now);
        if (!user.update()) {
            logger.error("Failed to save activate code ");
            return null;
        }
        logger.info("Activate code saved :" + activate_code);
        return activate_code;
    }

    public boolean resetPasswd(String token, String newPasswd) {
        /*
         * User user = User.dao.findByToken(token);
         * if (user == null)
         * return false;
         * logger.info("Reset passwd：" + token + "/" + user.getStr("id"));
         * String hash = SecurityService.encrypt(newPasswd);
         * user.set("password", hash);
         * user.set("activate_code", "");
         * user.set("activate_time", null);
         * user.set("status", 0);
         * return user.update();
         */
        return false;
    }

    public String getloginUserType() {
        return getLoginUser().get("role_ident");
    }

    public static String getloginUserType(User nowUser) {
        return nowUser.get("role_ident");
    }
}
