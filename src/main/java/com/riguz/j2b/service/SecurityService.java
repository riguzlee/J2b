package com.riguz.j2b.service;

import java.util.ArrayList;
import java.util.List;

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
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.riguz.j2b.config.DefaultSettings;
import com.riguz.j2b.model.Role;
import com.riguz.j2b.model.Url;
import com.riguz.j2b.model.User;
import com.riguz.j2b.shiro.ShiroPlugin;
import com.riguz.j2b.shiro.UrlFilter;
import com.riguz.j2b.util.RecordKit;

/**
 * 系统安全相关接口
 * 
 * @author solever
 *
 */
public class SecurityService {
    private static Logger logger = Logger.getLogger(SecurityService.class.getName());

    Controller controller;

    public SecurityService(Controller c) {
        this.controller = c;
    }

    /**
     * 执行登录操作
     * 
     * @return 登录成功返回true
     */
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

    private static List<String> getCurrentUserPermissions(Role role) {
        /// FIXME:
        List<String> permissionList = new ArrayList<String>();
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return permissionList;

        String query = "select distinct uri from sys_role_url_view where ident=?";
        List<Record> res = Db.find(query, role.get("ident"));
        List<String> urlList = RecordKit.recordsToStringList(res, "uri");

        for (String url : urlList) {
            if (subject.isPermitted(url))
                permissionList.add(url);
        }

        return permissionList;
    }

    /**
     * 获取当前登录用户实例
     * 
     * @return 当前登录用户，若未登录则为null
     */
    public static User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return null;
        Object attr = subject.getSession().getAttribute(DefaultSettings.SESSION_USER_OBJECT);
        return (User) attr;
    }

    /**
     * 获取当前用户是否有权限访问某URL
     * 
     * @param url URL路径
     * @return 有权限则返回true
     */
    public static boolean isPermitted(String url) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated())
            return false;
        return subject.isPermitted(url);
    }

    /**
     * 从数据库中构建过滤器列表。 获取所有的shiro过滤器
     * 
     * @return
     */
    public static List<UrlFilter> getAllFilters() {
        // 按照chain_order进行排序，确保过滤器的顺序
        List<Url> urls = Url.dao
                .find("select url_pattern,filters from sys_url where status >=0 order by chain_order asc");
        List<UrlFilter> filters = new ArrayList<UrlFilter>();
        for (Url u : urls) {
            // 按照URL路径表达式添加过滤器
            String pattern = u.getStr("url_pattern");
            String filter = u.getStr("filters");
            if (Strings.isNullOrEmpty(pattern) || Strings.isNullOrEmpty(filter)) {
                logger.warn("Undefiened filter found");
                continue;
            }
            filters.add(new UrlFilter(pattern, filter));
        }
        return filters;
    }

    /**
     * 加密明文生成哈希秘钥，以供存储到数据库中
     * 
     * @param plaintext 明文密码
     * @return 加密后的哈希值
     */
    public static String encrypt(String plaintext) {
        int logRounds = PropKit.use("jfinal.properties").getInt("jbcrypt.log_rounds", 10);
        String hashed = BCrypt.hashpw(plaintext, BCrypt.gensalt(logRounds));
        return hashed;
    }

    /**
     * 检测密码是否正确
     * 
     * @param plaintext 明文密码
     * @param hashed 数据库存储的密码哈希值
     * @return 如果正确返回true
     */
    public static boolean checkPassword(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }

    /**
     * 更新Shiro过滤器配置
     */
    public static void updateFilters() {
        List<UrlFilter> filters = SecurityService.getAllFilters();
        ShiroPlugin.updateFilterChains(filters);
        logger.info("Shiro filters updated");
    }
}
