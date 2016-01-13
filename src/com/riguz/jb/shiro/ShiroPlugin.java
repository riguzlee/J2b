package com.riguz.jb.shiro;

import java.util.List;

import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.IPlugin;

/*
 * http://jinnianshilongnian.iteye.com/blog/2018398
 */
public class ShiroPlugin implements IPlugin {
    private static Logger logger = LoggerFactory.getLogger(ShiroPlugin.class.getName());
    final Routes          routes;

    public ShiroPlugin(Routes routes) {
        this.routes = routes;
    }

    @Override
    public boolean stop() {
        return true;
    }

    @Override
    public boolean start() {
        logger.info("Shiro plugin started, please remember to update the filter chains via updateFilterChains...");
        return true;
    }

    public boolean updateFilterChains(List<UrlFilter> filters) {
        WebEnvironment env = WebUtils.getRequiredWebEnvironment(JFinal.me().getServletContext());
        FilterChainResolver resolver = env.getFilterChainResolver();
        if (resolver != null && resolver instanceof PathMatchingFilterChainResolver) {
            PathMatchingFilterChainResolver pathResolver = (PathMatchingFilterChainResolver) resolver;
            DefaultFilterChainManager chainManager = (DefaultFilterChainManager) pathResolver.getFilterChainManager();
            logger.info("Clearing shiro filter chains");
            chainManager.getFilterChains().clear();
            for (UrlFilter f : filters) {
                logger.debug("Create chain:" + f.getUrl() + "=" + f.getFilters());
                chainManager.createChain(f.getUrl(), f.getFilters());
            }
            return true;
        }
        else {
            logger.error("Unable to get filter chain manager");
        }
        return false;
    }
}
