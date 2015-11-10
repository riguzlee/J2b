package com.riguz.j2b.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;

import com.google.common.base.Strings;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.riguz.j2b.config.ConfigManager;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.bean.UrlFilter;
import com.riguz.j2b.model.entity.Shiro;
import com.riguz.j2b.model.entity.Token;
import com.riguz.j2b.model.entity.User;
import com.riguz.j2b.shiro.ShiroPlugin;

public class SecurityService extends AbstractService {
    private static Logger logger     = Logger.getLogger(SecurityService.class.getName());

    static final int      log_rounds = ConfigManager.getConfigToInt("jbcrypt.log_rounds", 10);

    public boolean doLoginAction(String userName, String passwd, boolean rememberMe) {
        logger.info("Login request:" + userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return true;
        }
        token.setRememberMe(rememberMe);

        try {
            subject.login(token);
            User currentUser = User.dao.findByLoginName(userName);
            subject.getSession(true).setAttribute(DefaultSettings.SESSION_USER_OBJECT, currentUser);
            logger.info("User logged in:" + userName);
            return true;
        }
        catch (UnknownAccountException ue) {
            token.clear();
            this.errorMsg = "登录失败，您输入的账号不存在";
        }
        catch (IncorrectCredentialsException ie) {
            ie.printStackTrace();
            token.clear();
            this.errorMsg = "登录失败，密码不匹配";
        }
        catch (RuntimeException re) {
            re.printStackTrace();
            token.clear();
            this.errorMsg = "登录失败";
        }
        return false;
    }

    public boolean doLogoutAction() {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userName = SecurityService.getLoginUser().getStr("LOGIN_NAME");
            subject.logout();
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
            String pattern = u.getStr("URL");
            String filter = u.getStr("FILTERS");
            if (Strings.isNullOrEmpty(pattern) || Strings.isNullOrEmpty(filter)) {
                logger.warn("Undefiened filter found");
                continue;
            }
            filters.add(new UrlFilter(pattern, filter));
        }
        return filters;
    }

    public static String encrypt(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(log_rounds));
    }

    public static boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    public static void updateFilters() {
        List<UrlFilter> filters = SecurityService.getAllFilters();
        ShiroPlugin.updateFilterChains(filters);
        logger.info("Shiro filters updated");
    }

    public String createActivateCode(final User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        final Token token = new Token();
        final String code = IdentityService.getNewToken();
        token.set("TOKEN_ID", IdentityService.getNewId());
        token.set("TOKEN", code);
        token.set("TYPE", Token.TOKEN_TYPE.ACTIVATE_CODE);
        token.set("EXPIRES_IN", ConfigManager.getConfigToInt("token.expires", 36000));
        Date now = new Date();
        user.set("TOKEN_ID", token.get("TOKEN_ID"));
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return token.save() && user.update();
            }
        });
        if (succeed) {
            logger.info("Token code saved for user:" + user.getStr("LOGIN_NAME"));
            return code;
        }
        else {
            logger.error("Failed to submit");
            return null;
        }
    }

    public boolean resetPasswd(String token, String newPasswd) {
        final Token t = Token.dao.findFirst("SELECT * FROM `TOKEN` WHERE `TOKEN`=? AND `THRU_DATE` IS NULL", token);
        if (t == null)
            return false;
        final User user = User.dao.findFirst("SELECT * FROM `USER` WHERE `TOKEN_ID` = ? AND `THRU_DATE` IS NULL", t.get("TOKEN_ID"));
        if (user == null)
            return false;
        logger.info("Reset passwd：" + token + "/" + user.getStr("id"));
        String hash = SecurityService.encrypt(newPasswd);
        user.set("PASSWORD", hash);
        if (User.ACCOUNT_STATUS.BLOCKED.getValue() == user.getInt("ACCOUNT_STATUS"))
            user.set("ACCOUNT_STATUS", User.ACCOUNT_STATUS.NORMAL);
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return t.delete() && user.update();
            }
        });
        return succeed;
    }

    public String getloginUserType() {
        return getLoginUser().get("role_ident");
    }

    public static String getloginUserType(User nowUser) {
        return nowUser.get("role_ident");
    }
}
