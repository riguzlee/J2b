package com.riguz.jb.web.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.riguz.jb.config.Constants;
import com.riguz.jb.model.core.Filter;
import com.riguz.jb.model.core.Token;
import com.riguz.jb.model.core.User;
import com.riguz.jb.shiro.ShiroPlugin;
import com.riguz.jb.shiro.UrlFilter;

public class SecurityService extends AbstractService {
    private static Logger logger     = LoggerFactory.getLogger(SecurityService.class.getName());

    static final Prop     config     = PropKit.use("jfinal.properties");
    static final int      log_rounds = config.getInt("jbcrypt.log_rounds", 10);

    public boolean doLoginAction(String userName, String passwd, boolean rememberMe) {
        logger.info("Login request:", userName);
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return true;
        }
        token.setRememberMe(rememberMe);

        try {
            subject.login(token);
            User currentUser = User.dao.findByLoginName(userName);
            subject.getSession(true).setAttribute(Constants.SESSION_USER_OBJECT, currentUser);
            logger.info("User logged in:", userName);
            return true;
        }
        catch (UnknownAccountException ue) {
            token.clear();
            this.setErrorMsg("登录失败，您输入的账号不存在");
        }
        catch (IncorrectCredentialsException ie) {
            ie.printStackTrace();
            token.clear();
            this.setErrorMsg("登录失败，密码不匹配");
        }
        catch (LockedAccountException le) {
            le.printStackTrace();
            token.clear();
            this.setErrorMsg("用户被锁定，请联系管理员");
        }
        catch (DisabledAccountException de) {
            de.printStackTrace();
            token.clear();
            this.setErrorMsg("用户未激活，请重置密码以激活用户");
        }
        catch (RuntimeException re) {
            re.printStackTrace();
            token.clear();
            this.setErrorMsg("登录失败");
        }
        return false;
    }

    public boolean doLogoutAction() {
        try {
            Subject subject = SecurityUtils.getSubject();
            String userName = SecurityService.getLoginUser().getLoginName();
            subject.logout();
            logger.info("User logout.", userName);
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
        Object attr = subject.getSession().getAttribute(Constants.SESSION_USER_OBJECT);
        return (User) attr;
    }

    public static boolean isPermitted(String url) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return false;
        return subject.isPermitted(url);
    }

    public static List<UrlFilter> getAllFilters() {
        List<Filter> urls = Filter.dao.getAllFilters();
        List<UrlFilter> filters = new ArrayList<UrlFilter>();
        for (Filter u : urls) {
            String pattern = u.getUrl();
            String filter = u.getRules();
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
        Object shiroObject = JFinal.me().getServletContext().getAttribute(Constants.SESSION_SHIRO_PLUGIN);
        if (shiroObject == null) {
            logger.error("Failed to get session shiro plugin");
            return;
        }
        ShiroPlugin shiroPlguin = (ShiroPlugin) shiroObject;
        shiroPlguin.updateFilterChains(filters);
        logger.info("Shiro filters updated");
    }

    public String createActivateCode(final User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        final Token token = new Token();
        final String code = IdentityService.getNewToken();
        token.setTokenId(IdentityService.getNewId(Token.dao.getTableName()));
        token.setToken(code);
        token.setTokenType(Token.TOKEN_TYPE.ACTIVATE_CODE.toString());
        token.setExpiresIn(config.getInt("token.expires", 36000));
        user.setTokenId(token.getTokenId());
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return token.save() && user.update();
            }
        });
        if (succeed) {
            logger.info("Token code saved for user:", code, user.getLoginName());
            return code;
        }
        else {
            logger.error("Failed to submit");
        }
        return null;
    }

    public boolean resetPasswd(String token, String newPasswd) {
        logger.info("Trying to reset user passwd:token=", token);
        final Token t = Token.dao.findToken(token);
        if (t == null) {
            logger.error("Token not found");
            return false;
        }
        final User user = User.dao.findByTokenId(t.getTokenId());
        if (user == null) {
            logger.error("User not found");
            return false;
        }
        logger.info("Reset passwd：" + token + "/", user.getUserId());
        String hash = SecurityService.encrypt(newPasswd);
        user.setPassword(hash);
        if (User.ACCOUNT_STATUS.LOCKED.equals(user.getAccountStatus()))
            user.setAccountStatus(User.ACCOUNT_STATUS.NORMAL.toString());
        boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                return t.delete() && user.update();
            }
        });
        return succeed;
    }
}
