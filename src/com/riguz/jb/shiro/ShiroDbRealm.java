package com.riguz.jb.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.riguz.jb.model.core.Role;
import com.riguz.jb.model.core.User;
import com.riguz.jb.web.service.SecurityService;

public class ShiroDbRealm extends AuthorizingRealm {
    private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class.getName());

    /**
     * 认证回调函数,登录时调用.
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 查询数据库存储用户信息
        User user = User.dao.findByLoginName(token.getUsername());
        if (user == null) {
            logger.warn("User not found:" + token.getUsername());
            throw new UnknownAccountException();
        }
        String status = user.getAccountStatus();
        // 用户被锁定
        if (User.ACCOUNT_STATUS.LOCKED.equals(status))
            throw new LockedAccountException(" USER Freezed");
        // 用户未激活
        if (User.ACCOUNT_STATUS.WAITING.equals(status))
            throw new LockedAccountException(" USER not validated");
        // 认证用户密码
        if (SecurityService.checkPassword(new String(token.getPassword()), user.getPassword())) {
            logger.info("Password validated.");
        }
        else
            throw new AuthenticationException("Username/Password not match");
        // 认证成功
        return new SimpleAuthenticationInfo(user.getStr("LOGIN_NAME"), user.getStr("PASSWORD"), this.getName());
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String loginName = (String) principals.fromRealm(getName()).iterator().next();
        User user = User.dao.findByLoginName(loginName);

        if (user == null)
            return null;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<Role> roles = user.getRoles();
        for (Role r : roles)
            info.addRole(r.getStr("ident"));
        return info;
    }

    /**
     * 更新用户授权信息缓存.
     * 
     * @param principal
     */
    public void clearCachedAuthorizationInfo(String principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();

        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

}
