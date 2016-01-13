package com.riguz.jb.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class HasRoleAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            // no roles specified, so nothing to check - allow access.
            return true;
        }
        for (String role : rolesArray) {
            // 只要其中任何一个角色满足就返回true
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getName() {
        return "hasRole";
    }

}
