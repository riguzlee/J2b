package com.riguz.j2b.shiro;

import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import com.jfinal.log.Logger;

public class JfinalIniWebEnvironment extends IniWebEnvironment {
    private static Logger logger = Logger.getLogger(JfinalIniWebEnvironment.class.getName());

    @Override
    public void init() {
        super.init();
        FilterChainResolver resolver = this.getFilterChainResolver();
        if (resolver != null && resolver instanceof PathMatchingFilterChainResolver) {
            PathMatchingFilterChainResolver pathResolver = (PathMatchingFilterChainResolver) resolver;
            ShiroPlugin.setFilterChainManager(pathResolver.getFilterChainManager());
            logger.info("Jfinal shiro plugin setted");
        }
        else {
            logger.warn("Shiro config ini not valid.");
        }
    }
}
